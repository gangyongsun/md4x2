package cn.com.goldwind.md4x.business.dao.datamart;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.com.goldwind.md4x.business.entity.datamartmap.MainField;

/**
 * 
 * @Title: MainFieldMapper.java
 * @Package cn.com.goldwind.md4x.business.dao.datamart
 * @description 自定义字段Mapper接口
 * @author 孙永刚
 * @date Aug 20, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
public interface MainFieldMapper extends BaseMapper<MainField> {

	/**
	 * 查询数量
	 * 
	 * @param data 条件map
	 * @return
	 */
	int count(Map<String, Object> data);

	/**
	 * 查询列表
	 * 
	 * @param data 条件map
	 * @return
	 */
	List<MainField> list(Map<String, Object> data);

	/**
	 * 根据分组信息和协议列表查询主变量列表
	 * 
	 * @param groupName   分组信息
	 * @param protocolIds 协议ID列表
	 * @return 变量列表
	 */
	List<MainField> getMainFieldList(String groupName, List<Integer> protocolIds);

}
