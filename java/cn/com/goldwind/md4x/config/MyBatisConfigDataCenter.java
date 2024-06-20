/**   
* @Title: MyBatisConfigDataCenter.java 
* @Package cn.com.goldwind.md4x.config 
* @Description: TODO(用一句话描述该文件做什么) 
* @author wangguiyu  
* @date Apr 10, 2020 4:11:00 PM 
* @version V1.0   
*/
package cn.com.goldwind.md4x.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * @ClassName: MyBatisConfigMetaData
 * @Description: DataCenter库 SqlSessionFactory
 * @author wangguiyu
 * @date Apr 10, 2020 4:11:00 PM 
 *
 */
@Configuration
public class MyBatisConfigDataCenter {
	@Autowired
	@Qualifier("dataCenter")
	DataSource dataCenter;
	
	@Bean
	SqlSessionFactory sqlSessionFactoryBeanDataCenter() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataCenter);
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		factoryBean.setMapperLocations(resolver.getResources("classpath:mapper/datacenter/*.xml"));
		return factoryBean.getObject();
	}
	
	@Bean
	SqlSessionTemplate sqlSessionTemplateDataCenter() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactoryBeanDataCenter());	
	}
	
	
	
	
}
