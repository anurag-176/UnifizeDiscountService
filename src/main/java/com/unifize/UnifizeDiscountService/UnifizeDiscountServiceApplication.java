package com.unifize.UnifizeDiscountService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class UnifizeDiscountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnifizeDiscountServiceApplication.class, args);
	}

}
