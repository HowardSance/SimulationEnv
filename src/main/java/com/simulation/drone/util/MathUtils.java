package com.simulation.drone.util;

import java.util.Map;

/**
 * 数学工具类
 * 提供常用的数学计算功能
 */
public class MathUtils {
    
    private MathUtils() {
        // 私有构造函数，防止实例化
    }
    
    /**
     * 计算两点之间的距离
     * @param pos1 第一个点的位置
     * @param pos2 第二个点的位置
     * @return 两点之间的距离
     */
    public static double calculateDistance(Map<String, Double> pos1, Map<String, Double> pos2) {
        double dx = pos2.get("x") - pos1.get("x");
        double dy = pos2.get("y") - pos1.get("y");
        double dz = pos2.get("z") - pos1.get("z");
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    /**
     * 计算两个向量之间的角度
     * @param v1 第一个向量
     * @param v2 第二个向量
     * @return 角度（度）
     */
    public static double calculateAngle(Map<String, Double> v1, Map<String, Double> v2) {
        double dotProduct = v1.get("x") * v2.get("x") + v1.get("y") * v2.get("y") + v1.get("z") * v2.get("z");
        double magnitude1 = Math.sqrt(v1.get("x") * v1.get("x") + v1.get("y") * v1.get("y") + v1.get("z") * v1.get("z"));
        double magnitude2 = Math.sqrt(v2.get("x") * v2.get("x") + v2.get("y") * v2.get("y") + v2.get("z") * v2.get("z"));
        return Math.acos(dotProduct / (magnitude1 * magnitude2)) * 180 / Math.PI;
    }
    
    /**
     * 计算目标相对于设备的方向角
     * @param devicePos 设备位置
     * @param deviceOrientation 设备朝向
     * @param targetPos 目标位置
     * @return 方向角（度）
     */
    public static double calculateDirectionAngle(Map<String, Double> devicePos, Map<String, Double> deviceOrientation, Map<String, Double> targetPos) {
        // 计算目标相对于设备的方向向量
        double dx = targetPos.get("x") - devicePos.get("x");
        double dy = targetPos.get("y") - devicePos.get("y");
        double dz = targetPos.get("z") - devicePos.get("z");
        
        // 获取设备朝向
        double deviceYaw = deviceOrientation.get("yaw");
        double devicePitch = deviceOrientation.get("pitch");
        
        // 计算目标方向角
        double targetYaw = Math.atan2(dy, dx) * 180 / Math.PI;
        double targetPitch = Math.atan2(dz, Math.sqrt(dx * dx + dy * dy)) * 180 / Math.PI;
        
        // 计算角度差
        double yawDiff = Math.abs(targetYaw - deviceYaw);
        double pitchDiff = Math.abs(targetPitch - devicePitch);
        
        return Math.max(yawDiff, pitchDiff);
    }
    
    /**
     * 计算探测概率
     * @param distance 距离
     * @param maxRange 最大探测范围
     * @param minRange 最小探测范围
     * @param angle 角度
     * @param maxAngle 最大探测角度
     * @param environmentFactor 环境因子
     * @param targetFactor 目标因子
     * @return 探测概率
     */
    public static double calculateDetectionProbability(
            double distance, double maxRange, double minRange,
            double angle, double maxAngle,
            double environmentFactor, double targetFactor) {
        
        // 检查距离和角度是否在有效范围内
        if (distance < minRange || distance > maxRange || Math.abs(angle) > maxAngle) {
            return 0.0;
        }
        
        // 计算基础探测概率
        double baseProbability = 1.0 - (distance / maxRange);
        
        // 应用环境因素和目标特征
        return baseProbability * environmentFactor * targetFactor;
    }
    
    /**
     * 计算有效探测范围
     * @param maxRange 设备最大探测范围
     * @param visibility 能见度
     * @param weatherFactor 天气因子
     * @return 有效探测范围
     */
    public static double calculateEffectiveRange(double maxRange, double visibility, double weatherFactor) {
        return Math.min(maxRange, visibility * weatherFactor);
    }
    
    /**
     * 将角度限制在指定范围内
     * @param angle 角度
     * @param min 最小角度
     * @param max 最大角度
     * @return 限制后的角度
     */
    public static double clampAngle(double angle, double min, double max) {
        while (angle < min) {
            angle += 360;
        }
        while (angle > max) {
            angle -= 360;
        }
        return angle;
    }
    
    /**
     * 将值限制在指定范围内
     * @param value 值
     * @param min 最小值
     * @param max 最大值
     * @return 限制后的值
     */
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * 计算两点之间的方位角
     * @param pos1 第一个点的位置
     * @param pos2 第二个点的位置
     * @return 方位角（度）
     */
    public static double calculateBearing(Map<String, Double> pos1, Map<String, Double> pos2) {
        double dx = pos2.get("x") - pos1.get("x");
        double dy = pos2.get("y") - pos1.get("y");
        return Math.atan2(dy, dx) * 180 / Math.PI;
    }
    
    /**
     * 计算两点之间的俯仰角
     * @param pos1 第一个点的位置
     * @param pos2 第二个点的位置
     * @return 俯仰角（度）
     */
    public static double calculateElevation(Map<String, Double> pos1, Map<String, Double> pos2) {
        double dx = pos2.get("x") - pos1.get("x");
        double dy = pos2.get("y") - pos1.get("y");
        double dz = pos2.get("z") - pos1.get("z");
        double horizontalDistance = Math.sqrt(dx * dx + dy * dy);
        return Math.atan2(dz, horizontalDistance) * 180 / Math.PI;
    }
} 