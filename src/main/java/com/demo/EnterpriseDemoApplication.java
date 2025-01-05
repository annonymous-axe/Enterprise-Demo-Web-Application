package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class EnterpriseDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnterpriseDemoApplication.class, args);
	}

}
