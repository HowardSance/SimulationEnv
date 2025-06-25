package com.JP.dronesim.domain.device.model.opticalcamera;

import com.JP.dronesim.domain.device.model.common.SensorParameters;

/**
 * 光电摄像头特有参数类，实现SensorParameters接口
 * 包含分辨率、焦距、灵敏度等光电摄像头专用参数
 * 
 * @author JP
 * @version 1.0
 */
public class OpticalParameters implements SensorParameters {
    
    /**
     * 图像分辨率宽度（像素）
     */
    private final int resolutionWidth;
    
    /**
     * 图像分辨率高度（像素）
     */
    private final int resolutionHeight;
    
    /**
     * 焦距（毫米）
     */
    private final double focalLength;
    
    /**
     * 灵敏度（ISO值）
     */
    private final int sensitivity;
    
    /**
     * 帧率（FPS）
     */
    private final double frameRate;
    
    /**
     * 最小检测目标尺寸（像素）
     */
    private final int minTargetSize;
    
    /**
     * 最大检测目标尺寸（像素）
     */
    private final int maxTargetSize;
    
    /**
     * 检测置信度阈值（0.0-1.0）
     */
    private final double confidenceThreshold;
    
    /**
     * 是否启用夜视模式
     */
    private final boolean nightVisionEnabled;
    
    /**
     * 光学变焦倍数
     */
    private final double zoomFactor;
    
    /**
     * 构造函数
     * 
     * @param resolutionWidth 分辨率宽度
     * @param resolutionHeight 分辨率高度
     * @param focalLength 焦距
     * @param sensitivity 灵敏度
     * @param frameRate 帧率
     * @param minTargetSize 最小目标尺寸
     * @param maxTargetSize 最大目标尺寸
     * @param confidenceThreshold 置信度阈值
     * @param nightVisionEnabled 夜视模式
     * @param zoomFactor 变焦倍数
     */
    public OpticalParameters(int resolutionWidth, int resolutionHeight, double focalLength,
                           int sensitivity, double frameRate, int minTargetSize, int maxTargetSize,
                           double confidenceThreshold, boolean nightVisionEnabled, double zoomFactor) {
        this.resolutionWidth = resolutionWidth;
        this.resolutionHeight = resolutionHeight;
        this.focalLength = focalLength;
        this.sensitivity = sensitivity;
        this.frameRate = frameRate;
        this.minTargetSize = minTargetSize;
        this.maxTargetSize = maxTargetSize;
        this.confidenceThreshold = confidenceThreshold;
        this.nightVisionEnabled = nightVisionEnabled;
        this.zoomFactor = zoomFactor;
    }
    
    /**
     * 获取分辨率宽度
     * 
     * @return 图像分辨率宽度（像素）
     */
    public int getResolutionWidth() {
        return resolutionWidth;
    }
    
    /**
     * 获取分辨率高度
     * 
     * @return 图像分辨率高度（像素）
     */
    public int getResolutionHeight() {
        return resolutionHeight;
    }
    
    /**
     * 获取焦距
     * 
     * @return 焦距（毫米）
     */
    public double getFocalLength() {
        return focalLength;
    }
    
    /**
     * 获取灵敏度
     * 
     * @return 灵敏度（ISO值）
     */
    public int getSensitivity() {
        return sensitivity;
    }
    
    /**
     * 获取帧率
     * 
     * @return 帧率（FPS）
     */
    public double getFrameRate() {
        return frameRate;
    }
    
    /**
     * 获取最小目标尺寸
     * 
     * @return 最小检测目标尺寸（像素）
     */
    public int getMinTargetSize() {
        return minTargetSize;
    }
    
    /**
     * 获取最大目标尺寸
     * 
     * @return 最大检测目标尺寸（像素）
     */
    public int getMaxTargetSize() {
        return maxTargetSize;
    }
    
    /**
     * 获取置信度阈值
     * 
     * @return 检测置信度阈值（0.0-1.0）
     */
    public double getConfidenceThreshold() {
        return confidenceThreshold;
    }
    
    /**
     * 是否启用夜视模式
     * 
     * @return true表示启用夜视，false表示禁用
     */
    public boolean isNightVisionEnabled() {
        return nightVisionEnabled;
    }
    
    /**
     * 获取变焦倍数
     * 
     * @return 光学变焦倍数
     */
    public double getZoomFactor() {
        return zoomFactor;
    }
    
    @Override
    public boolean isValid() {
        return resolutionWidth > 0 && resolutionHeight > 0 &&
               focalLength > 0.0 && sensitivity > 0 &&
               frameRate > 0.0 && frameRate <= 120.0 &&
               minTargetSize > 0 && maxTargetSize > minTargetSize &&
               confidenceThreshold >= 0.0 && confidenceThreshold <= 1.0 &&
               zoomFactor > 0.0;
    }
    
    @Override
    public String getParametersDescription() {
        return String.format("OpticalParameters[resolution=%dx%d, focal=%.2fmm, " +
                           "sensitivity=%d, fps=%.1f, targetSize=%d-%d, " +
                           "confidence=%.2f, nightVision=%s, zoom=%.1fx]",
                           resolutionWidth, resolutionHeight, focalLength,
                           sensitivity, frameRate, minTargetSize, maxTargetSize,
                           confidenceThreshold, nightVisionEnabled, zoomFactor);
    }
    
    @Override
    public SensorParameters clone() {
        return new OpticalParameters(resolutionWidth, resolutionHeight, focalLength,
                                   sensitivity, frameRate, minTargetSize, maxTargetSize,
                                   confidenceThreshold, nightVisionEnabled, zoomFactor);
    }
    
    @Override
    public void updateFrom(SensorParameters newParameters) {
        if (!(newParameters instanceof OpticalParameters)) {
            throw new IllegalArgumentException("参数类型不匹配，期望OpticalParameters类型");
        }
        
        OpticalParameters newOpticalParams = (OpticalParameters) newParameters;
        if (!newOpticalParams.isValid()) {
            throw new IllegalArgumentException("新参数无效");
        }
        
        // 由于所有字段都是final，需要创建新实例来更新参数
        // 在实际应用中，可以考虑使用Builder模式来支持部分更新
        throw new UnsupportedOperationException("当前实现不支持参数更新，请创建新的参数对象");
    }
    
    /**
     * 创建默认的光电摄像头参数
     * 
     * @return 默认参数配置
     */
    public static OpticalParameters createDefault() {
        return new OpticalParameters(
                1920, 1080,  // 1080p分辨率
                50.0,        // 50mm焦距
                800,         // ISO 800
                30.0,        // 30fps
                10,          // 最小目标10像素
                500,         // 最大目标500像素
                0.7,         // 70%置信度阈值
                false,       // 不启用夜视
                1.0          // 1倍光学变焦
        );
    }
    
    /**
     * 创建高精度配置的光电摄像头参数
     * 
     * @return 高精度参数配置
     */
    public static OpticalParameters createHighPrecision() {
        return new OpticalParameters(
                3840, 2160,  // 4K分辨率
                85.0,        // 85mm焦距
                400,         // ISO 400
                60.0,        // 60fps
                5,           // 最小目标5像素
                1000,        // 最大目标1000像素
                0.9,         // 90%置信度阈值
                true,        // 启用夜视
                3.0          // 3倍光学变焦
        );
    }
    
    @Override
    public String toString() {
        return getParametersDescription();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        OpticalParameters that = (OpticalParameters) o;
        
        return resolutionWidth == that.resolutionWidth &&
               resolutionHeight == that.resolutionHeight &&
               Double.compare(that.focalLength, focalLength) == 0 &&
               sensitivity == that.sensitivity &&
               Double.compare(that.frameRate, frameRate) == 0 &&
               minTargetSize == that.minTargetSize &&
               maxTargetSize == that.maxTargetSize &&
               Double.compare(that.confidenceThreshold, confidenceThreshold) == 0 &&
               nightVisionEnabled == that.nightVisionEnabled &&
               Double.compare(that.zoomFactor, zoomFactor) == 0;
    }
    
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = resolutionWidth;
        result = 31 * result + resolutionHeight;
        temp = Double.doubleToLongBits(focalLength);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + sensitivity;
        temp = Double.doubleToLongBits(frameRate);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + minTargetSize;
        result = 31 * result + maxTargetSize;
        temp = Double.doubleToLongBits(confidenceThreshold);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (nightVisionEnabled ? 1 : 0);
        temp = Double.doubleToLongBits(zoomFactor);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
} 