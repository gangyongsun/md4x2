package cn.com.goldwind.md4x.shiro.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.com.goldwind.md4x.business.bo.ResultInfoDTO;
import cn.com.goldwind.md4x.mybatis.Page;
import cn.com.goldwind.md4x.shiro.DTO.RoleDTO;
import cn.com.goldwind.md4x.shiro.domain.entity.Role;
import cn.com.goldwind.md4x.shiro.service.IRoleService;
import cn.com.goldwind.md4x.shiro.service.ITokenService;
import cn.com.goldwind.md4x.util.i18n.RoleCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @author alvin
 *
 */
@Api(value = "角色接口", tags = { "角色接口" })
@RestController
@RequestMapping("role")
public class RoleController extends BaseController {

	@Autowired
	private IRoleService roleService;

	@Autowired
	private ITokenService tokenService;

	/**
	 * 查询角色列表
	 * 
	 * @return
	 */
	@ApiOperation(value = "查询角色列表", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@RequiresPermissions("role:list")
	public ResultInfoDTO<Object> listAll() {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		List<RoleDTO> list = roleService.listRoles();
		if (null != list) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(RoleCode.ROLE_LIST_SUCCESS));
			response.setData(list);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(RoleCode.ROLE_LIST_FAILED));
		}
		return response;
	}

	/**
	 * 分页查询角色列表
	 * 
	 * @param pageNO   页码
	 * @param pageSize 条数
	 * @return
	 */
	@ApiOperation(value = "分页查询角色列表", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "pageNO", value = "当前页码", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "pageSize", value = "当前页条数", required = true, dataType = "Integer") })
	@RequestMapping(value = "listpage", method = RequestMethod.GET)
	@RequiresPermissions("role:listpage")
	public ResultInfoDTO<Object> listPermissions(Integer pageNO, Integer pageSize) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		Page<RoleDTO> pager = roleService.listRoles(pageNO, pageSize);
		if (null != pager) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(RoleCode.ROLE_LIST_SUCCESS));
			response.setData(pager);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(RoleCode.ROLE_LIST_FAILED));
		}
		return response;
	}

	/**
	 * 根据用户ID查询用户拥有的角色列表
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	@ApiOperation(value = "根据用户ID查询用户拥有的角色列表", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer") })
	@RequestMapping(value = "listRolesByUserId", method = RequestMethod.GET)
	@RequiresPermissions("role:listRolesByUserId")
	public ResultInfoDTO<Object> listRolesByUserId(Integer userId) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		List<RoleDTO> list = roleService.listRolesByUserId(userId);
		if (null != list) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(RoleCode.USER_ROLE_LIST_SUCCESS));
			response.setData(list);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(RoleCode.USER_ROLE_LIST_FAILED));
		}
		return response;
	}

	/**
	 * 根据用户ID查询用户拥有的部门角色列表
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	@ApiOperation(value = "根据用户ID获取用户拥有的部门角色列表", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer") })
	@RequestMapping(value = "listGroupRolesByUserId", method = RequestMethod.GET)
	@RequiresPermissions("role:listGroupRolesByUserId")
	public ResultInfoDTO<Object> listGroupRolesByUserId(Integer userId) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		List<RoleDTO> list = roleService.listGroupRolesByUserId(userId);
		if (null != list) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(RoleCode.USERGROUP_ROLE_LIST_SUCCESS));
			response.setData(list);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(RoleCode.USERGROUP_ROLE_LIST_FAILED));
		}
		return response;
	}

	/**
	 * 根据角色组(部门)ID查询角色组(部门)拥有的角色列表
	 * 
	 * @param roleGroupId 角色组(部门)ID
	 * @return
	 */
	@ApiOperation(value = "根据角色组(部门)ID查询角色组(部门)拥有的角色列表", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "groupRoleId", value = "角色组(部门)ID", required = true, dataType = "Integer") })
	@RequestMapping(value = "listByRoleGroupId", method = RequestMethod.GET)
	@RequiresPermissions("role:listByRoleGroupId")
	public ResultInfoDTO<Object> listByRoleGroupId(Integer roleGroupId) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		List<RoleDTO> list = roleService.listRolesByRoleGroupId(roleGroupId);
		if (null != list) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(RoleCode.GROUP_ROLE_LIST_SUCCESS));
			response.setData(list);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(RoleCode.GROUP_ROLE_LIST_FAILED));
		}
		return response;
	}

	/**
	 * 添加角色
	 * 
	 * @param role 角色对象
	 * @return
	 */
	@ApiOperation(value = "添加角色", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "role", value = "角色对象", required = true, dataType = "Role") })
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@RequiresPermissions("role:add")
	public ResultInfoDTO<Object> add(@RequestBody Role role) {
		role.setAvailable(true);
		role.setDeleted(false);
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		boolean flag = roleService.save(role);
		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(RoleCode.ROLE_ADD_SUCCESS));
			response.setData(role);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(RoleCode.ROLE_ADD_FAILED));
		}
		return response;
	}

	/**
	 * 更新角色
	 * 
	 * @param role 角色对象
	 * @return
	 */
	@ApiOperation(value = "更新角色", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "role", value = "角色对象", required = true, dataType = "Role") })
	@RequestMapping(value = "update", method = RequestMethod.PUT)
	@RequiresPermissions("role:update")
	public ResultInfoDTO<Object> update(@RequestBody Role role) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();

		// 如果角色被禁用或删除，应该让拥有这个角色的用户的token立马失效
		if (null != role.getAvailable() && !role.getAvailable() || null != role.getDeleted() && !role.getDeleted()) {
			tokenService.expireTokensByRoleId(role.getRoleId());
		}

		boolean flag = roleService.saveOrUpdate(role);
		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(RoleCode.ROLE_UPDATE_SUCCESS));
			response.setData(role);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(RoleCode.ROLE_UPDATE_FAILED));
		}
		return response;

	}

	/**
	 * 删除角色
	 * 
	 * @param role 角色
	 * @return
	 */
	@ApiOperation(value = "删除角色", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "role", value = "角色", required = true, dataType = "Role") })
	@RequestMapping(value = "delete", method = RequestMethod.PUT)
	@RequiresPermissions("role:delete")
	public ResultInfoDTO<Object> delete(@RequestBody Role role) {
		role.setDeleted(true);
		role.setAvailable(false);
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		boolean flag = roleService.saveOrUpdate(role);
		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(RoleCode.ROLE_DELETE_SUCCESS));
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(RoleCode.ROLE_DELETE_FAILED));
		}
		return response;
	}

//	/**
//	 * 有没有ADMIN角色
//	 * 
//	 * @param token
//	 * @return
//	 */
//	@RequiresRoles({ "ADMIN" }) // 没有的话 AuthorizationException
//	@RequestMapping(value = "admin", method = RequestMethod.GET)
//	public ResultInfoDTO<Object> adminUser() {
//		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
//		response.setCode(ResultInfoDTO.OK);
//		response.setMessage("该用户是系统管理员！");
//		return response;
//	}
//
//	/**
//	 * 有没有END_USER角色
//	 * 
//	 * @param token
//	 * @return
//	 */
//	@RequiresRoles({ "END_USER" }) // 没有的话 AuthorizationException
//	@RequestMapping(value = "end_user", method = RequestMethod.GET)
//	public ResultInfoDTO<Object> endUser() {
//		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
//		response.setCode(ResultInfoDTO.OK);
//		response.setMessage("该用户有END_USER角色！");
//		return response;
//	}
}
