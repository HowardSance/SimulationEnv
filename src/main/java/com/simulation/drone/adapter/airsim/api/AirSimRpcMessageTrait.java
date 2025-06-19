package com.simulation.drone.adapter.airsim.api;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import org.msgpack.MessagePackable;
import org.msgpack.packer.Packer;
import org.msgpack.type.ValueType;
import org.msgpack.unpacker.Unpacker;

/**
 * AirSim RPC消息特征类
 * 
 * 功能说明：
 * 1. 消息序列化 - 将Java对象序列化为msgpack格式用于网络传输
 * 2. 消息反序列化 - 将msgpack格式数据反序列化为Java对象
 * 3. 反射机制 - 使用Java反射自动处理对象字段的序列化/反序列化
 * 4. 协议兼容 - 确保与AirSim的msgpack-rpc协议完全兼容
 * 
 * 技术原理：
 * - 实现MessagePackable接口，提供标准的序列化方法
 * - 使用反射获取类的所有字段，自动处理字段名和值
 * - 支持Map格式的msgpack数据结构，便于跨语言兼容
 * - 提供错误处理机制，确保序列化过程的稳定性
 * 
 * 使用场景：
 * - 所有AirSim消息类的基类
 * - 网络通信数据格式转换
 * - 跨平台数据交换
 * - 协议兼容性保证
 * 
 * 继承关系：
 * 所有AirSim消息类（如MultirotorState、Vector3r、ImuData等）
 * 都应该继承此类以获得自动序列化能力
 */
public class AirSimRpcMessageTrait implements MessagePackable{
	
	/**
	 * 序列化方法 - 将对象转换为msgpack格式
	 * 
	 * 功能：将当前对象的所有字段序列化为msgpack格式的Map结构
	 * 
	 * 序列化流程：
	 * 1. 使用反射获取类的所有声明字段
	 * 2. 写入Map开始标记，包含字段数量
	 * 3. 遍历每个字段：
	 *    - 写入字段名作为Map的key
	 *    - 写入字段值作为Map的value
	 * 4. 写入Map结束标记
	 * 
	 * 数据结构：
	 * msgpack Map格式：
	 * {
	 *   "fieldName1": value1,
	 *   "fieldName2": value2,
	 *   ...
	 * }
	 * 
	 * 错误处理：
	 * - IllegalArgumentException: 字段访问异常
	 * - IllegalAccessException: 字段权限异常
	 * - IOException: 写入异常
	 * 
	 * 参数：
	 *   pk - msgpack的Packer对象，用于写入序列化数据
	 * 
	 * 示例：
	 * 对于Vector3r类（包含x_val, y_val, z_val字段），序列化结果为：
	 * {
	 *   "x_val": 1.0,
	 *   "y_val": 2.0,
	 *   "z_val": 3.0
	 * }
	 */
	public void writeTo(Packer pk) throws IOException {
		
		// 获取当前类的所有声明字段
		Field[] fields = this.getClass().getDeclaredFields();
		
		// 写入Map开始标记，指定字段数量
		pk.writeMapBegin(fields.length);
		 
		// 遍历每个字段进行序列化
		for (Field f : fields) {
			// 获取字段名作为Map的key
			String key = f.getName();
			
			// 写入字段名
			pk.write(key);
		
			try {
				// 获取字段值并写入
				pk.write(f.get(this));
			} catch (IllegalArgumentException e) {
				// 字段访问参数异常
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// 字段访问权限异常
				e.printStackTrace();
			} catch (IOException e) {
				// 写入异常
				e.printStackTrace();
			}
		}
		
		// 写入Map结束标记
		pk.writeMapEnd();
	}
	
	/**
	 * 反序列化方法 - 将msgpack格式转换为对象
	 * 
	 * 功能：从msgpack格式的Map结构中恢复对象的所有字段值
	 * 
	 * 反序列化流程：
	 * 1. 检查下一个数据类型是否为Map
	 * 2. 读取Map开始标记
	 * 3. 创建字段名到Field对象的映射表
	 * 4. 循环读取Map中的键值对：
	 *    - 读取字段名（key）
	 *    - 根据字段名找到对应的Field对象
	 *    - 读取字段值并设置到对象中
	 * 5. 读取Map结束标记
	 * 
	 * 数据结构解析：
	 * 从msgpack Map格式恢复：
	 * {
	 *   "fieldName1": value1,
	 *   "fieldName2": value2,
	 *   ...
	 * }
	 * 
	 * 错误处理：
	 * - NoSuchFieldException: 字段不存在异常
	 * - IllegalArgumentException: 字段设置参数异常
	 * - IllegalAccessException: 字段设置权限异常
	 * - SecurityException: 安全权限异常
	 * 
	 * 参数：
	 *   u - msgpack的Unpacker对象，用于读取序列化数据
	 * 
	 * 示例：
	 * 从msgpack数据恢复Vector3r对象：
	 * 输入：{"x_val": 1.0, "y_val": 2.0, "z_val": 3.0}
	 * 输出：Vector3r对象，x_val=1.0, y_val=2.0, z_val=3.0
	 */
	public void readFrom(Unpacker u) throws IOException {
		// 检查下一个数据类型是否为Map
		if(u.getNextType().equals(ValueType.MAP)) {
			// 读取Map开始标记
			u.readMapBegin();
			
			// 创建字段名到Field对象的映射表，提高查找效率
			HashMap<String, Field> fields = new HashMap<String, Field>();
			for (Field f : this.getClass().getDeclaredFields()) {
				fields.put(f.getName(), f);
			}
			
			// 循环读取Map中的键值对
			while(!u.trySkipNil() && u.getNextType()==ValueType.RAW) {
				// 读取字段名（Map的key）
				String key = u.readString();
				
				// 根据字段名查找对应的Field对象
				Field fie = fields.get(key);
				if(fie==null) {
					// 字段未找到，输出警告信息
					System.out.println("Fields not found");
				}
				
				// 读取字段值
				Object value = u.read(fie.getType());
				
				try {
					// 将值设置到对象的对应字段中
					this.getClass().getDeclaredField(key).set(this, value);
				
				} catch (IllegalArgumentException e) {
					// 字段设置参数异常
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// 字段设置权限异常
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// 字段不存在异常
					e.printStackTrace();
				} catch (SecurityException e) {
					// 安全权限异常
					e.printStackTrace();
				}
			}
			
			// 读取Map结束标记
			u.readMapEnd();
		}
	}
}