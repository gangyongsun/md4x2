package cn.com.goldwind.md4x.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "aws")
@Data
public class ApplicationProperties {
	private String credentialFlag;
	private String ak;
	private String sk;
	private String region;
}
