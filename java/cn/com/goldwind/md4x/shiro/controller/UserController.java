package cn.com.goldwind.md4x.shiro.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.com.goldwind.md4x.business.bo.ResultInfoDTO;
import cn.com.goldwind.md4x.business.bo.oa.EmployeeBO;
import cn.com.goldwind.md4x.mybatis.Page;
import cn.com.goldwind.md4x.shiro.DTO.RoleDTO;
import cn.com.goldwind.md4x.shiro.domain.entity.Permission;
import cn.com.goldwind.md4x.shiro.domain.entity.Role;
import cn.com.goldwind.md4x.shiro.domain.entity.RoleGroup;
import cn.com.goldwind.md4x.shiro.domain.entity.User;
import cn.com.goldwind.md4x.shiro.domain.entity.UserRole;
import cn.com.goldwind.md4x.shiro.domain.entity.UserRoleGroup;
import cn.com.goldwind.md4x.shiro.service.IPermissionService;
import cn.com.goldwind.md4x.shiro.service.IRoleGroupService;
import cn.com.goldwind.md4x.shiro.service.IRoleService;
import cn.com.goldwind.md4x.shiro.service.IUserRoleGroupService;
import cn.com.goldwind.md4x.shiro.service.IUserRoleService;
import cn.com.goldwind.md4x.shiro.service.IUserService;
import cn.com.goldwind.md4x.util.DateUtil;
import cn.com.goldwind.md4x.util.MathUtil;
import cn.com.goldwind.md4x.util.StringUtils;
import cn.com.goldwind.md4x.util.i18n.UserCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * user前端控制器
 * 
 * @author alvin
 *
 */
@Api(value = "用户接口", tags = { "用户接口" })
@RestController
@RequestMapping("user")
public class UserController extends BaseController {

	@Autowired
	private IUserService userService;

	@Autowired
	private IRoleService roleService;

	@Autowired
	private IUserRoleService userRoleService;

	@Autowired
	private IRoleGroupService roleGroupService;

	@Autowired
	private IUserRoleGroupService userRoleGroupService;

	@Autowired
	private IPermissionService permissionService;

	/**
	 * 非金风用户部门
	 */
	private final String EXTERNAL_NON_GOLDWINDER_ROLEGROUP_CODE = "EXTERNAL_NON_GOLDWINDER_ROLEGROUP_CODE";
	private final String EXTERNAL_NON_GOLDWINDER_ROLEGROUP_NAME = "EXTERNAL_非金风科技用户部门";

	/**
	 * 创建用户所分配的默认角色的名称，根据该名称能查到角色ID；注意END_USER是数据库初始化脚本默认给的普通用户的角色名，不要随便更改
	 */
	private final String DEFAULT_ROLE_NAME = "END_USER";

	/**
	 * 查询用户角色和权限
	 * 
	 * @param httpServletRequest
	 * @return
	 */
	@ApiOperation(value = "查询用户角色和权限", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@RequestMapping(value = "info", method = RequestMethod.GET)
	@RequiresPermissions("user:info")
	public ResultInfoDTO<Object> getUserInfo(HttpServletRequest httpServletRequest) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();

		// 1.获取用户ID
		ResultInfoDTO<Integer> result = getUserIdByToken(httpServletRequest);
		List<RoleDTO> roleList = null;
		if (result.getCode().equals(ResultInfoDTO.OK)) {
			// 2.根据用户ID查询用户角色列表
			roleList = roleService.listGroupRolesByUserId(result.getData());
		} else {
			response.setCode(result.getCode());
			response.setMessage(result.getMessage());
			return response;
		}

		// 3.判断角色列表是否为空
		if (null == roleList) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(UserCode.NULL_ROLES_FOR_USER));
			return response;
		}
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("roles", roleList);
		// 3.1循环查询角色拥有的权限
		List<Permission> permissions = new ArrayList<Permission>();
		for (RoleDTO role : roleList) {
			permissions.addAll(permissionService.listPermissions(role.getRoleId()));
		}
		// 4.去重
		permissions = permissions.stream().distinct().collect(Collectors.toList());
		// 5.判断权限列表是否为空
		if (null != permissions) {
			mapData.put("permissions", permissions);
		}
		response.setCode(ResultInfoDTO.OK);
		response.setMessage(i18nService.getMessage(UserCode.LIST_USER_ROLE_PERMISSION_SUCCESS));
		response.setData(mapData);
		return response;
	}

	/**
	 * 根据条件(可以是OA号或用户名)分页查询用户列表
	 * 
	 * @param pageNO   页码
	 * @param pageSize 条数
	 * @return
	 */
	@ApiOperation(value = "分页查询用户列表", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "pageNO", value = "当前页码", required = true, dataType = "Integer"), @ApiImplicitParam(name = "pageSize", value = "当前页条数", required = true, dataType = "Integer"), @ApiImplicitParam(name = "findContent", value = "查询条件", required = true, dataType = "String"), @ApiImplicitParam(name = "goldwinder", value = "是否是金风用户", required = true, dataType = "String") })
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@RequiresPermissions("user:list")
	public ResultInfoDTO<Object> listUsers(Integer pageNO, Integer pageSize, String findContent, String goldwinder) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		Page<User> pager = userService.listUsers(pageNO, pageSize, findContent, goldwinder);
		if (null != pager) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(UserCode.LIST_USERS_SUCCESS));
			response.setData(pager);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(UserCode.LIST_USERS_FAILED));
		}
		return response;
	}

	/**
	 * 用户注册（不需要权限限定）
	 * 
	 * @return
	 */
	@ApiOperation(value = "用户注册")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "user", value = "用户对象", required = true, dataType = "User") })
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public ResultInfoDTO<Object> registerUser(@RequestBody User user) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		// ******************逻辑处理：判断用户是否存在？******************
		User user_checker = userService.getOne(new QueryWrapper<User>().eq("user_name", user.getUserName()));

		// ******************逻辑分支：用户存在，直接返回******************
		if (null != user_checker) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(UserCode.EXISTED_USER_ON_REGISTER));
			response.setData(user_checker);
			return response;
		}
		// ******************逻辑分支：用户不存在，继续处理******************

		// ==================判断是否是金风员工？==================
		// 部门编号(默认是非金风科技用户部门编码)
		String roleGroupCode = EXTERNAL_NON_GOLDWINDER_ROLEGROUP_CODE;
		// 部门名称(默认是非金风科技用户部门名称)
		String roleGroupName = EXTERNAL_NON_GOLDWINDER_ROLEGROUP_NAME;
		boolean goldwinderFlag = user.getGoldwinder();
		if (goldwinderFlag) {
			// 根据OA号获取用户部门信息
			EmployeeBO employeeBO = findEmployeeByOA(user.getUserName());
			if (null != employeeBO) {
				// 更新部门编号、名称
				roleGroupCode = employeeBO.getDeptid();
				roleGroupName = employeeBO.getDeptname();
				// 设置用户昵称
				user.setNickName(employeeBO.getUsername());
				// 设置邮箱
				user.setEmail(employeeBO.getEmail());
				// 设置中心信息
				user.setCenterCode(employeeBO.getCenterid());
				user.setCenterName(employeeBO.getCentertxt());
				// 职位
				user.setPositionCode(employeeBO.getStell());
				user.setPositionName(employeeBO.getStext());

			} else {
				response.setCode(ResultInfoDTO.FAILED);
				response.setMessage(i18nService.getMessage(UserCode.NOT_GOLDWINDER_ON_REGISTER));
				return response;
			}
		} else {
			user.setNickName("非金风科技新注册用户");
			user.setSalt(StringUtils.getRandom(20));
			user.setPassword(MathUtil.getMD5(user.getPassword() + user.getCredentialsSalt()));
		}
		// 设置所属部门编码、名称
		user.setDepartmentCode(roleGroupCode);
		user.setDepartmentName(roleGroupName);
		Date now = new Date();
		user.setCreateTime(now);
		user.setExpiredTime(DateUtil.add_days(now, 10));
		user.setAvailable(false);
		user.setDeleted(false);

		// 保存用户
		boolean userSaveFlag = userService.save(user);
		// ----用户保存成功才能进行下一步
		if (!userSaveFlag) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(UserCode.USER_SAVE_FAILED_ON_REGISTER));
			return response;
		}

		// ******************逻辑处理：判断角色组(部门)是否存在******************
		RoleGroup roleGroup = roleGroupService.getOne(new QueryWrapper<RoleGroup>().eq("role_group_code", roleGroupCode));
		boolean roleGroupSaveFlag = false;
		if (null == roleGroup) {
			// 创建角色组(部门)
			roleGroup = new RoleGroup();
			roleGroup.setRoleGroupCode(roleGroupCode);
			roleGroup.setRoleGroupName(roleGroupName);
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
			response.setMessage(i18nService.getMessage(UserCode.ROLEGROUP_SAVE_FAILED_ON_REGISTER));
			userService.removeById(user.getUserId());
			return response;
		}

		// ******************逻辑处理：保存用户角色组关系******************
		UserRoleGroup userRoleGroup = new UserRoleGroup();
		userRoleGroup.setUserId(user.getUserId());
		userRoleGroup.setRoleGroupId(roleGroup.getId());
		userRoleGroup.setAtGroup(true);
		userRoleGroup.setBeginTime(now);
		boolean userRoleGroupSaveFlag = userRoleGroupService.save(userRoleGroup);
		// ----用户部门关系保存成功才能进行下一步
		if (!userRoleGroupSaveFlag) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(UserCode.USER_ROLEGROUP_RELEATIONSHIP_SAVE_FAILED_ON_REGISTER));
			roleGroupService.removeById(roleGroup.getId());
			userService.removeById(user.getUserId());
			return response;
		}

		// ******************逻辑处理：给用户分配默认角色******************
		Role role = roleService.getOne(new QueryWrapper<Role>().eq("role_name", DEFAULT_ROLE_NAME));
		UserRole userRole = new UserRole();
		userRole.setUserId(user.getUserId());
		userRole.setRoleId(role.getRoleId());
		boolean userRoleSaveFlag = userRoleService.save(userRole);
		// ----为用户分配默认角色成功才能进行下一步
		if (!userRoleSaveFlag) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(UserCode.ROLE_ALLOCATION_FAILED_ON_REGISTER));
			userRoleGroupService.removeById(userRoleGroup.getId());
			roleGroupService.removeById(roleGroup.getId());
			userService.removeById(user.getUserId());
			return response;
		}

		/**
		 * 用户保存成功、用户部门关系保存成功、分配默认角色成功，才算注册成功
		 */
		response.setCode(ResultInfoDTO.OK);
		response.setMessage(i18nService.getMessage(UserCode.REGISTER_SUCCESSS_ON_VERIFY));
		response.setData(user);
		return response;
	}

	/**
	 * 用户删除
	 * 
	 * @return
	 */
	@ApiOperation(value = "用户删除")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "user", value = "用户对象", required = true, dataType = "User") })
	@RequestMapping(value = "delete", method = RequestMethod.PUT)
	@RequiresPermissions("user:delete")
	public ResultInfoDTO<Object> deleteUser(@RequestBody User user) {
		user.setDeleted(true);
		user.setAvailable(false);
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		boolean flag = userService.saveOrUpdate(user);
		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(UserCode.USER_DELETE_SUCCESS));
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(UserCode.USER_DELETE_FAILED));
		}
		return response;
	}

	/**
	 * 批量更新用户信息(用于更新之前注册的用户)
	 * 
	 * @param userIds
	 * @return
	 */
	@RequestMapping(value = "updateusers", method = RequestMethod.PUT)
	public ResultInfoDTO<Object> updateUsers(@RequestBody List<Integer> userIds) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		System.out.println(userIds);
		for (Integer userId : userIds) {
			User user = new User();
			user.setUserId(userId);
			response = updateUser(user);
		}
		return response;
	}

	/**
	 * 用户更新
	 * 
	 * @return
	 */
	@ApiOperation(value = "用户更新")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "user", value = "用户对象", required = true, dataType = "User") })
	@RequestMapping(value = "update", method = RequestMethod.PUT)
	@RequiresPermissions("user:update")
	public ResultInfoDTO<Object> updateUser(@RequestBody User user) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		// **********************存在性判断：start**********************
		User entity = userService.getById(user.getUserId());
		if (null == entity) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(UserCode.NONE_EXIST_USER_ON_UPDATE));
			return response;
		}
		// **********************存在性判断：end**********************

		// **********************用户更新逻辑：start**********************
		// 获取金风员工标识
		boolean goldwinderFlag = entity.getGoldwinder();
		EmployeeBO employeeBO = null;
		// 部门编号默认是非金风用户的部门编号
		String departmentCode = EXTERNAL_NON_GOLDWINDER_ROLEGROUP_CODE;
		String departmentName = EXTERNAL_NON_GOLDWINDER_ROLEGROUP_NAME;
		if (goldwinderFlag) {
			// **********************金风员工注册用户更新逻辑：start**********************
			// ===============1.根据OA号获取用户部门信息：start===============
			employeeBO = findEmployeeByOA(entity.getUserName());
			if (null == employeeBO) {
				response.setCode(ResultInfoDTO.FAILED);
				response.setMessage(i18nService.getMessage(UserCode.NONE_GOLDWINDER_ON_UPDATE));
				return response;
			}
			// ===============1.根据OA号获取用户部门信息：end===============

			// ===============2.设置用户更新内容：start===============
			// 获取用户部门编号
			departmentCode = employeeBO.getDeptid();

			// 获取用户部门名称
			departmentName = employeeBO.getDeptname();

			// 设置中心信息
			user.setCenterCode(employeeBO.getCenterid());
			user.setCenterName(employeeBO.getCentertxt());
			// 职位
			user.setPositionCode(employeeBO.getStell());
			user.setPositionName(employeeBO.getStext());

			// 设置用户昵称
			if (StringUtils.isBlank(user.getNickName())) {
				user.setNickName(employeeBO.getUsername());
			}
			// 设置邮箱
			if (StringUtils.isBlank(user.getEmail())) {
				user.setEmail(employeeBO.getEmail());
			}
			// ===============2.设置用户更新内容：end===============
			// **********************金风员工注册用户更新逻辑：end**********************
		}
		// 不管是否是金风员工，设置用户部门编号始终更新
		user.setDepartmentCode(departmentCode);
		user.setDepartmentName(departmentName);
		// 设置更新时间
		user.setUpdateTime(new Date());
		boolean flag = userService.updateById(user);
		if (!flag) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(UserCode.USER_INFO_UPDATE_FAILED));
			return response;
		}
		// **********************用户更新逻辑：end**********************

		response.setCode(ResultInfoDTO.OK);
		response.setMessage(i18nService.getMessage(UserCode.USER_UPDATE_SUCCESS));
		return response;
	}

	/**
	 * 密码重置
	 * 
	 * @param oldPassword        旧密码
	 * @param newPassword        新密码
	 * @param newPasswordRepeat  新密码重复
	 * @param httpServletRequest
	 * @return
	 */
	@ApiOperation(value = "密码重置")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "oldPassword", value = "旧密码", required = true, dataType = "String"), @ApiImplicitParam(name = "newPassword", value = "新密码", required = true, dataType = "String"), @ApiImplicitParam(name = "newPasswordRepeat", value = "新密码重复", required = true, dataType = "String") })
	@RequestMapping(value = "resetPassword", method = RequestMethod.PUT)
	@RequiresPermissions("user:resetPassword")
	public ResultInfoDTO<Object> resetPassword(String oldPassword, String newPassword, String newPasswordRepeat, HttpServletRequest httpServletRequest) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		// 设置默认代码
		response.setCode(ResultInfoDTO.FAILED);
		// 逻辑判断
		if (StringUtils.isBlank(oldPassword)) {
			response.setMessage(i18nService.getMessage(UserCode.OLD_PASSWORD_EMPTY));
			return response;
		}
		if (StringUtils.isBlank(newPassword)) {
			response.setMessage(i18nService.getMessage(UserCode.NEW_PASSWORD_EMPTY));
			return response;
		}
		if (StringUtils.isBlank(newPasswordRepeat)) {
			response.setMessage(i18nService.getMessage(UserCode.REPEAT_PASSWORD_EMPTY));
			return response;
		}
		if (!newPassword.equals(newPasswordRepeat)) {
			response.setMessage(i18nService.getMessage(UserCode.INCONSISTENT_PASSWORD));
			return response;
		}
		// 1.获取用户ID
		ResultInfoDTO<Integer> result = getUserIdByToken(httpServletRequest);
		User user = null;
		if (result.getCode().equals(ResultInfoDTO.OK)) {
			// 2.根据用户ID查询用户信息
			user = userService.getById(result.getData());
		} else {
			response.setCode(result.getCode());
			response.setMessage(result.getMessage());
			return response;
		}

		// ===============3.旧密码正确性判断：start===============
		// 输入的旧密码加密处理
		String oldEncryptPassword = MathUtil.getMD5(oldPassword + user.getCredentialsSalt());
		if (!oldEncryptPassword.equals(user.getPassword())) {
			response.setMessage(i18nService.getMessage(UserCode.INCORRECT_OLD_PASSWORD));
			return response;
		}
		// ===============3.旧密码正确性判断：end===============

		// 4.设置新密码
		user.setPassword(MathUtil.getMD5(newPassword + user.getCredentialsSalt()));
		// 5.更新用户信息
		boolean flag = userService.saveOrUpdate(user);
		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(UserCode.PASSWORD_UPDATE_SUCCESS));
		} else {
			response.setMessage(i18nService.getMessage(UserCode.PASSWORD_UPDATE_FAILED));
		}
		return response;
	}

	/**
	 * 密码强制重置(管理员使用)
	 * 
	 * @param userId 用户Id
	 * @return
	 */
	@ApiOperation(value = "密码强制重置")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@RequestMapping(value = "forceResetPassword", method = RequestMethod.PUT)
	@RequiresPermissions("user:forceResetPassword")
	public ResultInfoDTO<Object> forceResetPassword(Integer userId) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		// 设置默认代码
		response.setCode(ResultInfoDTO.FAILED);
		// 1.根据用户ID查询用户信息
		User user = userService.getById(userId);

		// 2.设置新密码
		String randomPasswordString = StringUtils.getRandom(8);
		user.setPassword(MathUtil.getMD5(randomPasswordString + user.getCredentialsSalt()));
		// 3.更新用户信息
		boolean flag = userService.saveOrUpdate(user);
		if (flag) {
			StringBuilder dataMsg = new StringBuilder();
			dataMsg.append(i18nService.getMessage(UserCode.PASSWORD_RESET_AS_USER)).append(":").append(user.getNickName()).append("[").append(user.getUserName()).append("]").append(i18nService.getMessage(UserCode.PASSWORD_RESET_AS_NEWPASSWORD)).append(":").append(randomPasswordString);
			response.setData(dataMsg.toString());
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(UserCode.PASSWORD_RESET_SUCCESS));
		} else {
			response.setMessage(i18nService.getMessage(UserCode.PASSWORD_RESET_FAILED));
		}
		return response;
	}

	/**
	 * 帮助文档
	 * 
	 * @return
	 */
	@ApiOperation(value = "帮助文档")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@RequestMapping(value = "help", method = RequestMethod.GET)
	@RequiresPermissions("user:help")
	public ResultInfoDTO<Object> help() {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		response.setCode(ResultInfoDTO.OK);
		response.setMessage(i18nService.getMessage(UserCode.HELP_DOC_SHOW));
		return response;
	}

	/**
	 * 反馈建议
	 * 
	 * @param content 反馈内容
	 * @return
	 */
	@ApiOperation(value = "反馈建议")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "content", value = "反馈内容", required = true, dataType = "String") })
	@RequestMapping(value = "suggestion", method = RequestMethod.POST)
	@RequiresPermissions("user:suggestion")
	public ResultInfoDTO<Object> suggestion(String content) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		response.setCode(ResultInfoDTO.OK);
		response.setMessage(i18nService.getMessage(UserCode.SUGGESTION_SAVED));
		return response;
	}

}
