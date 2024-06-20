package cn.com.goldwind.md4x.business.entity.zeppelin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @ClassName: Sys_zeppelin_resource
 * @Description: 资源表实体类
 * @Author: yaleiwang
 * @Date: 2020-7-30 11:28
 */

@Data
public class SysZeppelinResource {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableId(value = "user_name")
    private String userName;

    @TableId(value = "resource_name")
    private String resourceName;

    @TableId(value = "task_flavor")
    private String taskFlavor;

    @TableId(value = "image_id")
    private Integer imageId;

    private SysZeppelinImage image;

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public void setImage(SysZeppelinImage image) {
        this.image = image;
    }


}
