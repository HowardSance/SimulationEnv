package com.simulation.drone.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 环境配置类
 * 用于管理仿真环境的各种参数
 */
@Data
@Component
@ConfigurationProperties(prefix = "simulation.environment")
public class EnvironmentConfig {
    
    /**
     * 天气配置
     */
    private WeatherConfig weather = new WeatherConfig();
    
    /**
     * 地形配置
     */
    private TerrainConfig terrain = new TerrainConfig();
    
    /**
     * 物理配置
     */
    private PhysicsConfig physics = new PhysicsConfig();
    
    /**
     * 天气配置类
     */
    @Data
    public static class WeatherConfig {
        /**
         * 天气类型
         * 可选值：CLEAR（晴朗）, CLOUDY（多云）, RAINY（雨天）, FOGGY（雾天）
         */
        private String type = "CLEAR";
        
        /**
         * 温度（摄氏度）
         * 范围：-50.0 到 50.0
         */
        private double temperature = 25.0;
        
        /**
         * 湿度（百分比）
         * 范围：0.0 到 100.0
         */
        private double humidity = 60.0;
        
        /**
         * 风速（米/秒）
         * 范围：0.0 到 30.0
         */
        private double windSpeed = 5.0;
        
        /**
         * 风向（度）
         * 范围：0.0 到 360.0
         */
        private double windDirection = 0.0;
        
        /**
         * 能见度（米）
         * 范围：0.0 到 10000.0
         */
        private double visibility = 10000.0;
        
        /**
         * 降水强度（毫米/小时）
         * 范围：0.0 到 100.0
         */
        private double precipitation = 0.0;
    }
    
    /**
     * 地形配置类
     */
    @Data
    public static class TerrainConfig {
        /**
         * 地形类型
         * 可选值：FLAT（平坦）, HILLY（丘陵）, MOUNTAINOUS（山地）
         */
        private String type = "FLAT";
        
        /**
         * 地形高度范围（米）
         * 范围：0.0 到 5000.0
         */
        private double heightRange = 1000.0;
        
        /**
         * 地形粗糙度
         * 范围：0.0 到 1.0
         */
        private double roughness = 0.1;
        
        /**
         * 地形覆盖类型
         * 可选值：GRASS（草地）, FOREST（森林）, DESERT（沙漠）, URBAN（城市）
         */
        private String coverage = "GRASS";
        
        /**
         * 地形分辨率（米）
         * 范围：1.0 到 100.0
         */
        private double resolution = 10.0;
    }
    
    /**
     * 物理配置类
     */
    @Data
    public static class PhysicsConfig {
        /**
         * 重力加速度（米/秒²）
         * 范围：0.0 到 20.0
         */
        private double gravity = 9.81;
        
        /**
         * 空气密度（千克/立方米）
         * 范围：0.0 到 2.0
         */
        private double airDensity = 1.225;
        
        /**
         * 空气阻力系数
         * 范围：0.0 到 1.0
         */
        private double dragCoefficient = 0.47;
        
        /**
         * 地面摩擦系数
         * 范围：0.0 到 1.0
         */
        private double frictionCoefficient = 0.3;
        
        /**
         * 碰撞检测精度
         * 范围：0.0 到 1.0
         */
        private double collisionPrecision = 0.1;
    }
    
    /**
     * 获取天气因子
     * 用于计算探测概率
     * @return 天气因子（0.0 到 1.0）
     */
    public double getWeatherFactor() {
        switch (weather.getType()) {
            case "CLEAR":
                return 1.0;
            case "CLOUDY":
                return 0.8;
            case "RAINY":
                return 0.6;
            case "FOGGY":
                return 0.4;
            default:
                return 1.0;
        }
    }
    
    /**
     * 获取地形因子
     * 用于计算探测概率
     * @return 地形因子（0.0 到 1.0）
     */
    public double getTerrainFactor() {
        switch (terrain.getType()) {
            case "FLAT":
                return 1.0;
            case "HILLY":
                return 0.8;
            case "MOUNTAINOUS":
                return 0.6;
            default:
                return 1.0;
        }
    }
    
    /**
     * 获取环境状态
     * @return 环境状态Map
     */
    public java.util.Map<String, Object> getEnvironmentStatus() {
        java.util.Map<String, Object> status = new java.util.HashMap<>();
        status.put("weather", weather);
        status.put("terrain", terrain);
        status.put("physics", physics);
        status.put("weatherFactor", getWeatherFactor());
        status.put("terrainFactor", getTerrainFactor());
        return status;
    }
} 