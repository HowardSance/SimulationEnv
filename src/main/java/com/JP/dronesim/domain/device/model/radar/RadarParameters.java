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
    
    /**
     * 创建默认的雷达参数（X波段雷达）
     * 
     * @return 默认参数配置
     */
    public static RadarParameters createDefault() {
        return new RadarParameters(
                9.375e9,     // 9.375 GHz (X波段)
                1000.0,      // 1kW发射功率
                30.0,        // 30dB天线增益
                3.0,         // 3dB噪声系数
                10.0,        // 10dB最小信噪比
                ScanPattern.SECTOR, // 扇形扫描
                1000.0,      // 1kHz脉冲重复频率
                1e-6,        // 1微秒脉冲宽度
                3.0,         // 3度波束宽度
                150000.0,    // 150km最大距离
                150.0,       // 150m距离分辨率
                1.0          // 1m/s速度分辨率
        );
    }
    
    /**
     * 创建高精度雷达参数
     * 
     * @return 高精度参数配置
     */
    public static RadarParameters createHighPrecision() {
        return new RadarParameters(
                35.0e9,      // 35 GHz (Ka波段)
                2000.0,      // 2kW发射功率
                40.0,        // 40dB天线增益
                2.0,         // 2dB噪声系数
                8.0,         // 8dB最小信噪比
                ScanPattern.FIXED, // 固定扫描
                2000.0,      // 2kHz脉冲重复频率
                0.5e-6,      // 0.5微秒脉冲宽度
                1.0,         // 1度波束宽度
                75000.0,     // 75km最大距离
                75.0,        // 75m距离分辨率
                0.5          // 0.5m/s速度分辨率
        );
    }
    
    /**
     * 创建远程监视雷达参数
     * 
     * @return 远程监视参数配置
     */
    public static RadarParameters createLongRange() {
        return new RadarParameters(
                3.0e9,       // 3 GHz (S波段)
                5000.0,      // 5kW发射功率
                35.0,        // 35dB天线增益
                4.0,         // 4dB噪声系数
                12.0,        // 12dB最小信噪比
                ScanPattern.DEGREE_360, // 360度扫描
                300.0,       // 300Hz脉冲重复频率
                2e-6,        // 2微秒脉冲宽度
                5.0,         // 5度波束宽度
                500000.0,    // 500km最大距离
                300.0,       // 300m距离分辨率
                2.0          // 2m/s速度分辨率
        );
    }
    
    /**
     * 计算理论最大探测距离
     * 基于雷达方程计算
     * 
     * @param targetRCS 目标雷达截面积（m²）
     * @return 理论最大探测距离（米）
     */
    public double calculateMaxDetectionRange(double targetRCS) {
        // 雷达方程: R_max = [(Pt * Gt * Gr * λ² * σ) / ((4π)³ * Pmin)]^(1/4)
        // 简化计算，假设Gt = Gr = gain, λ = c/f
        
        double wavelength = 3e8 / frequency; // 波长
        double minReceivedPower = calculateMinReceivedPower();
        
        double numerator = power * Math.pow(10, gain / 10.0) * Math.pow(10, gain / 10.0) * 
                          wavelength * wavelength * targetRCS;
        double denominator = Math.pow(4 * Math.PI, 3) * minReceivedPower;
        
        return Math.pow(numerator / denominator, 0.25);
    }
    
    /**
     * 计算最小接收功率
     * 
     * @return 最小接收功率（Watts）
     */
    private double calculateMinReceivedPower() {
        // Pmin = kTBF * SNRmin
        double k = 1.38e-23; // 玻尔兹曼常数
        double T = 290.0;    // 标准温度（K）
        double B = 1.0 / pulseWidth; // 带宽近似为脉冲宽度的倒数
        double F = Math.pow(10, noiseFigure / 10.0); // 噪声系数线性值
        double SNRmin = Math.pow(10, minSNRDetect / 10.0); // 最小信噪比线性值
        
        return k * T * B * F * SNRmin;
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