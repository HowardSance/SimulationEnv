package com.JP.dronesim.application.dtos.response;

import java.time.LocalDateTime;

/**
 * 仿真状态DTO
 * 用于仿真整体状态响�?
 *
 * @author JP Team
 * @version 1.0
 */
public class SimulationStatusDTO {

    /**
     * 空域ID
     */
    private String airspaceId;

    /**
     * 仿真状�?
     */
    private String status;

    /**
     * 当前仿真时间（秒�?
     */
    private Double currentTime;

    /**
     * 时间步长（秒�?
     */
    private Double timeStep;

    /**
     * 仿真开始时�?
     */
    private LocalDateTime startTime;

    /**
     * 仿真暂停时间
     */
    private LocalDateTime pauseTime;

    /**
     * 仿真恢复时间
     */
    private LocalDateTime resumeTime;

    /**
     * 仿真结束时间
     */
    private LocalDateTime endTime;

    /**
     * 最后步进时�?
     */
    private LocalDateTime lastStepTime;

    /**
     * 仿真速度倍数
     */
    private Double speedMultiplier;

    /**
     * 是否实时模式
     */
    private Boolean realTimeMode;

    /**
     * 实体总数
     */
    private Integer totalEntities;

    /**
     * 设备数量
     */
    private Integer deviceCount;

    /**
     * 无人机数�?
     */
    private Integer uavCount;

    /**
     * 探测事件总数
     */
    private Integer totalDetectionEvents;

    /**
     * 默认构造函�?
     */
    public SimulationStatusDTO() {
    }

    /**
     * 构造函�?
     *
     * @param airspaceId 空域ID
     * @param status 仿真状�?
     * @param currentTime 当前仿真时间
     * @param timeStep 时间步长
     */
    public SimulationStatusDTO(String airspaceId, String status, Double currentTime, Double timeStep) {
        this.airspaceId = airspaceId;
        this.status = status;
        this.currentTime = currentTime;
        this.timeStep = timeStep;
    }

    /**
     * 获取空域ID
     *
     * @return 空域ID
     */
    public String getAirspaceId() {
        return airspaceId;
    }

    /**
     * 设置空域ID
     *
     * @param airspaceId 空域ID
     */
    public void setAirspaceId(String airspaceId) {
        this.airspaceId = airspaceId;
    }

    /**
     * 获取仿真状�?
     *
     * @return 仿真状�?
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置仿真状�?
     *
     * @param status 仿真状�?
     */
    public void setStatus(String status) {
        this.status = status;
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
     * 获取仿真开始时�?
     *
     * @return 仿真开始时�?
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * 设置仿真开始时�?
     *
     * @param startTime 仿真开始时�?
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取仿真暂停时间
     *
     * @return 仿真暂停时间
     */
    public LocalDateTime getPauseTime() {
        return pauseTime;
    }

    /**
     * 设置仿真暂停时间
     *
     * @param pauseTime 仿真暂停时间
     */
    public void setPauseTime(LocalDateTime pauseTime) {
        this.pauseTime = pauseTime;
    }

    /**
     * 获取仿真恢复时间
     *
     * @return 仿真恢复时间
     */
    public LocalDateTime getResumeTime() {
        return resumeTime;
    }

    /**
     * 设置仿真恢复时间
     *
     * @param resumeTime 仿真恢复时间
     */
    public void setResumeTime(LocalDateTime resumeTime) {
        this.resumeTime = resumeTime;
    }

    /**
     * 获取仿真结束时间
     *
     * @return 仿真结束时间
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * 设置仿真结束时间
     *
     * @param endTime 仿真结束时间
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取最后步进时�?
     *
     * @return 最后步进时�?
     */
    public LocalDateTime getLastStepTime() {
        return lastStepTime;
    }

    /**
     * 设置最后步进时�?
     *
     * @param lastStepTime 最后步进时�?
     */
    public void setLastStepTime(LocalDateTime lastStepTime) {
        this.lastStepTime = lastStepTime;
    }

    /**
     * 获取仿真速度倍数
     *
     * @return 仿真速度倍数
     */
    public Double getSpeedMultiplier() {
        return speedMultiplier;
    }

    /**
     * 设置仿真速度倍数
     *
     * @param speedMultiplier 仿真速度倍数
     */
    public void setSpeedMultiplier(Double speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    /**
     * 获取是否实时模式
     *
     * @return 是否实时模式
     */
    public Boolean getRealTimeMode() {
        return realTimeMode;
    }

    /**
     * 设置是否实时模式
     *
     * @param realTimeMode 是否实时模式
     */
    public void setRealTimeMode(Boolean realTimeMode) {
        this.realTimeMode = realTimeMode;
    }

    /**
     * 获取实体总数
     *
     * @return 实体总数
     */
    public Integer getTotalEntities() {
        return totalEntities;
    }

    /**
     * 设置实体总数
     *
     * @param totalEntities 实体总数
     */
    public void setTotalEntities(Integer totalEntities) {
        this.totalEntities = totalEntities;
    }

    /**
     * 获取设备数量
     *
     * @return 设备数量
     */
    public Integer getDeviceCount() {
        return deviceCount;
    }

    /**
     * 设置设备数量
     *
     * @param deviceCount 设备数量
     */
    public void setDeviceCount(Integer deviceCount) {
        this.deviceCount = deviceCount;
    }

    /**
     * 获取无人机数�?
     *
     * @return 无人机数�?
     */
    public Integer getUavCount() {
        return uavCount;
    }

    /**
     * 设置无人机数�?
     *
     * @param uavCount 无人机数�?
     */
    public void setUavCount(Integer uavCount) {
        this.uavCount = uavCount;
    }

    /**
     * 获取探测事件总数
     *
     * @return 探测事件总数
     */
    public Integer getTotalDetectionEvents() {
        return totalDetectionEvents;
    }

    /**
     * 设置探测事件总数
     *
     * @param totalDetectionEvents 探测事件总数
     */
    public void setTotalDetectionEvents(Integer totalDetectionEvents) {
        this.totalDetectionEvents = totalDetectionEvents;
    }

    @Override
    public String toString() {
        return "SimulationStatusDTO{" +
                "airspaceId='" + airspaceId + '\'' +
                ", status='" + status + '\'' +
                ", currentTime=" + currentTime +
                ", timeStep=" + timeStep +
                ", startTime=" + startTime +
                ", pauseTime=" + pauseTime +
                ", resumeTime=" + resumeTime +
                ", endTime=" + endTime +
                ", lastStepTime=" + lastStepTime +
                ", speedMultiplier=" + speedMultiplier +
                ", realTimeMode=" + realTimeMode +
                ", totalEntities=" + totalEntities +
                ", deviceCount=" + deviceCount +
                ", uavCount=" + uavCount +
                ", totalDetectionEvents=" + totalDetectionEvents +
                '}';
    }
}
