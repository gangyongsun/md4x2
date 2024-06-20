package cn.com.goldwind.md4x.business.bo.upload;

import java.io.Serializable;

import cn.com.goldwind.md4x.business.entity.datamart.S3UploadfileIndex;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @Title: uploadFileBO.java
 * @Package cn.com.goldwind.md4x.business.bo
 * @description 上传文件信息
 * @author 孙永刚修改，秦德阳创建
 * @date Aug 5, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class uploadFileBO extends S3UploadfileIndex implements Serializable {
	private static final long serialVersionUID = 8654527287465577128L;

	/**
	 * 文件名
	 */
	private String fileName;

	/**
	 * 文件类型
	 */
	private String fileType;

	/**
	 * 状态
	 */
	@Deprecated
	private String fileStatus;

	/**
	 * 行数
	 */
	@Deprecated
	private String fileLine;

	/**
	 * 变量数
	 */
	@Deprecated
	private String fileVariable;

	/**
	 * 最近更新时间
	 */
	private String fileCreateTime;

}
