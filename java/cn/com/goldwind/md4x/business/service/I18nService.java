package cn.com.goldwind.md4x.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * 国际化服务类
 * 
 * @Title: I18nService.java
 * @Package cn.com.goldwind.md4x.business.service
 * @description 国际化服务类
 * @author 孙永刚
 * @date Sep 11, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
@Service
public class I18nService {
	@Autowired
	private MessageSource messageSource;

	public I18nService(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public String getMessage(String msgKey, Object[] args) {
		return messageSource.getMessage(msgKey, args, LocaleContextHolder.getLocale());
	}

	public String getMessage(String msgKey) {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(msgKey, null, locale);
	}
}