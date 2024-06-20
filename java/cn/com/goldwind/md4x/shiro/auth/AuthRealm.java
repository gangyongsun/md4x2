package cn.com.goldwind.md4x.shiro.auth;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.goldwind.md4x.shiro.DTO.RoleDTO;
import cn.com.goldwind.md4x.shiro.common.utils.TokenUtil;
import cn.com.goldwind.md4x.shiro.domain.entity.Permission;
import cn.com.goldwind.md4x.shiro.domain.entity.SysToken;
import cn.com.goldwind.md4x.shiro.domain.entity.User;
import cn.com.goldwind.md4x.shiro.service.IPermissionService;
import cn.com.goldwind.md4x.shiro.service.IRoleService;
import cn.com.goldwind.md4x.shiro.service.ITokenService;
import cn.com.goldwind.md4x.shiro.service.IUserService;
import cn.com.goldwind.md4x.util.DateUtil;

/**
 * 
 * @author alvin
 *
 */
@Component
public class AuthRealm extends AuthorizingRealm {

	@Autowired
	private ITokenService tokenService;

	@Resource
	private IUserService userService;

	@Resource
	private IRoleService roleService;

	@Resource
	private IPermissionService permissionService;

	/**
	 * 授权 获取用户的角色和权限
	 * 
	 * @param [principals]
	 * @return org.apache.shiro.authz.AuthorizationInfo
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// 1. 从 PrincipalCollection 中来获取登录用户的信息
		User user = (User) principals.getPrimaryPrincipal();
		// 2.添加角色和权限
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		// 查询用户角色，一个用户可能有多个角色
		List<RoleDTO> roles = roleService.listRolesByUserId(user.getUserId());
		for (RoleDTO role : roles) {
			// 2.1添加角色
			simpleAuthorizationInfo.addRole(role.getRoleName());
			// 根据角色查询权限
			List<Permission> permissions = permissionService.listPermissions(role.getRoleId());
			for (Permission p : permissions) {
				// 2.1.1添加权限
				simpleAuthorizationInfo.addStringPermission(p.getPermissionName());
			}
		}
		return simpleAuthorizationInfo;
	}

	/**
	 * 认证 判断token的有效性
	 * 
	 * @param [token]
	 * @return org.apache.shiro.authc.AuthenticationInfo
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// 获取token，既前端传入的token
		String accessToken = (String) token.getPrincipal();

		// 1. 根据accessToken，查询用户信息
		SysToken tokenEntity = tokenService.findByToken(accessToken);

		
		if (null != tokenEntity) {
			//当前时间
			Date nowDate=new Date();
			// 过期标志：true：过期；false：未过期
			boolean expireFlag = DateUtil.compareDate(nowDate, tokenEntity.getExpireTime());
			if (expireFlag) {
				throw new IncorrectCredentialsException("token已经过期，请重新登录！");
			}else {
				//token更新时间
				tokenEntity.setUpdateTime(nowDate);
				//token过期时间
				tokenEntity.setExpireTime(DateUtil.addMinutes(nowDate, TokenUtil.EXPIRE));
				//更新token过期时间
				tokenService.updateById(tokenEntity);
			}
		} else {
			throw new IncorrectCredentialsException("token不存在，请先登录！");
		}

		// 2. token失效
//		if (tokenEntity == null || tokenEntity.getExpireTime().before(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))) {
//			throw new IncorrectCredentialsException("token失效，请重新登录");
//		}

		// 3. 调用数据库的方法, 从数据库中查询 username 对应的用户记录
		User user = userService.getById(tokenEntity.getUserId());

		// 4. 若用户不存在, 则可以抛出 UnknownAccountException 异常
		if (user == null) {
			throw new UnknownAccountException("用户不存在!");
		}

		// 5. 根据用户的情况, 来构建 AuthenticationInfo 对象并返回. 通常使用的实现类为: SimpleAuthenticationInfo
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, accessToken, this.getName());
		return info;
	}

}
