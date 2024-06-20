package cn.com.goldwind.md4x.shiro.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.com.goldwind.md4x.business.bo.ResultInfoDTO;
import cn.com.goldwind.md4x.shiro.domain.entity.RolePermission;
import cn.com.goldwind.md4x.shiro.service.IRolePermissionService;
import cn.com.goldwind.md4x.shiro.service.IRoleService;
import cn.com.goldwind.md4x.util.i18n.RolePermissionCode;
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
@Api(value = "角色权限关系接口", tags = { "角色权限关系接口" })
@RestController
@RequestMapping("rolePermission")
public class RolePermissionController extends BaseController {

	@Autowired
	private IRoleService roleService;

	@Autowired
	private IRolePermissionService rolePermissionService;

	/**
	 * 为角色赋权限
	 * 
	 * @param permissionIdList 权限ID列表
	 * @param roleId           角色ID
	 * @return
	 */
	@ApiOperation(value = "为角色赋权限", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "paramMap", value = "权限ID列表", required = true, dataType = "map") })
	@RequestMapping(value = "grantPermission2Role", method = RequestMethod.POST)
	@RequiresPermissions("rolePermission:grantPermission2Role")
	public ResultInfoDTO<Object> grantPermission2Role(@RequestBody Map<String, Object> paramMap) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		// 1.解析paramMap，获取用角色ID和权限ID列表
		List<Integer> permissionIdList = null;
		Integer roleId = null;
		try {
			permissionIdList = (List<Integer>) paramMap.get("permissionIdList");
			roleId = Integer.parseInt(paramMap.get("roleId").toString());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(RolePermissionCode.API_PARAMATER_ERROR_ON_GRANT_PERMISSION_TO_ROLE));
			return response;
		}

		// 2.根据角色ID查询角色是否是删除状态
		boolean delFlag = roleService.getById(roleId).getDeleted();
		if (delFlag) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(RolePermissionCode.CANNOT_GRANT_PERMISSION_ON_ROLE_DELETED));
			return response;
		}

		// 3.先判断角色是否有权限，有权限就清空后进行赋权限，无权限就直接赋
		int count = rolePermissionService.count(new QueryWrapper<RolePermission>().eq("roleId", roleId));
		boolean clearFlag = false;
		if (count > 0) {
			// 先清空角色的权限
			clearFlag = rolePermissionService.remove(new QueryWrapper<RolePermission>().eq("roleId", roleId));
		} else {
			clearFlag = true;
		}
		// 4. 进行赋权限
		if (clearFlag) {
			// 4.1.角色可用的情况：构造角色ID和权限ID的对象数组
			boolean flag = false;
			if (null != permissionIdList && permissionIdList.size() > 0) {
				List<RolePermission> rolePermissionList = new ArrayList<RolePermission>();
				for (Integer permissionId : permissionIdList) {
					RolePermission rolePermission = new RolePermission();
					rolePermission.setRoleId(roleId);
					rolePermission.setPermissionId(permissionId);
					rolePermissionList.add(rolePermission);
				}
				// 4.2.批量插入
				// flag = rolePermissionService.saveBatch(rolePermissionList);
				flag = rolePermissionService.saveRolePermissions(rolePermissionList);
			} else {
				flag = true;
			}
			if (flag) {
				response.setCode(ResultInfoDTO.OK);
				response.setMessage(i18nService.getMessage(RolePermissionCode.GRANT_PERMISSION_TO_ROLE_SUCCESS));
			} else {
				response.setCode(ResultInfoDTO.FAILED);
				response.setMessage(i18nService.getMessage(RolePermissionCode.GRANT_PERMISSION_TO_ROLE_FAILED));
			}
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(RolePermissionCode.GRANT_PERMISSION_TO_ROLE_FAILED_FOR_CLEAR_OLD_FAILED));
		}
		return response;
	}

	/**
	 * 根据角色ID清空权限
	 * 
	 * @param roleIdList 角色ID列表
	 * @return
	 */
	@ApiOperation(value = "根据角色ID清空权限", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "roleIdList", value = "角色ID", required = true, dataType = "list") })
	@RequestMapping(value = "clearRolePermissions", method = RequestMethod.DELETE)
	@RequiresPermissions("rolePermission:clearRolePermissions")
	public ResultInfoDTO<Object> clearRolePermissions(@RequestBody List<Integer> roleIdList) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		boolean flag = rolePermissionService.removeByRoleIdList(roleIdList);
		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(RolePermissionCode.CLEAR_ROLE_PERMISSION_SUCCESS));
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(RolePermissionCode.CLEAR_ROLE_PERMISSION_FAILED));
		}
		return response;
	}
}
