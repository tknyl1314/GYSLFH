package com.otitan.util;

public class CoordinateConvert {

	double radius_a = 6399698.9018;
	double radius_b = 6367558.49686;
	double radian = 180 / Math.PI;
	int zoneWidth = 6;
	int zoneID = 0;
	double centerLongitude = 0;
	double longitude = 0;
	double latitude = 0;
	double x = 0;
	double y = 0;
	double gravity = 0;

	public void coordinateConvert() {
	}

	public void setZoneWidth(int zoneWidth_1) {
		zoneWidth = zoneWidth_1;
	}

	public int getzoneWidth() {
		return zoneWidth;
	}

	public void setZoneID(int zoneID_1) {
		zoneID = zoneID_1;
		centerLongitude = zoneID * zoneWidth - 3;
	}

	public int getZoneID() {
		return zoneID;
	}

	public void setCenterLongitude(double centerLongitude_1) {
		centerLongitude = centerLongitude_1;
	}

	public double getCenterLongitude() {
		return centerLongitude;
	}

	public void setLongitude(double longitude_1) {
		longitude = longitude_1;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLatitude(double latitude_1) {
		latitude = latitude_1;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setX(double x_1) {
		x = x_1;
	}

	public double getX() {
		return x;
	}

	public void setY(double y_1) {
		y = y_1;
	}

	public double getY() {
		return y;
	}

	public void setGravity(double gravity_1) {
		gravity = gravity_1;
	}

	public double getGravity() {
		return gravity;
	}

	public void gaussToUtm() {
		x = x * 0.9996;
		y = (y - 500000) * 0.9996 + 500000;
	}

	public void utmToGauss() {
		x = x / 0.9996;
		y = (y - 500000) / 0.9996 + 500000;
	}

	public void normal() {
		double y0 = 0;
		if (zoneWidth == 6)
			y0 = ((centerLongitude + 3) / 6) * 1000000 + 500000;
		if (zoneWidth == 3)
			y0 = (centerLongitude / 3) * 1000000 + 500000;
		if (zoneWidth == 1)
			y0 = 0;
		double a = y0 / 100000;
		double offset = longitude - centerLongitude;
		double w = offset / radian;
		double bf = latitude / radian;
		double t = Math.tan(bf);
		double c = Math.cos(bf);
		double e = 0.006738525415 * c * c;
		double h = t * t;
		double q = 1 + e;
		double n = (radius_a / Math.sqrt(q));
		double k1 = 32005.78006;
		double k2 = 133.92133;
		double k3 = 0.7031;
		w = w * c;
		double m = w * w;
		double u = t * c;
		double v = u * u;
		double k4 = k1 + v * (k2 + v * k3);
		x = radius_b
				* latitude
				/ radian
				- u
				* c
				* k4
				+ ((((h - 58) * h + 61) * m / 30 + (4 * e + 5) * q - h) * m
						/ 12 + 1) * n * t * m / 2;
		y = ((((h - 18) * h - (58 * h - 14) * e + 5) * m / 20 + q - h) * m / 6 + 1)
				* n * w + y0;
		gravity = (t * w * (1 + m
				* ((q + e) * q / 3 + m * u * w * (2 - h) / (15 * c))))
				* radian;
	}

	public void reverse() {
		double y0 = 0;
		if (zoneWidth == 6)
			y0 = ((centerLongitude + 3) / 6) * 1000000 + 500000;
		if (zoneWidth == 3)
			y0 = (centerLongitude / 3) * 1000000 + 500000;
		if (zoneWidth == 1)
			y0 = 0;
		double a = y0 / 100000;
		double x1 = x;
		double y1 = y;
		y1 = y1 - a * 100000;
		double bf = x1 / radius_b;
		double u = Math.sin(bf);
		double v = u * u;
		bf = bf + u * Math.cos(bf)
				* (0.005051773759 - v * (0.00002983718 - v * 0.000000238209));
		double t = Math.tan(bf);
		double c = Math.cos(bf);
		double e = 0.006738525415 * c * c;
		double h = t * t;
		double q = 1 + e;
		double n = radius_a / Math.sqrt(q);
		n = y1 / n;
		double m = n * n;
		latitude = bf
				- ((((45 * h + 90) * h + 61) * m / 30 - (3 - 9 * e) * h - 5 - e)
						* m / 12 + 1) * m * t * q / 2;
		latitude = latitude * radian;
		longitude = ((((24 * h + 28) * h + (8 * h + 6) * e + 5) * m / 20 - 2
				* h - q)
				* m / 6 + 1)
				* n / c;
		longitude = longitude * radian + centerLongitude;
	}


}
