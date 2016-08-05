package com.otitan.entity;

import java.io.Serializable;

public class TrackPoint  implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String time;
    private double latitude;
    private double longitude;
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
