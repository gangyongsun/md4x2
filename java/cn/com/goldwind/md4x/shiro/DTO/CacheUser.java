package cn.com.goldwind.md4x.shiro.DTO;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * 缓存用户信息
 * 
 * @author alvin
 *
 */
@Data
@Builder
public class CacheUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	
	private String userName;
	
	private String token;
	
	private Integer state;
	
	private String email;

}
