package com.JP.dronesim.infrastructure.external.airsim.dto;

import com.JP.dronesim.infrastructure.messaging.serialization.AirSimRpcMessageTrait;
import org.msgpack.annotation.Message;

/**
 * 三维向量数据传输对象
 * 用于与AirSim交换三维向量数据（位置、速度、加速度等）
 */
@Message
public class Vector3r extends AirSimRpcMessageTrait {
    
    /**
     * X轴分量
     */
    public float x;
    
    /**
     * Y轴分量
     */
    public float y;
    
    /**
     * Z轴分量
     */
    public float z;
    
    /**
     * 默认构造函数
     */
    public Vector3r() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }
    
    /**
     * 构造函数
     * @param x X轴分量
     * @param y Y轴分量
     * @param z Z轴分量
     */
    public Vector3r(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * 获取向量的模长
     * @return 向量模长
     */
    public float magnitude() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }
    
    /**
     * 向量归一化
     * @return 归一化后的向量
     */
    public Vector3r normalize() {
        float mag = magnitude();
        if (mag == 0) {
            return new Vector3r(0, 0, 0);
        }
        return new Vector3r(x / mag, y / mag, z / mag);
    }
    
    @Override
    public String toString() {
        return String.format("Vector3r(x=%.3f, y=%.3f, z=%.3f)", x, y, z);
    }
} 