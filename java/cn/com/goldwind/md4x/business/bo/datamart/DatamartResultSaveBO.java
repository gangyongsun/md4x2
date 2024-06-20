package cn.com.goldwind.md4x.business.bo.datamart;

import java.io.Serializable;
import java.util.List;

import cn.com.goldwind.md4x.business.entity.datamartmap.MainField;
import lombok.Data;

/**
 * 
 * @Title: DatamartResultSaveBO.java
 * @Package cn.com.goldwind.md4x.business.bo
 * @description 数据集保存结果业务类
 * @author 孙永刚更新，秦德阳创建
 * @date Aug 4, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Data
public class DatamartResultSaveBO implements Serializable {
	private static final long serialVersionUID = 6438957346078534192L;

	/**
	 * 风场ID
	 */
	private String wfid;

	/**
	 * 文件类型
	 */
	private String fileType;

	/**
	 * 开始时间
	 */
	private String beginTime;

	/**
	 * 结束时间
	 */
	private String endTime;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 数据集名
	 */
	private String datamartName;

	/**
	 * 备注
	 */
	private String remarks;

	/**
	 * 数据集状态：0：待抽取；1：抽取中；2：抽取完成
	 */
	private Integer dmStatus;

	/**
	 * 风场名
	 */
	private String wfname;

	/**
	 * 完整度
	 */
	private String dataIntegrity;

	/**
	 * 变量数
	 */
	private Integer variableNum;

	/**
	 * 保存的字段变量信息
	 */
	private List<MainField> conditions;

	/**
	 * 风机id数组
	 */
	private List<String> wtids;

}
