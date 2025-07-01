package com.simulation.drone.infrastructure.adapter.airsim.client;

import com.simulation.drone.domain.detection.model.DistanceSensorData;
import com.simulation.drone.domain.detection.model.ImuData;
import com.simulation.drone.domain.detection.model.LidarData;
import com.simulation.drone.domain.device.valueobject.KinematicsState;
import com.simulation.drone.domain.device.valueobject.Pose;

/**
 * AirSim RPC客户端基础接口
 * 定义与AirSim仿真器进行RPC通信的基础方法契约
 * 包含连接管理、仿真控制、载具操作、传感器数据获取等功能
 * 
 * @author JP
 * @version 1.0
 */
public interface RpcLibClientBase {

	/**
	 * 确认与AirSim仿真器的连接
	 * 用于验证客户端与服务器的连接状态
	 */
	public void confirmConnection();

	/**
	 * 重置仿真环境
	 * 将仿真器恢复到初始状态
	 */
	public void reset();
	
	/**
	 * 获取当前连接状态
	 * 
	 * @return 连接状态码，具体含义由实现类定义
	 */
	public int getConnectionState();
	
	/**
	 * 检测与服务器的连接是否正常
	 * 
	 * @return true表示连接正常，false表示连接异常
	 */
	public boolean ping();
	
	/**
	 * 获取客户端版本号
	 * 
	 * @return 客户端版本号
	 */
	public int getClientVersion();
	
	/**
	 * 获取服务器版本号
	 * 
	 * @return 服务器版本号
	 */
	public int getServerVersion();
	
	/**
	 * 获取服务器最低要求版本号
	 * 
	 * @return 服务器最低要求版本号
	 */
	public int getMinRequiredServerVersion();
	
	/**
	 * 获取客户端最低要求版本号
	 * 
	 * @return 客户端最低要求版本号
	 */
	public int getMinRequiredClientVersion();
	
	/**
	 * 检查仿真是否处于暂停状态
	 * 
	 * @return true表示仿真已暂停，false表示仿真正在运行
	 */
	public boolean simIsPaused();
	
	/**
	 * 设置仿真的暂停状态
	 * 
	 * @param isPaused true表示暂停仿真，false表示恢复仿真
	 */
	public void simPause(boolean isPaused);
	
	/**
	 * 让仿真继续运行指定的时间
	 * 
	 * @param seconds 继续运行的时间（秒）
	 */
	public void simContinueForTime(double seconds);
	
	/**
	 * 设置仿真环境的时间参数
	 * 
	 * @param isEnabeled 是否启用时间设置
	 * @param startDatetime 开始日期时间字符串
	 * @param isStartDatetimeDst 开始时间是否为夏令时
	 * @param celestialClockSpeed 天体时钟速度
	 * @param updateIntervalSecs 更新间隔（秒）
	 * @param moveSun 是否移动太阳
	 */
	public void simSetTimeOfDay(boolean isEnabeled, String startDatetime, boolean isStartDatetimeDst,
			float celestialClockSpeed, float updateIntervalSecs, boolean moveSun);
	
	/**
	 * 启用或禁用天气系统
	 * 
	 * @param enable true表示启用天气，false表示禁用天气
	 */
	public void simEnableWeather(boolean enable);
	
	//public void simSetWeatherParameter(WheaterParameter param, float value);
	
	/**
	 * 获取指定物体的位姿信息
	 * 
	 * @param objectName 物体名称
	 * @return 物体的位姿信息
	 */
	public Pose simGetObjectPose(String objectName);
	
	/**
	 * 设置指定物体的位姿
	 * 
	 * @param objectName 物体名称
	 * @param pose 要设置的位姿
	 * @param teleport 是否使用瞬移模式
	 * @return true表示设置成功，false表示设置失败
	 */
	public boolean simSetObjectPose(String objectName, Pose pose, boolean teleport);
	
	/**
	 * 取消指定载具的最后一个任务
	 * 
	 * @param vehicleName 载具名称
	 */
	public void cancelLastTask(String vehicleName);
	
	/**
	 * 控制载具的解锁/上锁状态
	 * 
	 * @param arm true表示解锁，false表示上锁
	 * @param vehicleName 载具名称
	 * @return true表示操作成功，false表示操作失败
	 */
	public boolean armDisarm(boolean arm, String vehicleName);
	
	/**
	 * 检查指定载具是否启用了API控制
	 * 
	 * @param vehicleName 载具名称
	 * @return true表示已启用API控制，false表示未启用
	 */
	public boolean isApiControlEnabled(String vehicleName);
	
	/**
	 * 启用或禁用指定载具的API控制
	 * 
	 * @param isEnabled true表示启用API控制，false表示禁用API控制
	 * @param vehicleName 载具名称
	 */
	public void enableApiControl(boolean isEnabled, String vehicleName);
	
	//public GeoPoint getHomeGeoPoint (String vehicleName);
	
	/**
	 * 获取激光雷达数据
	 * 
	 * @param lidarName 激光雷达名称
	 * @param vehicleName 载具名称
	 * @return 激光雷达数据
	 */
	public LidarData getLidarData(String lidarName, String vehicleName);
	
	/**
	 * 获取指定载具的位姿信息
	 * 
	 * @param vehicleName 载具名称
	 * @return 载具的位姿信息
	 */
	public Pose simGetVehiclePose(String vehicleName);
	
	/**
	 * 设置指定载具的位姿
	 * 
	 * @param pose 要设置的位姿
	 * @param ignoreCollision 是否忽略碰撞检测
	 * @param vehicleName 载具名称
	 */
	public void simSetVehiclePose(Pose pose, boolean ignoreCollision, String vehicleName);
	
	/**
	 * 获取指定载具的真实运动学状态
	 * 包含位置、速度、加速度等信息
	 * 
	 * @param vehicleName 载具名称
	 * @return 运动学状态信息
	 */
	public KinematicsState simGetGroundTruthKinematics(String vehicleName);
	
	/**
	 * 获取距离传感器数据
	 * 
	 * @param distance_sensor_name 距离传感器名称
	 * @param vehicleName 载具名称
	 * @return 距离传感器数据
	 */
	public DistanceSensorData getDistanceSensorData(String distance_sensor_name, String vehicleName);
	
	/**
	 * 获取IMU（惯性测量单元）数据
	 * 
	 * @param imuName IMU名称
	 * @param vehicleName 载具名称
	 * @return IMU数据
	 */
	public ImuData getImuData(String imuName, String vehicleName);
} 