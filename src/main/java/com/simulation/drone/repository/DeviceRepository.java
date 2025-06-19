package com.simulation.drone.repository;

import com.simulation.drone.entity.simulation.DetectionDevice;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<DetectionDevice, Long> {
    
    List<DetectionDevice> findByType(DetectionDevice.DeviceType type);
    
    List<DetectionDevice> findByEnabled(Boolean enabled);
    
    @Query("SELECT d FROM DetectionDevice d WHERE ST_DWithin(d.position, :point, :distance) = true")
    List<DetectionDevice> findNearbyDevices(@Param("point") Point point, @Param("distance") double distance);
    
    @Query("SELECT d FROM DetectionDevice d WHERE d.range >= :minRange")
    List<DetectionDevice> findByMinRange(@Param("minRange") double minRange);
} 