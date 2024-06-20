package cn.com.goldwind.md4x.business.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.glue.AWSGlue;
import com.amazonaws.services.glue.AWSGlueClientBuilder;
import com.amazonaws.services.glue.model.Action;
import com.amazonaws.services.glue.model.Condition;
import com.amazonaws.services.glue.model.CrawlerTargets;
import com.amazonaws.services.glue.model.CreateCrawlerRequest;
import com.amazonaws.services.glue.model.CreateJobRequest;
import com.amazonaws.services.glue.model.CreateJobResult;
import com.amazonaws.services.glue.model.CreateTriggerRequest;
import com.amazonaws.services.glue.model.CreateTriggerResult;
import com.amazonaws.services.glue.model.CreateWorkflowRequest;
import com.amazonaws.services.glue.model.CreateWorkflowResult;
import com.amazonaws.services.glue.model.DeleteCrawlerRequest;
import com.amazonaws.services.glue.model.DeleteTriggerRequest;
import com.amazonaws.services.glue.model.DeleteWorkflowRequest;
import com.amazonaws.services.glue.model.DeleteWorkflowResult;
import com.amazonaws.services.glue.model.EntityNotFoundException;
import com.amazonaws.services.glue.model.GetJobRunRequest;
import com.amazonaws.services.glue.model.GetJobRunResult;
import com.amazonaws.services.glue.model.GetWorkflowRequest;
import com.amazonaws.services.glue.model.GetWorkflowResult;
import com.amazonaws.services.glue.model.GetWorkflowRunRequest;
import com.amazonaws.services.glue.model.GetWorkflowRunResult;
import com.amazonaws.services.glue.model.ListWorkflowsRequest;
import com.amazonaws.services.glue.model.ListWorkflowsResult;
import com.amazonaws.services.glue.model.Logical;
import com.amazonaws.services.glue.model.LogicalOperator;
import com.amazonaws.services.glue.model.Predicate;
import com.amazonaws.services.glue.model.PutWorkflowRunPropertiesRequest;
import com.amazonaws.services.glue.model.S3Target;
import com.amazonaws.services.glue.model.StartJobRunRequest;
import com.amazonaws.services.glue.model.StartJobRunResult;
import com.amazonaws.services.glue.model.StartWorkflowRunRequest;
import com.amazonaws.services.glue.model.StartWorkflowRunResult;
import com.amazonaws.services.glue.model.StopWorkflowRunRequest;
import com.amazonaws.services.glue.model.StopWorkflowRunResult;
import com.amazonaws.services.glue.model.TriggerType;
import com.amazonaws.services.glue.model.ValidationException;
import com.amazonaws.services.glue.model.Workflow;
import com.amazonaws.services.glue.model.WorkflowRunStatus;
import com.amazonaws.util.StringUtils;

import cn.com.goldwind.md4x.business.dao.SysAwsGlueDao;
import cn.com.goldwind.md4x.business.entity.datamart.ExparamDatamart;
import cn.com.goldwind.md4x.business.entity.datamart.SysAwsGlue;
import cn.com.goldwind.md4x.business.service.datamart.IExparamDatamartService;

/**
 * AWS GLUE 服务封装
 * @author wangguiyu
 *
 */
@Service
public class AwsGlueService implements InitializingBean {

	private AWSGlue awsGlue;
	
	@Value("${aws.credentialFlag}")
	private String credentialFlag;
	
	@Value("${aws.ak}")
	private String accessKey;
	
	@Value("${aws.sk}")
	private String secretKey;
	
	@Value("${aws.s3bucketName}")
	private String s3bucketName;
	
	@Value("${aws.iamRole}")
	private String awsIAMRole;
	
	@Value("${aws.region}")
	private String region;
	
	@Value("${aws.jobName}")
	private String jobName;
	
	@Value("${aws.s3bucketPrefix}")
	private String s3bucketPrefix;
	
	@Value("${aws.awsGlueDataCatalogPrefix}")
	private String awsGlueDataCatalogPrefix;
	
	@Value("${aws.workflowNamePrefix}")
	private String workflowNamePrefix;
	
	@Value("${aws.triggerNamePrefix}")
	private String triggerNamePrefix;
	
	@Value("${aws.crawlerNamePrefix}")
	private String crawlerNamePrefix;
	
	@Autowired
	private SysAwsGlueDao sysAwsGlueDao;
	
	@Autowired
	private IExparamDatamartService exparamDatamartService;
    
	@Override
	public void afterPropertiesSet() throws Exception {
		if ("true".equals(credentialFlag)) {
			if(!StringUtils.isNullOrEmpty(accessKey) && !StringUtils.isNullOrEmpty(secretKey)){
				AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
				awsGlue = AWSGlueClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(region).build();
			}else {
				awsGlue = AWSGlueClientBuilder.standard().withRegion(region).build();
			}
			
		} else {
			awsGlue = AWSGlueClientBuilder.standard().withRegion(region).withCredentials(new InstanceProfileCredentialsProvider(false)).build();
		}
	}
	
	//1. 创建工作流
	public String createWorkflow(String workflowName) {
		//Todo:首先判断 workflowName 是否已经存在，不存在创建，如存在，不创建
		CreateWorkflowRequest createWorkflowRequest = new CreateWorkflowRequest();
		createWorkflowRequest.setName(workflowName);
		createWorkflowRequest.setDescription("工作流描述");
		CreateWorkflowResult result = awsGlue.createWorkflow(createWorkflowRequest);
		return result.getName();
	}
	
	//2. 新建触发器
	public String createTrigger(String triggerName,TriggerType triggerType,String workflowName, String jobName,String crawlerName) {
		CreateTriggerRequest  createTriggerRequest = new CreateTriggerRequest();
		createTriggerRequest.setName(triggerName);
		createTriggerRequest.setType(triggerType.name());
		createTriggerRequest.setWorkflowName(workflowName);
		if(TriggerType.ON_DEMAND.equals(triggerType)) {
			createTriggerRequest.setStartOnCreation(false);
			List<Action>  actions = new ArrayList<Action>();
			Action action = new Action();
			action.setJobName(jobName);
			actions.add(action);
			createTriggerRequest.setActions(actions);
			Predicate predicate = new Predicate();
			Condition condition = new Condition();
			condition.setJobName(jobName);
			predicate.withConditions(condition);
			createTriggerRequest.setPredicate(predicate);
			
		}else if(TriggerType.CONDITIONAL.equals(triggerType)) {
			createTriggerRequest.setStartOnCreation(true);
			Predicate predicate = new Predicate();
			List<Condition> conditions = new ArrayList<Condition>();
			Condition condition = new Condition();
			condition.setCrawlerName(crawlerName);
			condition.setJobName(jobName);
			condition.setState("SUCCEEDED");
			condition.setLogicalOperator(LogicalOperator.EQUALS.name());
			conditions.add(condition);
			predicate.setConditions(conditions);
			predicate.setLogical(Logical.AND.name());
			createTriggerRequest.setPredicate(predicate);
			List<Action>  actions = new ArrayList<Action>();
			Action action = new Action();
			action.setCrawlerName(crawlerName);//crawName
			createTriggerRequest.setStartOnCreation(true);
			//action.setJobName(jobName);//jobName  //Both JobName or CrawlerName cannot be set together in an action
			actions.add(action);
			createTriggerRequest.setActions(actions);
		}
		CreateTriggerResult result = awsGlue.createTrigger(createTriggerRequest);
		return result.getName();
	}

	//3. 创建crawler
	public void createCrawler(String crawlerName,String crawlerDatabaseName,String crawlerTablePrefix,String crawlerAWSS3Target) {
		CreateCrawlerRequest  createCrawlerRequest = new CreateCrawlerRequest();
		createCrawlerRequest.setName(crawlerName);                        // 1.crawler名字不能重复
		createCrawlerRequest.setDatabaseName(crawlerDatabaseName);// 2.catalog dataname
		createCrawlerRequest.setTablePrefix(crawlerTablePrefix); // 3.表前缀名
		createCrawlerRequest.setRole(awsIAMRole);
		CrawlerTargets crawlerTargets =new CrawlerTargets();
		List<S3Target> s3Targets = new ArrayList<S3Target>();
		S3Target s3Target = new S3Target();
		s3Target.setPath(crawlerAWSS3Target);//4.生成catalog data的数据源
		s3Targets.add(s3Target);
		crawlerTargets.setS3Targets(s3Targets);
		createCrawlerRequest.setTargets(crawlerTargets);
		awsGlue.createCrawler(createCrawlerRequest);
	}
		
	//4. 启动工作流******
	public String startWorkflow(String datasetId,String datasetScope,String datasetOwner) {
		String workflowName = workflowNamePrefix + datasetId;
		String triggerName =  triggerNamePrefix +  datasetId;
		String crawlerName =  crawlerNamePrefix +  datasetId;
		String crawlerDatabaseName = awsGlueDataCatalogPrefix+datasetScope+"_"+datasetOwner;
		String crawlerTablePrefix = awsGlueDataCatalogPrefix+datasetScope+"_"+datasetOwner+"_";
		String crawlerAWSS3Target = "s3://"+s3bucketName+"/"+datasetScope+"/"+datasetOwner+"/"+datasetId;
		//先判断工作流是否存在，如果存在且工作流状态不是running，则可以直接启动;否则不启动，返回最新的一次workflow runid
		//如果工作流不存在，则重新创建并启动。
		GetWorkflowResult getWorkflowResult = getWorkflows(workflowName);
		if(getWorkflowResult != null) {
			Workflow workflow = getWorkflowResult.getWorkflow();
			String status = workflow.getLastRun().getStatus();
			if(!status.equals(WorkflowRunStatus.RUNNING.name())) {
				StartWorkflowRunRequest startWorkflowRunRequest = new StartWorkflowRunRequest();
				startWorkflowRunRequest.setName(workflowName);
				StartWorkflowRunResult result = awsGlue.startWorkflowRun(startWorkflowRunRequest);
				//设置工作流运行时属性
				PutWorkflowRunPropertiesRequest putWorkflowRunPropertiesRequest = new PutWorkflowRunPropertiesRequest();
				Map<String,String> runProperties = new HashMap<String,String>();
				runProperties.put("datasetId",datasetId);
				runProperties.put("crawlerAWSS3Target",crawlerAWSS3Target);
				putWorkflowRunPropertiesRequest.setRunProperties(runProperties);
				putWorkflowRunPropertiesRequest.setName(workflowName);
				putWorkflowRunPropertiesRequest.setRunId(result.getRunId());
				awsGlue.putWorkflowRunProperties(putWorkflowRunPropertiesRequest);
				return result.getRunId();
			}else {//RUNNING
				return workflow.getLastRun().getWorkflowRunId();
			}
		}
		
		//新建工作流
		createWorkflow(workflowName);
		//新建按需触发器
		createTrigger(triggerName+"_start",TriggerType.ON_DEMAND,workflowName, jobName,null);
		//新建crawler
		createCrawler(crawlerName,crawlerDatabaseName,crawlerTablePrefix,crawlerAWSS3Target);
		//新建条件触发器
		createTrigger(triggerName,TriggerType.CONDITIONAL,workflowName, jobName,crawlerName);
		//启动工作流
		StartWorkflowRunRequest startWorkflowRunRequest = new StartWorkflowRunRequest();
		startWorkflowRunRequest.setName(workflowName);
		StartWorkflowRunResult result = awsGlue.startWorkflowRun(startWorkflowRunRequest);
		
		//---记录工作流的启动时间[数据抽取起始时间]---------
		cn.com.goldwind.md4x.mybatis.Condition c = cn.com.goldwind.md4x.mybatis.Condition.newInstance();
		c.addCondition("dataset_id", datasetId).addCondition("dataset_owner", datasetOwner);
		List<ExparamDatamart> eds = exparamDatamartService.getList(c);
		ExparamDatamart ed = eds.get(0);
		ed.setDataExtractingStarttime(new Date());
		exparamDatamartService.updateDataExtractingStarttime(ed);
		
		//设置工作流运行时属性
		PutWorkflowRunPropertiesRequest putWorkflowRunPropertiesRequest = new PutWorkflowRunPropertiesRequest();
		Map<String,String> runProperties = new HashMap<String,String>();
		runProperties.put("datasetId",datasetId);
		runProperties.put("crawlerAWSS3Target",crawlerAWSS3Target);
		putWorkflowRunPropertiesRequest.setRunProperties(runProperties);
		putWorkflowRunPropertiesRequest.setName(workflowName);
		putWorkflowRunPropertiesRequest.setRunId(result.getRunId());
		awsGlue.putWorkflowRunProperties(putWorkflowRunPropertiesRequest);
		
		//--相关信息写入表sys_aws_glue---
		SysAwsGlue sysAwsGlue = new SysAwsGlue();
		sysAwsGlue.setDatasetId(datasetId);
		sysAwsGlue.setDatasetOwner(datasetOwner);
		sysAwsGlue.setDatasetScope(datasetScope);
		sysAwsGlue.setWorkflowName(workflowName);
		sysAwsGlue.setWorkflowRunId(result.getRunId());
		sysAwsGlue.setTableName(crawlerTablePrefix+"_"+datasetId);
		sysAwsGlue.setCrawlerName(crawlerName);
		sysAwsGlue.setTriggerName(triggerName);
		sysAwsGlue.setDatabaseName(crawlerDatabaseName);
		sysAwsGlue.setDelFlag(false);
		sysAwsGlueDao.insertSelective(sysAwsGlue);
	    return result.getRunId();
	}
	
	
	// 获取一个工作流的详细信息
	public GetWorkflowResult getWorkflows(String workflowName) {
		GetWorkflowRequest getWorkflowRequest = new GetWorkflowRequest();
		getWorkflowRequest.setName(workflowName);
		getWorkflowRequest.setIncludeGraph(true);
		try {
			return awsGlue.getWorkflow(getWorkflowRequest);
		}catch(EntityNotFoundException e) {
			return null;
		}
	}

	// 列出所有工作流
	public List<String> getWorkflows() {
		ListWorkflowsRequest listWorkflowsRequest = new ListWorkflowsRequest();
		ListWorkflowsResult result = awsGlue.listWorkflows(listWorkflowsRequest);
		List<String> list = result.getWorkflows();
		return list;
	}

	// 停止工作流
	public String stopWorkflow(String datasetId) {
		String workflowName = workflowNamePrefix + datasetId;
		StopWorkflowRunRequest stopWorkflowRunRequest = new StopWorkflowRunRequest();
		stopWorkflowRunRequest.setName(workflowName);
		GetWorkflowResult getWorkflowResult = getWorkflows(workflowName);
		if(getWorkflowResult != null) {
			Workflow workflow = getWorkflowResult.getWorkflow();
			stopWorkflowRunRequest.setRunId(workflow.getLastRun().getWorkflowRunId());
		}else {
			return "工作流不存在!";
		}
		StopWorkflowRunResult result = awsGlue.stopWorkflowRun(stopWorkflowRunRequest);
		return result.getSdkResponseMetadata().getRequestId();
	}
	
	// 删除指定工作流、触发器、crawler
	public String deleteWorkflow(String datasetId) {
		String workflowName = workflowNamePrefix + datasetId;
		String triggerName = triggerNamePrefix + datasetId;// 条件触发器
		String triggerName_start = triggerNamePrefix + datasetId + "_start"; // 按需触发器
		String crawlerName = crawlerNamePrefix + datasetId;
		// String crawlerDatabaseName =
		// awsGlueDataCatalogPrefix+datasetScope+"_"+datasetOwner;
		// String crawlerTablePrefix =
		// awsGlueDataCatalogPrefix+datasetScope+"_"+datasetOwner+"_";
		// String crawlerAWSS3Target =
		// "s3://"+s3bucketName+"/"+datasetScope+"/"+datasetOwner+"/"+datasetId;
		String ret = "";
		try {
			// 删除工作流
			DeleteWorkflowRequest deleteWorkflowRequest = new DeleteWorkflowRequest();
			deleteWorkflowRequest.setName(workflowName);
			DeleteWorkflowResult result = awsGlue.deleteWorkflow(deleteWorkflowRequest);

			// 删除按需触发器
			DeleteTriggerRequest deleteTriggerRequest_start = new DeleteTriggerRequest();
			deleteTriggerRequest_start.setName(triggerName_start);
			awsGlue.deleteTrigger(deleteTriggerRequest_start);

			// 删除条件触发器
			DeleteTriggerRequest deleteTriggerRequest = new DeleteTriggerRequest();
			deleteTriggerRequest.setName(triggerName);
			awsGlue.deleteTrigger(deleteTriggerRequest);

			// 删除crawler
			DeleteCrawlerRequest deleteCrawlerRequest = new DeleteCrawlerRequest();
			deleteCrawlerRequest.setName(crawlerName);
			awsGlue.deleteCrawler(deleteCrawlerRequest);
			
			//从表sys_aws_glue删除相应记录,逻辑删除
			//sysAwsGlueDao.deleteByCondition(cn.com.goldwind.md4x.mybatis.Condition.newInstance().addCondition("workflow_name", workflowName));
			List<SysAwsGlue> list = sysAwsGlueDao.searchListByCondition(cn.com.goldwind.md4x.mybatis.Condition.newInstance().addCondition("workflow_name", workflowName));
			if(list != null && list.size()>0) {
				SysAwsGlue sysAwsGlue = list.get(0);
				sysAwsGlue.setDelFlag(true);
				sysAwsGlueDao.updateByPrimaryKeySelective(sysAwsGlue);
			}
			ret = result.getName();
		} catch (EntityNotFoundException e) {
			ret = e.getMessage();
		}
		return ret;
	}
	
	// 查询工作流状态
	public String getWorkflowsStatus(String datasetId) {
		String workflowName = workflowNamePrefix + datasetId;
		GetWorkflowResult getWorkflowResult = getWorkflows(workflowName);
		if(getWorkflowResult != null) {
			Workflow workflow = getWorkflowResult.getWorkflow();
			String status = workflow.getLastRun().getStatus();
			if("COMPLETED".equals(status)) {
				//---记录工作流的停止时间[数据抽取结束时间]---------
				cn.com.goldwind.md4x.mybatis.Condition c = cn.com.goldwind.md4x.mybatis.Condition.newInstance();
				c.addCondition("dataset_id", datasetId);
				List<ExparamDatamart> eds = exparamDatamartService.getList(c);
				ExparamDatamart ed = eds.get(0);
				ed.setDataExtractingEndtime(new Date());
				exparamDatamartService.updateDataExtractingEndtime(ed);
			}
			return status;//// RUNNING,COMPLETED、STOPPING、STOPPED
		}else {
			return "工作流不存在!";
		}
	}
	
	//查询工作流状态
	public String getWorkflowsStatusTest(String workflowName) {
		//通过workflowName查找runId
		List<SysAwsGlue> list = sysAwsGlueDao.searchListByCondition(cn.com.goldwind.md4x.mybatis.Condition.newInstance().addCondition("workflow_name", workflowName));
		String runId = null;
		String datasetId = null;
		String datasetOwner = null;
		if(list != null && list.size()>0) {
			SysAwsGlue sysAwsGlue = list.get(0);
			runId = sysAwsGlue.getWorkflowRunId();
			datasetId = sysAwsGlue.getDatasetId();
			datasetOwner = sysAwsGlue.getDatasetOwner();
		}
		GetWorkflowRunRequest getWorkflowRunRequest = new GetWorkflowRunRequest();
		getWorkflowRunRequest.setName(workflowName);
		getWorkflowRunRequest.setRunId(runId);
		String ret=null;
		try {
			GetWorkflowRunResult result = awsGlue.getWorkflowRun(getWorkflowRunRequest);
			ret = result.getRun().getStatus();// RUNNING,COMPLETED、STOPPING、STOPPED
			if("COMPLETED".equals(ret)) {
				//---记录工作流的停止时间[数据抽取结束时间]---------
				cn.com.goldwind.md4x.mybatis.Condition c = cn.com.goldwind.md4x.mybatis.Condition.newInstance();
				c.addCondition("dataset_id", datasetId).addCondition("dataset_owner", datasetOwner);
				List<ExparamDatamart> eds = exparamDatamartService.getList(c);
				ExparamDatamart ed = eds.get(0);
				ed.setDataExtractingEndtime(new Date());
				exparamDatamartService.updateDataExtractingEndtime(ed);
			}
		}catch(ValidationException e) {
			ret = "工作流不存在";
		}
		return ret;
	}

	//创建作业
	public String createJob(String jobName) {
		CreateJobRequest createJobRequest = new CreateJobRequest();
		createJobRequest.setName(jobName);
		CreateJobResult result = awsGlue.createJob(createJobRequest);
		return result.getName();
	}
	
	// 启动作业
	public String startJob(String jobName) {
		StartJobRunRequest startJobRunRequest = new StartJobRunRequest();
		startJobRunRequest.setJobName(jobName);
		Map<String, String> arguments = new HashMap<String, String>();
		// 注意，参数key定义为以两个连字符开头，在aws作业脚本中使用时，不需要连接符，直接取 key1,key2,k3,k4
		arguments.put("--key1", "value1");
		arguments.put("--key2", "value2");
		arguments.put("--key3", "value3");
		arguments.put("--key4", "value4");
		startJobRunRequest.setArguments(arguments);
		StartJobRunResult result = awsGlue.startJobRun(startJobRunRequest);
		return result.getJobRunId();
	}

	// 查询作业状态
	public String getJob(String runId) {
		GetJobRunRequest  getJobRunRequest  = new GetJobRunRequest ();
		getJobRunRequest.setRunId(runId);                   
		GetJobRunResult result = awsGlue.getJobRun(getJobRunRequest);
		return result.getJobRun().getJobRunState();
	}
	
	//创建触发器
	public String createTrigger(String triggerName,TriggerType triggerType){
		CreateTriggerRequest createTriggerRequest = new CreateTriggerRequest();
		createTriggerRequest.setName(triggerName);
		createTriggerRequest.setStartOnCreation(false);//ON_DEMAND 需要时触发
		createTriggerRequest.setType(triggerType.name());//触发类型

		CreateTriggerResult result = awsGlue.createTrigger(createTriggerRequest);
		return result.getName();
	}
	
	public static void main(String[] args) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String suffix = fmt.format(new Date());
		System.out.println(suffix);
	}
	
	
}
