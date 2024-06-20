package cn.com.goldwind.md4x.business.dao.zeppelin.mapper;

import cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinProject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: SysZeppelinProjectMapper
 * @Description: project mapper
 * @Author: yaleiwang
 * @Date: 2020-8-4 15:32
 */
public interface SysZeppelinProjectMapper extends BaseMapper<SysZeppelinProject> {
    /*
     * @Author yaleiwang
     * @Description 查询用户项目数量
     * @Date 2020-8-5 15:14
     * @param data
     * @return int
     **/
    int projectCount(Map<String, Object> data);
    
    /*
     * @Author yaleiwang
     * @Description 分页查询用户项目信息
     * @Date 2020-8-5 15:18
     * @param data
     * @return java.util.List<cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinProject>
     **/
    List<SysZeppelinProject> getProjectListInPage(Map<String, Object> data);

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
    int deleteProjectById(@Param("id") Integer id);

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
    List<SysZeppelinProject> selectProjectByUserName(@Param("userName") String userName);

    /*
     * @Author yaleiwang
     * @Description 查询项目信息
     * @Date 2020-8-4 15:38
     * @param id
     * @return cn.com.goldwind.md4x.business.entity.zeppelin.SysZeppelinProject
     **/
    SysZeppelinProject selectProjectById(@Param("id") Integer id);

   /*
    * @Author yaleiwang
    * @Description 获取项目名称个数
    * @Date 2020-8-4 15:51
    * @param userName
    * @param projectName
    * @return int
    **/
    int getProjectName(@Param("userName") String userName, @Param("projectName") String projectName);
}
