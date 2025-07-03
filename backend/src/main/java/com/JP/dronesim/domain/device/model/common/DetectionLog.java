package com.JP.dronesim.domain.device.model.common;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.JP.dronesim.domain.device.model.events.DetectionEvent;

/**
 * 探测日志实体
 * 负责管理探测设备的探测事件历史记录
 * 提供探测事件的添加、查询、过滤等功能
 * 
 * @author JP
 * @version 1.0
 */
public class DetectionLog {
    
    /**
     * 探测事件列表
     * 按时间顺序存储探测事件
     */
    private final List<DetectionEvent> events;
    
    /**
     * 最大日志容量
     * 防止内存溢出，超出容量时会删除最旧的记录
     */
    private final int maxCapacity;
    
    /**
     * 默认构造函数
     * 使用默认的最大容量（1000条记录）
     */
    public DetectionLog() {
        this(1000);
    }
    
    /**
     * 构造函数
     * 
     * @param maxCapacity 最大日志容量
     * @throws IllegalArgumentException 如果最大容量小于等于0
     */
    public DetectionLog(int maxCapacity) {
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("最大容量必须大于0");
        }
        this.maxCapacity = maxCapacity;
        this.events = new ArrayList<>();
    }
    
    /**
     * 添加探测事件
     * 如果超出最大容量，会删除最旧的事件记录
     * 
     * @param event 要添加的探测事件
     * @throws IllegalArgumentException 如果事件为null
     */
    public void addEvent(DetectionEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("探测事件不能为null");
        }
        
        synchronized (events) {
            // 如果超出容量，删除最旧的记录
            if (events.size() >= maxCapacity) {
                events.remove(0);
            }
            
            events.add(event);
        }
    }
    
    /**
     * 获取所有探测事件
     * 返回的是不可修改的列表副本
     * 
     * @return 所有探测事件的列表
     */
    public List<DetectionEvent> getAllEvents() {
        synchronized (events) {
            return Collections.unmodifiableList(new ArrayList<>(events));
        }
    }
    
    /**
     * 获取指定时间范围内的探测事件
     * 
     * @param startTime 开始时间（包含）
     * @param endTime 结束时间（包含）
     * @return 指定时间范围内的探测事件列表
     */
    public List<DetectionEvent> getEventsBetween(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("开始时间和结束时间不能为null");
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("开始时间不能晚于结束时间");
        }
        
        synchronized (events) {
            return events.stream()
                    .filter(event -> !event.getTimestamp().isBefore(startTime) 
                                  && !event.getTimestamp().isAfter(endTime))
                    .collect(Collectors.toList());
        }
    }
    
    /**
     * 根据无人机ID获取相关的探测事件
     * 
     * @param uavId 无人机ID
     * @return 与指定无人机相关的探测事件列表
     */
    public List<DetectionEvent> getEventsByUavId(String uavId) {
        if (uavId == null || uavId.trim().isEmpty()) {
            throw new IllegalArgumentException("无人机ID不能为空");
        }
        
        synchronized (events) {
            return events.stream()
                    .filter(event -> uavId.equals(event.getDetectedUavId()))
                    .collect(Collectors.toList());
        }
    }
    
    /**
     * 获取高置信度的探测事件
     * 置信度阈值为0.8
     * 
     * @return 高置信度的探测事件列表
     */
    public List<DetectionEvent> getHighConfidenceEvents() {
        synchronized (events) {
            return events.stream()
                    .filter(DetectionEvent::isHighConfidence)
                    .collect(Collectors.toList());
        }
    }
    
    /**
     * 获取最新的N个探测事件
     * 
     * @param count 要获取的事件数量
     * @return 最新的探测事件列表
     */
    public List<DetectionEvent> getLatestEvents(int count) {
        if (count <= 0) {
            return Collections.emptyList();
        }
        
        synchronized (events) {
            int size = events.size();
            int startIndex = Math.max(0, size - count);
            return new ArrayList<>(events.subList(startIndex, size));
        }
    }
    
    /**
     * 获取探测事件总数
     * 
     * @return 当前存储的探测事件数量
     */
    public int getEventCount() {
        synchronized (events) {
            return events.size();
        }
    }
    
    /**
     * 检查是否有探测事件
     * 
     * @return true表示有探测事件，false表示没有
     */
    public boolean hasEvents() {
        synchronized (events) {
            return !events.isEmpty();
        }
    }
    
    /**
     * 清空所有探测事件
     */
    public void clearEvents() {
        synchronized (events) {
            events.clear();
        }
    }
    
    /**
     * 删除指定时间之前的探测事件
     * 
     * @param cutoffTime 截止时间
     * @return 删除的事件数量
     */
    public int removeEventsBefore(LocalDateTime cutoffTime) {
        if (cutoffTime == null) {
            throw new IllegalArgumentException("截止时间不能为null");
        }
        
        synchronized (events) {
            int originalSize = events.size();
            events.removeIf(event -> event.getTimestamp().isBefore(cutoffTime));
            return originalSize - events.size();
        }
    }
    
    /**
     * 获取最大容量
     * 
     * @return 日志的最大容量
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }
    
    /**
     * 获取最新的探测事件
     * 
     * @return 最新的探测事件，如果没有事件则返回null
     */
    public DetectionEvent getLatestEvent() {
        synchronized (events) {
            return events.isEmpty() ? null : events.get(events.size() - 1);
        }
    }
    
    @Override
    public String toString() {
        synchronized (events) {
            return "DetectionLog{" +
                    "eventCount=" + events.size() +
                    ", maxCapacity=" + maxCapacity +
                    ", latestEvent=" + (events.isEmpty() ? "null" : events.get(events.size() - 1).getEventId()) +
                    '}';
        }
    }
} 