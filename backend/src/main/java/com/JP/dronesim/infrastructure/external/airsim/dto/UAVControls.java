package com.JP.dronesim.infrastructure.external.airsim.dto;

import com.JP.dronesim.infrastructure.messaging.serialization.AirSimRpcMessageTrait;
import org.msgpack.annotation.Message;

/**
 * 无人机控制指令数据传输对象
 * 用于与AirSim交换无人机控制指令
 */
@Message
public class UAVControls extends AirSimRpcMessageTrait {
    
    /**
     * 俯仰控制（-1.0 到 1.0）
     */
    public float pitch;
    
    /**
     * 横滚控制（-1.0 到 1.0）
     */
    public float roll;
    
    /**
     * 偏航率控制（-1.0 到 1.0）
     */
    public float yaw_rate;
    
    /**
     * 油门控制（0.0 到 1.0）
     */
    public float throttle;
    
    /**
     * 是否使用身体坐标系
     */
    public boolean is_body_frame;
    
    /**
     * 控制指令时间戳
     */
    public long timestamp;
    
    /**
     * 默认构造函数
     */
    public UAVControls() {
        this.pitch = 0.0f;
        this.roll = 0.0f;
        this.yaw_rate = 0.0f;
        this.throttle = 0.0f;
        this.is_body_frame = true;
        this.timestamp = System.nanoTime();
    }
    
    /**
     * 构造函数
     * @param pitch 俯仰控制
     * @param roll 横滚控制
     * @param yawRate 偏航率控制
     * @param throttle 油门控制
     */
    public UAVControls(float pitch, float roll, float yawRate, float throttle) {
        this.pitch = clamp(pitch, -1.0f, 1.0f);
        this.roll = clamp(roll, -1.0f, 1.0f);
        this.yaw_rate = clamp(yawRate, -1.0f, 1.0f);
        this.throttle = clamp(throttle, 0.0f, 1.0f);
        this.is_body_frame = true;
        this.timestamp = System.nanoTime();
    }
    
    /**
     * 构造函数
     * @param pitch 俯仰控制
     * @param roll 横滚控制
     * @param yawRate 偏航率控制
     * @param throttle 油门控制
     * @param isBodyFrame 是否使用身体坐标系
     */
    public UAVControls(float pitch, float roll, float yawRate, float throttle, boolean isBodyFrame) {
        this.pitch = clamp(pitch, -1.0f, 1.0f);
        this.roll = clamp(roll, -1.0f, 1.0f);
        this.yaw_rate = clamp(yawRate, -1.0f, 1.0f);
        this.throttle = clamp(throttle, 0.0f, 1.0f);
        this.is_body_frame = isBodyFrame;
        this.timestamp = System.nanoTime();
    }
    
    /**
     * 获取俯仰控制
     * @return 俯仰控制值
     */
    public float getPitch() {
        return pitch;
    }
    
    /**
     * 设置俯仰控制
     * @param pitch 俯仰控制值（-1.0 到 1.0）
     */
    public void setPitch(float pitch) {
        this.pitch = clamp(pitch, -1.0f, 1.0f);
        this.timestamp = System.nanoTime();
    }
    
    /**
     * 获取横滚控制
     * @return 横滚控制值
     */
    public float getRoll() {
        return roll;
    }
    
    /**
     * 设置横滚控制
     * @param roll 横滚控制值（-1.0 到 1.0）
     */
    public void setRoll(float roll) {
        this.roll = clamp(roll, -1.0f, 1.0f);
        this.timestamp = System.nanoTime();
    }
    
    /**
     * 获取偏航率控制
     * @return 偏航率控制值
     */
    public float getYawRate() {
        return yaw_rate;
    }
    
    /**
     * 设置偏航率控制
     * @param yawRate 偏航率控制值（-1.0 到 1.0）
     */
    public void setYawRate(float yawRate) {
        this.yaw_rate = clamp(yawRate, -1.0f, 1.0f);
        this.timestamp = System.nanoTime();
    }
    
    /**
     * 获取油门控制
     * @return 油门控制值
     */
    public float getThrottle() {
        return throttle;
    }
    
    /**
     * 设置油门控制
     * @param throttle 油门控制值（0.0 到 1.0）
     */
    public void setThrottle(float throttle) {
        this.throttle = clamp(throttle, 0.0f, 1.0f);
        this.timestamp = System.nanoTime();
    }
    
    /**
     * 是否使用身体坐标系
     * @return 是否使用身体坐标系
     */
    public boolean isBodyFrame() {
        return is_body_frame;
    }
    
    /**
     * 设置是否使用身体坐标系
     * @param isBodyFrame 是否使用身体坐标系
     */
    public void setBodyFrame(boolean isBodyFrame) {
        this.is_body_frame = isBodyFrame;
        this.timestamp = System.nanoTime();
    }
    
    /**
     * 获取时间戳
     * @return 时间戳
     */
    public long getTimestamp() {
        return timestamp;
    }
    
    /**
     * 重置所有控制指令为中性位置
     */
    public void reset() {
        this.pitch = 0.0f;
        this.roll = 0.0f;
        this.yaw_rate = 0.0f;
        this.throttle = 0.0f;
        this.timestamp = System.nanoTime();
    }
    
    /**
     * 设置为悬停状态
     */
    public void setHover() {
        this.pitch = 0.0f;
        this.roll = 0.0f;
        this.yaw_rate = 0.0f;
        this.throttle = 0.5f; // 中等油门保持悬停
        this.timestamp = System.nanoTime();
    }
    
    /**
     * 判断是否为中性控制状态
     * @return 是否为中性状态
     */
    public boolean isNeutral() {
        return Math.abs(pitch) < 0.01f && 
               Math.abs(roll) < 0.01f && 
               Math.abs(yaw_rate) < 0.01f && 
               Math.abs(throttle) < 0.01f;
    }
    
    /**
     * 判断是否为悬停控制状态
     * @return 是否为悬停状态
     */
    public boolean isHovering() {
        return Math.abs(pitch) < 0.01f && 
               Math.abs(roll) < 0.01f && 
               Math.abs(yaw_rate) < 0.01f && 
               throttle > 0.4f && throttle < 0.6f;
    }
    
    /**
     * 获取控制强度（所有控制量的向量和）
     * @return 控制强度
     */
    public float getControlIntensity() {
        return (float) Math.sqrt(pitch * pitch + roll * roll + yaw_rate * yaw_rate + throttle * throttle);
    }
    
    /**
     * 限制值在指定范围内
     * @param value 输入值
     * @param min 最小值
     * @param max 最大值
     * @return 限制后的值
     */
    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * 克隆控制指令
     * @return 控制指令副本
     */
    public UAVControls clone() {
        UAVControls clone = new UAVControls(pitch, roll, yaw_rate, throttle, is_body_frame);
        clone.timestamp = this.timestamp;
        return clone;
    }
    
    /**
     * 从另一个控制指令复制
     * @param other 另一个控制指令
     */
    public void copyFrom(UAVControls other) {
        if (other != null) {
            this.pitch = other.pitch;
            this.roll = other.roll;
            this.yaw_rate = other.yaw_rate;
            this.throttle = other.throttle;
            this.is_body_frame = other.is_body_frame;
            this.timestamp = System.nanoTime();
        }
    }
    
    @Override
    public String toString() {
        return String.format("UAVControls(pitch=%.3f, roll=%.3f, yaw_rate=%.3f, throttle=%.3f, body_frame=%s, intensity=%.3f)",
                           pitch, roll, yaw_rate, throttle, is_body_frame, getControlIntensity());
    }
} 