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
     * 无人机当前状态（包含运动状态和物理特性）
     */
    private UAVState currentState;

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
     * 无线电频率（MHz）
     */
    private double radioFrequency;
    
    /**
     * 构造函数
     * 
     * @param name 无人机名称
     * @param initialState 初始状态（包含位置和物理特性）
     */
    public UAV(String name, UAVState initialState) {
        // 参数验证
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("无人机名称不能为空");
        }
        if (initialState == null) {
            throw new IllegalArgumentException("初始状态不能为空");
        }
        
        this.id = UUID.randomUUID().toString();
        this.name = name.trim();
        this.currentState = initialState;
        this.flightPath = new ArrayList<>();
        this.currentWaypointIndex = 0;
        this.status = UAVStatus.STANDBY;
        this.createdAt = LocalDateTime.now();
        this.lastUpdatedAt = LocalDateTime.now();
        this.maxSpeed = 20.0; // 默认最大速度20 m/s
        this.maxAcceleration = 5.0; // 默认最大加速度5 m/s²
        this.radioFrequency = 2400.0; // 默认频率2.4GHz
    }
    
    /**
     * 兼容性构造函数 - 使用位置创建标准小型无人机
     * 
     * @param name 无人机名称
     * @param initialPosition 初始位置
     */
    public UAV(String name, Position initialPosition) {
        this(name, UAVState.createStandardSmallDrone(
            initialPosition, 
            Orientation.identity(), 
            Velocity.zero(), 
            Acceleration.zero()
        ));
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
        this.currentState = initialState;
        
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
        double distance = currentState.getPosition().distanceTo(targetLocation);
        if (distance < 0.1) { // 已经很接近目标位置
            return;
        }
        
        // 计算方向向量
        double dx = targetLocation.getX() - currentState.getPosition().getX();
        double dy = targetLocation.getY() - currentState.getPosition().getY();
        double dz = targetLocation.getZ() - currentState.getPosition().getZ();
        
        // 归一化方向向量
        double norm = Math.sqrt(dx * dx + dy * dy + dz * dz);
        dx /= norm;
        dy /= norm;
        dz /= norm;

        // 计算目标速度（考虑最大速度限制）
        double targetSpeed = Math.min(maxSpeed, distance * 2.0); // 距离越近速度越慢

        // 设置新的速度
        Velocity newVelocity = new Velocity(dx * targetSpeed, dy * targetSpeed, dz * targetSpeed);

        // 计算新朝向
        double yaw = Math.atan2(dy, dx);
        Orientation newOrientation = Orientation.fromEulerAngles(0, 0, yaw);

        // 更新状态
        this.currentState = currentState.updateMotionState(
            currentState.getPosition(),
            newOrientation,
            newVelocity,
            currentState.getAcceleration()
        );

        // 更新运行状态
        this.status = UAVStatus.MOVING;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    /**
     * 根据时间步长更新无人机位置和状态
     *
     * @param deltaTime 时间步长（秒）
     */
    public void updateState(double deltaTime) {
        if (deltaTime <= 0) {
            throw new IllegalArgumentException("时间步长必须大于零");
        }

        // 更新位置
        Velocity velocity = currentState.getVelocity();
        Position position = currentState.getPosition();
        double newX = position.getX() + velocity.getVx() * deltaTime;
        double newY = position.getY() + velocity.getVy() * deltaTime;
        double newZ = position.getZ() + velocity.getVz() * deltaTime;
        Position newPosition = new Position(newX, newY, newZ);

        // 更新速度（考虑加速度）
        Velocity newVelocity = velocity;
        Acceleration acceleration = currentState.getAcceleration();
        if (!acceleration.isZero()) {
            double newVx = velocity.getVx() + acceleration.getAx() * deltaTime;
            double newVy = velocity.getVy() + acceleration.getAy() * deltaTime;
            double newVz = velocity.getVz() + acceleration.getAz() * deltaTime;

            // 限制最大速度
            double speed = Math.sqrt(newVx * newVx + newVy * newVy + newVz * newVz);
            if (speed > maxSpeed) {
                double scale = maxSpeed / speed;
                newVx *= scale;
                newVy *= scale;
                newVz *= scale;
            }

            newVelocity = new Velocity(newVx, newVy, newVz);
        }

        // 更新状态
        this.currentState = currentState.updateMotionState(
            newPosition,
            currentState.getOrientation(),
            newVelocity,
            acceleration
        );

        // 检查是否到达航点
        checkWaypointReached();

        // 更新时间戳
        this.lastUpdatedAt = LocalDateTime.now();
    }

    /**
     * 更新无人机状态
     *
     * @param newStatus 新状态
     */
    public void updateStatus(UAVStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("状态不能为空");
        }

        UAVStatus oldStatus = this.status;
        this.status = newStatus;
        this.lastUpdatedAt = LocalDateTime.now();

        // 处理状态变化
        handleStatusChange(oldStatus, newStatus);
    }

    // Getter方法
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return currentState.getPosition();
    }

    public Orientation getOrientation() {
        return currentState.getOrientation();
    }

    public Velocity getVelocity() {
        return currentState.getVelocity();
    }

    public Acceleration getAcceleration() {
        return currentState.getAcceleration();
    }

    public UAVState getCurrentState() {
        return currentState;
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

    public double getRadioFrequency() {
        return radioFrequency;
    }

    /**
     * 获取当前航点
     *
     * @return 当前航点，如果没有航点返回null
     */
    public Waypoint getCurrentWaypoint() {
        if (flightPath.isEmpty() || currentWaypointIndex >= flightPath.size()) {
            return null;
        }
        return flightPath.get(currentWaypointIndex);
    }

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
     * 添加航点
     *
     * @param waypoint 要添加的航点
     */
    public void addWaypoint(Waypoint waypoint) {
        if (waypoint == null) {
            throw new IllegalArgumentException("航点不能为空");
        }

        this.flightPath.add(waypoint);
        this.lastUpdatedAt = LocalDateTime.now();
    }

    /**
     * 开始执行任务
     */
    public void startMission() {
        if (flightPath.isEmpty()) {
            throw new IllegalStateException("没有设置飞行路径");
        }

        this.currentWaypointIndex = 0;
        this.status = UAVStatus.MOVING;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    /**
     * 暂停任务
     */
    public void pauseMission() {
        if (status == UAVStatus.MOVING) {
            this.status = UAVStatus.HOVERING;
            this.currentState = currentState.updateMotionState(
                currentState.getPosition(),
                currentState.getOrientation(),
                Velocity.zero(),
                Acceleration.zero()
            );
            this.lastUpdatedAt = LocalDateTime.now();
        }
    }

    /**
     * 恢复任务
     */
    public void resumeMission() {
        if (status == UAVStatus.HOVERING) {
            this.status = UAVStatus.MOVING;
            this.lastUpdatedAt = LocalDateTime.now();
        }
    }

    /**
     * 返回起始点
     */
    public void returnToHome() {
        if (!flightPath.isEmpty()) {
            Waypoint homeWaypoint = flightPath.get(0);
            move(homeWaypoint.getPosition());
        }
    }

    /**
     * 紧急降落
     */
    public void emergencyLanding() {
        this.status = UAVStatus.LANDING;
        this.currentState = currentState.updateMotionState(
            currentState.getPosition(),
            currentState.getOrientation(),
            new Velocity(0, 0, 5.0), // 垂直下降
            Acceleration.zero()
        );
        this.lastUpdatedAt = LocalDateTime.now();
    }

    /**
     * 检查是否可以移动
     *
     * @return 是否可以移动
     */
    private boolean canMove() {
        return status == UAVStatus.STANDBY || status == UAVStatus.MOVING || status == UAVStatus.HOVERING;
    }

    /**
     * 检查是否到达航点
     */
    private void checkWaypointReached() {
        Waypoint currentWaypoint = getCurrentWaypoint();
        if (currentWaypoint != null) {
            double distance = currentState.getPosition().distanceTo(currentWaypoint.getPosition());
            if (distance < 1.0) { // 1米容差
                currentWaypoint.markAsReached();
                currentWaypointIndex++;

                if (currentWaypointIndex >= flightPath.size()) {
                    // 任务完成
                    this.status = UAVStatus.LANDED;
                    this.currentState = currentState.updateMotionState(
                        currentState.getPosition(),
                        currentState.getOrientation(),
                        Velocity.zero(),
                        Acceleration.zero()
                    );
                }
            }
        }
    }

    /**
     * 处理状态变化
     *
     * @param oldStatus 旧状态
     * @param newStatus 新状态
     */
    private void handleStatusChange(UAVStatus oldStatus, UAVStatus newStatus) {
        switch (newStatus) {
            case STANDBY:
                this.currentState = currentState.updateMotionState(
                    currentState.getPosition(),
                    currentState.getOrientation(),
                    Velocity.zero(),
                    Acceleration.zero()
                );
                break;
            case HOVERING:
                this.currentState = currentState.updateMotionState(
                    currentState.getPosition(),
                    currentState.getOrientation(),
                    Velocity.zero(),
                    Acceleration.zero()
                );
                break;
            case MALFUNCTION:
                // 故障状态下的处理
                this.currentState = currentState.updateMotionState(
                    currentState.getPosition(),
                    currentState.getOrientation(),
                    Velocity.zero(),
                    Acceleration.zero()
                );
                break;
            default:
                // 其他状态的处理
                break;
        }
    }

    /**
     * 验证无人机状态
     */
    private void validateState() {
        if (currentState == null) {
            throw new IllegalStateException("无人机状态不能为空");
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
        return String.format("UAV{id='%s', name='%s', position=%s, status=%s}",
                           id, name, currentState.getPosition(), status);
    }
}