package com.simulation.drone.adapter.airsim.impl;

import com.simulation.drone.adapter.airsim.AirSimAdapter;
import com.simulation.drone.adapter.airsim.DroneClientInterface;
import com.simulation.drone.adapter.airsim.messages.*;
import com.simulation.drone.config.AirSimConfig;
import com.simulation.drone.model.message.control.DroneControl;
import com.simulation.drone.model.message.state.DroneState;
import lombok.extern.slf4j.Slf4j;
import org.msgpack.rpc.Client;
import org.msgpack.rpc.loop.EventLoop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AirSim适配器实现类
 * 基于msgpack-rpc协议与AirSim进行通信
 * 参照Example.java中的官方示例实现
 */
@Slf4j
@Component
public class AirSimAdapterImpl implements AirSimAdapter {
    
    @Autowired
    private AirSimConfig airSimConfig;
    
    private Client client;
    private EventLoop loop;
    private DroneClientInterface droneClient;
    private final Map<String, MultirotorState> droneStateCache = new ConcurrentHashMap<>();
    private final Map<String, Boolean> droneControlStatus = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void init() {
        try {
            // 按照Example.java的模式初始化连接
            loop = EventLoop.defaultEventLoop();
            client = new Client(airSimConfig.getHost(), airSimConfig.getPort(), loop);
            droneClient = client.proxy(DroneClientInterface.class);
            
            // 确认连接
            droneClient.confirmConnection();
            log.info("AirSim连接确认成功");
            
            // 启用API控制(参考Example.java)
            droneClient.enableApiControl(true, "");
            boolean controlEnabled = droneClient.isApiControlEnabled("");
            log.info("AirSim连接初始化成功 - {}:{}, API控制状态: {}", 
                    airSimConfig.getHost(), airSimConfig.getPort(), controlEnabled);
                    
        } catch (Exception e) {
            log.error("AirSim连接初始化失败", e);
            throw new RuntimeException("AirSim连接初始化失败", e);
        }
    }
    
    @Override
    public boolean initialize(Map<String, Object> config) {
        try {
            return droneClient.setSimulationParams(config);
        } catch (Exception e) {
            log.error("AirSim初始化失败", e);
            return false;
        }
    }
    
    @Override
    public DroneState getDroneState(String droneId) {
        try {
            // 获取多旋翼无人机的完整状态信息
            MultirotorState multirotorState = droneClient.getMultirotorState(droneId);
            
            // 获取传感器数据
            ImuData imuData = droneClient.getImuData("imu", droneId);
            DistanceSensorData distanceData = droneClient.getDistanceSensorData("distance", droneId);
            LidarData lidarData = droneClient.getLidarData("lidar", droneId);
            GpsData gpsData = droneClient.getGpsData("gps", droneId);
            BarometerData barometerData = droneClient.getBarometerData("barometer", droneId);
            
            // 转换为系统内部的DroneState格式
            DroneState state = convertToInternalDroneState(droneId, multirotorState, 
                    imuData, distanceData, lidarData, gpsData, barometerData);
            
            // 缓存状态信息
            droneStateCache.put(droneId, multirotorState);
            return state;
            
        } catch (Exception e) {
            log.error("获取无人机状态失败: {}", droneId, e);
            // 返回缓存的状态或创建默认状态
            MultirotorState cachedState = droneStateCache.get(droneId);
            if (cachedState != null) {
                return convertToInternalDroneState(droneId, cachedState, null, null, null, null, null);
            }
            return createDefaultDroneState(droneId);
        }
    }
    
    @Override
    public boolean controlDrone(String droneId, DroneControl control) {
        try {
            // 确保API控制已启用
            if (!droneControlStatus.getOrDefault(droneId, false)) {
                droneClient.enableApiControl(true, droneId);
                droneControlStatus.put(droneId, true);
            }
            
            // 如果需要启动无人机
            if (control.isArmed()) {
                boolean armResult = droneClient.armDisarm(true, droneId);
                if (!armResult) {
                    log.warn("无人机启动失败: {}", droneId);
                    return false;
                }
            }
            
            // 使用速度控制模式
            YawMode yawMode = YawMode.createYawRateMode(control.getYaw());
            
            return droneClient.moveByVelocityAsync(
                    control.getRoll(),    // X方向速度
                    control.getPitch(),   // Y方向速度
                    control.getThrottle(), // Z方向速度
                    0.1f,                 // 持续时间(秒)
                    0,                    // 驱动类型(默认)
                    yawMode,              // 偏航模式
                    droneId
            );
            
        } catch (Exception e) {
            log.error("控制无人机失败: {}", droneId, e);
            return false;
        }
    }
    
    /**
     * 启动无人机(起飞)
     * @param droneId 无人机ID
     * @param altitude 目标高度(米，负值)
     * @return 是否成功
     */
    public boolean takeoff(String droneId, float altitude) {
        try {
            droneClient.enableApiControl(true, droneId);
            droneClient.armDisarm(true, droneId);
            return droneClient.takeoffAsync(10.0f, droneId);
        } catch (Exception e) {
            log.error("无人机起飞失败: {}", droneId, e);
            return false;
        }
    }
    
    /**
     * 降落无人机
     * @param droneId 无人机ID
     * @return 是否成功
     */
    public boolean land(String droneId) {
        try {
            return droneClient.landAsync(10.0f, droneId);
        } catch (Exception e) {
            log.error("无人机降落失败: {}", droneId, e);
            return false;
        }
    }
    
    /**
     * 移动到指定位置
     * @param droneId 无人机ID
     * @param x X坐标
     * @param y Y坐标
     * @param z Z坐标(负值向上)
     * @param velocity 移动速度
     * @return 是否成功
     */
    public boolean moveToPosition(String droneId, float x, float y, float z, float velocity) {
        try {
            YawMode yawMode = YawMode.createYawMode(0); // 保持当前偏航角
            return droneClient.moveToPositionAsync(x, y, z, velocity, 30.0f, 0, yawMode, droneId);
        } catch (Exception e) {
            log.error("无人机移动到位置失败: {} -> ({}, {}, {})", droneId, x, y, z, e);
            return false;
        }
    }
    
    @Override
    public byte[] getImageData(String cameraId) {
        try {
            // 创建图像请求
            ImageRequest[] requests = {
                new ImageRequest(cameraId, ImageRequest.SCENE, false, true)
            };
            
            // 获取图像响应
            ImageResponse[] responses = droneClient.simGetImages(requests, "");
            
            if (responses != null && responses.length > 0) {
                return responses[0].image_data_uint8;
            }
            
            return null;
        } catch (Exception e) {
            log.error("获取图像数据失败: {}", cameraId, e);
            return null;
        }
    }
    
    /**
     * 获取多种类型的图像
     * @param cameraName 相机名称
     * @param imageTypes 图像类型数组
     * @param vehicleName 载具名称
     * @return 图像响应数组
     */
    public ImageResponse[] getImages(String cameraName, int[] imageTypes, String vehicleName) {
        try {
            ImageRequest[] requests = new ImageRequest[imageTypes.length];
            for (int i = 0; i < imageTypes.length; i++) {
                requests[i] = new ImageRequest(cameraName, imageTypes[i], false, true);
            }
            
            return droneClient.simGetImages(requests, vehicleName);
        } catch (Exception e) {
            log.error("获取多种图像失败: {}", cameraName, e);
            return null;
        }
    }
    
    @Override
    public Map<String, Object> getEnvironmentInfo() {
        try {
            return droneClient.getEnvironmentInfo();
        } catch (Exception e) {
            log.error("获取环境信息失败", e);
            return null;
        }
    }
    
    @Override
    public Map<String, Object> getSimulationState() {
        try {
            return droneClient.getSimulationState();
        } catch (Exception e) {
            log.error("获取仿真状态失败", e);
            return null;
        }
    }
    
    @Override
    @PreDestroy
    public void close() {
        try {
            // 禁用所有无人机的API控制
            for (String droneId : droneControlStatus.keySet()) {
                droneClient.enableApiControl(false, droneId);
            }
            droneControlStatus.clear();
            
            if (client != null) {
                client.close();
            }
            if (loop != null) {
                loop.shutdown();
            }
            log.info("AirSim连接已关闭");
        } catch (Exception e) {
            log.error("关闭AirSim连接时发生错误", e);
        }
    }
    
    // ======================== 数据转换方法 ========================
    
    /**
     * 将AirSim的MultirotorState转换为系统内部的DroneState
     */
    private DroneState convertToInternalDroneState(String droneId, MultirotorState multirotorState,
                                                  ImuData imuData, DistanceSensorData distanceData,
                                                  LidarData lidarData, GpsData gpsData, BarometerData barometerData) {
        DroneState state = new DroneState();
        state.setDroneId(droneId);
        state.setTimestamp(System.currentTimeMillis());
        
        if (multirotorState != null) {
            // 设置位置信息(从运动学状态)
            if (multirotorState.kinematics_true != null) {
                state.setPosition(convertVector3r(multirotorState.kinematics_true.position));
                state.setVelocity(convertVector3r(multirotorState.kinematics_true.linear_velocity));
                state.setOrientation(convertQuaternion(multirotorState.kinematics_true.orientation));
                state.setAngularVelocity(convertVector3r(multirotorState.kinematics_true.angular_velocity));
            }
            
            // 设置碰撞信息
            state.setCollided(multirotorState.collision.has_collided);
            
            // 设置着陆状态
            state.setLanded(multirotorState.landed_state);
            
            // 设置准备状态
            state.setReady(multirotorState.ready);
            state.setCanArm(multirotorState.can_arm);
        }
        
        // 设置传感器数据
        if (imuData != null) {
            state.getImuData().put("imu", convertImuData(imuData));
        }
        if (distanceData != null) {
            state.getDistanceSensors().put("distance", convertDistanceSensorData(distanceData));
        }
        if (lidarData != null) {
            state.getLidarSensors().put("lidar", convertLidarData(lidarData));
        }
        if (gpsData != null) {
            state.setGpsData(convertGpsData(gpsData));
        }
        if (barometerData != null) {
            state.setBarometerData(convertBarometerData(barometerData));
        }
        
        return state;
    }
    
    /**
     * 创建默认的无人机状态
     */
    private DroneState createDefaultDroneState(String droneId) {
        DroneState state = new DroneState();
        state.setDroneId(droneId);
        state.setTimestamp(System.currentTimeMillis());
        // 设置默认值...
        return state;
    }
    
    // ======================== 类型转换辅助方法 ========================
    
    private com.simulation.drone.model.message.geometry.Vector3 convertVector3r(Vector3r vector3r) {
        if (vector3r == null) return new com.simulation.drone.model.message.geometry.Vector3();
        return new com.simulation.drone.model.message.geometry.Vector3(
            vector3r.x_val, vector3r.y_val, vector3r.z_val);
    }
    
    private com.simulation.drone.model.message.geometry.Quaternion convertQuaternion(Quaternionr quaternionr) {
        if (quaternionr == null) return new com.simulation.drone.model.message.geometry.Quaternion();
        return new com.simulation.drone.model.message.geometry.Quaternion(
            quaternionr.w_val, quaternionr.x_val, quaternionr.y_val, quaternionr.z_val);
    }
    
    private com.simulation.drone.model.message.sensor.ImuData convertImuData(ImuData airSimImu) {
        com.simulation.drone.model.message.sensor.ImuData imu = 
            new com.simulation.drone.model.message.sensor.ImuData();
        if (airSimImu != null) {
            imu.setTimestamp(airSimImu.time_stamp);
            imu.setLinearAcceleration(convertVector3r(airSimImu.linear_acceleration));
            imu.setAngularVelocity(convertVector3r(airSimImu.angular_velocity));
            imu.setOrientation(convertQuaternion(airSimImu.orientation));
        }
        return imu;
    }
    
    private com.simulation.drone.model.message.sensor.DistanceSensorData convertDistanceSensorData(
            DistanceSensorData airSimDistance) {
        com.simulation.drone.model.message.sensor.DistanceSensorData distance = 
            new com.simulation.drone.model.message.sensor.DistanceSensorData();
        if (airSimDistance != null) {
            distance.setTimestamp(airSimDistance.time_stamp);
            distance.setDistance(airSimDistance.distance);
            distance.setMinDistance(airSimDistance.min_distance);
            distance.setMaxDistance(airSimDistance.max_distance);
        }
        return distance;
    }
    
    private com.simulation.drone.model.message.sensor.LidarData convertLidarData(LidarData airSimLidar) {
        com.simulation.drone.model.message.sensor.LidarData lidar = 
            new com.simulation.drone.model.message.sensor.LidarData();
        if (airSimLidar != null) {
            lidar.setTimestamp(airSimLidar.time_stamp);
            lidar.setPointCloud(airSimLidar.point_cloud);
            lidar.setPose(convertPose(airSimLidar.pose));
        }
        return lidar;
    }
    
    private com.simulation.drone.model.message.sensor.GpsData convertGpsData(GpsData airSimGps) {
        com.simulation.drone.model.message.sensor.GpsData gps = 
            new com.simulation.drone.model.message.sensor.GpsData();
        if (airSimGps != null) {
            gps.setTimestamp(airSimGps.time_stamp);
            gps.setLatitude(airSimGps.gnss.latitude);
            gps.setLongitude(airSimGps.gnss.longitude);
            gps.setAltitude(airSimGps.gnss.altitude);
            gps.setVelocity(convertVector3r(airSimGps.velocity));
        }
        return gps;
    }
    
    private com.simulation.drone.model.message.sensor.BarometerData convertBarometerData(
            BarometerData airSimBarometer) {
        com.simulation.drone.model.message.sensor.BarometerData barometer = 
            new com.simulation.drone.model.message.sensor.BarometerData();
        if (airSimBarometer != null) {
            barometer.setTimestamp(airSimBarometer.time_stamp);
            barometer.setAltitude(airSimBarometer.altitude);
            barometer.setPressure(airSimBarometer.pressure);
        }
        return barometer;
    }
    
    private com.simulation.drone.model.message.geometry.Pose convertPose(Pose airSimPose) {
        if (airSimPose == null) return new com.simulation.drone.model.message.geometry.Pose();
        com.simulation.drone.model.message.geometry.Pose pose = 
            new com.simulation.drone.model.message.geometry.Pose();
        pose.setPosition(convertVector3r(airSimPose.position));
        pose.setOrientation(convertQuaternion(airSimPose.orientation));
        return pose;
    }
    
    // ======================== 缺失的接口方法实现 ========================
    
    @Override
    public Map<String, Double> getDronePosition(String droneId) {
        try {
            Vector3r position = droneClient.getPosition(droneId);
            Map<String, Double> posMap = new HashMap<>();
            posMap.put("x", (double) position.x_val);
            posMap.put("y", (double) position.y_val);
            posMap.put("z", (double) position.z_val);
            return posMap;
        } catch (Exception e) {
            log.error("获取无人机位置失败: {}", droneId, e);
            return new HashMap<>();
        }
    }
    
    @Override
    public Map<String, Double> getDroneOrientation(String droneId) {
        try {
            Quaternionr orientation = droneClient.getOrientation(droneId);
            Map<String, Double> oriMap = new HashMap<>();
            oriMap.put("w", (double) orientation.w_val);
            oriMap.put("x", (double) orientation.x_val);
            oriMap.put("y", (double) orientation.y_val);
            oriMap.put("z", (double) orientation.z_val);
            return oriMap;
        } catch (Exception e) {
            log.error("获取无人机姿态失败: {}", droneId, e);
            return new HashMap<>();
        }
    }
    
    @Override
    public Map<String, Double> getDroneVelocity(String droneId) {
        try {
            Vector3r velocity = droneClient.getVelocity(droneId);
            Map<String, Double> velMap = new HashMap<>();
            velMap.put("vx", (double) velocity.x_val);
            velMap.put("vy", (double) velocity.y_val);
            velMap.put("vz", (double) velocity.z_val);
            return velMap;
        } catch (Exception e) {
            log.error("获取无人机速度失败: {}", droneId, e);
            return new HashMap<>();
        }
    }
    
    @Override
    public List<Map<String, Object>> getAllDrones() {
        // AirSim没有直接获取所有无人机的API，需要通过配置文件或预定义列表
        List<Map<String, Object>> drones = new ArrayList<>();
        for (String droneId : droneStateCache.keySet()) {
            Map<String, Object> droneInfo = new HashMap<>();
            droneInfo.put("id", droneId);
            droneInfo.put("state", droneStateCache.get(droneId));
            drones.add(droneInfo);
        }
        return drones;
    }
    
    @Override
    public Map<String, Object> getSensorData(String sensorId) {
        try {
            // 根据传感器ID类型获取相应数据
            if (sensorId.startsWith("imu")) {
                return Map.of("data", droneClient.getImuData(sensorId, ""));
            } else if (sensorId.startsWith("distance")) {
                return Map.of("data", droneClient.getDistanceSensorData(sensorId, ""));
            } else if (sensorId.startsWith("lidar")) {
                return Map.of("data", droneClient.getLidarData(sensorId, ""));
            } else if (sensorId.startsWith("gps")) {
                return Map.of("data", droneClient.getGpsData(sensorId, ""));
            }
            return new HashMap<>();
        } catch (Exception e) {
            log.error("获取传感器数据失败: {}", sensorId, e);
            return new HashMap<>();
        }
    }
} 