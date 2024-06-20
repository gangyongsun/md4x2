package cn.com.goldwind.md4x.shiro.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 
 * @Title: RolePermission.java
 * @Package cn.com.goldwind.md4x.shiro.domain.entity
 * @description 角色权限关系类：表示你角色拥有的权限
 * @author 孙永刚
 * @date Jul 22, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_role_permission")
public class RolePermission implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 权限ID
	 */
	@TableField("permissionId")
	private Integer permissionId;

	/**
	 * 角色ID
	 */
	@TableField("roleId")
	private Integer roleId;

}
