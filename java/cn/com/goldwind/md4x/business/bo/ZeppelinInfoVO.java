package cn.com.goldwind.md4x.business.bo;

import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinImage;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: ZeppelinInfoVO
 * @Description: 用户实例显示
 * @Author: yaleiwang
 * @Date: 2020-7-24 17:07
 */
public class ZeppelinInfoVO {
    private String userName;

    private String userPwd;

    private String clusterName;

    private String taskFlavor;

    private String instanceName;

    private Date updateTime;

    private BigDecimal totalMinute;

    private String bitbucketUrl;

    private String bitbucketName;

    private String instanceStatus;

    private Integer imageId;

    private SysZeppelinImage image;

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

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getTaskFlavor() {
        return taskFlavor;
    }

    public void setTaskFlavor(String taskFlavor) {
        this.taskFlavor = taskFlavor;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
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

    public String getInstanceStatus() {
        return instanceStatus;
    }

    public void setInstanceStatus(String instanceStatus) {
        this.instanceStatus = instanceStatus;
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

}
