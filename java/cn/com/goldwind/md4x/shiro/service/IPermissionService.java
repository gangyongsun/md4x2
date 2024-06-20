package cn.com.goldwind.md4x.shiro.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.com.goldwind.md4x.mybatis.Page;
import cn.com.goldwind.md4x.shiro.domain.entity.Permission;

public interface IPermissionService extends IService<Permission> {

	/**
	 * 根据角色ID查询权限列表
	 * 
	 * @param roleId 角色ID
	 * @return
	 */
	List<Permission> listPermissions(Integer roleId);

	/**
	 * 分页查询权限列表
	 * 
	 * @param pageNO   页码
	 * @param pageSize 页码条数
	 * @return
	 */
	Page<Permission> listPermissions(Integer pageNO, Integer pageSize);

	/**
	 * 查询权限的所有划分组，用户更新权限可以把权限分到不同的组
	 * 
	 * @return
	 */
	List<String> getPermissionGroups();
}
