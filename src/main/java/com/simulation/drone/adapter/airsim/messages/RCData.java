package com.simulation.drone.adapter.airsim.messages;

import org.msgpack.annotation.Message;
import com.simulation.drone.adapter.airsim.api.AirSimRpcMessageTrait;

/**
 * 遥控器数据
 * 包含遥控器各通道的输入值
 */
@Message
public class RCData extends AirSimRpcMessageTrait {
    
    /** 时间戳 */
    public long timestamp;
    
    /** 横滚通道 */
    public float roll;
    
    /** 俯仰通道 */
    public float pitch;
    
    /** 偏航通道 */
    public float yaw;
    
    /** 油门通道 */
    public float throttle;
    
    /** 开关1 */
    public float switch1;
    
    /** 开关2 */
    public float switch2;
    
    /** 开关3 */
    public float switch3;
    
    /** 开关4 */
    public float switch4;
    
    /** 开关5 */
    public float switch5;
    
    /** 开关6 */
    public float switch6;
    
    /** 开关7 */
    public float switch7;
    
    /** 开关8 */
    public float switch8;
    
    /** 是否初始化 */
    public boolean is_initialized;
    
    /** 是否有效 */
    public boolean is_valid;
    
    public RCData() {
        this.timestamp = 0;
        this.roll = 0.0f;
        this.pitch = 0.0f;
        this.yaw = 0.0f;
        this.throttle = 0.0f;
        this.switch1 = 0.0f;
        this.switch2 = 0.0f;
        this.switch3 = 0.0f;
        this.switch4 = 0.0f;
        this.switch5 = 0.0f;
        this.switch6 = 0.0f;
        this.switch7 = 0.0f;
        this.switch8 = 0.0f;
        this.is_initialized = false;
        this.is_valid = false;
    }
} 