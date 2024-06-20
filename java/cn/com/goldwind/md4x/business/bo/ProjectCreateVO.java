package cn.com.goldwind.md4x.business.bo;

/**
 * @ClassName: ProjectCreateVO
 * @Description: 前端传入项目创建信息
 * @Author: yaleiwang
 * @Date: 2020-8-4 16:56
 */
public class ProjectCreateVO {
    private String taskFlavor;

    private Integer imageId;

    private String projectName;

    private String projectDes;

    private String datasetId;

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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDes() {
        return projectDes;
    }

    public void setProjectDes(String projectDes) {
        this.projectDes = projectDes;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }
}
