package com.JP.dronesim.application.dtos.response;

import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.common.valueobjects.Velocity;
import com.JP.dronesim.domain.common.valueobjects.Orientation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 实体状态DTO
 * 用于实体（设�?无人机）的当前状态响应，用于实时推送和查询
 *
 * @author JP Team
 * @version 1.0
 */
public class EntityStateDTO {

    /**
     * 实体ID
     */
    private String id;

    /**
     * 实体类型
     */
    private String type;

    /**
     * 实体名称
     */
    private String name;

    /**
     * 实体位置
     */
    private Position position;

    /**
     * 实体速度
     */
    private Velocity velocity;

    /**
     * 实体姿�?
     */
    private Orientation orientation;

    /**
     * 实体状�?
     */
    private String status;

    /**
     * 实体属�?
     */
    private Map<String, Object> properties;

    /**
     * 最后更新时�?
     */
    private LocalDateTime lastUpdateTime;

    /**
     * 是否活跃
     */
    private Boolean active;

    /**
     * 健康状�?
     */
    private String healthStatus;

    /**
     * 默认构造函�?
     */
    public EntityStateDTO() {
    }

    /**
     * 构造函�?
     *
     * @param id 实体ID
     * @param type 实体类型
     * @param name 实体名称
     * @param position 实体位置
     */
    public EntityStateDTO(String id, String type, String name, Position position) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.position = position;
    }

    /**
     * 构造函�?
     *
     * @param id 实体ID
     * @param type 实体类型
     * @param name 实体名称
     * @param position 实体位置
     * @param velocity 实体速度
     * @param orientation 实体姿�?
     * @param status 实体状�?
     */
    public EntityStateDTO(String id, String type, String name, Position position,
                         Velocity velocity, Orientation orientation, String status) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.position = position;
        this.velocity = velocity;
        this.orientation = orientation;
        this.status = status;
    }

    /**
     * 获取实体ID
     *
     * @return 实体ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置实体ID
     *
     * @param id 实体ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取实体类型
     *
     * @return 实体类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置实体类型
     *
     * @param type 实体类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取实体名称
     *
     * @return 实体名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置实体名称
     *
     * @param name 实体名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取实体位置
     *
     * @return 实体位置
     */
    public Position getPosition() {
        return position;
    }

    /**
     * 设置实体位置
     *
     * @param position 实体位置
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * 获取实体速度
     *
     * @return 实体速度
     */
    public Velocity getVelocity() {
        return velocity;
    }

    /**
     * 设置实体速度
     *
     * @param velocity 实体速度
     */
    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }

    /**
     * 获取实体姿�?
     *
     * @return 实体姿�?
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * 设置实体姿�?
     *
     * @param orientation 实体姿�?
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * 获取实体状�?
     *
     * @return 实体状�?
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置实体状�?
     *
     * @param status 实体状�?
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取实体属�?
     *
     * @return 实体属�?
     */
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * 设置实体属�?
     *
     * @param properties 实体属�?
     */
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    /**
     * 获取最后更新时�?
     *
     * @return 最后更新时�?
     */
    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * 设置最后更新时�?
     *
     * @param lastUpdateTime 最后更新时�?
     */
    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * 获取是否活跃
     *
     * @return 是否活跃
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * 设置是否活跃
     *
     * @param active 是否活跃
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * 获取健康状�?
     *
     * @return 健康状�?
     */
    public String getHealthStatus() {
        return healthStatus;
    }

    /**
     * 设置健康状�?
     *
     * @param healthStatus 健康状�?
     */
    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    @Override
    public String toString() {
        return "EntityStateDTO{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", position=" + position +
                ", velocity=" + velocity +
                ", orientation=" + orientation +
                ", status='" + status + '\'' +
                ", properties=" + properties +
                ", lastUpdateTime=" + lastUpdateTime +
                ", active=" + active +
                ", healthStatus='" + healthStatus + '\'' +
                '}';
    }
}
