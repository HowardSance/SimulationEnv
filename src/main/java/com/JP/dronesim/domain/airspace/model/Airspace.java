package com.JP.dronesim.domain.airspace.model;

import com.JP.dronesim.domain.common.enums.DeviceStatus;
import com.JP.dronesim.domain.common.enums.DeviceType;
import com.JP.dronesim.domain.common.enums.UAVStatus;
import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.common.valueobjects.Velocity;
import com.JP.dronesim.domain.device.model.ProbeDevice;
import com.JP.dronesim.domain.device.model.common.events.DetectionEvent;
import com.JP.dronesim.domain.uav.model.AirspaceEnvironment;
import com.JP.dronesim.domain.uav.model.UAV;
import com.JP.dronesim.domain.uav.model.UAVState;
import com.JP.dronesim.domain.uav.model.UAVMission;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 空域聚合根
 * 管理三维模拟空间、环境参数和所有实体（无人机、探测设备）
 * 提供空间索引、实体管理、探测计算和事件发布功能
 * 
 * @author JP Team
 * @version 1.0
 */
public class Airspace implements AirspaceEnvironment {
    
    /**
     * 空域唯一标识符
     */
    private final String id;
    
    /**
     * 空域名称
     */
    private String name;
    
    /**
     * 空域边界
     */
    private final AirspaceBoundary boundary;
    
    /**
     * 环境参数
     */
    private EnvironmentParameters environmentParameters;
    
    /**
     * 仿真时间步长（秒）
     */
    private double timeStep;
    
    /**
     * 当前仿真时间
     */
    private LocalDateTime simulationTime;
    
    /**
     * 仿真是否运行中
     */
    private boolean isRunning;
    
    /**
     * 空间索引系统 - 八叉树
     */
    private final Octree spatialIndex;
    
    /**
     * 无人机映射 - 快速ID查找
     */
    private final Map<String, UAV> uavs;
    
    /**
     * 探测设备映射 - 快速ID查找
     */
    private final Map<String, ProbeDevice> probeDevices;
    
    /**
     * 事件监听器列表
     */
    private final List<AirspaceEventListener> eventListeners;
    
    /**
     * 仿真统计信息
     */
    private final SimulationStatistics statistics;
    
    /**
     * 最后更新时间
     */
    private LocalDateTime lastUpdatedAt;
    
    /**
     * 创建时间
     */
    private final LocalDateTime createdAt;
    
    /**
     * 实体ID生成器
     */
    private final AtomicLong entityIdGenerator;
    
    /**
     * 构造函数
     * 
     * @param name 空域名称
     * @param boundary 空域边界
     * @param timeStep 仿真时间步长
     */
    public Airspace(String name, AirspaceBoundary boundary, double timeStep) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("空域名称不能为空");
        }
        if (boundary == null) {
            throw new IllegalArgumentException("空域边界不能为空");
        }
        if (timeStep <= 0) {
            throw new IllegalArgumentException("时间步长必须大于零");
        }
        
        this.id = UUID.randomUUID().toString();
        this.name = name.trim();
        this.boundary = boundary;
        this.timeStep = timeStep;
        this.simulationTime = LocalDateTime.now();
        this.isRunning = false;
        
        // 初始化空间索引
        this.spatialIndex = new Octree(boundary.getMinX(), boundary.getMinY(), boundary.getMinZ(),
                                      boundary.getMaxX(), boundary.getMaxY(), boundary.getMaxZ());
        
        // 初始化集合
        this.uavs = new ConcurrentHashMap<>();
        this.probeDevices = new ConcurrentHashMap<>();
        this.eventListeners = new CopyOnWriteArrayList<>();
        
        // 初始化统计信息
        this.statistics = new SimulationStatistics();
        
        // 设置默认环境参数
        this.environmentParameters = EnvironmentParameters.createDefault();
        
        this.createdAt = LocalDateTime.now();
        this.lastUpdatedAt = LocalDateTime.now();
        this.entityIdGenerator = new AtomicLong(1);
    }
    
    // ================ 空域管理方法 ================
    
    /**
     * 启动仿真
     */
    public void startSimulation() {
        if (isRunning) {
            throw new IllegalStateException("仿真已在运行中");
        }
        
        this.isRunning = true;
        this.simulationTime = LocalDateTime.now();
        this.lastUpdatedAt = LocalDateTime.now();
        
        // 发布仿真启动事件
        publishEvent(new AirspaceEvent(AirspaceEventType.SIMULATION_STARTED, this));
        
        // 启用所有活跃的探测设备
        enableActiveProbeDevices();
    }
    
    /**
     * 暂停仿真
     */
    public void pauseSimulation() {
        if (!isRunning) {
            throw new IllegalStateException("仿真未在运行");
        }
        
        this.isRunning = false;
        this.lastUpdatedAt = LocalDateTime.now();
        
        // 发布仿真暂停事件
        publishEvent(new AirspaceEvent(AirspaceEventType.SIMULATION_PAUSED, this));
    }
    
    /**
     * 停止仿真
     */
    public void stopSimulation() {
        this.isRunning = false;
        this.lastUpdatedAt = LocalDateTime.now();
        
        // 禁用所有探测设备
        disableAllProbeDevices();
        
        // 发布仿真停止事件
        publishEvent(new AirspaceEvent(AirspaceEventType.SIMULATION_STOPPED, this));
    }
    
    /**
     * 推进仿真时间
     * 更新所有实体的状态和位置
     */
    public void stepSimulation() {
        if (!isRunning) {
            return;
        }
        
        LocalDateTime previousTime = simulationTime;
        this.simulationTime = simulationTime.plusNanos((long) (timeStep * 1_000_000_000));
        this.lastUpdatedAt = LocalDateTime.now();
        
        // 更新所有无人机状态
        updateUAVs();
        
        // 更新空间索引
        updateSpatialIndex();
        
        // 执行探测计算
        performDetectionCalculations();
        
        // 更新统计信息
        updateStatistics();
        
        // 发布时间步进事件
        publishEvent(new AirspaceEvent(AirspaceEventType.TIME_STEPPED, this));
    }
    
    // ================ 实体管理方法 ================
    
    /**
     * 添加无人机到空域
     * 
     * @param uav 无人机
     */
    public void addUAV(UAV uav) {
        if (uav == null) {
            throw new IllegalArgumentException("无人机不能为空");
        }
        if (uavs.containsKey(uav.getId())) {
            throw new IllegalArgumentException("无人机已存在: " + uav.getId());
        }
        
        // 验证位置是否在空域边界内
        if (!boundary.contains(uav.getPosition())) {
            throw new IllegalArgumentException("无人机位置超出空域边界");
        }
        
        uavs.put(uav.getId(), uav);
        spatialIndex.insert(uav.getId(), uav.getPosition());
        
        // 发布无人机添加事件
        publishEvent(new AirspaceEvent(AirspaceEventType.UAV_ADDED, this, uav));
        
        statistics.incrementUAVCount();
    }
    
    /**
     * 移除无人机
     * 
     * @param uavId 无人机ID
     */
    public void removeUAV(String uavId) {
        if (uavId == null || uavId.trim().isEmpty()) {
            throw new IllegalArgumentException("无人机ID不能为空");
        }
        
        UAV uav = uavs.remove(uavId);
        if (uav != null) {
            spatialIndex.remove(uavId);
            
            // 发布无人机移除事件
            publishEvent(new AirspaceEvent(AirspaceEventType.UAV_REMOVED, this, uav));
            
            statistics.decrementUAVCount();
        }
    }
    
    /**
     * 添加探测设备到空域
     * 
     * @param device 探测设备
     */
    public void addProbeDevice(ProbeDevice device) {
        if (device == null) {
            throw new IllegalArgumentException("探测设备不能为空");
        }
        if (probeDevices.containsKey(device.getId())) {
            throw new IllegalArgumentException("探测设备已存在: " + device.getId());
        }
        
        // 验证位置是否在空域边界内
        if (!boundary.contains(device.getPosition())) {
            throw new IllegalArgumentException("探测设备位置超出空域边界");
        }
        
        probeDevices.put(device.getId(), device);
        spatialIndex.insert(device.getId(), device.getPosition());
        
        // 发布探测设备添加事件
        publishEvent(new AirspaceEvent(AirspaceEventType.PROBE_DEVICE_ADDED, this, device));
        
        statistics.incrementProbeDeviceCount();
    }
    
    /**
     * 移除探测设备
     * 
     * @param deviceId 设备ID
     */
    public void removeProbeDevice(String deviceId) {
        if (deviceId == null || deviceId.trim().isEmpty()) {
            throw new IllegalArgumentException("设备ID不能为空");
        }
        
        ProbeDevice device = probeDevices.remove(deviceId);
        if (device != null) {
            spatialIndex.remove(deviceId);
            
            // 发布探测设备移除事件
            publishEvent(new AirspaceEvent(AirspaceEventType.PROBE_DEVICE_REMOVED, this, device));
            
            statistics.decrementProbeDeviceCount();
        }
    }
    
    // ================ 空间查询方法 ================
    
    /**
     * 查询指定范围内的所有实体
     * 
     * @param center 查询中心点
     * @param radius 查询半径
     * @return 范围内的实体ID列表
     */
    public List<String> queryEntitiesInRange(Position center, double radius) {
        if (center == null) {
            throw new IllegalArgumentException("查询中心点不能为空");
        }
        if (radius <= 0) {
            throw new IllegalArgumentException("查询半径必须大于零");
        }
        
        return spatialIndex.queryRange(center, radius);
    }
    
    /**
     * 查询指定区域内的所有实体
     * 
     * @param minX 最小X坐标
     * @param minY 最小Y坐标
     * @param minZ 最小Z坐标
     * @param maxX 最大X坐标
     * @param maxY 最大Y坐标
     * @param maxZ 最大Z坐标
     * @return 区域内的实体ID列表
     */
    public List<String> queryEntitiesInBox(double minX, double minY, double minZ,
                                          double maxX, double maxY, double maxZ) {
        return spatialIndex.queryBox(minX, minY, minZ, maxX, maxY, maxZ);
    }
    
    /**
     * 查找最近的实体
     * 
     * @param position 参考位置
     * @param maxDistance 最大距离
     * @return 最近的实体ID，如果没有找到则返回null
     */
    public String findNearestEntity(Position position, double maxDistance) {
        if (position == null) {
            throw new IllegalArgumentException("参考位置不能为空");
        }
        if (maxDistance <= 0) {
            throw new IllegalArgumentException("最大距离必须大于零");
        }
        
        return spatialIndex.findNearest(position, maxDistance);
    }
    
    // ================ 探测计算方法 ================
    
    /**
     * 执行所有探测设备的探测计算
     */
    private void performDetectionCalculations() {
        List<UAV> activeUAVs = getActiveUAVs();
        
        for (ProbeDevice device : probeDevices.values()) {
            if (device.isActive()) {
                try {
                    List<DetectionEvent> events = device.performDetection(this, activeUAVs);
                    
                    if (events != null && !events.isEmpty()) {
                        // 发布探测事件
                        for (DetectionEvent event : events) {
                            publishEvent(new AirspaceEvent(AirspaceEventType.DETECTION_EVENT, this, event));
                        }
                        
                        // 更新统计信息
                        statistics.addDetectionEvents(events.size());
                    }
                } catch (Exception e) {
                    // 记录探测设备错误
                    device.setStatus(DeviceStatus.ERROR);
                    publishEvent(new AirspaceEvent(AirspaceEventType.PROBE_DEVICE_ERROR, this, device));
                }
            }
        }
    }
    
    // ================ 环境参数管理 ================
    
    /**
     * 更新环境参数
     * 
     * @param newParameters 新的环境参数
     */
    public void updateEnvironmentParameters(EnvironmentParameters newParameters) {
        if (newParameters == null) {
            throw new IllegalArgumentException("环境参数不能为空");
        }
        
        EnvironmentParameters oldParameters = this.environmentParameters;
        this.environmentParameters = newParameters;
        
        // 发布环境参数更新事件
        publishEvent(new AirspaceEvent(AirspaceEventType.ENVIRONMENT_UPDATED, this, oldParameters, newParameters));
    }
    
    // ================ AirspaceEnvironment接口实现 ================
    
    @Override
    public Velocity getWindVelocity() {
        double windSpeed = environmentParameters.getWindSpeed();
        double windDirection = environmentParameters.getWindDirection();
        
        // 将极坐标转换为笛卡尔坐标
        double windX = windSpeed * Math.cos(Math.toRadians(windDirection));
        double windY = windSpeed * Math.sin(Math.toRadians(windDirection));
        
        return new Velocity(windX, windY, 0.0);
    }
    
    @Override
    public double getTemperature() {
        return environmentParameters.getTemperature();
    }
    
    @Override
    public double getHumidity() {
        return environmentParameters.getHumidity() * 100.0; // 转换为百分比
    }
    
    @Override
    public double getAtmosphericPressure() {
        return environmentParameters.getAtmosphericPressure();
    }
    
    @Override
    public double getVisibility() {
        return environmentParameters.getVisibility();
    }
    
    // ================ 事件系统 ================
    
    /**
     * 添加事件监听器
     * 
     * @param listener 事件监听器
     */
    public void addEventListener(AirspaceEventListener listener) {
        if (listener != null) {
            eventListeners.add(listener);
        }
    }
    
    /**
     * 移除事件监听器
     * 
     * @param listener 事件监听器
     */
    public void removeEventListener(AirspaceEventListener listener) {
        eventListeners.remove(listener);
    }
    
    /**
     * 发布事件
     * 
     * @param event 空域事件
     */
    private void publishEvent(AirspaceEvent event) {
        for (AirspaceEventListener listener : eventListeners) {
            try {
                listener.onAirspaceEvent(event);
            } catch (Exception e) {
                // 记录监听器错误，但不中断事件发布
                System.err.println("事件监听器错误: " + e.getMessage());
            }
        }
    }
    
    // ================ 私有辅助方法 ================
    
    /**
     * 更新所有无人机状态
     */
    private void updateUAVs() {
        for (UAV uav : uavs.values()) {
            if (uav.getStatus().isOperational()) {
                uav.move(timeStep, this);
                
                // 更新空间索引中的位置
                spatialIndex.update(uav.getId(), uav.getPosition());
                
                // 检查是否超出边界
                if (!boundary.contains(uav.getPosition())) {
                    uav.updateStatus(UAVStatus.LOST_CONNECTION);
                    publishEvent(new AirspaceEvent(AirspaceEventType.UAV_OUT_OF_BOUNDS, this, uav));
                }
            }
        }
    }
    
    /**
     * 更新空间索引
     */
    private void updateSpatialIndex() {
        // 空间索引的更新在updateUAVs中已经完成
        // 这里可以添加额外的索引维护逻辑
    }
    
    /**
     * 启用所有活跃的探测设备
     */
    private void enableActiveProbeDevices() {
        for (ProbeDevice device : probeDevices.values()) {
            if (device.isInitialized() && !device.hasError()) {
                device.enable();
            }
        }
    }
    
    /**
     * 禁用所有探测设备
     */
    private void disableAllProbeDevices() {
        for (ProbeDevice device : probeDevices.values()) {
            device.disable();
        }
    }
    
    /**
     * 获取活跃的无人机列表
     * 
     * @return 活跃无人机列表
     */
    private List<UAV> getActiveUAVs() {
        return uavs.values().stream()
                .filter(uav -> uav.getStatus().isOperational())
                .collect(Collectors.toList());
    }
    
    /**
     * 更新统计信息
     */
    private void updateStatistics() {
        statistics.setCurrentTime(simulationTime);
        statistics.setActiveUAVCount((int) uavs.values().stream()
                .filter(uav -> uav.getStatus().isOperational()).count());
        statistics.setActiveProbeDeviceCount((int) probeDevices.values().stream()
                .filter(device -> device.isActive()).count());
    }
    
    // ================ Getter方法 ================
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
    }
    
    public AirspaceBoundary getBoundary() {
        return boundary;
    }
    
    public EnvironmentParameters getEnvironmentParameters() {
        return environmentParameters;
    }
    
    public double getTimeStep() {
        return timeStep;
    }
    
    public void setTimeStep(double timeStep) {
        if (timeStep > 0) {
            this.timeStep = timeStep;
        }
    }
    
    public LocalDateTime getSimulationTime() {
        return simulationTime;
    }
    
    public boolean isRunning() {
        return isRunning;
    }
    
    public Map<String, UAV> getUAVs() {
        return Collections.unmodifiableMap(uavs);
    }
    
    public Map<String, ProbeDevice> getProbeDevices() {
        return Collections.unmodifiableMap(probeDevices);
    }
    
    public SimulationStatistics getStatistics() {
        return statistics;
    }
    
    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    /**
     * 获取指定类型的探测设备
     * 
     * @param deviceType 设备类型
     * @return 指定类型的设备列表
     */
    public List<ProbeDevice> getProbeDevicesByType(DeviceType deviceType) {
        return probeDevices.values().stream()
                .filter(device -> device.getType() == deviceType)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取指定状态的无人机
     * 
     * @param status 无人机状态
     * @return 指定状态的无人机列表
     */
    public List<UAV> getUAVsByStatus(UAVStatus status) {
        return uavs.values().stream()
                .filter(uav -> uav.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    @Override
    public String toString() {
        return String.format("Airspace{id='%s', name='%s', running=%s, uavs=%d, devices=%d, time=%s}",
                           id, name, isRunning, uavs.size(), probeDevices.size(), simulationTime);
    }
} 