package com.titan.model;

import java.io.Serializable;

/**
 * 轨迹点
 */
public class TrackPoint implements Serializable {

	/**上传时间*/
	private String time;
	/**经度*/
	private double longitude;
	/**纬度*/
    private double latitude;

    public TrackPoint() {
    }

    public TrackPoint(double latitude, double longitude, String time) {
        this.latitude = latitude;
        this.time = time;
        this.longitude = longitude;
    }
    public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	


    
}
