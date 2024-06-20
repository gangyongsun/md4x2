package cn.com.goldwind.md4x.business.service.datamart.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.com.goldwind.md4x.business.dao.datamart.IecpathMapper;
import cn.com.goldwind.md4x.business.dao.datamart.MainFieldMapper;
import cn.com.goldwind.md4x.business.entity.datamartmap.Iecpath;
import cn.com.goldwind.md4x.business.entity.datamartmap.MainField;
import cn.com.goldwind.md4x.business.service.datamart.IMainFieldService;
import cn.com.goldwind.md4x.mybatis.Page;
import cn.com.goldwind.md4x.util.IKAnalyzerUtil;
import cn.com.goldwind.md4x.util.StringUtils;

/**
 * 
 * @Title: MainFieldServiceImpl.java
 * @Package cn.com.goldwind.md4x.business.service.datamart
 * @description 自定义字段服务实现类
 * @author 孙永刚
 * @date Aug 20, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Service
public class MainFieldServiceImpl extends ServiceImpl<MainFieldMapper, MainField> implements IMainFieldService {

	@Autowired
	private IecpathMapper iecpathMapper;

	@Autowired
	private MainFieldMapper mainFieldMapper;

	@Override
	public Page<MainField> listMainField(Integer pageNO, Integer pageSize, String findContent, String groupName, Boolean available, Boolean needMaintince) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (pageNO < 1) {
			pageNO = 1;
		}
		data.put("currIndex", (pageNO - 1) * pageSize);
		data.put("pageSize", pageSize);
		// 设置搜索内容
		if (StringUtils.isNotBlank(findContent)) {
			data.put("findContent", findContent.trim());
		}
		// 设置分组
		if (StringUtils.isNotBlank(groupName)) {
			data.put("groupName", groupName.trim());
		}
		// 设置启用状态
		if (null != available) {
			data.put("available", available);
		}
		// 设置是否需要维护条件，即需要维护的是model_entry_cn为null的数据
		if (null != needMaintince) {
			data.put("needMaintince", needMaintince);
		}
		int totalSize = mainFieldMapper.count(data);
		List<MainField> list = mainFieldMapper.list(data);
		if (null!=list) {
			// 为每个主变量设置iecpath列表
			for (MainField mainField : list) {
				List<Iecpath> iecpathList = iecpathMapper.selectList(new QueryWrapper<Iecpath>().eq("main_field_id", mainField.getId()));
				mainField.setIecpathList(iecpathList);
				mainField.setCnWordList(IKAnalyzerUtil.iKSegmenterToList(mainField.getModelEntryCN()));
			}
		}
		
		Page<MainField> page = new Page<MainField>(pageNO, 0, pageSize, new ArrayList<MainField>());
		if (totalSize > 0 && list != null && list.size() > 0) {
			page.setData(list);
			page.setTotalCount(totalSize);
		}
		return page;
	}

	@Override
	public List<MainField> getMainFieldList(String groupName, List<Integer> protocolIds) {
		return mainFieldMapper.getMainFieldList(groupName, protocolIds);
	}

	@Override
	public List<String> getGroupList() {
		QueryWrapper<MainField> queryWrapper = new QueryWrapper<MainField>();
		queryWrapper.select("DISTINCT group_name").isNotNull("group_name").orderByAsc("group_sort_id");
		List<MainField> tempList = mainFieldMapper.selectList(queryWrapper);

		List<String> groupList = new ArrayList<String>();
		for (MainField mainField : tempList) {
			groupList.add(mainField.getGroupName());
		}
		return groupList;
	}

}
