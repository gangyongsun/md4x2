package cn.com.goldwind.md4x.business.service.datamart.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.com.goldwind.md4x.business.dao.datamart.IecpathMapper;
import cn.com.goldwind.md4x.business.entity.datamartmap.Iecpath;
import cn.com.goldwind.md4x.business.service.datamart.IIecpathService;
import cn.com.goldwind.md4x.mybatis.Page;
import cn.com.goldwind.md4x.util.StringUtils;

/**
 * 
 * @Title: IecpathServiceImpl.java
 * @Package cn.com.goldwind.md4x.business.service.datamart
 * @description 原字段服务实现类
 * @author 孙永刚
 * @date Aug 20, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Service
public class IecpathServiceImpl extends ServiceImpl<IecpathMapper, Iecpath> implements IIecpathService {

	@Autowired
	private IecpathMapper iecpathMapper;

	@Override
	public Page<Iecpath> listIecpath(Integer pageNO, Integer pageSize, String findContent) {
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
		int totalSize = iecpathMapper.count(data);
		List<Iecpath> list = iecpathMapper.list(data);
		Page<Iecpath> page = new Page<Iecpath>(pageNO, 0, pageSize, new ArrayList<Iecpath>());
		if (totalSize > 0 && list != null && list.size() > 0) {
			page.setData(list);
			page.setTotalCount(totalSize);
		}
		return page;
	}

}
