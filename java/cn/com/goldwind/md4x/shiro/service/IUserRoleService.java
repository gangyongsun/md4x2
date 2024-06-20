package cn.com.goldwind.md4x.shiro.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.com.goldwind.md4x.shiro.domain.entity.UserRole;

public interface IUserRoleService extends IService<UserRole> {
	
	/**
	 * 根据用户ID清空用户拥有的部门角色
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	boolean removeUserGroupRolesByUserId(Integer userId);

	/**
	 * 根据用户ID列表清空其角色
	 * 
	 * @param userIdList 用户ID列表
	 * @return
	 */
	boolean removeByUserIdList(List<Integer> userIdList);

}
