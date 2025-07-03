package api;

import messages.CarControls;
import messages.CarState;

public interface CarClientInterface extends RpcLibClientBase{
	
	public void setCarControls(CarControls controls, String vehicleName);
	
	public CarState getCarState(String vehicleName);
}
