package com.JP.dronesim.application.dtos.request;

import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.common.valueobjects.Velocity;
import com.JP.dronesim.domain.common.valueobjects.Orientation;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 无人机状态DTO
 * 用于初始化无人机状态
 *
 * @author JP Team
 * @version 1.0
 */
public class UAVStateDTO {

    /**
     * 无人机ID
     */
    private String id;

    /**
     * 无人机名称
     */
    private String name;

    /**
     * 无人机位置
     */
    @NotNull(message = "无人机位置不能为空")
    private Position position;

    /**
     * 无人机速度
     */
    @NotNull(message = "无人机速度不能为空")
    private Velocity velocity;

    /**
     * 无人机姿态
     */
    @NotNull(message = "无人机姿态不能为空")
    private Orientation orientation;

    /**
     * 无人机状态
     */
    private String status;

    /**
     * 飞行路径
     */
    private List<Object> waypoints;

    /**
     * 最大速度（米/秒）
     */
    private Double maxSpeed;

    /**
     * 最大加速度（米/秒²）
     */
    private Double maxAcceleration;

    /**
     * 最大高度（米）
     */
    private Double maxAltitude;

    /**
     * 电池电量（百分比）
     */
    private Double batteryLevel;

    /**
     * 默认构造函数
     */
    public UAVStateDTO() {
    }

    /**
     * 构造函数
     *
     * @param id 无人机ID
     * @param name 无人机名称
     * @param position 无人机位置
     * @param velocity 无人机速度
     * @param orientation 无人机姿态
     */
    public UAVStateDTO(String id, String name, Position position, Velocity velocity, Orientation orientation) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.velocity = velocity;
        this.orientation = orientation;
    }

    /**
     * 获取无人机ID
     *
     * @return 无人机ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置无人机ID
     *
     * @param id 无人机ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取无人机名称
     *
     * @return 无人机名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置无人机名称
     *
     * @param name 无人机名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取无人机位置
     *
     * @return 无人机位置
     */
    public Position getPosition() {
        return position;
    }

    /**
     * 设置无人机位置
     *
     * @param position 无人机位置
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * 获取无人机速度
     *
     * @return 无人机速度
     */
    public Velocity getVelocity() {
        return velocity;
    }

    /**
     * 设置无人机速度
     *
     * @param velocity 无人机速度
     */
    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }

    /**
     * 获取无人机姿态
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * 设置无人机姿态
     *
     * @param orientation 无人机姿态
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * 获取无人机状态
     *
     * @return 无人机状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置无人机状态
     *
     * @param status 无人机状态
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取飞行路径
     *
     * @return 飞行路径
     */
    public List<Object> getWaypoints() {
        return waypoints;
    }

    /**
     * 设置飞行路径
     *
     * @param waypoints 飞行路径
     */
    public void setWaypoints(List<Object> waypoints) {
        this.waypoints = waypoints;
    }

    /**
     * 获取最大速度
     *
     * @return 最大速度
     */
    public Double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * 设置最大速度
     *
     * @param maxSpeed 最大速度
     */
    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * 获取最大加速度
     *
     * @return 最大加速度
     */
    public Double getMaxAcceleration() {
        return maxAcceleration;
    }

    /**
     * 设置最大加速度
     *
     * @param maxAcceleration 最大加速度
     */
    public void setMaxAcceleration(Double maxAcceleration) {
        this.maxAcceleration = maxAcceleration;
    }

    /**
     * 获取最大高度
     *
     * @return 最大高度
     */
    public Double getMaxAltitude() {
        return maxAltitude;
    }

    /**
     * 设置最大高度
     *
     * @param maxAltitude 最大高度
     */
    public void setMaxAltitude(Double maxAltitude) {
        this.maxAltitude = maxAltitude;
    }

    /**
     * 获取电池电量
     *
     * @return 电池电量
     */
    public Double getBatteryLevel() {
        return batteryLevel;
    }

    /**
     * 设置电池电量
     *
     * @param batteryLevel 电池电量
     */
    public void setBatteryLevel(Double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    @Override
    public String toString() {
        return "UAVStateDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", position=" + position +
                ", velocity=" + velocity +
                ", orientation=" + orientation +
                ", status='" + status + '\'' +
                ", waypoints=" + waypoints +
                ", maxSpeed=" + maxSpeed +
                ", maxAcceleration=" + maxAcceleration +
                ", maxAltitude=" + maxAltitude +
                ", batteryLevel=" + batteryLevel +
                '}';
    }
}
