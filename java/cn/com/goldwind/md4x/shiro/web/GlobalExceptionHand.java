package cn.com.goldwind.md4x.shiro.web;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.management.ServiceNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import cn.com.goldwind.md4x.business.bo.ResultInfoDTO;
import cn.com.goldwind.md4x.business.service.I18nService;
import cn.com.goldwind.md4x.shiro.web.exception.LoginException;
import cn.com.goldwind.md4x.util.i18n.ExceptionCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Title: GlobalExceptionHand.java
 * @Package cn.com.goldwind.md4x.shiro.web
 * @description 全局异常处理
 * @author 孙永刚
 * @date Sep 16, 2020
 * @version V1.0
 * @Copyright: 2020 www.goldwind.com.cn Inc. All rights reserved.
 *
 */
//@EnableWebMvc
//@RestControllerAdvice
@Slf4j
public class GlobalExceptionHand {
	@Resource
	private ResultInfoDTO<Object> response;

	@Autowired
	protected I18nService i18nService;

	private ResultInfoDTO<Object> generateResponse(String message, String exceptionMessage) {
		log.error(message, exceptionMessage);
		response.setCode(ResultInfoDTO.ERROR);
		response.setMessage(message);
		return response;
	}

	/**
	 * 400：缺少请求参数
	 * 
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResultInfoDTO<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
		String msg = i18nService.getMessage(ExceptionCode.REQUEST_PARAMATER_MISSING);
		return generateResponse(msg, e.getMessage());
	}

	/**
	 * 400：参数解析失败
	 * 
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResultInfoDTO<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		String msg = i18nService.getMessage(ExceptionCode.REQUEST_PARAMATER_PARSE_FAILED);
		return generateResponse(msg, e.getMessage());
	}

	/**
	 * 400：方法参数无效
	 * 
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResultInfoDTO<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		String msg = i18nService.getMessage(ExceptionCode.PARAMATER_INVALIDED);
		return generateResponse(msg, handleBindingResult(e.getBindingResult()));
	}

	/**
	 * 400：参数绑定失败
	 * 
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	public ResultInfoDTO<Object> handleBindException(BindException e) {
		String msg = i18nService.getMessage(ExceptionCode.PARAMETER_BINDING_FAILED);
		return generateResponse(msg, handleBindingResult(e.getBindingResult()));
	}

	/**
	 * 400：参数验证失败
	 * 
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	public ResultInfoDTO<Object> handleServiceException(ConstraintViolationException e) {
		Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
		String msg = i18nService.getMessage(ExceptionCode.PARAMETER_VERIFICATION_FAILED);
		return generateResponse(msg, violations.iterator().next().getMessage());
	}

	/**
	 * 400：参数验证失败
	 * 
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ValidationException.class)
	public ResultInfoDTO<Object> handleValidationException(ValidationException e) {
		String msg = i18nService.getMessage(ExceptionCode.PARAMETER_VERIFICATION_FAILED);
		return generateResponse(msg, e.getMessage());
	}

	/**
	 * 401：登录异常
	 * 
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(LoginException.class)
	public ResultInfoDTO<Object> handleLoginException(LoginException e) {
		String msg = i18nService.getMessage(ExceptionCode.LOGIN_EXCEPTION);
		return generateResponse(msg, e.getMessage());
	}

	/**
	 * 403：用户无权限
	 * 
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(UnauthorizedException.class)
	public ResultInfoDTO<Object> handleLoginException(UnauthorizedException e) {
		String msg = i18nService.getMessage(ExceptionCode.PERMISSION_LACK);
		return generateResponse(msg, e.getMessage());
	}

	/**
	 * 405：不支持当前请求方法
	 * 
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResultInfoDTO<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		String msg = i18nService.getMessage(ExceptionCode.METHOD_NOT_SUPPORT);
		return generateResponse(msg, e.getMessage());
	}

	/**
	 * 415：不支持当前媒体类型
	 * 
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResultInfoDTO<Object> handleHttpMediaTypeNotSupportedException(Exception e) {
		String msg = i18nService.getMessage(ExceptionCode.MEDIA_TYPE_NOT_SUPPORT);
		return generateResponse(msg, e.getMessage());
	}

	/**
	 * 422：所上传文件大小超过最大限制
	 * 
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResultInfoDTO<Object> handleMaxUploadSizeExceededException(Exception e) {
		String msg = i18nService.getMessage(ExceptionCode.UPLOAD_FILE_SIZE_LIMITED);
		return generateResponse(msg, e.getMessage());
	}

	/**
	 * 500:服务器内部异常
	 * 
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ServiceNotFoundException.class)
	public ResultInfoDTO<Object> handleServiceException(ServiceNotFoundException e) {
		String msg = i18nService.getMessage(ExceptionCode.SERVICE_INTERNAL_EXCEPTION);
		return generateResponse(msg, e.getMessage());
	}

	/**
	 * 500:服务器内部异常
	 * 
	 * @param e
	 * @return
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ResultInfoDTO<Object> handleException(Exception e) {
		String msg = i18nService.getMessage(ExceptionCode.SERVICE_INTERNAL_EXCEPTION);
		return generateResponse(msg, e.getMessage());
	}

	/**
	 * 处理参数绑定异常，并拼接出错的参数异常信息
	 * 
	 * @param result
	 * @return
	 */
	private String handleBindingResult(BindingResult result) {
		if (result.hasErrors()) {
			final List<FieldError> fieldErrors = result.getFieldErrors();
			return fieldErrors.iterator().next().getDefaultMessage();
		}
		return null;
	}
}
