package com.JP.dronesim.domain.airspace.model;

/**
 * 空域事件类型枚举
 * 定义空域中可能发生的各种事件类型
 * 
 * @author JP Team
 * @version 1.0
 */
public enum AirspaceEventType {
    
    /**
     * 仿真启动事件
     */
    SIMULATION_STARTED("仿真启动"),
    
    /**
     * 仿真暂停事件
     */
    SIMULATION_PAUSED("仿真暂停"),
    
    /**
     * 仿真停止事件
     */
    SIMULATION_STOPPED("仿真停止"),
    
    /**
     * 时间步进事件
     */
    TIME_STEPPED("时间步进"),
    
    /**
     * UAV添加事件
     */
    UAV_ADDED("无人机添加"),
    
    /**
     * UAV移除事件
     */
    UAV_REMOVED("无人机移除"),
    
    /**
     * UAV状态变化事件
     */
    UAV_STATUS_CHANGED("无人机状态变化"),
    
    /**
     * UAV超出边界事件
     */
    UAV_OUT_OF_BOUNDS("无人机超出边界"),
    
    /**
     * 探测设备添加事件
     */
    PROBE_DEVICE_ADDED("探测设备添加"),
    
    /**
     * 探测设备移除事件
     */
    PROBE_DEVICE_REMOVED("探测设备移除"),
    
    /**
     * 探测设备错误事件
     */
    PROBE_DEVICE_ERROR("探测设备错误"),
    
    /**
     * 探测事件
     */
    DETECTION_EVENT("探测事件"),
    
    /**
     * 环境参数更新事件
     */
    ENVIRONMENT_UPDATED("环境参数更新"),
    
    /**
     * 空域配置变更事件
     */
    AIRSPACE_CONFIGURATION_CHANGED("空域配置变更");
    
    /**
     * 事件描述
     */
    private final String description;
    
    /**
     * 构造函数
     * 
     * @param description 事件描述
     */
    AirspaceEventType(String description) {
        this.description = description;
    }
    
    /**
     * 获取事件描述
     * 
     * @return 事件描述
     */
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return description;
    }
} 