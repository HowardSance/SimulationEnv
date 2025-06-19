package com.simulation.drone.adapter.airsim.messages;

import org.msgpack.annotation.Message;
import com.simulation.drone.adapter.airsim.api.AirSimRpcMessageTrait;

/**
 * 磁力计传感器数据
 * 包含三轴磁场强度信息
 */
@Message
public class MagnetometerData extends AirSimRpcMessageTrait {
    
    /** 时间戳 */
    public long time_stamp;
    
    /** 磁场向量(特斯拉) */
    public Vector3r magnetic_field_body;
    
    /** 磁场强度 */
    public float magnetic_field_covariance;
    
    public MagnetometerData() {
        this.time_stamp = 0;
        this.magnetic_field_body = new Vector3r();
        this.magnetic_field_covariance = 0.0f;
    }
} 