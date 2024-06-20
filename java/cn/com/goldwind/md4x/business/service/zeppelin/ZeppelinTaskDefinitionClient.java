package cn.com.goldwind.md4x.business.service.zeppelin;

import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelin;
import com.amazonaws.services.ecs.AmazonECS;
import com.amazonaws.services.ecs.model.*;
import com.amazonaws.util.StringUtils;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZeppelinTaskDefinitionClient extends BaseClient {
    private static final String TASK_CONTAINER_IMAGE_NAME = "goldwind/zeppelin";
    private static final String TASK_CONTAINER_IMAGE_TAG ="latest"; //"0.9.0";
    private static final String TASK_DEFINITION_FAMILY_PREFIX = "goldwind-zeppelin-instance";
    private static final String TASK_LOG_STREAM_PREFIX = "ecs";
    private static final Logger log = LoggerFactory.getLogger("zeppelin-controller-task-definition-client");

    private final AmazonECS client;

    public ZeppelinTaskDefinitionClient(AmazonECS client) {
        Preconditions.checkNotNull(client);
        this.client = client;
    }

    public boolean taskDefinitionExists(String zeppelinInstanceID) {
        int count = this.listTaskDefinitions(zeppelinInstanceID).size();
        log.debug(String.format("found %d existing ECS task definition(s).", count));
        return count > 0;
    }
   
    public List<String> listTaskDefinitions(String zeppelinInstanceID) {
        Preconditions.checkState(!StringUtils.isNullOrEmpty(zeppelinInstanceID));

        String taskDefinitionFamily = this.getTaskDefinitionFamilyName(zeppelinInstanceID);

        ListTaskDefinitionsRequest request = new ListTaskDefinitionsRequest();
        request.setFamilyPrefix(taskDefinitionFamily);
        request.setMaxResults(100);

        log.debug(String.format("list ECS task definitions about the family \"%s\".", taskDefinitionFamily));

        ListTaskDefinitionsResult response = this.client.listTaskDefinitions(request);
        return response.getTaskDefinitionArns();
    }

    public void deregisterTaskDefinition(String zeppelinInstanceID) throws Exception {
        Preconditions.checkState(!StringUtils.isNullOrEmpty(zeppelinInstanceID));

        List<String> taskDefinitionArns = this.listTaskDefinitions(zeppelinInstanceID);

        for (String taskDefinitionArn : taskDefinitionArns) {
            DeregisterTaskDefinitionRequest request = new DeregisterTaskDefinitionRequest();
            request.withTaskDefinition(taskDefinitionArn);

            log.debug(String.format("deregister the ECS task definition \"%s\".", taskDefinitionArn));

            DeregisterTaskDefinitionResult response = this.client.deregisterTaskDefinition(request);
            if (response.getSdkHttpMetadata().getHttpStatusCode() != 200) {
                throw new Exception(String.format(
                        "failed to deregister ECS task definition \"%s\".", taskDefinitionArn));
            }

            log.debug(String.format("the ECS task definition \"%s\" has been de-registered.", taskDefinitionArn));
        }
    }

    /**
     * 
     * @param zeppelinEntity    zeppelin实例
     * @param taskFlavor            实例cpu、memery大小
     * @param imageUri              镜像URI
     * @param domain            zeppelin访问域名
     * @return
     * @throws Exception
     */
    public String registerTaskDefinition(SysZeppelin zeppelinEntity, ZeppelinInstanceFlavor.Flavor taskFlavor,
                                         String imageUri,String domain) throws Exception {

        String zeppelinInstanceID = zeppelinEntity.getInstanceName();
        String taskRoleArn = zeppelinEntity.getTaskRoleArn();
        String taskExecutionRoleArn = zeppelinEntity.getTaskRoleArn();
        String OAUsername = zeppelinEntity.getUserName();
        String internalPassword = zeppelinEntity.getUserPwd();
        String gitRemoteURL = zeppelinEntity.getBitbucketUrl();
        String gitUsername= zeppelinEntity.getBitbucketName();
        String gitAccessToken = zeppelinEntity.getBitbucketPwd();

        Preconditions.checkState(!StringUtils.isNullOrEmpty(zeppelinInstanceID));
        Preconditions.checkState(!StringUtils.isNullOrEmpty(taskRoleArn));
        Preconditions.checkState(!StringUtils.isNullOrEmpty(taskExecutionRoleArn));
        Preconditions.checkNotNull(taskFlavor);
        Preconditions.checkState(!StringUtils.isNullOrEmpty(OAUsername));
        Preconditions.checkState(!StringUtils.isNullOrEmpty(internalPassword));
        Preconditions.checkState(!StringUtils.isNullOrEmpty(domain));
        if (!StringUtils.isNullOrEmpty(gitRemoteURL)) {
            Preconditions.checkState(!StringUtils.isNullOrEmpty(gitUsername));
            Preconditions.checkState(!StringUtils.isNullOrEmpty(gitAccessToken));
        }

//        String image = String.format("%s.dkr.ecr.%s.amazonaws.com.cn/%s:%s",this.getAccountID(), this.getRegion(),TASK_CONTAINER_IMAGE_NAME, TASK_CONTAINER_IMAGE_TAG);

        PortMapping portMapping = new PortMapping()
                .withContainerPort(80)
                .withHostPort(80)
                .withProtocol(TransportProtocol.Tcp);
/*
        HealthCheck healthCheck = new HealthCheck()
                .withCommand("CMD-SHELL, curl http://127.0.0.1/ || exit 1")
                .withStartPeriod(30)
                .withInterval(5)
                .withTimeout(20)
                .withRetries(2);
                */

        Map<String, String> logOptions = new HashMap<>();
        logOptions.put("awslogs-region", this.getRegion());
        logOptions.put("awslogs-group", String.format(Const.TASK_LOG_GROUP_PATTERN, zeppelinInstanceID));
        logOptions.put("awslogs-stream-prefix", TASK_LOG_STREAM_PREFIX);

        LogConfiguration logConfiguration = new LogConfiguration()
                .withLogDriver("awslogs")
                .withOptions(logOptions);

        List<KeyValuePair> environmentVariables = new ArrayList<>();
        environmentVariables.add(new KeyValuePair().withName("ZEPPELIN_PORT").withValue("80"));  //zeppelin 端口
        environmentVariables.add(new KeyValuePair().withName("ZEPPELIN_SERVER_CONTEXT_PATH").withValue(String.format("/%s", zeppelinInstanceID)));//zeppelin上下文路径
        environmentVariables.add(new KeyValuePair().withName("GW_USERNAME").withValue(OAUsername));//zeppelin 登录用户名
        environmentVariables.add(new KeyValuePair().withName("GW_PASSWORD").withValue(internalPassword));//zeppelin 用户密码
        environmentVariables.add(new KeyValuePair().withName("GW_MD4XDOMAIN").withValue(domain));//zeppelin 域名
        
        if (!StringUtils.isNullOrEmpty(gitRemoteURL)) {
            environmentVariables.add(new KeyValuePair().withName("ZEPPELIN_NOTEBOOK_GIT_REMOTE_URL")
                    .withValue(gitRemoteURL));
            environmentVariables.add(new KeyValuePair().withName("ZEPPELIN_NOTEBOOK_GIT_REMOTE_USERNAME")
                    .withValue(gitUsername));
            environmentVariables.add(new KeyValuePair().withName("ZEPPELIN_NOTEBOOK_GIT_REMOTE_ACCESS_TOKEN")
                    .withValue(gitAccessToken));
            
            //notebook 存储到s3桶
            //environmentVariables.add(new KeyValuePair().withName("ZEPPELIN_NOTEBOOK_STORAGE").withValue("org.apache.zeppelin.notebook.repo.GitHubNotebookRepo"));
            environmentVariables.add(new KeyValuePair().withName("ZEPPELIN_NOTEBOOK_STORAGE").withValue("org.apache.zeppelin.notebook.repo.S3NotebookRepo"));
            environmentVariables.add(new KeyValuePair().withName("ZEPPELIN_NOTEBOOK_S3_ENDPOINT").withValue(zeppelinEntity.getZeppelinNotebookS3Endpoint()));
            environmentVariables.add(new KeyValuePair().withName("ZEPPELIN_NOTEBOOK_S3_BUCKET").withValue(zeppelinEntity.getZeppelinNotebookS3Bucket()));
            //environmentVariables.add(new KeyValuePair().withName("ZEPPELIN_NOTEBOOK_S3_USER").withValue(zeppelinEntity.getUserName()));
            environmentVariables.add(new KeyValuePair().withName("ZEPPELIN_NOTEBOOK_S3_USER").withValue(""));
            environmentVariables.add(new KeyValuePair().withName("ZEPPELIN_NOTEBOOK_RELOAD_FROM_STORAGE").withValue("true"));
        }

        ContainerDefinition containerDefinition = new ContainerDefinition()
                .withName(Const.TASK_CONTAINER_NAME)
                .withEssential(true)
                .withImage(imageUri)
                .withPortMappings(portMapping)
                //.withHealthCheck(healthCheck)
                .withStartTimeout(60)
                .withStopTimeout(120)
                .withLogConfiguration(logConfiguration)
                .withEnvironment(environmentVariables);

        String taskDefinitionFamily = getTaskDefinitionFamilyName(zeppelinInstanceID);

        RegisterTaskDefinitionRequest request = new RegisterTaskDefinitionRequest()
                .withFamily(taskDefinitionFamily)
                .withTaskRoleArn(taskRoleArn)
                .withExecutionRoleArn(taskExecutionRoleArn)
                .withNetworkMode(NetworkMode.Awsvpc)
                .withRequiresCompatibilities(Compatibility.FARGATE)
                .withCpu(taskFlavor.cpu)
                .withMemory(taskFlavor.memory)
                .withContainerDefinitions(containerDefinition);

        log.debug(String.format("register the ECS task definition family \"%s\".", taskDefinitionFamily));

        RegisterTaskDefinitionResult response = client.registerTaskDefinition(request);

        if (response.getSdkHttpMetadata().getHttpStatusCode() != 200) {
            throw new Exception(String.format("failed to register the ECS task definition family \"%s\".",
                    taskDefinitionFamily));
        }

        log.debug(String.format("the ECS task definition \"%s\" has been registered.",
                response.getTaskDefinition().getTaskDefinitionArn()));

        return response.getTaskDefinition().getTaskDefinitionArn();
    }

    private String getTaskDefinitionFamilyName(String zeppelinInstanceID) {
        return String.format("%s-%s", TASK_DEFINITION_FAMILY_PREFIX, zeppelinInstanceID);
    }
}
