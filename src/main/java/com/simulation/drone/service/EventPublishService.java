package com.simulation.drone.service;

import com.simulation.drone.dto.core.SimulationCoreDTO;
import com.simulation.drone.dto.device.DeviceStateDTO;
import com.simulation.drone.dto.detection.DetectionResultDTO;
import com.simulation.drone.dto.event.DetectionEventDTO;
import com.simulation.drone.dto.event.SimulationEventDTO;
import com.simulation.drone.dto.metrics.PerformanceMetricsDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;

/**
 * 事件发布服务接口
 * 负责将仿真过程中的各种事件和状态更新推送给订阅者
 */
public interface EventPublishService {
    
    /**
     * 发布仿真状态更新
     * @param status 仿真状态
     */
    void publishSimulationStatus(SimulationCoreDTO status);
    
    /**
     * 发布设备状态更新
     * @param deviceId 设备ID
     * @param status 设备状态
     */
    void publishDeviceStatus(String deviceId, DeviceStateDTO status);
    
    /**
     * 发布探测结果
     * @param result 探测结果
     */
    void publishDetectionResult(DetectionResultDTO result);
    
    /**
     * 发布探测事件
     * @param event 探测事件
     */
    void publishDetectionEvent(DetectionEventDTO event);
    
    /**
     * 发布性能指标
     * @param metrics 性能指标
     */
    void publishPerformanceMetrics(PerformanceMetricsDTO metrics);
    
    /**
     * 发布环境状态更新
     * @param status 环境状态
     */
    void publishEnvironmentStatus(Map<String, Object> status);
    
    /**
     * 获取当前仿真状态
     * @return 仿真状态
     */
    SimulationEventDTO getSimulationStatus();
    
    /**
     * 获取设备状态
     * @param deviceId 设备ID
     * @return 设备状态
     */
    DeviceStateDTO getDeviceStatus(String deviceId);
    
    /**
     * 获取最新探测事件
     * @return 探测事件
     */
    DetectionEventDTO getLatestDetectionEvent();
    
    /**
     * 获取性能指标
     * @return 性能指标
     */
    PerformanceMetricsDTO getPerformanceMetrics();
    
    /**
     * 获取环境状态
     * @return 环境状态
     */
    Map<String, Object> getEnvironmentStatus();
} 