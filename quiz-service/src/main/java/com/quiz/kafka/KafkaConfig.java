package com.quiz.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
	
	@Bean
	NewTopic topic() {
		
		return TopicBuilder.name(Topics.REPORT_STATUS_TOPIC)
				.build();
		
	}

}
