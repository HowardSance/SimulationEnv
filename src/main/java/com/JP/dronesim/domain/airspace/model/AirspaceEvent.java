package com.JP.dronesim.domain.airspace.model;

import java.time.LocalDateTime;

/**
 * 空域事件
 * 表示空域中发生的各种事件
 * 
 * @author JP Team
 * @version 1.0
 */
public class AirspaceEvent {
    
    /**
     * 事件唯一标识
     */
    private final String eventId;
    
    /**
     * 事件类型
     */
    private final AirspaceEventType eventType;
    
    /**
     * 事件发生时间
     */
    private final LocalDateTime timestamp;
    
    /**
     * 触发事件的空域
     */
    private final Airspace airspace;
    
    /**
     * 事件相关的数据对象
     */
    private final Object[] eventData;
    
    /**
     * 构造函数
     * 
     * @param eventType 事件类型
     * @param airspace 空域
     * @param eventData 事件数据
     */
    public AirspaceEvent(AirspaceEventType eventType, Airspace airspace, Object... eventData) {
        if (eventType == null) {
            throw new IllegalArgumentException("事件类型不能为空");
        }
        if (airspace == null) {
            throw new IllegalArgumentException("空域不能为空");
        }
        
        this.eventId = java.util.UUID.randomUUID().toString();
        this.eventType = eventType;
        this.timestamp = LocalDateTime.now();
        this.airspace = airspace;
        this.eventData = eventData != null ? eventData : new Object[0];
    }
    
    /**
     * 获取第一个事件数据对象
     * 
     * @param <T> 数据类型
     * @param clazz 数据类型Class
     * @return 数据对象，如果不存在或类型不匹配则返回null
     */
    @SuppressWarnings("unchecked")
    public <T> T getFirstEventData(Class<T> clazz) {
        if (eventData.length > 0 && clazz.isInstance(eventData[0])) {
            return (T) eventData[0];
        }
        return null;
    }
    
    /**
     * 获取指定索引的事件数据对象
     * 
     * @param <T> 数据类型
     * @param index 索引
     * @param clazz 数据类型Class
     * @return 数据对象，如果不存在或类型不匹配则返回null
     */
    @SuppressWarnings("unchecked")
    public <T> T getEventData(int index, Class<T> clazz) {
        if (index >= 0 && index < eventData.length && clazz.isInstance(eventData[index])) {
            return (T) eventData[index];
        }
        return null;
    }
    
    /**
     * 检查是否包含指定类型的事件数据
     * 
     * @param clazz 数据类型Class
     * @return 是否包含
     */
    public boolean hasEventData(Class<?> clazz) {
        for (Object data : eventData) {
            if (clazz.isInstance(data)) {
                return true;
            }
        }
        return false;
    }
    
    // Getters
    public String getEventId() { return eventId; }
    public AirspaceEventType getEventType() { return eventType; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Airspace getAirspace() { return airspace; }
    public Object[] getEventData() { return eventData.clone(); }
    public int getEventDataCount() { return eventData.length; }
    
    @Override
    public String toString() {
        return String.format("AirspaceEvent{id='%s', type=%s, time=%s, airspace=%s, dataCount=%d}",
                           eventId, eventType, timestamp, airspace.getId(), eventData.length);
    }
} 