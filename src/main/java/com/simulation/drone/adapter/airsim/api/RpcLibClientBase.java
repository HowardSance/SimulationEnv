package com.simulation.drone.adapter.airsim.api;

import com.simulation.drone.adapter.airsim.messages.DistanceSensorData;
import com.simulation.drone.adapter.airsim.messages.ImuData;
import com.simulation.drone.adapter.airsim.messages.KinematicsState;
import com.simulation.drone.adapter.airsim.messages.LidarData;
import com.simulation.drone.adapter.airsim.messages.Pose;

/**
 * AirSim RPC基础客户端接口
 * 
 * 功能说明：
 * 1. 连接管理 - 提供与AirSim仿真器的连接建立、状态检查和版本管理
 * 2. 仿真控制 - 控制仿真器的暂停、继续、时间设置等基础功能
 * 3. 环境控制 - 管理仿真环境的时间、天气、光照等参数
 * 4. 载具控制 - 提供载具的启动、API控制启用等基础控制功能
 * 5. 传感器数据 - 获取各种传感器（IMU、激光雷达、距离传感器）的实时数据
 * 6. 位姿管理 - 获取和设置载具的位置、姿态、运动学状态
 * 
 * 使用场景：
 * - 作为所有AirSim客户端接口的基础接口
 * - 提供通用的仿真控制和数据获取功能
 * - 支持无人机、汽车等多种载具类型
 */
public interface RpcLibClientBase {

	// ======================== 连接管理功能 ========================
	
	/**
	 * 确认与AirSim的连接状态
	 * 功能：验证RPC连接是否正常工作
	 * 用途：连接初始化后的状态确认
	 */
	public void confirmConnection();
	
	/**
	 * 重置仿真环境
	 * 功能：将仿真环境恢复到初始状态
	 * 用途：仿真重新开始或错误恢复
	 */
	public void reset();
	
	/**
	 * 获取当前连接状态
	 * 功能：返回连接的状态码
	 * 返回值：连接状态码（0=断开，1=连接中，2=已连接）
	 */
	public int getConnectionState();
	
	/**
	 * 测试连接连通性
	 * 功能：发送ping请求测试网络连通性
	 * 用途：网络状态监控和故障诊断
	 * 返回值：true=连通，false=断开
	 */
	public boolean ping();
	
	/**
	 * 获取客户端版本号
	 * 功能：返回当前客户端的版本信息
	 * 用途：版本兼容性检查
	 */
	public int getClientVersion();
	
	/**
	 * 获取服务器版本号
	 * 功能：返回AirSim服务器的版本信息
	 * 用途：版本兼容性检查
	 */
	public int getServerVersion();
	
	/**
	 * 获取最低要求的服务器版本
	 * 功能：返回客户端支持的最低服务器版本
	 * 用途：版本兼容性验证
	 */
	public int getMinRequiredServerVersion();
	
	/**
	 * 获取最低要求的客户端版本
	 * 功能：返回服务器支持的最低客户端版本
	 * 用途：版本兼容性验证
	 */
	public int getMinRequiredClientVersion();
	
	// ======================== 仿真控制功能 ========================
	
	/**
	 * 检查仿真是否暂停
	 * 功能：获取当前仿真的暂停状态
	 * 用途：仿真状态监控
	 * 返回值：true=已暂停，false=运行中
	 */
	public boolean simIsPaused();
	
	/**
	 * 暂停/恢复仿真
	 * 功能：控制仿真的暂停和恢复
	 * 参数：isPaused - true=暂停，false=恢复
	 * 用途：仿真流程控制
	 */
	public void simPause(boolean isPaused);
	
	/**
	 * 继续仿真指定时间
	 * 功能：让仿真继续运行指定的秒数后自动暂停
	 * 参数：seconds - 继续运行的秒数
	 * 用途：分步仿真控制
	 */
	public void simContinueForTime(double seconds);
	
	// ======================== 环境控制功能 ========================
	
	/**
	 * 设置仿真时间
	 * 功能：配置仿真环境的时间参数
	 * 参数：
	 *   isEnabled - 是否启用时间控制
	 *   startDatetime - 起始日期时间
	 *   isStartDatetimeDst - 是否夏令时
	 *   celestialClockSpeed - 天体时钟速度
	 *   updateIntervalSecs - 更新间隔秒数
	 *   moveSun - 是否移动太阳
	 * 用途：环境光照和阴影模拟
	 */
	public void simSetTimeOfDay(boolean isEnabeled, String startDatetime, boolean isStartDatetimeDst,
			float celestialClockSpeed, float updateIntervalSecs, boolean moveSun);
	
	/**
	 * 启用/禁用天气系统
	 * 功能：控制仿真环境的天气效果
	 * 参数：enable - true=启用天气，false=禁用天气
	 * 用途：环境真实感增强
	 */
	public void simEnableWeather(boolean enable);
	
	//public void simSetWeatherParameter(WheaterParameter param, float value);
	
	// ======================== 对象位姿管理功能 ========================
	
	/**
	 * 获取对象位姿
	 * 功能：获取指定对象的位置和姿态信息
	 * 参数：objectName - 对象名称
	 * 返回值：包含位置和姿态的Pose对象
	 * 用途：场景对象状态获取
	 */
	public Pose simGetObjectPose(String objectName);
	
	/**
	 * 设置对象位姿
	 * 功能：设置指定对象的位置和姿态
	 * 参数：
	 *   objectName - 对象名称
	 *   pose - 目标位姿
	 *   teleport - 是否瞬移（true=瞬移，false=平滑移动）
	 * 返回值：操作是否成功
	 * 用途：场景对象位置控制
	 */
	public boolean simSetObjectPose(String objectName, Pose pose, boolean teleport);
	
	/**
	 * 取消最后一个任务
	 * 功能：取消载具正在执行的最后一个控制任务
	 * 参数：vehicleName - 载具名称
	 * 用途：紧急停止和任务中断
	 */
	public void cancelLastTask(String vehicleName);
	
	// ======================== 载具控制功能 ========================
	
	/**
	 * 启动/关闭载具
	 * 功能：控制载具的启动和关闭状态
	 * 参数：
	 *   arm - true=启动，false=关闭
	 *   vehicleName - 载具名称
	 * 返回值：操作是否成功
	 * 用途：载具电源管理
	 */
	public boolean armDisarm(boolean arm, String vehicleName);
	
	/**
	 * 检查API控制是否启用
	 * 功能：检查指定载具是否允许外部API控制
	 * 参数：vehicleName - 载具名称
	 * 返回值：true=API控制已启用，false=API控制已禁用
	 * 用途：控制权限验证
	 */
	public boolean isApiControlEnabled(String vehicleName);
	
	/**
	 * 启用/禁用API控制
	 * 功能：控制是否允许外部API对载具进行操作
	 * 参数：
	 *   isEnabled - true=启用API控制，false=禁用API控制
	 *   vehicleName - 载具名称
	 * 用途：安全控制管理
	 */
	public void enableApiControl(boolean isEnabled, String vehicleName);
	
	//public GeoPoint getHomeGeoPoint (String vehicleName);
	
	// ======================== 传感器数据获取功能 ========================
	
	/**
	 * 获取激光雷达数据
	 * 功能：获取指定载具上激光雷达传感器的点云数据
	 * 参数：
	 *   lidarName - 激光雷达传感器名称
	 *   vehicleName - 载具名称
	 * 返回值：包含点云数据的LidarData对象
	 * 用途：环境感知和障碍物检测
	 */
	public LidarData getLidarData(String lidarName, String vehicleName);
	
	/**
	 * 获取载具位姿
	 * 功能：获取指定载具的当前位置和姿态
	 * 参数：vehicleName - 载具名称
	 * 返回值：载具的位姿信息
	 * 用途：载具状态监控和定位
	 */
	public Pose simGetVehiclePose(String vehicleName);
	
	/**
	 * 设置载具位姿
	 * 功能：设置指定载具的位置和姿态
	 * 参数：
	 *   pose - 目标位姿
	 *   ignoreCollision - 是否忽略碰撞检测
	 *   vehicleName - 载具名称
	 * 用途：载具位置重置和测试
	 */
	public void simSetVehiclePose(Pose pose, boolean ignoreCollision, String vehicleName);
	
	/**
	 * 获取载具真实运动学状态
	 * 功能：获取载具的精确运动学信息（位置、速度、加速度等）
	 * 参数：vehicleName - 载具名称
	 * 返回值：包含完整运动学信息的KinematicsState对象
	 * 用途：精确的状态估计和物理仿真
	 */
	public KinematicsState simGetGroundTruthKinematics(String vehicleName);
	
	/**
	 * 获取距离传感器数据
	 * 功能：获取指定载具上距离传感器的测量数据
	 * 参数：
	 *   distance_sensor_name - 距离传感器名称
	 *   vehicleName - 载具名称
	 * 返回值：包含距离信息的DistanceSensorData对象
	 * 用途：障碍物距离测量和避障
	 */
	public DistanceSensorData getDistanceSensorData(String distance_sensor_name, String vehicleName);
	
	/**
	 * 获取IMU数据
	 * 功能：获取指定载具上惯性测量单元的数据
	 * 参数：
	 *   imuName - IMU传感器名称
	 *   vehicleName - 载具名称
	 * 返回值：包含加速度、角速度、姿态的ImuData对象
	 * 用途：载具姿态估计和运动控制
	 */
	public ImuData getImuData(String imuName, String vehicleName);
}