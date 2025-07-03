package com.JP.dronesim.application.dtos.response;

import com.JP.dronesim.domain.airspace.model.EnvironmentParameters;
import com.JP.dronesim.domain.common.valueobjects.Position;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 空域详情DTO
 * 用于空域详情响应（包含实体列表）
 *
 * @author JP Team
 * @version 1.0
 */
public class AirspaceDetailsDTO {

    /**
     * 空域ID
     */
    private String id;

    /**
     * 空域名称
     */
    private String name;

    /**
     * 空域描述
     */
    private String description;

    /**
     * 空域边界最小坐�?
     */
    private Position boundaryMin;

    /**
     * 空域边界最大坐�?
     */
    private Position boundaryMax;

    /**
     * 环境参数
     */
    private EnvironmentParameters environmentParameters;

    /**
     * 时间步长（秒�?
     */
    private Double timeStep;

    /**
     * 当前仿真时间（秒�?
     */
    private Double currentTime;

    /**
     * 实体数量
     */
    private Integer entityCount;

    /**
     * 设备列表
     */
    private List<EntityStateDTO> devices;

    /**
     * 无人机列�?
     */
    private List<EntityStateDTO> uavs;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 默认构造函�?
     */
    public AirspaceDetailsDTO() {
    }

    /**
     * 构造函�?
     *
     * @param id 空域ID
     * @param name 空域名称
     * @param description 空域描述
     * @param boundaryMin 边界最小坐�?
     * @param boundaryMax 边界最大坐�?
     */
    public AirspaceDetailsDTO(String id, String name, String description,
                             Position boundaryMin, Position boundaryMax) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.boundaryMin = boundaryMin;
        this.boundaryMax = boundaryMax;
    }

    /**
     * 获取空域ID
     *
     * @return 空域ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置空域ID
     *
     * @param id 空域ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取空域名称
     *
     * @return 空域名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置空域名称
     *
     * @param name 空域名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取空域描述
     *
     * @return 空域描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置空域描述
     *
     * @param description 空域描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取空域边界最小坐�?
     *
     * @return 边界最小坐�?
     */
    public Position getBoundaryMin() {
        return boundaryMin;
    }

    /**
     * 设置空域边界最小坐�?
     *
     * @param boundaryMin 边界最小坐�?
     */
    public void setBoundaryMin(Position boundaryMin) {
        this.boundaryMin = boundaryMin;
    }

    /**
     * 获取空域边界最大坐�?
     *
     * @return 边界最大坐�?
     */
    public Position getBoundaryMax() {
        return boundaryMax;
    }

    /**
     * 设置空域边界最大坐�?
     *
     * @param boundaryMax 边界最大坐�?
     */
    public void setBoundaryMax(Position boundaryMax) {
        this.boundaryMax = boundaryMax;
    }

    /**
     * 获取环境参数
     *
     * @return 环境参数
     */
    public EnvironmentParameters getEnvironmentParameters() {
        return environmentParameters;
    }

    /**
     * 设置环境参数
     *
     * @param environmentParameters 环境参数
     */
    public void setEnvironmentParameters(EnvironmentParameters environmentParameters) {
        this.environmentParameters = environmentParameters;
    }

    /**
     * 获取时间步长
     *
     * @return 时间步长
     */
    public Double getTimeStep() {
        return timeStep;
    }

    /**
     * 设置时间步长
     *
     * @param timeStep 时间步长
     */
    public void setTimeStep(Double timeStep) {
        this.timeStep = timeStep;
    }

    /**
     * 获取当前仿真时间
     *
     * @return 当前仿真时间
     */
    public Double getCurrentTime() {
        return currentTime;
    }

    /**
     * 设置当前仿真时间
     *
     * @param currentTime 当前仿真时间
     */
    public void setCurrentTime(Double currentTime) {
        this.currentTime = currentTime;
    }

    /**
     * 获取实体数量
     *
     * @return 实体数量
     */
    public Integer getEntityCount() {
        return entityCount;
    }

    /**
     * 设置实体数量
     *
     * @param entityCount 实体数量
     */
    public void setEntityCount(Integer entityCount) {
        this.entityCount = entityCount;
    }

    /**
     * 获取设备列表
     *
     * @return 设备列表
     */
    public List<EntityStateDTO> getDevices() {
        return devices;
    }

    /**
     * 设置设备列表
     *
     * @param devices 设备列表
     */
    public void setDevices(List<EntityStateDTO> devices) {
        this.devices = devices;
    }

    /**
     * 获取无人机列�?
     *
     * @return 无人机列�?
     */
    public List<EntityStateDTO> getUavs() {
        return uavs;
    }

    /**
     * 设置无人机列�?
     *
     * @param uavs 无人机列�?
     */
    public void setUavs(List<EntityStateDTO> uavs) {
        this.uavs = uavs;
    }

    /**
     * 获取创建时间
     *
     * @return 创建时间
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 设置创建时间
     *
     * @param createdAt 创建时间
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 获取更新时间
     *
     * @return 更新时间
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 设置更新时间
     *
     * @param updatedAt 更新时间
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "AirspaceDetailsDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", boundaryMin=" + boundaryMin +
                ", boundaryMax=" + boundaryMax +
                ", environmentParameters=" + environmentParameters +
                ", timeStep=" + timeStep +
                ", currentTime=" + currentTime +
                ", entityCount=" + entityCount +
                ", devices=" + devices +
                ", uavs=" + uavs +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
