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
import cn.com.goldwind.md4x.shiro.domain.entity.RoleGroup;
import cn.com.goldwind.md4x.shiro.service.IRoleGroupService;
import cn.com.goldwind.md4x.util.i18n.RoleGroupCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @author alvin
 */
@Api(value = "角色组(部门)接口", tags = { "角色组(部门)接口" })
@RestController
@RequestMapping("roleGroup")
public class RoleGroupController extends BaseController {

	@Autowired
	private IRoleGroupService roleGroupService;

	/**
	 * 查询部门列表
	 * 
	 * @return
	 */
	@ApiOperation(value = "查询部门列表", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@RequiresPermissions("roleGroup:list")
	public ResultInfoDTO<Object> listAll() {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		List<RoleGroup> list = roleGroupService.list();
		if (null != list) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(RoleGroupCode.ROLEGROUP_LIST_SUCCESS));
			response.setData(list);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(RoleGroupCode.ROLEGROUP_LIST_FAILED));
		}
		return response;
	}

	/**
	 * 分页查询部门列表
	 * 
	 * @param pageNO   页码
	 * @param pageSize 条数
	 * @return
	 */
	@ApiOperation(value = "分页查询部门列表", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "pageNO", value = "当前页码", required = true, dataType = "Integer"),
			@ApiImplicitParam(name = "pageSize", value = "当前页条数", required = true, dataType = "Integer") })
	@RequestMapping(value = "listpage", method = RequestMethod.GET)
	@RequiresPermissions("roleGroup:listpage")
	public ResultInfoDTO<Object> listPermissions(Integer pageNO, Integer pageSize) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		Page<RoleGroup> pager = roleGroupService.listRoleGroups(pageNO, pageSize);
		if (null != pager) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(RoleGroupCode.ROLEGROUP_LIST_SUCCESS));
			response.setData(pager);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(RoleGroupCode.ROLEGROUP_LIST_FAILED));
		}
		return response;
	}

	/**
	 * 添加部门
	 * 
	 * @param role 部门对象
	 * @return
	 */
	@ApiOperation(value = "添加部门", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "role", value = "部门对象", required = true, dataType = "Role") })
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@RequiresPermissions("roleGroup:add")
	public ResultInfoDTO<Object> add(@RequestBody RoleGroup roleGroup) {
		roleGroup.setAvailable(true);
		roleGroup.setDeleted(false);
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		boolean flag = roleGroupService.save(roleGroup);
		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(RoleGroupCode.ROLEGROUP_ADD_SUCCESS));
			response.setData(roleGroup);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(RoleGroupCode.ROLEGROUP_ADD_FAILED));
		}
		return response;
	}

	/**
	 * 更新部门
	 * 
	 * @param roleGroup 部门对象
	 * @return
	 */
	@ApiOperation(value = "更新部门", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "roleGroup", value = "部门对象", required = true, dataType = "RoleGroup") })
	@RequestMapping(value = "update", method = RequestMethod.PUT)
	@RequiresPermissions("roleGroup:update")
	public ResultInfoDTO<Object> update(@RequestBody RoleGroup roleGroup) {
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		boolean flag = roleGroupService.updateById(roleGroup);
		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(RoleGroupCode.ROLEGROUP_UPDATE_SUCCESS));
			response.setData(roleGroup);
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(RoleGroupCode.ROLEGROUP_UPDATE_FAILED));
		}
		return response;

	}

	/**
	 * 删除部门
	 * 
	 * @param roleGroup 部门
	 * @return
	 */
	@ApiOperation(value = "删除部门", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 0, message = "成功"), @ApiResponse(code = -1, message = "失败") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "roleGroup", value = "部门", required = true, dataType = "RoleGroup") })
	@RequestMapping(value = "delete", method = RequestMethod.PUT)
	@RequiresPermissions("roleGroup:delete")
	public ResultInfoDTO<Object> delete(@RequestBody RoleGroup roleGroup) {
		roleGroup.setDeleted(true);
		roleGroup.setAvailable(false);
		ResultInfoDTO<Object> response = new ResultInfoDTO<Object>();
		boolean flag = roleGroupService.updateById(roleGroup);
		if (flag) {
			response.setCode(ResultInfoDTO.OK);
			response.setMessage(i18nService.getMessage(RoleGroupCode.ROLEGROUP_DELETE_SUCCESS));
		} else {
			response.setCode(ResultInfoDTO.FAILED);
			response.setMessage(i18nService.getMessage(RoleGroupCode.ROLEGROUP_DELETE_FAILED));
		}
		return response;
	}
}
