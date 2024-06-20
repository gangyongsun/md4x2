package cn.com.goldwind.md4x.shiro.domain.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 
 * @Title:  Permission.java
 * @Package cn.com.goldwind.md4x.shiro.domain.entity 
 * @description 权限类  
 * @author 孙永刚
 * @date Jul 22, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved. 
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_permission")
public class Permission implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String BUTTON = "button";

	public static final String MENU = "menu";

	/**
	 * 主键
	 */
	@TableId(value = "permissionId", type = IdType.AUTO)
	private Integer permissionId;

	/**
	 * 权限名称
	 */
	@TableField("permission_name")
	private String permissionName;

	/**
	 * 权限描述
	 */
	@TableField("description")
	private String description;

	/**
	 * 权限类型：button or menu
	 */
	@TableField("permission_type")
	private String permissionType;

	/**
	 * 权限分类(前端做树形结构用)
	 */
	@TableField("group_name")
	private String groupName;

	/**
	 * 是否可用:true为可用,false为不可用
	 */
	@TableField("available")
	private Boolean available;

	/**
	 * 删除标志:true为删除,false为未删除
	 */
	@TableField("deleted")
	private Boolean deleted;

	/**
	 * 权限相关URL(该字段暂时不用)
	 */
	@TableField("url")
	private String url;

}
