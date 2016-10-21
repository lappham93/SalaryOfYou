package com.mit.utils;

import java.util.List;

public class MathUtils {
	
	public static final double MILE2KM = 1.609;

	public static double round(double value, int decimal) {
		long factor = (long) Math.pow(10, decimal);
		value *= factor;
		long newVal = Math.round(value);
		value = (double)newVal/factor;
		
		return value;
	}
	
	/*
	 * Use Haversine formula:	
	 * 	a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
		c = 2 ⋅ atan2( √a, √(1−a) )
		d = R ⋅ c
		where	φ is latitude, λ is longitude, R is earth’s radius (mean radius = 6,371/1609mile );
		note that angles need to be in radians to pass to trig functions!
	 */
	public static double distance(double lon1, double lat1, double lon2, double lat2) {
		double dis = 0.0;
		double R = 6371000;
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);
		lon1 = Math.toRadians(lon1);
		lon2 = Math.toRadians(lon2);
		double dtLat = lat1 - lat2;
		double dtLon = lon1 - lon2;
		double a = Math.pow(Math.sin(dtLat/2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dtLon/2), 2);
		double c = 2 * Math.asin(Math.pow(a, 0.5));
		dis = R * c;
		
		return dis;		
	}
	
	/*
	 * User globel distance
	 * R * arccos (sin(latA) * sin(latB) + cos(latA) * cos(latB) * cos(lonA-lonB)) 
	 */
	public static double distance2(double lon1, double lat1, double lon2, double lat2) {
		double dis = 0.0;
		double R = 6371000;
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);
		lon1 = Math.toRadians(lon1);
		lon2 = Math.toRadians(lon2);
		double c = Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon1 - lon2);
		dis = R * Math.acos(c);
		
		return dis;
	}
	
	public static double distanceVector(List<Double> vector1, List<Double> vector2) {
		double distance = -1;
		if (vector1 != null && vector2 != null && vector1.size() == vector2.size()) {
			distance = 0.0;
			for (int idx = 0; idx < vector1.size(); idx++) {
				distance += Math.pow(vector1.get(idx) - vector2.get(idx), 2);
			}
			distance = Math.pow(distance, 0.5);
		}
		return distance;
	}
	
	public static void main(String[] args) {
		double dis = distance2(10.8175711, 106.6323632, 10.813678,106.633033);
		System.out.println(dis);
	}
}
