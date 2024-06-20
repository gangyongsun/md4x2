package cn.com.goldwind.md4x.shiro.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.com.goldwind.md4x.mybatis.Page;
import cn.com.goldwind.md4x.shiro.DTO.RoleDTO;
import cn.com.goldwind.md4x.shiro.domain.entity.Role;
import cn.com.goldwind.md4x.shiro.domain.mapper.RoleMapper;
import cn.com.goldwind.md4x.shiro.service.IRoleService;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

	@Resource
	private RoleMapper roleMapper;

	@Override
	public List<RoleDTO> listRolesByUserId(Integer userId) {
		return roleMapper.listRolesByUserId(userId);
	}

	/**
	 * 根据用户ID查询用户拥有的角色列表
	 */
	@Override
	public List<RoleDTO> listGroupRolesByUserId(Integer userId) {
		return roleMapper.listGroupRolesByUserId(userId);
	}

	@Override
	public List<RoleDTO> listRolesByRoleGroupId(Integer roleGroupId) {
		return roleMapper.listRolesByRoleGroupId(roleGroupId);
	}

	@Override
	public Page<RoleDTO> listRoles(Integer pageNO, Integer pageSize) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (pageNO < 1) {
			pageNO = 1;
		}
		data.put("currIndex", (pageNO - 1) * pageSize);
		data.put("pageSize", pageSize);
		int totalSize = roleMapper.roleCount();
		List<RoleDTO> list = roleMapper.getRoleListInPage(data);
		Page<RoleDTO> page = new Page<RoleDTO>(pageNO, 0, pageSize, new ArrayList<RoleDTO>());
		if (totalSize > 0 && list != null && list.size() > 0) {
			page.setData(list);
			page.setTotalCount(totalSize);
		}
		return page;
	}

	@Override
	public List<RoleDTO> listRoles() {
		return roleMapper.getRoleList();
	}

}
