package com.JP.dronesim.domain.airspace.model;

/**
 * 空域事件监听器接口
 * 用于监听和处理空域中发生的各种事件
 * 
 * @author JP Team
 * @version 1.0
 */
public interface AirspaceEventListener {
    
    /**
     * 处理空域事件
     * 
     * @param event 空域事件
     */
    void onAirspaceEvent(AirspaceEvent event);
    
    /**
     * 获取监听器名称
     * 
     * @return 监听器名称
     */
    default String getListenerName() {
        return this.getClass().getSimpleName();
    }
    
    /**
     * 检查是否支持处理指定类型的事件
     * 
     * @param eventType 事件类型
     * @return 是否支持
     */
    default boolean supportsEventType(AirspaceEventType eventType) {
        return true; // 默认支持所有事件类型
    }
} 