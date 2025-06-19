package com.simulation.drone.model.message.sensor;

import com.simulation.drone.model.message.BaseMessage;
import com.simulation.drone.model.message.geometry.Quaternion;
import com.simulation.drone.model.message.geometry.Vector3;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.msgpack.annotation.Message;

@Data
@EqualsAndHashCode(callSuper = true)
@Message
public class ImuData extends BaseMessage {
    private long timestamp;
    private Quaternion orientation;
    private Vector3 angularVelocity;
    private Vector3 linearAcceleration;
    
    public ImuData() {
        this.timestamp = 0L;
        this.orientation = new Quaternion();
        this.angularVelocity = new Vector3();
        this.linearAcceleration = new Vector3();
    }
} 