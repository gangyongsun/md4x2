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
 * @Title:  RoleGroup.java
 * @Package cn.com.goldwind.md4x.shiro.domain.entity 
 * @description 角色组(部门)类：角色组与角色是一对多的关系；用户与角色组是多对一的关系；  
 * @author 孙永刚
 * @date Jul 22, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved. 
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role_group")
public class RoleGroup implements Serializable {
	private static final long serialVersionUID = -6104719399745563648L;

	/**
	 * 自增主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 角色组(部门)名称
	 */
	@TableField("role_group_name")
	private String roleGroupName;
	
	/**
	 * 角色组(部门)名称编号
	 */
	@TableField("role_group_code")
	private String roleGroupCode;

	/**
	 * 角色组(部门)名称描述
	 */
	@TableField("description")
	private String description;

	/**
	 * 是否可用:true为可用,false为不可用
	 */
	@TableField("available")
	private Boolean available;

	/**
	 * 删除状态:true为删除,false为未删除
	 */
	@TableField("deleted")
	private Boolean deleted;

}
