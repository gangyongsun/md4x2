package cn.com.goldwind.md4x.business.service.zeppelin;

import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelin;
import com.amazonaws.services.ecs.AmazonECS;
import com.amazonaws.services.ecs.model.LoadBalancer;
import com.amazonaws.services.ecs.model.*;
import com.amazonaws.services.elasticloadbalancingv2.AmazonElasticLoadBalancing;
import com.amazonaws.services.elasticloadbalancingv2.model.*;
import com.amazonaws.util.StringUtils;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ZeppelinServiceClient extends BaseClient {

    private static final Logger log = LoggerFactory.getLogger(ZeppelinServiceClient.class);

    private final AmazonECS client;
    private final AmazonElasticLoadBalancing elbClient;

    public ZeppelinServiceClient(AmazonECS client,AmazonElasticLoadBalancing elbClient) {
        Preconditions.checkNotNull(client);
        this.client = client;
        this.elbClient = elbClient;
    }

    /**
     * 
     * @param zeppelinEntity
     * @param taskDefinitionArn
     * @return
     * @throws Exception
     */
    public String createService(SysZeppelin zeppelinEntity, String taskDefinitionArn) throws Exception {

        String zeppelinInstanceID = zeppelinEntity.getInstanceName();
        String ecsClusterName = zeppelinEntity.getClusterName();
        List<String> subNetworks = (List<String>)zeppelinEntity.getTaskSubnet();
        String securityGroupName = zeppelinEntity.getSecurityGroupId();
        String loadBalancerArn = zeppelinEntity.getElbArn();
        String loadBalancerListenerArn = zeppelinEntity.getElbListenerArn();


        Preconditions.checkState(!StringUtils.isNullOrEmpty(zeppelinInstanceID));
        Preconditions.checkState(!StringUtils.isNullOrEmpty(taskDefinitionArn));
        Preconditions.checkState(!StringUtils.isNullOrEmpty(ecsClusterName));
        Preconditions.checkNotNull(subNetworks);
        Preconditions.checkState(!subNetworks.isEmpty());
        for (String subNetwork : subNetworks) {
            Preconditions.checkState(!StringUtils.isNullOrEmpty(subNetwork));
        }
        Preconditions.checkState(!StringUtils.isNullOrEmpty(securityGroupName));
        Preconditions.checkState(!StringUtils.isNullOrEmpty(loadBalancerArn));
        Preconditions.checkState(!StringUtils.isNullOrEmpty(loadBalancerListenerArn));

        DeploymentConfiguration deploymentConfig = new DeploymentConfiguration();
        deploymentConfig.withMaximumPercent(200);
        deploymentConfig.withMinimumHealthyPercent(100);

        DeploymentController deploymentController = new DeploymentController();
        deploymentController.setType("ECS");
       
        AwsVpcConfiguration awsVPCConfig = new AwsVpcConfiguration();
        awsVPCConfig.setAssignPublicIp("DISABLED");  // for security, suppose the task running in private subnet
        awsVPCConfig.setSecurityGroups(Collections.singletonList(securityGroupName));
        awsVPCConfig.setSubnets(subNetworks);

        NetworkConfiguration networkConfig = new NetworkConfiguration();
        networkConfig.setAwsvpcConfiguration(awsVPCConfig);

        String targetGroupArn = this.createTargetGroup(zeppelinInstanceID, loadBalancerArn);
        zeppelinEntity.setTargetGroupArn(targetGroupArn);

        ModifyTargetGroupAttributesRequest modifyTargetGroupAttributesRequest = new ModifyTargetGroupAttributesRequest();
        modifyTargetGroupAttributesRequest.setTargetGroupArn(targetGroupArn);
        TargetGroupAttribute targetGroupAttribute = new TargetGroupAttribute();

        targetGroupAttribute.setKey("deregistration_delay.timeout_seconds");
        targetGroupAttribute.setValue("60");
        modifyTargetGroupAttributesRequest.setAttributes(Collections.singletonList(targetGroupAttribute));
        elbClient.modifyTargetGroupAttributes(modifyTargetGroupAttributesRequest);

        this.createTrafficRule(zeppelinEntity);

        LoadBalancer loadBalancer = new LoadBalancer();
        loadBalancer.setContainerName(Const.TASK_CONTAINER_NAME);
        loadBalancer.setContainerPort(80); // co-setting with the task definition
        loadBalancer.setTargetGroupArn(targetGroupArn);

        CreateServiceRequest request = new CreateServiceRequest();
        request.setCluster(ecsClusterName);//ECS 集群名
        request.setTaskDefinition(taskDefinitionArn);
        request.setPlatformVersion("LATEST");//The platform version must be null when specifying an EC2 launch type
        // FARGATE
        request.setLaunchType(LaunchType.FARGATE.name());//FARGATE or EC2(defalut)
//        request.setLaunchType("EC2");
        request.setServiceName(zeppelinInstanceID);
        request.setDesiredCount(1);
        request.setSchedulingStrategy("REPLICA");
        request.setDeploymentConfiguration(deploymentConfig);
        request.setDeploymentController(deploymentController);
        request.setNetworkConfiguration(networkConfig);//网络配置
        request.setHealthCheckGracePeriodSeconds(300);
        request.setLoadBalancers(Collections.singletonList(loadBalancer));
        log.debug(String.format("create the ECS service for the task of zeppelin instance \"%s\".",zeppelinInstanceID));

        // TODO: handle "Unable to Start a service that is still Draining." case, especially in quick-rerun/debug.

        CreateServiceResult response = this.client.createService(request);

        if (response.getSdkHttpMetadata().getHttpStatusCode() != 200) {
            throw new Exception(String.format(
                    "failed to create the ECS service for the task of zeppelin instance \"%s\".", zeppelinInstanceID));
        }

        log.info(String.format("the ECS service \"%s\" for the task of zeppelin instance has been created.",
                response.getService().getServiceArn()));

        return response.getService().getServiceArn();
    }

    
    /**
     * 
     * @param loadBalancerArn
     * @return
     * @throws Exception
     */
    private String getLoadBalancerVPCID(String loadBalancerArn) throws Exception {
        return this.getLoadBalancer(loadBalancerArn).getVpcId();
    }

    
    /**
     * 
     * @param loadBalancerArn
     * @return
     * @throws Exception
     */
    private com.amazonaws.services.elasticloadbalancingv2.model.LoadBalancer getLoadBalancer(
            String loadBalancerArn) throws Exception {
        Preconditions.checkState(!StringUtils.isNullOrEmpty(loadBalancerArn));

        DescribeLoadBalancersRequest request = new DescribeLoadBalancersRequest();
        request.setLoadBalancerArns(Collections.singletonList(loadBalancerArn));

        DescribeLoadBalancersResult response = elbClient.describeLoadBalancers(request);

        if (response.getLoadBalancers().size() < 1) {
            throw new Exception(String.format("loadbalancer %s is not found.", loadBalancerArn));
        }

        return response.getLoadBalancers().get(0);
    }

    
    
    /**
     * 
     * @param zeppelinInstanceID
     * @param loadBalancerArn
     * @return
     * @throws Exception
     */
    private String createTargetGroup(String zeppelinInstanceID, String loadBalancerArn) throws Exception {
        Preconditions.checkState(!StringUtils.isNullOrEmpty(zeppelinInstanceID));
        Preconditions.checkState(!StringUtils.isNullOrEmpty(loadBalancerArn));

        CreateTargetGroupRequest request = new CreateTargetGroupRequest();
        request.setHealthCheckEnabled(true);
        request.setHealthCheckIntervalSeconds(15);
        request.setHealthCheckPath(String.format("/%s/#/", zeppelinInstanceID));  // path for zeppelin home page
        request.setHealthCheckPort("80");
        request.setHealthCheckProtocol(ProtocolEnum.HTTP);
        request.setHealthCheckTimeoutSeconds(10);
        request.setHealthyThresholdCount(2);
        request.setUnhealthyThresholdCount(2);
        request.setProtocol(ProtocolEnum.HTTP);
        request.setPort(80);  // TODO: use HTTPS/443 instead
        request.setTargetType(TargetTypeEnum.Ip);
        request.setVpcId(this.getLoadBalancerVPCID(loadBalancerArn));//*****  VCPID可直接传入
        //request.setVpcId("vpc-0f89b0d8d429ced6c");//*****
        
        request.setName(zeppelinInstanceID);
        
        CreateTargetGroupResult response = elbClient.createTargetGroup(request);

        if (response.getSdkHttpMetadata().getHttpStatusCode() != 200) {
            throw new Exception(String.format("failed to create target group \"%s\".", zeppelinInstanceID));
        }

        log.debug(String.format("the target group \"%s\" has been created.",
                response.getTargetGroups().get(0).getTargetGroupName()));

        return response.getTargetGroups().get(0).getTargetGroupArn();
    }
    
    
    /**
     * 
     * @param zeppelinEntity zeppelin实例
     */
    private void createTrafficRule(SysZeppelin zeppelinEntity) {
        String zeppelinInstanceID = zeppelinEntity.getInstanceName();
        String listenerArn = zeppelinEntity.getElbListenerArn();
        String targetGroupArn = zeppelinEntity.getTargetGroupArn();


        Preconditions.checkState(!StringUtils.isNullOrEmpty(zeppelinInstanceID));
        Preconditions.checkState(!StringUtils.isNullOrEmpty(listenerArn));
        Preconditions.checkState(!StringUtils.isNullOrEmpty(targetGroupArn));

        PathPatternConditionConfig pathPatternConfig = new PathPatternConditionConfig();
        pathPatternConfig.setValues(Collections.singletonList(String.format("/%s*", zeppelinInstanceID)));
        RuleCondition condition = new RuleCondition();
        condition.setPathPatternConfig(pathPatternConfig);
        condition.setField("path-pattern");

        TargetGroupTuple targetGroupTuple = new TargetGroupTuple();
        targetGroupTuple.setTargetGroupArn(targetGroupArn);
        targetGroupTuple.setWeight(1);
        ForwardActionConfig forwardConfig = new ForwardActionConfig();
        forwardConfig.setTargetGroups(Collections.singletonList(targetGroupTuple));
        Action action = new Action();
        action.setForwardConfig(forwardConfig);
        action.setType(ActionTypeEnum.Forward);

        DescribeRulesRequest rulesRequest = new DescribeRulesRequest();
        rulesRequest.setListenerArn(listenerArn);
        rulesRequest.setPageSize(400);
        DescribeRulesResult desRules = elbClient.describeRules(rulesRequest);
        List<Rule> rules = desRules.getRules();


        List<Integer> priorityList = new ArrayList<>();
        rules.forEach(rule->{if(!rule.getPriority().equals("default")){
            priorityList.add(Integer.parseInt(rule.getPriority()));
        }});

        while(desRules.getNextMarker()!=null){
            desRules = elbClient.describeRules(rulesRequest);
            desRules.getRules().forEach(rule->{if(!rule.getPriority().equals("default")){
                priorityList.add(Integer.parseInt(rule.getPriority()));
            }});
        }

        CreateRuleRequest request = new CreateRuleRequest();
        request.setConditions(Collections.singletonList(condition));
        request.setActions(Collections.singletonList(action));
        request.setListenerArn(listenerArn);

        request.setPriority(getAvailablePriority(priorityList));//如果再次创建时，要判断这个优级是否已经存在，否则出错

        CreateRuleResult ruleResult = this.elbClient.createRule(request);
        String ruleArn = ruleResult.getRules().get(0).getRuleArn();
        zeppelinEntity.setRuleArn(ruleArn);
    }

    /*
     * @Author yaleiwang
     * @Description 获取可用priority算法
     * @Date 2020-8-7 17:20
     * @param priorityList
     * @return int
     **/
    private int getAvailablePriority(List<Integer> priorityList) {
        int priority = -1;
        if(priorityList.size() != 0) {
	        int minPri = Collections.min(priorityList);
	        int maxPri = Collections.max(priorityList);
	        for (int i = minPri; i < maxPri; i++) {
	            if (!priorityList.contains(i)) {
	                priority = i;
	                break;
	            }
	        }
	        if (priority == -1) {
	            priority = maxPri + 1;
	        }
        }else {
        	priority = 1;
        }
        return priority;
    }

    public boolean serviceRunning(String zeppelinInstanceID) {
        // TODO: implement
        return false;
    }

    public void waitUntilServiceHealthilyRunning(String zeppelinInstanceID) {
        // TODO: implement
    }

    public void deleteService(String zeppelinInstanceID, String taskDefinitionArn, String ecsClusterName,
                              String loadBalancerArn, String loadBalancerListenerArn) throws Exception {
        // TODO: implement
    }
}
