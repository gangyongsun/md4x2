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
 * @Title:  Role.java
 * @Package cn.com.goldwind.md4x.shiro.domain.entity 
 * @description 角色类 
 * @author 孙永刚
 * @date Jul 22, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved. 
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role")
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(value = "roleId", type = IdType.AUTO)
	private Integer roleId;

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
	 * 角色名
	 */
	@TableField("role_name")
	private String roleName;

	/**
	 * 角色描述
	 */
	@TableField("description")
	private String description;
	
	/**
	 * 角色组(部门)ID
	 */
	@TableField("roleGroupId")
	private Integer roleGroupId;

}
