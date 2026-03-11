package com.unihub.s3;

import com.unihub.s3.config.S3ConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableConfigurationProperties(S3ConfigurationProperties.class)
public class S3Application {

	public static void main(String[] args) {
		SpringApplication.run(S3Application.class, args);
	}

}
