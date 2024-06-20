package cn.com.goldwind.md4x.shiro.domain.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 
 * @Title: SysToken.java
 * @Package cn.com.goldwind.md4x.shiro.domain.entity
 * @description 用户登录token记录类
 * @author 孙永刚
 * @date Jul 22, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_token")
public class SysToken implements Serializable {
	private static final long serialVersionUID = 5479283782110273605L;

	/**
	 * 用户ID
	 */
	@TableId(value = "userId")
	private Integer userId;

	/**
	 * token值
	 */
	@TableField("token")
	private String token;

	/**
	 * 过期时间(目前逻辑里还没用到)
	 */
	@TableField("expire_time")
	private Date expireTime;

	/**
	 * 更新时间(目前逻辑里还没用到)
	 */
	@TableField("update_time")
	private Date updateTime;
}
