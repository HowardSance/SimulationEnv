package com.simulation.drone.model.message.geometry;

import com.simulation.drone.model.message.BaseMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.msgpack.annotation.Message;

@Data
@EqualsAndHashCode(callSuper = true)
@Message
@Schema(description = "四元数，用于表示三维空间中的旋转")
public class Quaternion extends BaseMessage {
    
    @Schema(description = "实部", example = "1.0")
    private float w;
    
    @Schema(description = "虚部X", example = "0.0")
    private float x;
    
    @Schema(description = "虚部Y", example = "0.0")
    private float y;
    
    @Schema(description = "虚部Z", example = "0.0")
    private float z;
    
    public Quaternion() {
        this.w = 1f;
        this.x = 0f;
        this.y = 0f;
        this.z = 0f;
    }
    
    public Quaternion(float w, float x, float y, float z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }
} 