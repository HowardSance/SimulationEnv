package com.simulation.drone.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.simulation.drone.application.facade.AirSimFacade;
import com.simulation.drone.domain.device.model.DroneControls;
import com.simulation.drone.domain.device.model.DroneState;
import com.simulation.drone.domain.detection.model.LidarData;
import com.simulation.drone.domain.detection.model.ImuData;
import com.simulation.drone.domain.detection.model.DistanceSensorData;

/**
 * AirSim集成测试类
 * 测试与AirSim仿真引擎的集成功能
 */
@SpringBootTest
@ActiveProfiles("test")
public class AirSimIntegrationTest {
    
    /**
     * 日志记录器
     */
    private static final Logger log = LoggerFactory.getLogger(AirSimIntegrationTest.class);
    
    /**
     * AirSim门面服务
     */
    private AirSimFacade airSimFacade;
    
    /**
     * 测试前准备
     */
    @BeforeEach
    public void setUp() {
        log.info("Setting up AirSim integration test");
        airSimFacade = new AirSimFacade();
    }
    
    /**
     * 测试后清理
     */
    @AfterEach
    public void tearDown() {
        log.info("Tearing down AirSim integration test");
        if (airSimFacade != null) {
            airSimFacade.shutdown();
        }
    }
    
    /**
     * 测试连接初始化
     */
    @Test
    public void testConnectionInitialization() {
        log.info("Testing connection initialization");
        
        try {
            airSimFacade.initializeConnection();
            log.info("Connection initialization test passed");
        } catch (Exception e) {
            log.error("Connection initialization test failed", e);
            throw e;
        }
    }
    
    /**
     * 测试无人机控制
     */
    @Test
    public void testDroneControl() {
        log.info("Testing drone control");
        
        try {
            airSimFacade.initializeConnection();
            
            // 测试获取无人机状态
            DroneState state = airSimFacade.getDroneState();
            log.info("Drone state: {}", state);
            
            // 测试发送控制指令
            DroneControls controls = new DroneControls();
            airSimFacade.sendDroneControls(controls);
            log.info("Drone controls sent successfully");
            
            // 测试起飞
            airSimFacade.takeoff();
            log.info("Takeoff command sent successfully");
            
            // 测试悬停
            airSimFacade.hover();
            log.info("Hover command sent successfully");
            
            // 测试降落
            airSimFacade.land();
            log.info("Land command sent successfully");
            
            log.info("Drone control test passed");
            
        } catch (Exception e) {
            log.error("Drone control test failed", e);
            throw e;
        }
    }
    
    /**
     * 测试传感器数据获取
     */
    @Test
    public void testSensorDataRetrieval() {
        log.info("Testing sensor data retrieval");
        
        try {
            airSimFacade.initializeConnection();
            
            // 测试获取激光雷达数据
            LidarData lidarData = airSimFacade.getLidarData();
            log.info("LiDAR data: {}", lidarData);
            
            // 测试获取IMU数据
            ImuData imuData = airSimFacade.getImuData();
            log.info("IMU data: {}", imuData);
            
            // 测试获取距离传感器数据
            DistanceSensorData distanceData = airSimFacade.getDistanceSensorData();
            log.info("Distance sensor data: {}", distanceData);
            
            log.info("Sensor data retrieval test passed");
            
        } catch (Exception e) {
            log.error("Sensor data retrieval test failed", e);
            throw e;
        }
    }
    
    /**
     * 测试完整工作流程
     */
    @Test
    public void testCompleteWorkflow() {
        log.info("Testing complete workflow");
        
        try {
            // 1. 初始化连接
            airSimFacade.initializeConnection();
            log.info("Step 1: Connection initialized");
            
            // 2. 起飞
            airSimFacade.takeoff();
            log.info("Step 2: Takeoff completed");
            
            // 3. 获取传感器数据
            LidarData lidarData = airSimFacade.getLidarData();
            ImuData imuData = airSimFacade.getImuData();
            DistanceSensorData distanceData = airSimFacade.getDistanceSensorData();
            log.info("Step 3: Sensor data retrieved");
            
            // 4. 悬停
            airSimFacade.hover();
            log.info("Step 4: Hover mode activated");
            
            // 5. 降落
            airSimFacade.land();
            log.info("Step 5: Landing completed");
            
            // 6. 关闭连接
            airSimFacade.shutdown();
            log.info("Step 6: Connection closed");
            
            log.info("Complete workflow test passed");
            
        } catch (Exception e) {
            log.error("Complete workflow test failed", e);
            throw e;
        }
    }
} 