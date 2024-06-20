package cn.com.goldwind.md4x.shiro.service;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.com.goldwind.md4x.mybatis.Page;
import cn.com.goldwind.md4x.shiro.domain.entity.RoleGroup;

public interface IRoleGroupService extends IService<RoleGroup> {

	/**
	 * 分页查询部门列表
	 * 
	 * @param pageNO   页码
	 * @param pageSize 条数
	 * @return
	 */
	Page<RoleGroup> listRoleGroups(Integer pageNO, Integer pageSize);

}
