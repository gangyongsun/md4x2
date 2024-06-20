package cn.com.goldwind.md4x.business.service.zeppelin;

import cn.com.goldwind.md4x.business.entity.zeppelin.SysResultDownload;
import cn.com.goldwind.md4x.mybatis.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: ResultDownloadService
 * @Description: TODO
 * @Author: yaleiwang
 * @Date: 2020-9-18 21:30
 */
public interface ResultDownloadService extends IService<SysResultDownload> {
   
  /**
   * @Author yaleiwang
   * @Description 分页查询
   * @Date 2020-9-21 9:51
   * @param currPage
   * @param pageSize
   * @param userName
   * @return cn.com.goldwind.md4x.mybatis.Page<cn.com.goldwind.md4x.business.entity.zeppelin.SysResultDownload>
   **/
   Page<SysResultDownload> listDownloadRecords(int currPage, int pageSize, String userName);

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
    int deleteDownloadRecordById(Integer id);

    /**
     * @Author yaleiwang
     * @Description 更新下载记录
     * @Date 2020-9-18 21:27
     * @param record
     * @return int
     **/
    int updateDownloadRecordById(SysResultDownload record);


    /*
     * @Author yaleiwang
     * @Description 查询
     * @Date 2020-9-18 21:40
     * @param id
     * @return cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinProject
     **/
    SysResultDownload selectDownloadRecordById(Integer id);
}
