package com.fisa.wooriarte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WooriarteApplication {

	public static void main(String[] args) {
		SpringApplication.run(WooriarteApplication.class, args);
	}

}