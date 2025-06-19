package com.simulation.drone.adapter.airsim.impl;

import com.simulation.drone.adapter.airsim.AirSimAdapter;
import com.simulation.drone.adapter.airsim.DroneClientInterface;
import com.simulation.drone.config.AirSimConfig;
import com.simulation.drone.model.message.control.DroneControl;
import com.simulation.drone.model.message.state.DroneState;
import com.simulation.drone.model.message.state.KinematicsState;
import com.simulation.drone.model.message.sensor.ImuData;
import com.simulation.drone.model.message.sensor.DistanceSensorData;
import com.simulation.drone.model.message.sensor.LidarData;
import com.simulation.drone.model.message.factory.MessageFactory;
import lombok.extern.slf4j.Slf4j;
import org.msgpack.rpc.Client;
import org.msgpack.rpc.loop.EventLoop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AirSim适配器实现类
 */
@Slf4j
@Component
public class AirSimAdapterImpl implements AirSimAdapter {
    
    @Autowired
    private AirSimConfig airSimConfig;
    
    @Autowired
    private MessageFactory messageFactory;
    
    private Client client;
    private EventLoop loop;
    private DroneClientInterface droneClient;
    private final Map<String, DroneState> droneCache = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void init() {
        try {
            loop = EventLoop.defaultEventLoop();
            client = new Client(airSimConfig.getHost(), airSimConfig.getPort(), loop);
            droneClient = client.proxy(DroneClientInterface.class);
            
            // 启用API控制
            droneClient.enableApiControl(true, "");
            log.info("AirSim连接初始化成功 - {}:{}", airSimConfig.getHost(), airSimConfig.getPort());
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
            KinematicsState kinematics = droneClient.simGetGroundTruthKinematics(droneId);
            ImuData imuData = droneClient.getImuData("imu", droneId);
            DistanceSensorData distanceData = droneClient.getDistanceSensorData("distance", droneId);
            LidarData lidarData = droneClient.getLidarData("lidar", droneId);
            
            DroneState state = messageFactory.createMessage(DroneState.class);
            state.setDroneId(droneId);
            state.setKinematics(kinematics);
            state.getImuData().put("imu", imuData);
            state.getDistanceSensors().put("distance", distanceData);
            state.getLidarSensors().put("lidar", lidarData);
            
            droneCache.put(droneId, state);
            return state;
        } catch (Exception e) {
            log.error("获取无人机状态失败: {}", droneId, e);
            return droneCache.get(droneId);
        }
    }
    
    @Override
    public boolean controlDrone(String droneId, DroneControl control) {
        try {
            Map<String, Object> command = new HashMap<>();
            command.put("throttle", control.getThrottle());
            command.put("roll", control.getRoll());
            command.put("pitch", control.getPitch());
            command.put("yaw", control.getYaw());
            command.put("isArmed", control.isArmed());
            
            return droneClient.setMultirotorControls(command, droneId);
        } catch (Exception e) {
            log.error("控制无人机失败: {}", droneId, e);
            return false;
        }
    }
    
    @Override
    public byte[] getImageData(String cameraId) {
        try {
            return droneClient.getImageData(cameraId);
        } catch (Exception e) {
            log.error("获取图像数据失败: {}", cameraId, e);
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
        if (client != null) {
            client.close();
        }
        if (loop != null) {
            loop.shutdown();
        }
        log.info("AirSim连接已关闭");
    }
} 