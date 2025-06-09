package com.infy;

import org.springframework.boot.SpringApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main Spring Boot Application for Customer Rewards
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.infy")
public class CustomerRewardsApplication {

	private static final Logger logger = LoggerFactory.getLogger(CustomerRewardsApplication.class);

	public static void main(String[] args) {
		logger.info("APPLICATION STARTED");

		SpringApplication.run(CustomerRewardsApplication.class, args);

	}

}