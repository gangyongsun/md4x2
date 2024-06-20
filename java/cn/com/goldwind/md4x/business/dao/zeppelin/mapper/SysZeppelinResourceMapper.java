package cn.com.goldwind.md4x.business.dao.zeppelin.mapper;

import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinResource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: SysZeppelinResourceMapper
 * @Description: TODO
 * @Author: yaleiwang
 * @Date: 2020-7-30 14:41
 */
public interface SysZeppelinResourceMapper extends BaseMapper<SysZeppelinResource> {
    /*
     * @Author yaleiwang
     * @Description 插入资源信息
     * @Date 2020-7-30 14:44
     * @param record
     * @return int
     **/
    int insertResource(SysZeppelinResource record);
    
    /*
     * @Author yaleiwang
     * @Description 通过id删除资源
     * @Date 2020-7-30 14:45
     * @param id
     * @return int
     **/
    int deleteResourceByid(@Param("id") Integer id);
    
    /*
     * @Author yaleiwang
     * @Description 通过用户OA删除资源
     * @Date 2020-7-30 14:48
     * @param userName
     * @return int
     **/
    int deleteResourceByUserName(@Param("userName") String userName);

    /*
     * @Author yaleiwang
     * @Description 通过id更新资源
     * @Date 2020-7-30 14:48
     * @param id
     * @return int
     **/
    int updateResourceById(SysZeppelinResource record);

    /*
     * @Author yaleiwang
     * @Description 通过用户OA查询用户资源记录
     * @Date 2020-7-30 14:49
     * @param userName
     * @return java.util.List<cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinResource>
     **/
    List<SysZeppelinResource> selectResourceByUserName(@Param("userName") String userName);
    
    /*
     * @Author yaleiwang
     * @Description 通过Id查询资源
     * @Date 2020-7-31 15:27
     * @param id
     * @return cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinResource
     **/
    SysZeppelinResource selectResourceById(@Param("id") Integer id);
    
    /*
     * @Author yaleiwang
     * @Description 查询resourceId
     * @Date 2020-7-31 22:28
     * @param userName
     * @param taskFlavor
     * @param imageId
     * @return int resourceId
     **/
    int selectResourceId(@Param("userName") String userName,@Param("taskFlavor") String taskFlavor,@Param("imageId") Integer imageId);

    /*
     * @Author yaleiwang
     * @Description 根据用户userName获取Resource个数
     * @Date 2020-7-31 17:51
     * @param userName
     * @return int
     **/
    int getCountResourceByUserName(SysZeppelinResource record);

}
