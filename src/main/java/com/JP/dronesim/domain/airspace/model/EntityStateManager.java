package com.JP.dronesim.domain.airspace.model;

import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.common.valueobjects.Velocity;
import com.JP.dronesim.domain.common.valueobjects.Orientation;
import com.JP.dronesim.domain.device.model.ProbeDevice;
import com.JP.dronesim.domain.device.model.common.events.DetectionEvent;
import com.JP.dronesim.domain.uav.model.UAV;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 实体状态管理器
 * 管理空域内所有实体（UAV、探测设备）的状态更新和事件发布
 * 提供实时状态跟踪、状态变化通知和历史记录功能
 *
 * @author JP Team
 * @version 1.0
 */
public class EntityStateManager {
    
    /**
     * UAV状态映射
     */
    private final Map<String, EntityState> uavStates = new ConcurrentHashMap<>();
    
    /**
     * 探测设备状态映射
     */
    private final Map<String, EntityState> deviceStates = new ConcurrentHashMap<>();
    
    /**
     * 状态变化监听器列表
     */
    private final List<StateChangeListener> stateListeners = new CopyOnWriteArrayList<>();
    
    /**
     * 探测事件列表（当前时间步）
     */
    private final List<DetectionEvent> currentStepEvents = new CopyOnWriteArrayList<>();
    
    /**
     * 历史探测事件列表
     */
    private final List<DetectionEvent> historicalEvents = new CopyOnWriteArrayList<>();
    
    /**
     * 最大历史事件数量
     */
    private static final int MAX_HISTORICAL_EVENTS = 10000;
    
    /**
     * 注册UAV实体
     * 
     * @param uav UAV实体
     */
    public void registerUAV(UAV uav) {
        if (uav == null) {
            throw new IllegalArgumentException("UAV不能为空");
        }
        
        EntityState state = new EntityState(
            uav.getId(),
            "UAV",
            uav.getName(),
            uav.getPosition(),
            uav.getVelocity(),
            uav.getOrientation(),
            uav.getStatus().toString(),
            LocalDateTime.now()
        );
        
        uavStates.put(uav.getId(), state);
        notifyStateChange(state, StateChangeType.ENTITY_ADDED);
    }
    
    /**
     * 注册探测设备实体
     *
     * @param device 探测设备
     */
    public void registerDevice(ProbeDevice device) {
        if (device == null) {
            throw new IllegalArgumentException("设备不能为空");
        }
        
        EntityState state = new EntityState(
            device.getId(),
            "DEVICE",
            device.getName(),
            device.getPosition(),
            Velocity.zero(), // 设备通常是静止的
            device.getAttitude(),
            device.getStatus().toString(),
            LocalDateTime.now()
        );
        
        deviceStates.put(device.getId(), state);
        notifyStateChange(state, StateChangeType.ENTITY_ADDED);
    }
    
    /**
     * 更新UAV状态
     *
     * @param uavId UAV ID
     * @param position 新位置
     * @param velocity 新速度
     * @param orientation 新姿态
     * @param status 新状态
     */
    public void updateUAVState(String uavId, Position position, Velocity velocity, Orientation orientation, String status) {
        EntityState currentState = uavStates.get(uavId);
        if (currentState == null) {
            return;
        }
        
        EntityState newState = currentState.updateState(position, velocity, orientation, status);
        uavStates.put(uavId, newState);
        
        notifyStateChange(newState, StateChangeType.STATE_UPDATED);
    }
    
    /**
     * 更新设备状态
     * 
     * @param deviceId 设备ID
     * @param position 新位置
     * @param orientation 新姿态
     * @param status 新状态
     */
    public void updateDeviceState(String deviceId, Position position, Orientation orientation, String status) {
        EntityState currentState = deviceStates.get(deviceId);
        if (currentState == null) {
            return;
        }
        
        EntityState newState = currentState.updateState(position, Velocity.zero(), orientation, status);
        deviceStates.put(deviceId, newState);
        
        notifyStateChange(newState, StateChangeType.STATE_UPDATED);
    }
    
    /**
     * 移除UAV实体
     * 
     * @param uavId UAV ID
     */
    public void unregisterUAV(String uavId) {
        EntityState state = uavStates.remove(uavId);
        if (state != null) {
            notifyStateChange(state, StateChangeType.ENTITY_REMOVED);
        }
    }
    
    /**
     * 移除设备实体
     * 
     * @param deviceId 设备ID
     */
    public void unregisterDevice(String deviceId) {
        EntityState state = deviceStates.remove(deviceId);
        if (state != null) {
            notifyStateChange(state, StateChangeType.ENTITY_REMOVED);
        }
    }
    
    /**
     * 获取UAV状态
     * 
     * @param uavId UAV ID
     * @return UAV状态，如果不存在返回null
     */
    public EntityState getUAVState(String uavId) {
        return uavStates.get(uavId);
    }
    
    /**
     * 获取设备状态
     * 
     * @param deviceId 设备ID
     * @return 设备状态，如果不存在返回null
     */
    public EntityState getDeviceState(String deviceId) {
        return deviceStates.get(deviceId);
    }
    
    /**
     * 获取所有UAV状态
     * 
     * @return UAV状态列表
     */
    public List<EntityState> getAllUAVStates() {
        return new ArrayList<>(uavStates.values());
    }
    
    /**
     * 获取所有设备状态
     * 
     * @return 设备状态列表
     */
    public List<EntityState> getAllDeviceStates() {
        return new ArrayList<>(deviceStates.values());
    }
    
    /**
     * 获取所有实体状态
     * 
     * @return 所有实体状态列表
     */
    public List<EntityState> getAllEntityStates() {
        List<EntityState> allStates = new ArrayList<>();
        allStates.addAll(uavStates.values());
        allStates.addAll(deviceStates.values());
        return allStates;
    }
    
    /**
     * 添加探测事件
     * 
     * @param event 探测事件
     */
    public void addDetectionEvent(DetectionEvent event) {
        if (event == null) {
            return;
        }
        
        currentStepEvents.add(event);
        historicalEvents.add(event);
        
        // 限制历史事件数量
        while (historicalEvents.size() > MAX_HISTORICAL_EVENTS) {
            historicalEvents.remove(0);
        }
        
        // 通知事件监听器
        notifyDetectionEvent(event);
    }
    
    /**
     * 获取当前时间步的探测事件
     * 
     * @return 当前时间步的探测事件列表
     */
    public List<DetectionEvent> getCurrentStepEvents() {
        return new ArrayList<>(currentStepEvents);
    }
    
    /**
     * 获取历史探测事件
     * 
     * @param limit 最大返回数量，0表示不限制
     * @return 历史探测事件列表
     */
    public List<DetectionEvent> getHistoricalEvents(int limit) {
        if (limit <= 0 || limit >= historicalEvents.size()) {
            return new ArrayList<>(historicalEvents);
        }
        
        int fromIndex = Math.max(0, historicalEvents.size() - limit);
        return new ArrayList<>(historicalEvents.subList(fromIndex, historicalEvents.size()));
    }
    
    /**
     * 清除当前时间步的探测事件
     */
    public void clearCurrentStepEvents() {
        currentStepEvents.clear();
    }
    
    /**
     * 在指定范围内查询实体
     * 
     * @param center 中心位置
     * @param radius 查询半径
     * @return 范围内的实体状态列表
     */
    public List<EntityState> queryEntitiesInRange(Position center, double radius) {
        List<EntityState> entitiesInRange = new ArrayList<>();
        
        for (EntityState state : getAllEntityStates()) {
            double distance = calculateDistance(center, state.getPosition());
            if (distance <= radius) {
                entitiesInRange.add(state);
            }
        }
        
        return entitiesInRange;
    }
    
    /**
     * 添加状态变化监听器
     * 
     * @param listener 监听器
     */
    public void addStateChangeListener(StateChangeListener listener) {
        if (listener != null) {
            stateListeners.add(listener);
        }
    }
    
    /**
     * 移除状态变化监听器
     * 
     * @param listener 监听器
     */
    public void removeStateChangeListener(StateChangeListener listener) {
        stateListeners.remove(listener);
    }
    
    /**
     * 获取实体总数
     * 
     * @return 实体总数
     */
    public int getTotalEntityCount() {
        return uavStates.size() + deviceStates.size();
    }
    
    /**
     * 获取UAV数量
     * 
     * @return UAV数量
     */
    public int getUAVCount() {
        return uavStates.size();
    }
    
    /**
     * 获取设备数量
     * 
     * @return 设备数量
     */
    public int getDeviceCount() {
        return deviceStates.size();
    }
    
    /**
     * 清除所有状态
     */
    public void clear() {
        uavStates.clear();
        deviceStates.clear();
        currentStepEvents.clear();
        historicalEvents.clear();
    }
    
    /**
     * 通知状态变化
     * 
     * @param state 实体状态
     * @param changeType 变化类型
     */
    private void notifyStateChange(EntityState state, StateChangeType changeType) {
        for (StateChangeListener listener : stateListeners) {
            try {
                listener.onStateChange(state, changeType);
            } catch (Exception e) {
                System.err.println("状态变化通知失败: " + e.getMessage());
            }
        }
    }
    
    /**
     * 通知探测事件
     * 
     * @param event 探测事件
     */
    private void notifyDetectionEvent(DetectionEvent event) {
        for (StateChangeListener listener : stateListeners) {
            try {
                listener.onDetectionEvent(event);
            } catch (Exception e) {
                System.err.println("探测事件通知失败: " + e.getMessage());
            }
        }
    }
    
    /**
     * 计算两点间距离
     * 
     * @param pos1 位置1
     * @param pos2 位置2
     * @return 距离
     */
    private double calculateDistance(Position pos1, Position pos2) {
        double dx = pos1.getX() - pos2.getX();
        double dy = pos1.getY() - pos2.getY();
        double dz = pos1.getZ() - pos2.getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    /**
     * 实体状态内部类
     */
    public static class EntityState {
        private final String entityId;
        private final String entityType;
        private final String entityName;
        private final Position position;
        private final Velocity velocity;
        private final Orientation orientation;
        private final String status;
        private final LocalDateTime lastUpdateTime;
        
        public EntityState(String entityId, String entityType, String entityName,
                          Position position, Velocity velocity, Orientation orientation,
                          String status, LocalDateTime lastUpdateTime) {
            this.entityId = entityId;
            this.entityType = entityType;
            this.entityName = entityName;
            this.position = position;
            this.velocity = velocity;
            this.orientation = orientation;
            this.status = status;
            this.lastUpdateTime = lastUpdateTime;
        }
        
        public EntityState updateState(Position newPosition, Velocity newVelocity,
                                     Orientation newOrientation, String newStatus) {
            return new EntityState(
                this.entityId,
                this.entityType,
                this.entityName,
                newPosition,
                newVelocity,
                newOrientation,
                newStatus,
                LocalDateTime.now()
            );
        }
        
        // Getters
        public String getEntityId() { return entityId; }
        public String getEntityType() { return entityType; }
        public String getEntityName() { return entityName; }
        public Position getPosition() { return position; }
        public Velocity getVelocity() { return velocity; }
        public Orientation getOrientation() { return orientation; }
        public String getStatus() { return status; }
        public LocalDateTime getLastUpdateTime() { return lastUpdateTime; }
    }
    
    /**
     * 状态变化类型枚举
     */
    public enum StateChangeType {
        ENTITY_ADDED,
        ENTITY_REMOVED,
        STATE_UPDATED
    }
    
    /**
     * 状态变化监听器接口
     */
    public interface StateChangeListener {
        /**
         * 状态变化回调
         * 
         * @param state 实体状态
         * @param changeType 变化类型
         */
        void onStateChange(EntityState state, StateChangeType changeType);
        
        /**
         * 探测事件回调
         * 
         * @param event 探测事件
         */
        void onDetectionEvent(DetectionEvent event);
    }
} 
