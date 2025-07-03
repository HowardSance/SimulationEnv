package com.JP.dronesim.domain.device.model.radar;

import com.JP.dronesim.domain.device.model.common.SensorParameters;

/**
 * 电磁波雷达特有参数值对象
 * 包含工作频率、发射功率、天线增益、噪声系数等雷达专用参数
 * 
 * @author JP
 * @version 1.0
 */
public class RadarParameters implements SensorParameters {
    
    /**
     * 工作频率（Hz）
     */
    private final double frequency;
    
    /**
     * 发射功率（Watts）
     */
    private final double power;
    
    /**
     * 天线增益（dB）
     */
    private final double gain;
    
    /**
     * 接收机噪声系数（dB）
     */
    private final double noiseFigure;
    
    /**
     * 最小信噪比探测阈值（dB）
     * 低于此值无法探测
     */
    private final double minSNRDetect;
    
    /**
     * 扫描模式
     */
    private final ScanPattern scanPattern;
    
    /**
     * 脉冲重复频率（Hz）
     */
    private final double pulseRepetitionFrequency;
    
    /**
     * 脉冲宽度（秒）
     */
    private final double pulseWidth;
    
    /**
     * 天线波束宽度（度）
     */
    private final double beamWidth;
    
    /**
     * 最大不模糊距离（米）
     */
    private final double maxUnambiguousRange;
    
    /**
     * 距离分辨率（米）
     */
    private final double rangeResolution;
    
    /**
     * 速度分辨率（m/s）
     */
    private final double velocityResolution;
    
    /**
     * 构造函数
     * 
     * @param frequency 工作频率
     * @param power 发射功率
     * @param gain 天线增益
     * @param noiseFigure 噪声系数
     * @param minSNRDetect 最小信噪比阈值
     * @param scanPattern 扫描模式
     * @param pulseRepetitionFrequency 脉冲重复频率
     * @param pulseWidth 脉冲宽度
     * @param beamWidth 波束宽度
     * @param maxUnambiguousRange 最大不模糊距离
     * @param rangeResolution 距离分辨率
     * @param velocityResolution 速度分辨率
     */
    public RadarParameters(double frequency, double power, double gain, double noiseFigure,
                          double minSNRDetect, ScanPattern scanPattern, double pulseRepetitionFrequency,
                          double pulseWidth, double beamWidth, double maxUnambiguousRange,
                          double rangeResolution, double velocityResolution) {
        this.frequency = frequency;
        this.power = power;
        this.gain = gain;
        this.noiseFigure = noiseFigure;
        this.minSNRDetect = minSNRDetect;
        this.scanPattern = scanPattern;
        this.pulseRepetitionFrequency = pulseRepetitionFrequency;
        this.pulseWidth = pulseWidth;
        this.beamWidth = beamWidth;
        this.maxUnambiguousRange = maxUnambiguousRange;
        this.rangeResolution = rangeResolution;
        this.velocityResolution = velocityResolution;
    }
    
    /**
     * 获取工作频率
     * 
     * @return 工作频率（Hz）
     */
    public double getFrequency() {
        return frequency;
    }
    
    /**
     * 获取发射功率
     * 
     * @return 发射功率（Watts）
     */
    public double getPower() {
        return power;
    }
    
    /**
     * 获取天线增益
     * 
     * @return 天线增益（dB）
     */
    public double getGain() {
        return gain;
    }
    
    /**
     * 获取噪声系数
     * 
     * @return 接收机噪声系数（dB）
     */
    public double getNoiseFigure() {
        return noiseFigure;
    }
    
    /**
     * 获取最小信噪比探测阈值
     * 
     * @return 最小信噪比阈值（dB）
     */
    public double getMinSNRDetect() {
        return minSNRDetect;
    }
    
    /**
     * 获取扫描模式
     * 
     * @return 扫描模式
     */
    public ScanPattern getScanPattern() {
        return scanPattern;
    }
    
    /**
     * 获取脉冲重复频率
     * 
     * @return 脉冲重复频率（Hz）
     */
    public double getPulseRepetitionFrequency() {
        return pulseRepetitionFrequency;
    }
    
    /**
     * 获取脉冲宽度
     * 
     * @return 脉冲宽度（秒）
     */
    public double getPulseWidth() {
        return pulseWidth;
    }
    
    /**
     * 获取波束宽度
     * 
     * @return 天线波束宽度（度）
     */
    public double getBeamWidth() {
        return beamWidth;
    }
    
    /**
     * 获取最大不模糊距离
     * 
     * @return 最大不模糊距离（米）
     */
    public double getMaxUnambiguousRange() {
        return maxUnambiguousRange;
    }
    
    /**
     * 获取距离分辨率
     * 
     * @return 距离分辨率（米）
     */
    public double getRangeResolution() {
        return rangeResolution;
    }
    
    /**
     * 获取速度分辨率
     * 
     * @return 速度分辨率（m/s）
     */
    public double getVelocityResolution() {
        return velocityResolution;
    }
    
    @Override
    public boolean isValid() {
        return frequency > 0.0 && power > 0.0 &&
               gain >= 0.0 && noiseFigure >= 0.0 &&
               minSNRDetect > 0.0 && scanPattern != null &&
               pulseRepetitionFrequency > 0.0 && pulseWidth > 0.0 &&
               beamWidth > 0.0 && beamWidth <= 360.0 &&
               maxUnambiguousRange > 0.0 &&
               rangeResolution > 0.0 && velocityResolution > 0.0;
    }
    
    @Override
    public String getParametersDescription() {
        return String.format("RadarParameters[freq=%.0fHz, power=%.1fW, gain=%.1fdB, " +
                           "noise=%.1fdB, minSNR=%.1fdB, scan=%s, PRF=%.0fHz, " +
                           "pulseWidth=%.6fs, beamWidth=%.1f°, maxRange=%.0fm, " +
                           "rangeRes=%.1fm, velRes=%.2fm/s]",
                           frequency, power, gain, noiseFigure, minSNRDetect,
                           scanPattern, pulseRepetitionFrequency, pulseWidth,
                           beamWidth, maxUnambiguousRange, rangeResolution, velocityResolution);
    }
    
    @Override
    public SensorParameters clone() {
        return new RadarParameters(frequency, power, gain, noiseFigure, minSNRDetect,
                                 scanPattern, pulseRepetitionFrequency, pulseWidth,
                                 beamWidth, maxUnambiguousRange, rangeResolution, velocityResolution);
    }
    
    @Override
    public void updateFrom(SensorParameters newParameters) {
        if (!(newParameters instanceof RadarParameters)) {
            throw new IllegalArgumentException("参数类型不匹配，期望RadarParameters类型");
        }
        
        RadarParameters newRadarParams = (RadarParameters) newParameters;
        if (!newRadarParams.isValid()) {
            throw new IllegalArgumentException("新参数无效");
        }
        
        // 由于所有字段都是final，需要创建新实例来更新参数
        throw new UnsupportedOperationException("当前实现不支持参数更新，请创建新的参数对象");
    }
    
    @Override
    public String toString() {
        return getParametersDescription();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        RadarParameters that = (RadarParameters) o;
        
        return Double.compare(that.frequency, frequency) == 0 &&
               Double.compare(that.power, power) == 0 &&
               Double.compare(that.gain, gain) == 0 &&
               Double.compare(that.noiseFigure, noiseFigure) == 0 &&
               Double.compare(that.minSNRDetect, minSNRDetect) == 0 &&
               scanPattern == that.scanPattern &&
               Double.compare(that.pulseRepetitionFrequency, pulseRepetitionFrequency) == 0 &&
               Double.compare(that.pulseWidth, pulseWidth) == 0 &&
               Double.compare(that.beamWidth, beamWidth) == 0;
    }
    
    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(frequency);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(power);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(gain);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(minSNRDetect);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (scanPattern != null ? scanPattern.hashCode() : 0);
        return result;
    }
    
    /**
     * 扫描模式枚举
     */
    public enum ScanPattern {
        /**
         * 固定角度扫描
         */
        FIXED("fixed", "固定角度"),
        
        /**
         * 扇形扫描
         */
        SECTOR("sector", "扇形扫描"),
        
        /**
         * 360度圆形扫描
         */
        DEGREE_360("360_degree", "360度扫描");
        
        private final String code;
        private final String description;
        
        ScanPattern(String code, String description) {
            this.code = code;
            this.description = description;
        }
        
        /**
         * 获取扫描模式代码
         * 
         * @return 模式代码
         */
        public String getCode() {
            return code;
        }
        
        /**
         * 获取扫描模式描述
         * 
         * @return 模式描述
         */
        public String getDescription() {
            return description;
        }
        
        /**
         * 根据代码获取扫描模式
         * 
         * @param code 模式代码
         * @return 扫描模式，如果未找到则返回null
         */
        public static ScanPattern fromCode(String code) {
            for (ScanPattern pattern : values()) {
                if (pattern.code.equals(code)) {
                    return pattern;
                }
            }
            return null;
        }
        
        @Override
        public String toString() {
            return description;
        }
    }
} 