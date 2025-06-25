package com.JP.dronesim.domain.uav.model;

import java.util.Objects;

/**
 * 无人机物理特性值对象
 * 描述无人机的物理材质特征、尺寸和电磁特性
 * 
 * @author JP Team
 * @version 1.0
 */
public class PhysicalProperties {
    
    /**
     * 无人机长度（米）
     */
    private final double length;
    
    /**
     * 无人机宽度（米）
     */
    private final double width;
    
    /**
     * 无人机高度（米）
     */
    private final double height;
    
    /**
     * 无人机重量（千克）
     */
    private final double weight;
    
    /**
     * 雷达截面积（RCS - Radar Cross Section，平方米）
     * 用于电磁波雷达探测计算
     */
    private final double radarCrossSection;
    
    /**
     * 材质类型
     * 影响电磁波反射特性
     */
    private final String materialType;
    
    /**
     * 电磁波反射系数
     * 范围：0.0 - 1.0，值越大反射越强
     */
    private final double electromagneticReflectivity;
    
    /**
     * 红外辐射强度（瓦特）
     * 用于红外探测计算
     */
    private final double infraredSignature;
    
    /**
     * 无线电信号发射功率（dBm）
     * 无人机自身发射的无线电信号强度
     */
    private final double radioEmissionPower;
    
    /**
     * 光学可见度系数
     * 范围：0.0 - 1.0，值越大越容易被光电设备发现
     */
    private final double opticalVisibility;
    
    /**
     * 构造函数
     * 
     * @param length 长度
     * @param width 宽度
     * @param height 高度
     * @param weight 重量
     * @param radarCrossSection 雷达截面积
     * @param materialType 材质类型
     * @param electromagneticReflectivity 电磁波反射系数
     * @param infraredSignature 红外辐射强度
     * @param radioEmissionPower 无线电发射功率
     * @param opticalVisibility 光学可见度系数
     */
    public PhysicalProperties(double length, double width, double height, double weight,
                            double radarCrossSection, String materialType, 
                            double electromagneticReflectivity, double infraredSignature,
                            double radioEmissionPower, double opticalVisibility) {
        
        // 参数验证
        if (length <= 0 || width <= 0 || height <= 0) {
            throw new IllegalArgumentException("无人机尺寸必须大于零");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("无人机重量必须大于零");
        }
        if (radarCrossSection < 0) {
            throw new IllegalArgumentException("雷达截面积不能为负数");
        }
        if (materialType == null || materialType.trim().isEmpty()) {
            throw new IllegalArgumentException("材质类型不能为空");
        }
        if (electromagneticReflectivity < 0.0 || electromagneticReflectivity > 1.0) {
            throw new IllegalArgumentException("电磁波反射系数必须在0.0-1.0之间");
        }
        if (infraredSignature < 0) {
            throw new IllegalArgumentException("红外辐射强度不能为负数");
        }
        if (opticalVisibility < 0.0 || opticalVisibility > 1.0) {
            throw new IllegalArgumentException("光学可见度系数必须在0.0-1.0之间");
        }
        
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.radarCrossSection = radarCrossSection;
        this.materialType = materialType.trim();
        this.electromagneticReflectivity = electromagneticReflectivity;
        this.infraredSignature = infraredSignature;
        this.radioEmissionPower = radioEmissionPower;
        this.opticalVisibility = opticalVisibility;
    }
    
    /**
     * 获取长度
     * 
     * @return 长度（米）
     */
    public double getLength() {
        return length;
    }
    
    /**
     * 获取宽度
     * 
     * @return 宽度（米）
     */
    public double getWidth() {
        return width;
    }
    
    /**
     * 获取高度
     * 
     * @return 高度（米）
     */
    public double getHeight() {
        return height;
    }
    
    /**
     * 获取重量
     * 
     * @return 重量（千克）
     */
    public double getWeight() {
        return weight;
    }
    
    /**
     * 获取雷达截面积
     * 
     * @return 雷达截面积（平方米）
     */
    public double getRadarCrossSection() {
        return radarCrossSection;
    }
    
    /**
     * 获取材质类型
     * 
     * @return 材质类型
     */
    public String getMaterialType() {
        return materialType;
    }
    
    /**
     * 获取电磁波反射系数
     * 
     * @return 电磁波反射系数
     */
    public double getElectromagneticReflectivity() {
        return electromagneticReflectivity;
    }
    
    /**
     * 获取红外辐射强度
     * 
     * @return 红外辐射强度（瓦特）
     */
    public double getInfraredSignature() {
        return infraredSignature;
    }
    
    /**
     * 获取无线电发射功率
     * 
     * @return 无线电发射功率（dBm）
     */
    public double getRadioEmissionPower() {
        return radioEmissionPower;
    }
    
    /**
     * 获取光学可见度系数
     * 
     * @return 光学可见度系数
     */
    public double getOpticalVisibility() {
        return opticalVisibility;
    }
    
    /**
     * 计算无人机体积
     * 
     * @return 体积（立方米）
     */
    public double getVolume() {
        return length * width * height;
    }
    
    /**
     * 计算无人机表面积（简化为长方体）
     * 
     * @return 表面积（平方米）
     */
    public double getSurfaceArea() {
        return 2 * (length * width + length * height + width * height);
    }
    
    /**
     * 计算密度
     * 
     * @return 密度（千克/立方米）
     */
    public double getDensity() {
        return weight / getVolume();
    }
    
    /**
     * 检查是否为隐身设计
     * 基于雷达截面积和电磁波反射系数判断
     * 
     * @return 是否为隐身设计
     */
    public boolean isStealthDesign() {
        // 隐身设计通常具有较小的RCS和较低的电磁波反射系数
        return radarCrossSection < 0.1 && electromagneticReflectivity < 0.3;
    }
    
    /**
     * 获取探测难度等级
     * 
     * @return 探测难度等级（1-5，5最难探测）
     */
    public int getDetectionDifficultyLevel() {
        double score = 0;
        
        // 基于RCS评分
        if (radarCrossSection < 0.01) score += 2;
        else if (radarCrossSection < 0.1) score += 1.5;
        else if (radarCrossSection < 1.0) score += 1;
        
        // 基于电磁波反射系数评分
        if (electromagneticReflectivity < 0.2) score += 1.5;
        else if (electromagneticReflectivity < 0.5) score += 1;
        
        // 基于光学可见度评分
        if (opticalVisibility < 0.3) score += 1.5;
        else if (opticalVisibility < 0.6) score += 1;
        
        return Math.min(5, Math.max(1, (int) Math.round(score)));
    }
    
    /**
     * 创建标准小型无人机物理特性
     * 
     * @return 标准小型无人机物理特性
     */
    public static PhysicalProperties createStandardSmallDrone() {
        return new PhysicalProperties(
            0.5,    // 长度：0.5米
            0.5,    // 宽度：0.5米
            0.2,    // 高度：0.2米
            1.5,    // 重量：1.5千克
            0.05,   // 雷达截面积：0.05平方米
            "碳纤维", // 材质类型
            0.4,    // 电磁波反射系数
            15.0,   // 红外辐射强度：15瓦特
            -20.0,  // 无线电发射功率：-20dBm
            0.6     // 光学可见度系数
        );
    }
    
    /**
     * 创建军用隐身无人机物理特性
     * 
     * @return 军用隐身无人机物理特性
     */
    public static PhysicalProperties createStealthDrone() {
        return new PhysicalProperties(
            2.0,    // 长度：2.0米
            1.5,    // 宽度：1.5米
            0.5,    // 高度：0.5米
            25.0,   // 重量：25千克
            0.001,  // 雷达截面积：0.001平方米（隐身设计）
            "雷达吸波材料", // 材质类型
            0.1,    // 电磁波反射系数（低反射）
            8.0,    // 红外辐射强度：8瓦特（低红外特征）
            -30.0,  // 无线电发射功率：-30dBm（低功率）
            0.2     // 光学可见度系数（低可见度）
        );
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PhysicalProperties that = (PhysicalProperties) obj;
        return Double.compare(that.length, length) == 0 &&
               Double.compare(that.width, width) == 0 &&
               Double.compare(that.height, height) == 0 &&
               Double.compare(that.weight, weight) == 0 &&
               Double.compare(that.radarCrossSection, radarCrossSection) == 0 &&
               Double.compare(that.electromagneticReflectivity, electromagneticReflectivity) == 0 &&
               Double.compare(that.infraredSignature, infraredSignature) == 0 &&
               Double.compare(that.radioEmissionPower, radioEmissionPower) == 0 &&
               Double.compare(that.opticalVisibility, opticalVisibility) == 0 &&
               Objects.equals(materialType, that.materialType);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(length, width, height, weight, radarCrossSection, 
                          materialType, electromagneticReflectivity, infraredSignature,
                          radioEmissionPower, opticalVisibility);
    }
    
    @Override
    public String toString() {
        return String.format("PhysicalProperties{尺寸=%.2fx%.2fx%.2fm, 重量=%.1fkg, " +
                           "RCS=%.4fm², 材质=%s, 隐身设计=%s, 探测难度=%d级}",
                           length, width, height, weight, radarCrossSection, 
                           materialType, isStealthDesign() ? "是" : "否", 
                           getDetectionDifficultyLevel());
    }
} 