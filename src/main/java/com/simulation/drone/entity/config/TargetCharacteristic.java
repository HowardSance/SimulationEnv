package com.simulation.drone.entity.config;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "target_characteristics")
public class TargetCharacteristic {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String type; // 目标类型
    
    @Column
    private Double rcs; // 雷达散射截面
    
    @Column(columnDefinition = "jsonb")
    private String movementPattern; // 运动模式配置
    
    @Column(columnDefinition = "jsonb")
    private String trackPreset; // 航迹预设
    
    @Column
    private Boolean enabled;
    
    @Column
    private String description;
} 