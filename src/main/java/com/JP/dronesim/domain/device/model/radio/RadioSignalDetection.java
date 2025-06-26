package com.JP.dronesim.domain.device.model.radio;

import com.JP.dronesim.domain.common.valueobjects.Position;

import java.time.LocalDateTime;

/**
 * 无线电信号检测结果值对象
 */
public class RadioSignalDetection {
    private final String uavId;
    private final LocalDateTime detectionTime;
    private final double azimuth; // 方位角(度)
    private final double elevation; // 仰角(度)
    private final double distance; // 距离(米)
    private final double signalStrength; // 接收信号强度(dBm)
    private final double frequency; // 频率(Hz)
    private final double directionAccuracy; // 方向精度(度)
    private final double signalQuality; // 信号质量(0.0-1.0)

    public RadioSignalDetection(String uavId, LocalDateTime detectionTime,
                                double azimuth, double elevation, double distance,
                                double signalStrength, double frequency,
                                double directionAccuracy, double signalQuality) {
        this.uavId = uavId;
        this.detectionTime = detectionTime;
        this.azimuth = azimuth;
        this.elevation = elevation;
        this.distance = distance;
        this.signalStrength = signalStrength;
        this.frequency = frequency;
        this.directionAccuracy = directionAccuracy;
        this.signalQuality = signalQuality;
    }

    // 各属性的getter方法...
    public String getUavId() {
        return uavId;
    }

    public LocalDateTime getDetectionTime() {
        return detectionTime;
    }

    public double getAzimuth() {
        return azimuth;
    }

    public double getElevation() {
        return elevation;
    }

    public double getDistance() {
        return distance;
    }

    public double getSignalStrength() {
        return signalStrength;
    }

    public double getFrequency() {
        return frequency;
    }

    public double getDirectionAccuracy() {
        return directionAccuracy;
    }

    public double getSignalQuality() {
        return signalQuality;
    }

    public Position getDetectorPosition() {
        return null;
    }
}

