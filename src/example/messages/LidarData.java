package messages;

import org.msgpack.annotation.Message;

import api.AirSimRpcMessageTrait;

/**
 * To verify
 */
@Message
public class LidarData extends AirSimRpcMessageTrait{
	public float point_cloud;
	public long time_stamp;
	public Pose pose;
	public int segmentation;
	
	public LidarData() {
		this.point_cloud = 0f;
		this.time_stamp = 0l;
		this.pose = new Pose();
		this.segmentation = 0;
	}

}