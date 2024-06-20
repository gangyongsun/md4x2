package cn.com.goldwind.md4x.shiro.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.com.goldwind.md4x.mybatis.Page;
import cn.com.goldwind.md4x.shiro.domain.entity.Role;
import cn.com.goldwind.md4x.shiro.domain.entity.RoleGroup;
import cn.com.goldwind.md4x.shiro.domain.entity.User;
import cn.com.goldwind.md4x.shiro.domain.entity.UserRole;
import cn.com.goldwind.md4x.shiro.domain.entity.UserRoleGroup;
import cn.com.goldwind.md4x.shiro.domain.mapper.UserMapper;
import cn.com.goldwind.md4x.shiro.service.IRoleGroupService;
import cn.com.goldwind.md4x.shiro.service.IRoleService;
import cn.com.goldwind.md4x.shiro.service.IUserRoleGroupService;
import cn.com.goldwind.md4x.shiro.service.IUserRoleService;
import cn.com.goldwind.md4x.shiro.service.IUserService;
import cn.com.goldwind.md4x.util.StringUtils;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private IUserRoleService userRoleService;

	@Autowired
	private IRoleService roleService;

	@Autowired
	private IRoleGroupService roleGroupService;

	@Autowired
	private IUserRoleGroupService userRoleGroupService;

	/**
	 * 创建用户所分配的默认角色的名称，根据该名称能查到角色ID；注意END_USER是数据库初始化脚本默认给的普通用户的角色名，不要随便更改
	 */
	private final String DEFAULT_ROLE_NAME = "END_USER";

	@Override
	public Page<User> listUsers(int currPage, int pageSize, String findContent, String goldwinder) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (currPage < 1) {
			currPage = 1;
		}
		data.put("currIndex", (currPage - 1) * pageSize);
		data.put("pageSize", pageSize);
		// 设置搜索内容
		if (StringUtils.isNotBlank(findContent)) {
			data.put("findContent", findContent.trim());
		}
		// 设置是否是金风员工条件
		if (StringUtils.isNotBlank(goldwinder)) {
			if (goldwinder.trim().equals("1")) {
				data.put("goldwinder", true);
			} else if (goldwinder.trim().equals("0")) {
				data.put("goldwinder", false);
			}
		}
		int totalSize = userMapper.userCount(data);
		List<User> list = userMapper.getUserListInPage(data);
		Page<User> page = new Page<User>(currPage, 0, pageSize, new ArrayList<User>());
		if (totalSize > 0 && list != null && list.size() > 0) {
			page.setData(list);
			page.setTotalCount(totalSize);
		}
		return page;
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
	public boolean registerUser(User user, String roleGroupCode, String roleGroupName) {
		// 保存用户
		this.save(user);

		// ******************逻辑处理：角色组(部门)不存在才保存******************
		RoleGroup roleGroup = roleGroupService.getOne(new QueryWrapper<RoleGroup>().eq("role_group_code", roleGroupCode));
		if (null == roleGroup) {
			// 创建角色组(部门)
			roleGroup = new RoleGroup();
			roleGroup.setRoleGroupCode(roleGroupCode);
			roleGroup.setRoleGroupName(roleGroupName);
			roleGroup.setDeleted(false);
			roleGroup.setAvailable(true);
			// 保存角色组(部门)
			roleGroupService.save(roleGroup);
		}

		// ******************逻辑处理：保存用户角色组关系******************
		UserRoleGroup userRoleGroup = new UserRoleGroup();
		userRoleGroup.setUserId(user.getUserId());
		userRoleGroup.setRoleGroupId(roleGroup.getId());
		userRoleGroup.setAtGroup(true);
		userRoleGroup.setBeginTime(new Date());
		userRoleGroupService.save(userRoleGroup);

		// ******************逻辑处理：给用户分配默认角色******************
		Role role = roleService.getOne(new QueryWrapper<Role>().eq("role_name", DEFAULT_ROLE_NAME));
		UserRole userRole = new UserRole();
		userRole.setUserId(user.getUserId());
		userRole.setRoleId(role.getRoleId());
		userRoleService.save(userRole);
		return true;
	}

}
