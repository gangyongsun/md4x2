package cn.com.goldwind.md4x.business.entity.zeppelin;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: SysZeppelinImage
 * @Description: 用户当前实例实体类
 * @Author: yaleiwang
 * @Date: 2020-7-21 20:54
 */
public class  SysZeppelin {
    private Integer id;

    private String userName;

    private String userPwd;

    private String elbName;

    private String elbArn;

    private String elbListenerArn;

    private String clusterName;

    private String taskRoleArn;

    private Object taskSubnet;

    private String securityGroupId;

    private String taskFlavor;

    private Integer imageId;

    private SysZeppelinImage image;

    private String instanceName;

    private String containerId;

    private Date updateTime;

    private BigDecimal totalMinute;

    private String bitbucketUrl;

    private String bitbucketName;

    private String bitbucketPwd;

    private String instanceStatus;

    private String zeppelinUrl;

    private String ruleArn;

    private String targetGroupArn;

    private String taskArn;

    private Integer logId;
    
    private String zeppelinNotebookS3Bucket;
    private String zeppelinNotebookS3Endpoint;
  

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getElbName() {
        return elbName;
    }

    public void setElbName(String elbName) {
        this.elbName = elbName;
    }

    public String getElbArn() {
        return elbArn;
    }

    public void setElbArn(String elbArn) {
        this.elbArn = elbArn;
    }

    public String getElbListenerArn() {
        return elbListenerArn;
    }

    public void setElbListenerArn(String elbListenerArn) {
        this.elbListenerArn = elbListenerArn;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getTaskRoleArn() {
        return taskRoleArn;
    }

    public void setTaskRoleArn(String taskRoleArn) {
        this.taskRoleArn = taskRoleArn;
    }

    public Object getTaskSubnet() {
        return taskSubnet;
    }

    public void setTaskSubnet(Object taskSubnet) {
        this.taskSubnet = taskSubnet;
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
    }

    public String getTaskFlavor() {
        return taskFlavor;
    }

    public void setTaskFlavor(String taskFlavor) {
        this.taskFlavor = taskFlavor;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public SysZeppelinImage getImage() {
        return image;
    }

    public void setImage(SysZeppelinImage image) {
        this.image = image;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public BigDecimal getTotalMinute() {
        return totalMinute;
    }

    public void setTotalMinute(BigDecimal totalMinute) {
        this.totalMinute = totalMinute;
    }

    public String getBitbucketUrl() {
        return bitbucketUrl;
    }

    public void setBitbucketUrl(String bitbucketUrl) {
        this.bitbucketUrl = bitbucketUrl;
    }

    public String getBitbucketName() {
        return bitbucketName;
    }

    public void setBitbucketName(String bitbucketName) {
        this.bitbucketName = bitbucketName;
    }

    public String getBitbucketPwd() {
        return bitbucketPwd;
    }

    public void setBitbucketPwd(String bitbucketPwd) {
        this.bitbucketPwd = bitbucketPwd;
    }

    public String getInstanceStatus() {
        return instanceStatus;
    }

    public void setInstanceStatus(String instanceStatus) {
        this.instanceStatus = instanceStatus;
    }

    public String getZeppelinUrl() {
        return zeppelinUrl;
    }

    public void setZeppelinUrl(String zeppelinUrl) {
        this.zeppelinUrl = zeppelinUrl;
    }

    public String getRuleArn() {
        return ruleArn;
    }

    public void setRuleArn(String ruleArn) {
        this.ruleArn = ruleArn;
    }

    public String getTargetGroupArn() {
        return targetGroupArn;
    }

    public void setTargetGroupArn(String targetGroupArn) {
        this.targetGroupArn = targetGroupArn;
    }

    public String getTaskArn() {
        return taskArn;
    }

    public void setTaskArn(String taskArn) {
        this.taskArn = taskArn;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

	public String getZeppelinNotebookS3Bucket() {
		return zeppelinNotebookS3Bucket;
	}

	public void setZeppelinNotebookS3Bucket(String zeppelinNotebookS3Bucket) {
		this.zeppelinNotebookS3Bucket = zeppelinNotebookS3Bucket;
	}

	public String getZeppelinNotebookS3Endpoint() {
		return zeppelinNotebookS3Endpoint;
	}

	public void setZeppelinNotebookS3Endpoint(String zeppelinNotebookS3Endpoint) {
		this.zeppelinNotebookS3Endpoint = zeppelinNotebookS3Endpoint;
	}

}
