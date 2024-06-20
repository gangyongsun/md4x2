package cn.com.goldwind.md4x.shiro.domain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.com.goldwind.md4x.shiro.domain.entity.RolePermission;

/**
 * 
 * @author alvin
 *
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

	/**
	 * 根据角色ID列表删除权限
	 * 
	 * @param roleIdList
	 * @return
	 */
	int deleteByRoleIds(List<Integer> roleIdList);

	/**
	 * 保存角色权限关系
	 * 
	 * @param rolePermissionList
	 * @return
	 */
	int insertRolePermissions(@Param("rolePermissionList")List<RolePermission> rolePermissionList);

}
