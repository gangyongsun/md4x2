package cn.com.goldwind.md4x.business.entity.datamartmap;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 
 * @Title: Iecpath.java
 * @Package cn.com.goldwind.md4x.business.entity.datamartmap
 * @description 源变量实体类
 * @author 孙永刚
 * @date Aug 25, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_iecpath")
public class Iecpath implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 原始英文名称
	 */
	private String iecpath;

	/**
	 * 原始中文名称
	 */
	private String descrcn;

	/**
	 * 排序号
	 */
	private Integer sortId;

	/**
	 * 所属主变量的主键ID
	 */
	@TableField(strategy = FieldStrategy.IGNORED)
	private Integer mainFieldId;
}
