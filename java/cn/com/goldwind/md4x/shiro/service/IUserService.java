package cn.com.goldwind.md4x.shiro.service;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.com.goldwind.md4x.mybatis.Page;
import cn.com.goldwind.md4x.shiro.domain.entity.User;

public interface IUserService extends IService<User> {

	/**
	 * 分页查询用户列表
	 * 
	 * @param currPage    页面
	 * @param pageSize    页码条数
	 * @param findContent 查询条件
	 * @param goldwinder  是否是金风用户
	 * @return
	 */
	Page<User> listUsers(int currPage, int pageSize, String findContent, String goldwinder);

	/**
	 * 用户注册
	 * 
	 * @param user          用户
	 * @param roleGroupCode 部门编码
	 * @param roleGroupName 部门名称
	 */
	boolean registerUser(User user, String roleGroupCode, String roleGroupName);

}
