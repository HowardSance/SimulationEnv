package com.simulation.drone.dto.core;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * 仿真核心数据传输对象
 */
@Data
public class SimulationCoreDTO {
    /**
     * 仿真ID
     */
    private String simulationId;
    
    /**
     * 仿真状态
     * 可选值：RUNNING, PAUSED, STOPPED, ERROR
     */
    private String status;
    
    /**
     * 当前时间步长
     */
    private long timeStep;
    
    /**
     * 当前仿真时间（毫秒）
     */
    private long currentTime;
    
    /**
     * 仿真指标
     */
    private Map<String, Object> metrics;
    
    /**
     * 活动设备ID列表
     */
    private List<String> activeDeviceIds;
    
    /**
     * 活动无人机ID列表
     */
    private List<String> activeDroneIds;
    
    /**
     * 环境参数
     */
    private Map<String, Object> environmentParams;
    
    /**
     * 错误信息（如果有）
     */
    private String errorMessage;
    
    /**
     * 创建时间
     */
    private long createdAt;
    
    /**
     * 最后更新时间
     */
    private long updatedAt;
} 