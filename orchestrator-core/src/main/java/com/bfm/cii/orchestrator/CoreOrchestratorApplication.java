package com.bfm.cii.orchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;

@SpringBootApplication(exclude = {
        KafkaAutoConfiguration.class
})
public class CoreOrchestratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreOrchestratorApplication.class, args);
	}

}
