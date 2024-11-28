package com.serviceapp.elk.transactionService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableJpaRepositories(basePackages = "com.serviceapp.elk.TransactionService.repo")
@EnableElasticsearchRepositories(basePackages = "com.serviceapp.elk.TransactionService.repo")
@SpringBootApplication 
public class TransactionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionServiceApplication.class, args);
	}

}
