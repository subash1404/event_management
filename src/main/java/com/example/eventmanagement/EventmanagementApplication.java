package com.example.eventmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan({"com.example.eventmanagement.query","com.example.eventmanagement.util","com.example.eventmanagement.security","com.example.eventmanagement.resolver","com.example.eventmanagement.services","com.example.eventmanagement.repositories","com.example.eventmanagement.mutation"})
@EntityScan("com.example.eventmanagement.models")
@EnableJpaRepositories("com.example.eventmanagement.repositories")
public class EventmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventmanagementApplication.class, args);
	}

}
