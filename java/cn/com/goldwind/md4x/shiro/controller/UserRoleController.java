package cn.com.goldwind.md4x.shiro.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.com.goldwind.md4x.business.bo.ResultInfoDTO;
import cn.com.goldwind.md4x.shiro.domain.entity.UserRole;
import cn.com.goldwind.md4x.shiro.service.IUserRoleService;
import cn.com.goldwind.md4x.shiro.service.IUserService;
import cn.com.goldwind.md4x.util.i18n.UserRoleCode;
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
@Api(value = "用户角色关系接口", tags = { "用户角色关系接口" })
@RestController
@RequestMapping("userRole")
public class UserRoleController extends BaseController {

	@Autowired
	private IUserService userService;

	@Autowired
	private IUserRoleService userRoleService;

	/**
	 * 为用户赋角色
	 * 
	 * @param roleIdList 角色ID列表
	 * @param userId     用户ID
	 * @return
	 */
	@ApiOperation(value = "为用户赋角色", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "roleIdList", value = "角色ID列表", required = true, dataType = "List<Integer>"), @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer") })
	@RequestMapping(value = "grantRole2User", method = RequestMethod.POST)
	@RequiresPermissions("userRole:grantRole2User")
	public ResultInfoDTO<Object> grantRole2User(@RequestBody HashMap<String, Object> paramMap) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		// 1.解析paramMap，获取用户ID和角色ID列表
		List<Integer> roleIdList = null;
		Integer userId = null;
		try {
			roleIdList = (List<Integer>) paramMap.get("roleIdList");
			userId = Integer.parseInt(paramMap.get("userId").toString());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(UserRoleCode.API_PARAMATER_ERROR_ON_ROLE_ALLOCATION));
			return response;
		}

		// 2.根据用户ID查询用户是否是删除状态
		boolean delFlag = userService.getById(userId).getDeleted();
		if (delFlag) {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(UserRoleCode.ROLE_ALLOCATION_FAILED_ON_DELETED_USER));
			return response;
		}

		// 3.先判断用户是否有角色，有角色就清空后进行角色分配，无角色就直接分配
		int count = userRoleService.count(new QueryWrapper<UserRole>().eq("userId", userId));
		boolean clearFlag = false;
		if (count > 0) {
			// 先清空用户的角色
			clearFlag = userRoleService.remove(new QueryWrapper<UserRole>().eq("userId", userId));
		} else {
			clearFlag = true;
		}

		// 4.进行角色分配
		if (clearFlag) {
			boolean flag = false;
			if (null != roleIdList && roleIdList.size() > 0) {
				// 41.构造用户ID和角色ID的对象数组
				List<UserRole> userRoleList = new ArrayList<UserRole>();
				for (Integer roleId : roleIdList) {
					UserRole userRole = new UserRole();
					userRole.setUserId(userId);
					userRole.setRoleId(roleId);
					userRoleList.add(userRole);
				}
				// 4.2.批量插入
				flag = userRoleService.saveBatch(userRoleList);
			} else {
				flag = true;
			}
			if (flag) {
				response.setCode(ResultInfoDTO.OK);
				response.setMessage(i18nService.getMessage(UserRoleCode.ROLE_ALLOCATION_SUCCESS));
			} else {
				response.setCode(ResultInfoDTO.FAILED);
				response.setMessage(i18nService.getMessage(UserRoleCode.ROLE_ALLOCATION_FAILED));
			}
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(UserRoleCode.PREVIOUS_ROLES_CLEAR_FAILED));
		}
		return response;
	}

	/**
	 * 根据用户ID清空用户角色
	 * 
	 * @param userIdList 用户ID列表
	 * @return
	 */
	@ApiOperation(value = "根据用户ID清空用户角色", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "userIdList", value = "用户ID", required = true, dataType = "List<Integer>") })
	@RequestMapping(value = "clearUserRoles", method = RequestMethod.DELETE)
	@RequiresPermissions("userRole:clearUserRoles")
	public ResultInfoDTO<Object> clearUserRoles(@RequestBody List<Integer> userIdList) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		boolean flag = userRoleService.removeByUserIdList(userIdList);
		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(UserRoleCode.USER_ROLES_CLEAR_SUCCESS));
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(UserRoleCode.USER_ROLES_CLEAR_FAILED));
		}
		return response;
	}

	/**
	 * 根据用户ID清空用户拥有的部门角色
	 * 
	 * @param userIdList 用户ID列表
	 * @return
	 */
	@ApiOperation(value = "根据用户ID清空用户拥有的部门角色", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer") })
	@RequestMapping(value = "clearUserGroupRoles", method = RequestMethod.DELETE)
	@RequiresPermissions("userRole:clearUserGroupRoles")
	public ResultInfoDTO<Object> clearUserGroupRoles(Integer userId) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		boolean flag = userRoleService.removeUserGroupRolesByUserId(userId);
		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(UserRoleCode.USER_ROLEGROUP_ROLES_CLEAR_SUCCESS));
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(UserRoleCode.USER_ROLEGROUP_ROLES_CLEAR_FAILED));
		}
		return response;
	}

}
