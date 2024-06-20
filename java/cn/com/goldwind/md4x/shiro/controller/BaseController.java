package cn.com.goldwind.md4x.shiro.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.com.goldwind.md4x.business.bo.ResultInfoDTO;
import cn.com.goldwind.md4x.business.bo.oa.EmployeeBO;
import cn.com.goldwind.md4x.business.bo.oa.OrganizationPageBO;
import cn.com.goldwind.md4x.business.service.I18nService;
import cn.com.goldwind.md4x.shiro.common.utils.TokenUtil;
import cn.com.goldwind.md4x.shiro.domain.entity.RoleGroup;
import cn.com.goldwind.md4x.shiro.domain.entity.SysToken;
import cn.com.goldwind.md4x.shiro.domain.entity.User;
import cn.com.goldwind.md4x.shiro.domain.entity.UserRoleGroup;
import cn.com.goldwind.md4x.shiro.service.IRoleGroupService;
import cn.com.goldwind.md4x.shiro.service.ITokenService;
import cn.com.goldwind.md4x.shiro.service.IUserRoleGroupService;
import cn.com.goldwind.md4x.shiro.service.IUserRoleService;
import cn.com.goldwind.md4x.shiro.service.IUserService;
import cn.com.goldwind.md4x.util.DateUtil;
import cn.com.goldwind.md4x.util.OAAuthUtil;
import cn.com.goldwind.md4x.util.i18n.BaseCode;

public class BaseController {
	@Value("${oa.username}")
	private String oaUsername;

	@Value("${oa.password}")
	private String oaPassword;

	@Value("${oa.url}")
	private String oaURL;

	@Value("${authdns.baseDN}")
	private String baseDN;

	@Value("${authdns.bindDN}")
	private String bindDN;

	@Value("${authdns.bindPWD}")
	private String bindPWD;

	@Value("${authdns.url}")
	private String ldapUrl;

	@Value("${oa.get-employee-url}")
	private String employeeUrl;

	@Autowired
	private ITokenService tokenService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IUserRoleService userRoleService;

	@Autowired
	private IRoleGroupService roleGroupService;

	@Autowired
	private IUserRoleGroupService userRoleGroupService;

	@Autowired
	protected I18nService i18nService;

	protected static final String DATA_SCOPE_PRIVATE = "private";

	/**
	 * 从request里获取token然后查询用户ID
	 * 
	 * @param httpServletRequest
	 * @return
	 */
	protected ResultInfoDTO<Integer> getUserIdByToken(HttpServletRequest httpServletRequest) {
		ResultInfoDTO<Integer> response = new ResultInfoDTO<Integer>();
		// 1.从request里获取token
		String token = TokenUtil.getRequestToken(httpServletRequest);
		// 2.根据request里携带的token去数据库查询
		SysToken sysToken = tokenService.findByToken(token);
		boolean flag = DateUtil.compareDate(sysToken.getExpireTime(), new Date());
		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(BaseCode.GET_ID_BY_TOKEN_SUCCESS));
			response.setData(sysToken.getUserId());
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(BaseCode.TOKEN_EXPIRE_ON_USERID_GET));
			response.setData(null);
		}
		return response;
	}

	/**
	 * 根据token获取OA号或用户名
	 * 
	 * @param httpServletRequest
	 * @return
	 */
	protected ResultInfoDTO<String> getUserNameByToken(HttpServletRequest httpServletRequest) {
		ResultInfoDTO<String> response = new ResultInfoDTO<String>();
		// 1.从request里获取token
		String token = TokenUtil.getRequestToken(httpServletRequest);
		// 2.根据request里携带的token去数据库查询
		SysToken sysToken = tokenService.findByToken(token);
		boolean flag = DateUtil.compareDate(sysToken.getExpireTime(), new Date());
		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(BaseCode.GET_USERNAME_BY_TOKEN_SUCCESS));
			response.setData(userService.getById(sysToken.getUserId()).getUserName());
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(BaseCode.TOKEN_EXPIRE_ON_USERNAME_GET));
			response.setData(null);
		}
		return response;
	}

	/**
	 * OA登陆认证
	 * 
	 * @param userName OA号
	 * @param password 密码
	 * @return
	 */
//	protected String authAtOA(String userName, String password) {
//		Map<String, Object> map = new HashMap<>();
//		map.put("userName", userName);
//		map.put("password", password);
//		return AuthOANetConnectUtil.authClient(oaUsername, oaPassword, oaURL, map);
//	}

	/**
	 * OA登陆认证
	 * 
	 * @param userName OA号
	 * @param password 密码
	 * @return
	 */
	protected Boolean authLDAP(String userName, String password) {
		return OAAuthUtil.checkingUser(ldapUrl, baseDN, bindDN, bindPWD, userName.trim(), password);
	}

	/**
	 * 根据用户oa号获取用户部门信息
	 * 
	 * @param userName oa号
	 * @return EmployeeBO
	 */
	protected EmployeeBO findEmployeeByOA(String userName) {
		EmployeeBO ebo = null;
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("param", userName);
			// 发起请求获取员工信息
			String employee = OAAuthUtil.authClient(oaUsername, oaPassword, employeeUrl, map);
			OrganizationPageBO org = JSON.parseObject(employee, OrganizationPageBO.class);
			if (!org.getCode().equals("001")) {
				ebo = org.getEmployees().get(0);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return ebo;
	}

	/**
	 * 检查并按情况更新用户和用户组(部门)的关系
	 * 
	 * @param userId
	 * @return
	 */
	protected ResultInfoDTO<Object> checkAndUpdateUserRoleGroupReleationship(Integer userId) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		response.setCode(ResultInfoDTO.OK);

		// **********************存在性判断：start**********************
		User entity = userService.getById(userId);
		if (null == entity) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(BaseCode.USER_NOT_EXIST));
			return response;
		}
		// **********************存在性判断：end**********************

		// **********************用户更新逻辑：start**********************
		EmployeeBO employeeBO = null;
		// ===============1.根据OA号获取用户部门信息：start===============
		employeeBO = findEmployeeByOA(entity.getUserName());
		if (null == employeeBO) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(BaseCode.NONE_GOLDWINDER));
			return response;
		}
		// ===============1.根据OA号获取用户部门信息：end===============

		// ******************逻辑处理：判断角色组(部门)是否存在，不存在则创建：start******************
		RoleGroup roleGroup = roleGroupService.getOne(new QueryWrapper<RoleGroup>().eq("role_group_code", employeeBO.getDeptid()));
		boolean roleGroupSaveFlag = false;
		if (null == roleGroup) {
			// 创建角色组(部门)
			roleGroup = new RoleGroup();
			roleGroup.setRoleGroupCode(employeeBO.getDeptid());
			roleGroup.setRoleGroupName(employeeBO.getDeptname());
			roleGroup.setDeleted(false);
			roleGroup.setAvailable(true);
			// 保存角色组(部门)
			roleGroupSaveFlag = roleGroupService.save(roleGroup);
		} else {
			roleGroupSaveFlag = true;
		}
		// ----部门保存成功才能进行下一步
		if (!roleGroupSaveFlag) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(BaseCode.DEPARTMENT_SAVE_FAILED));
			return response;
		}
		// ******************逻辑处理：判断角色组(部门)是否存在，不存在则创建：end******************

		// ******************逻辑处理：根据userId和atGroup=true判断用户角色组关系是否存在：start******************
		UserRoleGroup userRoleGroup = userRoleGroupService.getOne(new QueryWrapper<UserRoleGroup>().eq("user_id", userId).eq("at_group", true));
		if (null != userRoleGroup) {
			// 根据用户目前所在的角色组(部门)的Id查询角色组(部门)信息
			RoleGroup currentRoleGroup = roleGroupService.getById(userRoleGroup.getRoleGroupId());
			// *******如果用户在数据库的所在部门和现在查出的部门不一样，就要备份更新之前的部门，也要添加现在的部门关系*******
			if (null != userRoleGroup && !currentRoleGroup.getRoleGroupCode().equals(employeeBO.getDeptid())) {
				// ******************备份更新老部门关系：start******************
				// 设置用户部门关系为false
				userRoleGroup.setAtGroup(false);
				// 设置结束时间
				userRoleGroup.setEndTime(new Date());
				boolean backupFlag = userRoleGroupService.updateById(userRoleGroup);
				if (!backupFlag) {
					response.setCode(ResultInfoDTO.FAILED);
					response.setMessage(i18nService.getMessage(BaseCode.OLD_RELEATIONSHIP_UPDATE_FAILED));
					return response;
				}
				// ******************备份更新老部门关系：end******************

				// ******************添加新部门关系：start******************
				boolean userRoleGroupSaveFlag = addNewUserRoleGroupReleationship(userId, roleGroup.getId());
				// 用户部门新关系保存成功才能进行下一步
				if (!userRoleGroupSaveFlag) {
					response.setCode(ResultInfoDTO.FAILED);
					response.setMessage(i18nService.getMessage(BaseCode.NEW_RELEATIONSHIP_SAVE_SUCCESS));
					return response;
				}
				// ******************添加新部门关系：end******************

				// ******************清空用户的原部门角色：start******************
				boolean clearFlag = userRoleService.removeUserGroupRolesByUserId(userId);
				if (!clearFlag) {
					response.setCode(ResultInfoDTO.FAILED);
					response.setMessage(i18nService.getMessage(BaseCode.PREVIOUS_DEPARTMENT_ROLES_CLEAR_FAILED));
					return response;
				}
				// ******************清空用户的原部门角色：end******************

			}
		} else {
			// ******************添加新部门关系：start******************
			boolean userRoleGroupSaveFlag = addNewUserRoleGroupReleationship(userId, roleGroup.getId());
			// 用户部门新关系保存成功才能进行下一步
			if (!userRoleGroupSaveFlag) {
				response.setCode(ResultInfoDTO.FAILED);
				response.setMessage(i18nService.getMessage(BaseCode.NEW_RELEATIONSHIP_SAVE_FAILED));
				return response;
			}
			// ******************添加新部门关系：end******************
		}
		return response;
	}

	/**
	 * 添加用户部门新关系
	 * 
	 * @param userId
	 * @param roleGroupId
	 * @return
	 */
	private boolean addNewUserRoleGroupReleationship(Integer userId, Integer roleGroupId) {
		UserRoleGroup newUserRoleGroup = new UserRoleGroup();
		newUserRoleGroup.setUserId(userId);
		newUserRoleGroup.setRoleGroupId(roleGroupId);
		newUserRoleGroup.setAtGroup(true);
		newUserRoleGroup.setBeginTime(new Date());
		boolean userRoleGroupSaveFlag = userRoleGroupService.save(newUserRoleGroup);
		return userRoleGroupSaveFlag;
	}

}
