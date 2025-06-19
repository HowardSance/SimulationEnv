package com.simulation.drone.adapter.airsim.messages;

import org.msgpack.annotation.Message;
import com.simulation.drone.adapter.airsim.api.AirSimRpcMessageTrait;

/**
 * 多旋翼无人机状态信息
 * 
 * 功能说明：
 * 1. 状态监控 - 提供无人机的完整状态信息，包括位置、姿态、速度等
 * 2. 碰撞检测 - 记录无人机的碰撞状态和碰撞信息
 * 3. 运动学数据 - 提供估计和真实的运动学状态数据
 * 4. 系统状态 - 监控无人机的准备状态、控制权限等系统信息
 * 5. 传感器数据 - 整合GPS、RC等传感器数据
 * 
 * 使用场景：
 * - 无人机状态实时监控
 * - 飞行控制算法反馈
 * - 碰撞检测和安全监控
 * - 性能分析和调试
 * - 仿真数据记录和分析
 * 
 * 数据来源：
 * - AirSim物理引擎计算
 * - 传感器模拟数据
 * - 控制系统状态
 * 
 * 继承关系：
 * 继承AirSimRpcMessageTrait，支持msgpack-rpc协议序列化
 */
@Message
public class MultirotorState extends AirSimRpcMessageTrait {
    
    /**
     * 碰撞信息
     * 
     * 功能：记录无人机的碰撞状态和详细信息
     * 
     * 包含信息：
     * - has_collided: 是否发生碰撞
     * - collision_count: 碰撞次数
     * - impact_point: 碰撞点位置
     * - impact_normal: 碰撞法向量
     * - impact_penetration: 碰撞穿透深度
     * 
     * 用途：
     * - 安全监控和碰撞预警
     * - 飞行路径规划和避障
     * - 仿真场景碰撞检测
     * - 飞行安全分析
     */
    public CollisionInfo collision;
    
    /**
     * 估计运动学状态
     * 
     * 功能：提供基于传感器数据的估计运动学信息
     * 
     * 包含数据：
     * - position: 估计位置（x, y, z）
     * - orientation: 估计姿态（四元数）
     * - linear_velocity: 估计线速度
     * - angular_velocity: 估计角速度
     * - linear_acceleration: 估计线加速度
     * - angular_acceleration: 估计角加速度
     * 
     * 特点：
     * - 基于传感器融合算法
     * - 可能存在噪声和误差
     * - 用于实时控制算法
     * 
     * 用途：
     * - 飞行控制系统反馈
     * - 状态估计和滤波
     * - 实时位置跟踪
     */
    public KinematicsState kinematics_estimated;
    
    /**
     * 真实运动学状态
     * 
     * 功能：提供仿真环境中的真实运动学信息（地面真值）
     * 
     * 包含数据：
     * - position: 真实位置（x, y, z）
     * - orientation: 真实姿态（四元数）
     * - linear_velocity: 真实线速度
     * - angular_velocity: 真实角速度
     * - linear_acceleration: 真实线加速度
     * - angular_acceleration: 真实角加速度
     * 
     * 特点：
     * - 来自仿真物理引擎
     * - 无噪声和误差
     * - 用于算法验证和调试
     * 
     * 用途：
     * - 算法性能评估
     * - 传感器误差分析
     * - 仿真结果验证
     * - 基准数据对比
     */
    public KinematicsState kinematics_true;
    
    /**
     * GPS位置信息
     * 
     * 功能：提供无人机的GPS坐标信息
     * 
     * 包含数据：
     * - latitude: 纬度（度）
     * - longitude: 经度（度）
     * - altitude: 海拔高度（米）
     * 
     * 用途：
     * - 地理定位和导航
     * - 飞行路径规划
     * - 地理围栏监控
     * - 飞行日志记录
     */
    public GeoPoint gps_location;
    
    /**
     * 时间戳
     * 
     * 功能：记录状态数据的时间信息
     * 
     * 格式：Unix时间戳（毫秒）
     * 
     * 用途：
     * - 数据时序分析
     * - 性能时间统计
     * - 数据同步验证
     * - 历史数据查询
     */
    public long timestamp;
    
    /**
     * 着陆状态
     * 
     * 功能：指示无人机是否已着陆
     * 
     * 状态值：
     * - true: 已着陆
     * - false: 飞行中
     * 
     * 用途：
     * - 飞行状态监控
     * - 安全控制逻辑
     * - 任务状态管理
     * - 自动着陆控制
     */
    public boolean landed_state;
    
    /**
     * 遥控器数据
     * 
     * 功能：提供遥控器输入信号数据
     * 
     * 包含数据：
     * - timestamp: 数据时间戳
     * - pitch: 俯仰控制量
     * - roll: 横滚控制量
     * - throttle: 油门控制量
     * - yaw: 偏航控制量
     * - switch1-8: 开关状态
     * 
     * 用途：
     * - 手动控制模式
     * - 遥控器信号监控
     * - 控制模式切换
     * - 紧急控制接管
     */
    public RCData rc_data;
    
    /**
     * 准备就绪状态
     * 
     * 功能：指示无人机是否准备就绪可以飞行
     * 
     * 状态值：
     * - true: 准备就绪
     * - false: 未准备就绪
     * 
     * 检查项目：
     * - 传感器初始化完成
     * - 控制系统就绪
     * - 电池电量充足
     * - 无故障状态
     * 
     * 用途：
     * - 起飞前检查
     * - 系统状态监控
     * - 安全控制逻辑
     */
    public boolean ready;
    
    /**
     * API控制权限
     * 
     * 功能：指示无人机是否允许外部API控制
     * 
     * 状态值：
     * - true: 允许API控制
     * - false: 禁止API控制
     * 
     * 控制逻辑：
     * - 需要先启用API控制才能执行控制命令
     * - 安全机制，防止意外控制
     * - 可以动态切换控制模式
     * 
     * 用途：
     * - 控制权限管理
     * - 安全控制验证
     * - 多控制源协调
     */
    public boolean can_arm;
    
    /**
     * 构造函数
     * 
     * 功能：初始化多旋翼状态对象，设置默认值
     * 
     * 默认状态：
     * - 无碰撞状态
     * - 零位置和速度
     * - 已着陆状态
     * - 未准备就绪
     * - 禁止API控制
     * 
     * 初始化对象：
     * - 创建所有子对象实例
     * - 设置安全的默认值
     * - 确保对象完整性
     */
    public MultirotorState() {
        this.collision = new CollisionInfo();
        this.kinematics_estimated = new KinematicsState();
        this.kinematics_true = new KinematicsState();
        this.gps_location = new GeoPoint();
        this.rc_data = new RCData();
        this.timestamp = 0;
        this.landed_state = true;
        this.ready = false;
        this.can_arm = false;
    }
} 