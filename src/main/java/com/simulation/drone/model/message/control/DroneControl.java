package com.simulation.drone.model.message.control;

import com.simulation.drone.model.message.BaseMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.msgpack.annotation.Message;

@Data
@EqualsAndHashCode(callSuper = true)
@Message
@Schema(description = "无人机控制命令")
public class DroneControl extends BaseMessage {
    
    @Schema(description = "油门值，范围[-1.0, 1.0]", example = "0.5")
    private float throttle;
    
    @Schema(description = "横滚角，范围[-1.0, 1.0]", example = "0.0")
    private float roll;
    
    @Schema(description = "俯仰角，范围[-1.0, 1.0]", example = "0.0")
    private float pitch;
    
    @Schema(description = "偏航角，范围[-1.0, 1.0]", example = "0.0")
    private float yaw;
    
    @Schema(description = "是否解锁", example = "true")
    private boolean isArmed;
    
    public DroneControl() {
        this.throttle = 0f;
        this.roll = 0f;
        this.pitch = 0f;
        this.yaw = 0f;
        this.isArmed = false;
    }
} 