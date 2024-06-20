package cn.com.goldwind.md4x.business.service.datamart;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.com.goldwind.md4x.business.entity.datamart.ExparamDatamart;
import cn.com.goldwind.md4x.mybatis.Condition;
import cn.com.goldwind.md4x.mybatis.Page;

public interface IExparamDatamartService extends IService<ExparamDatamart> {
	/**
	 * 根据用户名分页查询数据集列表
	 * 
	 * @param currPage   页码
	 * @param pageSize   每页条数
	 * @param conditions 条件
	 * @return
	 */
	Page<ExparamDatamart> pageListDatamart(int currPage, int pageSize, Map<String, Object> conditions);
	
	/**
	 * 通过主键查询
	 * @param id
	 * @return
	 */
	ExparamDatamart getById(int id);
	
	
	/**
	 * 根据查询条件 查询结果集
	 * @param c
	 * @return
	 */
	List<ExparamDatamart>  getList(Condition c);
	
	int updateDataExtractingStarttime(ExparamDatamart ed);
	
	int updateDataExtractingEndtime(ExparamDatamart ed);

}
