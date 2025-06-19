package com.simulation.drone.entity.simulation;

import lombok.Data;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "detection_events")
public class DetectionEvent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private DetectionDevice device;
    
    @ManyToOne
    @JoinColumn(name = "drone_id", nullable = false)
    private DroneEntity drone;
    
    @Column(columnDefinition = "geometry(Point,4326)")
    private Point detectedPosition;
    
    @Column
    private Double range; // 探测距离(米)
    
    @Column
    private Double azimuth; // 方位角(度)
    
    @Column
    private Double elevation; // 俯仰角(度)
    
    @Column
    private Double radialVelocity; // 径向速度(m/s)
    
    @Column
    private Double snr; // 信噪比(dB)
    
    @Column
    private Boolean detected; // 是否成功探测
    
    @Column
    private LocalDateTime detectionTime;
    
    @PrePersist
    public void setDetectionTime() {
        detectionTime = LocalDateTime.now();
    }
} 