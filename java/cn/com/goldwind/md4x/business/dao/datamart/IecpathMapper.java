package cn.com.goldwind.md4x.business.dao.datamart;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.com.goldwind.md4x.business.entity.datamartmap.Iecpath;

/**
 * 
 * @Title: IecpathMapper.java
 * @Package cn.com.goldwind.md4x.business.dao.datamart
 * @description 源字段Mapper接口
 * @author 孙永刚
 * @date Aug 20, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
public interface IecpathMapper extends BaseMapper<Iecpath> {

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
	List<Iecpath> list(Map<String, Object> data);

}
