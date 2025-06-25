package com.JP.dronesim.application.services;

import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.device.model.radar.ElectromagneticRadar;
import com.JP.dronesim.domain.device.model.radar.RadarContact;
import com.JP.dronesim.domain.device.model.radar.RadarFactory;
import com.JP.dronesim.domain.device.model.radar.RadarParameters;
import com.JP.dronesim.domain.uav.model.AirspaceEnvironment;
import com.JP.dronesim.domain.uav.model.UAV;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 电磁波雷达应用服务类
 * 提供电磁波雷达的创建、管理、控制和数据处理的业务逻辑
 * 协调多个雷达的协同工作和数据融合
 * 
 * @author JP
 * @version 1.0
 */
public class RadarAppService {
    
    /**
     * 管理的电磁波雷达映射
     * Key: 设备ID，Value: 电磁波雷达实例
     */
    private final Map<String, ElectromagneticRadar> radars;
    
    /**
     * 雷达扫描历史记录
     * Key: 雷达ID，Value: 最近的扫描结果
     */
    private final Map<String, List<RadarContact>> scanHistory;
    
    /**
     * 构造函数
     */
    public RadarAppService() {
        this.radars = new ConcurrentHashMap<>();
        this.scanHistory = new ConcurrentHashMap<>();
    }
    
    // ================ 雷达管理方法 ================
    
    /**
     * 创建并部署标准电磁波雷达
     * 
     * @param deviceId 设备ID
     * @param deviceName 设备名称
     * @param position 部署位置
     * @param orientation 朝向
     * @param elevation 仰角
     * @param detectionRange 探测距离
     * @return 创建的雷达ID
     * @throws IllegalArgumentException 如果参数无效
     * @throws IllegalStateException 如果设备ID已存在
     */
    public String deployStandardRadar(String deviceId, String deviceName, Position position,
                                    double orientation, double elevation, double detectionRange) {
        validateRadarId(deviceId);
        
        ElectromagneticRadar radar = RadarFactory.createStandardRadar(
                deviceId, deviceName, position, orientation, elevation, detectionRange);
        
        radars.put(deviceId, radar);
        scanHistory.put(deviceId, new ArrayList<>());
        return deviceId;
    }
    
    /**
     * 创建并部署高精度电磁波雷达
     * 
     * @param deviceId 设备ID
     * @param deviceName 设备名称
     * @param position 部署位置
     * @param orientation 朝向
     * @param elevation 仰角
     * @param detectionRange 探测距离
     * @return 创建的雷达ID
     */
    public String deployHighPrecisionRadar(String deviceId, String deviceName, Position position,
                                         double orientation, double elevation, double detectionRange) {
        validateRadarId(deviceId);
        
        ElectromagneticRadar radar = RadarFactory.createHighPrecisionRadar(
                deviceId, deviceName, position, orientation, elevation, detectionRange);
        
        radars.put(deviceId, radar);
        scanHistory.put(deviceId, new ArrayList<>());
        return deviceId;
    }
    
    /**
     * 创建并部署远程监视雷达
     * 
     * @param deviceId 设备ID
     * @param deviceName 设备名称
     * @param position 部署位置
     * @param orientation 朝向
     * @param elevation 仰角
     * @param detectionRange 探测距离
     * @return 创建的雷达ID
     */
    public String deployLongRangeRadar(String deviceId, String deviceName, Position position,
                                     double orientation, double elevation, double detectionRange) {
        validateRadarId(deviceId);
        
        ElectromagneticRadar radar = RadarFactory.createLongRangeRadar(
                deviceId, deviceName, position, orientation, elevation, detectionRange);
        
        radars.put(deviceId, radar);
        scanHistory.put(deviceId, new ArrayList<>());
        return deviceId;
    }
    
    /**
     * 批量部署雷达网络
     * 
     * @param namePrefix 名称前缀
     * @param centerPosition 中心位置
     * @param gridSize 网格大小
     * @param spacing 间距
     * @param height 高度
     * @param detectionRange 探测距离
     * @return 部署的雷达ID列表
     */
    public List<String> deployRadarNetwork(String namePrefix, Position centerPosition,
                                         int gridSize, double spacing, double height,
                                         double detectionRange) {
        List<ElectromagneticRadar> radarList = RadarFactory.createRadarNetwork(
                namePrefix, centerPosition, gridSize, spacing, height, detectionRange);
        
        List<String> deployedIds = new ArrayList<>();
        for (ElectromagneticRadar radar : radarList) {
            radars.put(radar.getId(), radar);
            scanHistory.put(radar.getId(), new ArrayList<>());
            deployedIds.add(radar.getId());
        }
        
        return deployedIds;
    }
    
    /**
     * 部署多层雷达防护网
     * 
     * @param namePrefix 名称前缀
     * @param centerPosition 中心位置
     * @param innerRadius 内圈半径
     * @param outerRadius 外圈半径
     * @param innerCount 内圈雷达数量
     * @param outerCount 外圈雷达数量
     * @param height 高度
     * @param detectionRange 探测距离
     * @return 部署的雷达ID列表
     */
    public List<String> deployMultiLayerDefense(String namePrefix, Position centerPosition,
                                              double innerRadius, double outerRadius,
                                              int innerCount, int outerCount,
                                              double height, double detectionRange) {
        List<ElectromagneticRadar> radarList = RadarFactory.createMultiLayerRadarDefense(
                namePrefix, centerPosition, innerRadius, outerRadius,
                innerCount, outerCount, height, detectionRange);
        
        List<String> deployedIds = new ArrayList<>();
        for (ElectromagneticRadar radar : radarList) {
            radars.put(radar.getId(), radar);
            scanHistory.put(radar.getId(), new ArrayList<>());
            deployedIds.add(radar.getId());
        }
        
        return deployedIds;
    }
    
    /**
     * 移除指定的电磁波雷达
     * 
     * @param deviceId 设备ID
     * @return true表示成功移除，false表示设备不存在
     */
    public boolean removeRadar(String deviceId) {
        ElectromagneticRadar radar = radars.remove(deviceId);
        if (radar != null) {
            if (radar.isActive()) {
                radar.disable();
            }
            scanHistory.remove(deviceId);
        }
        return radar != null;
    }
    
    /**
     * 获取所有雷达ID列表
     * 
     * @return 雷达ID列表
     */
    public List<String> getAllRadarIds() {
        return new ArrayList<>(radars.keySet());
    }
    
    /**
     * 获取活跃雷达ID列表
     * 
     * @return 活跃雷达ID列表
     */
    public List<String> getActiveRadarIds() {
        return radars.values().stream()
                .filter(ElectromagneticRadar::isActive)
                .map(ElectromagneticRadar::getId)
                .collect(Collectors.toList());
    }
    
    // ================ 雷达控制方法 ================
    
    /**
     * 启用指定雷达
     * 
     * @param deviceId 设备ID
     * @return true表示成功启用，false表示设备不存在
     * @throws IllegalStateException 如果设备状态不允许启用
     */
    public boolean enableRadar(String deviceId) {
        ElectromagneticRadar radar = radars.get(deviceId);
        if (radar == null) {
            return false;
        }
        
        radar.enable();
        return true;
    }
    
    /**
     * 禁用指定雷达
     * 
     * @param deviceId 设备ID
     * @return true表示成功禁用，false表示设备不存在
     */
    public boolean disableRadar(String deviceId) {
        ElectromagneticRadar radar = radars.get(deviceId);
        if (radar == null) {
            return false;
        }
        
        radar.disable();
        return true;
    }
    
    /**
     * 设置雷达扫描角度（仅适用于固定扫描模式）
     * 
     * @param deviceId 设备ID
     * @param scanAngle 扫描角度
     * @return true表示成功设置，false表示设备不存在或操作失败
     */
    public boolean setScanAngle(String deviceId, double scanAngle) {
        ElectromagneticRadar radar = radars.get(deviceId);
        if (radar == null || !radar.isActive()) {
            return false;
        }
        
        try {
            radar.setScanAngle(scanAngle);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 重置雷达扫描状态
     * 
     * @param deviceId 设备ID
     * @return true表示成功重置，false表示设备不存在
     */
    public boolean resetRadarScanState(String deviceId) {
        ElectromagneticRadar radar = radars.get(deviceId);
        if (radar == null) {
            return false;
        }
        
        radar.resetScanState();
        return true;
    }
    
    // ================ 雷达扫描和探测方法 ================
    
    /**
     * 执行单个雷达扫描
     * 
     * @param deviceId 设备ID
     * @param airspace 空域环境
     * @param uavs 无人机列表
     * @return 雷达接触列表，如果设备不存在或不活跃则返回空列表
     */
    public List<RadarContact> performRadarScan(String deviceId, AirspaceEnvironment airspace, List<UAV> uavs) {
        ElectromagneticRadar radar = radars.get(deviceId);
        if (radar == null || !radar.isActive()) {
            return new ArrayList<>();
        }
        
        try {
            List<RadarContact> contacts = radar.scanArea(airspace, uavs);
            
            // 更新扫描历史
            updateScanHistory(deviceId, contacts);
            
            return contacts;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    /**
     * 执行全网雷达协同扫描
     * 
     * @param airspace 空域环境
     * @param uavs 无人机列表
     * @return 所有雷达的接触汇总
     */
    public List<RadarContact> performNetworkScan(AirspaceEnvironment airspace, List<UAV> uavs) {
        List<RadarContact> allContacts = new ArrayList<>();
        
        for (ElectromagneticRadar radar : radars.values()) {
            if (radar.isActive()) {
                try {
                    List<RadarContact> contacts = radar.scanArea(airspace, uavs);
                    updateScanHistory(radar.getId(), contacts);
                    allContacts.addAll(contacts);
                } catch (Exception e) {
                    // 记录错误但继续处理其他雷达
                    System.err.println("雷达 " + radar.getId() + " 扫描失败: " + e.getMessage());
                }
            }
        }
        
        return allContacts;
    }
    
    /**
     * 执行数据融合处理
     * 将多个雷达的探测结果进行融合，消除重复目标
     * 
     * @param allContacts 所有雷达接触
     * @return 融合后的目标列表
     */
    public List<RadarContact> performDataFusion(List<RadarContact> allContacts) {
        List<RadarContact> fusedContacts = new ArrayList<>();
        Map<String, List<RadarContact>> targetGroups = new HashMap<>();
        
        // 按目标ID分组
        for (RadarContact contact : allContacts) {
            String targetId = contact.getTargetId();
            if (targetId != null) {
                targetGroups.computeIfAbsent(targetId, k -> new ArrayList<>()).add(contact);
            } else {
                // 未知目标直接添加
                fusedContacts.add(contact);
            }
        }
        
        // 对每个目标进行数据融合
        for (Map.Entry<String, List<RadarContact>> entry : targetGroups.entrySet()) {
            List<RadarContact> contacts = entry.getValue();
            if (contacts.size() == 1) {
                fusedContacts.add(contacts.get(0));
            } else {
                // 多个雷达探测到同一目标，进行融合
                RadarContact fusedContact = fuseMultipleContacts(contacts);
                fusedContacts.add(fusedContact);
            }
        }
        
        return fusedContacts;
    }
    
    /**
     * 获取雷达网络覆盖统计
     * 
     * @return 覆盖统计信息
     */
    public RadarCoverageStatistics getNetworkCoverageStatistics() {
        int totalRadars = radars.size();
        int activeRadars = (int) radars.values().stream().filter(ElectromagneticRadar::isActive).count();
        
        // 计算扫描统计
        long totalScans = radars.values().stream()
                .mapToLong(ElectromagneticRadar::getScanCounter)
                .sum();
        
        // 计算最近扫描时间
        LocalDateTime lastScanTime = radars.values().stream()
                .map(ElectromagneticRadar::getLastScanTime)
                .filter(time -> time != null)
                .max(LocalDateTime::compareTo)
                .orElse(null);
        
        return new RadarCoverageStatistics(totalRadars, activeRadars, totalScans, lastScanTime);
    }
    
    /**
     * 获取指定雷达的扫描历史
     * 
     * @param deviceId 设备ID
     * @param maxRecords 最大记录数
     * @return 扫描历史记录
     */
    public List<RadarContact> getScanHistory(String deviceId, int maxRecords) {
        List<RadarContact> history = scanHistory.get(deviceId);
        if (history == null || history.isEmpty()) {
            return new ArrayList<>();
        }
        
        int fromIndex = Math.max(0, history.size() - maxRecords);
        return new ArrayList<>(history.subList(fromIndex, history.size()));
    }
    
    // ================ 参数管理方法 ================
    
    /**
     * 调整雷达参数
     * 
     * @param deviceId 设备ID
     * @param newParameters 新参数
     * @return true表示成功调整，false表示操作失败
     */
    public boolean adjustRadarParameters(String deviceId, RadarParameters newParameters) {
        ElectromagneticRadar radar = radars.get(deviceId);
        if (radar == null || !radar.isInitialized()) {
            return false;
        }
        
        try {
            radar.adjustParameters(newParameters);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 批量调整雷达参数
     * 
     * @param deviceIds 设备ID列表
     * @param newParameters 新参数
     * @return 成功调整的设备数量
     */
    public int batchAdjustParameters(List<String> deviceIds, RadarParameters newParameters) {
        int successCount = 0;
        for (String deviceId : deviceIds) {
            if (adjustRadarParameters(deviceId, newParameters)) {
                successCount++;
            }
        }
        return successCount;
    }
    
    /**
     * 获取雷达当前扫描状态
     * 
     * @param deviceId 设备ID
     * @return 扫描状态信息，如果设备不存在则返回null
     */
    public RadarScanStatus getRadarScanStatus(String deviceId) {
        ElectromagneticRadar radar = radars.get(deviceId);
        if (radar == null) {
            return null;
        }
        
        return new RadarScanStatus(
                deviceId, radar.getCurrentScanAngle(), radar.getScanDirection(),
                radar.getLastScanTime(), radar.getScanCounter()
        );
    }
    
    // ================ 私有辅助方法 ================
    
    /**
     * 验证雷达ID
     */
    private void validateRadarId(String deviceId) {
        if (deviceId == null || deviceId.trim().isEmpty()) {
            throw new IllegalArgumentException("设备ID不能为空");
        }
        if (radars.containsKey(deviceId)) {
            throw new IllegalStateException("设备ID已存在: " + deviceId);
        }
    }
    
    /**
     * 更新扫描历史
     */
    private void updateScanHistory(String deviceId, List<RadarContact> contacts) {
        List<RadarContact> history = scanHistory.get(deviceId);
        if (history != null) {
            history.addAll(contacts);
            
            // 限制历史记录数量，保留最近1000条记录
            if (history.size() > 1000) {
                history.subList(0, history.size() - 1000).clear();
            }
        }
    }
    
    /**
     * 融合多个雷达接触
     */
    private RadarContact fuseMultipleContacts(List<RadarContact> contacts) {
        // 简化的数据融合算法，使用加权平均
        
        // 选择信噪比最高的接触作为基准
        RadarContact bestContact = contacts.stream()
                .max((c1, c2) -> Double.compare(c1.getSignalToNoiseRatio(), c2.getSignalToNoiseRatio()))
                .orElse(contacts.get(0));
        
        // 计算加权平均位置
        double totalWeight = 0.0;
        double weightedX = 0.0, weightedY = 0.0, weightedZ = 0.0;
        double weightedRange = 0.0, weightedAzimuth = 0.0, weightedElevation = 0.0;
        double weightedRadialVel = 0.0;
        
        for (RadarContact contact : contacts) {
            double weight = Math.pow(10, contact.getSignalToNoiseRatio() / 10.0); // SNR线性值作为权重
            totalWeight += weight;
            
            Position pos = contact.getWorldPosition();
            weightedX += pos.getX() * weight;
            weightedY += pos.getY() * weight;
            weightedZ += pos.getZ() * weight;
            
            weightedRange += contact.getRange() * weight;
            weightedAzimuth += contact.getAzimuth() * weight;
            weightedElevation += contact.getElevation() * weight;
            weightedRadialVel += contact.getRadialVelocity() * weight;
        }
        
        if (totalWeight > 0) {
            weightedX /= totalWeight;
            weightedY /= totalWeight;
            weightedZ /= totalWeight;
            weightedRange /= totalWeight;
            weightedAzimuth /= totalWeight;
            weightedElevation /= totalWeight;
            weightedRadialVel /= totalWeight;
        }
        
        // 创建融合后的接触
        return new RadarContact(
                bestContact.getContactId() + "_FUSED",
                bestContact.getDetectionTime(),
                "FUSED_" + contacts.size() + "_RADARS",
                bestContact.getTargetId(),
                weightedRange, weightedAzimuth, weightedElevation, weightedRadialVel,
                new Position(weightedX, weightedY, weightedZ),
                bestContact.getVelocity(),
                bestContact.getSignalToNoiseRatio(), // 使用最佳SNR
                bestContact.getRadarCrossSection(),
                Math.min(1.0, bestContact.getConfidence() * 1.2), // 多雷达确认提高置信度
                bestContact.getClassification(),
                bestContact.getDopplerShift(),
                bestContact.getRangeAccuracy() / Math.sqrt(contacts.size()), // 多雷达提高精度
                bestContact.getAngleAccuracy() / Math.sqrt(contacts.size()),
                bestContact.getVelocityAccuracy() / Math.sqrt(contacts.size()),
                false, // 融合后不是新探测
                String.format("融合%d个雷达的探测结果", contacts.size())
        );
    }
    
    // ================ 内部类 ================
    
    /**
     * 雷达覆盖统计信息
     */
    public static class RadarCoverageStatistics {
        private final int totalRadars;
        private final int activeRadars;
        private final long totalScans;
        private final LocalDateTime lastScanTime;
        
        public RadarCoverageStatistics(int totalRadars, int activeRadars, long totalScans, LocalDateTime lastScanTime) {
            this.totalRadars = totalRadars;
            this.activeRadars = activeRadars;
            this.totalScans = totalScans;
            this.lastScanTime = lastScanTime;
        }
        
        public int getTotalRadars() { return totalRadars; }
        public int getActiveRadars() { return activeRadars; }
        public long getTotalScans() { return totalScans; }
        public LocalDateTime getLastScanTime() { return lastScanTime; }
        
        public double getActiveRate() {
            return totalRadars > 0 ? (double) activeRadars / totalRadars : 0.0;
        }
        
        @Override
        public String toString() {
            return String.format("RadarCoverageStatistics[total=%d, active=%d(%.1f%%), scans=%d, lastScan=%s]",
                    totalRadars, activeRadars, getActiveRate() * 100, totalScans, lastScanTime);
        }
    }
    
    /**
     * 雷达扫描状态信息
     */
    public static class RadarScanStatus {
        private final String radarId;
        private final double currentScanAngle;
        private final int scanDirection;
        private final LocalDateTime lastScanTime;
        private final long scanCounter;
        
        public RadarScanStatus(String radarId, double currentScanAngle, int scanDirection,
                             LocalDateTime lastScanTime, long scanCounter) {
            this.radarId = radarId;
            this.currentScanAngle = currentScanAngle;
            this.scanDirection = scanDirection;
            this.lastScanTime = lastScanTime;
            this.scanCounter = scanCounter;
        }
        
        public String getRadarId() { return radarId; }
        public double getCurrentScanAngle() { return currentScanAngle; }
        public int getScanDirection() { return scanDirection; }
        public LocalDateTime getLastScanTime() { return lastScanTime; }
        public long getScanCounter() { return scanCounter; }
        
        @Override
        public String toString() {
            return String.format("RadarScanStatus[radar=%s, angle=%.1f°, direction=%d, lastScan=%s, count=%d]",
                    radarId, currentScanAngle, scanDirection, lastScanTime, scanCounter);
        }
    }
} 