package com.simulation.drone.adapter.airsim.messages;

import org.msgpack.annotation.Message;
import com.simulation.drone.adapter.airsim.api.AirSimRpcMessageTrait;

/**
 * 地理位置点信息
 * 包含经度、纬度、海拔等GPS坐标信息
 */
@Message
public class GeoPoint extends AirSimRpcMessageTrait {
    
    /** 纬度(度) */
    public double latitude;
    
    /** 经度(度) */
    public double longitude;
    
    /** 海拔高度(米) */
    public float altitude;
    
    public GeoPoint() {
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.altitude = 0.0f;
    }
    
    public GeoPoint(double latitude, double longitude, float altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }
} 