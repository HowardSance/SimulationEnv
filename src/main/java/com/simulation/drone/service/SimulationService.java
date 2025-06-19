package com.simulation.drone.service;

import java.util.Map;

/**
 * 仿真服务接口
 * 提供仿真系统的核心控制功能
 */
public interface SimulationService {
    
    /**
     * 启动仿真
     */
    void startSimulation();
    
    /**
     * 暂停仿真
     */
    void pauseSimulation();
    
    /**
     * 恢复仿真
     */
    void resumeSimulation();
    
    /**
     * 停止仿真
     */
    void stopSimulation();
    
    /**
     * 获取仿真状态
     * @return 包含仿真状态信息的Map
     */
    Map<String, Object> getSimulationStatus();
    
    /**
     * 设置仿真时间步长
     * @param timeStep 时间步长(毫秒)
     */
    void setTimeStep(long timeStep);
    
    /**
     * 创建仿真快照
     * @return 快照ID
     */
    String createSnapshot();
    
    /**
     * 恢复仿真快照
     * @param snapshotId 快照ID
     */
    void restoreSnapshot(String snapshotId);
    
    /**
     * 获取仿真性能指标
     * @return 包含性能指标的Map
     */
    Map<String, Object> getSimulationMetrics();
} 