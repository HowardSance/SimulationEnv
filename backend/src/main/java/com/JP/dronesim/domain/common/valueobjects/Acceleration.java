package com.JP.dronesim.domain.common.valueobjects;

import java.util.Objects;

/**
 * 加速度值对象
 * 表示无人机在NED坐标系中的三维加速度 (ax, ay, az)
 * 
 * @author JP Team
 * @version 1.0
 */
public class Acceleration {
    
    /**
     * X方向加速度（北方向，单位：m/s²）
     */
    private final double ax;
    
    /**
     * Y方向加速度（东方向，单位：m/s²）
     */
    private final double ay;
    
    /**
     * Z方向加速度（下方向，单位：m/s²）
     */
    private final double az;
    
    /**
     * 构造函数
     * 
     * @param ax X方向加速度
     * @param ay Y方向加速度
     * @param az Z方向加速度
     */
    public Acceleration(double ax, double ay, double az) {
        this.ax = ax;
        this.ay = ay;
        this.az = az;
    }
    
    /**
     * 获取X方向加速度
     * 
     * @return X方向加速度
     */
    public double getAx() {
        return ax;
    }
    
    /**
     * 获取Y方向加速度
     * 
     * @return Y方向加速度
     */
    public double getAy() {
        return ay;
    }
    
    /**
     * 获取Z方向加速度
     * 
     * @return Z方向加速度
     */
    public double getAz() {
        return az;
    }
    
    /**
     * 计算加速度的大小（标量）
     * 
     * @return 加速度大小
     */
    public double getMagnitude() {
        return Math.sqrt(ax * ax + ay * ay + az * az);
    }
    
    /**
     * 计算水平加速度大小
     * 
     * @return 水平加速度大小
     */
    public double getHorizontalMagnitude() {
        return Math.sqrt(ax * ax + ay * ay);
    }
    
    /**
     * 获取垂直加速度（Z方向）
     * 
     * @return 垂直加速度
     */
    public double getVerticalAcceleration() {
        return az;
    }
    
    /**
     * 加速度向量加法
     * 
     * @param other 另一个加速度向量
     * @return 合成加速度
     */
    public Acceleration add(Acceleration other) {
        if (other == null) {
            throw new IllegalArgumentException("加速度参数不能为空");
        }
        return new Acceleration(this.ax + other.ax, this.ay + other.ay, this.az + other.az);
    }
    
    /**
     * 加速度向量减法
     * 
     * @param other 另一个加速度向量
     * @return 相对加速度
     */
    public Acceleration subtract(Acceleration other) {
        if (other == null) {
            throw new IllegalArgumentException("加速度参数不能为空");
        }
        return new Acceleration(this.ax - other.ax, this.ay - other.ay, this.az - other.az);
    }
    
    /**
     * 加速度向量标量乘法
     * 
     * @param scalar 标量
     * @return 缩放后的加速度
     */
    public Acceleration multiply(double scalar) {
        return new Acceleration(this.ax * scalar, this.ay * scalar, this.az * scalar);
    }
    
    /**
     * 限制加速度大小
     * 
     * @param maxAcceleration 最大加速度
     * @return 限制后的加速度
     */
    public Acceleration limit(double maxAcceleration) {
        if (maxAcceleration < 0) {
            throw new IllegalArgumentException("最大加速度不能为负数");
        }
        
        double currentMagnitude = getMagnitude();
        if (currentMagnitude <= maxAcceleration) {
            return this;
        }
        
        double scale = maxAcceleration / currentMagnitude;
        return multiply(scale);
    }
    
    /**
     * 根据加速度和时间步长计算速度变化
     * 
     * @param deltaTime 时间步长（秒）
     * @return 速度变化量
     */
    public Velocity toVelocityDelta(double deltaTime) {
        if (deltaTime < 0) {
            throw new IllegalArgumentException("时间步长不能为负数");
        }
        return new Velocity(ax * deltaTime, ay * deltaTime, az * deltaTime);
    }
    
    /**
     * 创建零加速度
     * 
     * @return 零加速度向量
     */
    public static Acceleration zero() {
        return new Acceleration(0.0, 0.0, 0.0);
    }
    
    /**
     * 创建重力加速度（向下）
     * 
     * @return 重力加速度
     */
    public static Acceleration gravity() {
        return new Acceleration(0.0, 0.0, 9.81); // 标准重力加速度
    }
    
    /**
     * 从速度变化和时间计算加速度
     * 
     * @param velocityChange 速度变化
     * @param deltaTime 时间间隔
     * @return 加速度
     */
    public static Acceleration fromVelocityChange(Velocity velocityChange, double deltaTime) {
        if (velocityChange == null) {
            throw new IllegalArgumentException("速度变化不能为空");
        }
        if (deltaTime <= 0) {
            throw new IllegalArgumentException("时间间隔必须大于零");
        }
        
        return new Acceleration(
            velocityChange.getVx() / deltaTime,
            velocityChange.getVy() / deltaTime,
            velocityChange.getVz() / deltaTime
        );
    }
    
    /**
     * 检查是否为零加速度
     * 
     * @param tolerance 容差值
     * @return 是否为零加速度
     */
    public boolean isZero(double tolerance) {
        return getMagnitude() < tolerance;
    }
    
    /**
     * 检查是否为零加速度（使用默认容差）
     * 
     * @return 是否为零加速度
     */
    public boolean isZero() {
        return isZero(0.001); // 默认容差 0.001 m/s²
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Acceleration that = (Acceleration) obj;
        return Double.compare(that.ax, ax) == 0 &&
               Double.compare(that.ay, ay) == 0 &&
               Double.compare(that.az, az) == 0;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(ax, ay, az);
    }
    
    @Override
    public String toString() {
        return String.format("Acceleration{ax=%.2f, ay=%.2f, az=%.2f, magnitude=%.2f m/s²}", 
                           ax, ay, az, getMagnitude());
    }
} 