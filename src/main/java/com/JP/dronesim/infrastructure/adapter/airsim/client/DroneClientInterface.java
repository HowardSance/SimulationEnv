package com.simulation.drone.infrastructure.adapter.airsim.client;

import com.simulation.drone.domain.device.model.DroneControls;
import com.simulation.drone.domain.device.model.DroneState;

public interface DroneClientInterface extends RpcLibClientBase{
	
	public void setDroneControls(DroneControls controls, String vehicleName);
	
	public DroneState getDroneState(String vehicleName);
	
	public void takeOff(String vehicleName);
	
	public void land(String vehicleName);
	
	public void hover(String vehicleName);
} 