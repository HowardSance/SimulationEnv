package com.simulation.drone.engine.impl;

import com.simulation.drone.engine.DetectionEngine;
import com.simulation.drone.entity.DetectionDevice;
import com.simulation.drone.entity.Drone;
import com.simulation.drone.entity.EnvironmentConfig;
import com.simulation.drone.entity.TargetCharacteristic;
import com.simulation.drone.util.MathUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class DetectionEngineImpl implements DetectionEngine {
    
    private EnvironmentConfig environmentConfig;
    private final Map<String, TargetCharacteristic> targetCharacteristics = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> deviceStatus = new ConcurrentHashMap<>();
    
    @Override
    public void initialize(EnvironmentConfig config) {
        this.environmentConfig = config;
        log.info("探测引擎初始化完成，环境配置：{}", config.getName());
    }
    
    @Override
    public Map<String, Object> detect(DetectionDevice device, List<Drone> targets, Map<String, Object> environment) {
        Map<String, Object> results = new HashMap<>();
        List<Map<String, Object>> detections = new ArrayList<>();
        
        for (Drone target : targets) {
            double probability = calculateDetectionProbability(device, target, environment);
            if (Math.random() < probability) {
                Map<String, Object> detection = new HashMap<>();
                detection.put("targetId", target.getId());
                detection.put("position", target.getPosition());
                detection.put("velocity", target.getVelocity());
                detection.put("confidence", probability);
                detection.put("timestamp", System.currentTimeMillis());
                detections.add(detection);
            }
        }
        
        results.put("deviceId", device.getId());
        results.put("detections", detections);
        results.put("timestamp", System.currentTimeMillis());
        
        // 更新设备状态
        updateDeviceStatus(device, results);
        
        return results;
    }
    
    @Override
    public double calculateDetectionProbability(DetectionDevice device, Drone target, Map<String, Object> environment) {
        // 获取设备参数
        Map<String, Object> deviceParams = device.getParameters();
        double maxRange = (double) deviceParams.getOrDefault("maxRange", 1000.0);
        double minRange = (double) deviceParams.getOrDefault("minRange", 100.0);
        double maxAngle = (double) deviceParams.getOrDefault("maxAngle", 60.0);
        
        // 计算目标距离
        double distance = calculateDistance(device.getPosition(), target.getPosition());
        if (distance < minRange || distance > maxRange) {
            return 0.0;
        }
        
        // 计算目标角度
        double angle = calculateAngle(device.getPosition(), device.getOrientation(), target.getPosition());
        if (Math.abs(angle) > maxAngle) {
            return 0.0;
        }
        
        // 获取环境参数
        double visibility = (double) environment.getOrDefault("visibility", 10000.0);
        double weatherFactor = (double) environment.getOrDefault("weatherFactor", 1.0);
        
        // 计算基础探测概率
        double baseProbability = 1.0 - (distance / maxRange);
        
        // 应用环境因素
        double environmentFactor = Math.min(1.0, visibility / maxRange) * weatherFactor;
        
        // 应用目标特征
        TargetCharacteristic characteristic = targetCharacteristics.get(target.getType());
        double targetFactor = characteristic != null ? characteristic.getDetectionFactor() : 1.0;
        
        return baseProbability * environmentFactor * targetFactor;
    }
    
    @Override
    public Map<String, Object> calculateDetectionRange(DetectionDevice device, Map<String, Object> environment) {
        Map<String, Object> range = new HashMap<>();
        Map<String, Object> deviceParams = device.getParameters();
        
        double maxRange = (double) deviceParams.getOrDefault("maxRange", 1000.0);
        double minRange = (double) deviceParams.getOrDefault("minRange", 100.0);
        double maxAngle = (double) deviceParams.getOrDefault("maxAngle", 60.0);
        
        // 获取环境参数
        double visibility = (double) environment.getOrDefault("visibility", 10000.0);
        double weatherFactor = (double) environment.getOrDefault("weatherFactor", 1.0);
        
        // 计算有效探测范围
        double effectiveRange = Math.min(maxRange, visibility * weatherFactor);
        
        range.put("maxRange", effectiveRange);
        range.put("minRange", minRange);
        range.put("maxAngle", maxAngle);
        range.put("position", device.getPosition());
        range.put("orientation", device.getOrientation());
        
        return range;
    }
    
    @Override
    public void updateDeviceParameters(DetectionDevice device, Map<String, Object> parameters) {
        device.getParameters().putAll(parameters);
        log.info("更新设备参数：{}", device.getId());
    }
    
    @Override
    public void updateTargetCharacteristics(List<TargetCharacteristic> characteristics) {
        targetCharacteristics.clear();
        for (TargetCharacteristic characteristic : characteristics) {
            targetCharacteristics.put(characteristic.getId(), characteristic);
        }
        log.info("更新目标特征，数量：{}", characteristics.size());
    }
    
    @Override
    public Map<String, Object> getDeviceStatus(DetectionDevice device) {
        return deviceStatus.getOrDefault(device.getId(), new HashMap<>());
    }
    
    @Override
    public void calibrateDevice(DetectionDevice device) {
        // 重置设备状态
        deviceStatus.put(device.getId(), new HashMap<>());
        log.info("校准设备：{}", device.getId());
    }
    
    private void updateDeviceStatus(DetectionDevice device, Map<String, Object> detectionResults) {
        Map<String, Object> status = deviceStatus.computeIfAbsent(device.getId(), k -> new HashMap<>());
        status.put("lastDetection", detectionResults);
        status.put("lastUpdate", System.currentTimeMillis());
        status.put("detectionCount", ((List<?>) detectionResults.get("detections")).size());
    }
    
    private double calculateDistance(Map<String, Double> pos1, Map<String, Double> pos2) {
        double dx = pos2.get("x") - pos1.get("x");
        double dy = pos2.get("y") - pos1.get("y");
        double dz = pos2.get("z") - pos1.get("z");
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    private double calculateAngle(Map<String, Double> devicePos, Map<String, Double> deviceOrientation, Map<String, Double> targetPos) {
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
} 