package com.JP.dronesim.infrastructure.external.airsim;

import com.JP.dronesim.infrastructure.external.airsim.dto.*;
import com.JP.dronesim.infrastructure.adapter.airsim.config.AirSimConnectionConfig;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * AirSim集成使用示例
 * 展示如何使用完善的AirSim交互代码
 */
@Component
public class AirSimIntegrationExample {
    
    private final AirSimApiClient airSimApiClient;
    
    public AirSimIntegrationExample(AirSimApiClient airSimApiClient) {
        this.airSimApiClient = airSimApiClient;
    }
    
    /**
     * 基础连接和控制示例
     */
    public void basicConnectionExample() {
        try {
            // 1. 初始化连接
            System.out.println("正在连接到AirSim...");
            airSimApiClient.initialize();
            
            if (airSimApiClient.isConnected()) {
                System.out.println("成功连接到AirSim");
                
                // 2. 注册无人机
                String droneId = "drone1";
                String vehicleName = "Drone1";
                airSimApiClient.registerVehicle(droneId, vehicleName);
                
                // 3. 启用API控制
                airSimApiClient.enableApiControl(droneId, true);
                System.out.println("API控制已启用: " + airSimApiClient.isApiControlEnabled(droneId));
                
                // 4. 解锁无人机
                airSimApiClient.armDisarm(droneId, true);
                System.out.println("无人机已解锁");
                
                // 5. 起飞
                System.out.println("无人机起飞中...");
                airSimApiClient.takeoff(droneId, 10.0f);
                
                // 等待起飞完成
                Thread.sleep(5000);
                
                // 6. 获取当前状态
                Pose currentPose = airSimApiClient.getVehiclePose(droneId);
                System.out.println("当前位姿: " + currentPose);
                
                KinematicsState kinematicsState = airSimApiClient.getKinematicsState(droneId);
                System.out.println("运动学状态: " + kinematicsState);
                
                // 7. 悬停
                System.out.println("无人机悬停中...");
                airSimApiClient.hover(droneId);
                Thread.sleep(3000);
                
                // 8. 降落
                System.out.println("无人机降落中...");
                airSimApiClient.land(droneId);
                
            } else {
                System.err.println("连接AirSim失败");
            }
            
        } catch (Exception e) {
            System.err.println("示例执行失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 断开连接
            airSimApiClient.disconnect();
            System.out.println("已断开AirSim连接");
        }
    }
    
    /**
     * 传感器数据获取示例
     */
    public void sensorDataExample() {
        try {
            // 初始化连接
            airSimApiClient.initialize();
            
            if (!airSimApiClient.isConnected()) {
                System.err.println("未连接到AirSim");
                return;
            }
            
            String droneId = "drone1";
            airSimApiClient.registerVehicle(droneId, "Drone1");
            airSimApiClient.enableApiControl(droneId, true);
            
            System.out.println("开始获取传感器数据...");
            
            // 循环获取传感器数据
            for (int i = 0; i < 10; i++) {
                System.out.println("\n=== 第 " + (i + 1) + " 次数据采集 ===");
                
                // 获取IMU数据
                try {
                    ImuData imuData = airSimApiClient.getImuData("imu", droneId);
                    if (imuData != null && imuData.isValidData()) {
                        System.out.println("IMU数据: " + imuData);
                        float[] eulerAngles = imuData.getEulerAnglesDegrees();
                        System.out.printf("姿态角度: Roll=%.2f°, Pitch=%.2f°, Yaw=%.2f°%n", 
                                        eulerAngles[0], eulerAngles[1], eulerAngles[2]);
                    }
                } catch (Exception e) {
                    System.err.println("获取IMU数据失败: " + e.getMessage());
                }
                
                // 获取距离传感器数据
                try {
                    DistanceSensorData distanceData = airSimApiClient.getDistanceSensorData("distance", droneId);
                    if (distanceData != null && distanceData.isValidMeasurement()) {
                        System.out.println("距离传感器数据: " + distanceData);
                        if (distanceData.hasObstacle()) {
                            System.out.println("检测到障碍物！距离: " + distanceData.getDistance() + "米");
                        }
                    }
                } catch (Exception e) {
                    System.err.println("获取距离传感器数据失败: " + e.getMessage());
                }
                
                // 获取激光雷达数据
                try {
                    LidarData lidarData = airSimApiClient.getLidarData("lidar", droneId);
                    if (lidarData != null && lidarData.hasValidData()) {
                        System.out.println("激光雷达数据: " + lidarData);
                        float[] boundingBox = lidarData.getBoundingBox();
                        if (boundingBox != null) {
                            System.out.printf("点云边界框: [%.2f,%.2f,%.2f] - [%.2f,%.2f,%.2f]%n",
                                            boundingBox[0], boundingBox[1], boundingBox[2],
                                            boundingBox[3], boundingBox[4], boundingBox[5]);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("获取激光雷达数据失败: " + e.getMessage());
                }
                
                // 等待1秒
                Thread.sleep(1000);
            }
            
        } catch (Exception e) {
            System.err.println("传感器数据示例执行失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            airSimApiClient.disconnect();
        }
    }
    
    /**
     * 异步操作示例
     */
    public void asyncOperationExample() {
        try {
            // 初始化连接
            airSimApiClient.initialize();
            
            if (!airSimApiClient.isConnected()) {
                System.err.println("未连接到AirSim");
                return;
            }
            
            String droneId = "drone1";
            airSimApiClient.registerVehicle(droneId, "Drone1");
            airSimApiClient.enableApiControl(droneId, true);
            airSimApiClient.armDisarm(droneId, true);
            
            System.out.println("开始异步操作示例...");
            
            // 异步起飞
            CompletableFuture<Void> takeoffFuture = airSimApiClient.takeoffAsync(droneId, 15.0f);
            
            // 在起飞的同时执行其他操作
            takeoffFuture.thenRun(() -> {
                System.out.println("起飞完成回调");
                
                try {
                    // 悬停3秒
                    Thread.sleep(3000);
                    
                    // 异步降落
                    CompletableFuture<Void> landFuture = airSimApiClient.landAsync(droneId);
                    
                    landFuture.thenRun(() -> {
                        System.out.println("降落完成回调");
                    }).exceptionally(throwable -> {
                        System.err.println("降落失败: " + throwable.getMessage());
                        return null;
                    });
                    
                    // 等待降落完成
                    landFuture.get(30, TimeUnit.SECONDS);
                    
                } catch (Exception e) {
                    System.err.println("异步操作中发生错误: " + e.getMessage());
                }
                
            }).exceptionally(throwable -> {
                System.err.println("起飞失败: " + throwable.getMessage());
                return null;
            });
            
            // 等待起飞完成
            takeoffFuture.get(30, TimeUnit.SECONDS);
            System.out.println("所有异步操作完成");
            
        } catch (Exception e) {
            System.err.println("异步操作示例执行失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            airSimApiClient.disconnect();
        }
    }
    
    /**
     * 仿真控制示例
     */
    public void simulationControlExample() {
        try {
            // 初始化连接
            airSimApiClient.initialize();
            
            if (!airSimApiClient.isConnected()) {
                System.err.println("未连接到AirSim");
                return;
            }
            
            System.out.println("仿真控制示例开始...");
            
            // 启用天气效果
            airSimApiClient.enableWeather(true);
            System.out.println("天气效果已启用");
            
            // 暂停仿真
            airSimApiClient.pauseSimulation(true);
            System.out.println("仿真已暂停");
            Thread.sleep(2000);
            
            // 恢复仿真
            airSimApiClient.pauseSimulation(false);
            System.out.println("仿真已恢复");
            
            // 继续仿真5秒
            airSimApiClient.continueForTime(5.0);
            System.out.println("仿真将继续5秒");
            Thread.sleep(6000);
            
            // 重置仿真
            airSimApiClient.resetSimulation();
            System.out.println("仿真已重置");
            
        } catch (Exception e) {
            System.err.println("仿真控制示例执行失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            airSimApiClient.disconnect();
        }
    }
    
    /**
     * 连接配置和重连示例
     */
    public void connectionManagementExample() {
        AirSimConnectionConfig config = airSimApiClient.getConnectionConfig();
        System.out.println("连接配置:");
        System.out.println("主机: " + config.getHost());
        System.out.println("端口: " + config.getPort());
        System.out.println("连接超时: " + config.getConnection().getTimeout() + "ms");
        System.out.println("最大重试次数: " + config.getConnection().getRetry().getMaxAttempts());
        
        try {
            // 尝试连接
            airSimApiClient.initialize();
            
            if (airSimApiClient.isConnected()) {
                System.out.println("连接成功");
                
                // 模拟连接断开后的重连
                System.out.println("模拟连接检查...");
                for (int i = 0; i < 5; i++) {
                    boolean connected = airSimApiClient.isConnected();
                    System.out.println("连接状态检查 " + (i + 1) + ": " + connected);
                    Thread.sleep(1000);
                }
                
                System.out.println("重连尝试次数: " + airSimApiClient.getReconnectAttempts());
                System.out.println("活跃载具: " + airSimApiClient.getActiveVehicles());
                
            } else {
                System.err.println("连接失败");
            }
            
        } catch (Exception e) {
            System.err.println("连接管理示例执行失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            airSimApiClient.disconnect();
        }
    }
    
    /**
     * 运行所有示例
     */
    public void runAllExamples() {
        System.out.println("=== AirSim集成示例开始 ===\n");
        
        System.out.println("1. 基础连接和控制示例");
        basicConnectionExample();
        
        System.out.println("\n2. 传感器数据获取示例");
        sensorDataExample();
        
        System.out.println("\n3. 异步操作示例");
        asyncOperationExample();
        
        System.out.println("\n4. 仿真控制示例");
        simulationControlExample();
        
        System.out.println("\n5. 连接管理示例");
        connectionManagementExample();
        
        System.out.println("\n=== AirSim集成示例结束 ===");
    }
} 