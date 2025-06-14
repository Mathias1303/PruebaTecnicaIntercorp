package com.interview.intercorp.ms02.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${app.kafka.input-topic}")
    private String inputTopic;

    @Value("${app.kafka.output-topic}")
    private String outputTopic;

    @Bean
    public NewTopic xmlToJsonTopic() {
        return TopicBuilder.name(inputTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic jsonProcessedTopic() {
        return TopicBuilder.name(outputTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
