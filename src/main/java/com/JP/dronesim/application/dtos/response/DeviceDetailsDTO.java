package com.JP.dronesim.application.dtos.response;

import com.JP.dronesim.domain.common.valueobjects.Position;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 设备详情DTO
 * 用于探测设备详细信息响应
 *
 * @author JP Team
 * @version 1.0
 */
public class DeviceDetailsDTO {

    /**
     * 设备ID
     */
    private String id;

    /**
     * 设备类型
     */
    private String type;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备状态
     */
    private String status;

    /**
     * 设备位置
     */
    private Position position;

    /**
     * 设备参数
     */
    private Map<String, Object> parameters;

    /**
     * 探测范围（米）
     */
    private Double detectionRange;

    /**
     * 探测精度
     */
    private Double accuracy;

    /**
     * 扫描频率（赫兹）
     */
    private Double scanFrequency;

    /**
     * 探测次数
     */
    private Integer detectionCount;

    /**
     * 最后探测时�?
     */
    private LocalDateTime lastDetectionTime;

    /**
     * 设备运行时间（秒�?
     */
    private Long uptime;

    /**
     * 最后维护时�?
     */
    private LocalDateTime lastMaintenance;

    /**
     * 设备健康状�?
     */
    private String healthStatus;

    /**
     * 是否启用
     */
    private Boolean enabled;

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
    public DeviceDetailsDTO() {
    }

    /**
     * 构造函�?
     *
     * @param id 设备ID
     * @param type 设备类型
     * @param name 设备名称
     * @param status 设备状�?
     * @param position 设备位置
     */
    public DeviceDetailsDTO(String id, String type, String name, String status, Position position) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.status = status;
        this.position = position;
    }

    /**
     * 获取设备ID
     *
     * @return 设备ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置设备ID
     *
     * @param id 设备ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取设备类型
     *
     * @return 设备类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置设备类型
     *
     * @param type 设备类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取设备名称
     *
     * @return 设备名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置设备名称
     *
     * @param name 设备名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取设备状�?
     *
     * @return 设备状�?
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置设备状�?
     *
     * @param status 设备状�?
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取设备位置
     *
     * @return 设备位置
     */
    public Position getPosition() {
        return position;
    }

    /**
     * 设置设备位置
     *
     * @param position 设备位置
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * 获取设备参数
     *
     * @return 设备参数
     */
    public Map<String, Object> getParameters() {
        return parameters;
    }

    /**
     * 设置设备参数
     *
     * @param parameters 设备参数
     */
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * 获取探测范围
     *
     * @return 探测范围
     */
    public Double getDetectionRange() {
        return detectionRange;
    }

    /**
     * 设置探测范围
     *
     * @param detectionRange 探测范围
     */
    public void setDetectionRange(Double detectionRange) {
        this.detectionRange = detectionRange;
    }

    /**
     * 获取探测精度
     *
     * @return 探测精度
     */
    public Double getAccuracy() {
        return accuracy;
    }

    /**
     * 设置探测精度
     *
     * @param accuracy 探测精度
     */
    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * 获取扫描频率
     *
     * @return 扫描频率
     */
    public Double getScanFrequency() {
        return scanFrequency;
    }

    /**
     * 设置扫描频率
     *
     * @param scanFrequency 扫描频率
     */
    public void setScanFrequency(Double scanFrequency) {
        this.scanFrequency = scanFrequency;
    }

    /**
     * 获取探测次数
     *
     * @return 探测次数
     */
    public Integer getDetectionCount() {
        return detectionCount;
    }

    /**
     * 设置探测次数
     *
     * @param detectionCount 探测次数
     */
    public void setDetectionCount(Integer detectionCount) {
        this.detectionCount = detectionCount;
    }

    /**
     * 获取最后探测时�?
     *
     * @return 最后探测时�?
     */
    public LocalDateTime getLastDetectionTime() {
        return lastDetectionTime;
    }

    /**
     * 设置最后探测时�?
     *
     * @param lastDetectionTime 最后探测时�?
     */
    public void setLastDetectionTime(LocalDateTime lastDetectionTime) {
        this.lastDetectionTime = lastDetectionTime;
    }

    /**
     * 获取设备运行时间
     *
     * @return 设备运行时间
     */
    public Long getUptime() {
        return uptime;
    }

    /**
     * 设置设备运行时间
     *
     * @param uptime 设备运行时间
     */
    public void setUptime(Long uptime) {
        this.uptime = uptime;
    }

    /**
     * 获取最后维护时�?
     *
     * @return 最后维护时�?
     */
    public LocalDateTime getLastMaintenance() {
        return lastMaintenance;
    }

    /**
     * 设置最后维护时�?
     *
     * @param lastMaintenance 最后维护时�?
     */
    public void setLastMaintenance(LocalDateTime lastMaintenance) {
        this.lastMaintenance = lastMaintenance;
    }

    /**
     * 获取设备健康状�?
     *
     * @return 设备健康状�?
     */
    public String getHealthStatus() {
        return healthStatus;
    }

    /**
     * 设置设备健康状�?
     *
     * @param healthStatus 设备健康状�?
     */
    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    /**
     * 获取是否启用
     *
     * @return 是否启用
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * 设置是否启用
     *
     * @param enabled 是否启用
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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
        return "DeviceDetailsDTO{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", position=" + position +
                ", parameters=" + parameters +
                ", detectionRange=" + detectionRange +
                ", accuracy=" + accuracy +
                ", scanFrequency=" + scanFrequency +
                ", detectionCount=" + detectionCount +
                ", lastDetectionTime=" + lastDetectionTime +
                ", uptime=" + uptime +
                ", lastMaintenance=" + lastMaintenance +
                ", healthStatus='" + healthStatus + '\'' +
                ", enabled=" + enabled +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
