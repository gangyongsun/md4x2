package cn.com.goldwind.md4x.shiro.domain.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.com.goldwind.md4x.shiro.DTO.RoleDTO;
import cn.com.goldwind.md4x.shiro.domain.entity.Role;

/**
 * 
 * @author alvin
 *
 */
public interface RoleMapper extends BaseMapper<Role> {

	/**
	 * 根据用户ID查询用户拥有的角色列表
	 * 
	 * @param userId
	 * @return
	 */
	List<RoleDTO> listRolesByUserId(Integer userId);

	/**
	 * 根据用户ID查询用户拥有的部门角色列表
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	List<RoleDTO> listGroupRolesByUserId(@Param("userId") Integer userId);

	/**
	 * 根据角色组(部门)ID查询角色组(部门)拥有的角色列表
	 * 
	 * @param roleGroupId 角色组(部门)ID
	 * @return
	 */
	List<RoleDTO> listRolesByRoleGroupId(@Param("roleGroupId") Integer roleGroupId);

	/**
	 * 角色数量
	 * 
	 * @return
	 */
	int roleCount();

	/**
	 * 分页查询角色列表
	 * 
	 * @param data
	 * @return
	 */
	List<RoleDTO> getRoleListInPage(Map<String, Object> data);

	/**
	 * 查询有部门划分的角色列表
	 * 
	 * @return
	 */
	List<RoleDTO> getRoleList();

}
