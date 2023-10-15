package com.example.kafkaretryjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
@ConfigurationPropertiesScan("kafkaretryjob")
public class KafkaRetryJobApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaRetryJobApplication.class, args);
	}

}
