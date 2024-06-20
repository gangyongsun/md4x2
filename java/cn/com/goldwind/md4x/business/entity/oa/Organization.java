package cn.com.goldwind.md4x.business.entity.oa;

import java.io.Serializable;

import lombok.Data;

/**
 * 
 * @Title: Organization.java
 * @Package cn.com.goldwind.md4x.business.entity.oa
 * @description 组织架构类
 * @author 孙永刚更新，秦德阳创建
 * @date Aug 7, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Data
public class Organization implements Serializable {

	private static final long serialVersionUID = -4693338579262477147L;

	private String orgcode;

	private String orgname;

	private String parentorgcode;

	private String parentorgname;

	private String orgdirectorleader;

	private String orgbranchleader;

	private String orgpostcode;

	private String jglb;

	private String jgbm;

	private String orgename;

}
