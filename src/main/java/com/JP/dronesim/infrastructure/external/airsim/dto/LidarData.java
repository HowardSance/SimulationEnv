package com.JP.dronesim.infrastructure.external.airsim.dto;

import com.JP.dronesim.infrastructure.messaging.serialization.AirSimRpcMessageTrait;
import org.msgpack.annotation.Message;

/**
 * 激光雷达数据传输对象
 * 用于与AirSim交换激光雷达传感器数据
 */
@Message
public class LidarData extends AirSimRpcMessageTrait {
    
    /**
     * 点云数据
     * 注意：实际实现中应该是float数组，这里简化为单个值
     */
    public float[] point_cloud;
    
    /**
     * 时间戳（纳秒）
     */
    public long time_stamp;
    
    /**
     * 传感器相对位姿
     */
    public Pose pose;
    
    /**
     * 分割信息
     */
    public int[] segmentation;
    
    /**
     * 点云点数
     */
    public int point_count;
    
    /**
     * 默认构造函数
     */
    public LidarData() {
        this.point_cloud = new float[0];
        this.time_stamp = 0L;
        this.pose = new Pose();
        this.segmentation = new int[0];
        this.point_count = 0;
    }
    
    /**
     * 构造函数
     * @param pointCloud 点云数据
     * @param timeStamp 时间戳
     * @param pose 传感器位姿
     * @param segmentation 分割信息
     */
    public LidarData(float[] pointCloud, long timeStamp, Pose pose, int[] segmentation) {
        this.point_cloud = pointCloud != null ? pointCloud : new float[0];
        this.time_stamp = timeStamp;
        this.pose = pose != null ? pose : new Pose();
        this.segmentation = segmentation != null ? segmentation : new int[0];
        this.point_count = this.point_cloud.length / 3; // 假设每个点有x,y,z三个坐标
    }
    
    /**
     * 获取点云数据
     * @return 点云数据数组
     */
    public float[] getPointCloud() {
        return point_cloud;
    }
    
    /**
     * 设置点云数据
     * @param pointCloud 点云数据
     */
    public void setPointCloud(float[] pointCloud) {
        this.point_cloud = pointCloud != null ? pointCloud : new float[0];
        this.point_count = this.point_cloud.length / 3;
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
     * 获取传感器位姿
     * @return 传感器位姿
     */
    public Pose getPose() {
        return pose;
    }
    
    /**
     * 设置传感器位姿
     * @param pose 传感器位姿
     */
    public void setPose(Pose pose) {
        this.pose = pose != null ? pose : new Pose();
    }
    
    /**
     * 获取点云点数
     * @return 点云点数
     */
    public int getPointCount() {
        return point_count;
    }
    
    /**
     * 判断是否有有效数据
     * @return 是否有有效数据
     */
    public boolean hasValidData() {
        return point_cloud != null && point_cloud.length > 0 && time_stamp > 0;
    }
    
    /**
     * 获取指定索引的点坐标
     * @param index 点索引
     * @return 点坐标 [x, y, z]，如果索引无效返回null
     */
    public float[] getPoint(int index) {
        if (point_cloud == null || index < 0 || index >= point_count) {
            return null;
        }
        
        int baseIndex = index * 3;
        if (baseIndex + 2 >= point_cloud.length) {
            return null;
        }
        
        return new float[]{
            point_cloud[baseIndex],
            point_cloud[baseIndex + 1],
            point_cloud[baseIndex + 2]
        };
    }
    
    /**
     * 计算点云的边界框
     * @return 边界框 [minX, minY, minZ, maxX, maxY, maxZ]，如果无数据返回null
     */
    public float[] getBoundingBox() {
        if (!hasValidData() || point_count == 0) {
            return null;
        }
        
        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, minZ = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE, maxZ = Float.MIN_VALUE;
        
        for (int i = 0; i < point_count; i++) {
            int baseIndex = i * 3;
            if (baseIndex + 2 < point_cloud.length) {
                float x = point_cloud[baseIndex];
                float y = point_cloud[baseIndex + 1];
                float z = point_cloud[baseIndex + 2];
                
                minX = Math.min(minX, x);
                minY = Math.min(minY, y);
                minZ = Math.min(minZ, z);
                maxX = Math.max(maxX, x);
                maxY = Math.max(maxY, y);
                maxZ = Math.max(maxZ, z);
            }
        }
        
        return new float[]{minX, minY, minZ, maxX, maxY, maxZ};
    }
    
    @Override
    public String toString() {
        return String.format("LidarData(point_count=%d, time_stamp=%d, pose=%s, has_data=%s)",
                           point_count,
                           time_stamp,
                           pose != null ? pose.toString() : "null",
                           hasValidData());
    }
} 