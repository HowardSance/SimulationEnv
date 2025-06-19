package com.simulation.drone.adapter.airsim.example;

import com.simulation.drone.adapter.airsim.DroneClientInterface;
import com.simulation.drone.adapter.airsim.messages.*;
import lombok.extern.slf4j.Slf4j;
import org.msgpack.rpc.Client;
import org.msgpack.rpc.loop.EventLoop;

/**
 * 无人机仿真示例
 * 展示如何使用重构后的AirSim适配器进行无人机控制和传感器数据获取
 * 基于AirSim官方API规范实现
 */
@Slf4j
public class DroneSimulationExample {
    
    private static final String AIRSIM_HOST = "127.0.0.1";
    private static final int AIRSIM_PORT = 41451;
    private static final String VEHICLE_NAME = ""; // 空字符串表示默认无人机
    
    public static void main(String[] args) {
        EventLoop loop = EventLoop.defaultEventLoop();
        Client client = new Client(AIRSIM_HOST, AIRSIM_PORT, loop);
        
        try {
            // 创建无人机客户端代理
            DroneClientInterface droneClient = client.proxy(DroneClientInterface.class);
            
            // 确认连接
            droneClient.confirmConnection();
            log.info("AirSim连接成功");
            
            // 演示基本控制流程
            demonstrateBasicControl(droneClient);
            
            // 演示传感器数据获取
            demonstrateSensorData(droneClient);
            
            // 演示图像获取
            demonstrateImageCapture(droneClient);
            
            // 演示运动控制
            demonstrateMovementControl(droneClient);
            
        } catch (Exception e) {
            log.error("示例执行失败", e);
        } finally {
            // 清理资源
            if (client != null) {
                client.close();
            }
            if (loop != null) {
                loop.shutdown();
            }
        }
    }
    
    /**
     * 演示基本控制流程：启用API控制、启动、起飞、降落
     */
    private static void demonstrateBasicControl(DroneClientInterface droneClient) {
        log.info("=== 演示基本控制流程 ===");
        
        try {
            // 1. 启用API控制
            droneClient.enableApiControl(true, VEHICLE_NAME);
            boolean controlEnabled = droneClient.isApiControlEnabled(VEHICLE_NAME);
            log.info("API控制状态: {}", controlEnabled);
            
            // 2. 启动无人机
            boolean armResult = droneClient.armDisarm(true, VEHICLE_NAME);
            log.info("无人机启动结果: {}", armResult);
            
            // 3. 起飞
            boolean takeoffResult = droneClient.takeoffAsync(10.0f, VEHICLE_NAME);
            log.info("起飞结果: {}", takeoffResult);
            
            // 等待起飞完成
            Thread.sleep(3000);
            
            // 4. 悬停
            boolean hoverResult = droneClient.hoverAsync(VEHICLE_NAME);
            log.info("悬停结果: {}", hoverResult);
            
            Thread.sleep(2000);
            
            // 5. 降落
            boolean landResult = droneClient.landAsync(10.0f, VEHICLE_NAME);
            log.info("降落结果: {}", landResult);
            
            Thread.sleep(3000);
            
            // 6. 关闭无人机
            droneClient.armDisarm(false, VEHICLE_NAME);
            log.info("无人机已关闭");
            
        } catch (Exception e) {
            log.error("基本控制演示失败", e);
        }
    }
    
    /**
     * 演示传感器数据获取
     */
    private static void demonstrateSensorData(DroneClientInterface droneClient) {
        log.info("=== 演示传感器数据获取 ===");
        
        try {
            // 获取无人机状态
            MultirotorState state = droneClient.getMultirotorState(VEHICLE_NAME);
            log.info("无人机状态 - 已着陆: {}, 准备就绪: {}, 可以启动: {}", 
                    state.landed_state, state.ready, state.can_arm);
            
            // 获取位置和姿态
            Vector3r position = droneClient.getPosition(VEHICLE_NAME);
            log.info("位置: x={}, y={}, z={}", position.x_val, position.y_val, position.z_val);
            
            Quaternionr orientation = droneClient.getOrientation(VEHICLE_NAME);
            log.info("姿态: w={}, x={}, y={}, z={}", 
                    orientation.w_val, orientation.x_val, orientation.y_val, orientation.z_val);
            
            Vector3r velocity = droneClient.getVelocity(VEHICLE_NAME);
            log.info("速度: vx={}, vy={}, vz={}", velocity.x_val, velocity.y_val, velocity.z_val);
            
            // 获取IMU数据
            ImuData imuData = droneClient.getImuData("imu", VEHICLE_NAME);
            log.info("IMU数据时间戳: {}", imuData.time_stamp);
            
            // 获取距离传感器数据
            DistanceSensorData distanceData = droneClient.getDistanceSensorData("distance", VEHICLE_NAME);
            log.info("距离传感器: 距离={}, 最小距离={}, 最大距离={}", 
                    distanceData.distance, distanceData.min_distance, distanceData.max_distance);
            
            // 获取GPS数据
            GpsData gpsData = droneClient.getGpsData("gps", VEHICLE_NAME);
            log.info("GPS: 纬度={}, 经度={}, 海拔={}", 
                    gpsData.gnss.latitude, gpsData.gnss.longitude, gpsData.gnss.altitude);
            
            // 获取气压计数据
            BarometerData barometerData = droneClient.getBarometerData("barometer", VEHICLE_NAME);
            log.info("气压计: 高度={}, 压力={}", barometerData.altitude, barometerData.pressure);
            
            // 获取碰撞信息
            CollisionInfo collisionInfo = droneClient.simGetCollisionInfo(VEHICLE_NAME);
            log.info("碰撞状态: {}", collisionInfo.has_collided);
            
        } catch (Exception e) {
            log.error("传感器数据获取演示失败", e);
        }
    }
    
    /**
     * 演示图像获取
     */
    private static void demonstrateImageCapture(DroneClientInterface droneClient) {
        log.info("=== 演示图像获取 ===");
        
        try {
            // 创建图像请求
            ImageRequest[] requests = {
                new ImageRequest("front_center", ImageRequest.SCENE, false, true),
                new ImageRequest("front_center", ImageRequest.DEPTH_VIS, false, true),
                new ImageRequest("front_center", ImageRequest.SEGMENTATION, false, true)
            };
            
            // 获取图像
            ImageResponse[] responses = droneClient.simGetImages(requests, VEHICLE_NAME);
            
            if (responses != null) {
                for (int i = 0; i < responses.length; i++) {
                    ImageResponse response = responses[i];
                    log.info("图像 {}: 相机={}, 类型={}, 尺寸={}x{}, 数据大小={}字节", 
                            i, response.camera_name, response.image_type, 
                            response.width, response.height, response.image_data_uint8.length);
                }
            }
            
        } catch (Exception e) {
            log.error("图像获取演示失败", e);
        }
    }
    
    /**
     * 演示运动控制
     */
    private static void demonstrateMovementControl(DroneClientInterface droneClient) {
        log.info("=== 演示运动控制 ===");
        
        try {
            // 启用API控制并起飞
            droneClient.enableApiControl(true, VEHICLE_NAME);
            droneClient.armDisarm(true, VEHICLE_NAME);
            droneClient.takeoffAsync(10.0f, VEHICLE_NAME);
            Thread.sleep(3000);
            
            // 1. 移动到指定位置
            YawMode yawMode = YawMode.createYawMode(0); // 保持当前偏航角
            boolean moveResult = droneClient.moveToPositionAsync(10, 0, -10, 5.0f, 30.0f, 0, yawMode, VEHICLE_NAME);
            log.info("移动到位置结果: {}", moveResult);
            Thread.sleep(5000);
            
            // 2. 以指定速度移动
            YawMode yawRateMode = YawMode.createYawRateMode(30); // 30度/秒旋转
            boolean velocityResult = droneClient.moveByVelocityAsync(2.0f, 0, 0, 3.0f, 0, yawRateMode, VEHICLE_NAME);
            log.info("速度控制结果: {}", velocityResult);
            Thread.sleep(4000);
            
            // 3. 旋转到指定角度
            boolean rotateResult = droneClient.rotateToYawAsync(90.0f, 10.0f, 5.0f, VEHICLE_NAME);
            log.info("旋转结果: {}", rotateResult);
            Thread.sleep(3000);
            
            // 4. 回到起始位置
            boolean homeResult = droneClient.goHomeAsync(30.0f, VEHICLE_NAME);
            log.info("回家结果: {}", homeResult);
            Thread.sleep(5000);
            
            // 降落
            droneClient.landAsync(10.0f, VEHICLE_NAME);
            Thread.sleep(3000);
            droneClient.armDisarm(false, VEHICLE_NAME);
            
        } catch (Exception e) {
            log.error("运动控制演示失败", e);
        }
    }
}