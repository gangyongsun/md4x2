package cn.com.goldwind.md4x.config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

/**
 * 
 * @Title: DataVizLocalResolver.java
 * @Package cn.com.goldwind.md4x.config
 * @description 自定义解析器，解析http请求中的language信息
 * @author 孙永刚
 * @date Sep 16, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
public class MyLocalResolver extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {
	List<Locale> LOCALES = Arrays.asList(new Locale("en_US"), new Locale("zh_CN"));

	@Override
	public Locale resolveLocale(HttpServletRequest httpServletRequest) {
		// Locale defaultLocale = new AcceptHeaderLocaleResolver().getDefaultLocale();
		// 获取地址上的参数
		String parameter = httpServletRequest.getHeader("lang");
		Locale locale = Locale.getDefault();
		// 判断是否为空
		if (!StringUtils.isEmpty(parameter)) {
			String[] s = parameter.split("_");
			locale = new Locale(s[0], s[1]);
		}
		return locale;
	}

}