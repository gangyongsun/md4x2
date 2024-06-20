package cn.com.goldwind.md4x.business.service.zeppelin;

import cn.com.goldwind.md4x.business.bo.ZeppelinInfoVO;
import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelin;

import java.util.List;

/**
 * @ClassName: ZeppelinService
 * @Description: TODO
 * @Author: yaleiwang
 * @Date: 2020-7-22 10:28
 */
public interface ZeppelinInstanceService {
    /*
     * @Author yaleiwang
     * @Description TODO
     * @Date 2020-7-22 10:30
     * @param record
     * @return int
     **/
    int insertZeppelin(SysZeppelin record);
    
    /*
     * @Author yaleiwang
     * @Description TODO
     * @Date 2020-7-22 10:30
     * @param record
     * @return int
     **/
    int updateByUserName(SysZeppelin record);

    /*
     * @Author yaleiwang
     * @Description TODO
     * @Date 2020-7-22 10:32
     * @param userName
     * @return int
     **/
    int deleteByUserName(String UserName);

    /*
     * @Author yaleiwang
     * @Description TODO
     * @Date 2020-7-22 10:32
     * @param userName
     * @return cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelin
     **/
    SysZeppelin selectByUserName(String userName);

    /*
     * @Author yaleiwang
     * @Description 前端返回信息
     * @Date 2020-7-24 17:15
     * @param userName
     * @return cn.com.goldwind.md4x.business.bo.ZeppelinInfoVO
     **/
    ZeppelinInfoVO selectInfoByUserName(String userName);

    /*
     * @Author yaleiwang
     * @Description TODO
     * @Date 2020-7-22 10:33
     * @param id
     * @return cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelin
     **/
    SysZeppelin selectByPrimaryKey(Integer id);

    /*
     * @Author yaleiwang
     * @Description 根据OA查询ARN信息
     * @Date 2020-7-23 14:01
     * @param userName OA
     * @return cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelin
     **/
    SysZeppelin selectArnByUserName(String userName);

    /*
     * @Author yaleiwang
     * @Description 查询所有工作台
     * @Date 2020-7-23 14:05
     * @param
     * @return java.util.List<cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelin>
     **/
    List<SysZeppelin> selectAll();
}
