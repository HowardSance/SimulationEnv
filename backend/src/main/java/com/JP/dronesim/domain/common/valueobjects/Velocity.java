package com.JP.dronesim.domain.common.valueobjects;

import java.util.Objects;

/**
 * 速度值对象
 * 表示无人机在NED坐标系中的三维速度 (vx, vy, vz)
 * 
 * @author JP Team
 * @version 1.0
 */
public class Velocity {
    
    /**
     * X方向速度（北方向，单位：m/s）
     */
    private final double vx;
    
    /**
     * Y方向速度（东方向，单位：m/s）
     */
    private final double vy;
    
    /**
     * Z方向速度（下方向，单位：m/s）
     */
    private final double vz;
    
    /**
     * 构造函数
     * 
     * @param vx X方向速度
     * @param vy Y方向速度
     * @param vz Z方向速度
     */
    public Velocity(double vx, double vy, double vz) {
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }
    
    /**
     * 获取X方向速度
     * 
     * @return X方向速度
     */
    public double getVx() {
        return vx;
    }
    
    /**
     * 获取Y方向速度
     * 
     * @return Y方向速度
     */
    public double getVy() {
        return vy;
    }
    
    /**
     * 获取Z方向速度
     * 
     * @return Z方向速度
     */
    public double getVz() {
        return vz;
    }
    
    /**
     * 计算速度的大小（标量）
     * 
     * @return 速度大小
     */
    public double getMagnitude() {
        return Math.sqrt(vx * vx + vy * vy + vz * vz);
    }
    
    /**
     * 计算水平速度大小
     * 
     * @return 水平速度大小
     */
    public double getHorizontalMagnitude() {
        return Math.sqrt(vx * vx + vy * vy);
    }
    
    /**
     * 获取垂直速度（Z方向）
     * 
     * @return 垂直速度
     */
    public double getVerticalVelocity() {
        return vz;
    }
    
    /**
     * 计算速度方向角（弧度）
     * 
     * @return 速度方向角，从北方向顺时针测量
     */
    public double getDirection() {
        return Math.atan2(vy, vx);
    }
    
    /**
     * 速度向量加法
     * 
     * @param other 另一个速度向量
     * @return 合成速度
     */
    public Velocity add(Velocity other) {
        if (other == null) {
            throw new IllegalArgumentException("速度参数不能为空");
        }
        return new Velocity(this.vx + other.vx, this.vy + other.vy, this.vz + other.vz);
    }
    
    /**
     * 速度向量减法
     * 
     * @param other 另一个速度向量
     * @return 相对速度
     */
    public Velocity subtract(Velocity other) {
        if (other == null) {
            throw new IllegalArgumentException("速度参数不能为空");
        }
        return new Velocity(this.vx - other.vx, this.vy - other.vy, this.vz - other.vz);
    }
    
    /**
     * 速度向量标量乘法
     * 
     * @param scalar 标量
     * @return 缩放后的速度
     */
    public Velocity multiply(double scalar) {
        return new Velocity(this.vx * scalar, this.vy * scalar, this.vz * scalar);
    }
    
    /**
     * 获取单位方向向量
     * 
     * @return 单位速度向量
     */
    public Velocity normalize() {
        double magnitude = getMagnitude();
        if (magnitude == 0.0) {
            return zero();
        }
        return new Velocity(vx / magnitude, vy / magnitude, vz / magnitude);
    }
    
    /**
     * 限制速度大小
     * 
     * @param maxSpeed 最大速度
     * @return 限制后的速度
     */
    public Velocity limit(double maxSpeed) {
        if (maxSpeed < 0) {
            throw new IllegalArgumentException("最大速度不能为负数");
        }
        
        double currentMagnitude = getMagnitude();
        if (currentMagnitude <= maxSpeed) {
            return this;
        }
        
        double scale = maxSpeed / currentMagnitude;
        return multiply(scale);
    }
    
    /**
     * 创建零速度
     * 
     * @return 零速度向量
     */
    public static Velocity zero() {
        return new Velocity(0.0, 0.0, 0.0);
    }
    
    /**
     * 从极坐标创建水平速度
     * 
     * @param speed 速度大小
     * @param direction 方向角（弧度）
     * @param verticalSpeed 垂直速度
     * @return 速度对象
     */
    public static Velocity fromPolar(double speed, double direction, double verticalSpeed) {
        double vx = speed * Math.cos(direction);
        double vy = speed * Math.sin(direction);
        return new Velocity(vx, vy, verticalSpeed);
    }
    
    /**
     * 检查是否为静止状态
     * 
     * @param tolerance 容差值
     * @return 是否静止
     */
    public boolean isStationary(double tolerance) {
        return getMagnitude() < tolerance;
    }
    
    /**
     * 检查是否为静止状态（使用默认容差）
     * 
     * @return 是否静止
     */
    public boolean isStationary() {
        return isStationary(0.01); // 默认容差 0.01 m/s
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Velocity velocity = (Velocity) obj;
        return Double.compare(velocity.vx, vx) == 0 &&
               Double.compare(velocity.vy, vy) == 0 &&
               Double.compare(velocity.vz, vz) == 0;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(vx, vy, vz);
    }
    
    @Override
    public String toString() {
        return String.format("Velocity{vx=%.2f, vy=%.2f, vz=%.2f, magnitude=%.2f m/s}", 
                           vx, vy, vz, getMagnitude());
    }
} 