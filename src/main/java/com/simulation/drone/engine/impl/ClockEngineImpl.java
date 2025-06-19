package com.simulation.drone.engine.impl;

import com.simulation.drone.engine.ClockEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 时钟引擎实现
 * 提供高精度的仿真时钟驱动功能
 */
@Slf4j
@Component
public class ClockEngineImpl implements ClockEngine {
    
    @Value("${simulation.clock.time-step:100}")
    private long defaultTimeStep;
    
    @Value("${simulation.clock.max-threads:8}")
    private int maxThreads;
    
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean paused = new AtomicBoolean(false);
    private final AtomicLong timeStep = new AtomicLong(100);
    private final AtomicLong simulationTime = new AtomicLong(0);
    private final AtomicLong lastUpdateTime = new AtomicLong(0);
    
    private ScheduledExecutorService scheduler;
    private final List<ClockUpdateListener> listeners = new CopyOnWriteArrayList<>();
    
    // 性能监控
    private final AtomicLong frameCount = new AtomicLong(0);
    private final AtomicLong totalUpdateTime = new AtomicLong(0);
    private volatile double currentFPS = 0.0;
    private volatile double averageUpdateTime = 0.0;
    
    private ScheduledFuture<?> clockTask;
    private ScheduledFuture<?> metricsTask;
    
    public ClockEngineImpl() {
        this.scheduler = Executors.newScheduledThreadPool(2);
    }
    
    @Override
    public void start() {
        if (running.compareAndSet(false, true)) {
            timeStep.set(defaultTimeStep);
            simulationTime.set(0);
            lastUpdateTime.set(System.currentTimeMillis());
            
            // 启动主时钟任务
            clockTask = scheduler.scheduleAtFixedRate(
                this::clockUpdate, 
                0, 
                timeStep.get(), 
                TimeUnit.MILLISECONDS
            );
            
            // 启动性能监控任务
            metricsTask = scheduler.scheduleAtFixedRate(
                this::updateMetrics,
                1000,
                1000,
                TimeUnit.MILLISECONDS
            );
            
            log.info("时钟引擎启动，时间步长: {}ms", timeStep.get());
        }
    }
    
    @Override
    public void pause() {
        if (running.get() && paused.compareAndSet(false, true)) {
            log.info("时钟引擎暂停");
        }
    }
    
    @Override
    public void resume() {
        if (running.get() && paused.compareAndSet(true, false)) {
            lastUpdateTime.set(System.currentTimeMillis());
            log.info("时钟引擎恢复");
        }
    }
    
    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            paused.set(false);
            
            if (clockTask != null) {
                clockTask.cancel(false);
            }
            if (metricsTask != null) {
                metricsTask.cancel(false);
            }
            
            log.info("时钟引擎停止");
        }
    }
    
    @Override
    public long getCurrentTime() {
        return simulationTime.get();
    }
    
    @Override
    public long getTimeStep() {
        return timeStep.get();
    }
    
    @Override
    public void setTimeStep(long timeStep) {
        if (timeStep < 1 || timeStep > 1000) {
            throw new IllegalArgumentException("时间步长必须在1-1000毫秒之间");
        }
        
        long oldTimeStep = this.timeStep.getAndSet(timeStep);
        
        // 如果正在运行，需要重新调度任务
        if (running.get() && oldTimeStep != timeStep) {
            if (clockTask != null) {
                clockTask.cancel(false);
            }
            
            clockTask = scheduler.scheduleAtFixedRate(
                this::clockUpdate,
                0,
                timeStep,
                TimeUnit.MILLISECONDS
            );
            
            log.info("时间步长更新: {}ms -> {}ms", oldTimeStep, timeStep);
        }
    }
    
    @Override
    public double getFPS() {
        return currentFPS;
    }
    
    @Override
    public double getAverageUpdateTime() {
        return averageUpdateTime;
    }
    
    @Override
    public boolean isRunning() {
        return running.get();
    }
    
    @Override
    public boolean isPaused() {
        return paused.get();
    }
    
    @Override
    public void registerUpdateListener(ClockUpdateListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
            log.debug("注册时钟更新监听器: {}", listener.getClass().getSimpleName());
        }
    }
    
    @Override
    public void removeUpdateListener(ClockUpdateListener listener) {
        if (listeners.remove(listener)) {
            log.debug("移除时钟更新监听器: {}", listener.getClass().getSimpleName());
        }
    }
    
    /**
     * 时钟更新的核心方法
     */
    private void clockUpdate() {
        if (!running.get() || paused.get()) {
            return;
        }
        
        long startTime = System.currentTimeMillis();
        long currentTime = simulationTime.addAndGet(timeStep.get());
        long deltaTime = timeStep.get();
        
        try {
            // 通知所有监听器
            for (ClockUpdateListener listener : listeners) {
                try {
                    listener.onClockUpdate(currentTime, deltaTime);
                } catch (Exception e) {
                    log.error("时钟更新监听器执行失败: {}", listener.getClass().getSimpleName(), e);
                }
            }
            
            frameCount.incrementAndGet();
            
        } catch (Exception e) {
            log.error("时钟更新执行失败", e);
        } finally {
            long updateTime = System.currentTimeMillis() - startTime;
            totalUpdateTime.addAndGet(updateTime);
            lastUpdateTime.set(System.currentTimeMillis());
        }
    }
    
    /**
     * 更新性能指标
     */
    private void updateMetrics() {
        long frames = frameCount.getAndSet(0);
        long totalTime = totalUpdateTime.getAndSet(0);
        
        currentFPS = frames;
        averageUpdateTime = frames > 0 ? (double) totalTime / frames : 0.0;
        
        // 如果FPS过低，记录警告
        if (currentFPS < timeStep.get() * 0.8 / 1000.0) {
            log.warn("时钟引擎性能警告 - FPS: {:.2f}, 平均更新时间: {:.2f}ms", 
                    currentFPS, averageUpdateTime);
        }
    }
    
    @PreDestroy
    public void shutdown() {
        stop();
        if (scheduler != null) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        log.info("时钟引擎已关闭");
    }
} 