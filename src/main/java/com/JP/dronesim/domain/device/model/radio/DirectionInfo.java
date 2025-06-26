package com.JP.dronesim.domain.device.model.radio;

import java.time.LocalDateTime;

/**
 * 方向信息值对象
 * 包含信号来源的方向、仰角、精度和可信度等信息
 */
public class DirectionInfo {
    private final double azimuth; // 方位角(度)
    private final double elevation; // 仰角(度)
    private final double accuracy; // 方向精度(度)
    private final double confidence; // 可信度(0.0-1.0)
    private final LocalDateTime timestamp; // 时间戳

    /**
     * 构造函数
     * @param azimuth 方位角(度)
     * @param elevation 仰角(度)
     * @param accuracy 方向精度(度)
     * @param confidence 可信度(0.0-1.0)
     * @param timestamp 时间戳
     */
    public DirectionInfo(double azimuth, double elevation,
                         double accuracy, double confidence,
                         LocalDateTime timestamp) {
        this.azimuth = azimuth;
        this.elevation = elevation;
        this.accuracy = accuracy;
        this.confidence = confidence;
        this.timestamp = timestamp;
    }

    // 各属性的getter方法
    public double getAzimuth() {
        return azimuth;
    }

    public double getElevation() {
        return elevation;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getConfidence() {
        return confidence;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * 获取方向描述
     */
    public String getDirectionDescription() {
        return String.format("方位: %.1f° ±%.1f° (仰角: %.1f°)",
                azimuth, accuracy, elevation);
    }
}

