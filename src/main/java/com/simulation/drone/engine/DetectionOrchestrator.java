package com.simulation.drone.engine;

import com.simulation.drone.adapter.airsim.AirSimAdapter;
import com.simulation.drone.algorithm.detection.DetectionAlgorithm;
import com.simulation.drone.dto.detection.DetectionResultDTO;
import com.simulation.drone.entity.simulation.DetectionDevice;
import com.simulation.drone.entity.simulation.DetectionEvent;
import com.simulation.drone.entity.simulation.DroneEntity;
import com.simulation.drone.model.message.state.DroneState;
import com.simulation.drone.repository.DeviceRepository;
import com.simulation.drone.repository.EventRepository;
import com.simulation.drone.repository.DroneRepository;
import com.simulation.drone.service.EventPublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 探测业务编排器
 * 统一协调多设备并行探测的核心业务逻辑
 * 实现：AirSim状态同步 → 多设备并行探测 → 事件生成发布
 */
@Slf4j
@Component
public class DetectionOrchestrator implements ClockEngine.ClockUpdateListener {
    
    @Autowired
    private AirSimAdapter airSimAdapter;
    
    @Autowired
    private DeviceRepository deviceRepository;
    
    @Autowired
    private DroneRepository droneRepository;
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private EventPublishService eventPublishService;
    
    @Autowired
    private List<DetectionAlgorithm> detectionAlgorithms;
    
    @Value("${simulation.detection.parallel-threads:4}")
    private int parallelThreads;
    
    @Value("${simulation.detection.detection-threshold:0.5}")
    private double detectionThreshold;
    
    // 并行执行器
    private ExecutorService detectionExecutor;
    
    // 缓存和状态管理
    private final Map<String, DroneEntity> activeDrones = new ConcurrentHashMap<>();
    private final Map<Long, DetectionDevice> activeDevices = new ConcurrentHashMap<>();
    private final Map<String, DetectionAlgorithm> algorithmMap = new HashMap<>();
    
    // 环境参数缓存
    private Map<String, Object> currentEnvironment = new ConcurrentHashMap<>();
    
    // 性能监控
    private long totalDetections = 0;
    private long successfulDetections = 0;
    private long lastPerformanceReport = 0;
    
    @PostConstruct
    public void initialize() {
        // 初始化线程池
        detectionExecutor = Executors.newFixedThreadPool(parallelThreads, 
            r -> new Thread(r, "DetectionWorker-" + System.nanoTime()));
        
        // 构建算法映射
        buildAlgorithmMap();
        
        // 加载活动设备
        loadActiveDevices();
        
        // 初始化环境参数
        initializeEnvironment();
        
        log.info("探测业务编排器初始化完成 - 并行线程数: {}, 算法数量: {}, 活动设备数: {}", 
                parallelThreads, algorithmMap.size(), activeDevices.size());
    }
    
    @Override
    public void onClockUpdate(long currentTime, long deltaTime) {
        try {
            // 核心业务流程
            performDetectionCycle(currentTime);
            
            // 性能监控
            if (currentTime - lastPerformanceReport > 10000) { // 每10秒报告一次
                reportPerformance();
                lastPerformanceReport = currentTime;
            }
            
        } catch (Exception e) {
            log.error("探测周期执行失败", e);
        }
    }
    
    /**
     * 执行一个完整的探测周期
     * 1. 同步AirSim状态
     * 2. 并行执行多设备探测
     * 3. 生成和发布探测事件
     */
    private void performDetectionCycle(long currentTime) {
        // 步骤1: 从AirSim同步无人机状态
        syncDroneStatesFromAirSim();
        
        // 步骤2: 更新环境参数
        updateEnvironmentParameters();
        
        // 步骤3: 并行执行探测
        List<CompletableFuture<List<DetectionResultDTO>>> detectionFutures = new ArrayList<>();
        
        for (DetectionDevice device : activeDevices.values()) {
            if (device.getEnabled() != null && device.getEnabled()) {
                CompletableFuture<List<DetectionResultDTO>> future = CompletableFuture
                    .supplyAsync(() -> performDeviceDetection(device), detectionExecutor);
                detectionFutures.add(future);
            }
        }
        
        // 步骤4: 收集所有探测结果
        try {
            CompletableFuture<Void> allDetections = CompletableFuture.allOf(
                detectionFutures.toArray(new CompletableFuture[0]));
            
            // 等待所有探测完成(带超时)
            allDetections.get(100, TimeUnit.MILLISECONDS);
            
            // 收集结果
            List<DetectionResultDTO> allResults = detectionFutures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        log.error("获取探测结果失败", e);
                        return Collections.<DetectionResultDTO>emptyList();
                    }
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
            
            // 步骤5: 处理探测结果
            processDetectionResults(allResults, currentTime);
            
        } catch (TimeoutException e) {
            log.warn("探测周期超时，部分设备探测未完成");
        } catch (Exception e) {
            log.error("探测结果收集失败", e);
        }
    }
    
    /**
     * 从AirSim同步无人机状态
     */
    private void syncDroneStatesFromAirSim() {
        try {
            // 获取所有活动无人机的状态
            List<Map<String, Object>> airSimDrones = airSimAdapter.getAllDrones();
            
            for (Map<String, Object> droneInfo : airSimDrones) {
                String droneId = (String) droneInfo.get("id");
                if (droneId != null) {
                    DroneState state = airSimAdapter.getDroneState(droneId);
                    if (state != null) {
                        DroneEntity entity = convertToDroneEntity(state);
                        activeDrones.put(droneId, entity);
                        
                        // 保存到数据库(异步)
                        CompletableFuture.runAsync(() -> droneRepository.save(entity));
                    }
                }
            }
            
        } catch (Exception e) {
            log.error("从AirSim同步无人机状态失败", e);
        }
    }
    
    /**
     * 执行单个设备的探测
     */
    private List<DetectionResultDTO> performDeviceDetection(DetectionDevice device) {
        List<DetectionResultDTO> results = new ArrayList<>();
        
        try {
            // 获取适合此设备的算法
            DetectionAlgorithm algorithm = getAlgorithmForDevice(device);
            if (algorithm == null) {
                log.warn("未找到适合设备 {} 的探测算法", device.getId());
                return results;
            }
            
            // 对每个活动无人机执行探测
            for (DroneEntity drone : activeDrones.values()) {
                try {
                    DetectionResultDTO result = algorithm.detect(device, drone, currentEnvironment);
                    if (result != null) {
                        results.add(result);
                        totalDetections++;
                        
                        // 统计成功探测
                        if (result.getProbability() >= detectionThreshold) {
                            successfulDetections++;
                        }
                    }
                } catch (Exception e) {
                    log.error("设备 {} 探测无人机 {} 失败", device.getId(), drone.getDroneId(), e);
                }
            }
            
        } catch (Exception e) {
            log.error("设备 {} 探测执行失败", device.getId(), e);
        }
        
        return results;
    }
    
    /**
     * 处理探测结果
     */
    private void processDetectionResults(List<DetectionResultDTO> results, long currentTime) {
        List<DetectionEvent> events = new ArrayList<>();
        
        for (DetectionResultDTO result : results) {
            try {
                // 只处理超过阈值的探测结果
                if (result.getProbability() >= detectionThreshold) {
                    DetectionEvent event = convertToDetectionEvent(result);
                    events.add(event);
                    
                    // 发布实时事件
                    eventPublishService.publishDetectionResult(result);
                }
            } catch (Exception e) {
                log.error("处理探测结果失败: {}", result.getResultId(), e);
            }
        }
        
        // 批量保存事件
        if (!events.isEmpty()) {
            try {
                eventRepository.saveAll(events);
                log.debug("保存了 {} 个探测事件", events.size());
            } catch (Exception e) {
                log.error("批量保存探测事件失败", e);
            }
        }
    }
    
    /**
     * 更新环境参数
     */
    private void updateEnvironmentParameters() {
        // 模拟环境参数变化
        currentEnvironment.put("temperature", 15.0 + Math.random() * 10); // 15-25°C
        currentEnvironment.put("humidity", 50.0 + Math.random() * 30); // 50-80%
        currentEnvironment.put("visibility", 8000.0 + Math.random() * 4000); // 8-12km
        currentEnvironment.put("atmosphericLoss", 0.1 + Math.random() * 0.05); // 0.1-0.15 dB/km
        currentEnvironment.put("weatherFactor", 0.8 + Math.random() * 0.4); // 0.8-1.2
        currentEnvironment.put("clutterLevel", -45.0 + Math.random() * 10); // -45 to -35 dBm
    }
    
    /**
     * 获取设备对应的探测算法
     */
    private DetectionAlgorithm getAlgorithmForDevice(DetectionDevice device) {
        String deviceType = device.getType().name();
        return algorithmMap.get(deviceType);
    }
    
    /**
     * 构建算法映射
     */
    private void buildAlgorithmMap() {
        for (DetectionAlgorithm algorithm : detectionAlgorithms) {
            String algorithmType = algorithm.getAlgorithmType();
            if (algorithmType.contains("Radar")) {
                algorithmMap.put("RADAR", algorithm);
            } else if (algorithmType.contains("Optical")) {
                algorithmMap.put("OPTICAL", algorithm);
            } else if (algorithmType.contains("Radio")) {
                algorithmMap.put("RADIO", algorithm);
            }
        }
        log.info("构建算法映射完成: {}", algorithmMap.keySet());
    }
    
    /**
     * 加载活动设备
     */
    private void loadActiveDevices() {
        try {
            List<DetectionDevice> devices = deviceRepository.findByEnabled(true);
            for (DetectionDevice device : devices) {
                activeDevices.put(device.getId(), device);
            }
            log.info("加载了 {} 个活动探测设备", activeDevices.size());
        } catch (Exception e) {
            log.error("加载活动设备失败", e);
        }
    }
    
    /**
     * 初始化环境参数
     */
    private void initializeEnvironment() {
        currentEnvironment.put("temperature", 20.0);
        currentEnvironment.put("humidity", 60.0);
        currentEnvironment.put("visibility", 10000.0);
        currentEnvironment.put("atmosphericLoss", 0.12);
        currentEnvironment.put("weatherFactor", 1.0);
        currentEnvironment.put("clutterLevel", -40.0);
    }
    
    /**
     * 转换DroneState为DroneEntity
     */
    private DroneEntity convertToDroneEntity(DroneState state) {
        DroneEntity entity = new DroneEntity();
        entity.setDroneId(state.getDroneId());
        entity.setTimestamp(LocalDateTime.now());
        
        if (state.getPosition() != null) {
            entity.setPosition(state.getPosition());
        }
        if (state.getVelocity() != null) {
            entity.setVelocity(state.getVelocity());
        }
        if (state.getOrientation() != null) {
            entity.setOrientation(state.getOrientation());
        }
        
        return entity;
    }
    
    /**
     * 转换DetectionResultDTO为DetectionEvent
     */
    private DetectionEvent convertToDetectionEvent(DetectionResultDTO result) {
        DetectionEvent event = new DetectionEvent();
        
        // 查找设备和无人机实体
        DetectionDevice device = activeDevices.get(Long.valueOf(result.getDeviceId()));
        DroneEntity drone = activeDrones.get(result.getTargetId());
        
        if (device != null) {
            event.setDevice(device);
        }
        if (drone != null) {
            event.setDrone(drone);
        }
        
        // 设置探测结果
        if (result.getPosition() != null) {
            // 创建Point对象 (简化处理)
            event.setDetectedPosition(device.getPosition()); // 暂时使用设备位置
        }
        
        // 计算距离和角度 (从设备状态中获取)
        if (result.getDeviceStatus() != null) {
            Object range = result.getDeviceStatus().get("range");
            if (range instanceof Double) {
                event.setRange((Double) range);
            }
            Object bearing = result.getDeviceStatus().get("bearing");
            if (bearing instanceof Double) {
                event.setAzimuth((Double) bearing);
            }
        }
        
        event.setDetected(result.getProbability() >= detectionThreshold);
        event.setDetectionTime(LocalDateTime.now());
        
        return event;
    }
    
    /**
     * 性能报告
     */
    private void reportPerformance() {
        double detectionRate = totalDetections > 0 ? 
            (double) successfulDetections / totalDetections * 100 : 0;
        
        log.info("探测性能报告 - 总探测次数: {}, 成功探测: {}, 成功率: {:.2f}%, 活动无人机: {}, 活动设备: {}", 
                totalDetections, successfulDetections, detectionRate, 
                activeDrones.size(), activeDevices.size());
                
        // 重置计数器
        totalDetections = 0;
        successfulDetections = 0;
    }
    
    /**
     * 添加无人机到探测范围
     */
    public void addDrone(String droneId) {
        try {
            DroneState state = airSimAdapter.getDroneState(droneId);
            if (state != null) {
                DroneEntity entity = convertToDroneEntity(state);
                activeDrones.put(droneId, entity);
                log.info("添加无人机到探测范围: {}", droneId);
            }
        } catch (Exception e) {
            log.error("添加无人机失败: {}", droneId, e);
        }
    }
    
    /**
     * 从探测范围移除无人机
     */
    public void removeDrone(String droneId) {
        activeDrones.remove(droneId);
        log.info("从探测范围移除无人机: {}", droneId);
    }
    
    /**
     * 添加探测设备
     */
    public void addDevice(DetectionDevice device) {
        activeDevices.put(device.getId(), device);
        log.info("添加探测设备: {}", device.getId());
    }
    
    /**
     * 移除探测设备
     */
    public void removeDevice(Long deviceId) {
        activeDevices.remove(deviceId);
        log.info("移除探测设备: {}", deviceId);
    }
    
    /**
     * 获取当前状态
     */
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("activeDrones", activeDrones.size());
        status.put("activeDevices", activeDevices.size());
        status.put("availableAlgorithms", algorithmMap.size());
        status.put("currentEnvironment", currentEnvironment);
        status.put("totalDetections", totalDetections);
        status.put("successfulDetections", successfulDetections);
        return status;
    }
    
    /**
     * 关闭编排器
     */
    @PreDestroy
    public void shutdown() {
        if (detectionExecutor != null) {
            detectionExecutor.shutdown();
            try {
                if (!detectionExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    detectionExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                detectionExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        log.info("探测业务编排器已关闭");
    }
} 