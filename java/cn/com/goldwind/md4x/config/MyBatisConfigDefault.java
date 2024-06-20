/**   
* @Title: MyBatisConfigDefault.java 
* @Package cn.com.goldwind.md4x.config 
* @Description: 默认数据源
* @author wangguiyu  
* @date Apr 10, 2020 4:11:00 PM 
* @version V1.0   
*/
package cn.com.goldwind.md4x.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @ClassName: MyBatisConfigDefault
 * @Description: 默认 SqlSessionFactory
 * @author wangguiyu
 * @date Apr 10, 2020 4:11:00 PM
 *
 */
@Configuration
@MapperScan(value = { "cn.com.goldwind.md4x.shiro.domain.mapper", "cn.com.goldwind.md4x.business.dao.datamart",
		"cn.com.goldwind.md4x.business.dao.zeppelin.mapper" }, sqlSessionFactoryRef = "sqlSessionFactoryBeanDefault")
public class MyBatisConfigDefault {
	@Autowired
	@Qualifier("defaultDataSource")
	DataSource defaultDataSource;

	@Bean
	@Primary
	SqlSessionFactory sqlSessionFactoryBeanDefault() throws Exception {
		MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
		factoryBean.setDataSource(defaultDataSource);
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		factoryBean.setMapperLocations(resolver.getResources("classpath:mapper/*/*.xml"));
		return factoryBean.getObject();
	}

	@Bean
	SqlSessionTemplate sqlSessionTemplateDefault() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactoryBeanDefault());
	}

}
