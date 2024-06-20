package cn.com.goldwind.md4x.shiro.domain.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.com.goldwind.md4x.shiro.domain.entity.RoleGroup;

/**
 * 角色组(部门)Mapper
 * 
 * @author alvin
 *
 */
public interface RoleGroupMapper extends BaseMapper<RoleGroup> {

	/**
	 * 分页查询部门列表
	 * 
	 * @param data
	 * @return
	 */
	List<RoleGroup> getRoleGroupListInPage(Map<String, Object> data);

	/**
	 * 部门数量
	 * 
	 * @return
	 */
	int count();

}
