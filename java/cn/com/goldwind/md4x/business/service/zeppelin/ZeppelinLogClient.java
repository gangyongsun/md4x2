package cn.com.goldwind.md4x.business.service.zeppelin;

import com.amazonaws.services.logs.AWSLogs;
import com.amazonaws.services.logs.AWSLogsClientBuilder;
import com.amazonaws.services.logs.model.*;
import com.amazonaws.util.StringUtils;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZeppelinLogClient extends BaseClient {
    private static final Logger log = LoggerFactory.getLogger("zeppelin-controller-logging-client");

    private final AWSLogs logsClient;

    public ZeppelinLogClient() {
        this.logsClient = AWSLogsClientBuilder.standard().build();
    }

    public boolean logGroupExists(String zeppelinInstanceID) {
        Preconditions.checkState(!StringUtils.isNullOrEmpty(zeppelinInstanceID));

        String logGroupName = String.format(Const.TASK_LOG_GROUP_PATTERN, zeppelinInstanceID);

        DescribeLogGroupsRequest request = new DescribeLogGroupsRequest();
        request.setLimit(1);
        request.setLogGroupNamePrefix(logGroupName);

        DescribeLogGroupsResult response = this.logsClient.describeLogGroups(request);

        return response.getLogGroups().size() > 0;
    }


    public void createLogGroup(String zeppelinInstanceID) throws Exception {
        Preconditions.checkState(!StringUtils.isNullOrEmpty(zeppelinInstanceID));

        String logGroupName = String.format(Const.TASK_LOG_GROUP_PATTERN, zeppelinInstanceID);

        CreateLogGroupRequest request = new CreateLogGroupRequest();
        request.setLogGroupName(logGroupName);

        log.debug(String.format("create the ECS task log group \"%s\".", logGroupName));

        CreateLogGroupResult response = this.logsClient.createLogGroup(request);

        if (response.getSdkHttpMetadata().getHttpStatusCode() != 200) {
            throw new Exception(String.format("failed to create the ECS task log group \"%s\".", logGroupName));
        }

        log.debug(String.format("the ECS task log group \"%s\" has been created.", logGroupName));
    }

    public void deleteLogGroup(String zeppelinInstanceID) throws Exception {
        Preconditions.checkState(!StringUtils.isNullOrEmpty(zeppelinInstanceID));

        String logGroupName = String.format(Const.TASK_LOG_GROUP_PATTERN, zeppelinInstanceID);

        DeleteLogGroupRequest request = new DeleteLogGroupRequest();
        request.setLogGroupName(logGroupName);

        log.debug(String.format("delete the ECS task log group \"%s\".", logGroupName));

        DeleteLogGroupResult response = this.logsClient.deleteLogGroup(request);

        if (response.getSdkHttpMetadata().getHttpStatusCode() != 200) {
            throw new Exception(String.format("failed to delete the ECS task log group \"%s\".", logGroupName));
        }

        log.debug(String.format("the ECS task log group \"%s\" has been deleted.", logGroupName));
    }
}
