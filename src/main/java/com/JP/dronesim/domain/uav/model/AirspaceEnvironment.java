package com.JP.dronesim.domain.uav.model;

import com.JP.dronesim.domain.common.valueobjects.Velocity;

/**
 * 空域环境接口
 * 提供环境信息，如风力等影响无人机飞行的因素
 * 
 * @author JP Team
 * @version 1.0
 */
public interface AirspaceEnvironment {
    
    /**
     * 获取风速向量
     * 
     * @return 风速向量
     */
    Velocity getWindVelocity();
    
    /**
     * 获取温度（摄氏度）
     * 
     * @return 温度
     */
    double getTemperature();
    
    /**
     * 获取湿度百分比
     * 
     * @return 湿度百分比（0-100）
     */
    double getHumidity();
    
    /**
     * 获取大气压力（帕斯卡）
     * 
     * @return 大气压力
     */
    double getAtmosphericPressure();
    
    /**
     * 获取能见度（米）
     * 
     * @return 能见度
     */
    double getVisibility();
} 