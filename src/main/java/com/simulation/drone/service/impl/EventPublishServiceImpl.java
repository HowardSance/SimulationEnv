package com.simulation.drone.service.impl;

import com.simulation.drone.dto.core.SimulationCoreDTO;
import com.simulation.drone.dto.device.DeviceStateDTO;
import com.simulation.drone.dto.detection.DetectionResultDTO;
import com.simulation.drone.dto.event.DetectionEventDTO;
import com.simulation.drone.dto.event.SimulationEventDTO;
import com.simulation.drone.dto.metrics.PerformanceMetricsDTO;
import com.simulation.drone.service.EventPublishService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件发布服务实现类
 */
@Service
public class EventPublishServiceImpl implements EventPublishService {
    
    private static final Logger logger = LoggerFactory.getLogger(EventPublishServiceImpl.class);
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    // 缓存最新的状态信息
    private final Map<String, Object> statusCache = new ConcurrentHashMap<>();
    
    @Override
    public void publishSimulationStatus(SimulationCoreDTO status) {
        try {
            messagingTemplate.convertAndSend("/topic/simulation/status", status);
            statusCache.put("simulation", status);
            logger.debug("已发布仿真状态更新: {}", status);
        } catch (Exception e) {
            logger.error("发布仿真状态更新失败", e);
        }
    }
    
    @Override
    public void publishDeviceStatus(String deviceId, DeviceStateDTO status) {
        try {
            String destination = "/topic/device/" + deviceId + "/status";
            messagingTemplate.convertAndSend(destination, status);
            statusCache.put("device:" + deviceId, status);
            logger.debug("已发布设备状态更新: deviceId={}, status={}", deviceId, status);
        } catch (Exception e) {
            logger.error("发布设备状态更新失败: deviceId={}", deviceId, e);
        }
    }
    
    @Override
    public void publishDetectionResult(DetectionResultDTO result) {
        try {
            messagingTemplate.convertAndSend("/topic/detection/results", result);
            logger.debug("已发布探测结果: {}", result);
        } catch (Exception e) {
            logger.error("发布探测结果失败", e);
        }
    }
    
    @Override
    public void publishDetectionEvent(DetectionEventDTO event) {
        try {
            messagingTemplate.convertAndSend("/topic/detection/events", event);
            statusCache.put("latestDetection", event);
            logger.debug("已发布探测事件: {}", event);
        } catch (Exception e) {
            logger.error("发布探测事件失败", e);
        }
    }
    
    @Override
    public void publishPerformanceMetrics(PerformanceMetricsDTO metrics) {
        try {
            messagingTemplate.convertAndSend("/topic/simulation/metrics", metrics);
            statusCache.put("metrics", metrics);
            logger.debug("已发布性能指标: {}", metrics);
        } catch (Exception e) {
            logger.error("发布性能指标失败", e);
        }
    }
    
    @Override
    public void publishEnvironmentStatus(Map<String, Object> status) {
        try {
            messagingTemplate.convertAndSend("/topic/simulation/environment", status);
            statusCache.put("environment", status);
            logger.debug("已发布环境状态更新: {}", status);
        } catch (Exception e) {
            logger.error("发布环境状态更新失败", e);
        }
    }
    
    @Override
    public SimulationEventDTO getSimulationStatus() {
        return (SimulationEventDTO) statusCache.get("simulation");
    }
    
    @Override
    public DeviceStateDTO getDeviceStatus(String deviceId) {
        return (DeviceStateDTO) statusCache.get("device:" + deviceId);
    }
    
    @Override
    public DetectionEventDTO getLatestDetectionEvent() {
        return (DetectionEventDTO) statusCache.get("latestDetection");
    }
    
    @Override
    public PerformanceMetricsDTO getPerformanceMetrics() {
        return (PerformanceMetricsDTO) statusCache.get("metrics");
    }
    
    @Override
    public Map<String, Object> getEnvironmentStatus() {
        return (Map<String, Object>) statusCache.get("environment");
    }
} 