package com.elearning.config;

import java.util.Map;

import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.google.common.collect.ImmutableMap;

@EnableKafka
@Configuration
public class KafkaConfig {

	@Bean
	public ProducerFactory<Integer, String> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs(), null, new JsonSerializer<String>());
	}

	@Bean
	public KafkaTemplate<Integer, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public Map<String, Object> producerConfigs() {

		return ImmutableMap.<String, Object>builder()
					.put("bootstrap.servers", "localhost:29092")
					.put("key.serializer", IntegerSerializer.class)
					.put("value.serializer", JsonSerializer.class)
					
					.put("group.id", "spring-boot-test") // needed but don't know why...

				.build();
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<Integer, String> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

	@Bean
	public ConsumerFactory<Integer, String> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigs(), null, new JsonDeserializer<>(String.class));
	}

	@Bean
    public Map<String, Object> consumerConfigs() {
		return ImmutableMap.<String, Object>builder()
				.put("bootstrap.servers", "localhost:29092")
				.put("key.deserializer", IntegerDeserializer.class)
				.put("value.deserializer", JsonDeserializer.class)
				
				.put("group.id", "spring-boot-test") // needed but dont know why...

			.build();
    }
}
