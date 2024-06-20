package cn.com.goldwind.md4x.business.dao.zeppelin.mapper;

import cn.com.goldwind.md4x.business.bo.ZeppelinInfoVO;
import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: SysZeppelinMapper
 * @Description: TODO
 * @Author: yaleiwang
 * @Date: 2020-7-19 19:06
 */
public interface SysZeppelinMapper {
    /*
     * @Author yaleiwang
     * @Description 根据用户名返回工作台信息
     * @Date 2020-7-19 20:27
     * @param userName
     * @return cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelin
     **/
    SysZeppelin selectByUserName(@Param("userName") String userName);
    
    /*
     * @Author yaleiwang
     * @Description 前端返回信息
     * @Date 2020-7-24 17:15
     * @param userName
     * @return cn.com.goldwind.md4x.business.bo.ZeppelinInfoVO
     **/
    ZeppelinInfoVO selectInfoByUserName(@Param("userName") String userName);

    /*
     * @Author yaleiwang
     * @Description 根据id返回工作台信息
     * @Date 2020-7-19 20:33
     * @param id
     * @return cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelin
     **/
    SysZeppelin selectByPrimaryKey(@Param("id") Integer id);

    /*
     * @Author yaleiwang
     * @Description 根据OA查询ARN信息
     * @Date 2020-7-23 14:01
     * @param userName OA
     * @return cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelin
     **/
    SysZeppelin selectArnByUserName(@Param("userName") String userName);

    /*
     * @Author yaleiwang
     * @Description 查询所有工作台
     * @Date 2020-7-23 14:04
     * @param
     * @return java.util.List<cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelin>
     **/
    List<SysZeppelin> selectAll();

    /*
     * @Author yaleiwang
     * @Description 根据UserName更新工作台信息
     * @Date 2020-7-19 20:53
     * @param record
     * @return int
     **/
    int updateByUserName(SysZeppelin record);

    /*
     * @Author yaleiwang
     * @Description 插入一条zeppelin工作台
     * @Date 2020-7-19 21:02
     * @param record
     * @return int
     **/
    int insertZeppelin(SysZeppelin record);

    /*
     * @Author yaleiwang
     * @Description 根据UserName删除zeppelin工作台
     * @Date 2020-7-19 21:06
     * @param UserName 用户OA
     * @return int
     **/
    int deleteByUserName(@Param("userName") String UserName);

}
