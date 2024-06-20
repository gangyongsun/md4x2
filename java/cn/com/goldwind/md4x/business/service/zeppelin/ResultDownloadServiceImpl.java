package cn.com.goldwind.md4x.business.service.zeppelin;

import cn.com.goldwind.md4x.business.dao.zeppelin.mapper.SysResultDownloadMapper;
import cn.com.goldwind.md4x.business.entity.zeppelin.SysResultDownload;
import cn.com.goldwind.md4x.mybatis.Page;
import cn.com.goldwind.md4x.util.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: ResultDownloadServiceImpl
 * @Description: TODO
 * @Author: yaleiwang
 * @Date: 2020-9-18 21:31
 */

@Service
public class ResultDownloadServiceImpl extends ServiceImpl<SysResultDownloadMapper, SysResultDownload> implements ResultDownloadService {
    @Autowired
    SysResultDownloadMapper sysResultDownloadMapper;

    @Override
    public Page<SysResultDownload> listDownloadRecords(int currPage, int pageSize, String userName) {
        Map<String, Object> data = new HashMap<String, Object>();
        if (currPage < 1) {
            currPage = 1;
        }
        data.put("currIndex", (currPage - 1) * pageSize);
        data.put("pageSize", pageSize);
        Page<SysResultDownload> page = null;

        if (StringUtils.isNotBlank(userName)) {
            data.put("userName", userName);

            int totalSize = sysResultDownloadMapper.downloadCount(data);
            List<SysResultDownload> listProject = sysResultDownloadMapper.getDownloadListInPage(data);
            page = new Page<SysResultDownload>(currPage, 0, pageSize, new ArrayList<SysResultDownload>());
            if (totalSize > 0 && listProject != null && listProject.size() > 0) {
                page.setData(listProject);
                page.setTotalCount(totalSize);
            }
        }

        return page;
    }

    @Override
    public int insertDownloadRecord(SysResultDownload record) {
        return sysResultDownloadMapper.insertDownloadRecord(record);
    }

    @Override
    public int deleteDownloadRecordById(Integer id) {
        return sysResultDownloadMapper.deleteRecordById(id);
    }

    @Override
    public int updateDownloadRecordById(SysResultDownload record) {
        return sysResultDownloadMapper.updateDownloadById(record);
    }

    @Override
    public SysResultDownload selectDownloadRecordById(Integer id) {
        return sysResultDownloadMapper.selectDownloadById(id);
    }
}
