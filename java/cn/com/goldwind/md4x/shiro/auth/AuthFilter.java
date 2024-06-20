package cn.com.goldwind.md4x.shiro.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;

import cn.com.goldwind.md4x.business.bo.ResultInfoDTO;
import cn.com.goldwind.md4x.shiro.common.utils.TokenUtil;

/**
 * auth过滤器
 * 
 * @author alvin
 *
 */
public class AuthFilter extends AuthenticatingFilter {
	
	@Resource
	private ResultInfoDTO<Object> response;

	/**
	 * 生成自定义token
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		// 获取请求token
		String token = TokenUtil.getRequestToken((HttpServletRequest) request);

		return new AuthToken(token);
	}

	/**
	 * 步骤1.所有请求全部拒绝访问
	 *
	 * @param request
	 * @param response
	 * @param mappedValue
	 * @return
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		if (((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())) {
			return true;
		}
		return false;
	}

	/**
	 * 步骤2，拒绝访问的请求，会调用onAccessDenied方法，onAccessDenied方法先获取 token，再调用executeLogin方法
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		// 获取请求token，如果token不存在，直接返回
		String token = TokenUtil.getRequestToken((HttpServletRequest) request);
		if (StringUtils.isBlank(token)) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
			//httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtil.getOrigin());
			httpResponse.setCharacterEncoding("UTF-8");
			Map<String, Object> result = new HashMap<>();
			result.put("code", 566);
			result.put("message", "请先登录");
			String json = JSON.toJSONString(result);
			httpResponse.getWriter().print(json);
			return false;
		}
		return executeLogin(request, response);
	}

	/**
	 * token失效时候调用
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setContentType("application/json;charset=utf-8");
		httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
		//httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtil.getOrigin());
		httpResponse.setCharacterEncoding("UTF-8");
		try {
			// 处理登录失败的异常
			Throwable throwable = e.getCause() == null ? e : e.getCause();
			throwable.printStackTrace();
			Map<String, Object> result = new HashMap<>();
			result.put("code", 567);
			result.put("message", "登录凭证已失效，请重新登录");

			String json = JSON.toJSONString(result);
			httpResponse.getWriter().print(json);
		} catch (IOException e1) {
		}
		return false;
	}

}
