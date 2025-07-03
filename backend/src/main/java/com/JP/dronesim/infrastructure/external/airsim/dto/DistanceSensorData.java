package com.JP.dronesim.infrastructure.external.airsim.dto;

import com.JP.dronesim.infrastructure.messaging.serialization.AirSimRpcMessageTrait;
import org.msgpack.annotation.Message;

/**
 * 距离传感器数据传输对象
 * 用于与AirSim交换距离传感器数据
 */
@Message
public class DistanceSensorData extends AirSimRpcMessageTrait {
    
    /**
     * 时间戳（纳秒）
     */
    public long time_stamp;
    
    /**
     * 测量距离（米）
     */
    public float distance;
    
    /**
     * 最小测量距离（米）
     */
    public float min_distance;
    
    /**
     * 最大测量距离（米）
     */
    public float max_distance;
    
    /**
     * 传感器相对位姿
     */
    public Pose relative_pose;
    
    /**
     * 默认构造函数
     */
    public DistanceSensorData() {
        this.time_stamp = 0L;
        this.distance = 0.0f;
        this.min_distance = 0.0f;
        this.max_distance = 100.0f; // 默认最大距离100米
        this.relative_pose = new Pose();
    }
    
    /**
     * 构造函数
     * @param timeStamp 时间戳
     * @param distance 测量距离
     * @param minDistance 最小测量距离
     * @param maxDistance 最大测量距离
     * @param relativePose 传感器相对位姿
     */
    public DistanceSensorData(long timeStamp, float distance, float minDistance, 
                             float maxDistance, Pose relativePose) {
        this.time_stamp = timeStamp;
        this.distance = distance;
        this.min_distance = minDistance;
        this.max_distance = maxDistance;
        this.relative_pose = relativePose != null ? relativePose : new Pose();
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
     * 获取测量距离
     * @return 测量距离（米）
     */
    public float getDistance() {
        return distance;
    }
    
    /**
     * 设置测量距离
     * @param distance 测量距离（米）
     */
    public void setDistance(float distance) {
        this.distance = distance;
    }
    
    /**
     * 获取最小测量距离
     * @return 最小测量距离（米）
     */
    public float getMinDistance() {
        return min_distance;
    }
    
    /**
     * 设置最小测量距离
     * @param minDistance 最小测量距离（米）
     */
    public void setMinDistance(float minDistance) {
        this.min_distance = minDistance;
    }
    
    /**
     * 获取最大测量距离
     * @return 最大测量距离（米）
     */
    public float getMaxDistance() {
        return max_distance;
    }
    
    /**
     * 设置最大测量距离
     * @param maxDistance 最大测量距离（米）
     */
    public void setMaxDistance(float maxDistance) {
        this.max_distance = maxDistance;
    }
    
    /**
     * 获取传感器相对位姿
     * @return 传感器相对位姿
     */
    public Pose getRelativePose() {
        return relative_pose;
    }
    
    /**
     * 设置传感器相对位姿
     * @param relativePose 传感器相对位姿
     */
    public void setRelativePose(Pose relativePose) {
        this.relative_pose = relativePose != null ? relativePose : new Pose();
    }
    
    /**
     * 判断距离测量是否有效
     * @return 是否有效
     */
    public boolean isValidMeasurement() {
        return distance >= min_distance && distance <= max_distance && time_stamp > 0;
    }
    
    /**
     * 判断是否检测到障碍物
     * @param threshold 阈值距离
     * @return 是否检测到障碍物
     */
    public boolean hasObstacle(float threshold) {
        return isValidMeasurement() && distance < threshold;
    }
    
    /**
     * 判断是否检测到障碍物（使用默认阈值5米）
     * @return 是否检测到障碍物
     */
    public boolean hasObstacle() {
        return hasObstacle(5.0f);
    }
    
    /**
     * 获取距离百分比（相对于最大距离）
     * @return 距离百分比 (0.0 - 1.0)
     */
    public float getDistancePercentage() {
        if (max_distance <= min_distance) {
            return 0.0f;
        }
        
        float normalizedDistance = Math.max(0, Math.min(distance, max_distance) - min_distance);
        float range = max_distance - min_distance;
        return normalizedDistance / range;
    }
    
    /**
     * 判断是否超出测量范围
     * @return 是否超出范围
     */
    public boolean isOutOfRange() {
        return distance < min_distance || distance > max_distance;
    }
    
    /**
     * 克隆距离传感器数据
     * @return 距离传感器数据副本
     */
    public DistanceSensorData clone() {
        return new DistanceSensorData(
            time_stamp,
            distance,
            min_distance,
            max_distance,
            relative_pose != null ? relative_pose.clone() : new Pose()
        );
    }
    
    @Override
    public String toString() {
        return String.format("DistanceSensorData(distance=%.3f, range=[%.3f-%.3f], time_stamp=%d, valid=%s)",
                           distance,
                           min_distance,
                           max_distance,
                           time_stamp,
                           isValidMeasurement());
    }
} 