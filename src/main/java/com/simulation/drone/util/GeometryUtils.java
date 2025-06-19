package com.simulation.drone.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

@Component
public class GeometryUtils {
    
    /**
     * 计算两点之间的距离（米）
     */
    public static double calculateDistance(Point p1, Point p2) {
        return p1.distance(p2) * 111000; // 粗略转换为米（1度约等于111公里）
    }
    
    /**
     * 计算方位角（度）
     */
    public static double calculateBearing(Point from, Point to) {
        double lat1 = Math.toRadians(from.getY());
        double lat2 = Math.toRadians(to.getY());
        double lon1 = Math.toRadians(from.getX());
        double lon2 = Math.toRadians(to.getX());
        
        double y = Math.sin(lon2 - lon1) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) -
                  Math.sin(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1);
        
        double bearing = Math.toDegrees(Math.atan2(y, x));
        return (bearing + 360) % 360;
    }
    
    /**
     * 计算俯仰角（度）
     */
    public static double calculateElevation(Point from, Point to, double fromAltitude, double toAltitude) {
        double horizontalDistance = calculateDistance(from, to);
        double verticalDistance = toAltitude - fromAltitude;
        return Math.toDegrees(Math.atan2(verticalDistance, horizontalDistance));
    }
    
    /**
     * 创建3D点
     */
    public static Point createPoint(double longitude, double latitude, double altitude) {
        return new Point(new Coordinate(longitude, latitude, altitude), null, 0);
    }
} 