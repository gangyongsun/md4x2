package cn.com.goldwind.md4x.business.service.zeppelin;

import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinProject;
import cn.com.goldwind.md4x.mybatis.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName: ZeppelinProjectService
 * @Description: project service interface
 * @Author: yaleiwang
 * @Date: 2020-8-4 15:41
 */
public interface ZeppelinProjectService extends IService<SysZeppelinProject> {

    /*
     * @Author yaleiwang
     * @Description 分页查询项目信息
     * @Date 2020-8-5 16:52 
     * @param currPage
     * @param pageSize
     * @param userName
     * @return cn.com.goldwind.md4x.mybatis.Page<cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinProject>
     **/
    Page<SysZeppelinProject>  listProjects(int currPage, int pageSize, String userName);

    /**
     * @Author yaleiwang
     * @Description 插入项目信息
     * @Date 2020-8-4 15:34
     * @param record
     * @return int
     **/
    int insertProject(SysZeppelinProject record);

    /*
     * @Author yaleiwang
     * @Description 删除项目信息
     * @Date 2020-8-4 15:34
     * @param id
     * @return int
     **/
    int deleteProjectById(Integer id);

    /*
     * @Author yaleiwang
     * @Description 更新项目信息
     * @Date 2020-8-4 15:36
     * @param id
     * @return int
     **/
    int updateProjectById(SysZeppelinProject project);

    /*
     * @Author yaleiwang
     * @Description 查询用户所有项目
     * @Date 2020-8-4 15:37
     * @param userName
     * @return java.util.List<cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinProject>
     **/
    List<SysZeppelinProject> getProjectByUserName(String userName);

    /*
     * @Author yaleiwang
     * @Description 查询项目信息
     * @Date 2020-8-4 15:38
     * @param id
     * @return cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinProject
     **/
    SysZeppelinProject getProjectById(Integer id);
    
    /*
     * @Author yaleiwang
     * @Description 获取项目名称个数
     * @Date 2020-8-4 15:52 
     * @param userName
     * @param projectName
     * @return int
     **/
    int getProjectName(String userName, String projectName);
}
