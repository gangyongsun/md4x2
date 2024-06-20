package cn.com.goldwind.md4x.business.entity.oa;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * @Title: Employees.java
 * @Package cn.com.goldwind.md4x.business.entity.oa
 * @description 员工类
 * @author 孙永刚更新，秦德阳创建
 * @date Aug 7, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Data
public class Employees implements Serializable {
	private static final long serialVersionUID = -1289848584740665158L;

	private String userid;

	private String username;

	private String unitid;

	private String unittxt;

	private String systemid;

	private String systemtxt;

	private String stell;

	private String stext;

	private String deptid;

	private String deptname;

	private String zhrotext;

	private String phonenumber;

	private String email;

	private String centerid;

	private String centertxt;

	private String zhrtime1;

	private String zhrrzrq;

	private String orgeh;

	private String officeid;

	private String officetxt;

	private String directorcode;

	private String branchcode;

	private String zhrcost;

	private String zhrcosttxt;

}
