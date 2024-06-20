package cn.com.goldwind.md4x.business.service.zeppelin;

import cn.com.goldwind.md4x.business.dao.zeppelin.mapper.SysZeppelinProjectMapper;
import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinProject;
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
 * @ClassName: ZeppelinProjectServiceImpl
 * @Description: project implement class
 * @Author: yaleiwang
 * @Date: 2020-8-4 15:43
 */

@Service
public class ZeppelinProjectServiceImpl extends ServiceImpl<SysZeppelinProjectMapper,SysZeppelinProject> implements ZeppelinProjectService {
    @Autowired
    SysZeppelinProjectMapper sysZeppelinProjectMapper;

    @Override
    public Page<SysZeppelinProject> listProjects(int currPage, int pageSize, String userName) {
        Map<String, Object> data = new HashMap<String, Object>();
        if (currPage < 1) {
            currPage = 1;
        }
        data.put("currIndex", (currPage - 1) * pageSize);
        data.put("pageSize", pageSize);
        Page<SysZeppelinProject> page = null;

        if (StringUtils.isNotBlank(userName)) {
            data.put("userName", userName);

            int totalSize = sysZeppelinProjectMapper.projectCount(data);
            List<SysZeppelinProject> listProject = sysZeppelinProjectMapper.getProjectListInPage(data);
            page = new Page<SysZeppelinProject>(currPage, 0, pageSize, new ArrayList<SysZeppelinProject>());
            if (totalSize > 0 && listProject != null && listProject.size() > 0) {
                page.setData(listProject);
                page.setTotalCount(totalSize);
            }
        }

        return page;
    }

    @Override
    public int insertProject(SysZeppelinProject record) {
        return sysZeppelinProjectMapper.insertProject(record);
    }

    @Override
    public int deleteProjectById(Integer id) {
        return sysZeppelinProjectMapper.deleteProjectById(id);
    }

    @Override
    public int updateProjectById(SysZeppelinProject project) {
        return sysZeppelinProjectMapper.updateProjectById(project);
    }

    @Override
    public List<SysZeppelinProject> getProjectByUserName(String userName) {
        return sysZeppelinProjectMapper.selectProjectByUserName(userName);
    }

    @Override
    public SysZeppelinProject getProjectById(Integer id) {
        return sysZeppelinProjectMapper.selectProjectById(id);
    }

    @Override
    public int getProjectName(String userName, String projectName) {
        return sysZeppelinProjectMapper.getProjectName(userName,projectName);
    }

}
