package cn.com.goldwind.md4x;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
// spring 事务的总开关，开启后事务支持后，在访问数据库的 Service 方法上添加注解 @Transactional 即可
@EnableTransactionManagement
// 由于使用了@EnableWebMvc，所以web.xml可以简化，只需要启动Spring IoC容器、添加DispatcherServlet配置即可
@EnableWebMvc
public class Md4xApplication {

	public static void main(String[] args) {
		SpringApplication.run(Md4xApplication.class, args);
	}

}
