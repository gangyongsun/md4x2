package cn.com.goldwind.md4x.shiro.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.com.goldwind.md4x.shiro.domain.entity.UserRoleGroup;
import cn.com.goldwind.md4x.shiro.domain.mapper.UserRoleGroupMapper;
import cn.com.goldwind.md4x.shiro.service.IUserRoleGroupService;

@Service
public class UserRoleGroupServiceImpl extends ServiceImpl<UserRoleGroupMapper, UserRoleGroup> implements IUserRoleGroupService {

	@Override
	public UserRoleGroup getByUserIdandRoleGroupId(Integer userId, Integer roleGroupId) {
		return baseMapper.selectOne(new LambdaQueryWrapper<UserRoleGroup>().eq(UserRoleGroup::getUserId, userId).eq(UserRoleGroup::getRoleGroupId, roleGroupId));
	}

}
