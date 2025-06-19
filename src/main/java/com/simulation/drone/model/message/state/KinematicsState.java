package com.simulation.drone.model.message.state;

import com.simulation.drone.model.message.BaseMessage;
import com.simulation.drone.model.message.geometry.Quaternion;
import com.simulation.drone.model.message.geometry.Vector3;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.msgpack.annotation.Message;

@Data
@EqualsAndHashCode(callSuper = true)
@Message
@Schema(description = "运动学状态信息")
public class KinematicsState extends BaseMessage {
    
    @Schema(description = "位置向量")
    private Vector3 position;
    
    @Schema(description = "姿态四元数")
    private Quaternion orientation;
    
    @Schema(description = "线速度向量")
    private Vector3 linearVelocity;
    
    @Schema(description = "角速度向量")
    private Vector3 angularVelocity;
    
    @Schema(description = "线加速度向量")
    private Vector3 linearAcceleration;
    
    @Schema(description = "角加速度向量")
    private Vector3 angularAcceleration;
    
    public KinematicsState() {
        this.position = new Vector3();
        this.orientation = new Quaternion();
        this.linearVelocity = new Vector3();
        this.angularVelocity = new Vector3();
        this.linearAcceleration = new Vector3();
        this.angularAcceleration = new Vector3();
    }
} 