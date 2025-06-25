package com.JP.dronesim.domain.device.model;

import com.JP.dronesim.application.dtos.request.DeviceInitParamsDTO;
import com.JP.dronesim.domain.common.enums.DeviceStatus;
import com.JP.dronesim.domain.common.enums.DeviceType;
import com.JP.dronesim.domain.common.valueobjects.Orientation;
import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.device.model.common.DetectionLog;
import com.JP.dronesim.domain.device.model.common.SensorParameters;
import com.JP.dronesim.domain.device.model.common.events.DetectionEvent;
import com.JP.dronesim.domain.uav.model.AirspaceEnvironment;
import com.JP.dronesim.domain.uav.model.UAV;

import java.util.List;

/**
 * 探测设备基类接口
 * 定义所有探测设备的共同属性和行为
 * 适用于雷达、光电摄像头、无线电探测器、GPS诱导器等设备
 * 
 * @author JP
 * @version 1.0
 */
public interface ProbeDevice {
    
    // ================ 基础属性访问方法 ================
    
    /**
     * 获取设备唯一标识符
     * 
     * @return 设备ID
     */
    String getId();
    
    /**
     * 获取设备名称
     * 
     * @return 设备名称
     */
    String getName();
    
    /**
     * 获取设备类型
     * 
     * @return 设备类型枚举
     */
    DeviceType getType();
    
    /**
     * 获取设备位置坐标
     * 
     * @return NED坐标系中的三维位置
     */
    Position getPosition();
    
    /**
     * 设置设备位置坐标
     * 
     * @param position NED坐标系中的三维位置
     */
    void setPosition(Position position);
    
    /**
     * 获取设备朝向
     * 
     * @return 设备朝向角度（0-360度）
     */
    double getOrientation();
    
    /**
     * 设置设备朝向
     * 
     * @param orientation 设备朝向角度（0-360度）
     */
    void setOrientation(double orientation);
    
    /**
     * 获取设备仰角
     * 
     * @return 设备仰角（0-90度）
     */
    double getElevation();
    
    /**
     * 设置设备仰角
     * 
     * @param elevation 设备仰角（0-90度）
     */
    void setElevation(double elevation);
    
    /**
     * 获取设备姿态信息
     * 
     * @return 三维姿态信息
     */
    Orientation getAttitude();
    
    /**
     * 设置设备姿态信息
     * 
     * @param attitude 三维姿态信息
     */
    void setAttitude(Orientation attitude);
    
    /**
     * 获取设备有效探测距离
     * 
     * @return 最大有效探测/影响距离（米）
     */
    double getDetectionRange();
    
    /**
     * 设置设备有效探测距离
     * 
     * @param detectionRange 最大有效探测/影响距离（米）
     */
    void setDetectionRange(double detectionRange);
    
    /**
     * 获取设备视场角
     * 
     * @return 探测设备的视场角度（例如光电摄像头的角度，雷达的波束宽度）
     */
    double getFieldOfView();
    
    /**
     * 设置设备视场角
     * 
     * @param fieldOfView 探测设备的视场角度
     */
    void setFieldOfView(double fieldOfView);
    
    /**
     * 获取设备运行状态
     * 
     * @return 设备状态枚举（active/inactive/error）
     */
    DeviceStatus getStatus();
    
    /**
     * 设置设备运行状态
     * 
     * @param status 设备状态枚举
     */
    void setStatus(DeviceStatus status);
    
    /**
     * 获取探测参数
     * 
     * @return 特定传感器类型的详细参数
     */
    SensorParameters getDetectionParameters();
    
    /**
     * 设置探测参数
     * 
     * @param parameters 特定传感器类型的详细参数
     */
    void setDetectionParameters(SensorParameters parameters);
    
    /**
     * 获取探测日志
     * 
     * @return 探测事件的存储列表
     */
    DetectionLog getDetectionLog();
    
    // ================ 行为方法 ================
    
    /**
     * 初始化探测设备
     * 设置设备的初始位置、姿态和特定类型参数
     * 
     * @param params 包含设备ID、名称、位置、姿态和特定类型参数的初始化参数对象
     * @throws IllegalArgumentException 如果参数无效
     * @throws IllegalStateException 如果设备已经初始化
     */
    void initialize(DeviceInitParamsDTO params);
    
    /**
     * 执行探测扫描/影响逻辑
     * 此方法将与AirSim API和内部探测/影响算法结合，
     * 尝试探测/影响空域中的无人机
     * 
     * @param airspace 当前空域环境对象
     * @param uavs 当前空域中的所有无人机列表
     * @return 成功探测到的无人机事件列表（GPS诱导器可能返回受影响的UAV列表或影响事件）
     * @throws IllegalStateException 如果设备未初始化或处于错误状态
     */
    List<DetectionEvent> performDetection(AirspaceEnvironment airspace, List<UAV> uavs);
    
    /**
     * 运行时动态调整探测设备的参数
     * 
     * @param newParams 包含需要更新的参数及其新值的参数对象
     * @throws IllegalArgumentException 如果新参数无效或类型不匹配
     * @throws IllegalStateException 如果设备不支持参数调整
     */
    void adjustParameters(SensorParameters newParams);
    
    // ================ 状态检查方法 ================
    
    /**
     * 检查设备是否已初始化
     * 
     * @return true表示设备已初始化，false表示未初始化
     */
    boolean isInitialized();
    
    /**
     * 检查设备是否处于活跃状态
     * 
     * @return true表示设备可以执行探测任务，false表示不可以
     */
    boolean isActive();
    
    /**
     * 检查设备是否处于错误状态
     * 
     * @return true表示设备有故障，false表示正常
     */
    boolean hasError();
    
    /**
     * 启用设备
     * 将设备状态设置为活跃状态
     * 
     * @throws IllegalStateException 如果设备未初始化或处于错误状态
     */
    void enable();
    
    /**
     * 禁用设备
     * 将设备状态设置为非活跃状态
     */
    void disable();
    
    /**
     * 重置设备
     * 清除探测日志，重置设备状态
     * 
     * @throws IllegalStateException 如果设备未初始化
     */
    void reset();
    
    // ================ 验证方法 ================
    
    /**
     * 验证设备配置的有效性
     * 检查所有参数是否在合理范围内
     * 
     * @return true表示配置有效，false表示配置无效
     */
    boolean validateConfiguration();
    
    /**
     * 获取设备配置的描述信息
     * 用于日志记录和调试
     * 
     * @return 设备配置的字符串描述
     */
    String getConfigurationDescription();
}
