package cn.com.goldwind.md4x.business.service.zeppelin;

import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinImage;

import java.util.List;

/**
 * @ClassName: ZeppelinImageService
 * @Description: TODO
 * @Author: yaleiwang
 * @Date: 2020-7-23 0:17
 */
public interface ZeppelinImageService {
    /*
     * @Author yaleiwang
     * @Description 插入镜像记录
     * @Date 2020-7-21 21:10
     * @param record
     * @return int
     **/
    int insertZeppelinImage(SysZeppelinImage record);

    /*
     * @Author yaleiwang
     * @Description 更新镜像
     * @Date 2020-7-21 21:12
     * @param record
     * @return int
     **/
    int updateByPrimaryKey(SysZeppelinImage record);

    /*
     * @Author yaleiwang
     * @Description TODO
     * @Date 2020-7-21 21:14
     * @param id
     * @return cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinImage
     **/
    SysZeppelinImage getImagesByPrimaryKey(Integer id);

    /*
     * @Author yaleiwang
     * @Description TODO
     * @Date 2020-7-23 0:24
     * @param
     * @return cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinImage
     **/
    List<SysZeppelinImage> getAllImage();

    /*
     * @Author yaleiwang
     * @Description TODO
     * @Date 2020-7-21 21:27
     * @param id
     * @return int
     **/
    int deleteByPrimaryKey(Integer id);
}
