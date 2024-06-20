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
 * @Title: User.java
 * @Package cn.com.goldwind.md4x.shiro.domain.entity
 * @description 用户实体类：可以表示金风用户和非金风用户
 * @author 孙永刚
 * @date Jul 22, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
// @Data 相当于 @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode 这 5 个注解的合集
@Data
@NoArgsConstructor
// 1. callSuper = true， 根据子类自身的字段值和从父类继承的字段值 来生成 hashcode，当两个子类对象比较时，只有子类对象的本身的字段值和继承父类的字段值都相同，equals 方法的返回值是 true。
// 2. callSuper = false，根据子类自身的字段值来生成 hashcode， 当两个子类对象比较时，只有子类对象的本身的字段值相同，父类字段值可以不同，equals 方法的返回值是 true。
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
public class User implements Serializable {
	//定义程序序列化ID(相当于身份认证)，主要用于程序的版本控制，保持不同版本的兼容性，在程序版本升级时避免程序报出版本不一致的错误
	private static final long serialVersionUID = 1L;

	@TableId(value = "userId", type = IdType.AUTO)
	private Integer userId;

	/**
	 * 用户名(非金风用户)或OA号(金风用户)
	 */
	@TableField("user_name")
	private String userName;

	/**
	 * 密码(非金风用户用)
	 */
	@TableField("passwd")
	private String password;

	/**
	 * 是否是金风用户
	 */
	@TableField("goldwinder")
	private Boolean goldwinder;

	/**
	 * 是否可用:true为可用,false为不可用
	 */
	@TableField("available")
	private Boolean available;

	/**
	 * 是否删除:true为删除,false为未删除
	 */
	@TableField("deleted")
	private Boolean deleted;

	/**
	 * 昵称
	 */
	@TableField("nick_name")
	private String nickName;
	
	/**
	 * 岗位编码
	 */
	@TableField("position_code")
	private String positionCode;

	/**
	 * 岗位名称
	 */
	@TableField("position_name")
	private String positionName;

	/**
	 * 邮箱
	 */
	@TableField("email")
	private String email;

	/**
	 * 密码盐
	 */
	@TableField("salt")
	private String salt;

	/**
	 * 创建时间
	 */
	@TableField("create_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 最后登录时间
	 */
	@TableField("last_login_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date lastLoginTime;
	/**
	 * 更新时间
	 */
	@TableField("update_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	/**
	 * 过期时间(该字段目前没用到)
	 */
	@TableField("expired_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date expiredTime;

	/**
	 * 用户所属部门编号
	 */
	@TableField("department_code")
	private String departmentCode;
	
	/**
	 * 用户所属部门编号
	 */
	@TableField("department_name")
	private String departmentName;
	
	/**
	 * 所在中心编码
	 */
	@TableField("center_code")
	private String centerCode;

	/**
	 * 所在中心名称
	 */
	@TableField("center_name")
	private String centerName;

	/**
	 * 密码盐. 重新对盐重新进行了定义，用户名+salt，这样就不容易被破解，可以采用多种方式定义加盐
	 *
	 * @return
	 */
	public String getCredentialsSalt() {
		return getUserName() + getSalt();
	}

	public User(User user) {
	}

}
