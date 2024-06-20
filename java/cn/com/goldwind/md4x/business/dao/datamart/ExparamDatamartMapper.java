package cn.com.goldwind.md4x.business.dao.datamart;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.com.goldwind.md4x.business.entity.datamart.ExparamDatamart;
import cn.com.goldwind.md4x.mybatis.Condition;

/**
 * 
 * @Title: ExparamDatamartMapper.java
 * @Package cn.com.goldwind.md4x.business.dao.datamart
 * @description 数据集Dao
 * @author 孙永刚更新，秦德阳创建
 * @date Aug 7, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
public interface ExparamDatamartMapper extends BaseMapper<ExparamDatamart> {

	/**
	 * 根据用户名查询数据集列表
	 * 
	 * @param data
	 * @return
	 */
	List<ExparamDatamart> pageListDatamart(Map<String, Object> data);

	/**
	 * 根据用户名查询数据集个数
	 * 
	 * @param data
	 * @return
	 */
	int count(Map<String, Object> data);
	
	/**
	 * 通过主键id查询
	 * @param id
	 * @return
	 */
	ExparamDatamart getById(int id);
	
	List<ExparamDatamart> searchListByCondition(Condition c);
	
	int updateDataExtractingStarttime(ExparamDatamart ed);
	
	int updateDataExtractingEndtime(ExparamDatamart ed);
}