package com.kbopark.operation.util;

/**
 * @Author:sunwenzhi
 * @Description:
 * @Date: 2020/9/8 11:29
 **/
public class GpsUtil {


	/**
	 * 单位：km
	 **/
	public static final int distance = 5;
	/**
	 * 单位：km
	 **/
	public static final double EARTH_RADIUS = 6371.137;


	public static PositionQ findPosition(double longitude, double latitude) {
		return findPosition(longitude, latitude, distance);
	}

	/**
	 * 根据定位经纬度查询附近几公里内得范围条件
	 *
	 * @param longitude 经度
	 * @param latitude  纬度
	 * @param dis       距离，单位：km
	 */
	public static PositionQ findPosition(double longitude, double latitude, double dis) {
		//先计算查询点的经纬度范围
		//地球半径千米
		double r = EARTH_RADIUS;
		double dLng = 2 * Math.asin(Math.sin(dis / (2 * r)) / Math.cos(latitude * Math.PI / 180));
		//角度转为弧度
		dLng = dLng * 180 / Math.PI;
		double dLat = dis / r;
		dLat = dLat * 180 / Math.PI;
		double minLng = longitude - dLng;
		double maxLng = longitude + dLng;
		double minLat = latitude - dLat;
		double maxLat = latitude + dLat;
		return new PositionQ(minLng, maxLng, minLat, maxLat);
	}


	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}


	/**
	 * 经纬度之间的距离
	 *
	 * @param lat1 纬度1
	 * @param lng1 经度1
	 * @param lat2 纬度2
	 * @param lng2 经度2
	 * @return 两点间直线距离，单位：m
	 */
	public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.abs(
				Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2))));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 1000);
		return s;
	}

}
