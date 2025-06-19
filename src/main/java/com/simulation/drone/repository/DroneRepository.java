package com.simulation.drone.repository;

import com.simulation.drone.entity.simulation.DroneEntity;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneRepository extends JpaRepository<DroneEntity, Long> {
    
    List<DroneEntity> findByType(String type);
    
    @Query("SELECT d FROM DroneEntity d WHERE ST_DWithin(d.position, :point, :distance) = true")
    List<DroneEntity> findNearbyDrones(@Param("point") Point point, @Param("distance") double distance);
    
    @Query("SELECT d FROM DroneEntity d WHERE d.altitude BETWEEN :minAltitude AND :maxAltitude")
    List<DroneEntity> findByAltitudeRange(@Param("minAltitude") double minAltitude, @Param("maxAltitude") double maxAltitude);
} 