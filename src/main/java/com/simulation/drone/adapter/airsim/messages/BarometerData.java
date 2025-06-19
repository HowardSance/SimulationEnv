package com.simulation.drone.adapter.airsim.messages;

import org.msgpack.annotation.Message;
import com.simulation.drone.adapter.airsim.api.AirSimRpcMessageTrait;

/**
 * 气压计传感器数据
 * 包含大气压力和高度信息
 */
@Message
public class BarometerData extends AirSimRpcMessageTrait {
    
    /** 时间戳 */
    public long time_stamp;
    
    /** 高度(米) */
    public float altitude;
    
    /** 大气压力(帕斯卡) */
    public float pressure;
    
    /** QNH(海平面修正气压) */
    public float qnh;
    
    public BarometerData() {
        this.time_stamp = 0;
        this.altitude = 0.0f;
        this.pressure = 101325.0f; // 标准大气压
        this.qnh = 101325.0f;
    }
} 