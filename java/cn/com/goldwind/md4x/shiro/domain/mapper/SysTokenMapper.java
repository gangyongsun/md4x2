package cn.com.goldwind.md4x.shiro.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.com.goldwind.md4x.shiro.domain.entity.SysToken;

/**
 * 
 * @author alvin
 *
 */
public interface SysTokenMapper extends BaseMapper<SysToken> {

	/**
	 * 根据PermissionID 使token过期
	 * 
	 * @param permissionId
	 * @return
	 */
	int expireTokensByPermissionId(Integer permissionId);

	/**
	 * 如果角色被禁用或删除，应该让拥有这个角色的用户的token立马失效
	 * 
	 * @param roleId
	 * @return 
	 */
	int expireTokensByRoleId(Integer roleId);

}
