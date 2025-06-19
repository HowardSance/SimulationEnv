package com.simulation.drone.config;

import com.simulation.drone.engine.ClockEngine;
import com.simulation.drone.engine.DetectionOrchestrator;
import com.simulation.drone.engine.SimulationEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

/**
 * 仿真引擎配置类
 * 负责系统启动时的初始化和组件协调
 */
@Slf4j
@Configuration
public class SimulationEngineConfig implements ApplicationRunner {
    
    @Autowired
    private ClockEngine clockEngine;
    
    @Autowired
    private SimulationEngine simulationEngine;
    
    @Autowired
    private DetectionOrchestrator detectionOrchestrator;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("开始初始化仿真引擎系统...");
        
        try {
            // 步骤1: 初始化探测业务编排器
            detectionOrchestrator.initialize();
            
            // 步骤2: 初始化仿真引擎
            // 创建默认环境配置
            // EnvironmentConfig defaultConfig = createDefaultEnvironmentConfig();
            // simulationEngine.initialize(defaultConfig);
            
            log.info("仿真引擎系统初始化完成");
            log.info("系统状态:");
            log.info("- 时钟引擎: {}", clockEngine.isRunning() ? "就绪" : "待启动");
            log.info("- 探测编排器状态: {}", detectionOrchestrator.getStatus());
            
        } catch (Exception e) {
            log.error("仿真引擎系统初始化失败", e);
            throw e;
        }
    }
    
    /**
     * 创建默认环境配置
     */
    // private EnvironmentConfig createDefaultEnvironmentConfig() {
    //     EnvironmentConfig config = new EnvironmentConfig();
    //     config.setName("Default Environment");
    //     config.setDescription("默认仿真环境配置");
    //     return config;
    // }
} 