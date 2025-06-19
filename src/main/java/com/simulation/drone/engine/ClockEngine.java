package com.simulation.drone.engine;

/**
 * 仿真时钟引擎接口
 * 负责驱动整个仿真系统的时间推进和状态更新
 */
public interface ClockEngine {
    
    /**
     * 启动时钟引擎
     */
    void start();
    
    /**
     * 暂停时钟引擎
     */
    void pause();
    
    /**
     * 恢复时钟引擎
     */
    void resume();
    
    /**
     * 停止时钟引擎
     */
    void stop();
    
    /**
     * 获取当前仿真时间
     * @return 仿真时间(毫秒)
     */
    long getCurrentTime();
    
    /**
     * 获取时间步长
     * @return 时间步长(毫秒)
     */
    long getTimeStep();
    
    /**
     * 设置时间步长
     * @param timeStep 时间步长(毫秒)
     */
    void setTimeStep(long timeStep);
    
    /**
     * 获取当前FPS
     * @return 帧率
     */
    double getFPS();
    
    /**
     * 获取平均更新时间
     * @return 平均更新时间(毫秒)
     */
    double getAverageUpdateTime();
    
    /**
     * 检查是否正在运行
     * @return 是否运行中
     */
    boolean isRunning();
    
    /**
     * 检查是否已暂停
     * @return 是否已暂停
     */
    boolean isPaused();
    
    /**
     * 注册时钟更新监听器
     * @param listener 监听器
     */
    void registerUpdateListener(ClockUpdateListener listener);
    
    /**
     * 移除时钟更新监听器
     * @param listener 监听器
     */
    void removeUpdateListener(ClockUpdateListener listener);
    
    /**
     * 时钟更新监听器接口
     */
    interface ClockUpdateListener {
        /**
         * 时钟更新回调
         * @param currentTime 当前时间
         * @param deltaTime 时间增量
         */
        void onClockUpdate(long currentTime, long deltaTime);
    }
} 