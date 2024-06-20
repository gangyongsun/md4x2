package cn.com.goldwind.md4x.business.service.datamart;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.com.goldwind.md4x.business.entity.datamartmap.Iecpath;
import cn.com.goldwind.md4x.mybatis.Page;

/**
 * 
 * @Title: IIecpathService.java
 * @Package cn.com.goldwind.md4x.business.service.datamart
 * @description 原字段服务类
 * @author 孙永刚
 * @date Aug 20, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
public interface IIecpathService extends IService<Iecpath> {

	/**
	 * 按条件分页查询源字段
	 * 
	 * @param pageNO      页码
	 * @param pageSize    页码条数
	 * @param findContent 关键词
	 * @return
	 */
	Page<Iecpath> listIecpath(Integer pageNO, Integer pageSize, String findContent);

}
