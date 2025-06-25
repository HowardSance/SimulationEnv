package com.JP.dronesim.domain.uav.model;

import com.JP.dronesim.domain.common.valueobjects.Position;
import com.JP.dronesim.domain.common.valueobjects.Orientation;
import com.JP.dronesim.domain.common.valueobjects.Velocity;
import com.JP.dronesim.domain.common.valueobjects.Acceleration;
import java.util.Objects;

/**
 * 无人机状态值对象
 * 包含无人机的位置、姿态、速度、加速度等状态信息
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
     * 构造函数
     * 
     * @param position 位置
     * @param orientation 姿态
     * @param velocity 速度
     * @param acceleration 加速度
     */
    public UAVState(Position position, Orientation orientation, 
                   Velocity velocity, Acceleration acceleration) {
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
        
        this.position = position;
        this.orientation = orientation;
        this.velocity = velocity;
        this.acceleration = acceleration;
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
        return Objects.equals(position, uavState.position) &&
               Objects.equals(orientation, uavState.orientation) &&
               Objects.equals(velocity, uavState.velocity) &&
               Objects.equals(acceleration, uavState.acceleration);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(position, orientation, velocity, acceleration);
    }
    
    @Override
    public String toString() {
        return String.format("UAVState{position=%s, orientation=%s, velocity=%s, acceleration=%s}",
                           position, orientation, velocity, acceleration);
    }
} 