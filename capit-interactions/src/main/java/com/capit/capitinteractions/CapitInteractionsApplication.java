package com.capit.capitinteractions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.capit.capitinteractions.domain")
@EnableJpaRepositories(basePackages = "com.capit.capitinteractions.domain")
public class CapitInteractionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapitInteractionsApplication.class, args);
	}

}
