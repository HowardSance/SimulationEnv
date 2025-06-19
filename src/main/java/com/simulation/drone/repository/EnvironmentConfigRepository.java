package com.simulation.drone.repository;

import com.simulation.drone.entity.config.EnvironmentConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnvironmentConfigRepository extends JpaRepository<EnvironmentConfig, Long> {
    
    List<EnvironmentConfig> findByEnabled(Boolean enabled);
    
    EnvironmentConfig findByName(String name);
} 