package com.JP.dronesim.infrastructure.external.airsim.dto;

import com.JP.dronesim.infrastructure.messaging.serialization.AirSimRpcMessageTrait;
import org.msgpack.annotation.Message;

/**
 * 四元数数据传输对象
 * 用于与AirSim交换姿态数据
 */
@Message
public class Quaternionr extends AirSimRpcMessageTrait {
    
    /**
     * W分量（标量部分）
     */
    public float w;
    
    /**
     * X分量（向量部分）
     */
    public float x;
    
    /**
     * Y分量（向量部分）
     */
    public float y;
    
    /**
     * Z分量（向量部分）
     */
    public float z;
    
    /**
     * 默认构造函数（单位四元数）
     */
    public Quaternionr() {
        this.w = 1.0f;
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }
    
    /**
     * 构造函数
     * @param w W分量
     * @param x X分量
     * @param y Y分量
     * @param z Z分量
     */
    public Quaternionr(float w, float x, float y, float z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * 获取四元数的模长
     * @return 四元数模长
     */
    public float magnitude() {
        return (float) Math.sqrt(w * w + x * x + y * y + z * z);
    }
    
    /**
     * 四元数归一化
     * @return 归一化后的四元数
     */
    public Quaternionr normalize() {
        float mag = magnitude();
        if (mag == 0) {
            return new Quaternionr(1, 0, 0, 0);
        }
        return new Quaternionr(w / mag, x / mag, y / mag, z / mag);
    }
    
    /**
     * 转换为欧拉角（弧度）
     * @return 欧拉角数组 [roll, pitch, yaw]
     */
    public float[] toEulerAngles() {
        float[] angles = new float[3];
        
        // Roll (x-axis rotation)
        double sinr_cosp = 2 * (w * x + y * z);
        double cosr_cosp = 1 - 2 * (x * x + y * y);
        angles[0] = (float) Math.atan2(sinr_cosp, cosr_cosp);
        
        // Pitch (y-axis rotation)
        double sinp = 2 * (w * y - z * x);
        if (Math.abs(sinp) >= 1) {
            angles[1] = (float) Math.copySign(Math.PI / 2, sinp);
        } else {
            angles[1] = (float) Math.asin(sinp);
        }
        
        // Yaw (z-axis rotation)
        double siny_cosp = 2 * (w * z + x * y);
        double cosy_cosp = 1 - 2 * (y * y + z * z);
        angles[2] = (float) Math.atan2(siny_cosp, cosy_cosp);
        
        return angles;
    }
    
    @Override
    public String toString() {
        return String.format("Quaternionr(w=%.3f, x=%.3f, y=%.3f, z=%.3f)", w, x, y, z);
    }
} 