package com.JP.dronesim.domain.airspace.model;

import com.JP.dronesim.domain.common.enums.DeviceStatus;
import com.JP.dronesim.domain.common.enums.DeviceType;
import com.JP.dronesim.domain.common.enums.UAVStatus;
import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.common.valueobjects.Velocity;
import com.JP.dronesim.domain.device.model.AbstractProbeDevice;
import com.JP.dronesim.domain.uav.model.UAV;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 空域聚合根
 * 负责管理三维空域边界、环境参数和实体集合的核心业务逻辑
 * 确保空域内所有实体和操作的业务一致性
 *
 * @author JP Team
 * @version 1.0
 */
public class Airspace {

    /**
     * 空域唯一标识
     */
    private final String id;

    /**
     * 空域名称
     */
    private String name;

    /**
     * 空域边界最小/最大坐标
     */
    private final double minX, minY, minZ, maxX, maxY, maxZ;

    /**
     * 环境参数
     */
    private EnvironmentParameters environmentParameters;

    /**
     * 仿真状态
     */
    private SimulationState simulationState;

    /**
     * 时间步长管理
     */
    private TimeStep timeStep;

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
    private final Map<String, AbstractProbeDevice> probeDevices;

    /**
     * 最后更新时间
     */
    private LocalDateTime lastUpdatedAt;

    /**
     * 创建时间
     */
    private final LocalDateTime createdAt;

    /**
     * 构造函数
     *
     * @param name 空域名称
     * @param minX 最小X
     * @param minY 最小Y
     * @param minZ 最小Z
     * @param maxX 最大X
     * @param maxY 最大Y
     * @param maxZ 最大Z
     */
    public Airspace(String name, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        validateConstructorParameters(name, minX, minY, minZ, maxX, maxY, maxZ);

        this.id = UUID.randomUUID().toString();
        this.name = name.trim();
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        
        // 初始化组件
        this.environmentParameters = EnvironmentParameters.createDefault();
        this.simulationState = SimulationState.STOPPED;
        this.timeStep = TimeStep.createDefault();
        this.spatialIndex = new SpatialIndex(minX, minY, minZ, maxX, maxY, maxZ);
        this.uavs = new HashMap<>();
        this.probeDevices = new HashMap<>();
        this.createdAt = LocalDateTime.now();
        this.lastUpdatedAt = LocalDateTime.now();
    }

    /**
     * 默认构造函数
     */
    public Airspace() {
        this("默认空域", -1000, -1000, 0, 1000, 1000, 500);
    }

    // ================ 仿真控制方法 ================

    /**
     * 启动仿真
     *
     * @throws IllegalStateException 如果仿真状态不允许启动
     */
    public void startSimulation() {
        if (!simulationState.canStart()) {
            throw new IllegalStateException("当前状态不能启动仿真: " + simulationState.getDisplayName());
        }

        this.simulationState = SimulationState.RUNNING;
        this.timeStep = timeStep.reset();
        updateLastModifiedTime();

        // 启用所有可用的探测设备
        enableAvailableProbeDevices();
    }

    /**
     * 暂停仿真
     *
     * @throws IllegalStateException 如果仿真状态不允许暂停
     */
    public void pauseSimulation() {
        if (!simulationState.canPause()) {
            throw new IllegalStateException("当前状态不能暂停仿真: " + simulationState.getDisplayName());
        }

        this.simulationState = SimulationState.PAUSED;
        updateLastModifiedTime();
    }

    /**
     * 恢复仿真
     *
     * @throws IllegalStateException 如果仿真状态不允许恢复
     */
    public void resumeSimulation() {
        if (!simulationState.canResume()) {
            throw new IllegalStateException("当前状态不能恢复仿真: " + simulationState.getDisplayName());
        }

        this.simulationState = SimulationState.RUNNING;
        updateLastModifiedTime();
    }

    /**
     * 停止仿真
     */
    public void stopSimulation() {
        this.simulationState = SimulationState.STOPPED;
        this.timeStep = timeStep.reset();
        updateLastModifiedTime();

        // 禁用所有探测设备
        disableAllProbeDevices();
    }

    /**
     * 推进仿真时间步
     */
    public void stepSimulation() {
        if (!simulationState.isRunning()) {
            throw new IllegalStateException("仿真未运行，不能推进时间步");
        }

        this.timeStep = timeStep.advance();
        updateLastModifiedTime();

        // 更新空间索引
        updateSpatialIndex();
    }

    // ================ 实体管理方法 ================

    /**
     * 添加无人机
     *
     * @param uav 无人机实体
     * @throws IllegalArgumentException 如果无人机参数无效
     * @throws IllegalStateException 如果无人机位置超出边界
     */
    public void addUAV(UAV uav) {
        if (uav == null) {
            throw new IllegalArgumentException("无人机不能为空");
        }
        if (uavs.containsKey(uav.getId())) {
            throw new IllegalArgumentException("无人机ID已存在: " + uav.getId());
        }
        if (!contains(uav.getPosition())) {
            throw new IllegalStateException("无人机位置超出空域边界");
        }

        uavs.put(uav.getId(), uav);
        spatialIndex.addEntity(uav.getId(), uav.getPosition());
        updateLastModifiedTime();
    }

    /**
     * 移除无人机
     *
     * @param uavId 无人机ID
     * @throws IllegalArgumentException 如果无人机不存在
     */
    public void removeUAV(String uavId) {
        if (!uavs.containsKey(uavId)) {
            throw new IllegalArgumentException("无人机不存在: " + uavId);
        }

        uavs.remove(uavId);
        spatialIndex.removeEntity(uavId);
        updateLastModifiedTime();
    }

    /**
     * 添加探测设备
     *
     * @param device 探测设备
     * @throws IllegalArgumentException 如果设备参数无效
     * @throws IllegalStateException 如果设备位置超出边界
     */
    public void addProbeDevice(AbstractProbeDevice device) {
        if (device == null) {
            throw new IllegalArgumentException("探测设备不能为空");
        }
        if (probeDevices.containsKey(device.getId())) {
            throw new IllegalArgumentException("设备ID已存在: " + device.getId());
        }
        if (!contains(device.getPosition())) {
            throw new IllegalStateException("设备位置超出空域边界");
        }

        probeDevices.put(device.getId(), device);
        spatialIndex.addEntity(device.getId(), device.getPosition());
        updateLastModifiedTime();
    }

    /**
     * 移除探测设备
     *
     * @param deviceId 设备ID
     * @throws IllegalArgumentException 如果设备不存在
     */
    public void removeProbeDevice(String deviceId) {
        if (!probeDevices.containsKey(deviceId)) {
            throw new IllegalArgumentException("探测设备不存在: " + deviceId);
        }

        probeDevices.remove(deviceId);
        spatialIndex.removeEntity(deviceId);
        updateLastModifiedTime();
    }

    /**
     * 更新实体位置
     *
     * @param entityId 实体ID
     * @param newPosition 新位置
     * @throws IllegalArgumentException 如果实体不存在或位置无效
     */
    public void updateEntityPosition(String entityId, Position newPosition) {
        if (newPosition == null) {
            throw new IllegalArgumentException("位置不能为空");
        }
        if (!contains(newPosition)) {
            throw new IllegalArgumentException("位置超出空域边界");
        }

        if (uavs.containsKey(entityId)) {
            spatialIndex.updateEntityPosition(entityId, newPosition);
        } else if (probeDevices.containsKey(entityId)) {
            spatialIndex.updateEntityPosition(entityId, newPosition);
        } else {
            throw new IllegalArgumentException("实体不存在: " + entityId);
        }

        updateLastModifiedTime();
    }

    // ================ 空间查询方法 ================

    /**
     * 查询指定范围内的实体
     *
     * @param center 中心位置
     * @param radius 半径
     * @return 实体ID列表
     */
    public List<String> queryEntitiesInRange(Position center, double radius) {
        return spatialIndex.queryInRange(center, radius);
    }

    /**
     * 查询指定区域内的实体
     *
     * @param minX 最小X
     * @param minY 最小Y
     * @param minZ 最小Z
     * @param maxX 最大X
     * @param maxY 最大Y
     * @param maxZ 最大Z
     * @return 实体ID列表
     */
    public List<String> queryEntitiesInBox(double minX, double minY, double minZ,
                                          double maxX, double maxY, double maxZ) {
        return spatialIndex.queryInBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * 查找最近的实体
     *
     * @param position 查询位置
     * @param maxDistance 最大距离
     * @return 最近实体ID，如果没有则返回null
     */
    public String findNearestEntity(Position position, double maxDistance) {
        return spatialIndex.findNearest(position, maxDistance);
    }

    // ================ 环境管理方法 ================

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

        this.environmentParameters = newParameters;
        updateLastModifiedTime();
    }

    /**
     * 更新时间步长
     *
     * @param newStepSize 新的时间步长（秒）
     * @throws IllegalArgumentException 如果步长无效
     */
    public void updateTimeStep(double newStepSize) {
        this.timeStep = timeStep.withStepSize(newStepSize);
        updateLastModifiedTime();
    }

    // ================ 查询方法 ================

    /**
     * 根据类型获取探测设备
     *
     * @param deviceType 设备类型
     * @return 设备列表
     */
    public List<ProbeDevice> getProbeDevicesByType(DeviceType deviceType) {
        return probeDevices.values().stream()
                .filter(device -> device.getType() == deviceType)
                .collect(Collectors.toList());
    }

    /**
     * 根据状态获取无人机
     *
     * @param status 无人机状态
     * @return 无人机列表
     */
    public List<UAV> getUAVsByStatus(UAVStatus status) {
        return uavs.values().stream()
                .filter(uav -> uav.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * 获取活跃的无人机
     *
     * @return 活跃无人机列表
     */
    public List<UAV> getActiveUAVs() {
        return uavs.values().stream()
                .filter(uav -> uav.getStatus() != UAVStatus.INACTIVE)
                .collect(Collectors.toList());
    }

    /**
     * 获取活跃的探测设备
     *
     * @return 活跃设备列表
     */
    public List<ProbeDevice> getActiveProbeDevices() {
        return probeDevices.values().stream()
                .filter(device -> device.getStatus() == DeviceStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    /**
     * 检查位置是否在边界内
     *
     * @param position 位置
     * @return 是否在边界内
     */
    public boolean contains(Position position) {
        if (position == null) {
            return false;
        }
        return position.getX() >= minX && position.getX() <= maxX &&
               position.getY() >= minY && position.getY() <= maxY &&
               position.getZ() >= minZ && position.getZ() <= maxZ;
    }

    // ================ 私有辅助方法 ================

    /**
     * 验证构造函数参数
     */
    private void validateConstructorParameters(String name, double minX, double minY, double minZ, 
                                             double maxX, double maxY, double maxZ) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("空域名称不能为空");
        }
        if (minX >= maxX || minY >= maxY || minZ >= maxZ) {
            throw new IllegalArgumentException("空域边界参数无效");
        }
    }

    /**
     * 启用可用的探测设备
     */
    private void enableAvailableProbeDevices() {
        probeDevices.values().forEach(device -> {
            if (device.getStatus() == DeviceStatus.INACTIVE) {
                // 这里可以添加设备启用逻辑
            }
        });
    }

    /**
     * 禁用所有探测设备
     */
    private void disableAllProbeDevices() {
        probeDevices.values().forEach(device -> {
            // 这里可以添加设备禁用逻辑
        });
    }

    /**
     * 更新空间索引
     */
    private void updateSpatialIndex() {
        // 更新所有实体的空间索引
        uavs.values().forEach(uav -> 
            spatialIndex.updateEntityPosition(uav.getId(), uav.getPosition()));
        probeDevices.values().forEach(device -> 
            spatialIndex.updateEntityPosition(device.getId(), device.getPosition()));
    }

    /**
     * 更新最后修改时间
     */
    private void updateLastModifiedTime() {
        this.lastUpdatedAt = LocalDateTime.now();
    }

    // ================ Getters ================

    public String getId() { return id; }
    public String getName() { return name; }
    public double getMinX() { return minX; }
    public double getMinY() { return minY; }
    public double getMinZ() { return minZ; }
    public double getMaxX() { return maxX; }
    public double getMaxY() { return maxY; }
    public double getMaxZ() { return maxZ; }
    public EnvironmentParameters getEnvironmentParameters() { return environmentParameters; }
    public SimulationState getSimulationState() { return simulationState; }
    public TimeStep getTimeStep() { return timeStep; }
    public boolean isRunning() { return simulationState.isRunning(); }
    public boolean isPaused() { return simulationState.isPaused(); }
    public boolean isStopped() { return simulationState.isStopped(); }
    public Map<String, UAV> getUAVs() { return Collections.unmodifiableMap(uavs); }
    public Map<String, ProbeDevice> getProbeDevices() { return Collections.unmodifiableMap(probeDevices); }
    public LocalDateTime getLastUpdatedAt() { return lastUpdatedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public int getUAVCount() { return uavs.size(); }
    public int getProbeDeviceCount() { return probeDevices.size(); }
    public int getTotalEntityCount() { return uavs.size() + probeDevices.size(); }

    /**
     * 获取空域的风速向量（从环境参数中提取）
     *
     * @return 风速向量
     */
    public Velocity getWindVelocity() {
        double windSpeed = environmentParameters.getWindSpeed();
        double windDirection = Math.toRadians(environmentParameters.getWindDirection());
        double vx = windSpeed * Math.cos(windDirection);
        double vy = windSpeed * Math.sin(windDirection);
        return new Velocity(vx, vy, 0);
    }

    /**
     * 获取环境温度
     *
     * @return 温度（摄氏度）
     */
    public double getTemperature() {
        return environmentParameters.getTemperature();
    }

    /**
     * 获取湿度
     *
     * @return 湿度（0-1）
     */
    public double getHumidity() {
        return environmentParameters.getHumidity();
    }

    /**
     * 获取大气压力
     *
     * @return 大气压力（帕斯卡）
     */
    public double getAtmosphericPressure() {
        return environmentParameters.getAtmosphericPressure();
    }

    /**
     * 获取能见度
     *
     * @return 能见度（米）
     */
    public double getVisibility() {
        return environmentParameters.getVisibility();
    }
}
