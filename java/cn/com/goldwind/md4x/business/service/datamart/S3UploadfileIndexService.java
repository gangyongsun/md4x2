package cn.com.goldwind.md4x.business.service.datamart;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.com.goldwind.md4x.business.entity.datamart.S3UploadfileIndex;

/**
 * 
 * @Title: S3UploadfileIndexService.java
 * @Package cn.com.goldwind.md4x.business.service.datamart
 * @description S3文件操作维护服务类实现
 * @author 孙永刚
 * @date Aug 14, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
public interface S3UploadfileIndexService extends IService<S3UploadfileIndex> {

	/**
	 * 保存
	 * 
	 * @param entity
	 * @return
	 */
	public boolean saveOne(S3UploadfileIndex entity);

	/**
	 * 更新
	 * 
	 * @param entity
	 * @return
	 */
	public boolean updateOne(S3UploadfileIndex entity);
	
	/**
	 * 通过主键查询
	 * @param id
	 * @return
	 */
	public S3UploadfileIndex getById(int id);

}
