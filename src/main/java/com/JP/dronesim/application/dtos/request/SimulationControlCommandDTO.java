package com.JP.dronesim.application.dtos.request;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * 仿真控制指令DTO
 * 用于初始化仿真控制指�?(start/pause/stop)
 *
 * @author JP Team
 * @version 1.0
 */
public class SimulationControlCommandDTO {

    /**
     * 时间步长（秒�?
     */
    @NotNull(message = "时间步长不能为空")
    @DecimalMin(value = "0.1", message = "时间步长必须大于0.1�?)
    private Double timeStep;

    /**
     * 控制指令类型
     */
    private String commandType;

    /**
     * 仿真速度倍数
     */
    @DecimalMin(value = "0.1", message = "仿真速度倍数必须大于0.1")
    private Double speedMultiplier;

    /**
     * 是否实时模式
     */
    private Boolean realTimeMode;

    /**
     * 最大仿真时间（秒）
     */
    @DecimalMin(value = "0", message = "最大仿真时间不能为负数")
    private Double maxSimulationTime;

    /**
     * 默认构造函�?
     */
    public SimulationControlCommandDTO() {
    }

    /**
     * 构造函�?
     *
     * @param timeStep 时间步长
     */
    public SimulationControlCommandDTO(Double timeStep) {
        this.timeStep = timeStep;
    }

    /**
     * 构造函�?
     *
     * @param timeStep 时间步长
     * @param commandType 控制指令类型
     * @param speedMultiplier 仿真速度倍数
     */
    public SimulationControlCommandDTO(Double timeStep, String commandType, Double speedMultiplier) {
        this.timeStep = timeStep;
        this.commandType = commandType;
        this.speedMultiplier = speedMultiplier;
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
     * 获取控制指令类型
     *
     * @return 控制指令类型
     */
    public String getCommandType() {
        return commandType;
    }

    /**
     * 设置控制指令类型
     *
     * @param commandType 控制指令类型
     */
    public void setCommandType(String commandType) {
        this.commandType = commandType;
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
     * 获取最大仿真时�?
     *
     * @return 最大仿真时�?
     */
    public Double getMaxSimulationTime() {
        return maxSimulationTime;
    }

    /**
     * 设置最大仿真时�?
     *
     * @param maxSimulationTime 最大仿真时�?
     */
    public void setMaxSimulationTime(Double maxSimulationTime) {
        this.maxSimulationTime = maxSimulationTime;
    }

    @Override
    public String toString() {
        return "SimulationControlCommandDTO{" +
                "timeStep=" + timeStep +
                ", commandType='" + commandType + '\'' +
                ", speedMultiplier=" + speedMultiplier +
                ", realTimeMode=" + realTimeMode +
                ", maxSimulationTime=" + maxSimulationTime +
                '}';
    }
}
