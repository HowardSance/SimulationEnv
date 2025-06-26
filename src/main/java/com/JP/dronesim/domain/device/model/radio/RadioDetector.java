package com.JP.dronesim.domain.device.model.radio;

import com.JP.dronesim.application.dtos.request.DeviceInitParamsDTO;
import com.JP.dronesim.domain.common.enums.DeviceType;
import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.device.model.AbstractProbeDevice;
import com.JP.dronesim.domain.device.model.common.SensorParameters;
import com.JP.dronesim.domain.device.model.common.events.DetectionEvent;
import com.JP.dronesim.domain.uav.model.AirspaceEnvironment;
import com.JP.dronesim.domain.uav.model.PhysicalSignature;
import com.JP.dronesim.domain.uav.model.UAV;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 无线电监测器实现
 * 用于侦测无人机的无线电信号
 */
public class RadioDetector extends AbstractProbeDevice {

    // 特有属性
    private final double[] frequencyRange; // [min, max] Hz
    private final double sensitivityThreshold; // dBm
    private final double directionFindingAccuracy; // 度

    // 工作状态
    private double currentScanFrequency; // Hz
    private double currentScanBandwidth; // Hz

    /**
     * 构造函数
     */
    public RadioDetector() {
        super(DeviceType.RADIO_DETECTOR);
        this.frequencyRange = new double[]{0, 0};
        this.sensitivityThreshold = 0;
        this.directionFindingAccuracy = 0;
    }

    /**
     * 初始化无线电监测器
     */
    @Override
    public void initialize(DeviceInitParamsDTO params) {
        super.initialize(params);

        // 类型检查
        if (!(params.getDetectionParameters() instanceof RadioParameters)) {
            throw new IllegalArgumentException("无线电监测器需要RadioParameters类型参数");
        }

        RadioParameters radioParams = (RadioParameters) params.getDetectionParameters();
        this.frequencyRange[0] = radioParams.getMinFrequency();
        this.frequencyRange[1] = radioParams.getMaxFrequency();
        // this.sensitivityThreshold = radioParams.getSensitivity();
        // this.directionFindingAccuracy = radioParams.getDirectionAccuracy();
    }

    /**
     * 执行无线电侦测
     */
    @Override
    public List<DetectionEvent> performDetection(AirspaceEnvironment airspace, List<UAV> uavs) {
        List<DetectionEvent> detections = new ArrayList<>();

        if (!getStatus().isActive()) {
            return detections;
        }

        for (UAV uav : uavs) {
            PhysicalSignature signature = uav.getPhysicalSignature();

            // 1. 检查信号强度是否可检测
            double signalPower = signature.getEffectiveRadioEmissionPower();
            if (signalPower < sensitivityThreshold) {
                continue;
            }

            // 2. 检查频率是否在侦测范围内
            if (!isFrequencyInRange(uav.getRadioFrequency())) {
                continue;
            }

            // 3. 计算方位角
            Position uavPos = signature.getCurrentPosition();
            Position detectorPos = getPosition();
            double azimuth = calculateAzimuth(detectorPos, uavPos);
            double elevation = calculateElevation(detectorPos, uavPos);

            // 4. 计算距离
            double distance = detectorPos.distanceTo(uavPos);

            // 5. 创建探测事件
            RadioDetectionEvent event = new RadioDetectionEvent(
                    uav.getId(),
                    LocalDateTime.now(),
                    azimuth,
                    elevation,
                    distance,
                    signalPower,
                    uav.getRadioFrequency(),
                    directionFindingAccuracy
            );

            detections.add(event);
        }

        return detections;
    }

    /**
     * 调整监测器参数
     */
    @Override
    public void adjustParameters(SensorParameters newParams) {
        if (!(newParams instanceof RadioParameters)) {
            throw new IllegalArgumentException("需要RadioParameters类型参数");
        }

        RadioParameters params = (RadioParameters) newParams;
        this.frequencyRange[0] = params.getMinFrequency();
        this.frequencyRange[1] = params.getMaxFrequency();
        // this.sensitivityThreshold = params.getSensitivity();
        // this.directionFindingAccuracy = params.getDirectionAccuracy();

        // 更新扫描频率和带宽
        this.currentScanFrequency = params.getScanFrequency();
        this.currentScanBandwidth = params.getScanBandwidth();
    }

    /**
     * 特有初始化
     */
    @Override
    protected void doSpecificInitialization(DeviceInitParamsDTO params) {

    }

    /**
     * 特有执行侦测
     */
    @Override
    protected List<DetectionEvent> doPerformDetection(AirspaceEnvironment airspace, List<UAV> uavs) {
        return null;
    }

    /**
     * 特有参数调整
     */
    @Override
    protected void doAdjustParameters(SensorParameters newParams) {

    }

    /**
     * 特有重置
     */
    @Override
    protected void doReset() {

    }

    // ================ 特有方法 ================

    /**
     * 检查频率是否在侦测范围内
     */
    private boolean isFrequencyInRange(double frequency) {
        return frequency >= frequencyRange[0] && frequency <= frequencyRange[1];
    }

    /**
     * 计算目标方位角 (0-360度)
     */
    private double calculateAzimuth(Position detectorPos, Position targetPos) {
        double dx = targetPos.getX() - detectorPos.getX();
        double dy = targetPos.getY() - detectorPos.getY();
        double angle = Math.toDegrees(Math.atan2(dy, dx));
        return (angle + 360) % 360; // 确保在0-360范围内
    }

    /**
     * 计算目标仰角 (-90到90度)
     */
    private double calculateElevation(Position detectorPos, Position targetPos) {
        double dz = targetPos.getZ() - detectorPos.getZ();
        double horizontalDist = detectorPos.distanceTo2D(targetPos);
        return Math.toDegrees(Math.atan2(dz, horizontalDist));
    }

    /**
     * 监听空域中无人机发出的无线电信号
     *
     * @param airspace 当前空域环境
     * @param uavs 当前空域中的无人机列表
     * @return 监测到的信号列表，包含信号强度、频率等信息
     */
    public List<RadioSignalDetection> listenForSignals(AirspaceEnvironment airspace, List<UAV> uavs) {
        List<RadioSignalDetection> detections = new ArrayList<>();

        // 检查设备是否处于活跃状态
        if (!getStatus().isActive()) {
            return detections;
        }

        // 遍历所有无人机
        for (UAV uav : uavs) {
            PhysicalSignature signature = uav.getPhysicalSignature();

            // 1. 获取无人机有效无线电发射功率
            double signalPower = signature.getEffectiveRadioEmissionPower();

            // 2. 检查信号强度是否达到灵敏度阈值
            if (signalPower < sensitivityThreshold) {
                continue;
            }

            // 3. 检查频率是否在侦测范围内
            double uavFrequency = uav.getRadioFrequency();
            if (!isFrequencyInRange(uavFrequency)) {
                continue;
            }

            // 4. 计算信号衰减（基于距离和环境因素）
            Position uavPos = signature.getCurrentPosition();
            Position detectorPos = getPosition();
            double distance = detectorPos.distanceTo(uavPos);
            double attenuation = calculateAttenuation(distance, airspace);

            // 5. 计算接收信号强度（考虑衰减）
            double receivedSignalStrength = signalPower - attenuation;

            // 6. 计算方位和仰角
            double azimuth = calculateAzimuth(detectorPos, uavPos);
            double elevation = calculateElevation(detectorPos, uavPos);

            // 7. 创建信号检测结果
            RadioSignalDetection detection = new RadioSignalDetection(
                uav.getId(),
                LocalDateTime.now(),
                azimuth,
                elevation,
                distance,
                receivedSignalStrength,
                uavFrequency,
                directionFindingAccuracy,
                calculateSignalQuality(receivedSignalStrength)
            );

            detections.add(detection);
        }

        return detections;
    }

    /**
     * 计算信号衰减（dB）
     * 自由空间路径损耗公式：FSPL = 20log10(d) + 20log10(f) + 20log10(4π/c)
     * 加上环境因素影响
     */
    private double calculateAttenuation(double distance, AirspaceEnvironment airspace) {
        // 自由空间路径损耗
        double fspl = 20 * Math.log10(distance)
                    + 20 * Math.log10(currentScanFrequency)
                    + 20 * Math.log10(4 * Math.PI / 3e8);

        // 环境因素影响（湿度、温度等）
        double envFactor = 0.0;
        if (airspace.getHumidity() > 70) {
            envFactor += 2.0; // 高湿度增加衰减
        }
        if (airspace.getTemperature() < 0) {
            envFactor += 1.5; // 低温增加衰减
        }

        return fspl + envFactor;
    }

    /**
     * 计算信号质量（0.0-1.0）
     */
    private double calculateSignalQuality(double signalStrength) {
        // 标准化信号强度到0-1范围
        double normalized = (signalStrength - sensitivityThreshold) / 30.0; // 假设30dB动态范围
        return Math.min(1.0, Math.max(0.0, normalized));
    }

    /**
     * 对接收到的信号进行方向估算
     *
     * @param signal 监测到的无线电信号
     * @return 包含信号来源方向信息的 DirectionInfo 对象
     */
    public DirectionInfo estimateDirection(RadioSignalDetection signal) {
        // 1. 获取基本方向信息
        double azimuth = signal.getAzimuth();
        double elevation = signal.getElevation();
        double accuracy = signal.getDirectionAccuracy();

        // 2. 根据信号质量调整精度
        double adjustedAccuracy = adjustAccuracyBasedOnSignalQuality(
                accuracy,
                signal.getSignalQuality()
        );

        // 3. 计算可信度
        double confidence = calculateDirectionConfidence(
                signal.getSignalStrength(),
                signal.getDistance(),
                adjustedAccuracy
        );

        // 4. 返回方向信息
        return new DirectionInfo(
                azimuth,
                elevation,
                adjustedAccuracy,
                confidence,
                LocalDateTime.now()
        );
    }

    /**
     * 根据信号质量调整方向精度
     */
    private double adjustAccuracyBasedOnSignalQuality(double baseAccuracy, double signalQuality) {
        // 信号质量越好，精度越高（误差越小）
        // 线性调整：信号质量为1时精度提高50%，为0时精度降低50%
        double adjustmentFactor = 1.0 - (0.5 * signalQuality);
        return baseAccuracy * adjustmentFactor;
    }

    /**
     * 计算方向可信度 (0.0-1.0)
     */
    private double calculateDirectionConfidence(double signalStrength, double distance, double accuracy) {
        // 基础可信度基于信号强度
        double strengthFactor = Math.min(1.0, (signalStrength - sensitivityThreshold) / 30.0);

        // 距离因素：距离越近可信度越高
        double distanceFactor = 1.0 - Math.min(1.0, distance / 10000.0); // 10km为最大参考距离

        // 精度因素：精度越高可信度越高
        double accuracyFactor = 1.0 - (accuracy / 30.0); // 30度为最大参考误差

        // 综合可信度
        return Math.min(1.0, strengthFactor * 0.5 + distanceFactor * 0.3 + accuracyFactor * 0.2);
    }

    /**
     * 结合多个监测器的信号检测数据，对无人机进行三角定位
     *
     * @param detections 来自不同监测器的同一无人机信号检测列表
     * @return 估算出的无人机位置
     * @throws IllegalArgumentException 如果检测数据不足或无效
     */
    public static Position triangulatePosition(List<RadioSignalDetection> detections) {
        // 1. 参数校验
        if (detections == null || detections.size() < 2) {
            throw new IllegalArgumentException("至少需要两个监测点的数据才能进行三角定位");
        }

        // 2. 准备定位数据
        List<Position> detectorPositions = new ArrayList<>();
        List<Double> azimuths = new ArrayList<>();
        List<Double> elevations = new ArrayList<>();
        List<Double> weights = new ArrayList<>();

        for (RadioSignalDetection detection : detections) {
            // 获取监测器位置（假设RadioSignalDetection有getDetectorPosition方法）
            Position detectorPos = detection.getDetectorPosition();
            detectorPositions.add(detectorPos);

            // 获取方位角和仰角
            azimuths.add(detection.getAzimuth());
            elevations.add(detection.getElevation());

            // 计算权重（基于信号质量和距离）
            double weight = calculateDetectionWeight(detection);
            weights.add(weight);
        }

        // 3. 进行三角定位计算
        if (detections.size() == 2) {
            // 两个监测点时使用简单交叉定位
            return triangulateWithTwoPoints(
                    detectorPositions.get(0), azimuths.get(0), elevations.get(0),
                    detectorPositions.get(1), azimuths.get(1), elevations.get(1)
            );
        } else {
            // 三个及以上监测点时使用加权最小二乘法
            return triangulateWithMultiplePoints(detectorPositions, azimuths, elevations, weights);
        }
    }

    /**
     * 计算检测数据的权重
     */
    private static double calculateDetectionWeight(RadioSignalDetection detection) {
        // 信号质量权重 (0.0-1.0)
        double qualityWeight = detection.getSignalQuality();

        // 距离权重 - 距离越近权重越高
        double distanceWeight = 1.0 / (1.0 + detection.getDistance() / 1000.0); // 1km为参考距离

        // 方向精度权重 - 精度越高权重越高
        double accuracyWeight = 1.0 / (1.0 + detection.getDirectionAccuracy() / 10.0); // 10度为参考误差

        return qualityWeight * 0.5 + distanceWeight * 0.3 + accuracyWeight * 0.2;
    }

    /**
     * 两个监测点的简单交叉定位
     */
    private static Position triangulateWithTwoPoints(
            Position pos1, double azimuth1, double elevation1,
            Position pos2, double azimuth2, double elevation2) {

        // 将极坐标转换为方向向量
        double[] dir1 = sphericalToCartesian(azimuth1, elevation1);
        double[] dir2 = sphericalToCartesian(azimuth2, elevation2);

        // 计算两条射线的交点
        return findLinesIntersection(pos1, dir1, pos2, dir2);
    }

    /**
     * 多个监测点的加权最小二乘定位
     */
    private static Position triangulateWithMultiplePoints(
            List<Position> positions,
            List<Double> azimuths,
            List<Double> elevations,
            List<Double> weights) {

        // 初始估计位置 - 使用第一个检测点沿方向延伸的平均距离
        Position initialEstimate = getInitialEstimate(positions, azimuths, elevations);

        // 使用Levenberg-Marquardt算法进行非线性优化
        return optimizePosition(initialEstimate, positions, azimuths, elevations, weights);
    }

    /**
     * 将球面坐标(方位角,仰角)转换为笛卡尔方向向量
     */
    private static double[] sphericalToCartesian(double azimuth, double elevation) {
        double azimuthRad = Math.toRadians(azimuth);
        double elevationRad = Math.toRadians(elevation);

        double x = Math.cos(elevationRad) * Math.cos(azimuthRad);
        double y = Math.cos(elevationRad) * Math.sin(azimuthRad);
        double z = Math.sin(elevationRad);

        return new double[]{x, y, z};
    }

    /**
     * 计算两条射线的交点
     */
    private static Position findLinesIntersection(Position p1, double[] dir1,
                                                  Position p2, double[] dir2) {
        // 实现两条射线最短距离点的计算
        // 此处简化处理，返回中点位置
        double x = (p1.getX() + p2.getX()) / 2;
        double y = (p1.getY() + p2.getY()) / 2;
        double z = (p1.getZ() + p2.getZ()) / 2;

        return new Position(x, y, z);
    }

    /**
     * 获取初始估计位置
     */
    private static Position getInitialEstimate(List<Position> positions,
                                               List<Double> azimuths,
                                               List<Double> elevations) {
        double sumX = 0, sumY = 0, sumZ = 0;
        double count = positions.size();

        for (int i = 0; i < positions.size(); i++) {
            Position pos = positions.get(i);
            double[] dir = sphericalToCartesian(azimuths.get(i), elevations.get(i));

            // 沿方向延伸平均距离500米
            sumX += pos.getX() + dir[0] * 500;
            sumY += pos.getY() + dir[1] * 500;
            sumZ += pos.getZ() + dir[2] * 500;
        }

        return new Position(sumX / count, sumY / count, sumZ / count);
    }

    /**
     * 使用Levenberg-Marquardt算法优化位置估计
     */
    private static Position optimizePosition(Position initial,
                                             List<Position> positions,
                                             List<Double> azimuths,
                                             List<Double> elevations,
                                             List<Double> weights) {
        // 简化实现 - 实际项目应使用数学优化库
        // 这里返回初始估计位置
        return initial;
    }



}

