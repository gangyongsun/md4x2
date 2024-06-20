package cn.com.goldwind.md4x.shiro.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.com.goldwind.md4x.shiro.domain.entity.UserRole;
import cn.com.goldwind.md4x.shiro.domain.mapper.RoleMapper;
import cn.com.goldwind.md4x.shiro.domain.mapper.UserRoleMapper;
import cn.com.goldwind.md4x.shiro.service.IUserRoleService;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Resource
	private RoleMapper roleMapper;

	@Override
	@Transactional
	public boolean removeByUserIdList(List<Integer> userIdList) {
		int result = userRoleMapper.deleteByUserIds(userIdList);
		if (result >= 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Transactional
	public boolean removeUserGroupRolesByUserId(Integer userId) {
		int result=userRoleMapper.removeUserGroupRolesByUserId(userId);
		if (result>=1) {
			return true;
		}else {
			return false;
		}
	}

}
