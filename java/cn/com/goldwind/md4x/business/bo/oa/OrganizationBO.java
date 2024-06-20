package cn.com.goldwind.md4x.business.bo.oa;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * @Title: OrganizationBO.java
 * @Package cn.com.goldwind.md4x.business.bo.oa
 * @description 组织类,用于json解析
 * @author 孙永刚更新，秦德阳创建
 * @date Aug 4, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Data
public class OrganizationBO implements Serializable {
	private static final long serialVersionUID = 3455943227400585313L;

	/**
	 * 所在组织编号
	 */
	private String orgcode;

	/**
	 * 所在组织名称
	 */
	private String orgname;

	/**
	 * 上级组织编号
	 */
	private String parentorgcode;

	/**
	 * 上级组织名称
	 */
	private String parentorgname;

	/**
	 * 所在组织直管领导
	 */
	private String orgdirectorleader;

	/**
	 * 所在组织分管领导
	 */
	private String orgbranchleader;

	/**
	 * 
	 */
	private String orgpostcode;

	/**
	 * 机构类别
	 */
	private String jglb;

	/**
	 * 机构编码
	 */
	private String jgbm;

	/**
	 * 所在组织英文名称
	 */
	private String orgename;

}
