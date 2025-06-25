package com.simulation.drone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 无人机仿真系统主应用类
 * 系统启动入口
 */
@SpringBootApplication
@EnableConfigurationProperties
public class DroneSimulationApplication {

    /**
     * 日志记录器
     */
    private static final Logger log = LoggerFactory.getLogger(DroneSimulationApplication.class);

    /**
     * 应用程序主入口
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        try {
            log.info("Starting Drone Simulation System...");
            SpringApplication.run(DroneSimulationApplication.class, args);
            log.info("Drone Simulation System started successfully!");
        } catch (Exception e) {
            log.error("Failed to start Drone Simulation System", e);
            System.exit(1);
        }
    }
} 