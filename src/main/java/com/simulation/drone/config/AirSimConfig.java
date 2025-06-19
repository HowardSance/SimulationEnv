package com.simulation.drone.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * AirSim配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "airsim")
public class AirSimConfig {
    
    /**
     * AirSim服务器主机地址
     */
    private String host;
    
    /**
     * AirSim服务器端口
     */
    private int port;
    
    /**
     * 连接配置
     */
    private Connection connection = new Connection();
    
    @Data
    public static class Connection {
        /**
         * 连接超时时间（毫秒）
         */
        private int timeout = 5000;
        
        /**
         * 重试配置
         */
        private Retry retry = new Retry();
        
        @Data
        public static class Retry {
            /**
             * 最大重试次数
             */
            private int maxAttempts = 3;
            
            /**
             * 初始重试间隔（毫秒）
             */
            private long initialInterval = 1000;
            
            /**
             * 重试间隔乘数
             */
            private double multiplier = 2.0;
            
            /**
             * 最大重试间隔（毫秒）
             */
            private long maxInterval = 10000;
        }
    }
} 