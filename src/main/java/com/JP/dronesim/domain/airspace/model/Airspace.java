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
 * 负责管理三维空域边界、环境参数和实体集合的核心业务逻辑
 * 确保空域内所有实体和操作的业务一致性
 *
 * @author JP Team
 * @version 1.0
 */
public class Airspace implements AirspaceEnvironment {

    /**
     * 空域唯一标识
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
     * 仿真配置
     */
    private SimulationConfig simulationConfig;

    /**
     * 仿真状态
     */
    private SimulationState simulationState;

    /**
     * 空间索引（用于快速查找实体）
     */
    private final SpatialIndex spatialIndex;

    /**
     * 无人机集合
     */
    private final Map<String, UAV> uavs;

    /**
     * 探测设备集合
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
     */
    public Airspace(String name, AirspaceBoundary boundary) {
        validateConstructorParameters(name, boundary);

        this.id = UUID.randomUUID().toString();
        this.name = name.trim();
        this.boundary = boundary;
        
        // 初始化默认值
        this.environmentParameters = EnvironmentParameters.createDefault();
        this.simulationConfig = SimulationConfig.createDefault();
        this.simulationState = SimulationState.STOPPED;
        
        // 初始化集合和索引
        this.spatialIndex = new SpatialIndex(boundary);
        this.uavs = new ConcurrentHashMap<>();
        this.probeDevices = new ConcurrentHashMap<>();
        this.eventListeners = new CopyOnWriteArrayList<>();
        this.statistics = new SimulationStatistics();
        
        // 设置时间戳
        this.createdAt = LocalDateTime.now();
        this.lastUpdatedAt = LocalDateTime.now();
        this.entityIdGenerator = new AtomicLong(1);
    }

    // ================ 仿真控制方法 ================

    /**
     * 启动仿真
     * 
     * @throws IllegalStateException 如果仿真已在运行或状态不允许启动
     */
    public void startSimulation() {
        validateSimulationStart();

        this.simulationState = SimulationState.RUNNING;
        this.statistics.setStartTime(LocalDateTime.now());
        updateLastModifiedTime();

        // 启用所有可用的探测设备
        enableAvailableProbeDevices();

        // 发布仿真启动事件
        publishEvent(AirspaceEventType.SIMULATION_STARTED);
    }

    /**
     * 暂停仿真
     * 
     * @throws IllegalStateException 如果仿真未在运行
     */
    public void pauseSimulation() {
        validateSimulationPause();

        this.simulationState = SimulationState.PAUSED;
        updateLastModifiedTime();

        // 发布仿真暂停事件
        publishEvent(AirspaceEventType.SIMULATION_PAUSED);
    }

    /**
     * 恢复仿真
     * 
     * @throws IllegalStateException 如果仿真未暂停
     */
    public void resumeSimulation() {
        validateSimulationResume();

        this.simulationState = SimulationState.RUNNING;
        updateLastModifiedTime();

        // 发布仿真启动事件
        publishEvent(AirspaceEventType.SIMULATION_STARTED);
    }

    /**
     * 停止仿真
     */
    public void stopSimulation() {
        this.simulationState = SimulationState.STOPPED;
        updateLastModifiedTime();

        // 禁用所有探测设备
        disableAllProbeDevices();

        // 发布仿真停止事件
        publishEvent(AirspaceEventType.SIMULATION_STOPPED);
    }

    /**
     * 推进仿真时间步长
     * 仅在仿真运行时执行
     */
    public void stepSimulation() {
        if (simulationState != SimulationState.RUNNING) {
            return;
        }

        // 增加仿真步数
        statistics.incrementSimulationSteps();
        statistics.setCurrentTime(LocalDateTime.now());
        updateLastModifiedTime();

        // 发布时间步进事件
        publishEvent(AirspaceEventType.TIME_STEPPED);
    }

    // ================ 实体管理方法 ================

    /**
     * 添加无人机到空域
     *
     * @param uav 无人机实体
     * @throws IllegalArgumentException 如果参数无效或无人机已存在
     * @throws IllegalStateException 如果无人机位置超出边界
     */
    public void addUAV(UAV uav) {
        validateUAVForAdd(uav);

        uavs.put(uav.getId(), uav);
        spatialIndex.insert(uav.getId(), uav.getPosition());
        statistics.incrementUAVCount();
        updateLastModifiedTime();

        // 发布无人机添加事件
        publishEvent(AirspaceEventType.UAV_ADDED, uav);
    }

    /**
     * 移除无人机
     *
     * @param uavId 无人机ID
     * @throws IllegalArgumentException 如果UAV ID为空
     */
    public void removeUAV(String uavId) {
        validateEntityId(uavId, "无人机ID");

        UAV removedUav = uavs.remove(uavId);
        if (removedUav != null) {
            spatialIndex.remove(uavId);
            statistics.decrementUAVCount();
            updateLastModifiedTime();

            // 发布无人机移除事件
            publishEvent(AirspaceEventType.UAV_REMOVED, removedUav);
        }
    }

    /**
     * 添加探测设备到空域
     *
     * @param device 探测设备
     * @throws IllegalArgumentException 如果参数无效或设备已存在
     * @throws IllegalStateException 如果设备位置超出边界
     */
    public void addProbeDevice(ProbeDevice device) {
        validateProbeDeviceForAdd(device);

        probeDevices.put(device.getId(), device);
        spatialIndex.insert(device.getId(), device.getPosition());
        statistics.incrementProbeDeviceCount();
        updateLastModifiedTime();

        // 发布探测设备添加事件
        publishEvent(AirspaceEventType.PROBE_DEVICE_ADDED, device);
    }

    /**
     * 移除探测设备
     *
     * @param deviceId 设备ID
     * @throws IllegalArgumentException 如果设备ID为空
     */
    public void removeProbeDevice(String deviceId) {
        validateEntityId(deviceId, "设备ID");

        ProbeDevice removedDevice = probeDevices.remove(deviceId);
        if (removedDevice != null) {
            spatialIndex.remove(deviceId);
            statistics.decrementProbeDeviceCount();
            updateLastModifiedTime();

            // 发布探测设备移除事件
            publishEvent(AirspaceEventType.PROBE_DEVICE_REMOVED, removedDevice);
        }
    }

    /**
     * 更新实体位置
     * 当实体位置发生变化时调用此方法更新空间索引
     *
     * @param entityId 实体ID
     * @param newPosition 新位置
     */
    public void updateEntityPosition(String entityId, Position newPosition) {
        validateEntityId(entityId, "实体ID");
        if (newPosition == null) {
            throw new IllegalArgumentException("新位置不能为空");
        }

        spatialIndex.update(entityId, newPosition);
        updateLastModifiedTime();
    }

    // ================ 空间查询方法 ================

    /**
     * 查询指定范围内的所有实体
     *
     * @param center 查询中心点
     * @param radius 查询半径（米）
     * @return 范围内的实体ID列表
     */
    public List<String> queryEntitiesInRange(Position center, double radius) {
        return spatialIndex.queryRange(center, radius);
    }

    /**
     * 查询指定矩形区域内的所有实体
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
     * @param maxDistance 最大距离（米）
     * @return 最近的实体ID，如果没有找到则返回null
     */
    public String findNearestEntity(Position position, double maxDistance) {
        return spatialIndex.findNearest(position, maxDistance);
    }

    // ================ 环境参数管理 ================

    /**
     * 更新环境参数
     *
     * @param newParameters 新的环境参数
     * @throws IllegalArgumentException 如果参数为空
     */
    public void updateEnvironmentParameters(EnvironmentParameters newParameters) {
        if (newParameters == null) {
            throw new IllegalArgumentException("环境参数不能为空");
        }

        EnvironmentParameters oldParameters = this.environmentParameters;
        this.environmentParameters = newParameters;
        updateLastModifiedTime();

        // 发布环境参数更新事件
        publishEvent(AirspaceEventType.ENVIRONMENT_UPDATED, oldParameters, newParameters);
    }

    /**
     * 更新仿真配置
     *
     * @param newConfig 新的仿真配置
     * @throws IllegalArgumentException 如果配置为空
     */
    public void updateSimulationConfig(SimulationConfig newConfig) {
        if (newConfig == null) {
            throw new IllegalArgumentException("仿真配置不能为空");
        }

        this.simulationConfig = newConfig;
        updateLastModifiedTime();

        // 发布配置变更事件
        publishEvent(AirspaceEventType.AIRSPACE_CONFIGURATION_CHANGED, newConfig);
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
        if (listener != null && !eventListeners.contains(listener)) {
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
     * @param eventType 事件类型
     * @param eventData 事件数据
     */
    private void publishEvent(AirspaceEventType eventType, Object... eventData) {
        if (eventListeners.isEmpty()) {
            return;
        }

        AirspaceEvent event = new AirspaceEvent(eventType, this, eventData);
        
        for (AirspaceEventListener listener : eventListeners) {
            if (listener.supportsEventType(eventType)) {
                try {
                    listener.onAirspaceEvent(event);
                } catch (Exception e) {
                    // 记录监听器错误，但不中断其他监听器
                    System.err.println("事件监听器错误: " + e.getMessage());
                }
            }
        }
    }

    // ================ 查询方法 ================

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

    /**
     * 获取活跃的无人机列表
     *
     * @return 活跃无人机列表
     */
    public List<UAV> getActiveUAVs() {
        return uavs.values().stream()
                .filter(uav -> uav.getStatus().isOperational())
                .collect(Collectors.toList());
    }

    /**
     * 获取活跃的探测设备列表
     *
     * @return 活跃探测设备列表
     */
    public List<ProbeDevice> getActiveProbeDevices() {
        return probeDevices.values().stream()
                .filter(device -> device.isActive())
                .collect(Collectors.toList());
    }

    // ================ 私有辅助方法 ================

    /**
     * 验证构造函数参数
     */
    private void validateConstructorParameters(String name, AirspaceBoundary boundary) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("空域名称不能为空");
        }
        if (boundary == null) {
            throw new IllegalArgumentException("空域边界不能为空");
        }
    }

    /**
     * 验证仿真启动条件
     */
    private void validateSimulationStart() {
        if (simulationState == SimulationState.RUNNING) {
            throw new IllegalStateException("仿真已在运行");
        }
    }

    /**
     * 验证仿真暂停条件
     */
    private void validateSimulationPause() {
        if (simulationState != SimulationState.RUNNING) {
            throw new IllegalStateException("仿真未在运行");
        }
    }

    /**
     * 验证仿真恢复条件
     */
    private void validateSimulationResume() {
        if (simulationState != SimulationState.PAUSED) {
            throw new IllegalStateException("仿真未暂停");
        }
    }

    /**
     * 验证UAV添加参数
     */
    private void validateUAVForAdd(UAV uav) {
        if (uav == null) {
            throw new IllegalArgumentException("无人机不能为空");
        }
        if (uavs.containsKey(uav.getId())) {
            throw new IllegalArgumentException("无人机已存在: " + uav.getId());
        }
        if (!boundary.contains(uav.getPosition())) {
            throw new IllegalStateException("无人机位置超出空域边界");
        }
    }

    /**
     * 验证探测设备添加参数
     */
    private void validateProbeDeviceForAdd(ProbeDevice device) {
        if (device == null) {
            throw new IllegalArgumentException("探测设备不能为空");
        }
        if (probeDevices.containsKey(device.getId())) {
            throw new IllegalArgumentException("探测设备已存在: " + device.getId());
        }
        if (!boundary.contains(device.getPosition())) {
            throw new IllegalStateException("探测设备位置超出空域边界");
        }
    }

    /**
     * 验证实体ID
     */
    private void validateEntityId(String entityId, String entityType) {
        if (entityId == null || entityId.trim().isEmpty()) {
            throw new IllegalArgumentException(entityType + "不能为空");
        }
    }

    /**
     * 启用所有可用的探测设备
     */
    private void enableAvailableProbeDevices() {
        for (ProbeDevice device : probeDevices.values()) {
            if (device.getStatus() == DeviceStatus.INACTIVE) {
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
     * 更新最后修改时间
     */
    private void updateLastModifiedTime() {
        this.lastUpdatedAt = LocalDateTime.now();
    }

    // ================ Getter方法 ================

    public String getId() { return id; }
    public String getName() { return name; }
    public AirspaceBoundary getBoundary() { return boundary; }
    public EnvironmentParameters getEnvironmentParameters() { return environmentParameters; }
    public SimulationConfig getSimulationConfig() { return simulationConfig; }
    public SimulationState getSimulationState() { return simulationState; }
    public boolean isRunning() { return simulationState == SimulationState.RUNNING; }
    public boolean isPaused() { return simulationState == SimulationState.PAUSED; }
    public boolean isStopped() { return simulationState == SimulationState.STOPPED; }
    public Map<String, UAV> getUAVs() { return Collections.unmodifiableMap(uavs); }
    public Map<String, ProbeDevice> getProbeDevices() { return Collections.unmodifiableMap(probeDevices); }
    public SimulationStatistics getStatistics() { return statistics; }
    public LocalDateTime getLastUpdatedAt() { return lastUpdatedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public int getUAVCount() { return uavs.size(); }
    public int getProbeDeviceCount() { return probeDevices.size(); }
    public int getTotalEntityCount() { return uavs.size() + probeDevices.size(); }

    /**
     * 设置空域名称
     *
     * @param name 新名称
     */
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
            updateLastModifiedTime();
        }
    }

    @Override
    public String toString() {
        return String.format("Airspace{id='%s', name='%s', state=%s, uavs=%d, devices=%d}",
                           id, name, simulationState, uavs.size(), probeDevices.size());
    }

    // ================ 内部枚举类 ================

    /**
     * 仿真状态枚举
     */
    public enum SimulationState {
        /**
         * 已停止
         */
        STOPPED("已停止"),
        
        /**
         * 运行中
         */
        RUNNING("运行中"),
        
        /**
         * 已暂停
         */
        PAUSED("已暂停");

        private final String description;

        SimulationState(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    /**
     * 仿真配置值对象
     */
    public static class SimulationConfig {
        /**
         * 时间步长（秒）
         */
        private final double timeStep;

        /**
         * 最大仿真时间（秒）
         */
        private final double maxSimulationTime;

        /**
         * 是否启用实时模式
         */
        private final boolean realTimeMode;

        public SimulationConfig(double timeStep, double maxSimulationTime, boolean realTimeMode) {
            if (timeStep <= 0) {
                throw new IllegalArgumentException("时间步长必须大于0");
            }
            if (maxSimulationTime <= 0) {
                throw new IllegalArgumentException("最大仿真时间必须大于0");
            }

            this.timeStep = timeStep;
            this.maxSimulationTime = maxSimulationTime;
            this.realTimeMode = realTimeMode;
        }

        public static SimulationConfig createDefault() {
            return new SimulationConfig(0.1, 3600.0, true);
        }

        public double getTimeStep() { return timeStep; }
        public double getMaxSimulationTime() { return maxSimulationTime; }
        public boolean isRealTimeMode() { return realTimeMode; }

        @Override
        public String toString() {
            return String.format("SimulationConfig{timeStep=%.3f, maxTime=%.1f, realTime=%s}",
                               timeStep, maxSimulationTime, realTimeMode);
        }
    }
}
