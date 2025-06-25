package api;

import messages.DistanceSensorData;
import messages.ImuData;
import messages.KinematicsState;
import messages.LidarData;
import messages.Pose;

public interface RpcLibClientBase {

	public void confirmConnection();
	public void reset();
	
	public int getConnectionState();
	public boolean ping();
	public int getClientVersion();
	public int getServerVersion();
	public int getMinRequiredServerVersion();
	public int getMinRequiredClientVersion();
	
	public boolean simIsPaused();
	public void simPause(boolean isPaused);
	public void simContinueForTime(double seconds);
	
	public void simSetTimeOfDay(boolean isEnabeled, String startDatetime, boolean isStartDatetimeDst,
			float celestialClockSpeed, float updateIntervalSecs, boolean moveSun);
	public void simEnableWeather(boolean enable);
	//public void simSetWeatherParameter(WheaterParameter param, float value);
	
	public Pose simGetObjectPose(String objectName);
	public boolean simSetObjectPose(String objectName, Pose pose, boolean teleport);
	
	public void cancelLastTask(String vehicleName);
	
	public boolean armDisarm(boolean arm, String vehicleName);
	public boolean isApiControlEnabled(String vehicleName);
	public void enableApiControl(boolean isEnabled, String vehicleName);
	
	//public GeoPoint getHomeGeoPoint (String vehicleName);
	
	public LidarData getLidarData(String lidarName, String vehicleName);
	
	public Pose simGetVehiclePose(String vehicleName);
	public void simSetVehiclePose(Pose pose, boolean ignoreCollision, String vehicleName);
	
	public KinematicsState simGetGroundTruthKinematics(String vehicleName);
	
	public DistanceSensorData getDistanceSensorData(String distance_sensor_name, String vehicleName);
	
	public ImuData getImuData(String imuName, String vehicleName);
}