package cn.com.goldwind.md4x.shiro.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.com.goldwind.md4x.shiro.domain.entity.RolePermission;

public interface IRolePermissionService extends IService<RolePermission> {

	/**
	 * 根据角色ID列表清空权限
	 * 
	 * @param roleIdList
	 * @return
	 */
	boolean removeByRoleIdList(List<Integer> roleIdList);
	
	/**
	 * 保存角色权限关系
	 * 
	 * @param rolePermissionList
	 * @return
	 */
	boolean saveRolePermissions(List<RolePermission> rolePermissionList);

}
