package com.JP.dronesim.domain.uav.model;

import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.common.valueobjects.Orientation;
import com.JP.dronesim.domain.common.valueobjects.Velocity;
import com.JP.dronesim.domain.common.valueobjects.Acceleration;
import com.JP.dronesim.domain.common.enums.UAVStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 无人机聚合根
 * 无人机作为被探测目标，其移动和特征生成是仿真的关键
 * 
 * @author JP Team
 * @version 1.0
 */
public class UAV {
    
    /**
     * 设备唯一标识符
     */
    private final String id;
    
    /**
     * 无人机名称
     */
    private String name;
    
    /**
     * 位置：表示实体在NED坐标系中的三维位置 (x, y, z)
     */
    private Position position;
    
    /**
     * 姿态：表示实体的三维姿态（四元数或欧拉角）
     */
    private Orientation orientation;
    
    /**
     * 速度：表示无人机在NED坐标系中的三维速度 (vx, vy, vz)
     */
    private Velocity velocity;
    
    /**
     * 加速度：表示无人机在NED坐标系中的三维加速度 (ax, ay, az)
     */
    private Acceleration acceleration;
    
    /**
     * 物理材质：描述无人机的材质特征（电磁波反射）
     */
    private PhysicalProperties physicalProperties;
    
    /**
     * 飞行路径：无人机的飞行路径，航点列表
     */
    private List<Waypoint> flightPath;
    
    /**
     * 当前目标航点索引
     */
    private int currentWaypointIndex;
    
    /**
     * 运行状态：悬停、运动、侦测（枚举）
     */
    private UAVStatus status;
    
    /**
     * 创建时间
     */
    private final LocalDateTime createdAt;
    
    /**
     * 最后更新时间
     */
    private LocalDateTime lastUpdatedAt;
    
    /**
     * 最大速度限制（m/s）
     */
    private double maxSpeed;
    
    /**
     * 最大加速度限制（m/s²）
     */
    private double maxAcceleration;
    
    /**
     * 电池电量百分比（0-100）
     */
    private double batteryLevel;
    
    /**
     * 构造函数
     * 
     * @param name 无人机名称
     * @param initialPosition 初始位置
     * @param physicalProperties 物理特性
     */
    public UAV(String name, Position initialPosition, PhysicalProperties physicalProperties) {
        // 参数验证
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("无人机名称不能为空");
        }
        if (initialPosition == null) {
            throw new IllegalArgumentException("初始位置不能为空");
        }
        if (physicalProperties == null) {
            throw new IllegalArgumentException("物理特性不能为空");
        }
        
        this.id = UUID.randomUUID().toString();
        this.name = name.trim();
        this.position = initialPosition;
        this.orientation = Orientation.identity(); // 初始姿态为无旋转
        this.velocity = Velocity.zero(); // 初始速度为零
        this.acceleration = Acceleration.zero(); // 初始加速度为零
        this.physicalProperties = physicalProperties;
        this.flightPath = new ArrayList<>();
        this.currentWaypointIndex = 0;
        this.status = UAVStatus.STANDBY;
        this.createdAt = LocalDateTime.now();
        this.lastUpdatedAt = LocalDateTime.now();
        this.maxSpeed = 20.0; // 默认最大速度20 m/s
        this.maxAcceleration = 5.0; // 默认最大加速度5 m/s²
        this.batteryLevel = 100.0; // 初始电量100%
    }
    
    /**
     * 初始化无人机，设置其初始位置、速度、姿态和飞行任务
     * 
     * @param initialState 包含初始位置、速度、姿态等的 UAVState 对象
     * @param mission 定义飞行路径（航点）或任务类型的 UAVMission 对象
     */
    public void initialize(UAVState initialState, UAVMission mission) {
        if (initialState == null) {
            throw new IllegalArgumentException("初始状态不能为空");
        }
        if (mission == null) {
            throw new IllegalArgumentException("飞行任务不能为空");
        }
        
        // 设置初始状态
        this.position = initialState.getPosition();
        this.orientation = initialState.getOrientation();
        this.velocity = initialState.getVelocity();
        this.acceleration = initialState.getAcceleration();
        
        // 设置飞行路径
        this.flightPath = new ArrayList<>(mission.getWaypoints());
        this.currentWaypointIndex = 0;
        
        // 设置性能限制
        this.maxSpeed = mission.getMaxSpeed();
        this.maxAcceleration = mission.getMaxAcceleration();
        
        // 更新状态
        this.status = UAVStatus.STANDBY;
        this.lastUpdatedAt = LocalDateTime.now();
        
        // 验证初始化状态
        validateState();
    }
    
    /**
     * 根据指定坐标，更新无人机在当前状态下去到指定位置的航行状态
     * 
     * @param targetLocation 目标位置对象
     */
    public void move(Position targetLocation) {
        if (targetLocation == null) {
            throw new IllegalArgumentException("目标位置不能为空");
        }
        if (!canMove()) {
            throw new IllegalStateException("无人机当前状态不允许移动：" + status);
        }
        
        // 计算到目标位置的方向和距离
        double distance = position.distanceTo(targetLocation);
        if (distance < 0.1) { // 已经很接近目标位置
            return;
        }
        
        // 计算方向向量
        double dx = targetLocation.getX() - position.getX();
        double dy = targetLocation.getY() - position.getY();
        double dz = targetLocation.getZ() - position.getZ();
        
        // 归一化方向向量
        double norm = Math.sqrt(dx * dx + dy * dy + dz * dz);
        dx /= norm;
        dy /= norm;
        dz /= norm;
        
        // 设置目标速度（限制在最大速度内）
        double targetSpeed = Math.min(maxSpeed, distance * 0.5); // 根据距离调整速度
        this.velocity = new Velocity(dx * targetSpeed, dy * targetSpeed, dz * targetSpeed);
        
        // 更新状态
        this.status = UAVStatus.MOVING;
        this.lastUpdatedAt = LocalDateTime.now();
    }
    
    /**
     * 根据自身速度、加速度和飞行任务，以及空域环境状态，更新无人机在指定时间步长后的位置和姿态
     * 
     * @param deltaTime 时间步长
     * @param environment 当前空域环境对象，用于获取风力等影响因素
     */
    public void move(double deltaTime, AirspaceEnvironment environment) {
        if (deltaTime <= 0) {
            throw new IllegalArgumentException("时间步长必须大于零");
        }
        if (environment == null) {
            throw new IllegalArgumentException("空域环境不能为空");
        }
        if (!canMove()) {
            return; // 不能移动时直接返回
        }
        
        // 获取环境影响
        Velocity windEffect = environment.getWindVelocity();
        Acceleration gravityEffect = Acceleration.gravity();
        
        // 计算总加速度（包括环境影响）
        Acceleration totalAcceleration = acceleration.add(gravityEffect);
        
        // 限制加速度
        totalAcceleration = totalAcceleration.limit(maxAcceleration);
        
        // 更新速度
        Velocity velocityDelta = totalAcceleration.toVelocityDelta(deltaTime);
        this.velocity = velocity.add(velocityDelta).add(windEffect);
        
        // 限制速度
        this.velocity = velocity.limit(maxSpeed);
        
        // 更新位置
        double newX = position.getX() + velocity.getVx() * deltaTime;
        double newY = position.getY() + velocity.getVy() * deltaTime;
        double newZ = position.getZ() + velocity.getVz() * deltaTime;
        this.position = new Position(newX, newY, newZ);
        
        // 更新姿态（根据速度方向）
        updateOrientationFromVelocity();
        
        // 检查是否到达当前目标航点
        checkWaypointReached();
        
        // 消耗电量
        consumeBattery(deltaTime);
        
        // 更新时间戳
        this.lastUpdatedAt = LocalDateTime.now();
        
        // 验证状态
        validateState();
    }
    
    /**
     * 更新无人机的状态，如被探测、受损、故障等
     * 
     * @param newStatus UAVStatus 枚举值，表示无人机的新状态
     */
    public void updateStatus(UAVStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("新状态不能为空");
        }
        
        UAVStatus oldStatus = this.status;
        this.status = newStatus;
        this.lastUpdatedAt = LocalDateTime.now();
        
        // 状态变化时的特殊处理
        handleStatusChange(oldStatus, newStatus);
    }
    
    /**
     * 获取无人机当前的物理特征签名，供探测设备进行探测计算
     * 
     * @return PhysicalSignature：包含当前RCS、光学特征、无线电发射功率等
     */
    public PhysicalSignature getPhysicalSignature() {
        return new PhysicalSignature(
            physicalProperties.getRadarCrossSection(),
            physicalProperties.getOpticalVisibility(),
            physicalProperties.getRadioEmissionPower(),
            physicalProperties.getInfraredSignature(),
            position,
            velocity.getMagnitude(),
            status
        );
    }
    
    // Getter方法
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public Orientation getOrientation() {
        return orientation;
    }
    
    public Velocity getVelocity() {
        return velocity;
    }
    
    public Acceleration getAcceleration() {
        return acceleration;
    }
    
    public PhysicalProperties getPhysicalProperties() {
        return physicalProperties;
    }
    
    public List<Waypoint> getFlightPath() {
        return Collections.unmodifiableList(flightPath);
    }
    
    public UAVStatus getStatus() {
        return status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }
    
    public double getMaxSpeed() {
        return maxSpeed;
    }
    
    public double getMaxAcceleration() {
        return maxAcceleration;
    }
    
    public double getBatteryLevel() {
        return batteryLevel;
    }
    
    public Waypoint getCurrentWaypoint() {
        if (flightPath.isEmpty() || currentWaypointIndex >= flightPath.size()) {
            return null;
        }
        return flightPath.get(currentWaypointIndex);
    }
    
    // 业务方法
    
    /**
     * 设置飞行路径
     * 
     * @param waypoints 航点列表
     */
    public void setFlightPath(List<Waypoint> waypoints) {
        if (waypoints == null) {
            throw new IllegalArgumentException("航点列表不能为空");
        }
        this.flightPath = new ArrayList<>(waypoints);
        this.currentWaypointIndex = 0;
        this.lastUpdatedAt = LocalDateTime.now();
    }
    
    /**
     * 添加航点到飞行路径
     * 
     * @param waypoint 航点
     */
    public void addWaypoint(Waypoint waypoint) {
        if (waypoint == null) {
            throw new IllegalArgumentException("航点不能为空");
        }
        this.flightPath.add(waypoint);
        this.lastUpdatedAt = LocalDateTime.now();
    }
    
    /**
     * 开始执行飞行任务
     */
    public void startMission() {
        if (flightPath.isEmpty()) {
            throw new IllegalStateException("没有设置飞行路径");
        }
        if (!status.canAcceptCommands()) {
            throw new IllegalStateException("无人机当前状态不允许开始任务：" + status);
        }
        
        this.status = UAVStatus.TAKING_OFF;
        this.currentWaypointIndex = 0;
        this.lastUpdatedAt = LocalDateTime.now();
    }
    
    /**
     * 暂停当前任务
     */
    public void pauseMission() {
        if (status.isMoving()) {
            this.status = UAVStatus.HOVERING;
            this.velocity = Velocity.zero();
            this.lastUpdatedAt = LocalDateTime.now();
        }
    }
    
    /**
     * 恢复任务
     */
    public void resumeMission() {
        if (status == UAVStatus.HOVERING && !flightPath.isEmpty()) {
            this.status = UAVStatus.MOVING;
            this.lastUpdatedAt = LocalDateTime.now();
        }
    }
    
    /**
     * 返航
     */
    public void returnToHome() {
        this.status = UAVStatus.RETURNING;
        // 可以添加返航逻辑，比如设置返航航点
        this.lastUpdatedAt = LocalDateTime.now();
    }
    
    /**
     * 紧急降落
     */
    public void emergencyLanding() {
        this.status = UAVStatus.LANDING;
        this.velocity = new Velocity(0, 0, 2.0); // 垂直降落
        this.lastUpdatedAt = LocalDateTime.now();
    }
    
    // 私有辅助方法
    
    /**
     * 检查是否可以移动
     */
    private boolean canMove() {
        return status.canAcceptCommands() && batteryLevel > 10.0;
    }
    
    /**
     * 根据速度方向更新姿态
     */
    private void updateOrientationFromVelocity() {
        if (!velocity.isStationary()) {
            double yaw = velocity.getDirection();
            double pitch = Math.atan2(-velocity.getVz(), velocity.getHorizontalMagnitude());
            this.orientation = Orientation.fromEulerAngles(0, pitch, yaw);
        }
    }
    
    /**
     * 检查是否到达当前航点
     */
    private void checkWaypointReached() {
        Waypoint currentWaypoint = getCurrentWaypoint();
        if (currentWaypoint != null && currentWaypoint.isNearby(position)) {
            currentWaypoint.markAsReached();
            currentWaypointIndex++;
            
            // 检查是否完成所有航点
            if (currentWaypointIndex >= flightPath.size()) {
                this.status = UAVStatus.HOVERING; // 任务完成，悬停
            }
        }
    }
    
    /**
     * 消耗电量
     */
    private void consumeBattery(double deltaTime) {
        // 根据速度和加速度计算电量消耗
        double speedFactor = velocity.getMagnitude() / maxSpeed;
        double accelerationFactor = acceleration.getMagnitude() / maxAcceleration;
        double consumptionRate = 0.1 + speedFactor * 0.2 + accelerationFactor * 0.1; // 每秒消耗百分比
        
        this.batteryLevel = Math.max(0, batteryLevel - consumptionRate * deltaTime);
        
        // 电量不足时更新状态
        if (batteryLevel < 20.0 && status != UAVStatus.LOW_BATTERY) {
            this.status = UAVStatus.LOW_BATTERY;
        }
    }
    
    /**
     * 处理状态变化
     */
    private void handleStatusChange(UAVStatus oldStatus, UAVStatus newStatus) {
        // 状态变化时的特殊逻辑
        if (newStatus == UAVStatus.MALFUNCTION || newStatus == UAVStatus.DAMAGED) {
            // 故障或受损时停止移动
            this.velocity = Velocity.zero();
            this.acceleration = Acceleration.zero();
        } else if (newStatus == UAVStatus.LANDING) {
            // 降落时设置向下速度
            this.velocity = new Velocity(0, 0, 2.0);
        } else if (newStatus == UAVStatus.LANDED) {
            // 已降落时停止所有运动
            this.velocity = Velocity.zero();
            this.acceleration = Acceleration.zero();
        }
    }
    
    /**
     * 验证状态一致性
     */
    private void validateState() {
        if (batteryLevel < 0 || batteryLevel > 100) {
            throw new IllegalStateException("电池电量超出有效范围：" + batteryLevel);
        }
        if (position.getZ() < 0) {
            // 地面以下，设置为地面
            this.position = new Position(position.getX(), position.getY(), 0);
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UAV uav = (UAV) obj;
        return Objects.equals(id, uav.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("UAV{id='%s', name='%s', position=%s, status=%s, battery=%.1f%%}",
                           id, name, position, status, batteryLevel);
    }

    public double getRadioFrequency() {
    }
} 