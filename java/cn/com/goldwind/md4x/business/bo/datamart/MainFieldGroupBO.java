package cn.com.goldwind.md4x.business.bo.datamart;

import java.io.Serializable;
import java.util.List;

import cn.com.goldwind.md4x.business.entity.datamartmap.MainField;
import lombok.Data;

/**
 * 
 * @Title: LabelParentBO.java
 * @Package cn.com.goldwind.md4x.business.bo
 * @description 映射字段父节点
 * @author 孙永刚更新，德阳创建
 * @date Aug 5, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Data
public class MainFieldGroupBO implements Serializable {
	private static final long serialVersionUID = 5213757992433202976L;

	private Integer groupId;

	/**
	 * 标签
	 */
	private String label;

	/**
	 * 孩子
	 */
	private List<MainField> mainFieldList;

}
