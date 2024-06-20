/**   
* @Title: TWtinfoServiceImpl.java 
* @Package cn.com.goldwind.md4x.business.service.impl 
* @Description: TODO(用一句话描述该文件做什么) 
* @author wangguiyu  
* @date Apr 15, 2020 8:33:39 AM 
* @version V1.0   
*/
package cn.com.goldwind.md4x.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.goldwind.md4x.business.dao.datacenter.Md4xWtinfoDao;
import cn.com.goldwind.md4x.business.entity.datacenter.Md4xWtinfo;
import cn.com.goldwind.md4x.mybatis.Condition;
import cn.com.goldwind.md4x.mybatis.IGenericPage;
import cn.com.goldwind.md4x.mybatis.Page;
import cn.com.goldwind.md4x.util.StringUtils;
import cn.com.goldwind.md4x.util.http.HttpAPIService;


/**
 * @ClassName: TWfinfoServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author wangguiyu
 * @date Apr 15, 2020 8:33:39 AM
 *
 */

@Service
public class Md4xWtinfoServiceImpl implements Md4xWtinfoService {

	@Autowired
	HttpAPIService httpAPIService;
	
	@Autowired
	Md4xWtinfoDao md4xWtinfoDao;

	@Override
	public Page<Md4xWtinfo> getWtinfos(Integer pageNo, Integer pageSize, String wfName) {
		if(StringUtils.isBlank(wfName)){
			return null;
		}
		Integer start = 0;
		Condition c = Condition.newInstance();
		c.addMapCondition("wfName", wfName);
		c.addSort("wtid", Condition.SORT_ASC);// 按id升序
		// ---------判断分页-------------
		if (pageNo == null || pageSize == null) {
			pageSize = md4xWtinfoDao.getCountByCondition("getCountByConditionWithPage", c);
			if(pageSize == 0)
				return null;
		} else {
			if (pageNo <= 0)
				start = 0;
			else
				start = (pageNo - 1) * pageSize;
		}
		c.setLimit(pageSize).setStart(start);
		// ----------模糊查询------------
		IGenericPage<Md4xWtinfo> genericPage = md4xWtinfoDao.selectPageBy("searchPageBy", c);
		Page<Md4xWtinfo> p = new Page<Md4xWtinfo>(start, genericPage.getTotalCount(), pageSize,
				genericPage.getThisPageElements());
		return p;
	}

}
