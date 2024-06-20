package cn.com.goldwind.md4x.business.entity.datamartmap;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 
 * @Title: Collection.java
 * @Package cn.com.goldwind.md4x.business.entity.datamartmap
 * @description 用户收藏夹Entity
 * @author 孙永刚
 * @date Sep 9, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_collection")
public class Collection implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 收藏夹名
	 */
	private String collectionName;

	/**
	 * 用户ID
	 */
	private Integer userId;

	/**
	 * 主变量列表
	 */
	@TableField(exist = false)
	private List<MainField> mainFieldList;

}
