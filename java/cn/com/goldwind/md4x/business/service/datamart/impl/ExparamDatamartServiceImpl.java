package cn.com.goldwind.md4x.business.service.datamart.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.com.goldwind.md4x.business.dao.datamart.ExparamDatamartMapper;
import cn.com.goldwind.md4x.business.entity.datamart.ExparamDatamart;
import cn.com.goldwind.md4x.business.service.datamart.IExparamDatamartService;
import cn.com.goldwind.md4x.mybatis.Condition;
import cn.com.goldwind.md4x.mybatis.Page;

@Service
public class ExparamDatamartServiceImpl extends ServiceImpl<ExparamDatamartMapper, ExparamDatamart> implements IExparamDatamartService {
	@Autowired
	ExparamDatamartMapper exparamDatamartMapper;

	@Override
	public Page<ExparamDatamart> pageListDatamart(int currPage, int pageSize, Map<String, Object> conditions) {
		if (currPage < 1) {
			currPage = 1;
		}
		conditions.put("currIndex", (currPage - 1) * pageSize);
		conditions.put("pageSize", pageSize);
		// 分页查询
		List<ExparamDatamart> list = exparamDatamartMapper.pageListDatamart(conditions);
		// 查询总条数
		int totalSize = exparamDatamartMapper.count(conditions);
		Page<ExparamDatamart> page = new Page<ExparamDatamart>(currPage, 0, pageSize, new ArrayList<ExparamDatamart>());
		if (totalSize > 0 && list != null && list.size() > 0) {
			page.setData(list);
			page.setTotalCount(totalSize);
		}
		return page;
	}

	@Override
	public ExparamDatamart getById(int id) {
		return exparamDatamartMapper.getById(id);
	}

	@Override
	public List<ExparamDatamart> getList(Condition c) {
		return exparamDatamartMapper.searchListByCondition(c);
	}

	@Override
	public int updateDataExtractingStarttime(ExparamDatamart ed) {
		return exparamDatamartMapper.updateDataExtractingStarttime(ed);
	}

	@Override
	public int updateDataExtractingEndtime(ExparamDatamart ed) {
		return exparamDatamartMapper.updateDataExtractingEndtime(ed);
	}

}
