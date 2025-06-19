package com.simulation.drone.model.message.sensor;

import com.simulation.drone.model.message.BaseMessage;
import com.simulation.drone.model.message.geometry.Pose;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.msgpack.annotation.Message;

@Data
@EqualsAndHashCode(callSuper = true)
@Message
public class LidarData extends BaseMessage {
    private long timestamp;
    private float[] pointCloud;
    private Pose pose;
    private int segmentation;
    
    public LidarData() {
        this.timestamp = 0L;
        this.pointCloud = new float[0];
        this.pose = new Pose();
        this.segmentation = 0;
    }
} 