package cn.com.goldwind.md4x.business.service.datamart.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.com.goldwind.md4x.business.dao.datamart.S3UploadfileIndexMapper;
import cn.com.goldwind.md4x.business.entity.datamart.S3UploadfileIndex;
import cn.com.goldwind.md4x.business.service.datamart.S3UploadfileIndexService;

/**
 * 
 * @Title: S3UploadfileIndexServiceImpl.java
 * @Package cn.com.goldwind.md4x.business.service.datamart.impl
 * @description S3文件操作维护服务类
 * @author 孙永刚
 * @date Aug 14, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Service
public class S3UploadfileIndexServiceImpl extends ServiceImpl<S3UploadfileIndexMapper, S3UploadfileIndex> implements S3UploadfileIndexService {

	@Autowired
	private S3UploadfileIndexMapper s3UploadfileIndexMapper;

	@Override
	public boolean saveOne(S3UploadfileIndex entity) {
		return s3UploadfileIndexMapper.saveOne(entity);
	}

	@Override
	public boolean updateOne(S3UploadfileIndex entity) {
		return s3UploadfileIndexMapper.updateOne(entity);
	}

	@Override
	public S3UploadfileIndex getById(int id) {
		return s3UploadfileIndexMapper.getById(id);
	}
}
