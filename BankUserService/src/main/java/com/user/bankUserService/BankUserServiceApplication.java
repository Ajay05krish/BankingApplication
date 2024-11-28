package com.user.bankUserService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableElasticsearchRepositories(basePackages = "com.user.BankUserService.repository")
@EnableJpaRepositories(basePackages = "com.user.BankUserService.repository")
@SpringBootApplication
public class BankUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankUserServiceApplication.class, args);
	}

}


