package cn.com.goldwind.md4x.business.dao.zeppelin.mapper;

import cn.com.goldwind.md4x.business.entity.zeppelin.SysResultDownload;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: SysResultDownload
 * @Description: TODO
 * @Author: yaleiwang
 * @Date: 2020-9-18 16:53
 */
public interface SysResultDownloadMapper extends BaseMapper<SysResultDownload> {

    /**
     * @Author yaleiwang
     * @Description 获取download记录条数
     * @Date 2020-9-18 21:25
     * @param data
     * @return int
     **/
    int downloadCount(Map<String, Object> data);

    /**
     * @Author yaleiwang
     * @Description 分页查询用户下载记录
     * @Date 2020-9-18 21:22
     * @param data
     * @return java.util.List<cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinProject>
     **/
    List<SysResultDownload> getDownloadListInPage(Map<String, Object> data);

   /**
    * @Author yaleiwang
    * @Description 插入download record
    * @Date 2020-9-18 21:25
    * @param record
    * @return int
    **/
    int insertDownloadRecord(SysResultDownload record);

    /**
     * @Author yaleiwang
     * @Description 删除
     * @Date 2020-9-18 21:26
     * @param id
     * @return int
     **/
    int deleteRecordById(@Param("id") Integer id);

    /**
     * @Author yaleiwang
     * @Description 更新下载记录
     * @Date 2020-9-18 21:27
     * @param record
     * @return int
     **/
    int updateDownloadById(SysResultDownload record);


    /*
     * @Author yaleiwang
     * @Description 查询
     * @Date 2020-9-18 21:40
     * @param id
     * @return cn.com.goldwind.md4x.business.entity.zeppelin.SysResultDownload
     **/
    SysResultDownload selectDownloadById(@Param("id") Integer id);

}
