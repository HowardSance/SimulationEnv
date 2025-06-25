package com.JP.dronesim.domain.common.valueobjects;

import java.util.Objects;

/**
 * 位置值对象
 * 表示实体在NED坐标系中的三维位置 (x, y, z)
 * NED坐标系：North-East-Down，北-东-下坐标系
 * 
 * @author JP Team
 * @version 1.0
 */
public class Position {
    
    /**
     * X坐标（北方向，单位：米）
     */
    private final double x;
    
    /**
     * Y坐标（东方向，单位：米）
     */
    private final double y;
    
    /**
     * Z坐标（下方向，单位：米）
     */
    private final double z;
    
    /**
     * 构造函数
     * 
     * @param x X坐标（北方向）
     * @param y Y坐标（东方向）
     * @param z Z坐标（下方向）
     */
    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * 获取X坐标
     * 
     * @return X坐标值
     */
    public double getX() {
        return x;
    }
    
    /**
     * 获取Y坐标
     * 
     * @return Y坐标值
     */
    public double getY() {
        return y;
    }
    
    /**
     * 获取Z坐标
     * 
     * @return Z坐标值
     */
    public double getZ() {
        return z;
    }
    
    /**
     * 计算到另一个位置的距离
     * 
     * @param other 另一个位置
     * @return 欧几里得距离
     */
    public double distanceTo(Position other) {
        if (other == null) {
            throw new IllegalArgumentException("目标位置不能为空");
        }
        
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double dz = this.z - other.z;
        
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    /**
     * 计算到另一个位置的方位角（弧度）
     * 
     * @param other 另一个位置
     * @return 方位角（弧度），从北方向顺时针测量
     */
    public double bearingTo(Position other) {
        if (other == null) {
            throw new IllegalArgumentException("目标位置不能为空");
        }
        
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        
        return Math.atan2(dy, dx);
    }
    
    /**
     * 在当前位置基础上添加偏移量
     * 
     * @param deltaX X方向偏移量
     * @param deltaY Y方向偏移量
     * @param deltaZ Z方向偏移量
     * @return 新的位置对象
     */
    public Position add(double deltaX, double deltaY, double deltaZ) {
        return new Position(this.x + deltaX, this.y + deltaY, this.z + deltaZ);
    }
    
    /**
     * 在当前位置基础上添加另一个位置的坐标
     * 
     * @param other 另一个位置
     * @return 新的位置对象
     */
    public Position add(Position other) {
        if (other == null) {
            throw new IllegalArgumentException("位置参数不能为空");
        }
        return new Position(this.x + other.x, this.y + other.y, this.z + other.z);
    }
    
    /**
     * 创建零位置（原点）
     * 
     * @return 原点位置
     */
    public static Position zero() {
        return new Position(0.0, 0.0, 0.0);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return Double.compare(position.x, x) == 0 &&
               Double.compare(position.y, y) == 0 &&
               Double.compare(position.z, z) == 0;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
    
    @Override
    public String toString() {
        return String.format("Position{x=%.2f, y=%.2f, z=%.2f}", x, y, z);
    }
} 