package cn.com.goldwind.md4x.business.entity.datamartmap;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 
 * @Title: MainField.java
 * @Package cn.com.goldwind.md4x.business.entity.datamartmap
 * @description 主变量实体类
 * @author 孙永刚
 * @date Aug 25, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_main_field")
public class MainField implements Serializable {

	private static final long serialVersionUID = 1L;

	// 主人
	public static final String MASTER = "master";
	// 奴隶
	public static final String SLAVE = "slave";
	// 自由身份
	public static final String FREEMAN = "freeman";

	/**
	 * ID自增主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 词条ID
	 */
	private String deid;

	/**
	 * 字段类型标签：0:不显示;1:有deid;2:没有deid
	 */
	private String flag;

	/**
	 * 分组名称信息
	 */
	private String groupName;

	/**
	 * 分组信息排序字段
	 */
	private Integer groupSortId;

	/**
	 * 从t_iecpath映射过来的源变量名
	 */
	@TableField("iecpath")
	private String iecpath;
	
	/**
	 * 统一英文名称
	 */
	@TableField("model_entry_en")
	private String modelEntryEN;

	/**
	 * 统一中文名称
	 */
	@TableField("model_entry_cn")
	private String modelEntryCN;

	/**
	 * 自定义EN字段名
	 */
	@TableField(exist = false)
	private String customModelEntryEN;

	/**
	 * 是否可用标志
	 */
	private Boolean available;

	/**
	 * 主变量从属标志<br>
	 * MASTER:主人； SLAVE:奴隶； FREEMAN:自由身份
	 */
	private String subordinateType;

	/**
	 * 权重值：选择抽取变量创建数据集时，被选中的变量权重值会+1
	 */
	@TableField("weights")
	private Integer weights;

	/**
	 * 源字段列表
	 */
	@TableField(exist = false)
	private List<Iecpath> iecpathList;

	/**
	 * 条件
	 */
	@TableField(exist = false)
	private String condition;

	/**
	 * 值
	 */
	@TableField(exist = false)
	private Integer conditionValue;

	/**
	 * 中文分词列表
	 */
	@TableField(exist = false)
	private List<String> cnWordList;

	/**
	 * 显示的标签
	 */
	@TableField(exist = false)
	private String label;

	public String getLabel() {
		return this.getModelEntryCN() + "(" + this.getModelEntryEN() + ")";
	}

}
