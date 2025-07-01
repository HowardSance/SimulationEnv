package com.JP.dronesim.domain.airspace.model;

import com.JP.dronesim.domain.common.valueobjects.Position;

/**
 * 空域边界值对象
 * 定义三维空域的边界范围
 * 
 * @author JP Team
 * @version 1.0
 */
public class AirspaceBoundary {
    
    /**
     * 最小坐标点（西南下角）
     */
    private final Position minPoint;
    
    /**
     * 最大坐标点（东北上角）
     */
    private final Position maxPoint;
    
    /**
     * 构造函数
     * 
     * @param minPoint 最小坐标点
     * @param maxPoint 最大坐标点
     */
    public AirspaceBoundary(Position minPoint, Position maxPoint) {
        if (minPoint == null || maxPoint == null) {
            throw new IllegalArgumentException("边界坐标点不能为空");
        }
        
        validateBoundary(minPoint, maxPoint);
        
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
    }
    
    /**
     * 构造函数（使用具体坐标）
     * 
     * @param minX 最小X坐标
     * @param minY 最小Y坐标
     * @param minZ 最小Z坐标
     * @param maxX 最大X坐标
     * @param maxY 最大Y坐标
     * @param maxZ 最大Z坐标
     */
    public AirspaceBoundary(double minX, double minY, double minZ, 
                           double maxX, double maxY, double maxZ) {
        this(new Position(minX, minY, minZ), new Position(maxX, maxY, maxZ));
    }
    
    /**
     * 检查位置是否在边界内
     * 
     * @param position 位置
     * @return 是否在边界内
     */
    public boolean contains(Position position) {
        if (position == null) {
            return false;
        }
        
        return position.getX() >= minPoint.getX() && position.getX() <= maxPoint.getX() &&
               position.getY() >= minPoint.getY() && position.getY() <= maxPoint.getY() &&
               position.getZ() >= minPoint.getZ() && position.getZ() <= maxPoint.getZ();
    }
    
    /**
     * 获取边界中心点
     * 
     * @return 中心点位置
     */
    public Position getCenter() {
        double centerX = (minPoint.getX() + maxPoint.getX()) / 2.0;
        double centerY = (minPoint.getY() + maxPoint.getY()) / 2.0;
        double centerZ = (minPoint.getZ() + maxPoint.getZ()) / 2.0;
        
        return new Position(centerX, centerY, centerZ);
    }
    
    /**
     * 获取边界体积
     * 
     * @return 体积（立方米）
     */
    public double getVolume() {
        double width = maxPoint.getX() - minPoint.getX();
        double height = maxPoint.getY() - minPoint.getY();
        double depth = maxPoint.getZ() - minPoint.getZ();
        
        return width * height * depth;
    }
    
    /**
     * 验证边界有效性
     * 
     * @param minPoint 最小点
     * @param maxPoint 最大点
     */
    private void validateBoundary(Position minPoint, Position maxPoint) {
        if (minPoint.getX() >= maxPoint.getX() ||
            minPoint.getY() >= maxPoint.getY() ||
            minPoint.getZ() >= maxPoint.getZ()) {
            throw new IllegalArgumentException("边界定义无效：最小点坐标必须小于最大点坐标");
        }
    }
    
    // Getters
    public Position getMinPoint() { return minPoint; }
    public Position getMaxPoint() { return maxPoint; }
    public double getMinX() { return minPoint.getX(); }
    public double getMinY() { return minPoint.getY(); }
    public double getMinZ() { return minPoint.getZ(); }
    public double getMaxX() { return maxPoint.getX(); }
    public double getMaxY() { return maxPoint.getY(); }
    public double getMaxZ() { return maxPoint.getZ(); }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        AirspaceBoundary that = (AirspaceBoundary) obj;
        return minPoint.equals(that.minPoint) && maxPoint.equals(that.maxPoint);
    }
    
    @Override
    public int hashCode() {
        return minPoint.hashCode() * 31 + maxPoint.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("AirspaceBoundary{min=%s, max=%s}", minPoint, maxPoint);
    }
} 