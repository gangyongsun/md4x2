package cn.com.goldwind.md4x.business.dao.zeppelin.mapper;

import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinLogs;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: SysZeppelinLogsMapper
 * @Description: TODO
 * @Author: yaleiwang
 * @Date: 2020-7-19 21:08
 */
public interface SysZeppelinLogsMapper {
    /*
     * @Author yaleiwang
     * @Description 插入一条zeppelin工作台日志
     * @Date 2020-7-19 21:13
     * @param record
     * @return int
     **/
    int insertZeppelinLog(SysZeppelinLogs record);
    
    /**
     * @Author yaleiwang
     * @Description 根据id更新EndTime
     * @Date 2020-7-19 21:28
     * @param record
     * @return int
     **/
    int updateById(SysZeppelinLogs record);
    
    /*
     * @Author yaleiwang
     * @Description 根据用户OA查询所有zeppelin工作台启停日志
     * @Date 2020-7-23 14:07
     * @param userName OA
     * @return java.util.List<cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinLogs>
     **/
    List<SysZeppelinLogs> selectByUserName(@Param("userName") String userName);

    /**
     * @Author yaleiwang
     * @Description 根据OA计算总小时数
     * @Date 2020-7-23 21:03
     * @param userName OA
     * @return java.math.BigDecimal
     **/
    BigDecimal getTotalMinuteByUserName(@Param("userName") String userName);
    
    /*
     * @Author yaleiwang
     * @Description 根据id查询zeppelinLog
     * @Date 2020-7-19 21:32 
     * @param id
     * @return cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinLogs
     **/
    SysZeppelinLogs selectByPrimaryKey(@Param("id") Integer id);

    /*
     * @Author yaleiwang
     * @Description 根据userName删除zeppelinLog
     * @Date 2020-7-19 22:30
     * @param userName
     * @return int
     **/
    int deleteByUserName(@Param("userName") String userName);
    
    /*
     * @Author yaleiwang
     * @Description 根据id删除zeppelinLog
     * @Date 2020-7-19 22:33
     * @param id
     * @return int
     **/
    int deleteByPrimaryKey(@Param("id") Integer id);
    
    /*
     * @Author yaleiwang
     * @Description 查询用户实例日志数量
     * @Date 2020-8-6 10:19
     * @param data
     * @return int
     **/
    int logCount(Map<String, Object> data);

    /*
     * @Author yaleiwang
     * @Description 分页获取用户实例日志信息
     * @Date 2020-8-6 10:21
     * @param data
     * @return java.util.List<cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinProject>
     **/
    List<SysZeppelinLogs> getLogListInPage(Map<String, Object> data);
}
