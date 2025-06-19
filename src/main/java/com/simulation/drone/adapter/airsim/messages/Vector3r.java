package com.simulation.drone.adapter.airsim.messages;

import org.msgpack.annotation.Message;

import com.simulation.drone.adapter.airsim.api.AirSimRpcMessageTrait;

/**
 * 三维向量类
 * 
 * 功能说明：
 * 1. 空间坐标表示 - 表示三维空间中的位置、速度、加速度等向量量
 * 2. 坐标系兼容 - 兼容AirSim的NED（北东地）坐标系
 * 3. 数学运算支持 - 支持向量的基本数学运算
 * 4. 协议序列化 - 支持msgpack-rpc协议的序列化和反序列化
 * 
 * 坐标系说明：
 * AirSim使用NED（North-East-Down）坐标系：
 * - x_val: 北向分量（North），正值向北
 * - y_val: 东向分量（East），正值向东  
 * - z_val: 地向分量（Down），正值向下（注意：向下为正）
 * - w_val: 四元数w分量（当用作四元数时）
 * 
 * 使用场景：
 * - 位置坐标表示（x, y, z）
 * - 速度向量表示（vx, vy, vz）
 * - 加速度向量表示（ax, ay, az）
 * - 力向量表示（fx, fy, fz）
 * - 四元数表示（w, x, y, z）
 * 
 * 注意事项：
 * - AirSim的Z轴向下为正，与常见的向上为正坐标系不同
 * - 在与其他系统集成时需要注意坐标系转换
 * - 浮点数精度，适用于实时仿真计算
 * 
 * 继承关系：
 * 继承AirSimRpcMessageTrait，支持msgpack-rpc协议序列化
 */
@Message
public class Vector3r extends AirSimRpcMessageTrait {
	
	/**
	 * 四元数w分量
	 * 
	 * 功能：当Vector3r用作四元数时，表示四元数的w分量
	 * 
	 * 四元数格式：q = w + xi + yj + zk
	 * 
	 * 用途：
	 * - 姿态表示（旋转四元数）
	 * - 3D旋转计算
	 * - 姿态插值和平滑
	 * 
	 * 默认值：0.0f
	 */
	public float w_val;
	
	/**
	 * X轴分量（北向分量）
	 * 
	 * 功能：表示向量在X轴方向的分量
	 * 
	 * 坐标系：NED坐标系中的北向（North）
	 * - 正值：向北
	 * - 负值：向南
	 * 
	 * 单位：根据使用场景而定
	 * - 位置：米（m）
	 * - 速度：米/秒（m/s）
	 * - 加速度：米/秒²（m/s²）
	 * - 力：牛顿（N）
	 * 
	 * 默认值：0.0f
	 */
	public float x_val;
	
	/**
	 * Y轴分量（东向分量）
	 * 
	 * 功能：表示向量在Y轴方向的分量
	 * 
	 * 坐标系：NED坐标系中的东向（East）
	 * - 正值：向东
	 * - 负值：向西
	 * 
	 * 单位：根据使用场景而定
	 * - 位置：米（m）
	 * - 速度：米/秒（m/s）
	 * - 加速度：米/秒²（m/s²）
	 * - 力：牛顿（N）
	 * 
	 * 默认值：0.0f
	 */
	public float y_val;
	
	/**
	 * Z轴分量（地向分量）
	 * 
	 * 功能：表示向量在Z轴方向的分量
	 * 
	 * 坐标系：NED坐标系中的地向（Down）
	 * - 正值：向下（注意：与常见坐标系相反）
	 * - 负值：向上
	 * 
	 * 重要说明：
	 * AirSim使用向下为正的Z轴，这与常见的向上为正坐标系不同
	 * 在与其他系统集成时需要特别注意坐标系转换
	 * 
	 * 单位：根据使用场景而定
	 * - 位置：米（m）
	 * - 速度：米/秒（m/s）
	 * - 加速度：米/秒²（m/s²）
	 * - 力：牛顿（N）
	 * 
	 * 默认值：0.0f
	 */
	public float z_val;
	
	/**
	 * 构造函数
	 * 
	 * 功能：初始化三维向量对象，设置所有分量为零
	 * 
	 * 默认值：
	 * - w_val = 0.0f
	 * - x_val = 0.0f
	 * - y_val = 0.0f
	 * - z_val = 0.0f
	 * 
	 * 用途：
	 * - 创建零向量
	 * - 初始化向量变量
	 * - 作为默认值使用
	 * 
	 * 示例：
	 * Vector3r position = new Vector3r(); // 创建原点位置
	 * Vector3r velocity = new Vector3r(); // 创建零速度向量
	 */
	public Vector3r() {
		this.w_val = 0f;
		this.x_val = 0f;
		this.y_val = 0f;
		this.z_val = 0f;
	}
}
