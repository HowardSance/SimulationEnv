package com.simulation.drone.entity.simulation;

import lombok.Data;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "detection_devices")
public class DetectionDevice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceType type;
    
    @Column(columnDefinition = "geometry(Point,4326)")
    private Point position;
    
    @Column
    private Double altitude;
    
    @Column
    private Double heading;
    
    @Column
    private Double pitch;
    
    @Column
    private Double range; // 探测范围(米)
    
    @Column
    private Double beamWidth; // 波束宽度(度)
    
    @Column
    private Double frequency; // 工作频率(Hz)
    
    @Column
    private Double power; // 发射功率(W)
    
    @Column
    private Boolean enabled;
    
    @Column
    private LocalDateTime lastUpdateTime;
    
    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        lastUpdateTime = LocalDateTime.now();
    }
    
    public enum DeviceType {
        RADAR,
        OPTICAL,
        RADIO
    }
} 