package cn.com.goldwind.md4x.business.entity.datamartmap;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 
 * @Title: CollectionMainfield.java
 * @Package cn.com.goldwind.md4x.business.entity.datamartmap
 * @description 收藏夹与变量关系Entity
 * @author 孙永刚
 * @date Sep 9, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_collection_mainfield")
public class CollectionMainfield implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 自增主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 收藏夹ID
	 */
	private Integer collectionId;

	/**
	 * 主变量ID
	 */
	private Integer mainfieldId;

}
