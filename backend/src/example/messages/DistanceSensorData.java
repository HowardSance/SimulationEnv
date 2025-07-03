package messages;

import org.msgpack.annotation.Message;

import api.AirSimRpcMessageTrait;

@Message
public class DistanceSensorData extends AirSimRpcMessageTrait {

	public long time_stamp;
	public float distance;
	public float min_distance;
	public float max_distance;
	public Pose relative_pose;
	
	public DistanceSensorData() {
		this.time_stamp = 0;
		this.distance = 0f;
		this.min_distance = 0f;
		this.max_distance = 0f;
		this.relative_pose = new Pose();
	}
}
