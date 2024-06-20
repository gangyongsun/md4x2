package cn.com.goldwind.md4x.business.service.zeppelin;

import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinLogs;
import cn.com.goldwind.md4x.mybatis.Page;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName: ZeppelinLogsService
 * @Description: logs service interface
 * @Author: yaleiwang
 * @Date: 2020-7-22 10:42
 */
public interface ZeppelinLogsService {
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
    List<SysZeppelinLogs> selectByUserName(String userName);

    /*
     * @Author yaleiwang
     * @Description 根据id查询zeppelinLog
     * @Date 2020-7-19 21:32
     * @param id
     * @return cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinLogs
     **/
    SysZeppelinLogs selectByPrimaryKey(Integer id);

    /**
     * @Author yaleiwang
     * @Description 根据OA计算总小时数
     * @Date 2020-7-23 21:03
     * @param userName OA
     * @return java.math.BigDecimal
     **/
    BigDecimal getTotalMinuteByUserName(String userName);

    /*
     * @Author yaleiwang
     * @Description 根据userName删除zeppelinLog
     * @Date 2020-7-19 22:30
     * @param userName
     * @return int
     **/
    int deleteByUserName(String userName);

    /*
     * @Author yaleiwang
     * @Description 根据id删除zeppelinLog
     * @Date 2020-7-19 22:33
     * @param id
     * @return int
     **/
    int deleteByPrimaryKey(Integer id);
    
    /*
     * @Author yaleiwang
     * @Description 分页查询实例日志信息
     * @Date 2020-8-6 10:11 
     * @param pageNO
     * @param pageSize
     * @param userName
     * @return cn.com.goldwind.md4x.mybatis.Page<cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinLogs>
     **/
    Page<SysZeppelinLogs> listLogs(int currPage, int pageSize, String userName);
}
