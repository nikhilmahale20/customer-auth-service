package com.neobank.customer_auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CustomerAuthServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(
				CustomerAuthServiceApplication.class,
				args
		);
	}
}