package com.simulation.drone.entity.config;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "device_templates")
public class DeviceTemplate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceType type;
    
    @Column(nullable = false)
    private String description;
    
    @Column(columnDefinition = "jsonb")
    private String parameters; // JSON格式存储参数配置
    
    @Column
    private Boolean enabled;
    
    public enum DeviceType {
        RADAR,
        OPTICAL,
        RADIO
    }
} 