package com.JP.dronesim.domain.uav.model;

import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.common.enums.UAVStatus;
import java.util.Objects;

/**
 * 物理特征签名值对象
 * 包含无人机当前的RCS、光学特征、无线电发射功率等信息
 * 供探测设备进行探测计算使用
 * 
 * @author JP Team
 * @version 1.0
 */
public class PhysicalSignature {
    
    /**
     * 雷达截面积（RCS）
     */
    private final double radarCrossSection;
    
    /**
     * 光学可见度系数
     */
    private final double opticalVisibility;
    
    /**
     * 无线电发射功率（dBm）
     */
    private final double radioEmissionPower;
    
    /**
     * 红外辐射强度（瓦特）
     */
    private final double infraredSignature;
    
    /**
     * 当前位置
     */
    private final Position currentPosition;
    
    /**
     * 当前速度大小
     */
    private final double currentSpeed;
    
    /**
     * 当前状态
     */
    private final UAVStatus currentStatus;
    
    /**
     * 环境影响因子
     */
    private final double environmentalFactor;
    
    /**
     * 构造函数
     * 
     * @param radarCrossSection 雷达截面积
     * @param opticalVisibility 光学可见度
     * @param radioEmissionPower 无线电发射功率
     * @param infraredSignature 红外辐射强度
     * @param currentPosition 当前位置
     * @param currentSpeed 当前速度
     * @param currentStatus 当前状态
     */
    public PhysicalSignature(double radarCrossSection, double opticalVisibility, 
                           double radioEmissionPower, double infraredSignature,
                           Position currentPosition, double currentSpeed, 
                           UAVStatus currentStatus) {
        
        if (radarCrossSection < 0) {
            throw new IllegalArgumentException("雷达截面积不能为负数");
        }
        if (opticalVisibility < 0 || opticalVisibility > 1) {
            throw new IllegalArgumentException("光学可见度必须在0-1之间");
        }
        if (currentPosition == null) {
            throw new IllegalArgumentException("当前位置不能为空");
        }
        if (currentSpeed < 0) {
            throw new IllegalArgumentException("当前速度不能为负数");
        }
        if (currentStatus == null) {
            throw new IllegalArgumentException("当前状态不能为空");
        }
        
        this.radarCrossSection = radarCrossSection;
        this.opticalVisibility = opticalVisibility;
        this.radioEmissionPower = radioEmissionPower;
        this.infraredSignature = infraredSignature;
        this.currentPosition = currentPosition;
        this.currentSpeed = currentSpeed;
        this.currentStatus = currentStatus;
        this.environmentalFactor = calculateEnvironmentalFactor();
    }
    
    /**
     * 获取雷达截面积
     * 
     * @return 雷达截面积
     */
    public double getRadarCrossSection() {
        return radarCrossSection;
    }
    
    /**
     * 获取光学可见度
     * 
     * @return 光学可见度
     */
    public double getOpticalVisibility() {
        return opticalVisibility;
    }
    
    /**
     * 获取无线电发射功率
     * 
     * @return 无线电发射功率
     */
    public double getRadioEmissionPower() {
        return radioEmissionPower;
    }
    
    /**
     * 获取红外辐射强度
     * 
     * @return 红外辐射强度
     */
    public double getInfraredSignature() {
        return infraredSignature;
    }
    
    /**
     * 获取当前位置
     * 
     * @return 当前位置
     */
    public Position getCurrentPosition() {
        return currentPosition;
    }
    
    /**
     * 获取当前速度
     * 
     * @return 当前速度
     */
    public double getCurrentSpeed() {
        return currentSpeed;
    }
    
    /**
     * 获取当前状态
     * 
     * @return 当前状态
     */
    public UAVStatus getCurrentStatus() {
        return currentStatus;
    }
    
    /**
     * 获取环境影响因子
     * 
     * @return 环境影响因子
     */
    public double getEnvironmentalFactor() {
        return environmentalFactor;
    }
    
    /**
     * 获取有效雷达截面积（考虑环境和状态影响）
     * 
     * @return 有效雷达截面积
     */
    public double getEffectiveRadarCrossSection() {
        double statusMultiplier = getStatusMultiplier();
        double speedMultiplier = getSpeedMultiplier();
        
        return radarCrossSection * statusMultiplier * speedMultiplier * environmentalFactor;
    }
    
    /**
     * 获取有效光学可见度（考虑环境和状态影响）
     * 
     * @return 有效光学可见度
     */
    public double getEffectiveOpticalVisibility() {
        double statusMultiplier = getStatusMultiplier();
        double altitudeMultiplier = getAltitudeMultiplier();
        
        return Math.min(1.0, opticalVisibility * statusMultiplier * altitudeMultiplier * environmentalFactor);
    }
    
    /**
     * 获取有效无线电发射功率（考虑状态影响）
     * 
     * @return 有效无线电发射功率
     */
    public double getEffectiveRadioEmissionPower() {
        if (currentStatus == UAVStatus.OFFLINE || currentStatus == UAVStatus.MALFUNCTION) {
            return -100.0; // 离线或故障时无信号
        }
        
        double statusMultiplier = getStatusMultiplier();
        return radioEmissionPower * statusMultiplier;
    }
    
    /**
     * 获取有效红外辐射强度（考虑速度和状态影响）
     * 
     * @return 有效红外辐射强度
     */
    public double getEffectiveInfraredSignature() {
        double statusMultiplier = getStatusMultiplier();
        double speedMultiplier = 1.0 + (currentSpeed / 20.0) * 0.3; // 速度越快，红外特征越明显
        
        return infraredSignature * statusMultiplier * speedMultiplier;
    }
    
    /**
     * 检查是否容易被雷达探测
     * 
     * @return 是否容易被雷达探测
     */
    public boolean isEasilyDetectableByRadar() {
        return getEffectiveRadarCrossSection() > 0.1;
    }
    
    /**
     * 检查是否容易被光电设备探测
     * 
     * @return 是否容易被光电设备探测
     */
    public boolean isEasilyDetectableByOptical() {
        return getEffectiveOpticalVisibility() > 0.5;
    }
    
    /**
     * 检查是否容易被无线电侦测器探测
     * 
     * @return 是否容易被无线电侦测器探测
     */
    public boolean isEasilyDetectableByRadio() {
        return getEffectiveRadioEmissionPower() > -30.0; // 大于-30dBm认为容易探测
    }
    
    /**
     * 检查是否容易被红外设备探测
     * 
     * @return 是否容易被红外设备探测
     */
    public boolean isEasilyDetectableByInfrared() {
        return getEffectiveInfraredSignature() > 10.0;
    }
    
    /**
     * 获取综合探测难度评分
     * 
     * @return 探测难度评分（0-10，10最难探测）
     */
    public double getDetectionDifficultyScore() {
        double radarScore = Math.max(0, 3 - Math.log10(getEffectiveRadarCrossSection() + 0.001) * 2);
        double opticalScore = Math.max(0, 3 - getEffectiveOpticalVisibility() * 3);
        double radioScore = Math.max(0, 3 - (getEffectiveRadioEmissionPower() + 50) / 20);
        double infraredScore = Math.max(0, 1 - getEffectiveInfraredSignature() / 50);
        
        return Math.min(10, radarScore + opticalScore + radioScore + infraredScore);
    }
    
    /**
     * 计算环境影响因子
     */
    private double calculateEnvironmentalFactor() {
        // 简化的环境影响计算，实际应该考虑天气、时间等因素
        double altitudeFactor = 1.0 - Math.min(0.3, currentPosition.getZ() / 1000.0); // 高度影响
        return Math.max(0.5, altitudeFactor);
    }
    
    /**
     * 获取状态影响倍数
     */
    private double getStatusMultiplier() {
        switch (currentStatus) {
            case HOVERING:
            case STANDBY:
                return 0.8; // 静止时特征较小
            case MOVING:
            case DETECTING:
                return 1.0; // 正常移动
            case TAKING_OFF:
            case LANDING:
                return 1.2; // 起降时特征较明显
            case MALFUNCTION:
            case DAMAGED:
                return 1.5; // 故障时可能特征异常
            case OFFLINE:
            case LANDED:
                return 0.1; // 离线或已降落时特征很小
            default:
                return 1.0;
        }
    }
    
    /**
     * 获取速度影响倍数
     */
    private double getSpeedMultiplier() {
        if (currentSpeed < 1.0) {
            return 0.8; // 低速时RCS较小
        } else if (currentSpeed > 15.0) {
            return 1.3; // 高速时RCS较大
        }
        return 1.0;
    }
    
    /**
     * 获取高度影响倍数
     */
    private double getAltitudeMultiplier() {
        double altitude = currentPosition.getZ();
        if (altitude > 500) {
            return 0.7; // 高空时光学可见度降低
        } else if (altitude < 50) {
            return 1.2; // 低空时更容易被发现
        }
        return 1.0;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PhysicalSignature that = (PhysicalSignature) obj;
        return Double.compare(that.radarCrossSection, radarCrossSection) == 0 &&
               Double.compare(that.opticalVisibility, opticalVisibility) == 0 &&
               Double.compare(that.radioEmissionPower, radioEmissionPower) == 0 &&
               Double.compare(that.infraredSignature, infraredSignature) == 0 &&
               Double.compare(that.currentSpeed, currentSpeed) == 0 &&
               Objects.equals(currentPosition, that.currentPosition) &&
               currentStatus == that.currentStatus;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(radarCrossSection, opticalVisibility, radioEmissionPower, 
                          infraredSignature, currentPosition, currentSpeed, currentStatus);
    }
    
    @Override
    public String toString() {
        return String.format("PhysicalSignature{RCS=%.4f, 光学=%.2f, 无线电=%.1fdBm, " +
                           "红外=%.1fW, 速度=%.1fm/s, 状态=%s, 探测难度=%.1f}",
                           getEffectiveRadarCrossSection(), getEffectiveOpticalVisibility(), 
                           getEffectiveRadioEmissionPower(), getEffectiveInfraredSignature(),
                           currentSpeed, currentStatus, getDetectionDifficultyScore());
    }
} 