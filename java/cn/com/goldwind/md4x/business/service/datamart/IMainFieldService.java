package cn.com.goldwind.md4x.business.service.datamart;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.com.goldwind.md4x.business.entity.datamartmap.MainField;
import cn.com.goldwind.md4x.mybatis.Page;

/**
 * 
 * @Title: IMainFieldService.java
 * @Package cn.com.goldwind.md4x.business.service.datamart
 * @description 自定义字段服务类
 * @author 孙永刚
 * @date Aug 20, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
public interface IMainFieldService extends IService<MainField> {

	/**
	 * 按条件分页查询自定义字段
	 * 
	 * @param pageNO        页码
	 * @param pageSize      页码条数
	 * @param findContent   关键词
	 * @param groupName     分组
	 * @param available     启用状态
	 * @param needMaintince 是否需要维护
	 * @return
	 */
	Page<MainField> listMainField(Integer pageNO, Integer pageSize, String findContent, String groupName, Boolean available, Boolean needMaintince);

	/**
	 * 根据分组信息和协议列表查询主变量列表
	 * 
	 * @param groupName   分组信息
	 * @param protocolIds 协议ID列表
	 * @return 变量列表
	 */
	List<MainField> getMainFieldList(String groupName, List<Integer> protocolIds);

	/**
	 * 查询主变量分组列表
	 * 
	 * @return
	 */
	List<String> getGroupList();

}
