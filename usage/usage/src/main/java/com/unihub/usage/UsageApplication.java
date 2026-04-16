package com.unihub.usage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableFeignClients
@SpringBootApplication
@EnableAspectJAutoProxy
public class UsageApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsageApplication.class, args);
	}

}
