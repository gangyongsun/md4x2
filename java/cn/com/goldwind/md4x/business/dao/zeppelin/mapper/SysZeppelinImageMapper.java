package cn.com.goldwind.md4x.business.dao.zeppelin.mapper;

import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: SysZeppelinImageMapper
 * @Description: TODO
 * @Author: yaleiwang
 * @Date: 2020-7-21 21:08
 */
public interface SysZeppelinImageMapper {
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
     * @Description 查询镜像
     * @Date 2020-7-21 21:14
     * @param id
     * @return cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinImage
     **/
    SysZeppelinImage selectByPrimaryKey(@Param("id") Integer id);
    
    /*
     * @Author yaleiwang
     * @Description 查询所有镜像
     * @Date 2020-7-23 0:24
     * @param 
     * @return cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinImage
     **/
    List<SysZeppelinImage> selectAllImage();
    
    /*
     * @Author yaleiwang
     * @Description 删除镜像
     * @Date 2020-7-21 21:27
     * @param id
     * @return int
     **/
    int deleteByPrimaryKey(Integer id);


}
