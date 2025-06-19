package com.simulation.drone.engine;

import com.simulation.drone.adapter.airsim.AirSimAdapter;
import com.simulation.drone.entity.simulation.DroneEntity;
import com.simulation.drone.model.message.control.DroneControl;
import com.simulation.drone.model.message.state.DroneState;
import com.simulation.drone.repository.DroneRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class EnvironmentEngine {
    
    private final AirSimAdapter airSimAdapter;
    private final DroneRepository droneRepository;
    
    @Value("${simulation.environment.spatial-index-depth}")
    private int spatialIndexDepth;
    
    private final Map<String, DroneEntity> activeDrones = new ConcurrentHashMap<>();
    
    public EnvironmentEngine(AirSimAdapter airSimAdapter, DroneRepository droneRepository) {
        this.airSimAdapter = airSimAdapter;
        this.droneRepository = droneRepository;
    }
    
    /**
     * 定期更新环境状态
     */
    @Scheduled(fixedRateString = "${simulation.clock.time-step}")
    public void updateEnvironment() {
        try {
            // 更新所有活动无人机的状态
            for (String droneName : activeDrones.keySet()) {
                DroneState state = airSimAdapter.getDroneState(droneName);
                if (state != null) {
                    DroneEntity drone = convertToDroneEntity(state);
                    droneRepository.save(drone);
                    activeDrones.put(droneName, drone);
                }
            }
        } catch (Exception e) {
            log.error("环境状态更新失败", e);
        }
    }
    
    /**
     * 添加无人机到环境
     */
    public void addDrone(String droneName) {
        if (!activeDrones.containsKey(droneName)) {
            DroneState state = airSimAdapter.getDroneState(droneName);
            if (state != null) {
                DroneEntity drone = convertToDroneEntity(state);
                drone = droneRepository.save(drone);
                activeDrones.put(droneName, drone);
                log.info("无人机已添加到环境: {}", droneName);
            }
        }
    }
    
    /**
     * 从环境中移除无人机
     */
    public void removeDrone(String droneName) {
        activeDrones.remove(droneName);
        log.info("无人机已从环境中移除: {}", droneName);
    }
    
    /**
     * 获取环境中的所有无人机
     */
    public List<DroneEntity> getAllDrones() {
        return List.copyOf(activeDrones.values());
    }
    
    /**
     * 控制无人机移动
     */
    public void controlDrone(String droneName, double vx, double vy, double vz, double yawRate) {
        if (activeDrones.containsKey(droneName)) {
            DroneControl control = new DroneControl();
            control.setThrottle((float) vz);
            control.setRoll((float) vx);
            control.setPitch((float) vy);
            control.setYaw((float) yawRate);
            control.setArmed(true);
            
            airSimAdapter.controlDrone(droneName, control);
        } else {
            log.warn("尝试控制未激活的无人机: {}", droneName);
        }
    }
    
    /**
     * 获取相机图像
     */
    public byte[] getDroneCameraImage(String droneName, String cameraName) {
        if (activeDrones.containsKey(droneName)) {
            return airSimAdapter.getImageData(cameraName);
        }
        return null;
    }
    
    /**
     * 将DroneState转换为DroneEntity
     */
    private DroneEntity convertToDroneEntity(DroneState state) {
        DroneEntity entity = new DroneEntity();
        entity.setDroneId(state.getDroneId());
        entity.setPosition(state.getKinematics().getPosition());
        entity.setOrientation(state.getKinematics().getOrientation());
        entity.setVelocity(state.getKinematics().getLinearVelocity());
        entity.setAngularVelocity(state.getKinematics().getAngularVelocity());
        entity.setAcceleration(state.getKinematics().getLinearAcceleration());
        entity.setAngularAcceleration(state.getKinematics().getAngularAcceleration());
        return entity;
    }
} 