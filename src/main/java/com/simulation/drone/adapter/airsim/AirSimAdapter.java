package com.simulation.drone.adapter.airsim;

import com.simulation.drone.model.message.control.DroneControl;
import com.simulation.drone.model.message.state.DroneState;

import java.util.Map;
import java.util.List;

/**
 * AirSim适配器接口
 * 负责与AirSim进行通信和数据交换
 */
public interface AirSimAdapter {
    
    /**
     * 初始化AirSim连接
     * @param config AirSim配置参数
     * @return 是否连接成功
     */
    boolean initialize(Map<String, Object> config);
    
    /**
     * 获取无人机位置
     * @param droneId 无人机ID
     * @return 位置信息 (x, y, z)
     */
    Map<String, Double> getDronePosition(String droneId);
    
    /**
     * 获取无人机姿态
     * @param droneId 无人机ID
     * @return 姿态信息 (pitch, roll, yaw)
     */
    Map<String, Double> getDroneOrientation(String droneId);
    
    /**
     * 获取无人机速度
     * @param droneId 无人机ID
     * @return 速度信息 (vx, vy, vz)
     */
    Map<String, Double> getDroneVelocity(String droneId);
    
    /**
     * 获取无人机状态
     * @param droneId 无人机ID
     * @return 无人机状态信息
     */
    DroneState getDroneState(String droneId);
    
    /**
     * 获取所有无人机信息
     * @return 无人机信息列表
     */
    List<Map<String, Object>> getAllDrones();
    
    /**
     * 获取环境信息
     * @return 环境信息
     */
    Map<String, Object> getEnvironmentInfo();
    
    /**
     * 获取图像数据
     * @param cameraId 相机ID
     * @return 图像数据
     */
    byte[] getImageData(String cameraId);
    
    /**
     * 获取传感器数据
     * @param sensorId 传感器ID
     * @return 传感器数据
     */
    Map<String, Object> getSensorData(String sensorId);
    
    /**
     * 控制无人机
     * @param droneId 无人机ID
     * @param control 控制命令
     * @return 是否执行成功
     */
    boolean controlDrone(String droneId, DroneControl control);
    
    /**
     * 设置仿真参数
     * @param params 参数配置
     * @return 是否设置成功
     */
    boolean setSimulationParams(Map<String, Object> params);
    
    /**
     * 获取仿真状态
     * @return 仿真状态信息
     */
    Map<String, Object> getSimulationState();
    
    /**
     * 关闭AirSim连接
     */
    void close();
} 