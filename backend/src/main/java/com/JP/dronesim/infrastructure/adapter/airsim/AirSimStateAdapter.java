package com.JP.dronesim.infrastructure.adapter.airsim;

import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.common.valueobjects.Velocity;
import com.JP.dronesim.domain.common.valueobjects.Orientation;
import com.JP.dronesim.domain.airspace.model.EnvironmentParameters;
import com.JP.dronesim.infrastructure.adapter.airsim.client.DroneClientInterface;
import com.JP.dronesim.infrastructure.adapter.airsim.client.SensorClientInterface;
import com.JP.dronesim.infrastructure.adapter.airsim.config.AirSimConnectionConfig;
import com.JP.dronesim.infrastructure.external.airsim.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AirSim状态适配器
 * 负责与AirSim仿真环境同步实体状态数据
 * 获取UAV位置、姿态、速度等实时信息
 * 
 * @author JP Team
 * @version 1.0
 */
@Component
public class AirSimStateAdapter {
    
    @Autowired
    private DroneClientInterface droneClient;
    
    @Autowired
    private SensorClientInterface sensorClient;
    
    @Autowired
    private AirSimConnectionConfig connectionConfig;
    
    /**
     * 已部署的UAV映射 (模拟器内ID -> 领域模型ID)
     */
    private final Map<String, String> deployedUAVs = new ConcurrentHashMap<>();
    
    /**
     * 连接状态
     */
    private volatile boolean connected = false;
    
    /**
     * 初始化与AirSim的连接
     * 
     * @return 是否连接成功
     */
    public boolean initializeConnection() {
        try {
            // 建立连接
            droneClient.initialize(connectionConfig.getHost(), connectionConfig.getPort());
            sensorClient.initialize(connectionConfig.getHost(), connectionConfig.getPort());
            
            // 确认连接
            droneClient.confirmConnection();
            
            this.connected = true;
            return true;
            
        } catch (Exception e) {
            this.connected = false;
            System.err.println("连接AirSim失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 断开与AirSim的连接
     */
    public void shutdown() {
        try {
            if (connected) {
                droneClient.shutdown();
                sensorClient.shutdown();
            }
        } catch (Exception e) {
            System.err.println("关闭AirSim连接时出错: " + e.getMessage());
        } finally {
            this.connected = false;
            deployedUAVs.clear();
        }
    }
    
    /**
     * 部署UAV到AirSim环境
     * 
     * @param uavId 领域模型中的UAV ID
     * @param initialPosition 初始位置
     * @return 是否部署成功
     */
    public boolean deployUAV(String uavId, Position initialPosition) {
        if (!connected) {
            System.err.println("未连接到AirSim");
            return false;
        }
        
        try {
            String vehicleName = "UAV_" + uavId;
            
            // 设置UAV位置
            droneClient.simSetVehiclePose(vehicleName, 
                                        initialPosition.getX(), 
                                        initialPosition.getY(), 
                                        initialPosition.getZ(),
                                        0, 0, 0, 1); // 默认姿态（四元数）
            
            // 启用API控制
            droneClient.enableApiControl(true, vehicleName);
            
            // 记录部署的UAV
            deployedUAVs.put(vehicleName, uavId);
            
            return true;
            
        } catch (Exception e) {
            System.err.println("部署UAV失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 移除UAV从AirSim环境
     * 
     * @param uavId 领域模型中的UAV ID
     * @return 是否移除成功
     */
    public boolean removeUAV(String uavId) {
        if (!connected) {
            return false;
        }
        
        try {
            String vehicleName = getVehicleName(uavId);
            if (vehicleName != null) {
                // 禁用API控制
                droneClient.enableApiControl(false, vehicleName);
                
                // 从映射中移除
                deployedUAVs.remove(vehicleName);
            }
            
            return true;
            
        } catch (Exception e) {
            System.err.println("移除UAV失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 从AirSim获取UAV的当前状态
     * 
     * @param uavId 领域模型中的UAV ID
     * @return UAV状态信息，如果获取失败返回null
     */
    public UAVStateInfo getUAVState(String uavId) {
        if (!connected) {
            return null;
        }
        
        try {
            String vehicleName = getVehicleName(uavId);
            if (vehicleName == null) {
                return null;
            }
            
            // 获取运动学状态
            KinematicsState kinematics = droneClient.getMultirotorState(vehicleName);
            
            // 获取位置信息
            Pose pose = droneClient.simGetVehiclePose(vehicleName);
            
            // 转换为领域模型对象
            Position position = new Position(
                pose.getPosition().getX(),
                pose.getPosition().getY(),
                pose.getPosition().getZ()
            );
            
            Velocity velocity = new Velocity(
                kinematics.getLinearVelocity().getX(),
                kinematics.getLinearVelocity().getY(),
                kinematics.getLinearVelocity().getZ()
            );
            
            Orientation orientation = new Orientation(
                pose.getOrientation().getX(),
                pose.getOrientation().getY(),
                pose.getOrientation().getZ(),
                pose.getOrientation().getW()
            );
            
            return new UAVStateInfo(uavId, position, velocity, orientation, kinematics.getLanded());
            
        } catch (Exception e) {
            System.err.println("获取UAV状态失败: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 设置UAV在AirSim中的目标位置
     * 
     * @param uavId UAV ID
     * @param targetPosition 目标位置
     * @param speed 移动速度
     * @return 是否设置成功
     */
    public boolean moveUAVTo(String uavId, Position targetPosition, double speed) {
        if (!connected) {
            return false;
        }
        
        try {
            String vehicleName = getVehicleName(uavId);
            if (vehicleName == null) {
                return false;
            }
            
            // 发送移动指令到AirSim
            droneClient.moveToPositionAsync(vehicleName, 
                                          targetPosition.getX(),
                                          targetPosition.getY(), 
                                          targetPosition.getZ(),
                                          speed);
            
            return true;
            
        } catch (Exception e) {
            System.err.println("移动UAV失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 设置AirSim环境参数
     * 
     * @param envParams 环境参数
     * @return 是否设置成功
     */
    public boolean setEnvironmentParameters(EnvironmentParameters envParams) {
        if (!connected) {
            return false;
        }
        
        try {
            // 设置风力条件
            droneClient.simSetWind(envParams.getWindSpeed(), envParams.getWindDirection());
            
            // 设置天气条件
            // 注意：AirSim可能不支持所有环境参数，这里做适配
            double weatherIntensity = Math.min(1.0, envParams.getPrecipitationRate() / 10.0);
            if (weatherIntensity > 0.1) {
                droneClient.simSetWeatherParameter("Rain", weatherIntensity);
            }
            
            // 设置光照条件
            if (envParams.isNightTime()) {
                droneClient.simSetTimeOfDay(true, "2023-01-01 22:00:00", true, 1.0);
            } else {
                droneClient.simSetTimeOfDay(false, "2023-01-01 12:00:00", true, envParams.getLightIntensity());
            }
            
            return true;
            
        } catch (Exception e) {
            System.err.println("设置环境参数失败: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 获取传感器数据
     * 
     * @param uavId UAV ID
     * @param sensorType 传感器类型
     * @return 传感器数据
     */
    public Object getSensorData(String uavId, String sensorType) {
        if (!connected) {
            return null;
        }
        
        try {
            String vehicleName = getVehicleName(uavId);
            if (vehicleName == null) {
                return null;
            }
            
            switch (sensorType.toLowerCase()) {
                case "lidar":
                    return sensorClient.getLidarData("Lidar", vehicleName);
                case "imu":
                    return sensorClient.getImuData("Imu", vehicleName);
                case "distance":
                    return sensorClient.getDistanceSensorData("Distance", vehicleName);
                default:
                    return null;
            }
            
        } catch (Exception e) {
            System.err.println("获取传感器数据失败: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 检查连接状态
     * 
     * @return 是否已连接
     */
    public boolean isConnected() {
        return connected;
    }
    
    /**
     * 获取已部署的UAV数量
     * 
     * @return UAV数量
     */
    public int getDeployedUAVCount() {
        return deployedUAVs.size();
    }
    
    /**
     * 根据UAV ID获取AirSim中的vehicle name
     * 
     * @param uavId UAV ID
     * @return vehicle name，如果没找到返回null
     */
    private String getVehicleName(String uavId) {
        for (Map.Entry<String, String> entry : deployedUAVs.entrySet()) {
            if (entry.getValue().equals(uavId)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    /**
     * UAV状态信息封装类
     */
    public static class UAVStateInfo {
        private final String uavId;
        private final Position position;
        private final Velocity velocity;
        private final Orientation orientation;
        private final boolean isLanded;
        
        public UAVStateInfo(String uavId, Position position, Velocity velocity, 
                           Orientation orientation, boolean isLanded) {
            this.uavId = uavId;
            this.position = position;
            this.velocity = velocity;
            this.orientation = orientation;
            this.isLanded = isLanded;
        }
        
        // Getters
        public String getUavId() { return uavId; }
        public Position getPosition() { return position; }
        public Velocity getVelocity() { return velocity; }
        public Orientation getOrientation() { return orientation; }
        public boolean isLanded() { return isLanded; }
    }
} 
