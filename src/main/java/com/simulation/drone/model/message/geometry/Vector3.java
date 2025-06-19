package com.simulation.drone.model.message.geometry;

import com.simulation.drone.model.message.BaseMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.msgpack.annotation.Message;

@Data
@EqualsAndHashCode(callSuper = true)
@Message
@Schema(description = "三维向量")
public class Vector3 extends BaseMessage {
    
    @Schema(description = "X轴分量", example = "0.0")
    private float x;
    
    @Schema(description = "Y轴分量", example = "0.0")
    private float y;
    
    @Schema(description = "Z轴分量", example = "0.0")
    private float z;
    
    public Vector3() {
        this.x = 0f;
        this.y = 0f;
        this.z = 0f;
    }
    
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
} 