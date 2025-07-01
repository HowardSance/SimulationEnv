package com.simulation.drone.infrastructure.adapter.airsim;

import com.simulation.drone.domain.port.SimulationEnginePort;
import com.simulation.drone.domain.port.DeviceControlPort;
import com.simulation.drone.domain.port.SensorDataPort;
import com.simulation.drone.domain.device.model.Drone;
import com.simulation.drone.domain.device.valueobject.Position;
import com.simulation.drone.domain.device.valueobject.Orientation;
import com.simulation.drone.infrastructure.adapter.airsim.client.DroneClientInterface;
import com.simulation.drone.infrastructure.adapter.airsim.client.SensorClientInterface;
import com.simulation.drone.infrastructure.adapter.airsim.config.AirSimConnectionConfig;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;

/**
 * AirSim适配器
 * 实现仿真引擎、设备控制和传感器数据端口接口
 * 封装对AirSim的具体调用
 */
@Component
public class AirSimAdapter implements SimulationEnginePort, DeviceControlPort, SensorDataPort {

    private final DroneClientInterface droneClient;
    private final SensorClientInterface sensorClient;
    private final AirSimConnectionConfig connectionConfig;

    /**
     * 已部署的设备映射
     */
    private final Map<String, String> deployedDevices = new ConcurrentHashMap<>();

    /**
     * 连接状态
     */
    private volatile boolean connected = false;

    public AirSimAdapter(DroneClientInterface droneClient, 
                        SensorClientInterface sensorClient,
                        AirSimConnectionConfig connectionConfig) {
        this.droneClient = droneClient;
        this.sensorClient = sensorClient;
        this.connectionConfig = connectionConfig;
    }

    // ==================== SimulationEnginePort 实现 ====================

    @Override
    public void initializeConnection(String host, int port) {
        try {
            // 初始化客户端连接
            droneClient.initialize(host, port);
            sensorClient.initialize(host, port);

            // 确认连接
            droneClient.confirmConnection();

            this.connected = true;

            // 启用API控制
            droneClient.enableApiControl(true);

        } catch (Exception e) {
            this.connected = false;
            throw new RuntimeException("连接AirSim失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void shutdown() {
        try {
            if (connected) {
                // 禁用API控制
                droneClient.enableApiControl(false);

                // 关闭连接
                droneClient.shutdown();
                sensorClient.shutdown();
            }
        } catch (Exception e) {
            // 记录错误但不抛出异常
            System.err.println("关闭AirSim连接时出错: " + e.getMessage());
        } finally {
            this.connected = false;
            deployedDevices.clear();
        }
    }

    @Override
    public void resetSimulation() {
        if (!connected) {
            throw new IllegalStateException("未连接到AirSim");
        }

        try {
            droneClient.reset();
            deployedDevices.clear();
        } catch (Exception e) {
            throw new RuntimeException("重置仿真环境失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void setTimeScale(double scaleFactor) {
        if (!connected) {
            throw new IllegalStateException("未连接到AirSim");
        }

        // AirSim可能不直接支持时间缩放，这里记录请求
        System.out.println("设置时间缩放因子: " + scaleFactor);
    }

    @Override
    public void pauseSimulation() {
        if (!connected) {
            throw new IllegalStateException("未连接到AirSim");
        }

        try {
            droneClient.simPause(true);
        } catch (Exception e) {
            throw new RuntimeException("暂停仿真失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void resumeSimulation() {
        if (!connected) {
            throw new IllegalStateException("未连接到AirSim");
        }

        try {
            droneClient.simPause(false);
        } catch (Exception e) {
            throw new RuntimeException("恢复仿真失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean isConnected() {
        return connected;
    }
    
    @Override
    public void setWeatherConditions(double windSpeed, double windDirection, double temperature) {
        if (!connected) {
            throw new IllegalStateException("未连接到AirSim");
        }
        
        try {
            // 设置风力条件
            droneClient.simSetWind(windSpeed, windDirection);
            
            // 记录温度设置（AirSim可能不直接支持温度设置）
            System.out.printf("设置天气条件 - 风速: %.2f m/s, 风向: %.1f°, 温度: %.1f°C%n", 
                            windSpeed, windDirection, temperature);
            
        } catch (Exception e) {
            throw new RuntimeException("设置天气条件失败: " + e.getMessage(), e);
        }
    }
    
    // ==================== DeviceControlPort 实现 ====================
    
    @Override
    public boolean deployDrone(Drone drone, Position initialPosition) {
        if (!connected) {
            throw new IllegalStateException("未连接到AirSim");
        }
        
        try {
            String vehicleName = drone.getDroneId();
            
            // 设置无人机位置
            droneClient.simSetVehiclePose(vehicleName, 
                                        initialPosition.getX(), 
                                        initialPosition.getY(), 
                                        initialPosition.getZ(),
                                        0, 0, 0, 1); // 默认姿态
            
            // 启用API控制
            droneClient.enableApiControl(true, vehicleName);
            
            // 记录部署的设备
            deployedDevices.put(drone.getDroneId(), vehicleName);
            
            return true;
            
        } catch (Exception e) {
            System.err.println("部署无人机失败: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean deployDetectionDevice(String deviceId, String deviceType, Position position) {
        if (!connected) {
            throw new IllegalStateException("未连接到AirSim");
        }
        
        try {
            // 在AirSim中创建静态对象代表检测设备
            // 这里可能需要调用AirSim的场景编辑API
            String objectName = deviceType + "_" + deviceId;
            
            // 记录设备位置（AirSim中静态物体的创建可能需要特殊API）
            System.out.printf("部署检测设备 %s 在位置 (%.2f, %.2f, %.2f)%n", 
                            deviceId, position.getX(), position.getY(), position.getZ());
            
            deployedDevices.put(deviceId, objectName);
            
            return true;
            
        } catch (Exception e) {
            System.err.println("部署检测设备失败: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public void takeoff(String droneId, double targetAltitude) {
        String vehicleName = getVehicleName(droneId);
        
        try {
            droneClient.armDisarm(true, vehicleName);
            droneClient.takeoffAsync(targetAltitude, vehicleName);
        } catch (Exception e) {
            throw new RuntimeException("无人机起飞失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void land(String droneId) {
        String vehicleName = getVehicleName(droneId);
        
        try {
            droneClient.landAsync(vehicleName);
        } catch (Exception e) {
            throw new RuntimeException("无人机降落失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void hover(String droneId) {
        String vehicleName = getVehicleName(droneId);
        
        try {
            droneClient.hoverAsync(vehicleName);
        } catch (Exception e) {
            throw new RuntimeException("无人机悬停失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void moveTo(String droneId, Position targetPosition, double speed) {
        String vehicleName = getVehicleName(droneId);
        
        try {
            droneClient.moveToPositionAsync(
                targetPosition.getX(), 
                targetPosition.getY(), 
                targetPosition.getZ(),
                speed, 
                vehicleName
            );
        } catch (Exception e) {
            throw new RuntimeException("无人机移动失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void setOrientation(String droneId, Orientation orientation) {
        String vehicleName = getVehicleName(droneId);
        
        try {
            // 转换欧拉角到四元数
            double[] quaternion = orientation.toQuaternion();
            
            droneClient.rotateToYawAsync(orientation.getYaw(), vehicleName);
        } catch (Exception e) {
            throw new RuntimeException("设置无人机姿态失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Position getCurrentPosition(String droneId) {
        String vehicleName = getVehicleName(droneId);
        
        try {
            double[] pose = droneClient.simGetVehiclePose(vehicleName);
            return new Position(pose[0], pose[1], pose[2]);
        } catch (Exception e) {
            throw new RuntimeException("获取无人机位置失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Orientation getCurrentOrientation(String droneId) {
        String vehicleName = getVehicleName(droneId);
        
        try {
            double[] pose = droneClient.simGetVehiclePose(vehicleName);
            // pose数组包含：[x, y, z, qw, qx, qy, qz]
            return Orientation.fromQuaternion(pose[3], pose[4], pose[5], pose[6]);
        } catch (Exception e) {
            throw new RuntimeException("获取无人机姿态失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public DroneStateInfo getDroneState(String droneId) {
        String vehicleName = getVehicleName(droneId);
        
        try {
            // 获取多旋翼状态
            Map<String, Object> state = droneClient.getMultirotorState(vehicleName);
            
            double batteryLevel = (Double) state.getOrDefault("battery", 100.0);
            double speed = (Double) state.getOrDefault("speed", 0.0);
            boolean isArmed = (Boolean) state.getOrDefault("armed", false);
            boolean isFlying = (Boolean) state.getOrDefault("flying", false);
            
            return new DroneStateInfo(batteryLevel, speed, isArmed, isFlying);
            
        } catch (Exception e) {
            throw new RuntimeException("获取无人机状态失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void removeDevice(String deviceId) {
        if (!connected) {
            throw new IllegalStateException("未连接到AirSim");
        }
        
        try {
            String vehicleName = deployedDevices.get(deviceId);
            if (vehicleName != null) {
                // 对于无人机，先降落再移除
                if (deployedDevices.containsKey(deviceId)) {
                    try {
                        land(deviceId);
                    } catch (Exception e) {
                        // 忽略降落失败
                    }
                }
                
                deployedDevices.remove(deviceId);
            }
        } catch (Exception e) {
            throw new RuntimeException("移除设备失败: " + e.getMessage(), e);
        }
    }
    
    // ==================== SensorDataPort 实现 ====================
    
    @Override
    public LidarData getLidarData(String sensorId, String vehicleId) {
        String vehicleName = getVehicleName(vehicleId);
        
        try {
            List<Point3D> pointCloud = sensorClient.getLidarData(sensorId, vehicleName);
            long timestamp = System.currentTimeMillis();
            
            return new LidarData(pointCloud, timestamp);
            
        } catch (Exception e) {
            throw new RuntimeException("获取激光雷达数据失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public ImuData getImuData(String sensorId, String vehicleId) {
        String vehicleName = getVehicleName(vehicleId);
        
        try {
            Map<String, Object> imuData = sensorClient.getImuData(sensorId, vehicleName);
            
            Vector3D acceleration = (Vector3D) imuData.get("acceleration");
            Vector3D angularVelocity = (Vector3D) imuData.get("angularVelocity");
            Vector3D magneticField = (Vector3D) imuData.get("magneticField");
            Quaternion orientation = (Quaternion) imuData.get("orientation");
            long timestamp = System.currentTimeMillis();
            
            return new ImuData(acceleration, angularVelocity, magneticField, orientation, timestamp);
            
        } catch (Exception e) {
            throw new RuntimeException("获取IMU数据失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public DistanceSensorData getDistanceSensorData(String sensorId, String vehicleId) {
        String vehicleName = getVehicleName(vehicleId);
        
        try {
            Map<String, Object> distanceData = sensorClient.getDistanceSensorData(sensorId, vehicleName);
            
            double distance = (Double) distanceData.get("distance");
            double minDistance = (Double) distanceData.get("minDistance");
            double maxDistance = (Double) distanceData.get("maxDistance");
            long timestamp = System.currentTimeMillis();
            
            return new DistanceSensorData(distance, minDistance, maxDistance, timestamp);
            
        } catch (Exception e) {
            throw new RuntimeException("获取距离传感器数据失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public CameraData getCameraData(String cameraId, String vehicleId) {
        String vehicleName = getVehicleName(vehicleId);
        
        try {
            Map<String, Object> imageData = sensorClient.simGetImage(cameraId, vehicleName);
            
            byte[] imageBytes = (byte[]) imageData.get("imageData");
            int width = (Integer) imageData.get("width");
            int height = (Integer) imageData.get("height");
            String format = (String) imageData.get("format");
            long timestamp = System.currentTimeMillis();
            
            return new CameraData(imageBytes, width, height, format, timestamp);
            
        } catch (Exception e) {
            throw new RuntimeException("获取摄像头数据失败: " + e.getMessage(), e);
        }
    }
    
    // ==================== 私有辅助方法 ====================
    
    /**
     * 获取设备在AirSim中的车辆名称
     * @param deviceId 设备ID
     * @return 车辆名称
     */
    private String getVehicleName(String deviceId) {
        String vehicleName = deployedDevices.get(deviceId);
        if (vehicleName == null) {
            throw new IllegalArgumentException("设备未部署: " + deviceId);
        }
        return vehicleName;
    }
    
    /**
     * 检查连接状态
     */
    private void checkConnection() {
        if (!connected) {
            throw new IllegalStateException("未连接到AirSim");
        }
    }
    
    /**
     * 获取连接配置
     * @return 连接配置
     */
    public AirSimConnectionConfig getConnectionConfig() {
        return connectionConfig;
    }
    
    /**
     * 获取已部署设备列表
     * @return 设备映射
     */
    public Map<String, String> getDeployedDevices() {
        return new ConcurrentHashMap<>(deployedDevices);
    }
}