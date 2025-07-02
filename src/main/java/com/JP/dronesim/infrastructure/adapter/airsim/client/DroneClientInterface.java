package com.JP.dronesim.infrastructure.adapter.airsim.client;

import com.JP.dronesim.domain.device.model.DroneControls;
import com.JP.dronesim.domain.device.model.DroneState;

public interface DroneClientInterface extends RpcLibClientBase{

	public void setDroneControls(DroneControls controls, String vehicleName);

	public DroneState getDroneState(String vehicleName);

	public void takeOff(String vehicleName);

	public void land(String vehicleName);

	public void hover(String vehicleName);
}