package com.simulation.drone.entity.config;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "environment_configs")
public class EnvironmentConfig {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "jsonb")
    private String weather; // 气象条件配置
    
    @Column(columnDefinition = "jsonb")
    private String atmosphere; // 大气模型配置
    
    @Column(columnDefinition = "jsonb")
    private String terrain; // 地形数据配置
    
    @Column
    private Boolean enabled;
    
    @Column
    private String description;
} 