package com.JP.dronesim.domain.airspace.model;

import com.JP.dronesim.domain.common.valueobjects.Position;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 简单的空间索引实现
 * 用于快速查找空域中的实体位置
 *
 * @author JP Team
 * @version 1.0
 */
public class SpatialIndex {

    /**
     * 实体位置映射
     */
    private final Map<String, Position> entityPositions;

    /**
     * 空域边界最小/最大坐标
     */
    private final double minX, minY, minZ, maxX, maxY, maxZ;

    /**
     * 构造函数
     *
     * @param minX 最小X
     * @param minY 最小Y
     * @param minZ 最小Z
     * @param maxX 最大X
     * @param maxY 最大Y
     * @param maxZ 最大Z
     */
    public SpatialIndex(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.entityPositions = new HashMap<>();
    }

    /**
     * 添加实体位置
     *
     * @param entityId 实体ID
     * @param position 位置
     */
    public void addEntity(String entityId, Position position) {
        if (entityId == null || entityId.trim().isEmpty()) {
            throw new IllegalArgumentException("实体ID不能为空");
        }
        if (position == null) {
            throw new IllegalArgumentException("位置不能为空");
        }

        entityPositions.put(entityId, position);
    }

    /**
     * 更新实体位置
     *
     * @param entityId 实体ID
     * @param newPosition 新位置
     */
    public void updateEntityPosition(String entityId, Position newPosition) {
        if (entityId == null || entityId.trim().isEmpty()) {
            throw new IllegalArgumentException("实体ID不能为空");
        }
        if (newPosition == null) {
            throw new IllegalArgumentException("新位置不能为空");
        }

        if (entityPositions.containsKey(entityId)) {
            entityPositions.put(entityId, newPosition);
        }
    }

    /**
     * 移除实体
     *
     * @param entityId 实体ID
     */
    public void removeEntity(String entityId) {
        if (entityId != null) {
            entityPositions.remove(entityId);
        }
    }

    /**
     * 获取实体位置
     *
     * @param entityId 实体ID
     * @return 位置，如果不存在则返回null
     */
    public Position getPosition(String entityId) {
        return entityPositions.get(entityId);
    }

    /**
     * 查询指定范围内的实体
     *
     * @param center 查询中心点
     * @param radius 查询半径
     * @return 范围内的实体ID列表
     */
    public List<String> queryInRange(Position center, double radius) {
        if (center == null) {
            throw new IllegalArgumentException("查询中心点不能为空");
        }
        if (radius <= 0) {
            throw new IllegalArgumentException("查询半径必须大于0");
        }

        return entityPositions.entrySet().stream()
                .filter(entry -> calculateDistance(center, entry.getValue()) <= radius)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * 查询指定矩形区域内的实体
     *
     * @param minX 最小X坐标
     * @param minY 最小Y坐标
     * @param minZ 最小Z坐标
     * @param maxX 最大X坐标
     * @param maxY 最大Y坐标
     * @param maxZ 最大Z坐标
     * @return 区域内的实体ID列表
     */
    public List<String> queryInBox(double minX, double minY, double minZ,
                                  double maxX, double maxY, double maxZ) {
        return entityPositions.entrySet().stream()
                .filter(entry -> {
                    Position pos = entry.getValue();
                    return pos.getX() >= minX && pos.getX() <= maxX &&
                           pos.getY() >= minY && pos.getY() <= maxY &&
                           pos.getZ() >= minZ && pos.getZ() <= maxZ;
                })
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    
    /**
     * 查找最近的实体
     * 
     * @param position 参考位置
     * @param maxDistance 最大距离
     * @return 最近的实体ID，如果没有找到则返回null
     */
    public String findNearest(Position position, double maxDistance) {
        if (position == null) {
            throw new IllegalArgumentException("参考位置不能为空");
        }
        if (maxDistance <= 0) {
            throw new IllegalArgumentException("最大距离必须大于0");
        }
        
        String nearestEntity = null;
        double nearestDistance = Double.MAX_VALUE;
        
        for (Map.Entry<String, Position> entry : entityPositions.entrySet()) {
            double distance = calculateDistance(position, entry.getValue());
            if (distance <= maxDistance && distance < nearestDistance) {
                nearestDistance = distance;
                nearestEntity = entry.getKey();
            }
        }
        
        return nearestEntity;
    }
    
    /**
     * 获取所有实体ID
     * 
     * @return 实体ID集合
     */
    public Set<String> getAllEntityIds() {
        return new HashSet<>(entityPositions.keySet());
    }
    
    /**
     * 获取实体数量
     * 
     * @return 实体数量
     */
    public int getEntityCount() {
        return entityPositions.size();
    }
    
    /**
     * 清空索引
     */
    public void clear() {
        entityPositions.clear();
    }
    
    /**
     * 检查是否包含指定实体
     * 
     * @param entityId 实体ID
     * @return 是否包含
     */
    public boolean contains(String entityId) {
        return entityPositions.containsKey(entityId);
    }
    
    /**
     * 计算两点之间的距离
     * 
     * @param pos1 位置1
     * @param pos2 位置2
     * @return 距离
     */
    private double calculateDistance(Position pos1, Position pos2) {
        double dx = pos1.getX() - pos2.getX();
        double dy = pos1.getY() - pos2.getY();
        double dz = pos1.getZ() - pos2.getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    /**
     * 检查位置是否在边界内
     * 
     * @param pos 位置
     * @return 是否在边界内
     */
    public boolean contains(Position pos) {
        return pos.getX() >= minX && pos.getX() <= maxX &&
               pos.getY() >= minY && pos.getY() <= maxY &&
               pos.getZ() >= minZ && pos.getZ() <= maxZ;
    }

    @Override
    public String toString() {
        return String.format("SpatialIndex{entities=%d, bounds=[%.1f,%.1f,%.1f] to [%.1f,%.1f,%.1f]}", 
                           entityPositions.size(), minX, minY, minZ, maxX, maxY, maxZ);
    }
}