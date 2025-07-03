package com.JP.dronesim.infrastructure.external.airsim.dto;

import com.JP.dronesim.infrastructure.messaging.serialization.AirSimRpcMessageTrait;
import org.msgpack.annotation.Message;

/**
 * IMU传感器数据传输对象
 * 用于与AirSim交换惯性测量单元数据
 */
@Message
public class ImuData extends AirSimRpcMessageTrait {
    
    /**
     * 时间戳（纳秒）
     */
    public long time_stamp;
    
    /**
     * 线性加速度（m/s²）
     */
    public Vector3r linear_acceleration;
    
    /**
     * 角速度（rad/s）
     */
    public Vector3r angular_velocity;
    
    /**
     * 姿态（四元数）
     */
    public Quaternionr orientation;
    
    /**
     * 默认构造函数
     */
    public ImuData() {
        this.time_stamp = 0L;
        this.linear_acceleration = new Vector3r();
        this.angular_velocity = new Vector3r();
        this.orientation = new Quaternionr();
    }
    
    /**
     * 构造函数
     * @param timeStamp 时间戳
     * @param linearAcceleration 线性加速度
     * @param angularVelocity 角速度
     * @param orientation 姿态
     */
    public ImuData(long timeStamp, Vector3r linearAcceleration, 
                   Vector3r angularVelocity, Quaternionr orientation) {
        this.time_stamp = timeStamp;
        this.linear_acceleration = linearAcceleration != null ? linearAcceleration : new Vector3r();
        this.angular_velocity = angularVelocity != null ? angularVelocity : new Vector3r();
        this.orientation = orientation != null ? orientation : new Quaternionr();
    }
    
    /**
     * 获取时间戳
     * @return 时间戳（纳秒）
     */
    public long getTimeStamp() {
        return time_stamp;
    }
    
    /**
     * 设置时间戳
     * @param timeStamp 时间戳（纳秒）
     */
    public void setTimeStamp(long timeStamp) {
        this.time_stamp = timeStamp;
    }
    
    /**
     * 获取线性加速度
     * @return 线性加速度向量
     */
    public Vector3r getLinearAcceleration() {
        return linear_acceleration;
    }
    
    /**
     * 设置线性加速度
     * @param linearAcceleration 线性加速度向量
     */
    public void setLinearAcceleration(Vector3r linearAcceleration) {
        this.linear_acceleration = linearAcceleration != null ? linearAcceleration : new Vector3r();
    }
    
    /**
     * 获取角速度
     * @return 角速度向量
     */
    public Vector3r getAngularVelocity() {
        return angular_velocity;
    }
    
    /**
     * 设置角速度
     * @param angularVelocity 角速度向量
     */
    public void setAngularVelocity(Vector3r angularVelocity) {
        this.angular_velocity = angularVelocity != null ? angularVelocity : new Vector3r();
    }
    
    /**
     * 获取姿态
     * @return 姿态四元数
     */
    public Quaternionr getOrientation() {
        return orientation;
    }
    
    /**
     * 设置姿态
     * @param orientation 姿态四元数
     */
    public void setOrientation(Quaternionr orientation) {
        this.orientation = orientation != null ? orientation : new Quaternionr();
    }
    
    /**
     * 获取加速度大小
     * @return 加速度大小（m/s²）
     */
    public float getAccelerationMagnitude() {
        return linear_acceleration != null ? linear_acceleration.magnitude() : 0.0f;
    }
    
    /**
     * 获取角速度大小
     * @return 角速度大小（rad/s）
     */
    public float getAngularSpeed() {
        return angular_velocity != null ? angular_velocity.magnitude() : 0.0f;
    }
    
    /**
     * 判断IMU数据是否有效
     * @return 是否有效
     */
    public boolean isValidData() {
        return time_stamp > 0 && 
               linear_acceleration != null && 
               angular_velocity != null && 
               orientation != null;
    }
    
    /**
     * 判断是否处于静止状态
     * @param accelerationThreshold 加速度阈值（m/s²）
     * @param angularThreshold 角速度阈值（rad/s）
     * @return 是否静止
     */
    public boolean isStationary(float accelerationThreshold, float angularThreshold) {
        return isValidData() && 
               getAccelerationMagnitude() < accelerationThreshold &&
               getAngularSpeed() < angularThreshold;
    }
    
    /**
     * 判断是否处于静止状态（使用默认阈值）
     * @return 是否静止
     */
    public boolean isStationary() {
        return isStationary(0.5f, 0.1f); // 默认阈值：0.5 m/s²，0.1 rad/s
    }
    
    /**
     * 获取欧拉角（弧度）
     * @return 欧拉角数组 [roll, pitch, yaw]
     */
    public float[] getEulerAngles() {
        return orientation != null ? orientation.toEulerAngles() : new float[]{0, 0, 0};
    }
    
    /**
     * 获取欧拉角（度）
     * @return 欧拉角数组 [roll, pitch, yaw]（度）
     */
    public float[] getEulerAnglesDegrees() {
        float[] radians = getEulerAngles();
        return new float[]{
            (float) Math.toDegrees(radians[0]),
            (float) Math.toDegrees(radians[1]),
            (float) Math.toDegrees(radians[2])
        };
    }
    
    /**
     * 检测是否有异常震动
     * @param threshold 震动阈值（m/s²）
     * @return 是否有异常震动
     */
    public boolean hasAbnormalVibration(float threshold) {
        return isValidData() && getAccelerationMagnitude() > threshold;
    }
    
    /**
     * 检测是否有异常震动（使用默认阈值20 m/s²）
     * @return 是否有异常震动
     */
    public boolean hasAbnormalVibration() {
        return hasAbnormalVibration(20.0f);
    }
    
    /**
     * 克隆IMU数据
     * @return IMU数据副本
     */
    public ImuData clone() {
        return new ImuData(
            time_stamp,
            linear_acceleration != null ? 
                new Vector3r(linear_acceleration.x, linear_acceleration.y, linear_acceleration.z) : 
                new Vector3r(),
            angular_velocity != null ? 
                new Vector3r(angular_velocity.x, angular_velocity.y, angular_velocity.z) : 
                new Vector3r(),
            orientation != null ? 
                new Quaternionr(orientation.w, orientation.x, orientation.y, orientation.z) : 
                new Quaternionr()
        );
    }
    
    @Override
    public String toString() {
        return String.format("ImuData(accel_mag=%.3f, angular_speed=%.3f, time_stamp=%d, valid=%s)",
                           getAccelerationMagnitude(),
                           getAngularSpeed(),
                           time_stamp,
                           isValidData());
    }
} 