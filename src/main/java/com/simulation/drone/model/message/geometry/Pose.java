package com.simulation.drone.model.message.geometry;

import com.simulation.drone.model.message.BaseMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.msgpack.annotation.Message;

@Data
@EqualsAndHashCode(callSuper = true)
@Message
public class Pose extends BaseMessage {
    private Vector3 position;
    private Quaternion orientation;
    
    public Pose() {
        this.position = new Vector3();
        this.orientation = new Quaternion();
    }
    
    public Pose(Vector3 position, Quaternion orientation) {
        this.position = position;
        this.orientation = orientation;
    }
} 