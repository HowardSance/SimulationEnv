package com.JP.dronesim.infrastructure.external.airsim.dto;

import com.JP.dronesim.infrastructure.messaging.serialization.AirSimRpcMessageTrait;
import org.msgpack.annotation.Message;

/**
 * 位姿数据传输对象
 * 包含位置和姿态信息，用于与AirSim交换完整的空间状态数据
 */
@Message
public class Pose extends AirSimRpcMessageTrait {
    
    /**
     * 位置信息
     */
    public Vector3r position;
    
    /**
     * 姿态信息（四元数）
     */
    public Quaternionr orientation;
    
    /**
     * 默认构造函数
     */
    public Pose() {
        this.position = new Vector3r();
        this.orientation = new Quaternionr();
    }
    
    /**
     * 构造函数
     * @param position 位置
     * @param orientation 姿态
     */
    public Pose(Vector3r position, Quaternionr orientation) {
        this.position = position != null ? position : new Vector3r();
        this.orientation = orientation != null ? orientation : new Quaternionr();
    }
    
    /**
     * 构造函数
     * @param x X坐标
     * @param y Y坐标
     * @param z Z坐标
     * @param w 四元数W分量
     * @param qx 四元数X分量
     * @param qy 四元数Y分量
     * @param qz 四元数Z分量
     */
    public Pose(float x, float y, float z, float w, float qx, float qy, float qz) {
        this.position = new Vector3r(x, y, z);
        this.orientation = new Quaternionr(w, qx, qy, qz);
    }
    
    /**
     * 获取位置
     * @return 位置向量
     */
    public Vector3r getPosition() {
        return position;
    }
    
    /**
     * 设置位置
     * @param position 位置向量
     */
    public void setPosition(Vector3r position) {
        this.position = position != null ? position : new Vector3r();
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
     * 计算到另一个位姿的距离
     * @param other 另一个位姿
     * @return 距离
     */
    public float distanceTo(Pose other) {
        if (other == null || other.position == null) {
            return Float.MAX_VALUE;
        }
        
        float dx = this.position.x - other.position.x;
        float dy = this.position.y - other.position.y;
        float dz = this.position.z - other.position.z;
        
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    /**
     * 克隆位姿
     * @return 位姿副本
     */
    public Pose clone() {
        return new Pose(
            new Vector3r(position.x, position.y, position.z),
            new Quaternionr(orientation.w, orientation.x, orientation.y, orientation.z)
        );
    }
    
    @Override
    public String toString() {
        return String.format("Pose(position=%s, orientation=%s)", 
                           position != null ? position.toString() : "null",
                           orientation != null ? orientation.toString() : "null");
    }
} 