package cn.com.goldwind.md4x.shiro.web.exception;

/**
 * 登录异常
 * 
 * @author alvin
 *
 */
public class LoginException extends RuntimeException {
	private static final long serialVersionUID = 4721907800025813211L;

	public LoginException() {
		super();
	}

	public LoginException(String message) {
		super(message);
	}

	public LoginException(String message, Throwable cause) {
		super(message, cause);
	}
}
