package com.simulation.drone.infrastructure.adapter.airsim.client;

import com.simulation.drone.domain.port.SensorDataPort;
import java.util.List;
import java.util.Map;

/**
 * 传感器客户端接口
 * 定义与AirSim传感器数据交互的方法
 */
public interface SensorClientInterface {
    
    /**
     * 初始化连接
     * @param host 主机地址
     * @param port 端口号
     */
    void initialize(String host, int port);
    
    /**
     * 关闭连接
     */
    void shutdown();
    
    /**
     * 获取激光雷达数据
     * @param sensorName 传感器名称
     * @param vehicleName 载体名称
     * @return 点云数据
     */
    List<SensorDataPort.Point3D> getLidarData(String sensorName, String vehicleName);
    
    /**
     * 获取IMU数据
     * @param sensorName 传感器名称
     * @param vehicleName 载体名称
     * @return IMU数据映射
     */
    Map<String, Object> getImuData(String sensorName, String vehicleName);
    
    /**
     * 获取距离传感器数据
     * @param sensorName 传感器名称
     * @param vehicleName 载体名称
     * @return 距离数据映射
     */
    Map<String, Object> getDistanceSensorData(String sensorName, String vehicleName);
    
    /**
     * 获取摄像头图像数据
     * @param cameraName 摄像头名称
     * @param vehicleName 载体名称
     * @return 图像数据映射
     */
    Map<String, Object> simGetImage(String cameraName, String vehicleName);
    
    /**
     * 获取摄像头图像数据（指定类型）
     * @param cameraName 摄像头名称
     * @param imageType 图像类型
     * @param vehicleName 载体名称
     * @return 图像数据映射
     */
    Map<String, Object> simGetImage(String cameraName, int imageType, String vehicleName);
    
    /**
     * 获取摄像头信息
     * @param cameraName 摄像头名称
     * @param vehicleName 载体名称
     * @return 摄像头信息
     */
    Map<String, Object> simGetCameraInfo(String cameraName, String vehicleName);
    
    /**
     * 设置摄像头角度
     * @param cameraName 摄像头名称
     * @param pitch 俯仰角
     * @param roll 翻滚角
     * @param yaw 偏航角
     * @param vehicleName 载体名称
     */
    void simSetCameraPose(String cameraName, double pitch, double roll, double yaw, String vehicleName);
    
    /**
     * 获取传感器数据
     * @param sensorName 传感器名称
     * @param sensorType 传感器类型
     * @param vehicleName 载体名称
     * @return 传感器数据
     */
    Map<String, Object> getSensorData(String sensorName, String sensorType, String vehicleName);
} 