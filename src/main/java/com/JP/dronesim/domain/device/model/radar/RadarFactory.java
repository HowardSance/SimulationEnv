package com.JP.dronesim.domain.device.model.radar;

import com.JP.dronesim.application.dtos.request.DeviceInitParamsDTO;
import com.JP.dronesim.domain.common.enums.DeviceType;
import com.JP.dronesim.domain.common.valueobjects.Orientation;
import com.JP.dronesim.domain.common.valueobjects.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * 电磁波雷达工厂类
 * 提供创建和配置雷达的便捷方法
 * 支持不同类型和配置级别的雷达创建
 * 
 * @author JP
 * @version 1.0
 */
public class RadarFactory {
    
    /**
     * 创建标准X波段雷达
     * 
     * @param deviceId 设备ID
     * @param deviceName 设备名称
     * @param position 设备位置
     * @param orientation 设备朝向
     * @param elevation 设备仰角
     * @param detectionRange 探测距离
     * @return 配置好的电磁波雷达实例
     */
    public static ElectromagneticRadar createStandardRadar(String deviceId, String deviceName,
                                                         Position position, double orientation,
                                                         double elevation, double detectionRange) {
        ElectromagneticRadar radar = new ElectromagneticRadar();
        
        DeviceInitParamsDTO initParams = new DeviceInitParamsDTO(
                deviceId, deviceName, DeviceType.ELECTROMAGNETIC_RADAR,
                position, orientation, elevation,
                new Orientation(0.0, 0.0, 0.0), // 默认姿态
                detectionRange, 120.0, // 120度视场角（扇形扫描）
                RadarParameters.createDefault()
        );
        
        radar.initialize(initParams);
        return radar;
    }
    
    /**
     * 创建高精度Ka波段雷达
     * 
     * @param deviceId 设备ID
     * @param deviceName 设备名称
     * @param position 设备位置
     * @param orientation 设备朝向
     * @param elevation 设备仰角
     * @param detectionRange 探测距离
     * @return 配置好的高精度电磁波雷达实例
     */
    public static ElectromagneticRadar createHighPrecisionRadar(String deviceId, String deviceName,
                                                              Position position, double orientation,
                                                              double elevation, double detectionRange) {
        ElectromagneticRadar radar = new ElectromagneticRadar();
        
        DeviceInitParamsDTO initParams = new DeviceInitParamsDTO(
                deviceId, deviceName, DeviceType.ELECTROMAGNETIC_RADAR,
                position, orientation, elevation,
                new Orientation(0.0, 0.0, 0.0), // 默认姿态
                detectionRange, 60.0, // 60度视场角
                RadarParameters.createHighPrecision()
        );
        
        radar.initialize(initParams);
        return radar;
    }
    
    /**
     * 创建远程监视S波段雷达
     * 
     * @param deviceId 设备ID
     * @param deviceName 设备名称
     * @param position 设备位置
     * @param orientation 设备朝向
     * @param elevation 设备仰角
     * @param detectionRange 探测距离
     * @return 配置好的远程监视雷达实例
     */
    public static ElectromagneticRadar createLongRangeRadar(String deviceId, String deviceName,
                                                          Position position, double orientation,
                                                          double elevation, double detectionRange) {
        ElectromagneticRadar radar = new ElectromagneticRadar();
        
        DeviceInitParamsDTO initParams = new DeviceInitParamsDTO(
                deviceId, deviceName, DeviceType.ELECTROMAGNETIC_RADAR,
                position, orientation, elevation,
                new Orientation(0.0, 0.0, 0.0), // 默认姿态
                detectionRange, 360.0, // 360度视场角
                RadarParameters.createLongRange()
        );
        
        radar.initialize(initParams);
        return radar;
    }
    
    /**
     * 创建固定扫描雷达
     * 
     * @param deviceId 设备ID
     * @param deviceName 设备名称
     * @param position 设备位置
     * @param orientation 设备朝向
     * @param elevation 设备仰角
     * @param detectionRange 探测距离
     * @param frequency 工作频率
     * @param power 发射功率
     * @param gain 天线增益
     * @return 配置好的固定扫描雷达实例
     */
    public static ElectromagneticRadar createFixedScanRadar(String deviceId, String deviceName,
                                                          Position position, double orientation,
                                                          double elevation, double detectionRange,
                                                          double frequency, double power, double gain) {
        ElectromagneticRadar radar = new ElectromagneticRadar();
        
        RadarParameters fixedParams = new RadarParameters(
                frequency, power, gain,
                3.0,  // 3dB噪声系数
                10.0, // 10dB最小信噪比
                RadarParameters.ScanPattern.FIXED, // 固定扫描
                1000.0, // 1kHz脉冲重复频率
                1e-6,   // 1微秒脉冲宽度
                2.0,    // 2度波束宽度
                detectionRange, // 最大不模糊距离等于探测距离
                150.0,  // 150m距离分辨率
                1.0     // 1m/s速度分辨率
        );
        
        DeviceInitParamsDTO initParams = new DeviceInitParamsDTO(
                deviceId, deviceName, DeviceType.ELECTROMAGNETIC_RADAR,
                position, orientation, elevation,
                new Orientation(0.0, 0.0, 0.0), // 默认姿态
                detectionRange, 10.0, // 10度视场角（固定扫描）
                fixedParams
        );
        
        radar.initialize(initParams);
        return radar;
    }
    
    /**
     * 创建自定义配置雷达
     * 
     * @param deviceId 设备ID
     * @param deviceName 设备名称
     * @param position 设备位置
     * @param orientation 设备朝向
     * @param elevation 设备仰角
     * @param attitude 设备姿态
     * @param detectionRange 探测距离
     * @param fieldOfView 视场角
     * @param radarParams 雷达参数
     * @return 配置好的自定义雷达实例
     */
    public static ElectromagneticRadar createCustomRadar(String deviceId, String deviceName,
                                                        Position position, double orientation,
                                                        double elevation, Orientation attitude,
                                                        double detectionRange, double fieldOfView,
                                                        RadarParameters radarParams) {
        ElectromagneticRadar radar = new ElectromagneticRadar();
        
        DeviceInitParamsDTO initParams = new DeviceInitParamsDTO(
                deviceId, deviceName, DeviceType.ELECTROMAGNETIC_RADAR,
                position, orientation, elevation, attitude,
                detectionRange, fieldOfView, radarParams
        );
        
        radar.initialize(initParams);
        return radar;
    }
    
    /**
     * 创建用于测试的模拟雷达
     * 
     * @return 配置好的测试用电磁波雷达实例
     */
    public static ElectromagneticRadar createTestRadar() {
        return createStandardRadar(
                "TEST_RADAR_001",
                "测试电磁波雷达",
                new Position(0.0, 0.0, 50.0), // 50米高度
                0.0,     // 朝北
                10.0,    // 10度仰角
                50000.0  // 50km探测距离
        );
    }
    
    /**
     * 批量创建雷达网络
     * 在指定区域内按网格布局创建多个雷达
     * 
     * @param namePrefix 设备名称前缀
     * @param centerPosition 中心位置
     * @param gridSize 网格大小（N x N）
     * @param spacing 雷达间距（米）
     * @param height 雷达高度（米）
     * @param detectionRange 探测距离
     * @return 电磁波雷达列表
     */
    public static List<ElectromagneticRadar> createRadarNetwork(String namePrefix, Position centerPosition,
                                                              int gridSize, double spacing, double height,
                                                              double detectionRange) {
        List<ElectromagneticRadar> radars = new ArrayList<>();
        
        double startX = centerPosition.getX() - (gridSize - 1) * spacing / 2.0;
        double startY = centerPosition.getY() - (gridSize - 1) * spacing / 2.0;
        
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                String deviceId = String.format("%s_RADAR_%02d_%02d", namePrefix, i, j);
                String deviceName = String.format("%s网格雷达[%d,%d]", namePrefix, i, j);
                
                Position radarPos = new Position(
                        startX + i * spacing,
                        startY + j * spacing,
                        height
                );
                
                ElectromagneticRadar radar = createStandardRadar(
                        deviceId, deviceName, radarPos,
                        0.0,  // 朝北
                        15.0, // 15度仰角
                        detectionRange
                );
                
                radars.add(radar);
            }
        }
        
        return radars;
    }
    
    /**
     * 创建环形雷达布局
     * 围绕中心点创建环形分布的雷达
     * 
     * @param namePrefix 设备名称前缀
     * @param centerPosition 中心位置
     * @param radius 半径（米）
     * @param radarCount 雷达数量
     * @param height 雷达高度（米）
     * @param detectionRange 探测距离
     * @return 电磁波雷达列表
     */
    public static List<ElectromagneticRadar> createCircularRadarLayout(String namePrefix, Position centerPosition,
                                                                     double radius, int radarCount,
                                                                     double height, double detectionRange) {
        List<ElectromagneticRadar> radars = new ArrayList<>();
        
        for (int i = 0; i < radarCount; i++) {
            double angle = 2.0 * Math.PI * i / radarCount;
            String deviceId = String.format("%s_CIRCLE_RADAR_%02d", namePrefix, i);
            String deviceName = String.format("%s环形雷达%d", namePrefix, i);
            
            Position radarPos = new Position(
                    centerPosition.getX() + radius * Math.cos(angle),
                    centerPosition.getY() + radius * Math.sin(angle),
                    height
            );
            
            // 雷达朝向中心
            double orientation = Math.toDegrees(angle) + 180.0;
            if (orientation >= 360.0) orientation -= 360.0;
            
            ElectromagneticRadar radar = createStandardRadar(
                    deviceId, deviceName, radarPos,
                    orientation, // 朝向中心
                    20.0,        // 20度仰角
                    detectionRange
            );
            
            radars.add(radar);
        }
        
        return radars;
    }
    
    /**
     * 创建多层雷达防护网
     * 创建内外两层环形雷达布局
     * 
     * @param namePrefix 设备名称前缀
     * @param centerPosition 中心位置
     * @param innerRadius 内圈半径（米）
     * @param outerRadius 外圈半径（米）
     * @param innerCount 内圈雷达数量
     * @param outerCount 外圈雷达数量
     * @param height 雷达高度（米）
     * @param detectionRange 探测距离
     * @return 电磁波雷达列表
     */
    public static List<ElectromagneticRadar> createMultiLayerRadarDefense(String namePrefix, Position centerPosition,
                                                                         double innerRadius, double outerRadius,
                                                                         int innerCount, int outerCount,
                                                                         double height, double detectionRange) {
        List<ElectromagneticRadar> radars = new ArrayList<>();
        
        // 内圈雷达（高精度短程）
        List<ElectromagneticRadar> innerRadars = createCircularRadarLayout(
                namePrefix + "_INNER", centerPosition, innerRadius, innerCount, height, detectionRange / 2
        );
        
        // 将内圈雷达配置为高精度模式
        for (ElectromagneticRadar radar : innerRadars) {
            radar.adjustParameters(RadarParameters.createHighPrecision());
        }
        
        radars.addAll(innerRadars);
        
        // 外圈雷达（远程监视）
        List<ElectromagneticRadar> outerRadars = createCircularRadarLayout(
                namePrefix + "_OUTER", centerPosition, outerRadius, outerCount, height + 20, detectionRange
        );
        
        // 将外圈雷达配置为远程模式
        for (ElectromagneticRadar radar : outerRadars) {
            radar.adjustParameters(RadarParameters.createLongRange());
        }
        
        radars.addAll(outerRadars);
        
        return radars;
    }
    
    /**
     * 创建边界监视雷达链
     * 沿指定路径创建线性排列的雷达
     * 
     * @param namePrefix 设备名称前缀
     * @param startPosition 起始位置
     * @param endPosition 结束位置
     * @param radarCount 雷达数量
     * @param height 雷达高度（米）
     * @param detectionRange 探测距离
     * @return 电磁波雷达列表
     */
    public static List<ElectromagneticRadar> createBorderRadarChain(String namePrefix, Position startPosition,
                                                                  Position endPosition, int radarCount,
                                                                  double height, double detectionRange) {
        List<ElectromagneticRadar> radars = new ArrayList<>();
        
        double deltaX = (endPosition.getX() - startPosition.getX()) / (radarCount - 1);
        double deltaY = (endPosition.getY() - startPosition.getY()) / (radarCount - 1);
        
        // 计算雷达朝向（垂直于边界线）
        double lineAngle = Math.toDegrees(Math.atan2(deltaY, deltaX));
        double radarOrientation = lineAngle + 90.0; // 垂直朝向
        if (radarOrientation >= 360.0) radarOrientation -= 360.0;
        
        for (int i = 0; i < radarCount; i++) {
            String deviceId = String.format("%s_BORDER_%02d", namePrefix, i);
            String deviceName = String.format("%s边界雷达%d", namePrefix, i);
            
            Position radarPos = new Position(
                    startPosition.getX() + i * deltaX,
                    startPosition.getY() + i * deltaY,
                    height
            );
            
            ElectromagneticRadar radar = createStandardRadar(
                    deviceId, deviceName, radarPos,
                    radarOrientation, // 垂直于边界
                    25.0,             // 25度仰角
                    detectionRange
            );
            
            radars.add(radar);
        }
        
        return radars;
    }
    
    /**
     * 私有构造函数，防止实例化
     */
    private RadarFactory() {
        // 工厂类不应被实例化
    }
} 