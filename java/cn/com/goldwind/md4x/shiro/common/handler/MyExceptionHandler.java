package cn.com.goldwind.md4x.shiro.common.handler;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.goldwind.md4x.business.bo.ResultInfoDTO;

/**
 * 
 * @author alvin
 *
 */
@ControllerAdvice
public class MyExceptionHandler {

	@ExceptionHandler(value = AuthorizationException.class)
	@ResponseBody
	public ResultInfoDTO<Object> handleException(AuthorizationException e) {
		ResultInfoDTO<Object> response=new ResultInfoDTO<Object>();
		// 获取错误中中括号的内容
		String message = e.getMessage();
		String msg = message.substring(message.indexOf("[") + 1, message.indexOf("]"));
		// 判断是角色错误还是权限错误
		if (message.contains("role")) {
			response.setMessage("对不起，您没有<" + msg + ">角色！");
		} else if (message.contains("permission")) {
			response.setMessage("对不起，您没有<" + msg + ">权限！");
		} else {
			response.setMessage("对不起，您的权限有误！");
		}
		response.setCode(ResultInfoDTO.ERROR);
		return response;
	}
}
