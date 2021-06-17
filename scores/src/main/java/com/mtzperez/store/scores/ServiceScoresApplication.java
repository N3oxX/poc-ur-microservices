package com.mtzperez.store.scores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ServiceScoresApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceScoresApplication.class, args);
	}

}
