package cn.com.goldwind.md4x.business.dao.datamart;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import cn.com.goldwind.md4x.business.entity.datamart.S3UploadfileIndex;

/**
 * 
 * @Title: S3UploadfileIndexMapper.java
 * @Package cn.com.goldwind.md4x.business.dao.datamart
 * @description S3文件操作维护Dao
 * @author 孙永刚
 * @date Aug 14, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
public interface S3UploadfileIndexMapper extends BaseMapper<S3UploadfileIndex> {

	boolean saveOne(S3UploadfileIndex entity);

	boolean updateOne(S3UploadfileIndex entity);
	
	S3UploadfileIndex getById(int id);

}
