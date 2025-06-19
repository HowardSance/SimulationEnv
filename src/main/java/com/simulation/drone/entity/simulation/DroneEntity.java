package com.simulation.drone.entity.simulation;

import lombok.Data;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "drone_entities")
public class DroneEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String type;
    
    @Column(columnDefinition = "geometry(Point,4326)")
    private Point position;
    
    @Column
    private Double altitude;
    
    @Column
    private Double heading;
    
    @Column
    private Double speed;
    
    @Column
    private Double rcs; // 雷达散射截面
    
    @Column
    private LocalDateTime lastUpdateTime;
    
    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        lastUpdateTime = LocalDateTime.now();
    }
} 