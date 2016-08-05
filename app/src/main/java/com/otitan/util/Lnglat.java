package com.otitan.util;

import com.esri.core.geometry.Point;

public class Lnglat {

	private double lng;// 经度
	private double lat;// 纬度

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public Lnglat(double lng, double lat) {
		this.lng = lng;
		this.lat = lat;
	}

	public String toString() {
		return "lng(" + this.lng + "),lat(" + this.lat + ")";
	}

	public static Lnglat transform(Point point,int m) {
		double n = Math.pow(2.0D, m);//系数
		double lng = point.getX() / n * 360.0D - 180.0D;//经度
		double lat = Math.atan(Math.sinh(3.141592653589793D * (1.0D - 2 * point.getY() / n)));//纬度
		lat = lat * 180.0D / 3.141592653589793D;
		return new Lnglat(lng, lat);
	}

}
