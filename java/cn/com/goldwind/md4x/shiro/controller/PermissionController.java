package cn.com.goldwind.md4x.shiro.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.com.goldwind.md4x.business.bo.ResultInfoDTO;
import cn.com.goldwind.md4x.mybatis.Page;
import cn.com.goldwind.md4x.shiro.domain.entity.Permission;
import cn.com.goldwind.md4x.shiro.service.IPermissionService;
import cn.com.goldwind.md4x.shiro.service.ITokenService;
import cn.com.goldwind.md4x.util.i18n.PermissionCode;
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
@Api(value = "权限接口", tags = { "权限接口" })
@RestController
@RequestMapping("permission")
public class PermissionController extends BaseController {
	@Autowired
	private IPermissionService permissionService;

	@Autowired
	private ITokenService tokenService;

	/**
	 * 查询权限列表
	 * 
	 * @return
	 */
	@ApiOperation(value = "查询权限列表", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@RequiresPermissions("permission:list")
	public ResultInfoDTO<Object> listAll() {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		List<Permission> list = permissionService.list(new QueryWrapper<Permission>().eq("deleted", false));
		if (null != list) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(PermissionCode.PERMISSION_LIST_SUCCESS));
			response.setData(list);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(PermissionCode.PERMISSION_LIST_FAILED));
		}
		return response;
	}

	/**
	 * 分页查询权限列表
	 * 
	 * @param pageNO   页码
	 * @param pageSize 条数
	 * @return
	 */
	@ApiOperation(value = "分页查询权限列表", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "pageNO", value = "当前页码", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "pageSize", value = "当前页条数", required = true, dataType = "Integer") })
	@RequestMapping(value = "listpage", method = RequestMethod.GET)
	@RequiresPermissions("permission:listpage")
	public ResultInfoDTO<Object> listPermissions(Integer pageNO, Integer pageSize) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		Page<Permission> pager = permissionService.listPermissions(pageNO, pageSize);
		if (null != pager) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(PermissionCode.PERMISSION_LIST_SUCCESS));
			response.setData(pager);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(PermissionCode.PERMISSION_LIST_FAILED));
		}
		return response;
	}

	/**
	 * 根据角色ID查询角色拥有的权限列表
	 * 
	 * @param roleId 权限ID
	 * @return
	 */
	@ApiOperation(value = "根据角色ID查询角色拥有的权限列表", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "roleId", value = "权限ID", required = true, dataType = "Integer") })
	@RequestMapping(value = "listByRoleId", method = RequestMethod.GET)
	@RequiresPermissions("permission:listByRoleId")
	public ResultInfoDTO<Object> list(Integer roleId) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		List<Permission> list = permissionService.listPermissions(roleId);
		if (null != list) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(PermissionCode.ROLE_PERMISSION_LIST_SUCCESS));
			response.setData(list);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(PermissionCode.ROLE_PERMISSION_LIST_FAILED));
		}
		return response;
	}

	/**
	 * 查询权限的所有划分组，用户更新权限可以把权限分到不同的组
	 * 
	 * @return
	 */
	@ApiOperation(value = "查询权限的所有划分组，用户更新权限可以把权限分到不同的组", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@RequestMapping(value = "groups", method = RequestMethod.GET)
	@RequiresPermissions("permission:groups")
	public ResultInfoDTO<Object> getPermissionGroups() {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		List<String> groups = permissionService.getPermissionGroups();
		if (null != groups) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(PermissionCode.PERMISSION_GROUPS_LIST_SUCCESS));
			response.setData(groups);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(PermissionCode.PERMISSION_GROUPS_LIST_FAILED));
		}
		return response;
	}

	/**
	 * 添加权限
	 * 
	 * @param permission 权限对象
	 * @return
	 */
	@ApiOperation(value = "添加权限", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "permission", value = "权限对象", required = true, dataType = "Permission") })
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@RequiresPermissions("permission:add")
	public ResultInfoDTO<Object> add(@RequestBody Permission permission) {
		permission.setAvailable(true);
		permission.setDeleted(false);
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		boolean flag = permissionService.save(permission);
		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(PermissionCode.PERMISSION_ADD_SUCCESS));
			response.setData(permission);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(PermissionCode.PERMISSION_ADD_FAILED));
		}
		return response;

	}

	/**
	 * 更新权限
	 * 
	 * @param permission 权限对象
	 * @return
	 */
	@ApiOperation(value = "更新权限", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "permission", value = "权限对象", required = true, dataType = "Permission") })
	@RequestMapping(value = "update", method = RequestMethod.PUT)
	@RequiresPermissions("permission:update")
	public ResultInfoDTO<Object> update(@RequestBody Permission permission) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();

		// 如果权限被禁用或删除，应该让拥有这个权限的用户的token立马失效
		if (null != permission.getAvailable() && !permission.getAvailable() || null != permission.getDeleted() && !permission.getDeleted()) {
			tokenService.expireTokensByPermissionId(permission.getPermissionId());
		}

		boolean flag = permissionService.saveOrUpdate(permission);
		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(PermissionCode.PERMISSION_UPDATE_SUCCESS));
			response.setData(permission);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(PermissionCode.PERMISSION_UPDATE_FAILED));
		}
		return response;

	}

	/**
	 * 删除权限
	 * 
	 * @param idList 权限ID列表
	 * @return
	 */
	@ApiOperation(value = "删除权限", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "idList", value = "权限ID列表", required = true, dataType = "List<Integer>") })
	@RequestMapping(value = "delete", method = RequestMethod.PUT)
	@RequiresPermissions("permission:delete")
	public ResultInfoDTO<Object> delete(@RequestBody Permission permission) {
		permission.setDeleted(true);
		permission.setAvailable(false);
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		boolean flag = permissionService.saveOrUpdate(permission);
		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(PermissionCode.PERMISSION_DELETE_SUCCESS));
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(PermissionCode.PERMISSION_DELETE_FAILED));
		}
		return response;
	}

}
