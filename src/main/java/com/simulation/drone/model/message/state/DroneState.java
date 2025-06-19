package com.simulation.drone.model.message.state;

import com.simulation.drone.model.message.BaseMessage;
import com.simulation.drone.model.message.sensor.DistanceSensorData;
import com.simulation.drone.model.message.sensor.ImuData;
import com.simulation.drone.model.message.sensor.LidarData;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.msgpack.annotation.Message;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@Message
@Schema(description = "无人机状态信息")
public class DroneState extends BaseMessage {
    
    @Schema(description = "无人机ID", example = "drone_001")
    private String droneId;
    
    @Schema(description = "运动学状态")
    private KinematicsState kinematics;
    
    @Schema(description = "IMU传感器数据，key为传感器ID")
    private Map<String, ImuData> imuData;
    
    @Schema(description = "距离传感器数据，key为传感器ID")
    private Map<String, DistanceSensorData> distanceSensors;
    
    @Schema(description = "激光雷达数据，key为传感器ID")
    private Map<String, LidarData> lidarSensors;
    
    @Schema(description = "相机图像数据，key为相机ID")
    private Map<String, byte[]> cameraImages;
    
    public DroneState() {
        this.kinematics = new KinematicsState();
        this.imuData = new HashMap<>();
        this.distanceSensors = new HashMap<>();
        this.lidarSensors = new HashMap<>();
        this.cameraImages = new HashMap<>();
    }
} 