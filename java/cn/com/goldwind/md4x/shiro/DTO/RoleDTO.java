package cn.com.goldwind.md4x.shiro.DTO;

import lombok.Data;

/**
 * 
 * @Title: RoleDTO.java
 * @Package cn.com.goldwind.md4x.shiro.DTO
 * @description 角色列表DTO
 * @author 孙永刚
 * @date Jul 24, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Data
public class RoleDTO {
	
	/**
	 * 用户角色关系ID
	 */
	private Integer userRoleId;
	
	/**
	 * 用户ID
	 */
	private Integer userId;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 用户昵称
	 */
	private String nickName;
	
	/**
	 * 角色ID
	 */
	private Integer roleId;

	/**
	 * 角色是否可用:true为可用,false为不可用
	 */
	private Boolean available;

	/**
	 * 角色删除标志:true为删除,false为未删除
	 */
	private Boolean deleted;

	/**
	 * 角色名
	 */
	private String roleName;

	/**
	 * 角色描述
	 */
	private String description;

	/**
	 * 角色组(部门)ID
	 */
	private Integer roleGroupId;
	
	/**
	 * 角色组(部门)名称
	 */
	private String roleGroupName;
	
	/**
	 * 角色组(部门)编码
	 */
	private String roleGroupCode;
	
	/**
	 * 角色组(部门)描述
	 */
	private String roleGroupDescription;

}
