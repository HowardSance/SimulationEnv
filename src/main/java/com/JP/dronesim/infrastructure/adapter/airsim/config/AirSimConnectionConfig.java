package com.simulation.drone.infrastructure.adapter.airsim.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * AirSim连接配置类
 * 用于配置与AirSim仿真引擎的连接参数
 */
@Configuration
@ConfigurationProperties(prefix = "airsim")
public class AirSimConnectionConfig {
    
    /**
     * AirSim服务器主机地址
     */
    private String host = "localhost";
    
    /**
     * AirSim服务器端口
     */
    private int port = 41451;
    
    /**
     * 连接配置
     */
    private Connection connection = new Connection();
    
    /**
     * 获取主机地址
     * @return 主机地址
     */
    public String getHost() {
        return host;
    }
    
    /**
     * 设置主机地址
     * @param host 主机地址
     */
    public void setHost(String host) {
        this.host = host;
    }
    
    /**
     * 获取端口
     * @return 端口
     */
    public int getPort() {
        return port;
    }
    
    /**
     * 设置端口
     * @param port 端口
     */
    public void setPort(int port) {
        this.port = port;
    }
    
    /**
     * 获取连接配置
     * @return 连接配置
     */
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * 设置连接配置
     * @param connection 连接配置
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * 连接配置内部类
     */
    public static class Connection {
        
        /**
         * 连接超时时间（毫秒）
         */
        private int timeout = 5000;
        
        /**
         * 重试配置
         */
        private Retry retry = new Retry();
        
        /**
         * 获取超时时间
         * @return 超时时间
         */
        public int getTimeout() {
            return timeout;
        }
        
        /**
         * 设置超时时间
         * @param timeout 超时时间
         */
        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }
        
        /**
         * 获取重试配置
         * @return 重试配置
         */
        public Retry getRetry() {
            return retry;
        }
        
        /**
         * 设置重试配置
         * @param retry 重试配置
         */
        public void setRetry(Retry retry) {
            this.retry = retry;
        }
        
        /**
         * 重试配置内部类
         */
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
             * 重试间隔倍数
             */
            private double multiplier = 2.0;
            
            /**
             * 最大重试间隔（毫秒）
             */
            private long maxInterval = 10000;
            
            /**
             * 获取最大重试次数
             * @return 最大重试次数
             */
            public int getMaxAttempts() {
                return maxAttempts;
            }
            
            /**
             * 设置最大重试次数
             * @param maxAttempts 最大重试次数
             */
            public void setMaxAttempts(int maxAttempts) {
                this.maxAttempts = maxAttempts;
            }
            
            /**
             * 获取初始重试间隔
             * @return 初始重试间隔
             */
            public long getInitialInterval() {
                return initialInterval;
            }
            
            /**
             * 设置初始重试间隔
             * @param initialInterval 初始重试间隔
             */
            public void setInitialInterval(long initialInterval) {
                this.initialInterval = initialInterval;
            }
            
            /**
             * 获取重试间隔倍数
             * @return 重试间隔倍数
             */
            public double getMultiplier() {
                return multiplier;
            }
            
            /**
             * 设置重试间隔倍数
             * @param multiplier 重试间隔倍数
             */
            public void setMultiplier(double multiplier) {
                this.multiplier = multiplier;
            }
            
            /**
             * 获取最大重试间隔
             * @return 最大重试间隔
             */
            public long getMaxInterval() {
                return maxInterval;
            }
            
            /**
             * 设置最大重试间隔
             * @param maxInterval 最大重试间隔
             */
            public void setMaxInterval(long maxInterval) {
                this.maxInterval = maxInterval;
            }
        }
    }
} 