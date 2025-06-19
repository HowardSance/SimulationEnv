package com.simulation.drone.service.impl;

import com.simulation.drone.engine.ClockEngine;
import com.simulation.drone.engine.EnvironmentEngine;
import com.simulation.drone.service.EventPublishService;
import com.simulation.drone.service.SimulationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimulationServiceImpl implements SimulationService {
    
    private final ClockEngine clockEngine;
    private final EnvironmentEngine environmentEngine;
    private final EventPublishService eventService;
    
    private final Map<String, Map<String, Object>> snapshots = new ConcurrentHashMap<>();
    private boolean isRunning = false;
    private boolean isPaused = false;
    
    @Override
    public void startSimulation() {
        if (!isRunning) {
            isRunning = true;
            isPaused = false;
            clockEngine.start();
            log.info("仿真系统已启动");
            eventService.publishSimulationStatus("STARTED");
        }
    }
    
    @Override
    public void pauseSimulation() {
        if (isRunning && !isPaused) {
            isPaused = true;
            clockEngine.pause();
            log.info("仿真系统已暂停");
            eventService.publishSimulationStatus("PAUSED");
        }
    }
    
    @Override
    public void resumeSimulation() {
        if (isRunning && isPaused) {
            isPaused = false;
            clockEngine.resume();
            log.info("仿真系统已恢复");
            eventService.publishSimulationStatus("RUNNING");
        }
    }
    
    @Override
    public void stopSimulation() {
        if (isRunning) {
            isRunning = false;
            isPaused = false;
            clockEngine.stop();
            log.info("仿真系统已停止");
            eventService.publishSimulationStatus("STOPPED");
        }
    }
    
    @Override
    public Map<String, Object> getSimulationStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("running", isRunning);
        status.put("paused", isPaused);
        status.put("currentTime", clockEngine.getCurrentTime());
        status.put("timeStep", clockEngine.getTimeStep());
        status.put("environmentStatus", environmentEngine.getStatus());
        return status;
    }
    
    @Override
    public void setTimeStep(long timeStep) {
        clockEngine.setTimeStep(timeStep);
        log.info("仿真时间步长已更新: {}ms", timeStep);
    }
    
    @Override
    public String createSnapshot() {
        String snapshotId = UUID.randomUUID().toString();
        Map<String, Object> snapshot = new HashMap<>();
        snapshot.put("timestamp", System.currentTimeMillis());
        snapshot.put("simulationStatus", getSimulationStatus());
        snapshot.put("environmentState", environmentEngine.getState());
        snapshots.put(snapshotId, snapshot);
        log.info("创建仿真快照: {}", snapshotId);
        return snapshotId;
    }
    
    @Override
    public void restoreSnapshot(String snapshotId) {
        Map<String, Object> snapshot = snapshots.get(snapshotId);
        if (snapshot != null) {
            environmentEngine.restoreState((Map<String, Object>) snapshot.get("environmentState"));
            log.info("恢复仿真快照: {}", snapshotId);
        } else {
            log.warn("快照不存在: {}", snapshotId);
        }
    }
    
    @Override
    public Map<String, Object> getSimulationMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("fps", clockEngine.getFPS());
        metrics.put("updateTime", clockEngine.getAverageUpdateTime());
        metrics.put("entityCount", environmentEngine.getEntityCount());
        metrics.put("memoryUsage", Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        return metrics;
    }
} 