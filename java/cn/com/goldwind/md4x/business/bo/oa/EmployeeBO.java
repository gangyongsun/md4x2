package cn.com.goldwind.md4x.business.bo.oa;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * @Title: EmployeeBO.java
 * @Package cn.com.goldwind.md4x.business.bo.oa
 * @description 员工业务类 用于json解析
 * @author 孙永刚更新，秦德阳创建
 * @date Aug 4, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Data
public class EmployeeBO implements Serializable {
	private static final long serialVersionUID = 6849302679874485614L;

	/**
	 * 人员编码
	 */
	private String userid;

	/**
	 * 人员姓名
	 */
	private String username;

	/**
	 * 所在单位编码
	 */
	private String unitid;

	/**
	 * 所在单位名称
	 */
	private String unittxt;

	/**
	 * 所属系统编码
	 */
	private String systemid;

	/**
	 * 所属系统名称
	 */
	private String systemtxt;

	/**
	 * 岗位编码
	 */
	private String stell;

	/**
	 * 岗位名称
	 */
	private String stext;

	/**
	 * 所在部门编码
	 */
	private String deptid;

	/**
	 * 所在部门名称
	 */
	private String deptname;

	/**
	 * 人员直属组织名称
	 */
	private String zhrotext;

	/**
	 * 电话
	 */
	private String phonenumber;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 所在中心编码
	 */
	private String centerid;

	/**
	 * 所在中心名称
	 */
	private String centertxt;

	/**
	 * 入司日期
	 */
	private String zhrtime1;

	/**
	 * 入职日期
	 */
	private String zhrrzrq;

	/**
	 * 人员直属组织编码
	 */
	private String orgeh;

	/**
	 * 所在科室编码
	 */
	private String officeid;

	/**
	 * 所在科室名称
	 */
	private String officetxt;

	/**
	 * 人员直管领导编码
	 */
	private String directorcode;

	/**
	 * 人员分管领导编码
	 */
	private String branchcode;

	/**
	 * 成本中心编码
	 */
	private String zhrcost;

	/**
	 * 成本中心名称
	 */
	private String zhrcosttxt;

}
