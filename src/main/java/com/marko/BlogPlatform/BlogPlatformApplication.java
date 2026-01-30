package com.marko.BlogPlatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BlogPlatformApplication {
	public static void main(String[] args) {
		SpringApplication.run(BlogPlatformApplication.class, args);
	}
}