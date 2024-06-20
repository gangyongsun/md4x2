package cn.com.goldwind.md4x.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.Assert;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AbstractLocaleContextResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * 
 * @Title: MessageConfig.java
 * @Package cn.com.goldwind.md4x.config
 * @description 国际化配置
 * @author 孙永刚
 * @date Sep 14, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Configuration
public class MessageConfig extends AbstractLocaleContextResolver {
	/**
	 * 国际化文件前缀
	 */
	@Value("${spring.messages.basename}")
	public String[] basenames;

	@Bean(name = "messageSource")
	public ResourceBundleMessageSource resourceBundleMessageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		if (basenames != null) {
			for (int i = 0; i < basenames.length; i++) {
				String basename = basenames[i];
				Assert.hasText(basename, "Basename must not be empty");
				this.basenames[i] = basename.trim();
			}
			source.setBasenames(basenames);
		} else {
			this.basenames = new String[0];
			source.setBasename(basenames[0]);
		}
		source.setDefaultEncoding("UTF-8");
		source.setUseCodeAsDefaultMessage(true);
		return source;
	}

	@Bean
	public LocaleResolver localeResolver() {
//		SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
//		sessionLocaleResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
//		return sessionLocaleResolver;
		return new MyLocalResolver();
	}

	/**
	 * 国际化，设置url识别参数
	 *
	 * @return
	 */
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

	@Override
	public LocaleContext resolveLocaleContext(HttpServletRequest request) {
		return null;
	}

	@Override
	public void setLocaleContext(HttpServletRequest request, HttpServletResponse response, LocaleContext localeContext) {
	}
}