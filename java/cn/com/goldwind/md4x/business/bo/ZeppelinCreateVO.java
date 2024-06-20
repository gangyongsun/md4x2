package cn.com.goldwind.md4x.business.bo;

/**
 * @ClassName: ZeppelinCreateVO
 * @Description: zeppelin create
 * @Author: yaleiwang
 * @Date: 2020-7-24 13:17
 */
public class ZeppelinCreateVO {
    private String userName;

    private String taskFlavor;

    private Integer imageId;

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

}
