package com.JP.dronesim.domain.device.model.radio;

import com.JP.dronesim.domain.device.model.common.events.DetectionEvent;

import java.time.LocalDateTime;

/**
 * 无线电探测事件
 */
public class RadioDetectionEvent extends DetectionEvent {
    private static final String eventId = null;
    private static final String detectorId = null;
    private static final String detectorName = null;
    private final double azimuth; // 方位角
    private final double elevation; // 仰角
    private final double distance; // 距离
    private final double signalStrength; // dBm
    private final double frequency; // Hz
    private final double directionAccuracy; // 度


    public RadioDetectionEvent(String targetId, LocalDateTime detectionTime,
                               double azimuth, double elevation, double distance,
                               double signalStrength, double frequency,
                               double directionAccuracy) {
        super(targetId, detectionTime);
        this.azimuth = azimuth;
        this.elevation = elevation;
        this.distance = distance;
        this.signalStrength = signalStrength;
        this.frequency = frequency;
        this.directionAccuracy = directionAccuracy;
    }

    // 各属性的getter方法...
}

