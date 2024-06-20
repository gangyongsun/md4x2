package cn.com.goldwind.md4x.business.service.zeppelin;

import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelin;
import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinImage;
import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinLogs;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.ecs.AmazonECS;
import com.amazonaws.services.ecs.AmazonECSClientBuilder;
import com.amazonaws.services.ecs.model.*;
import com.amazonaws.services.elasticloadbalancingv2.AmazonElasticLoadBalancing;
import com.amazonaws.services.elasticloadbalancingv2.AmazonElasticLoadBalancingClientBuilder;
import com.amazonaws.services.elasticloadbalancingv2.model.LoadBalancer;
import com.amazonaws.services.elasticloadbalancingv2.model.*;
import com.amazonaws.util.StringUtils;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


/**
 * AWS ECS 服务封装
 * @author wangguiyu
 *
 */
@Service
public class AwsEcsService extends BaseClient implements InitializingBean{
	private static final Logger log = LoggerFactory.getLogger(AwsEcsService.class);
	
	private AmazonECS amazonECS;
	private AmazonElasticLoadBalancing elbClient;

//	task pending:PROVISIONING、PENDING、ACTIVATING
//	task running:RUNNING
//	task stopping:DEACTIVATING、STOPPING、DEPROVISIONING
//	task stopped:STOPPED
//
//	health pending:initial
//	health running:healthy
//	health stoppiing:draining
//	health stopped:404
//	health other:unhealthy、unused、unavailable
	private List<String> unhealthStatus = new ArrayList<>(Arrays.asList("unhealthy","unused","unavailable"));

	@Value("${aws.credentialFlag}")
	private String credentialFlag;
	
	@Value("${aws.ak}")
	private String accessKey;

	@Value("${aws.sk}")
	private String secretKey;
	
	@Value("${aws.region}")
	private String region;
	
	@Value("${aws.ecs.taskRoleArn}")
	private String taskRoleArn;
	
	@Value("${aws.ecs.taskExecutionRoleArn}")
	private String taskExecutionRoleArn;
	
	@Value("${aws.ecs.cookieDomain}")
	private String cookieDomain;
	
	@Value("${aws.ecs.ecsClusterName}")
	private String ecsClusterName;
	
	@Value("${aws.ecs.taskSubnetId1}")
	private String taskSubnetId1;
	
	@Value("${aws.ecs.taskSubnetId2}")
	private String taskSubnetId2;
	
	@Value("${aws.ecs.taskSecurityGroupId}")
	private String taskSecurityGroupId;
	
	@Value("${aws.ecs.serviceLoadbalancerArn}")
	private String serviceLoadbalancerArn;
	
	@Value("${aws.ecs.serviceLoadbalancerListenerArn}")
	private String serviceLoadbalancerListenerArn;
	
	@Value("${zeppelin.zeppelinNotebookS3Bucket}")
	private String zeppelinNotebookS3Bucket;
	
	@Value("${zeppelin.zeppelinNotebookS3Endpoint}")
	private String zeppelinNotebookS3Endpoint;

	@Autowired
	private ZeppelinInstanceService zeppelinService;

	@Autowired
	private ZeppelinLogsService zeppelinLogsService;

	@Autowired
	private ZeppelinImageService zeppelinImageService;

	@Override
	public void afterPropertiesSet() throws Exception {
		if ("true".equals(credentialFlag)) {
			if(!StringUtils.isNullOrEmpty(accessKey) && !StringUtils.isNullOrEmpty(secretKey)){
				AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
				amazonECS = AmazonECSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(region).build();
				elbClient = AmazonElasticLoadBalancingClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(region).build();
			}else{
				amazonECS = AmazonECSClientBuilder.standard().withRegion(region).build();
				elbClient = AmazonElasticLoadBalancingClientBuilder.standard().withRegion(region).build();
			}
		} else {
			amazonECS = AmazonECSClientBuilder.standard().withRegion(region).withCredentials(new InstanceProfileCredentialsProvider(false)).build();
			elbClient = AmazonElasticLoadBalancingClientBuilder.standard().withRegion(region).withCredentials(new InstanceProfileCredentialsProvider(false)).build();
		}
	}
	
	/*
	 * @Author yaleiwang
	 * @Description ECS任务定义
	 * @Date 2020-7-21 17:06
	 * @param zeppelinInstanceID
	 * @param userName OA
	 * @param taskFlavor 任务实例大小 C1M4、C2M8、C4M24
	 * @param imageUri
	 * @return java.lang.String
	 **/
	public String prepareZeppelinInstanceTaskDefinition(SysZeppelin zeppelinEntity) {
		String zeppelinInstanceID = zeppelinEntity.getInstanceName();
		String userName = zeppelinEntity.getUserName();
		String taskFlavor = zeppelinEntity.getTaskFlavor();
		SysZeppelinImage image = zeppelinImageService.getImagesByPrimaryKey(zeppelinEntity.getImageId());
		String imageUri = image.getImageUri();

		Preconditions.checkNotNull(amazonECS);
		Preconditions.checkState(!StringUtils.isNullOrEmpty(zeppelinInstanceID));
		ZeppelinTaskDefinitionClient taskDefinitionClient = new ZeppelinTaskDefinitionClient(amazonECS);
		boolean exists = taskDefinitionClient.taskDefinitionExists(zeppelinInstanceID);
		if (exists) { // deregister whole task definition family first
			log.warn(String.format("the task definition for the zeppeline instance \"%s\" exists, will deregister the family first.",zeppelinInstanceID));
			try {
				taskDefinitionClient.deregisterTaskDefinition(zeppelinInstanceID);
				log.warn(String.format("the task definition for the zeppeline instance \"%s\" has been de-registered.",zeppelinInstanceID));
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
		ZeppelinInstanceFlavor.Flavor flavor = chooseFlavor(taskFlavor);
		String userPwd = UUID.randomUUID().toString();
		zeppelinEntity.setUserPwd(userPwd);
		String[] strs = serviceLoadbalancerArn.split("/");
		zeppelinEntity.setElbName(strs[strs.length-2]);
		zeppelinEntity.setElbArn(serviceLoadbalancerArn);
		zeppelinEntity.setElbListenerArn(serviceLoadbalancerListenerArn);
		zeppelinEntity.setClusterName(ecsClusterName);
		zeppelinEntity.setTaskRoleArn(taskRoleArn);

		//设置bitBucket信息
		zeppelinEntity.setBitbucketUrl("http://internal-prod-atlassian-bitbucket-659803850.cn-north-1.elb.amazonaws.com.cn/scm/md4x2_0/dt_md4x_zeppelin_notebooks.git");
		zeppelinEntity.setBitbucketName("loadportal");
		zeppelinEntity.setBitbucketPwd("loadportal.,a");
		
		//设置notebook s3和s3 endpoint
		zeppelinEntity.setZeppelinNotebookS3Bucket(zeppelinNotebookS3Bucket);
		zeppelinEntity.setZeppelinNotebookS3Endpoint(zeppelinNotebookS3Endpoint);

		try {
			return taskDefinitionClient.registerTaskDefinition(zeppelinEntity,flavor, imageUri,cookieDomain);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * @Author yaleiwang
	 * @Description choose zeppelin flavor task size C1M4，C2M8，C4M24
	 * @Date 2020-7-22 10:48
	 * @param flavor
	 * @return
	 **/
	private ZeppelinInstanceFlavor.Flavor chooseFlavor(String flavor){
		switch (flavor){
			case "C1M4":
				return ZeppelinInstanceFlavor.C1M4;
			case "C2M8":
				return ZeppelinInstanceFlavor.C2M8;
			case "C4M24":
				return ZeppelinInstanceFlavor.C4M24;
			case "C10M24":
				return ZeppelinInstanceFlavor.C10M24;
			default:
				return ZeppelinInstanceFlavor.C1M4;
		}
	}

	/*
	 * @Author yaleiwang
	 * @Description 判断service是否存在
	 * @Date 2020-7-22 22:47
	 * @param zeppelinInstanceID instanceID
	 * @param clusterName 集群名称
	 * @return boolean
	 **/
	public boolean zeppelinServiceExists(String zeppelinInstanceID){
		String userName = zeppelinInstanceID.split("-")[2];

		SysZeppelin zeppelinEntity = zeppelinService.selectByUserName(userName);

		if(zeppelinEntity == null){
			return false;
		}

		//旧逻辑，从ECS中获取所有的服务名称做判断，此方法查询速度慢
		try{
			String serviceArn = getServiceArn(zeppelinInstanceID, zeppelinEntity.getClusterName());
			ListServicesRequest servicesRequest = new ListServicesRequest();
			servicesRequest.setCluster(zeppelinEntity.getClusterName());
			servicesRequest.setMaxResults(100);
			ListServicesResult result = amazonECS.listServices(servicesRequest);
			List<String> listService = result.getServiceArns();
			if(listService.contains(serviceArn)){
				return true;
			}

			while (result.getNextToken()!=null){
				servicesRequest.setNextToken(result.getNextToken());
				servicesRequest.setMaxResults(100);
				result = amazonECS.listServices(servicesRequest);
				listService = result.getServiceArns();

				if(listService.contains(serviceArn)){
					return true;
				}
			}

			return false;
		}catch (Exception ex){
			log.error(ex.getMessage());
			throw ex;
		}
	}

	/*
	 * @Author yaleiwang
	 * @Description 判断用户是否可以创建实例
	 * @Date 2020-8-19 22:04
	 * @param userName
	 * @return boolean
	 **/
	public boolean checkAvailable(String userName){
		SysZeppelin zeppelinEntity = zeppelinService.selectByUserName(userName);

		if(zeppelinEntity == null){
			return true;
		}

		String instanceStatus = zeppelinEntity.getInstanceStatus();
		if(instanceStatus.equals("DELETED")){
			return true;
		}else {
			return false;
		}
	}

	/*
	 * @Author yaleiwang
	 * @Description 删除服务
	 * @Date 2020-7-22 23:47
	 * @param zeppelinInstanceID zeppelinInstanceID
	 * @return boolean
	 **/
	public boolean deleteService(String zeppelinInstanceID) throws Exception {
		String userName = zeppelinInstanceID.split("-")[2];

		//1.获取 Arns
		SysZeppelin zeppelin = zeppelinService.selectArnByUserName(userName);

		String serviceArn = getServiceArn(zeppelinInstanceID,zeppelin.getClusterName());

		if (zeppelin != null) {
			//1. 删除elb 规则
			DeleteRuleRequest deleteRuleRequest = new DeleteRuleRequest();
			deleteRuleRequest.setRuleArn(zeppelin.getRuleArn());
			elbClient.deleteRule(deleteRuleRequest);

			//2. 删除elb 目标组
			DeleteTargetGroupRequest deleteTargetGroupRequest = new DeleteTargetGroupRequest();
			deleteTargetGroupRequest.setTargetGroupArn(zeppelin.getTargetGroupArn());
			elbClient.deleteTargetGroup(deleteTargetGroupRequest);

			//3. 删除service
			DeleteServiceRequest delRequest = new DeleteServiceRequest();
			delRequest.setCluster(zeppelin.getClusterName());
			delRequest.setService(serviceArn);
			delRequest.setForce(true);
			DeleteServiceResult delResult = amazonECS.deleteService(delRequest);

			//更新zeppelin instance
			SysZeppelin zeppelinEntity = new SysZeppelin();
			zeppelinEntity.setUserName(userName);
			zeppelinEntity.setUpdateTime(new Date());
			zeppelinEntity.setInstanceStatus("STOPPING");
			zeppelinService.updateByUserName(zeppelinEntity);

			//异步定时器更新状态
			updateStatusFromTimer(zeppelinInstanceID);

			return true;
		}else{
			throw new Exception(String.format("%s not exists zeppelin record!",userName));
		}
	}

	/*
	 * @Author yaleiwang
	 * @Description 获取task状态
	 * @Date 2020-7-22 23:47
	 * @param zeppelinInstanceID
	 * @return java.lang.String
	 **/

	/*
	task pending:PROVISIONING、PENDING、ACTIVATING
	task running:RUNNING
	task stopping:DEACTIVATING、STOPPING、DEPROVISIONING
	task stopped:STOPPED

	health pending:initial
	health running:healthy
	health stoppiing:draining
	health stopped:404
	health other:unhealthy、unused、unavailable

	说明：
	创建过程：首先创建task、然后创建targetGroup
	删除过程：依次删除rule、targetGroup、service
	状态查询过程：
	 */
	public String getTaskStatus(String zeppelinInstanceID) throws Exception {
		String userName = zeppelinInstanceID.split("-")[2];

		//1.Arns
		SysZeppelin zeppelinEntity = zeppelinService.selectByUserName(userName);

		String serviceArn = getServiceArn(zeppelinInstanceID,zeppelinEntity.getClusterName());

		if (zeppelinEntity != null) {
			String targetGroupArn = zeppelinEntity.getTargetGroupArn();
			String taskArn = zeppelinEntity.getTaskArn();
			String instanceStatus = zeppelinEntity.getInstanceStatus();
			String currentStatus = "";

			// 已经删除
			if (instanceStatus != null && instanceStatus.equals("DELETED")) {
				currentStatus = "DELETED";
				// 未删除则在生命周期内
			} else {
				//1. running、unhealth等状态获取真实状态
				if(instanceStatus.equals("RUNNING") || unhealthStatus.contains(instanceStatus)){
					try{
						DescribeTargetHealthRequest targetHealthRequest = new DescribeTargetHealthRequest();
						targetHealthRequest.setTargetGroupArn(targetGroupArn);
						DescribeTargetHealthResult healthResult = elbClient.describeTargetHealth(targetHealthRequest);
						List<TargetHealthDescription> listHelth = healthResult.getTargetHealthDescriptions();

						if (listHelth.size() > 0) {
							String targetHealth = listHelth.get(0).getTargetHealth().getState();
							if (targetHealth.equals("healthy")) {
								currentStatus =  "RUNNING";
							}else{
								currentStatus = targetHealth;
							}
						}
					}catch (Exception e){
						throw new Exception(String.format("%s ERROR:%s", userName,e.getMessage()));
					}
				//2.PROVISIONING、DELETED状态从数据库中获取
				}else {
					//根据update_time判断是否从数据库中获取状态
					double minutes = getDiffMinutesByDate(zeppelinEntity.getUpdateTime(),new Date());
//					System.out.println("minutes: "+String.valueOf(minutes));

					if(minutes>=4){
						currentStatus = getStatusFromAws(zeppelinEntity);
						if (currentStatus.equals("RUNNING")) {
							// 获取 taskArn
							taskArn = getTaskArnFromECS(zeppelinEntity.getClusterName(), serviceArn);
							//设置taskArn
							zeppelinEntity.setTaskArn(taskArn);

							// 更新logs开始时间为image拉取时间
							Date pullImageDate = getTaskPullImageTime(zeppelinEntity);
							SysZeppelinLogs logsEntity = new SysZeppelinLogs();
							logsEntity.setStartTime(pullImageDate);
							logsEntity.setId(zeppelinEntity.getLogId());

							zeppelinLogsService.updateById(logsEntity);
						}

						if (currentStatus.equals("DELETED")) {
							//更新zepplinLogs
							SysZeppelinLogs zeppelinLogsEntity = zeppelinLogsService.selectByPrimaryKey(zeppelinEntity.getLogId());
							//更新logs结束时间为task结束时间
							Date taskStoppedDate = getTaskStoppedTime(zeppelinEntity);
							zeppelinLogsEntity.setEndTime(taskStoppedDate);

							minutes = getDiffMinutesByDate(zeppelinLogsEntity.getStartTime(),taskStoppedDate);
							BigDecimal minuteDecimal = new BigDecimal(String.valueOf(minutes));
							minuteDecimal = minuteDecimal.divide(BigDecimal.ONE,1,BigDecimal.ROUND_CEILING);
							zeppelinLogsEntity.setDuration(minuteDecimal);

							//执行更新zeppelin logs
							zeppelinLogsService.updateById(zeppelinLogsEntity);

							//更新zeppelin instance
							BigDecimal totalMinute = zeppelinLogsService.getTotalMinuteByUserName(userName);
							zeppelinEntity.setTotalMinute(totalMinute);
						}

					}else{
						currentStatus = zeppelinEntity.getInstanceStatus();
					}
				}

				if(!currentStatus.equals(instanceStatus)){
					zeppelinEntity.setUpdateTime(new Date());
					zeppelinEntity.setInstanceStatus(currentStatus);

					if(currentStatus.equals("DELETED")){
						//清空taskArn
						zeppelinEntity.setTaskArn("");
					}
					zeppelinService.updateByUserName(zeppelinEntity);
				}
			}
			return currentStatus;
		} else {
			throw new Exception(String.format("%s not exists zeppelin instance!", userName));
		}
	}

	/**
	 * @Author yaleiwang
	 * @Description 启动或关闭阶段使用定时器查询状态
	 * @Date 2020-7-26 18:27
	 * @param zeppelinInstanceID zeppelin instance id
	 * @return void
	 **/
	private void updateStatusFromTimer(String zeppelinInstanceID) throws Exception {
		String userName = zeppelinInstanceID.split("-")[2];

		//1.Arns
		SysZeppelin zeppelinEntity = zeppelinService.selectByUserName(userName);

		String serviceArn = getServiceArn(zeppelinInstanceID,zeppelinEntity.getClusterName());

		if (zeppelinEntity != null) {
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					String taskArn = zeppelinEntity.getTaskArn();
					String instanceStatus = zeppelinEntity.getInstanceStatus();
					String currentStatus = getStatusFromAws(zeppelinEntity);

					if(StringUtils.isNullOrEmpty(currentStatus)){
						System.out.println(String.format("%s current status is %s timer will cancel！", userName, currentStatus));
						timer.cancel();
					}

					if (!currentStatus.equals(instanceStatus)) {
						Date now = new Date();
						zeppelinEntity.setUpdateTime(now);
						zeppelinEntity.setInstanceStatus(currentStatus);
						if (currentStatus.equals("DELETED") || currentStatus.equals("RUNNING") || currentStatus.equals("unhealthy")||currentStatus.equals("unused")||currentStatus.equals("unavailable")) {
							System.out.println(String.format("%s current status is %s timer will cancel！", userName, currentStatus));
							timer.cancel();

							if (currentStatus.equals("RUNNING")) {
								// 获取 taskArn
								taskArn = getTaskArnFromECS(zeppelinEntity.getClusterName(), serviceArn);
								zeppelinEntity.setTaskArn(taskArn);

								// 更新logs开始时间为image拉取时间
								Date pullImageDate = getTaskPullImageTime(zeppelinEntity);
								SysZeppelinLogs logsEntity = new SysZeppelinLogs();
								logsEntity.setStartTime(pullImageDate);
								logsEntity.setId(zeppelinEntity.getLogId());

								zeppelinLogsService.updateById(logsEntity);
							}

							if (currentStatus.equals("DELETED")) {
								//更新zepplinLogs
								SysZeppelinLogs zeppelinLogsEntity = zeppelinLogsService.selectByPrimaryKey(zeppelinEntity.getLogId());
								//更新logs结束时间为task结束时间
								Date taskStoppedDate = getTaskStoppedTime(zeppelinEntity);
								zeppelinLogsEntity.setEndTime(taskStoppedDate);

								double minutes = getDiffMinutesByDate(zeppelinLogsEntity.getStartTime(),taskStoppedDate);
								BigDecimal minuteDecimal = new BigDecimal(String.valueOf(minutes));
								minuteDecimal = minuteDecimal.divide(BigDecimal.ONE,1,BigDecimal.ROUND_CEILING);
								zeppelinLogsEntity.setDuration(minuteDecimal);

								//执行更新zeppelin logs
								zeppelinLogsService.updateById(zeppelinLogsEntity);

								//更新zeppelin instance
								BigDecimal totalMinute = zeppelinLogsService.getTotalMinuteByUserName(userName);
								zeppelinEntity.setTotalMinute(totalMinute);
								//清空taskArn
								zeppelinEntity.setTaskArn("");

							}
						}
						//更新状态
						zeppelinService.updateByUserName(zeppelinEntity);
					}
				}
			}, 1000, 3000);
		}else{
			throw new Exception(String.format("%s not exists zeppelin instance!", userName));
		}
	}

	/*
	 * @Author yaleiwang
	 * @Description 在开始或停止过程中从aws ecs、elb获取服务状态
	 * @Date 2020-8-11 18:32
	 * @param zeppelinEntity
	 * @return java.lang.String
	 **/
	private String getStatusFromAws(SysZeppelin zeppelinEntity){
		String targetGroupArn = zeppelinEntity.getTargetGroupArn();
		String taskArn = zeppelinEntity.getTaskArn();
		String instanceStatus = zeppelinEntity.getInstanceStatus();
		String currentStatus = "";

		// 创建过程,只通过TGhealth判断
		if (instanceStatus != null && (instanceStatus.equals("PROVISIONING")||instanceStatus.equals("PENDING"))) {
			try {
				DescribeTargetHealthRequest targetHealthRequest = new DescribeTargetHealthRequest();
				targetHealthRequest.setTargetGroupArn(targetGroupArn);
				DescribeTargetHealthResult healthResult = elbClient.describeTargetHealth(targetHealthRequest);
				List<TargetHealthDescription> listHelth = healthResult.getTargetHealthDescriptions();

				if (listHelth.size() > 0) {
					String targetHealth = listHelth.get(0).getTargetHealth().getState();
					if (targetHealth.equals("healthy")) {
						currentStatus = "RUNNING";
					} else {
						if (targetHealth.equals("initial")) {
							currentStatus = "PENDING";
						} else {
							currentStatus = targetHealth;
						}
					}
				} else {
					currentStatus = "PENDING";
				}
			} catch (Exception e) {
				currentStatus = "PENDING";
			}
			//停止状态
		} else if (instanceStatus != null && instanceStatus.equals("STOPPING")) {
			//1.获取task状态
//			DescribeTasksRequest describeTasksRequest = new DescribeTasksRequest();
//			describeTasksRequest.setCluster(zeppelinEntity.getClusterName());
//			describeTasksRequest.setTasks(Collections.singletonList(taskArn));
//			DescribeTasksResult describeTasksResult = amazonECS.describeTasks(describeTasksRequest);
//			String taskLastStatus = describeTasksResult.getTasks().get(0).getLastStatus();

			//2. get service status
			DescribeServicesRequest describeServicesRequest = new DescribeServicesRequest();
			describeServicesRequest.setCluster(zeppelinEntity.getClusterName());
			describeServicesRequest.setServices(Collections.singleton(zeppelinEntity.getInstanceName()));

			DescribeServicesResult describeServicesResult = amazonECS.describeServices(describeServicesRequest);
			String serviceStatus = describeServicesResult.getServices().get(0).getStatus();

//			System.out.println("serviceStatus:" + serviceStatus);
//			System.out.println("status:" + taskLastStatus);

			if (serviceStatus.equals("INACTIVE")) {
				currentStatus = "DELETED";
			} else {
				currentStatus = "STOPPING";
			}
		} else {
			currentStatus = instanceStatus;
		}

		return currentStatus;
	}
	
	/*
	 * @Author yaleiwang
	 * @Description 获取开始下载容器映像（Docker 拉取）时间
	 * @Date 2020-8-12 9:42
	 * @param zeppelinEntity
	 * @return java.util.Date
	 **/
	private Date getTaskPullImageTime(SysZeppelin zeppelinEntity){
		Date imagePullDate = new Date();
		try{
			//1.获取image 拉取开始时间
			DescribeTasksRequest describeTasksRequest = new DescribeTasksRequest();
			describeTasksRequest.setCluster(zeppelinEntity.getClusterName());
			describeTasksRequest.setTasks(Collections.singletonList(zeppelinEntity.getTaskArn()));
			DescribeTasksResult describeTasksResult = amazonECS.describeTasks(describeTasksRequest);
			imagePullDate = describeTasksResult.getTasks().get(0).getPullStartedAt();
		}catch (Exception ex){
			ex.printStackTrace();
		}

		return imagePullDate;
	}
	
	/*
	 * @Author yaleiwang
	 * @Description 获取任务终止时间
	 * @Date 2020-8-12 9:45 
	 * @param zeppelinEntity
	 * @return java.util.Date
	 **/
	private Date getTaskStoppedTime(SysZeppelin zeppelinEntity){
		Date taskStoppedDate = new Date();
		try{
			//1.获取任务终止时间
			DescribeTasksRequest describeTasksRequest = new DescribeTasksRequest();
			describeTasksRequest.setCluster(zeppelinEntity.getClusterName());
			describeTasksRequest.setTasks(Collections.singletonList(zeppelinEntity.getTaskArn()));
			DescribeTasksResult describeTasksResult = amazonECS.describeTasks(describeTasksRequest);
			taskStoppedDate = describeTasksResult.getTasks().get(0).getStoppedAt();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return taskStoppedDate;
	}
	
	/*
	 * @Author yaleiwang
	 * @Description 获取taskArn
	 * @Date 2020-8-12 9:50
	 * @param clusterName 集群名称
	 * @param serviceArn 服务Arn
	 * @return java.lang.String
	 **/
	private String getTaskArnFromECS(String clusterName,String serviceArn){
		// 获取 taskArn
		ListTasksRequest listTasksRequest = new ListTasksRequest();
		listTasksRequest.setServiceName(serviceArn);
		listTasksRequest.setCluster(clusterName);
		String taskArn = amazonECS.listTasks(listTasksRequest).getTaskArns().get(0);
		
		return taskArn;
	}

	/*
	 * @Author yaleiwang
	 * @Description 获取两个日期的分钟差
	 * @Date 2020-8-12 10:13
	 * @param startDate
	 * @param endDate
	 * @return double
	 **/
	private double getDiffMinutesByDate(Date startDate,Date endDate){
		long diff = endDate.getTime()-startDate.getTime();
		double minutes = diff/(1000 * 60* 1.0);

		return minutes;
	}

	
	/**
	 * ECS 创建Service
	 * 
	 * @param zeppelinEntity
	 * @param taskDefinitionArn
	 * @return
	 */
	public String prepareZeppelinInstanceService(SysZeppelin zeppelinEntity, String taskDefinitionArn) {
		String zeppelinInstanceID = zeppelinEntity.getInstanceName();

		List<String> subNetworks = Arrays.asList(taskSubnetId1, taskSubnetId2);//任务子网
		Preconditions.checkNotNull(amazonECS);
		Preconditions.checkState(!StringUtils.isNullOrEmpty(zeppelinInstanceID));
		Preconditions.checkState(!StringUtils.isNullOrEmpty(taskDefinitionArn));
		Preconditions.checkState(!StringUtils.isNullOrEmpty(ecsClusterName));
		Preconditions.checkState(!subNetworks.isEmpty());
		for (String subNetwork : subNetworks) {
			Preconditions.checkState(!StringUtils.isNullOrEmpty(subNetwork));
		}
		Preconditions.checkState(!StringUtils.isNullOrEmpty(taskSecurityGroupId));
		Preconditions.checkState(!StringUtils.isNullOrEmpty(serviceLoadbalancerArn));
		Preconditions.checkState(!StringUtils.isNullOrEmpty(serviceLoadbalancerListenerArn));

		ZeppelinLogClient logClient = new ZeppelinLogClient();

		boolean exists = logClient.logGroupExists(zeppelinInstanceID);
		if (exists) { // delete the log group first
			log.warn(String.format(
					"the log group for the zeppeline instance \"%s\" exists, will delete the group first.",
					zeppelinInstanceID));
			try {
				logClient.deleteLogGroup(zeppelinInstanceID);
				log.warn(String.format("the log group for the zeppeline instance \"%s\" has been deleted.",zeppelinInstanceID));
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
				return null;
			}
		}

		try {
			logClient.createLogGroup(zeppelinInstanceID);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}

		ZeppelinServiceClient serviceClient = new ZeppelinServiceClient(amazonECS,elbClient);
		try {
			zeppelinEntity.setTaskSubnet(subNetworks);
			//zeppelinUrl = "internal-md4x-zeppelin-1441652762.cn-northwest-1.elb.amazonaws.com.cn/md4x-zeppelin-31240*"

			//获取 elb DNS
			DescribeLoadBalancersRequest describeLoadBalancersRequest = new DescribeLoadBalancersRequest();
			describeLoadBalancersRequest.setLoadBalancerArns(Collections.singleton(serviceLoadbalancerArn));
			DescribeLoadBalancersResult desResult = elbClient.describeLoadBalancers(describeLoadBalancersRequest);
			LoadBalancer loadBalancer = desResult.getLoadBalancers().get(0);
			String elbDNSName = loadBalancer.getDNSName();
			String zeppelinUrl = String.format("%s/%s",elbDNSName,zeppelinInstanceID);
			zeppelinEntity.setZeppelinUrl(zeppelinUrl);
			zeppelinEntity.setUpdateTime(new Date());
			zeppelinEntity.setSecurityGroupId(taskSecurityGroupId);

			String serviceArn = serviceClient.createService(zeppelinEntity, taskDefinitionArn);

			//插入zeppelin log
			int logId = insertZeppelinLog(zeppelinEntity);

			zeppelinEntity.setLogId(logId);

			//向mysql插入或更新zeppelin instance infor
			insertZeppelinInstanceRecord(zeppelinEntity);

			//异步定时器更新状态
			updateStatusFromTimer(zeppelinInstanceID);

			return serviceArn;

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * @Author yaleiwang
	 * @Description 插入或更新zeppelin实例信息
	 * @Date 2020-7-23 22:13
	 * @param zeppelinEntity
	 * @return void
	 **/
	public void insertZeppelinInstanceRecord(SysZeppelin zeppelinEntity) throws Exception {
		try{
			//插入或更新zeppelin信息
			zeppelinService.insertZeppelin(zeppelinEntity);
		}catch (Exception e){
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * @Author yaleiwang
	 * @Description 插入zepplin log
	 * @Date 2020-7-23 22:20
	 * @param zeppelinEntity
	 * @return void
	 **/
	public int insertZeppelinLog(SysZeppelin zeppelinEntity) throws Exception {
		try{
			SysZeppelinLogs zeppelinLogsEntity = new SysZeppelinLogs();
			zeppelinLogsEntity.setUserName(zeppelinEntity.getUserName());
			zeppelinLogsEntity.setTaskFlavor(zeppelinEntity.getTaskFlavor());
			zeppelinLogsEntity.setImageId(zeppelinEntity.getImageId());
			zeppelinLogsEntity.setStartTime(zeppelinEntity.getUpdateTime());

			//插入zeppelin Log
			zeppelinLogsService.insertZeppelinLog(zeppelinLogsEntity);
			int logId = zeppelinLogsEntity.getId();
			return logId;

		}catch (Exception e){
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	/**
	 * @Author yaleiwang
	 * @Description 获取ecs serviceArn
	 * @Date 2020-10-14 10:39
	 * @param zeppelinInstanceID zeppelin instance name
	 * @param clusterName cluster name
	 * @return java.lang.String service arn
	 **/
	public String getServiceArn(String zeppelinInstanceID, String clusterName){
		//arn:aws-cn:ecs:cn-northwest-1:774564366034:service/md4x-zeppelin/md4x-zeppelin-12345
		//String serviceArn = String.format("arn:aws-cn:ecs:%s:%s:service/%s/%s", this.getRegion(), this.getAccountID(), clusterName, zeppelinInstanceID);
		String serviceArn = String.format("arn:aws-cn:ecs:%s:%s:service/%s/%s", region, this.getAccountID(), clusterName, zeppelinInstanceID);
		return serviceArn;
	}

}
