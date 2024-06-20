package cn.com.goldwind.md4x.business.service.zeppelin;

import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinResource;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName: ZeppelinResourceService
 * @Description: 用户资源Dao接口
 * @Author: yaleiwang
 * @Date: 2020-7-30 15:33
 */
public interface ZeppelinResourceService extends IService<SysZeppelinResource> {
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
    int deleteResourceByid(Integer id);

    /*
     * @Author yaleiwang
     * @Description 通过用户OA删除资源
     * @Date 2020-7-30 14:48
     * @param userName
     * @return int
     **/
    int deleteResourceByUserName(String userName);

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
    List<SysZeppelinResource> selectResourceByUserName(String userName);

    /*
     * @Author yaleiwang
     * @Description 通过Id查询资源
     * @Date 2020-7-31 15:27
     * @param id
     * @return cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinResource
     **/
    SysZeppelinResource selectResourceById(Integer id);

    /*
     * @Author yaleiwang
     * @Description 根据用户userName获取Resource个数
     * @Date 2020-7-31 17:51
     * @param userName
     * @return int
     **/
    int getCountResourceByUserName(SysZeppelinResource record);

    /*
     * @Author yaleiwang
     * @Description 查询resourceId
     * @Date 2020-7-31 22:28
     * @param userName
     * @param taskFlavor
     * @param imageId
     * @return int resourceId
     **/
    int getResourceId(String userName, String taskFlavor, Integer imageId);

}
