package cn.com.goldwind.md4x.shiro.domain.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.com.goldwind.md4x.shiro.domain.entity.User;

/**
 * 
 * @author alvin
 *
 */
public interface UserMapper extends BaseMapper<User> {

	/**
	 * 分页查询用户列表
	 * 
	 * @param data
	 * @return
	 */
	List<User> getUserListInPage(Map<String, Object> data);

	/**
	 * 查询用户数量
	 * 
	 * @param data
	 * @return
	 */
	int userCount(Map<String, Object> data);

}
