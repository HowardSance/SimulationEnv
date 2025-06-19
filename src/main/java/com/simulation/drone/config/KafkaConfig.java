package com.simulation.drone.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic detectionEventsTopic() {
        return new NewTopic("detection-events", 3, (short) 1);
    }

    @Bean
    public NewTopic deviceCommandsTopic() {
        return new NewTopic("device-commands", 3, (short) 1);
    }

    @Bean
    public NewTopic configUpdatesTopic() {
        return new NewTopic("config-updates", 3, (short) 1);
    }

    @Bean
    public NewTopic simulationMetricsTopic() {
        return new NewTopic("simulation-metrics", 3, (short) 1);
    }
} 