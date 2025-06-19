package com.simulation.drone.adapter.airsim.messages;

import org.msgpack.annotation.Message;
import com.simulation.drone.adapter.airsim.api.AirSimRpcMessageTrait;

/**
 * 偏航模式
 * 用于指定无人机的偏航控制方式
 */
@Message
public class YawMode extends AirSimRpcMessageTrait {
    
    /** 是否为角速度模式(true)还是角度模式(false) */
    public boolean is_rate;
    
    /** 偏航值：角度(度)或角速度(度/秒) */
    public float yaw_or_rate;
    
    public YawMode() {
        this.is_rate = false;
        this.yaw_or_rate = 0.0f;
    }
    
    public YawMode(boolean isRate, float yawOrRate) {
        this.is_rate = isRate;
        this.yaw_or_rate = yawOrRate;
    }
    
    /**
     * 创建角度模式的偏航
     * @param yaw 目标偏航角(度)
     * @return YawMode实例
     */
    public static YawMode createYawMode(float yaw) {
        return new YawMode(false, yaw);
    }
    
    /**
     * 创建角速度模式的偏航
     * @param yawRate 偏航角速度(度/秒)
     * @return YawMode实例
     */
    public static YawMode createYawRateMode(float yawRate) {
        return new YawMode(true, yawRate);
    }
} 