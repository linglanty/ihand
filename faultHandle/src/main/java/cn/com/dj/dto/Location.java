package cn.com.dj.dto;

public class Location {

	/*
	 * "longitude" : 116.4070129394531200,
        "latitude" : 39.9265861511230470
	 * */
	
	private double longitude;
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	private double latitude;
}
