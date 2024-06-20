package cn.com.goldwind.md4x.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.SortControl;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * @author 50343 OA号和个人域密码认证
 */
public class OAAuthUtil {

	/**
	 * @param url      地址
	 * @param baseDN   域名
	 * @param bindDN   账号
	 * @param loginPWD 密码
	 * @param username 个人账号
	 * @param password 个人密码
	 * @return 成功：true 失败：false
	 */
	public static boolean checkingUser(String url, String baseDN, String bindDN, String loginPWD, String username,
			String password) {
		boolean result = false;
		LdapContext dirContext = null;
		DirContext context = null;
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			Control[] sortConnCtls = new SortControl[1];
			sortConnCtls[0] = new SortControl("sAMAccountName", Control.CRITICAL);
			// 参数设置
			env.put(Context.PROVIDER_URL, url + baseDN);
			env.put(Context.SECURITY_PRINCIPAL, bindDN);
			env.put(Context.SECURITY_CREDENTIALS, loginPWD);
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.BATCHSIZE, "50");
			env.put("com.sun.jndi.ldap.connect.timeout", "3000");
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put("com.sun.jndi.ldap.connect.pool", "true");
			env.put("com.sun.jndi.ldap.connect.pool.maxsize", "3");
			env.put("com.sun.jndi.ldap.connect.pool.prefsize", "1");
			env.put("com.sun.jndi.ldap.connect.pool.timeout", "300000");
			env.put("com.sun.jndi.ldap.connect.pool.initsize", "1");
			env.put("com.sun.jndi.ldap.connect.pool.authentication", "simple");

			// 连接认证
			dirContext = new InitialLdapContext(env, sortConnCtls);
			dirContext.setRequestControls(sortConnCtls);
			SearchControls controls = new SearchControls();
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String filter = "(sAMAccountName=" + username + ")";
			NamingEnumeration<?> answer = dirContext.search("", filter, controls);
			String userDN = null;
			while (answer.hasMoreElements()) {
				// 获得用户DN
				userDN = ((NameClassPair) answer.nextElement()).getName();

			}
			// 个人认证
			if ("".equals(password)) {
				password = " ";
			}
			Hashtable<String, String> personenv = new Hashtable<String, String>();
			personenv.put(Context.PROVIDER_URL, url + baseDN);
			personenv.put(Context.SECURITY_PRINCIPAL, userDN + "," + baseDN);
			personenv.put(Context.SECURITY_CREDENTIALS, password);
			personenv.put(Context.SECURITY_AUTHENTICATION, "simple");//
			personenv.put("com.sun.jndi.ldap.connect.timeout", "1000");
			personenv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

			context = new InitialDirContext(personenv);
			result = true;
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			try {
				if (context != null) {
					context.close();
					context=null;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				if (dirContext != null) {
					dirContext.close();
					dirContext=null;
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * OA登录验证
	 * 
	 * @param userName OA号
	 * @param password OA密码
	 * @param url      URL
	 * @param params
	 * @return
	 */
	public static String authClient(String userName, String password, String url, Map<String, Object> params) {
		String result = "{\"code\":\"001\",\"message\":\"认证失败\",\"employee\":{}}";
		try {
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials(userName,
					password);
			credentialsProvider.setCredentials(AuthScope.ANY, usernamePasswordCredentials);
			CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider)
					.build();
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("Content-type", "application/json; charset=utf-8");
			httpPost.setHeader("Accept", "application/json");
			if (params == null) {
				params = new HashMap<>();
			}
			String jsonString = JSONObject.toJSONString(params);
			httpPost.setEntity(new StringEntity(jsonString, Charset.forName("UTF-8")));
			CloseableHttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			httpClient.close();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return result;
	}
}
