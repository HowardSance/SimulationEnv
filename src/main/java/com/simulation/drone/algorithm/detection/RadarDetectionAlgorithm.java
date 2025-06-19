package com.simulation.drone.algorithm.detection;

import com.simulation.drone.dto.detection.DetectionResultDTO;
import com.simulation.drone.entity.simulation.DetectionDevice;
import com.simulation.drone.entity.simulation.DroneEntity;
import com.simulation.drone.util.MathUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 雷达探测算法实现
 * 基于雷达方程的物理模型：Pr = (Pt × Gt × Gr × λ² × σ) / ((4π)³ × R⁴ × L)
 */
@Slf4j
@Component
public class RadarDetectionAlgorithm implements DetectionAlgorithm {
    
    // 物理常数
    private static final double LIGHT_SPEED = 3.0e8; // 光速 m/s
    private static final double FOUR_PI_CUBED = Math.pow(4 * Math.PI, 3);
    private static final double BOLTZMANN_CONSTANT = 1.38e-23; // 玻尔兹曼常数
    
    // 默认参数
    private static final double DEFAULT_SYSTEM_TEMPERATURE = 290.0; // 系统温度 K
    private static final double DEFAULT_NOISE_FIGURE = 3.0; // 噪声系数 dB
    private static final double DEFAULT_DETECTION_THRESHOLD = 13.0; // 检测阈值 dB
    
    @Override
    public DetectionResultDTO detect(DetectionDevice device, DroneEntity drone, Map<String, Object> environment) {
        DetectionResultDTO result = new DetectionResultDTO();
        result.setDeviceId(device.getId().toString());
        result.setTargetId(drone.getDroneId());
        result.setDetectionType("RADAR");
        result.setTimestamp(System.currentTimeMillis());
        
        try {
            // 计算几何关系
            Map<String, Double> devicePos = createDevicePositionMap(device);
            Map<String, Double> dronePos = createDronePositionMap(drone);
            
            double range = MathUtils.calculateDistance(devicePos, dronePos);
            
            // 检查基本范围
            double maxRange = device.getRange() != null ? device.getRange() : 10000.0;
            double minRange = 100.0;
            
            if (range < minRange || range > maxRange) {
                result.setProbability(0.0);
                result.setQuality(0.0);
                result.setErrorMessage("目标超出探测范围");
                return result;
            }
            
            // 雷达参数
            double transmitPower = device.getPower() != null ? device.getPower() : 1000.0; // W
            double frequency = device.getFrequency() != null ? device.getFrequency() : 3.0e9; // Hz
            double wavelength = LIGHT_SPEED / frequency;
            double antennaGain = 35.0; // dB
            double beamWidth = device.getBeamWidth() != null ? device.getBeamWidth() : 2.0; // 度
            
            // 目标特征
            double rcs = 0.1; // m² 典型小型无人机RCS
            
            // 环境因素
            double atmosphericLoss = (Double) environment.getOrDefault("atmosphericLoss", 0.1); // dB/km
            double weatherFactor = (Double) environment.getOrDefault("weatherFactor", 1.0);
            
            // 计算角度
            double bearing = MathUtils.calculateBearing(devicePos, dronePos);
            double deviceHeading = device.getHeading() != null ? device.getHeading() : 0.0;
            double beamAngle = Math.abs(bearing - deviceHeading);
            
            // 检查是否在波束内
            if (beamAngle > beamWidth / 2.0) {
                result.setProbability(0.0);
                result.setQuality(0.0);
                result.setErrorMessage("目标不在雷达波束内");
                return result;
            }
            
            // 雷达方程计算
            double gtLinear = Math.pow(10, antennaGain / 10.0);
            double grLinear = gtLinear; // 假设收发天线相同
            
            // 计算接收功率
            double numerator = transmitPower * gtLinear * grLinear * 
                              Math.pow(wavelength, 2) * rcs;
            double denominator = FOUR_PI_CUBED * Math.pow(range, 4);
            
            // 大气损耗
            double atmosphericLossLinear = Math.pow(10, -atmosphericLoss * range / 1000.0 / 10.0);
            
            // 天线方向图损失
            double patternLoss = Math.exp(-2.77 * Math.pow(beamAngle / (beamWidth / 2.0), 2));
            
            double receivedPower = numerator / denominator * atmosphericLossLinear * patternLoss * weatherFactor;
            
            // 计算噪声功率
            double bandwidth = 1.0e6; // 1MHz
            double noiseFigureLinear = Math.pow(10, DEFAULT_NOISE_FIGURE / 10.0);
            double noisePower = BOLTZMANN_CONSTANT * DEFAULT_SYSTEM_TEMPERATURE * bandwidth * noiseFigureLinear;
            
            // 信噪比
            double snr = receivedPower / noisePower;
            double snrDb = 10 * Math.log10(snr);
            
            // 计算探测概率
            double detectionProbability = calculateDetectionProbability(snrDb);
            
            // 计算探测质量
            double quality = Math.max(0.0, Math.min(1.0, snrDb / 30.0));
            
            // 填充结果
            result.setProbability(detectionProbability);
            result.setQuality(quality);
            result.setPosition(dronePos);
            result.setVelocity(createVelocityMap(drone));
            
            // 设备状态
            Map<String, Object> deviceStatus = new HashMap<>();
            deviceStatus.put("snr", snrDb);
            deviceStatus.put("range", range);
            deviceStatus.put("bearing", bearing);
            deviceStatus.put("receivedPower", receivedPower);
            result.setDeviceStatus(deviceStatus);
            
            // 环境因素
            Map<String, Double> envFactors = new HashMap<>();
            envFactors.put("atmosphericLoss", atmosphericLoss);
            envFactors.put("weatherFactor", weatherFactor);
            result.setEnvironmentFactors(envFactors);
            
        } catch (Exception e) {
            log.error("雷达探测算法执行失败", e);
            result.setProbability(0.0);
            result.setQuality(0.0);
            result.setErrorMessage("算法执行异常: " + e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public boolean supportsDevice(DetectionDevice device) {
        return DetectionDevice.DeviceType.RADAR.equals(device.getType());
    }
    
    @Override
    public Map<String, Object> getAlgorithmConfiguration() {
        Map<String, Object> config = new HashMap<>();
        config.put("algorithmType", "RADAR");
        config.put("version", "1.0");
        config.put("description", "基于雷达方程的探测算法");
        config.put("physicalModel", "雷达方程");
        config.put("detectionThreshold", DEFAULT_DETECTION_THRESHOLD);
        return config;
    }
    
    /**
     * 计算探测概率
     */
    private double calculateDetectionProbability(double snrDb) {
        if (snrDb < 0) {
            return 0.0;
        }
        
        if (snrDb >= DEFAULT_DETECTION_THRESHOLD) {
            return Math.min(0.99, 0.5 + (snrDb - DEFAULT_DETECTION_THRESHOLD) / 20.0);
        } else {
            return Math.max(0.01, 0.5 * Math.exp((snrDb - DEFAULT_DETECTION_THRESHOLD) / 5.0));
        }
    }
    
    private Map<String, Double> createDevicePositionMap(DetectionDevice device) {
        Map<String, Double> pos = new HashMap<>();
        pos.put("x", device.getPosition().getX());
        pos.put("y", device.getPosition().getY());
        pos.put("z", device.getPosition().getZ() + (device.getAltitude() != null ? device.getAltitude() : 0.0));
        return pos;
    }
    
    private Map<String, Double> createDronePositionMap(DroneEntity drone) {
        Map<String, Double> pos = new HashMap<>();
        pos.put("x", drone.getPosition().getX());
        pos.put("y", drone.getPosition().getY());
        pos.put("z", drone.getPosition().getZ() + (drone.getAltitude() != null ? drone.getAltitude() : 0.0));
        return pos;
    }
    
    private Map<String, Double> createVelocityMap(DroneEntity drone) {
        Map<String, Double> vel = new HashMap<>();
        if (drone.getVelocity() != null) {
            vel.put("vx", drone.getVelocity().getX());
            vel.put("vy", drone.getVelocity().getY());
            vel.put("vz", drone.getVelocity().getZ());
        } else {
            vel.put("vx", 0.0);
            vel.put("vy", 0.0);
            vel.put("vz", 0.0);
        }
        return vel;
    }
} 