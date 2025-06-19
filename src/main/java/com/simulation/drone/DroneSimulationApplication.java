package com.simulation.drone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DroneSimulationApplication {
    public static void main(String[] args) {
        SpringApplication.run(DroneSimulationApplication.class, args);
    }
} 