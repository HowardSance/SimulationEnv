package com.JP.dronesim.domain.device.model.opticalcamera;

import com.JP.dronesim.application.dtos.request.DeviceInitParamsDTO;
import com.JP.dronesim.domain.common.enums.DeviceType;
import com.JP.dronesim.domain.common.valueobjects.Orientation;
import com.JP.dronesim.domain.common.valueobjects.Position;

/**
 * 光电摄像头工厂类
 * 提供创建和配置光电摄像头的便捷方法
 * 支持不同配置级别的摄像头创建
 * 
 * @author JP
 * @version 1.0
 */
public class OpticalCameraFactory {
    
    /**
     * 创建标准配置的光电摄像头
     * 
     * @param deviceId 设备ID
     * @param deviceName 设备名称
     * @param position 设备位置
     * @param orientation 设备朝向
     * @param elevation 设备仰角
     * @param detectionRange 探测距离
     * @return 配置好的光电摄像头实例
     */
    public static OpticalCamera createStandardCamera(String deviceId, String deviceName,
                                                   Position position, double orientation, 
                                                   double elevation, double detectionRange) {
        OpticalCamera camera = new OpticalCamera();
        
        DeviceInitParamsDTO initParams = new DeviceInitParamsDTO(
                deviceId, deviceName, DeviceType.OPTICAL_CAMERA,
                position, orientation, elevation,
                new Orientation(0.0, 0.0, 0.0), // 默认姿态
                detectionRange, 60.0, // 60度视场角
                OpticalParameters.createDefault()
        );
        
        camera.initialize(initParams);
        return camera;
    }
    
    /**
     * 创建高精度配置的光电摄像头
     * 
     * @param deviceId 设备ID
     * @param deviceName 设备名称
     * @param position 设备位置
     * @param orientation 设备朝向
     * @param elevation 设备仰角
     * @param detectionRange 探测距离
     * @return 配置好的高精度光电摄像头实例
     */
    public static OpticalCamera createHighPrecisionCamera(String deviceId, String deviceName,
                                                        Position position, double orientation,
                                                        double elevation, double detectionRange) {
        OpticalCamera camera = new OpticalCamera();
        
        DeviceInitParamsDTO initParams = new DeviceInitParamsDTO(
                deviceId, deviceName, DeviceType.OPTICAL_CAMERA,
                position, orientation, elevation,
                new Orientation(0.0, 0.0, 0.0), // 默认姿态
                detectionRange, 45.0, // 45度视场角
                OpticalParameters.createHighPrecision()
        );
        
        camera.initialize(initParams);
        return camera;
    }
    
    /**
     * 创建夜视配置的光电摄像头
     * 
     * @param deviceId 设备ID
     * @param deviceName 设备名称
     * @param position 设备位置
     * @param orientation 设备朝向
     * @param elevation 设备仰角
     * @param detectionRange 探测距离
     * @return 配置好的夜视光电摄像头实例
     */
    public static OpticalCamera createNightVisionCamera(String deviceId, String deviceName,
                                                      Position position, double orientation,
                                                      double elevation, double detectionRange) {
        OpticalCamera camera = new OpticalCamera();
        
        // 夜视特化参数
        OpticalParameters nightVisionParams = new OpticalParameters(
                1920, 1080,  // 1080p分辨率
                50.0,        // 50mm焦距
                3200,        // 高ISO用于夜视
                30.0,        // 30fps
                8,           // 最小目标8像素
                600,         // 最大目标600像素
                0.6,         // 60%置信度阈值（夜间降低要求）
                true,        // 启用夜视
                2.0          // 2倍光学变焦
        );
        
        DeviceInitParamsDTO initParams = new DeviceInitParamsDTO(
                deviceId, deviceName, DeviceType.OPTICAL_CAMERA,
                position, orientation, elevation,
                new Orientation(0.0, 0.0, 0.0), // 默认姿态
                detectionRange, 50.0, // 50度视场角
                nightVisionParams
        );
        
        camera.initialize(initParams);
        return camera;
    }
    
    /**
     * 创建自定义配置的光电摄像头
     * 
     * @param deviceId 设备ID
     * @param deviceName 设备名称
     * @param position 设备位置
     * @param orientation 设备朝向
     * @param elevation 设备仰角
     * @param attitude 设备姿态
     * @param detectionRange 探测距离
     * @param fieldOfView 视场角
     * @param opticalParams 光电参数
     * @return 配置好的自定义光电摄像头实例
     */
    public static OpticalCamera createCustomCamera(String deviceId, String deviceName,
                                                 Position position, double orientation,
                                                 double elevation, Orientation attitude,
                                                 double detectionRange, double fieldOfView,
                                                 OpticalParameters opticalParams) {
        OpticalCamera camera = new OpticalCamera();
        
        DeviceInitParamsDTO initParams = new DeviceInitParamsDTO(
                deviceId, deviceName, DeviceType.OPTICAL_CAMERA,
                position, orientation, elevation, attitude,
                detectionRange, fieldOfView, opticalParams
        );
        
        camera.initialize(initParams);
        return camera;
    }
    
    /**
     * 创建用于测试的模拟光电摄像头
     * 
     * @return 配置好的测试用光电摄像头实例
     */
    public static OpticalCamera createTestCamera() {
        return createStandardCamera(
                "TEST_OPTICAL_001",
                "测试光电摄像头",
                new Position(0.0, 0.0, 10.0), // 10米高度
                0.0,   // 朝北
                45.0,  // 45度仰角
                1000.0 // 1000米探测距离
        );
    }
    
    /**
     * 批量创建光电摄像头网络
     * 在指定区域内按网格布局创建多个摄像头
     * 
     * @param namePrefix 设备名称前缀
     * @param centerPosition 中心位置
     * @param gridSize 网格大小（N x N）
     * @param spacing 摄像头间距（米）
     * @param height 摄像头高度（米）
     * @param detectionRange 探测距离
     * @return 光电摄像头列表
     */
    public static java.util.List<OpticalCamera> createCameraNetwork(String namePrefix, Position centerPosition,
                                                                   int gridSize, double spacing, double height,
                                                                   double detectionRange) {
        java.util.List<OpticalCamera> cameras = new java.util.ArrayList<>();
        
        double startX = centerPosition.getX() - (gridSize - 1) * spacing / 2.0;
        double startY = centerPosition.getY() - (gridSize - 1) * spacing / 2.0;
        
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                String deviceId = String.format("%s_%02d_%02d", namePrefix, i, j);
                String deviceName = String.format("%s网格摄像头[%d,%d]", namePrefix, i, j);
                
                Position cameraPos = new Position(
                        startX + i * spacing,
                        startY + j * spacing,
                        height
                );
                
                OpticalCamera camera = createStandardCamera(
                        deviceId, deviceName, cameraPos,
                        0.0,  // 朝北
                        30.0, // 30度仰角
                        detectionRange
                );
                
                cameras.add(camera);
            }
        }
        
        return cameras;
    }
    
    /**
     * 创建环形摄像头布局
     * 围绕中心点创建环形分布的摄像头
     * 
     * @param namePrefix 设备名称前缀
     * @param centerPosition 中心位置
     * @param radius 半径（米）
     * @param cameraCount 摄像头数量
     * @param height 摄像头高度（米）
     * @param detectionRange 探测距离
     * @return 光电摄像头列表
     */
    public static java.util.List<OpticalCamera> createCircularCameraLayout(String namePrefix, Position centerPosition,
                                                                          double radius, int cameraCount, 
                                                                          double height, double detectionRange) {
        java.util.List<OpticalCamera> cameras = new java.util.ArrayList<>();
        
        for (int i = 0; i < cameraCount; i++) {
            double angle = 2.0 * Math.PI * i / cameraCount;
            String deviceId = String.format("%s_CIRCLE_%02d", namePrefix, i);
            String deviceName = String.format("%s环形摄像头%d", namePrefix, i);
            
            Position cameraPos = new Position(
                    centerPosition.getX() + radius * Math.cos(angle),
                    centerPosition.getY() + radius * Math.sin(angle),
                    height
            );
            
            // 摄像头朝向中心
            double orientation = Math.toDegrees(angle) + 180.0;
            if (orientation >= 360.0) orientation -= 360.0;
            
            OpticalCamera camera = createStandardCamera(
                    deviceId, deviceName, cameraPos,
                    orientation, // 朝向中心
                    30.0,        // 30度仰角
                    detectionRange
            );
            
            cameras.add(camera);
        }
        
        return cameras;
    }
    
    /**
     * 私有构造函数，防止实例化
     */
    private OpticalCameraFactory() {
        // 工厂类不应被实例化
    }
} 