package com.simulation.drone.model.message.sensor;

import com.simulation.drone.model.message.BaseMessage;
import com.simulation.drone.model.message.geometry.Pose;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.msgpack.annotation.Message;

@Data
@EqualsAndHashCode(callSuper = true)
@Message
public class DistanceSensorData extends BaseMessage {
    private long timestamp;
    private float distance;
    private float minDistance;
    private float maxDistance;
    private Pose relativePose;
    
    public DistanceSensorData() {
        this.timestamp = 0L;
        this.distance = 0f;
        this.minDistance = 0f;
        this.maxDistance = 0f;
        this.relativePose = new Pose();
    }
} 