package com.JP.dronesim.domain.device.model.common;

/**
 * 传感器参数通用接口
 * 定义所有探测设备传感器参数的基础接口
 * 不同类型的探测设备可以实现此接口来定义特定的参数结构
 * 
 * @author JP
 * @version 1.0
 */
public interface SensorParameters {
    
    /**
     * 验证参数的有效性
     * 每个具体的传感器参数实现类都应该验证其参数的合法性
     * 
     * @return true表示参数有效，false表示参数无效
     */
    boolean isValid();
    
    /**
     * 获取参数的字符串表示
     * 用于日志记录和调试
     * 
     * @return 参数的字符串描述
     */
    String getParametersDescription();
    
    /**
     * 克隆参数对象
     * 创建当前参数对象的深度拷贝
     * 
     * @return 参数对象的副本
     */
    SensorParameters clone();
    
    /**
     * 将参数更新为指定的新参数
     * 用于运行时动态调整设备参数
     * 
     * @param newParameters 新的参数对象
     * @throws IllegalArgumentException 如果新参数无效或类型不匹配
     */
    void updateFrom(SensorParameters newParameters);
} 