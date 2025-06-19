package com.simulation.drone.adapter.airsim.messages;

import org.msgpack.annotation.Message;

import com.simulation.drone.adapter.airsim.api.AirSimRpcMessageTrait;

/**
 * IMU（惯性测量单元）数据类
 * 
 * 功能说明：
 * 1. 姿态测量 - 提供载具的实时姿态信息
 * 2. 运动感知 - 测量载具的加速度和角速度
 * 3. 导航支持 - 为惯性导航系统提供基础数据
 * 4. 控制反馈 - 为飞行控制系统提供姿态反馈
 * 
 * IMU传感器组成：
 * - 加速度计：测量线性加速度
 * - 陀螺仪：测量角速度
 * - 磁力计：测量磁场方向（用于航向）
 * 
 * 数据特点：
 * - 高频率输出（通常100Hz以上）
 * - 存在噪声和漂移
 * - 需要滤波和融合算法处理
 * - 短期精度高，长期会累积误差
 * 
 * 使用场景：
 * - 飞行姿态控制
 * - 惯性导航系统
 * - 运动状态估计
 * - 传感器融合算法
 * - 飞行数据记录
 * 
 * 技术应用：
 * - 姿态稳定控制
 * - 自动飞行模式
 * - 运动轨迹跟踪
 * - 故障检测和诊断
 * 
 * 继承关系：
 * 继承AirSimRpcMessageTrait，支持msgpack-rpc协议序列化
 */
@Message
public class ImuData extends AirSimRpcMessageTrait{
	
	/**
	 * 时间戳
	 * 
	 * 功能：记录IMU数据的时间信息
	 * 
	 * 格式：Unix时间戳（毫秒）
	 * 
	 * 重要性：
	 * - 数据时序分析
	 * - 传感器同步
	 * - 性能时间统计
	 * - 数据融合时间对齐
	 * 
	 * 用途：
	 * - 多传感器数据同步
	 * - 数据记录和回放
	 * - 实时性能监控
	 * - 故障时间定位
	 */
	public long time_stamp;
	
	/**
	 * 姿态四元数
	 * 
	 * 功能：表示载具的当前姿态（旋转状态）
	 * 
	 * 四元数格式：q = w + xi + yj + zk
	 * 
	 * 姿态表示：
	 * - 相对于参考坐标系的旋转
	 * - 避免万向锁问题
	 * - 支持平滑插值
	 * 
	 * 坐标系：
	 * - 参考坐标系：NED（北东地）
	 * - 载具坐标系：前右下
	 * 
	 * 应用场景：
	 * - 飞行姿态控制
	 * - 相机姿态计算
	 * - 传感器标定
	 * - 运动学计算
	 * 
	 * 数据来源：
	 * - 传感器融合算法
	 * - 卡尔曼滤波器
	 * - 互补滤波器
	 * 
	 * 精度特点：
	 * - 短期精度高
	 * - 长期可能存在漂移
	 * - 需要定期校准
	 */
	public Quaternionr orientation;
}

