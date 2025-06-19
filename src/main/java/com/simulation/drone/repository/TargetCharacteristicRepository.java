package com.simulation.drone.repository;

import com.simulation.drone.entity.config.TargetCharacteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TargetCharacteristicRepository extends JpaRepository<TargetCharacteristic, Long> {
    
    List<TargetCharacteristic> findByType(String type);
    
    List<TargetCharacteristic> findByEnabled(Boolean enabled);
    
    TargetCharacteristic findByName(String name);
} 