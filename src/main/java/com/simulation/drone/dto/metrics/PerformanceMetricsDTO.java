package com.simulation.drone.dto.metrics;

import lombok.Data;
import java.util.Map;

/**
 * 性能指标数据传输对象
 */
@Data
public class PerformanceMetricsDTO {
    /**
     * 帧率 (FPS)
     */
    private double fps;
    
    /**
     * 内存使用率 (0-1)
     */
    private double memoryUsage;
    
    /**
     * CPU使用率 (0-1)
     */
    private double cpuUsage;
    
    /**
     * 实体数量
     */
    private int entityCount;
    
    /**
     * 设备指标
     */
    private Map<String, Object> deviceMetrics;
    
    /**
     * 仿真指标
     */
    private Map<String, Object> simulationMetrics;
    
    /**
     * 性能瓶颈
     */
    private Map<String, Object> bottlenecks;
    
    /**
     * 时间戳
     */
    private long timestamp;
    
    /**
     * 系统状态
     * 可选值：NORMAL, WARNING, CRITICAL
     */
    private String systemStatus;
    
    /**
     * 警告信息
     */
    private String warningMessage;
    
    /**
     * 错误信息（如果有）
     */
    private String errorMessage;
} 