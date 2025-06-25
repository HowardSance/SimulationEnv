package com.JP.dronesim.infrastructure.external.airsim.dto;

import com.JP.dronesim.infrastructure.messaging.serialization.AirSimRpcMessageTrait;
import org.msgpack.annotation.Message;

/**
 * 运动学状态数据传输对象
 * 包含位置、姿态、线性和角度速度、线性和角度加速度信息
 */
@Message
public class KinematicsState extends AirSimRpcMessageTrait {
    
    /**
     * 位置信息
     */
    public Vector3r position;
    
    /**
     * 姿态信息（四元数）
     */
    public Quaternionr orientation;
    
    /**
     * 线性速度
     */
    public Vector3r linear_velocity;
    
    /**
     * 角速度
     */
    public Vector3r angular_velocity;
    
    /**
     * 线性加速度
     */
    public Vector3r linear_acceleration;
    
    /**
     * 角加速度
     */
    public Vector3r angular_acceleration;
    
    /**
     * 默认构造函数
     */
    public KinematicsState() {
        this.position = new Vector3r();
        this.orientation = new Quaternionr();
        this.linear_velocity = new Vector3r();
        this.angular_velocity = new Vector3r();
        this.linear_acceleration = new Vector3r();
        this.angular_acceleration = new Vector3r();
    }
    
    /**
     * 构造函数
     * @param position 位置
     * @param orientation 姿态
     * @param linearVelocity 线性速度
     * @param angularVelocity 角速度
     * @param linearAcceleration 线性加速度
     * @param angularAcceleration 角加速度
     */
    public KinematicsState(Vector3r position, Quaternionr orientation,
                          Vector3r linearVelocity, Vector3r angularVelocity,
                          Vector3r linearAcceleration, Vector3r angularAcceleration) {
        this.position = position != null ? position : new Vector3r();
        this.orientation = orientation != null ? orientation : new Quaternionr();
        this.linear_velocity = linearVelocity != null ? linearVelocity : new Vector3r();
        this.angular_velocity = angularVelocity != null ? angularVelocity : new Vector3r();
        this.linear_acceleration = linearAcceleration != null ? linearAcceleration : new Vector3r();
        this.angular_acceleration = angularAcceleration != null ? angularAcceleration : new Vector3r();
    }
    
    /**
     * 获取位姿信息
     * @return 位姿对象
     */
    public Pose getPose() {
        return new Pose(position, orientation);
    }
    
    /**
     * 获取速度大小
     * @return 速度大小
     */
    public float getSpeed() {
        return linear_velocity != null ? linear_velocity.magnitude() : 0.0f;
    }
    
    /**
     * 获取加速度大小
     * @return 加速度大小
     */
    public float getAccelerationMagnitude() {
        return linear_acceleration != null ? linear_acceleration.magnitude() : 0.0f;
    }
    
    /**
     * 获取角速度大小
     * @return 角速度大小
     */
    public float getAngularSpeed() {
        return angular_velocity != null ? angular_velocity.magnitude() : 0.0f;
    }
    
    /**
     * 判断是否处于静止状态
     * @param threshold 速度阈值
     * @return 是否静止
     */
    public boolean isStationary(float threshold) {
        return getSpeed() < threshold && getAngularSpeed() < threshold;
    }
    
    /**
     * 判断是否处于静止状态（使用默认阈值0.1）
     * @return 是否静止
     */
    public boolean isStationary() {
        return isStationary(0.1f);
    }
    
    /**
     * 克隆运动学状态
     * @return 运动学状态副本
     */
    public KinematicsState clone() {
        return new KinematicsState(
            new Vector3r(position.x, position.y, position.z),
            new Quaternionr(orientation.w, orientation.x, orientation.y, orientation.z),
            new Vector3r(linear_velocity.x, linear_velocity.y, linear_velocity.z),
            new Vector3r(angular_velocity.x, angular_velocity.y, angular_velocity.z),
            new Vector3r(linear_acceleration.x, linear_acceleration.y, linear_acceleration.z),
            new Vector3r(angular_acceleration.x, angular_acceleration.y, angular_acceleration.z)
        );
    }
    
    @Override
    public String toString() {
        return String.format("KinematicsState(position=%s, orientation=%s, speed=%.3f, angular_speed=%.3f)",
                           position != null ? position.toString() : "null",
                           orientation != null ? orientation.toString() : "null",
                           getSpeed(),
                           getAngularSpeed());
    }
} 