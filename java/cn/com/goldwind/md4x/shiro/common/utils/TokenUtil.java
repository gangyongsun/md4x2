package cn.com.goldwind.md4x.shiro.common.utils;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author alvin
 *
 */
public class TokenUtil {

	// Token过期时间(min:分钟)
	public final static int EXPIRE = 120;

	/**
	 * 获取请求的token
	 * 
	 * @param httpRequest
	 * @return
	 */
	public static String getRequestToken(HttpServletRequest httpRequest) {
		// 从header中获取token
		String token = httpRequest.getHeader("token");
		// 如果header中不存在token，则从参数中获取token
		if (StringUtils.isBlank(token)) {
			token = httpRequest.getParameter("token");
		}
		return token;
	}
}
