package com.simulation.drone.dto.device;

import lombok.Data;
import java.util.Map;

/**
 * 设备状态数据传输对象
 */
@Data
public class DeviceStateDTO {
    /**
     * 设备ID
     */
    private String deviceId;
    
    /**
     * 设备类型
     * 可选值：RADAR, OPTICAL, RADIO
     */
    private String type;
    
    /**
     * 设备名称
     */
    private String name;
    
    /**
     * 设备状态
     * 可选值：ACTIVE, INACTIVE, CALIBRATING, ERROR
     */
    private String status;
    
    /**
     * 设备位置 (x, y, z)
     */
    private Map<String, Double> position;
    
    /**
     * 设备姿态 (pitch, roll, yaw)
     */
    private Map<String, Double> orientation;
    
    /**
     * 设备参数
     */
    private Map<String, Object> parameters;
    
    /**
     * 探测范围
     */
    private Map<String, Double> detectionRange;
    
    /**
     * 设备指标
     */
    private Map<String, Object> metrics;
    
    /**
     * 错误信息（如果有）
     */
    private String errorMessage;
    
    /**
     * 最后更新时间
     */
    private long updatedAt;
} 