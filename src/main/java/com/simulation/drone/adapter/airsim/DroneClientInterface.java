package com.simulation.drone.adapter.airsim;

import java.util.Map;

/**
 * 无人机客户端接口
 * 参考reference模块的实现，定义与AirSim交互的方法
 */
public interface DroneClientInterface {
    /**
     * 启用API控制
     */
    boolean enableApiControl(boolean enable, String vehicleName);
    
    /**
     * 检查API控制是否启用
     */
    boolean isApiControlEnabled(String vehicleName);
    
    /**
     * 获取无人机状态
     */
    Map<String, Object> getMultirotorState(String vehicleName);
    
    /**
     * 获取距离传感器数据
     */
    Map<String, Object> getDistanceSensorData(String vehicleName, String sensorName);
    
    /**
     * 设置无人机控制
     */
    boolean setMultirotorControls(Map<String, Object> controls, String vehicleName);
    
    /**
     * 获取环境信息
     */
    Map<String, Object> getEnvironmentInfo();
    
    /**
     * 获取图像数据
     */
    byte[] getImageData(String cameraName);
    
    /**
     * 设置仿真参数
     */
    boolean setSimulationParams(Map<String, Object> params);
    
    /**
     * 获取仿真状态
     */
    Map<String, Object> getSimulationState();
} 