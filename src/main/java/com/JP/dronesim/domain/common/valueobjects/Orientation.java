package com.JP.dronesim.domain.common.valueobjects;

import java.util.Objects;

/**
 * 姿态值对象
 * 表示实体的三维姿态，使用四元数表示
 * 
 * @author JP Team
 * @version 1.0
 */
public class Orientation {
    
    /**
     * 四元数的w分量（标量部分）
     */
    private final double w;
    
    /**
     * 四元数的x分量（向量部分）
     */
    private final double x;
    
    /**
     * 四元数的y分量（向量部分）
     */
    private final double y;
    
    /**
     * 四元数的z分量（向量部分）
     */
    private final double z;
    
    /**
     * 构造函数 - 使用四元数
     * 
     * @param w 四元数w分量
     * @param x 四元数x分量
     * @param y 四元数y分量
     * @param z 四元数z分量
     */
    public Orientation(double w, double x, double y, double z) {
        // 归一化四元数
        double norm = Math.sqrt(w * w + x * x + y * y + z * z);
        if (norm == 0.0) {
            throw new IllegalArgumentException("四元数不能为零向量");
        }
        
        this.w = w / norm;
        this.x = x / norm;
        this.y = y / norm;
        this.z = z / norm;
    }
    
    /**
     * 从欧拉角创建姿态对象
     * 
     * @param roll 滚转角（弧度）- 绕X轴旋转
     * @param pitch 俯仰角（弧度）- 绕Y轴旋转
     * @param yaw 偏航角（弧度）- 绕Z轴旋转
     * @return 姿态对象
     */
    public static Orientation fromEulerAngles(double roll, double pitch, double yaw) {
        double cr = Math.cos(roll * 0.5);
        double sr = Math.sin(roll * 0.5);
        double cp = Math.cos(pitch * 0.5);
        double sp = Math.sin(pitch * 0.5);
        double cy = Math.cos(yaw * 0.5);
        double sy = Math.sin(yaw * 0.5);
        
        double w = cr * cp * cy + sr * sp * sy;
        double x = sr * cp * cy - cr * sp * sy;
        double y = cr * sp * cy + sr * cp * sy;
        double z = cr * cp * sy - sr * sp * cy;
        
        return new Orientation(w, x, y, z);
    }
    
    /**
     * 获取四元数w分量
     * 
     * @return w分量
     */
    public double getW() {
        return w;
    }
    
    /**
     * 获取四元数x分量
     * 
     * @return x分量
     */
    public double getX() {
        return x;
    }
    
    /**
     * 获取四元数y分量
     * 
     * @return y分量
     */
    public double getY() {
        return y;
    }
    
    /**
     * 获取四元数z分量
     * 
     * @return z分量
     */
    public double getZ() {
        return z;
    }
    
    /**
     * 转换为欧拉角
     * 
     * @return 欧拉角数组 [roll, pitch, yaw]（弧度）
     */
    public double[] toEulerAngles() {
        double[] angles = new double[3];
        
        // Roll (x-axis rotation)
        double sinr_cosp = 2 * (w * x + y * z);
        double cosr_cosp = 1 - 2 * (x * x + y * y);
        angles[0] = Math.atan2(sinr_cosp, cosr_cosp);
        
        // Pitch (y-axis rotation)
        double sinp = 2 * (w * y - z * x);
        if (Math.abs(sinp) >= 1) {
            angles[1] = Math.copySign(Math.PI / 2, sinp); // use 90 degrees if out of range
        } else {
            angles[1] = Math.asin(sinp);
        }
        
        // Yaw (z-axis rotation)
        double siny_cosp = 2 * (w * z + x * y);
        double cosy_cosp = 1 - 2 * (y * y + z * z);
        angles[2] = Math.atan2(siny_cosp, cosy_cosp);
        
        return angles;
    }
    
    /**
     * 获取滚转角（弧度）
     * 
     * @return 滚转角
     */
    public double getRoll() {
        return toEulerAngles()[0];
    }
    
    /**
     * 获取俯仰角（弧度）
     * 
     * @return 俯仰角
     */
    public double getPitch() {
        return toEulerAngles()[1];
    }
    
    /**
     * 获取偏航角（弧度）
     * 
     * @return 偏航角
     */
    public double getYaw() {
        return toEulerAngles()[2];
    }
    
    /**
     * 四元数乘法（组合旋转）
     * 
     * @param other 另一个姿态
     * @return 组合后的姿态
     */
    public Orientation multiply(Orientation other) {
        if (other == null) {
            throw new IllegalArgumentException("姿态参数不能为空");
        }
        
        double newW = this.w * other.w - this.x * other.x - this.y * other.y - this.z * other.z;
        double newX = this.w * other.x + this.x * other.w + this.y * other.z - this.z * other.y;
        double newY = this.w * other.y - this.x * other.z + this.y * other.w + this.z * other.x;
        double newZ = this.w * other.z + this.x * other.y - this.y * other.x + this.z * other.w;
        
        return new Orientation(newW, newX, newY, newZ);
    }
    
    /**
     * 创建单位姿态（无旋转）
     * 
     * @return 单位姿态
     */
    public static Orientation identity() {
        return new Orientation(1.0, 0.0, 0.0, 0.0);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Orientation that = (Orientation) obj;
        return Double.compare(that.w, w) == 0 &&
               Double.compare(that.x, x) == 0 &&
               Double.compare(that.y, y) == 0 &&
               Double.compare(that.z, z) == 0;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(w, x, y, z);
    }
    
    @Override
    public String toString() {
        double[] euler = toEulerAngles();
        return String.format("Orientation{quaternion(w=%.3f, x=%.3f, y=%.3f, z=%.3f), euler(roll=%.2f°, pitch=%.2f°, yaw=%.2f°)}", 
                           w, x, y, z, 
                           Math.toDegrees(euler[0]), Math.toDegrees(euler[1]), Math.toDegrees(euler[2]));
    }
} 