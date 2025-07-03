package com.JP.dronesim.domain.uav.model;

import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.common.valueobjects.Orientation;
import com.JP.dronesim.domain.common.valueobjects.Velocity;
import com.JP.dronesim.domain.common.valueobjects.Acceleration;
import java.util.Objects;

/**
 * 无人机状态值对象
 * 包含无人机的位置、姿态、速度、加速度和物理探测特性
 * 
 * @author JP Team
 * @version 1.0
 */
public class UAVState {
    
    /**
     * 位置
     */
    private final Position position;
    
    /**
     * 姿态
     */
    private final Orientation orientation;
    
    /**
     * 速度
     */
    private final Velocity velocity;
    
    /**
     * 加速度
     */
    private final Acceleration acceleration;
    
    /**
     * 雷达截面积（RCS - Radar Cross Section，平方米）
     * 用于电磁波雷达探测计算
     */
    private final double radarCrossSection;
    
    /**
     * 红外辐射强度（瓦特）
     * 用于红外探测计算
     */
    private final double infraredSignature;
    
    /**
     * 光学可见度系数
     * 范围：0.0 - 1.0，值越大越容易被光电设备发现
     */
    private final double opticalVisibility;
    
    /**
     * 构造函数 - 包含运动状态和物理特性
     * 
     * @param position 位置
     * @param orientation 姿态
     * @param velocity 速度
     * @param acceleration 加速度
     * @param radarCrossSection 雷达截面积
     * @param infraredSignature 红外辐射强度
     * @param opticalVisibility 光学可见度系数
     */
    public UAVState(Position position, Orientation orientation, 
                   Velocity velocity, Acceleration acceleration,
                   double radarCrossSection, double infraredSignature,
                   double opticalVisibility) {
        // 运动状态验证
        if (position == null) {
            throw new IllegalArgumentException("位置不能为空");
        }
        if (orientation == null) {
            throw new IllegalArgumentException("姿态不能为空");
        }
        if (velocity == null) {
            throw new IllegalArgumentException("速度不能为空");
        }
        if (acceleration == null) {
            throw new IllegalArgumentException("加速度不能为空");
        }
        
        // 物理特性验证
        if (radarCrossSection < 0) {
            throw new IllegalArgumentException("雷达截面积不能为负数");
        }
        if (infraredSignature < 0) {
            throw new IllegalArgumentException("红外辐射强度不能为负数");
        }
        if (opticalVisibility < 0.0 || opticalVisibility > 1.0) {
            throw new IllegalArgumentException("光学可见度系数必须在0.0-1.0之间");
        }
        
        this.position = position;
        this.orientation = orientation;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.radarCrossSection = radarCrossSection;
        this.infraredSignature = infraredSignature;
        this.opticalVisibility = opticalVisibility;
    }
    
    /**
     * 简化构造函数 - 只包含运动状态，使用默认物理特性
     * 
     * @param position 位置
     * @param orientation 姿态
     * @param velocity 速度
     * @param acceleration 加速度
     */
    public UAVState(Position position, Orientation orientation, 
                   Velocity velocity, Acceleration acceleration) {
        this(position, orientation, velocity, acceleration, 
             0.01, 50.0, 0.7); // 使用标准小型无人机的默认物理特性
    }
    
    /**
     * 获取位置
     * 
     * @return 位置
     */
    public Position getPosition() {
        return position;
    }
    
    /**
     * 获取姿态
     * 
     * @return 姿态
     */
    public Orientation getOrientation() {
        return orientation;
    }
    
    /**
     * 获取速度
     * 
     * @return 速度
     */
    public Velocity getVelocity() {
        return velocity;
    }
    
    /**
     * 获取加速度
     * 
     * @return 加速度
     */
    public Acceleration getAcceleration() {
        return acceleration;
    }
    
    /**
     * 获取雷达截面积
     * 
     * @return 雷达截面积
     */
    public double getRadarCrossSection() {
        return radarCrossSection;
    }
    
    /**
     * 获取红外辐射强度
     * 
     * @return 红外辐射强度
     */
    public double getInfraredSignature() {
        return infraredSignature;
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
     * 创建静止状态
     * 
     * @param position 位置
     * @return 静止状态
     */
    public static UAVState createStationary(Position position) {
        return new UAVState(
            position,
            Orientation.identity(),
            Velocity.zero(),
            Acceleration.zero()
        );
    }
    
    /**
     * 创建悬停状态
     * 
     * @param position 悬停位置
     * @param orientation 悬停姿态
     * @return 悬停状态
     */
    public static UAVState createHovering(Position position, Orientation orientation) {
        return new UAVState(
            position,
            orientation,
            Velocity.zero(),
            Acceleration.zero()
        );
    }
    
    /**
     * 创建飞行状态
     * 
     * @param position 当前位置
     * @param orientation 当前姿态
     * @param velocity 当前速度
     * @return 飞行状态
     */
    public static UAVState createFlying(Position position, Orientation orientation, Velocity velocity) {
        return new UAVState(
            position,
            orientation,
            velocity,
            Acceleration.zero()
        );
    }
    
    /**
     * 创建标准小型无人机状态
     * 
     * @param position 位置
     * @param orientation 姿态
     * @param velocity 速度
     * @param acceleration 加速度
     * @return 标准小型无人机状态
     */
    public static UAVState createStandardSmallDrone(Position position, Orientation orientation,
                                                   Velocity velocity, Acceleration acceleration) {
        return new UAVState(
            position, orientation, velocity, acceleration,
            0.01,    // RCS: 0.01 m²
            50.0,    // 红外辐射: 50瓦
            0.7      // 光学可见度: 0.7
        );
    }
    
    /**
     * 创建隐身无人机状态
     * 
     * @param position 位置
     * @param orientation 姿态
     * @param velocity 速度
     * @param acceleration 加速度
     * @return 隐身无人机状态
     */
    public static UAVState createStealthDrone(Position position, Orientation orientation,
                                             Velocity velocity, Acceleration acceleration) {
        return new UAVState(
            position, orientation, velocity, acceleration,
            0.001,   // RCS: 0.001 m² (较低)
            20.0,    // 红外辐射: 20瓦 (较低)
            0.3      // 光学可见度: 0.3 (较低)
        );
    }
    
    /**
     * 更新运动状态（保持物理特性不变）
     * 
     * @param newPosition 新位置
     * @param newOrientation 新姿态
     * @param newVelocity 新速度
     * @param newAcceleration 新加速度
     * @return 更新后的状态
     */
    public UAVState updateMotionState(Position newPosition, Orientation newOrientation,
                                     Velocity newVelocity, Acceleration newAcceleration) {
        return new UAVState(
            newPosition, newOrientation, newVelocity, newAcceleration,
            this.radarCrossSection, this.infraredSignature, this.opticalVisibility
        );
    }
    
    /**
     * 检查是否为静止状态
     * 
     * @return 是否静止
     */
    public boolean isStationary() {
        return velocity.isStationary() && acceleration.isZero();
    }
    
    /**
     * 检查是否在移动
     * 
     * @return 是否在移动
     */
    public boolean isMoving() {
        return !velocity.isStationary();
    }
    
    /**
     * 检查是否在加速
     * 
     * @return 是否在加速
     */
    public boolean isAccelerating() {
        return !acceleration.isZero();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UAVState uavState = (UAVState) obj;
        return Double.compare(uavState.radarCrossSection, radarCrossSection) == 0 &&
               Double.compare(uavState.infraredSignature, infraredSignature) == 0 &&
               Double.compare(uavState.opticalVisibility, opticalVisibility) == 0 &&
               Objects.equals(position, uavState.position) &&
               Objects.equals(orientation, uavState.orientation) &&
               Objects.equals(velocity, uavState.velocity) &&
               Objects.equals(acceleration, uavState.acceleration);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(position, orientation, velocity, acceleration,
                          radarCrossSection, infraredSignature, opticalVisibility);
    }
    
    @Override
    public String toString() {
        return String.format("UAVState{position=%s, orientation=%s, velocity=%s, acceleration=%s, " +
                           "RCS=%.4f, IR=%.1f, Optical=%.2f}",
                           position, orientation, velocity, acceleration,
                           radarCrossSection, infraredSignature, opticalVisibility);
    }
} 