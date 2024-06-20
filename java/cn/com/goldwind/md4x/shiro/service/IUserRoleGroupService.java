package cn.com.goldwind.md4x.shiro.service;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.com.goldwind.md4x.shiro.domain.entity.UserRoleGroup;

/**
 * 
 * @Title: IUserRoleGroupService.java
 * @Package cn.com.goldwind.md4x.shiro.service
 * @description 用户部门关系逻辑服务类
 * @author 孙永刚
 * @date Jul 22, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
public interface IUserRoleGroupService extends IService<UserRoleGroup> {

	/**
	 * 根据用户ID和角色组(部门)ID获取关系对象
	 * 
	 * @param userId      用户ID
	 * @param roleGroupId 角色组(部门)ID
	 * @return
	 */
	UserRoleGroup getByUserIdandRoleGroupId(Integer userId, Integer roleGroupId);
}
