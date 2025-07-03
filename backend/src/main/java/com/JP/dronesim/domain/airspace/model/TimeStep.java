package com.JP.dronesim.domain.airspace.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 时间步长值对象
 * 封装仿真时间步长和当前仿真时间，提供时间推进控制
 * 
 * @author JP Team
 * @version 1.0
 */
public class TimeStep {
    
    /**
     * 时间步长（秒）
     */
    private final double stepSize;
    
    /**
     * 当前仿真时间
     */
    private final LocalDateTime currentSimulationTime;
    
    /**
     * 仿真开始时间
     */
    private final LocalDateTime simulationStartTime;
    
    /**
     * 已运行的仿真时长（秒）
     */
    private final double elapsedTime;
    
    /**
     * 默认时间步长（秒）
     */
    public static final double DEFAULT_STEP_SIZE = 0.1;
    
    /**
     * 最小时间步长（秒）
     */
    public static final double MIN_STEP_SIZE = 0.001;
    
    /**
     * 最大时间步长（秒）
     */
    public static final double MAX_STEP_SIZE = 1.0;
    
    /**
     * 构造函数
     * 
     * @param stepSize 时间步长（秒）
     * @param currentSimulationTime 当前仿真时间
     * @param simulationStartTime 仿真开始时间
     * @param elapsedTime 已运行时长（秒）
     */
    public TimeStep(double stepSize, LocalDateTime currentSimulationTime, 
                   LocalDateTime simulationStartTime, double elapsedTime) {
        validateStepSize(stepSize);
        if (currentSimulationTime == null) {
            throw new IllegalArgumentException("当前仿真时间不能为空");
        }
        if (simulationStartTime == null) {
            throw new IllegalArgumentException("仿真开始时间不能为空");
        }
        if (elapsedTime < 0) {
            throw new IllegalArgumentException("已运行时长不能为负数");
        }
        
        this.stepSize = stepSize;
        this.currentSimulationTime = currentSimulationTime;
        this.simulationStartTime = simulationStartTime;
        this.elapsedTime = elapsedTime;
    }
    
    /**
     * 创建默认时间步长
     * 
     * @return 默认时间步长对象
     */
    public static TimeStep createDefault() {
        LocalDateTime now = LocalDateTime.now();
        return new TimeStep(DEFAULT_STEP_SIZE, now, now, 0.0);
    }
    
    /**
     * 创建自定义时间步长
     * 
     * @param stepSize 时间步长（秒）
     * @return 时间步长对象
     */
    public static TimeStep create(double stepSize) {
        LocalDateTime now = LocalDateTime.now();
        return new TimeStep(stepSize, now, now, 0.0);
    }
    
    /**
     * 推进到下一个时间步
     * 
     * @return 新的时间步长对象
     */
    public TimeStep advance() {
        LocalDateTime nextTime = currentSimulationTime.plusNanos((long)(stepSize * 1_000_000_000));
        double nextElapsedTime = elapsedTime + stepSize;
        return new TimeStep(stepSize, nextTime, simulationStartTime, nextElapsedTime);
    }
    
    /**
     * 推进指定的时间
     * 
     * @param deltaTime 要推进的时间（秒）
     * @return 新的时间步长对象
     */
    public TimeStep advance(double deltaTime) {
        if (deltaTime < 0) {
            throw new IllegalArgumentException("推进时间不能为负数");
        }
        LocalDateTime nextTime = currentSimulationTime.plusNanos((long)(deltaTime * 1_000_000_000));
        double nextElapsedTime = elapsedTime + deltaTime;
        return new TimeStep(stepSize, nextTime, simulationStartTime, nextElapsedTime);
    }
    
    /**
     * 更改时间步长
     * 
     * @param newStepSize 新的时间步长（秒）
     * @return 新的时间步长对象
     */
    public TimeStep withStepSize(double newStepSize) {
        return new TimeStep(newStepSize, currentSimulationTime, simulationStartTime, elapsedTime);
    }
    
    /**
     * 重置仿真时间
     * 
     * @return 重置后的时间步长对象
     */
    public TimeStep reset() {
        LocalDateTime now = LocalDateTime.now();
        return new TimeStep(stepSize, now, now, 0.0);
    }
    
    /**
     * 验证时间步长参数
     * 
     * @param stepSize 时间步长
     */
    private void validateStepSize(double stepSize) {
        if (stepSize < MIN_STEP_SIZE || stepSize > MAX_STEP_SIZE) {
            throw new IllegalArgumentException(
                String.format("时间步长必须在 %.3f 到 %.1f 秒之间", MIN_STEP_SIZE, MAX_STEP_SIZE));
        }
    }
    
    /**
     * 获取时间步长
     * 
     * @return 时间步长（秒）
     */
    public double getStepSize() {
        return stepSize;
    }
    
    /**
     * 获取当前仿真时间
     * 
     * @return 当前仿真时间
     */
    public LocalDateTime getCurrentSimulationTime() {
        return currentSimulationTime;
    }
    
    /**
     * 获取仿真开始时间
     * 
     * @return 仿真开始时间
     */
    public LocalDateTime getSimulationStartTime() {
        return simulationStartTime;
    }
    
    /**
     * 获取已运行时长
     * 
     * @return 已运行时长（秒）
     */
    public double getElapsedTime() {
        return elapsedTime;
    }
    
    /**
     * 计算到下一个时间步的剩余时间
     * 
     * @return 剩余时间（秒）
     */
    public double getTimeToNextStep() {
        double fractionalPart = elapsedTime % stepSize;
        return stepSize - fractionalPart;
    }
    
    /**
     * 判断是否应该执行时间步
     * 
     * @param realElapsedTime 实际经过的时间（秒）
     * @return 是否应该执行时间步
     */
    public boolean shouldStep(double realElapsedTime) {
        return realElapsedTime >= stepSize;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeStep timeStep = (TimeStep) o;
        return Double.compare(timeStep.stepSize, stepSize) == 0 &&
               Double.compare(timeStep.elapsedTime, elapsedTime) == 0 &&
               Objects.equals(currentSimulationTime, timeStep.currentSimulationTime) &&
               Objects.equals(simulationStartTime, timeStep.simulationStartTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(stepSize, currentSimulationTime, simulationStartTime, elapsedTime);
    }
    
    @Override
    public String toString() {
        return String.format("TimeStep{stepSize=%.3fs, elapsedTime=%.3fs, currentTime=%s}", 
                           stepSize, elapsedTime, currentSimulationTime);
    }
} 