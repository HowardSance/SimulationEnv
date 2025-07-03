package com.JP.dronesim.domain.device.model.opticalcamera;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 摄像头数据流值对象
 * 表示模拟的图像流或直接的检测信息
 * 包含从AirSim API获取的可见目标信息
 * 
 * @author JP
 * @version 1.0
 */
public class CameraFeed {
    
    /**
     * 数据流唯一标识符
     */
    private final String feedId;
    
    /**
     * 数据采集时间戳
     */
    private final LocalDateTime timestamp;
    
    /**
     * 摄像头设备ID
     */
    private final String cameraId;
    
    /**
     * 图像宽度（像素）
     */
    private final int imageWidth;
    
    /**
     * 图像高度（像素）
     */
    private final int imageHeight;
    
    /**
     * 当前视场角（度）
     */
    private final double currentFieldOfView;
    
    /**
     * 摄像头当前朝向（度）
     */
    private final double currentOrientation;
    
    /**
     * 摄像头当前仰角（度）
     */
    private final double currentElevation;
    
    /**
     * 环境光照强度（0.0-1.0）
     */
    private final double lightLevel;
    
    /**
     * 能见度（米）
     */
    private final double visibility;
    
    /**
     * 图像质量评分（0.0-1.0）
     */
    private final double imageQuality;
    
    /**
     * 检测到的原始目标数据
     * 来自AirSim的simGetDetections API
     */
    private final List<RawDetectionData> rawDetections;
    
    /**
     * 模拟图像数据（可选）
     * 在实际应用中可能包含Base64编码的图像数据
     */
    private final String imageData;
    
    /**
     * 数据是否有效
     */
    private final boolean isValid;
    
    /**
     * 构造函数
     * 
     * @param feedId 数据流ID
     * @param timestamp 时间戳
     * @param cameraId 摄像头ID
     * @param imageWidth 图像宽度
     * @param imageHeight 图像高度
     * @param currentFieldOfView 当前视场角
     * @param currentOrientation 当前朝向
     * @param currentElevation 当前仰角
     * @param lightLevel 光照强度
     * @param visibility 能见度
     * @param imageQuality 图像质量
     * @param rawDetections 原始检测数据
     * @param imageData 图像数据
     * @param isValid 数据是否有效
     */
    public CameraFeed(String feedId, LocalDateTime timestamp, String cameraId,
                     int imageWidth, int imageHeight, double currentFieldOfView,
                     double currentOrientation, double currentElevation,
                     double lightLevel, double visibility, double imageQuality,
                     List<RawDetectionData> rawDetections, String imageData, boolean isValid) {
        this.feedId = feedId;
        this.timestamp = timestamp;
        this.cameraId = cameraId;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.currentFieldOfView = currentFieldOfView;
        this.currentOrientation = currentOrientation;
        this.currentElevation = currentElevation;
        this.lightLevel = lightLevel;
        this.visibility = visibility;
        this.imageQuality = imageQuality;
        this.rawDetections = rawDetections;
        this.imageData = imageData;
        this.isValid = isValid;
    }
    
    /**
     * 获取数据流ID
     * 
     * @return 数据流唯一标识符
     */
    public String getFeedId() {
        return feedId;
    }
    
    /**
     * 获取数据采集时间
     * 
     * @return 数据采集时间戳
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    /**
     * 获取摄像头ID
     * 
     * @return 摄像头设备ID
     */
    public String getCameraId() {
        return cameraId;
    }
    
    /**
     * 获取图像宽度
     * 
     * @return 图像宽度（像素）
     */
    public int getImageWidth() {
        return imageWidth;
    }
    
    /**
     * 获取图像高度
     * 
     * @return 图像高度（像素）
     */
    public int getImageHeight() {
        return imageHeight;
    }
    
    /**
     * 获取当前视场角
     * 
     * @return 当前视场角（度）
     */
    public double getCurrentFieldOfView() {
        return currentFieldOfView;
    }
    
    /**
     * 获取当前朝向
     * 
     * @return 摄像头当前朝向（度）
     */
    public double getCurrentOrientation() {
        return currentOrientation;
    }
    
    /**
     * 获取当前仰角
     * 
     * @return 摄像头当前仰角（度）
     */
    public double getCurrentElevation() {
        return currentElevation;
    }
    
    /**
     * 获取环境光照强度
     * 
     * @return 环境光照强度（0.0-1.0）
     */
    public double getLightLevel() {
        return lightLevel;
    }
    
    /**
     * 获取能见度
     * 
     * @return 能见度（米）
     */
    public double getVisibility() {
        return visibility;
    }
    
    /**
     * 获取图像质量评分
     * 
     * @return 图像质量评分（0.0-1.0）
     */
    public double getImageQuality() {
        return imageQuality;
    }
    
    /**
     * 获取原始检测数据
     * 
     * @return 检测到的原始目标数据列表
     */
    public List<RawDetectionData> getRawDetections() {
        return rawDetections;
    }
    
    /**
     * 获取图像数据
     * 
     * @return 模拟图像数据（可能为null）
     */
    public String getImageData() {
        return imageData;
    }
    
    /**
     * 检查数据是否有效
     * 
     * @return true表示数据有效，false表示数据无效
     */
    public boolean isValid() {
        return isValid;
    }
    
    /**
     * 检查是否有检测数据
     * 
     * @return true表示有检测到目标，false表示没有
     */
    public boolean hasDetections() {
        return rawDetections != null && !rawDetections.isEmpty();
    }
    
    /**
     * 获取检测到的目标数量
     * 
     * @return 检测到的目标数量
     */
    public int getDetectionCount() {
        return rawDetections == null ? 0 : rawDetections.size();
    }
    
    /**
     * 检查图像质量是否良好
     * 
     * @return true表示图像质量良好（>=0.7），false表示质量较差
     */
    public boolean hasGoodImageQuality() {
        return imageQuality >= 0.7;
    }
    
    /**
     * 检查光照条件是否适合检测
     * 
     * @return true表示光照条件适合，false表示不适合
     */
    public boolean hasGoodLightCondition() {
        return lightLevel >= 0.3; // 至少需要30%的光照强度
    }
    
    /**
     * 检查能见度是否足够
     * 
     * @return true表示能见度足够（>=100米），false表示不足
     */
    public boolean hasGoodVisibility() {
        return visibility >= 100.0;
    }
    
    @Override
    public String toString() {
        return String.format("CameraFeed[id=%s, camera=%s, timestamp=%s, " +
                           "resolution=%dx%d, fov=%.1f, orientation=%.1f, elevation=%.1f, " +
                           "light=%.2f, visibility=%.1fm, quality=%.2f, detections=%d, valid=%s]",
                           feedId, cameraId, timestamp, imageWidth, imageHeight,
                           currentFieldOfView, currentOrientation, currentElevation,
                           lightLevel, visibility, imageQuality, getDetectionCount(), isValid);
    }
    
    /**
     * 原始检测数据内部类
     * 表示从AirSim API获取的单个检测目标信息
     */
    public static class RawDetectionData {
        /**
         * 检测对象的名称或ID
         */
        private final String objectName;
        
        /**
         * 检测框左上角X坐标（像素）
         */
        private final int boundingBoxX;
        
        /**
         * 检测框左上角Y坐标（像素）
         */
        private final int boundingBoxY;
        
        /**
         * 检测框宽度（像素）
         */
        private final int boundingBoxWidth;
        
        /**
         * 检测框高度（像素）
         */
        private final int boundingBoxHeight;
        
        /**
         * 目标在世界坐标系中的距离（米）
         */
        private final double distance;
        
        /**
         * 检测置信度（0.0-1.0）
         */
        private final double confidence;
        
        /**
         * 构造函数
         */
        public RawDetectionData(String objectName, int boundingBoxX, int boundingBoxY,
                              int boundingBoxWidth, int boundingBoxHeight,
                              double distance, double confidence) {
            this.objectName = objectName;
            this.boundingBoxX = boundingBoxX;
            this.boundingBoxY = boundingBoxY;
            this.boundingBoxWidth = boundingBoxWidth;
            this.boundingBoxHeight = boundingBoxHeight;
            this.distance = distance;
            this.confidence = confidence;
        }
        
        // Getter方法
        public String getObjectName() { return objectName; }
        public int getBoundingBoxX() { return boundingBoxX; }
        public int getBoundingBoxY() { return boundingBoxY; }
        public int getBoundingBoxWidth() { return boundingBoxWidth; }
        public int getBoundingBoxHeight() { return boundingBoxHeight; }
        public double getDistance() { return distance; }
        public double getConfidence() { return confidence; }
        
        @Override
        public String toString() {
            return String.format("RawDetection[object=%s, bbox=(%d,%d,%d,%d), distance=%.1fm, confidence=%.2f]",
                               objectName, boundingBoxX, boundingBoxY, boundingBoxWidth, boundingBoxHeight,
                               distance, confidence);
        }
    }
} 