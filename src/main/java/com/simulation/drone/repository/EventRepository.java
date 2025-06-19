package com.simulation.drone.repository;

import com.simulation.drone.entity.simulation.DetectionEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<DetectionEvent, Long> {
    
    List<DetectionEvent> findByDeviceId(Long deviceId);
    
    List<DetectionEvent> findByDroneId(Long droneId);
    
    List<DetectionEvent> findByDetected(Boolean detected);
    
    @Query("SELECT e FROM DetectionEvent e WHERE e.detectionTime BETWEEN :startTime AND :endTime")
    List<DetectionEvent> findByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT e FROM DetectionEvent e WHERE e.snr >= :minSnr")
    List<DetectionEvent> findByMinSnr(@Param("minSnr") double minSnr);
} 