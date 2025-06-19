package com.simulation.drone.adapter.airsim.api;

import com.simulation.drone.adapter.airsim.messages.CarControls;
import com.simulation.drone.adapter.airsim.messages.CarState;

public interface CarClientInterface extends RpcLibClientBase{
	
	public void setCarControls(CarControls controls, String vehicleName);
	
	public CarState getCarState(String vehicleName);
}
