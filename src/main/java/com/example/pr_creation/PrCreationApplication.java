package com.example.pr_creation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.example")
@EnableFeignClients(basePackages = "com.example.pr_creation.dto")
@EnableAsync
public class PrCreationApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrCreationApplication.class, args);
	}
//testing
}
