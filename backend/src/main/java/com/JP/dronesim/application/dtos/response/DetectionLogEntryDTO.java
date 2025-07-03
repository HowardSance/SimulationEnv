package com.JP.dronesim.application.dtos.response;

import com.JP.dronesim.domain.common.valueobjects.Position;

import java.time.LocalDateTime;

/**
 * 探测日志条目DTO
 * 用于探测日志条目响应
 *
 * @author JP Team
 * @version 1.0
 */
public class DetectionLogEntryDTO {

    /**
     * 探测事件ID
     */
    private String eventId;

    /**
     * 探测设备ID
     */
    private String deviceId;

    /**
     * 探测设备类型
     */
    private String deviceType;

    /**
     * 被探测目标ID
     */
    private String targetId;

    /**
     * 被探测目标类�?
     */
    private String targetType;

    /**
     * 探测时间�?
     */
    private LocalDateTime timestamp;

    /**
     * 探测位置
     */
    private Position detectionPosition;

    /**
     * 目标位置
     */
    private Position targetPosition;

    /**
     * 探测距离（米�?
     */
    private Double distance;

    /**
     * 探测置信度（0-1�?
     */
    private Double confidence;

    /**
     * 探测信号强度
     */
    private Double signalStrength;

    /**
     * 探测质量
     */
    private String quality;

    /**
     * 探测状�?
     */
    private String status;

    /**
     * 环境因素影响
     */
    private String environmentalFactors;

    /**
     * 探测参数
     */
    private String detectionParameters;

    /**
     * 备注信息
     */
    private String remarks;

    /**
     * 默认构造函�?
     */
    public DetectionLogEntryDTO() {
    }

    /**
     * 构造函�?
     *
     * @param eventId 探测事件ID
     * @param deviceId 探测设备ID
     * @param targetId 被探测目标ID
     * @param timestamp 探测时间�?
     * @param distance 探测距离
     * @param confidence 探测置信�?
     */
    public DetectionLogEntryDTO(String eventId, String deviceId, String targetId,
                               LocalDateTime timestamp, Double distance, Double confidence) {
        this.eventId = eventId;
        this.deviceId = deviceId;
        this.targetId = targetId;
        this.timestamp = timestamp;
        this.distance = distance;
        this.confidence = confidence;
    }

    /**
     * 获取探测事件ID
     *
     * @return 探测事件ID
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * 设置探测事件ID
     *
     * @param eventId 探测事件ID
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * 获取探测设备ID
     *
     * @return 探测设备ID
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * 设置探测设备ID
     *
     * @param deviceId 探测设备ID
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * 获取探测设备类型
     *
     * @return 探测设备类型
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * 设置探测设备类型
     *
     * @param deviceType 探测设备类型
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * 获取被探测目标ID
     *
     * @return 被探测目标ID
     */
    public String getTargetId() {
        return targetId;
    }

    /**
     * 设置被探测目标ID
     *
     * @param targetId 被探测目标ID
     */
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    /**
     * 获取被探测目标类�?
     *
     * @return 被探测目标类�?
     */
    public String getTargetType() {
        return targetType;
    }

    /**
     * 设置被探测目标类�?
     *
     * @param targetType 被探测目标类�?
     */
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    /**
     * 获取探测时间�?
     *
     * @return 探测时间�?
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * 设置探测时间�?
     *
     * @param timestamp 探测时间�?
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 获取探测位置
     *
     * @return 探测位置
     */
    public Position getDetectionPosition() {
        return detectionPosition;
    }

    /**
     * 设置探测位置
     *
     * @param detectionPosition 探测位置
     */
    public void setDetectionPosition(Position detectionPosition) {
        this.detectionPosition = detectionPosition;
    }

    /**
     * 获取目标位置
     *
     * @return 目标位置
     */
    public Position getTargetPosition() {
        return targetPosition;
    }

    /**
     * 设置目标位置
     *
     * @param targetPosition 目标位置
     */
    public void setTargetPosition(Position targetPosition) {
        this.targetPosition = targetPosition;
    }

    /**
     * 获取探测距离
     *
     * @return 探测距离
     */
    public Double getDistance() {
        return distance;
    }

    /**
     * 设置探测距离
     *
     * @param distance 探测距离
     */
    public void setDistance(Double distance) {
        this.distance = distance;
    }

    /**
     * 获取探测置信�?
     *
     * @return 探测置信�?
     */
    public Double getConfidence() {
        return confidence;
    }

    /**
     * 设置探测置信�?
     *
     * @param confidence 探测置信�?
     */
    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    /**
     * 获取探测信号强度
     *
     * @return 探测信号强度
     */
    public Double getSignalStrength() {
        return signalStrength;
    }

    /**
     * 设置探测信号强度
     *
     * @param signalStrength 探测信号强度
     */
    public void setSignalStrength(Double signalStrength) {
        this.signalStrength = signalStrength;
    }

    /**
     * 获取探测质量
     *
     * @return 探测质量
     */
    public String getQuality() {
        return quality;
    }

    /**
     * 设置探测质量
     *
     * @param quality 探测质量
     */
    public void setQuality(String quality) {
        this.quality = quality;
    }

    /**
     * 获取探测状�?
     *
     * @return 探测状�?
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置探测状�?
     *
     * @param status 探测状�?
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取环境因素影响
     *
     * @return 环境因素影响
     */
    public String getEnvironmentalFactors() {
        return environmentalFactors;
    }

    /**
     * 设置环境因素影响
     *
     * @param environmentalFactors 环境因素影响
     */
    public void setEnvironmentalFactors(String environmentalFactors) {
        this.environmentalFactors = environmentalFactors;
    }

    /**
     * 获取探测参数
     *
     * @return 探测参数
     */
    public String getDetectionParameters() {
        return detectionParameters;
    }

    /**
     * 设置探测参数
     *
     * @param detectionParameters 探测参数
     */
    public void setDetectionParameters(String detectionParameters) {
        this.detectionParameters = detectionParameters;
    }

    /**
     * 获取备注信息
     *
     * @return 备注信息
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置备注信息
     *
     * @param remarks 备注信息
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "DetectionLogEntryDTO{" +
                "eventId='" + eventId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", targetId='" + targetId + '\'' +
                ", targetType='" + targetType + '\'' +
                ", timestamp=" + timestamp +
                ", detectionPosition=" + detectionPosition +
                ", targetPosition=" + targetPosition +
                ", distance=" + distance +
                ", confidence=" + confidence +
                ", signalStrength=" + signalStrength +
                ", quality='" + quality + '\'' +
                ", status='" + status + '\'' +
                ", environmentalFactors='" + environmentalFactors + '\'' +
                ", detectionParameters='" + detectionParameters + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
