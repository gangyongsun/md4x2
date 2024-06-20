package cn.com.goldwind.md4x.shiro.domain.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.com.goldwind.md4x.shiro.domain.entity.Permission;

/**
 * 
 * @author alvin
 *
 */
public interface PermissionMapper extends BaseMapper<Permission> {

	/**
	 * 根据角色ID查询权限列表
	 * 
	 * @param roleId
	 * @return
	 */
	List<Permission> listPermissions(@Param("roleId") Integer roleId);

	/**
	 * 权限数量
	 * 
	 * @return
	 */
	int permissionCount();

	/**
	 * 分页查询权限列表
	 * 
	 * @param data
	 * @return
	 */
	List<Permission> getPermissionListInPage(Map<String, Object> data);

	/**
	 * 查询权限的所有划分组，用户更新权限可以把权限分到不同的组
	 * 
	 * @return
	 */
	List<String> getPermissionGroups();
}
