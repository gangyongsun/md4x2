package cn.com.goldwind.md4x.shiro.auth;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 
 * @author alvin
 *
 */
public class AuthToken extends UsernamePasswordToken {
	private static final long serialVersionUID = 4737180344814003372L;

	private String token;

	public AuthToken(String token) {
		this.token = token;
	}

	@Override
	public Object getPrincipal() {
		return token;
	}

	@Override
	public Object getCredentials() {
		return token;
	}
}
