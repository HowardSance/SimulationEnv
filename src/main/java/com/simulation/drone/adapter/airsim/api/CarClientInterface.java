package com.simulation.drone.adapter.airsim.api;

import com.simulation.drone.adapter.airsim.messages.CarControls;
import com.simulation.drone.adapter.airsim.messages.CarState;

/**
 * AirSim汽车客户端接口
 * 
 * 功能说明：
 * 1. 汽车控制 - 提供对仿真汽车的运动控制功能
 * 2. 状态获取 - 获取汽车的实时状态信息
 * 3. 继承基础功能 - 继承RpcLibClientBase的所有基础功能
 * 
 * 使用场景：
 * - 自动驾驶算法测试
 * - 汽车运动控制仿真
 * - 地面载具仿真研究
 * - 与无人机协同仿真（空地协同）
 * 
 * 技术特点：
 * - 基于msgpack-rpc协议
 * - 支持实时控制指令
 * - 提供完整的汽车状态反馈
 */
public interface CarClientInterface extends RpcLibClientBase{
	
	/**
	 * 设置汽车控制参数
	 * 
	 * 功能：向仿真汽车发送控制指令
	 * 
	 * 控制参数说明：
	 * - throttle: 油门控制（0.0-1.0，0=停止，1=最大油门）
	 * - steering: 转向控制（-1.0到1.0，负值=左转，正值=右转）
	 * - brake: 刹车控制（0.0-1.0，0=无刹车，1=最大刹车）
	 * - handbrake: 手刹控制（true=启用，false=禁用）
	 * - isManualGear: 手动档位模式（true=手动，false=自动）
	 * - manualGear: 手动档位（-1=倒档，0=空档，1-6=前进档）
	 * - gearImmediate: 档位立即切换（true=立即，false=平滑）
	 * 
	 * 使用场景：
	 * - 自动驾驶控制算法实现
	 * - 手动驾驶仿真
	 * - 汽车性能测试
	 * 
	 * 参数：
	 *   controls - 汽车控制参数对象，包含油门、转向、刹车等控制量
	 *   vehicleName - 汽车载具名称，空字符串表示默认载具
	 * 
	 * 示例用法：
	 * CarControls control = new CarControls();
	 * control.throttle = 0.5f;    // 50%油门
	 * control.steering = 0.2f;    // 轻微右转
	 * control.brake = 0.0f;       // 无刹车
	 * carClient.setCarControls(control, "");
	 */
	public void setCarControls(CarControls controls, String vehicleName);
	
	/**
	 * 获取汽车状态信息
	 * 
	 * 功能：获取仿真汽车的实时状态数据
	 * 
	 * 状态信息包含：
	 * - speed: 当前速度（米/秒）
	 * - gear: 当前档位（-1=倒档，0=空档，1-6=前进档）
	 * - rpm: 发动机转速（转/分钟）
	 * - maxrpm: 最大发动机转速
	 * - handbrake: 手刹状态
	 * - kinematics_estimated: 估计的运动学状态（位置、速度、加速度等）
	 * - timestamp: 状态时间戳
	 * 
	 * 使用场景：
	 * - 汽车状态监控
	 * - 性能数据收集
	 * - 控制算法反馈
	 * - 仿真数据分析
	 * 
	 * 参数：
	 *   vehicleName - 汽车载具名称，空字符串表示默认载具
	 * 
	 * 返回值：
	 *   CarState对象，包含汽车的完整状态信息
	 * 
	 * 示例用法：
	 * CarState state = carClient.getCarState("");
	 * System.out.println("当前速度: " + state.speed + " m/s");
	 * System.out.println("当前档位: " + state.gear);
	 * System.out.println("发动机转速: " + state.rpm + " RPM");
	 */
	public CarState getCarState(String vehicleName);
}
