package com.elearning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.elearning.config.KafkaConfig;
import com.elearning.config.StompConfig;
import com.elearning.config.UiConfig;


@SpringBootApplication
@Import({
		KafkaConfig.class, 
		UiConfig.class,
		StompConfig.class
	})
public class KafkaChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaChatApplication.class, args);
	}
	
}
