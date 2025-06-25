package com.JP.dronesim.infrastructure.config;

import com.JP.dronesim.infrastructure.external.airsim.AirSimApiClient;
import com.JP.dronesim.infrastructure.adapter.airsim.config.AirSimConnectionConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AirSim配置类
 * 配置AirSim相关的Bean和连接参数
 */
@Configuration
@EnableConfigurationProperties(AirSimConnectionConfig.class)
public class AirSimConfig {
    
    /**
     * 创建AirSim API客户端Bean
     * @param connectionConfig 连接配置
     * @return AirSim API客户端
     */
    @Bean
    public AirSimApiClient airSimApiClient(AirSimConnectionConfig connectionConfig) {
        return new AirSimApiClient(connectionConfig);
    }
    
    /**
     * 配置AirSim连接参数
     * @return 连接配置
     */
    @Bean
    public AirSimConnectionConfig airSimConnectionConfig() {
        AirSimConnectionConfig config = new AirSimConnectionConfig();
        
        // 默认连接参数
        config.setHost("localhost");
        config.setPort(41451);
        
        // 连接超时和重试配置
        AirSimConnectionConfig.Connection connection = config.getConnection();
        connection.setTimeout(5000);
        
        AirSimConnectionConfig.Connection.Retry retry = connection.getRetry();
        retry.setMaxAttempts(3);
        retry.setInitialInterval(1000);
        retry.setMultiplier(2.0);
        retry.setMaxInterval(10000);
        
        return config;
    }
} 