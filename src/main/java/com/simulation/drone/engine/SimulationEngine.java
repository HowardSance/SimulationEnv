package com.simulation.drone.engine;

import com.simulation.drone.entity.DetectionDevice;
import com.simulation.drone.entity.Drone;
import com.simulation.drone.entity.EnvironmentConfig;
import com.simulation.drone.entity.TargetCharacteristic;

import java.util.List;
import java.util.Map;

/**
 * 仿真引擎核心接口
 * 负责管理仿真系统的核心逻辑，包括实体管理、状态更新、碰撞检测等
 */
public interface SimulationEngine {
    
    /**
     * 初始化仿真引擎
     * @param config 环境配置
     */
    void initialize(EnvironmentConfig config);
    
    /**
     * 启动仿真
     */
    void start();
    
    /**
     * 暂停仿真
     */
    void pause();
    
    /**
     * 恢复仿真
     */
    void resume();
    
    /**
     * 停止仿真
     */
    void stop();
    
    /**
     * 获取仿真状态
     * @return 仿真状态信息
     */
    Map<String, Object> getStatus();
    
    /**
     * 设置时间步长
     * @param timeStep 时间步长（毫秒）
     */
    void setTimeStep(long timeStep);
    
    /**
     * 添加无人机
     * @param drone 无人机实体
     */
    void addDrone(Drone drone);
    
    /**
     * 移除无人机
     * @param droneId 无人机ID
     */
    void removeDrone(String droneId);
    
    /**
     * 添加探测设备
     * @param device 探测设备
     */
    void addDevice(DetectionDevice device);
    
    /**
     * 移除探测设备
     * @param deviceId 设备ID
     */
    void removeDevice(String deviceId);
    
    /**
     * 更新设备参数
     * @param deviceId 设备ID
     * @param parameters 参数映射
     */
    void updateDeviceParameters(String deviceId, Map<String, Object> parameters);
    
    /**
     * 获取所有无人机
     * @return 无人机列表
     */
    List<Drone> getAllDrones();
    
    /**
     * 获取所有探测设备
     * @return 探测设备列表
     */
    List<DetectionDevice> getAllDevices();
    
    /**
     * 获取指定设备的探测结果
     * @param deviceId 设备ID
     * @return 探测结果
     */
    Map<String, Object> getDeviceDetectionResults(String deviceId);
    
    /**
     * 更新环境参数
     * @param parameters 环境参数
     */
    void updateEnvironmentParameters(Map<String, Object> parameters);
    
    /**
     * 获取环境状态
     * @return 环境状态信息
     */
    Map<String, Object> getEnvironmentStatus();
    
    /**
     * 设置目标特征
     * @param characteristics 目标特征列表
     */
    void setTargetCharacteristics(List<TargetCharacteristic> characteristics);
    
    /**
     * 创建快照
     * @return 快照ID
     */
    String createSnapshot();
    
    /**
     * 恢复快照
     * @param snapshotId 快照ID
     */
    void restoreSnapshot(String snapshotId);
    
    /**
     * 获取性能指标
     * @return 性能指标信息
     */
    Map<String, Object> getPerformanceMetrics();
} 