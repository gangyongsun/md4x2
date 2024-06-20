package cn.com.goldwind.md4x.business.entity.zeppelin;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: SysZeppelinImage
 * @Description: 用户使用日志实体类
 * @Author: yaleiwang
 * @Date: 2020-7-21 20:54
 */
public class SysZeppelinLogs {
    private Integer id;

    private String userName;

    private String taskFlavor;

    private Integer imageId;

    private SysZeppelinImage image;

    private Date startTime;

    private Date endTime;

    private BigDecimal duration;

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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getDuration() {
        return duration;
    }

    public void setDuration(BigDecimal duration) {
        this.duration = duration;
    }

}
