package com.simulation.drone.adapter.airsim;

import com.simulation.drone.adapter.airsim.api.RpcLibClientBase;
import com.simulation.drone.adapter.airsim.messages.*;

/**
 * Airsim客户端接口
 * 基于AirSim官方API规范，定义与AirSim设备交互的方法
 * 继承RpcLibClientBase获得基础的仿真控制功能
 */
public interface DroneClientInterface extends RpcLibClientBase {
    
    // ======================== 无人机控制相关 ========================
    
    /**
     * 启用/禁用API控制
     * @param isEnabled 是否启用API控制
     * @param vehicleName 载具名称，空字符串表示默认载具
     */
    void enableApiControl(boolean isEnabled, String vehicleName);
    
    /**
     * 检查API控制是否启用
     * @param vehicleName 载具名称
     * @return 是否启用API控制
     */
    boolean isApiControlEnabled(String vehicleName);
    
    /**
     * 启动/关闭无人机
     * @param arm true为启动，false为关闭
     * @param vehicleName 载具名称
     * @return 操作是否成功
     */
    boolean armDisarm(boolean arm, String vehicleName);
    
    /**
     * 起飞到指定高度
     * @param timeout 超时时间(秒)
     * @param vehicleName 载具名称
     * @return 操作是否成功
     */
    boolean takeoffAsync(float timeout, String vehicleName);
    
    /**
     * 降落
     * @param timeout 超时时间(秒)
     * @param vehicleName 载具名称
     * @return 操作是否成功
     */
    boolean landAsync(float timeout, String vehicleName);
    
    /**
     * 回到起始位置
     * @param timeout 超时时间(秒)
     * @param vehicleName 载具名称
     * @return 操作是否成功
     */
    boolean goHomeAsync(float timeout, String vehicleName);
    
    // ======================== 运动控制相关 ========================
    
    /**
     * 移动到指定位置
     * @param x X坐标
     * @param y Y坐标
     * @param z Z坐标(负值表示向上)
     * @param velocity 移动速度
     * @param timeout 超时时间
     * @param drivetrain 驱动类型
     * @param yawMode 偏航模式
     * @param vehicleName 载具名称
     * @return 操作是否成功
     */
    boolean moveToPositionAsync(float x, float y, float z, float velocity, 
                               float timeout, int drivetrain, YawMode yawMode, String vehicleName);
    
    /**
     * 以指定速度移动
     * @param vx X方向速度
     * @param vy Y方向速度
     * @param vz Z方向速度
     * @param duration 持续时间
     * @param drivetrain 驱动类型
     * @param yawMode 偏航模式
     * @param vehicleName 载具名称
     * @return 操作是否成功
     */
    boolean moveByVelocityAsync(float vx, float vy, float vz, float duration,
                               int drivetrain, YawMode yawMode, String vehicleName);
    
    /**
     * 旋转到指定角度
     * @param yaw 偏航角(度)
     * @param timeout 超时时间
     * @param margin 误差范围
     * @param vehicleName 载具名称
     * @return 操作是否成功
     */
    boolean rotateToYawAsync(float yaw, float timeout, float margin, String vehicleName);
    
    /**
     * 以指定角速度旋转
     * @param yawRate 偏航角速度(度/秒)
     * @param duration 持续时间
     * @param vehicleName 载具名称
     * @return 操作是否成功
     */
    boolean rotateByYawRateAsync(float yawRate, float duration, String vehicleName);
    
    /**
     * 悬停在当前位置
     * @param vehicleName 载具名称
     * @return 操作是否成功
     */
    boolean hoverAsync(String vehicleName);
    
    // ======================== 状态获取相关 ========================
    
    /**
     * 获取多旋翼无人机状态
     * @param vehicleName 载具名称
     * @return 无人机状态
     */
    MultirotorState getMultirotorState(String vehicleName);
    
    /**
     * 获取无人机位置
     * @param vehicleName 载具名称
     * @return 位置信息
     */
    Vector3r getPosition(String vehicleName);
    
    /**
     * 获取无人机速度
     * @param vehicleName 载具名称
     * @return 速度信息
     */
    Vector3r getVelocity(String vehicleName);
    
    /**
     * 获取无人机方向
     * @param vehicleName 载具名称
     * @return 方向信息(四元数)
     */
    Quaternionr getOrientation(String vehicleName);
    
    /**
     * 获取GPS数据
     * @param gpsName GPS传感器名称
     * @param vehicleName 载具名称
     * @return GPS数据
     */
    GpsData getGpsData(String gpsName, String vehicleName);
    
    /**
     * 获取气压计数据
     * @param barometerName 气压计传感器名称
     * @param vehicleName 载具名称
     * @return 气压计数据
     */
    BarometerData getBarometerData(String barometerName, String vehicleName);
    
    /**
     * 获取磁力计数据
     * @param magnetometerName 磁力计传感器名称
     * @param vehicleName 载具名称
     * @return 磁力计数据
     */
    MagnetometerData getMagnetometerData(String magnetometerName, String vehicleName);
    
    // ======================== 图像相关 ========================
    
    /**
     * 获取图像
     * @param requests 图像请求列表
     * @param vehicleName 载具名称
     * @return 图像响应列表
     */
    ImageResponse[] simGetImages(ImageRequest[] requests, String vehicleName);
    
    /**
     * 获取单张图像
     * @param cameraName 相机名称
     * @param imageType 图像类型
     * @param vehicleName 载具名称
     * @return 图像数据
     */
    byte[] getImage(String cameraName, int imageType, String vehicleName);
    
    // ======================== 碰撞检测相关 ========================
    
    /**
     * 获取碰撞信息
     * @param vehicleName 载具名称
     * @return 碰撞信息
     */
    CollisionInfo simGetCollisionInfo(String vehicleName);
    
    // ======================== 取消操作 ========================
    
    /**
     * 取消最后一个任务
     * @param vehicleName 载具名称
     */
    void cancelLastTask(String vehicleName);
} 