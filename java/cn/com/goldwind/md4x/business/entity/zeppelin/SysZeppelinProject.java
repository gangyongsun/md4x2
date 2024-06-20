package cn.com.goldwind.md4x.business.entity.zeppelin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: SysZeppelinProject
 * @Description: 项目信息表实体类
 * @Author: yaleiwang
 * @Date: 2020-8-4 15:22
 */
@Data
public class SysZeppelinProject {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableId(value = "user_name")
    private String userName;

    @TableId(value = "task_flavor")
    private String taskFlavor;

    @TableId(value = "image_id")
    private Integer imageId;

    @TableId(value = "project_name")
    private String projectName;

    @TableId(value = "project_des")
    private String projectDes;

    @TableId(value = "dataset_id")
    private Object datasetId;

    @TableId(value = "s3_path")
    private String s3Path;

    @TableId(value = "update_time")
    private Date updateTime;

    private SysZeppelinImage image;
}
