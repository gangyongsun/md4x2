package cn.com.goldwind.md4x.shiro.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.com.goldwind.md4x.shiro.domain.entity.SysToken;

/**
 * 
 * @Title: ITokenService.java
 * @Package cn.com.goldwind.md4x.shiro.service
 * @description token操作相关的Service
 * @author 孙永刚
 * @date Jul 30, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
public interface ITokenService extends IService<SysToken> {

	/**
	 * 创建token
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	Map<String, Object> createToken(Integer userId);

	/**
	 * 登出
	 * 
	 * @param token
	 */
	void logout(String token);

	/**
	 * 查询登录记录
	 * 
	 * @param accessToken
	 * @return
	 */
	SysToken findByToken(String accessToken);

	/**
	 * 根据PermissionID 使token过期
	 * 
	 * @param permissionId
	 */
	boolean expireTokensByPermissionId(Integer permissionId);

	/**
	 * 如果角色被禁用或删除，应该让拥有这个角色的用户的token立马失效
	 * 
	 * @param roleId
	 */
	boolean expireTokensByRoleId(Integer roleId);

}
