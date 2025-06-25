package messages;

import org.msgpack.annotation.Message;

import api.AirSimRpcMessageTrait;

@Message
public class CarControls extends AirSimRpcMessageTrait{
	public float throttle;
	public float steering;
	public float brake;
	public boolean handbrake;
	public boolean isManualGear;
	public int manualGear;
	public boolean gearImmediate;
	
	public CarControls() {
		this.throttle=0f;
		this.steering=0f;
		this.brake=0f;
		this.handbrake=false;
		this.isManualGear=false;
		this.manualGear=0;
		this.gearImmediate=true;
	}
}
