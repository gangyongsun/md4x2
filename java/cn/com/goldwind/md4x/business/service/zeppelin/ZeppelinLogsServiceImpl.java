package cn.com.goldwind.md4x.business.service.zeppelin;

import cn.com.goldwind.md4x.business.dao.zeppelin.mapper.SysZeppelinLogsMapper;
import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinLogs;
import cn.com.goldwind.md4x.mybatis.Page;
import cn.com.goldwind.md4x.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ZeppelinLogsServiceImpl
 * @Description: TODO
 * @Author: yaleiwang
 * @Date: 2020-7-22 10:43
 */
@Service
public class ZeppelinLogsServiceImpl implements ZeppelinLogsService {
    @Autowired
    SysZeppelinLogsMapper sysZeppelinLogsMapper;

    @Override
    public int insertZeppelinLog(SysZeppelinLogs record) {
        return sysZeppelinLogsMapper.insertZeppelinLog(record);
    }

    @Override
    public int updateById(SysZeppelinLogs record) {
        return sysZeppelinLogsMapper.updateById(record);
    }

    @Override
    public List<SysZeppelinLogs> selectByUserName(String userName) {
        return sysZeppelinLogsMapper.selectByUserName(userName);
    }

    @Override
    public SysZeppelinLogs selectByPrimaryKey(Integer id) {
        return sysZeppelinLogsMapper.selectByPrimaryKey(id);
    }

    @Override
    public BigDecimal getTotalMinuteByUserName(String userName) {
        return sysZeppelinLogsMapper.getTotalMinuteByUserName(userName);
    }

    @Override
    public int deleteByUserName(String userName) {
        return sysZeppelinLogsMapper.deleteByUserName(userName);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return sysZeppelinLogsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Page<SysZeppelinLogs> listLogs(int currPage, int pageSize, String userName) {
        Map<String, Object> data = new HashMap<String, Object>();
        if (currPage < 1) {
            currPage = 1;
        }
        data.put("currIndex", (currPage - 1) * pageSize);
        data.put("pageSize", pageSize);
        Page<SysZeppelinLogs> page = null;

        if (StringUtils.isNotBlank(userName)) {
            data.put("userName", userName);

            int totalSize = sysZeppelinLogsMapper.logCount(data);
            List<SysZeppelinLogs> listLog = sysZeppelinLogsMapper.getLogListInPage(data);
            page = new Page<SysZeppelinLogs>(currPage, 0, pageSize, new ArrayList<SysZeppelinLogs>());
            if (totalSize > 0 && listLog != null && listLog.size() > 0) {
                page.setData(listLog);
                page.setTotalCount(totalSize);
            }
        }

        return page;
    }
}
