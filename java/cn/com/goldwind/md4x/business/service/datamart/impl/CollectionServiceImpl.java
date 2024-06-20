package cn.com.goldwind.md4x.business.service.datamart.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.com.goldwind.md4x.business.dao.datamart.CollectionMapper;
import cn.com.goldwind.md4x.business.entity.datamartmap.Collection;
import cn.com.goldwind.md4x.business.service.datamart.ICollectionService;

/**
 * 
 * @Title:  CollectionServiceImpl.java
 * @Package cn.com.goldwind.md4x.business.service.datamart.impl 
 * @description 用户收藏夹服务实现类
 * @author 孙永刚
 * @date Sep 9, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved. 
 *
 */
@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection> implements ICollectionService {

}
