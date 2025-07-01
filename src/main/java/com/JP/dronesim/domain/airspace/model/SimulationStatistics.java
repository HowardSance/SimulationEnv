package com.JP.dronesim.domain.airspace.model;

import java.time.LocalDateTime;

/**
 * 仿真统计信息值对象
 * 记录仿真过程中的各种统计数据
 *
 * @author JP Team
 * @version 1.0
 */
public class SimulationStatistics {

    /**
     * 当前仿真时间
     */
    private LocalDateTime currentTime;

    /**
     * UAV总数
     */
    private int totalUAVCount;

    /**
     * 活跃UAV数量
     */
    private int activeUAVCount;

    /**
     * 探测设备总数
     */
    private int totalProbeDeviceCount;

    /**
     * 活跃探测设备数量
     */
    private int activeProbeDeviceCount;

    /**
     * 探测事件总数
     */
    private long totalDetectionEvents;

    /**
     * 仿真步数
     */
    private long simulationSteps;

    /**
     * 仿真开始时间
     */
    private LocalDateTime startTime;

    /**
     * 默认构造函数
     */
    public SimulationStatistics() {
        this.currentTime = LocalDateTime.now();
        this.totalUAVCount = 0;
        this.activeUAVCount = 0;
        this.totalProbeDeviceCount = 0;
        this.activeProbeDeviceCount = 0;
        this.totalDetectionEvents = 0;
        this.simulationSteps = 0;
        this.startTime = LocalDateTime.now();
    }

    /**
     * 增加UAV数量
     */
    public void incrementUAVCount() {
        this.totalUAVCount++;
    }

    /**
     * 减少UAV数量
     */
    public void decrementUAVCount() {
        if (this.totalUAVCount > 0) {
            this.totalUAVCount--;
        }
    }

    /**
     * 增加探测设备数量
     */
    public void incrementProbeDeviceCount() {
        this.totalProbeDeviceCount++;
    }

    /**
     * 减少探测设备数量
     */
    public void decrementProbeDeviceCount() {
        if (this.totalProbeDeviceCount > 0) {
            this.totalProbeDeviceCount--;
        }
    }

    /**
     * 添加探测事件数量
     *
     * @param count 事件数量
     */
    public void addDetectionEvents(int count) {
        this.totalDetectionEvents += count;
    }

    /**
     * 增加仿真步数
     */
    public void incrementSimulationSteps() {
        this.simulationSteps++;
    }

    /**
     * 重置统计信息
     */
    public void reset() {
        this.totalUAVCount = 0;
        this.activeUAVCount = 0;
        this.totalProbeDeviceCount = 0;
        this.activeProbeDeviceCount = 0;
        this.totalDetectionEvents = 0;
        this.simulationSteps = 0;
        this.startTime = LocalDateTime.now();
    }

    /**
     * 获取仿真运行时长（毫秒）
     *
     * @return 运行时长
     */
    public long getRunDurationMillis() {
        if (startTime == null) {
            return 0;
        }
        return java.time.Duration.between(startTime, LocalDateTime.now()).toMillis();
    }

    /**
     * 获取平均每步探测事件数
     *
     * @return 平均探测事件数
     */
    public double getAverageDetectionEventsPerStep() {
        if (simulationSteps == 0) {
            return 0.0;
        }
        return (double) totalDetectionEvents / simulationSteps;
    }

    // Getters and Setters
    public LocalDateTime getCurrentTime() { return currentTime; }
    public void setCurrentTime(LocalDateTime currentTime) { this.currentTime = currentTime; }

    public int getTotalUAVCount() { return totalUAVCount; }
    public void setTotalUAVCount(int totalUAVCount) { this.totalUAVCount = totalUAVCount; }

    public int getActiveUAVCount() { return activeUAVCount; }
    public void setActiveUAVCount(int activeUAVCount) { this.activeUAVCount = activeUAVCount; }

    public int getTotalProbeDeviceCount() { return totalProbeDeviceCount; }
    public void setTotalProbeDeviceCount(int totalProbeDeviceCount) { this.totalProbeDeviceCount = totalProbeDeviceCount; }

    public int getActiveProbeDeviceCount() { return activeProbeDeviceCount; }
    public void setActiveProbeDeviceCount(int activeProbeDeviceCount) { this.activeProbeDeviceCount = activeProbeDeviceCount; }

    public long getTotalDetectionEvents() { return totalDetectionEvents; }
    public void setTotalDetectionEvents(long totalDetectionEvents) { this.totalDetectionEvents = totalDetectionEvents; }

    public long getSimulationSteps() { return simulationSteps; }
    public void setSimulationSteps(long simulationSteps) { this.simulationSteps = simulationSteps; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    @Override
    public String toString() {
        return String.format(
            "SimulationStatistics{time=%s, uavs=%d/%d, devices=%d/%d, events=%d, steps=%d}",
            currentTime, activeUAVCount, totalUAVCount, activeProbeDeviceCount, 
            totalProbeDeviceCount, totalDetectionEvents, simulationSteps
        );
    }
}