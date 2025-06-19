package com.simulation.drone.adapter.airsim.messages;

import org.msgpack.annotation.Message;
import com.simulation.drone.adapter.airsim.api.AirSimRpcMessageTrait;

/**
 * GPS传感器数据
 * 包含GPS定位的详细信息
 */
@Message
public class GpsData extends AirSimRpcMessageTrait {
    
    /** 时间戳 */
    public long time_stamp;
    
    /** 地理位置 */
    public GeoPoint gnss;
    
    /** 速度 */
    public Vector3r velocity;
    
    /** 是否有fix */
    public int fix_type;
    
    /** ENU坐标系中的位置 */
    public Vector3r enu_velocity;
    
    public GpsData() {
        this.time_stamp = 0;
        this.gnss = new GeoPoint();
        this.velocity = new Vector3r();
        this.fix_type = 0;
        this.enu_velocity = new Vector3r();
    }
} 