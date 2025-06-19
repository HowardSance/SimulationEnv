package com.simulation.drone.dto.event;

import lombok.Data;
import java.util.Map;

/**
 * 探测事件数据传输对象
 */
@Data
public class DetectionEventDTO {
    /**
     * 事件ID
     */
    private String eventId;
    
    /**
     * 事件类型
     * 可选值：DETECTION_START, DETECTION_END, TARGET_DETECTED, TARGET_LOST
     */
    private String eventType;
    
    /**
     * 设备ID
     */
    private String deviceId;
    
    /**
     * 目标ID
     */
    private String targetId;
    
    /**
     * 探测概率 (0-1)
     */
    private double probability;
    
    /**
     * 目标位置 (x, y, z)
     */
    private Map<String, Double> position;
    
    /**
     * 目标特征
     */
    private Map<String, Object> characteristics;
    
    /**
     * 事件时间戳
     */
    private long timestamp;
    
    /**
     * 事件优先级
     * 可选值：LOW, MEDIUM, HIGH, CRITICAL
     */
    private String priority;
    
    /**
     * 事件描述
     */
    private String description;
    
    /**
     * 相关数据
     */
    private Map<String, Object> data;
    
    /**
     * 错误信息（如果有）
     */
    private String errorMessage;
} 