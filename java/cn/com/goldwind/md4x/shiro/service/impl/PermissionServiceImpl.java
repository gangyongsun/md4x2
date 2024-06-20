package cn.com.goldwind.md4x.shiro.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.com.goldwind.md4x.mybatis.Page;
import cn.com.goldwind.md4x.shiro.domain.entity.Permission;
import cn.com.goldwind.md4x.shiro.domain.mapper.PermissionMapper;
import cn.com.goldwind.md4x.shiro.service.IPermissionService;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

	@Resource
	private PermissionMapper permissionMapper;

	@Override
	public List<Permission> listPermissions(Integer roleId) {
		return permissionMapper.listPermissions(roleId);
	}

	@Override
	public Page<Permission> listPermissions(Integer pageNO, Integer pageSize) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (pageNO < 1) {
			pageNO = 1;
		}
		data.put("currIndex", (pageNO - 1) * pageSize);
		data.put("pageSize", pageSize);
		int totalSize = permissionMapper.permissionCount();
		List<Permission> list = permissionMapper.getPermissionListInPage(data);
		Page<Permission> page = new Page<Permission>(pageNO, 0, pageSize, new ArrayList<Permission>());
		if (totalSize > 0 && list != null && list.size() > 0) {
			page.setData(list);
			page.setTotalCount(totalSize);
		}
		return page;
	}

	@Override
	public List<String> getPermissionGroups() {
		return permissionMapper.getPermissionGroups();
	}
}
