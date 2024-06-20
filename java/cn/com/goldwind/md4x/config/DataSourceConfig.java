/**   
* @Title: DataSourceConfig.java 
* @Package cn.com.goldwind.md4x.config 
* @Description: TODO(用一句话描述该文件做什么) 
* @author wangguiyu  
* @date Apr 10, 2020 4:01:24 PM 
* @version V1.0   
*/
package cn.com.goldwind.md4x.config;



import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

/**
 * @ClassName: DataSourceConfig 
 * @Description: 配置多个数据源
 * @author wangguiyu
 * @date Apr 10, 2020 4:01:24 PM 
 *
 */
@Configuration
public class DataSourceConfig {
	
	@Bean
	@ConfigurationProperties("spring.datasource.datacenter")
	DataSource dataCenter() {
		return DruidDataSourceBuilder.create().build();
	}
	
	@Bean
	@ConfigurationProperties("spring.datasource")
	DataSource defaultDataSource() {
		return DruidDataSourceBuilder.create().build();
	}
	
	@Bean
	@ConfigurationProperties("spring.datasource.datamartmap")
	DataSource datamartMap() {
		return DruidDataSourceBuilder.create().build();
	}
}
