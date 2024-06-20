package cn.com.goldwind.md4x.business.bo.oa;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 
 * @Title: OrganizationPageBO.java
 * @Package cn.com.goldwind.md4x.business.bo.oa
 * @description json解析类
 * @author 孙永刚更新，秦德阳创建
 * @date Aug 4, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Data
public class OrganizationPageBO implements Serializable {
	private static final long serialVersionUID = -1509145076813616417L;

	private String code;

	private String message;

	private List<OrganizationBO> resultOrgList;

	private List<EmployeeBO> employees;

}