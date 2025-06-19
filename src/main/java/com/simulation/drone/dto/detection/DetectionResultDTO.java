package com.simulation.drone.dto.detection;

import lombok.Data;
import java.util.Map;

/**
 * 探测结果数据传输对象
 */
@Data
public class DetectionResultDTO {
    /**
     * 结果ID
     */
    private String resultId;
    
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
     * 目标速度 (vx, vy, vz)
     */
    private Map<String, Double> velocity;
    
    /**
     * 目标特征
     */
    private Map<String, Object> characteristics;
    
    /**
     * 探测时间戳
     */
    private long timestamp;
    
    /**
     * 探测类型
     * 可选值：RADAR, OPTICAL, RADIO
     */
    private String detectionType;
    
    /**
     * 探测质量 (0-1)
     */
    private double quality;
    
    /**
     * 环境因素影响
     */
    private Map<String, Double> environmentFactors;
    
    /**
     * 设备状态
     */
    private Map<String, Object> deviceStatus;
    
    /**
     * 错误信息（如果有）
     */
    private String errorMessage;
} 