package cn.com.goldwind.md4x.shiro.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.com.goldwind.md4x.shiro.auth.AuthFilter;
import cn.com.goldwind.md4x.shiro.auth.AuthRealm;

/**
 * 
 * @author alvin
 *
 */
@Configuration
public class ShiroConfig {

	@Bean("securityManager")
	public SecurityManager securityManager(AuthRealm authRealm) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(authRealm);
		securityManager.setRememberMeManager(null);
		return securityManager;
	}

	@Bean("shiroFilter")
	public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
		shiroFilter.setSecurityManager(securityManager);
		// oauth过滤
		Map<String, Filter> filters = new HashMap<>();
		filters.put("auth", new AuthFilter());
		shiroFilter.setFilters(filters);
		Map<String, String> filterMap = new LinkedHashMap<>();
		// =======德阳接口=======
		// 本地上传
//		filterMap.put("/localupload/**", "anon");
		// 数据集
//		filterMap.put("/datamart/**", "anon");
		// OA认证
//		filterMap.put("/oa/login", "anon");

		// =======贵宇接口=======
		// 测试
//		filterMap.put("/demo/*", "anon");
		// 风场风机等
//		filterMap.put("/data/*", "anon");

		// 系统登录
		filterMap.put("/sys/login", "anon");
		// 新用户注册
		filterMap.put("/user/add", "anon");

		filterMap.put("/webjars/**", "anon");
		filterMap.put("/druid/**", "anon");

		// swagger
		filterMap.put("/swagger/**", "anon");
		filterMap.put("/v2/api-docs", "anon");
		filterMap.put("/swagger-ui.html", "anon");
		filterMap.put("/swagger-resources/**", "anon");
		filterMap.put("/data/healthy", "anon");

		// 对所有用户认证
		filterMap.put("/**", "auth,authc");

		shiroFilter.setFilterChainDefinitionMap(filterMap);
		return shiroFilter;
	}

	/**
	 * Shiro生命周期处理器
	 */
	@Bean("lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

//	/***
//	 * 授权所用配置
//	 *
//	 * @return
//	 */
//	@Bean
//	public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
//		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
//		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
//		return defaultAdvisorAutoProxyCreator;
//	}

	/***
	 * 使授权注解起作用,如不想配置可以在pom文件中加入 <dependency>
	 * <groupId>org.springframework.boot</groupId>
	 * <artifactId>spring-boot-starter-aop</artifactId> </dependency>
	 * 
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}
}
