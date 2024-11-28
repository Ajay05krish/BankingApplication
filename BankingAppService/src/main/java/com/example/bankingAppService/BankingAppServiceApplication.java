package com.example.bankingAppService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableElasticsearchRepositories(basePackages = "com.example.BankingAppService.Repository")
@EnableJpaRepositories(basePackages = "com.example.BankingAppService.Repository")
@SpringBootApplication
public class BankingAppServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingAppServiceApplication.class, args);
	}

}
