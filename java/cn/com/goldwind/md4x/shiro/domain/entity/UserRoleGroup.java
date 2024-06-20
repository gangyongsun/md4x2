package cn.com.goldwind.md4x.shiro.domain.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 
 * @Title: UserRoleGroup.java
 * @Package cn.com.goldwind.md4x.shiro.domain.entity
 * @description 用户部门关系类:记录用户曾经所在的部门和时间范围
 * @author 孙永刚
 * @date Jul 22, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_role_group")
public class UserRoleGroup implements Serializable {
	private static final long serialVersionUID = 3894744026063832165L;

	/**
	 * 自增主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 用户ID
	 */
	@TableField("user_id")
	private Integer userId;

	/**
	 * 角色组(部门)ID
	 */
	@TableField("role_group_id")
	private Integer roleGroupId;

	/**
	 * 是否还属于当前部门
	 */
	@TableField("at_group")
	private Boolean atGroup;

	/**
	 * 用户所属部门：所属开始时间
	 */
	@TableField("begin_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date beginTime;

	/**
	 * 用户所属部门：所属结束时间
	 */
	@TableField("end_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date endTime;

}
