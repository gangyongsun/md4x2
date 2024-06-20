package cn.com.goldwind.md4x.business.entity.zeppelin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: SysResultDownload
 * @Description: TODO
 * @Author: yaleiwang
 * @Date: 2020-9-18 16:55
 */

@Data
public class SysResultDownload {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableId(value = "user_name")
    private String userName;

    @TableId(value = "file_name")
    private Object fileName;

    @TableId(value = "file_size")
    private BigDecimal fileSize;

    @TableId(value = "download_time")
    private Date downloadTime;

    @TableId(value = "project_id")
    private Integer projectId;

    private SysZeppelinProject project;
}
