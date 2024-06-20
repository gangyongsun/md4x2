package cn.com.goldwind.md4x.shiro.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.com.goldwind.md4x.shiro.domain.entity.RolePermission;
import cn.com.goldwind.md4x.shiro.domain.mapper.RolePermissionMapper;
import cn.com.goldwind.md4x.shiro.service.IRolePermissionService;

@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements IRolePermissionService {

	@Autowired
	private RolePermissionMapper rolePermissionMapper;

	@Override
	@Transactional
	public boolean removeByRoleIdList(List<Integer> roleIdList) {
		int result = rolePermissionMapper.deleteByRoleIds(roleIdList);
		if (result >= 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Transactional
	public boolean saveRolePermissions(List<RolePermission> rolePermissionList) {
		int result = rolePermissionMapper.insertRolePermissions(rolePermissionList);
		if (result >= 1) {
			return true;
		} else {
			return false;
		}
	}

}
