package cn.com.goldwind.md4x.shiro.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.com.goldwind.md4x.mybatis.Page;
import cn.com.goldwind.md4x.shiro.DTO.RoleDTO;
import cn.com.goldwind.md4x.shiro.domain.entity.Role;

public interface IRoleService extends IService<Role> {

	/**
	 * 根据用户ID查询用户拥有的部门角色列表
	 * 
	 * @param userId 用户ID
	 * @return 用户部门角色列表
	 */
	List<RoleDTO> listGroupRolesByUserId(Integer userId);

	/**
	 * 根据角色组(部门)ID查询角色组(部门)拥有的角色列表
	 * 
	 * @param roleGroupId 角色组(部门)ID
	 * @return
	 */
	List<RoleDTO> listRolesByRoleGroupId(Integer roleGroupId);

	/**
	 * 查询有部门划分的角色列表
	 * 
	 * @return
	 */
	List<RoleDTO> listRoles();
	
	/**
	 * 分页查询角色列表
	 * 
	 * @param pageNO   页码
	 * @param pageSize 条数
	 * @return
	 */
	Page<RoleDTO> listRoles(Integer pageNO, Integer pageSize);

	/**
	 * 根据用户ID查询用户拥有的角色列表
	 * 
	 * @param userId
	 * @return
	 */
	List<RoleDTO> listRolesByUserId(Integer userId);

}
