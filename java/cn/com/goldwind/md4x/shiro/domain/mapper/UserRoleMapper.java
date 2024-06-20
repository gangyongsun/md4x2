package cn.com.goldwind.md4x.shiro.domain.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.com.goldwind.md4x.shiro.domain.entity.UserRole;

/**
 * 
 * @author alvin
 *
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

	/**
	 * 根据用户ID删除其角色
	 * 
	 * @param userIdList 用户ID列表
	 * @return
	 */
	int deleteByUserIds(List<Integer> userIdList);


	/**
	 * 根据用户ID清空用户拥有的部门角色
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	int removeUserGroupRolesByUserId(Integer userId);

}
