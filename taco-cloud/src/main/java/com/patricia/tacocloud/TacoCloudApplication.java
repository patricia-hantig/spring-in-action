package com.patricia.tacocloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication												// Spring Boot Application
public class TacoCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(TacoCloudApplication.class, args);	// runs the application
																	// performs the bootstrapping of the application by creating the Spring application context
	}

}
