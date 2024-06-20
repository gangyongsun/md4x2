package cn.com.goldwind.md4x.shiro.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.com.goldwind.md4x.mybatis.Page;
import cn.com.goldwind.md4x.shiro.domain.entity.RoleGroup;
import cn.com.goldwind.md4x.shiro.domain.mapper.RoleGroupMapper;
import cn.com.goldwind.md4x.shiro.service.IRoleGroupService;

@Service
public class RoleGroupServiceImpl extends ServiceImpl<RoleGroupMapper, RoleGroup> implements IRoleGroupService {
	@Resource
	private RoleGroupMapper roleGroupMapper;

	@Override
	public Page<RoleGroup> listRoleGroups(Integer pageNO, Integer pageSize) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (pageNO < 1) {
			pageNO = 1;
		}
		data.put("currIndex", (pageNO - 1) * pageSize);
		data.put("pageSize", pageSize);
		int totalSize = roleGroupMapper.count();
		List<RoleGroup> list = roleGroupMapper.getRoleGroupListInPage(data);
		Page<RoleGroup> page = new Page<RoleGroup>(pageNO, 0, pageSize, new ArrayList<RoleGroup>());
		if (totalSize > 0 && list != null && list.size() > 0) {
			page.setData(list);
			page.setTotalCount(totalSize);
		}
		return page;
	}
}
