package com.otitan.util;


import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.esri.core.geometry.Point;
import com.otitan.gis.PositionUtil;


public class GpsUtil {
	private Context context;
	private LocationManager locationManager;
	/** 判别是取的gps坐标还是百度坐标 true 为百度坐标 */
	boolean dwflag = false;

	private double longitude, latitude, altitude;
	public static GpsUtil gpsutil;

	/**
	 * 构造函数
	 *
	 * @param context
	 */
	public GpsUtil(Context context) {
		this.context = context;
	}

	public GpsUtil() {
	}

	public static synchronized GpsUtil getInstance(Context context) {
		if (gpsutil == null) {
			gpsutil = new GpsUtil(context);
		}
		return gpsutil;
	}

	/** 获取位置坐标（优先使用gps定位） GPS定位 网络定位 百度定位 */
	public Point getGPSpoint(BDLocation blocation) {
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		/*Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}*/
		Location location=null;
		try {
			location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}catch (SecurityException e){
			e.printStackTrace();
		}

		if (location == null) {
			try{
				location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
			catch (SecurityException e){
				e.printStackTrace();
			}

		}

		if (location != null) {
			latitude = location.getLatitude(); // 纬度 26.567741305680546
			longitude = location.getLongitude(); // 经度 106.68937683886078
			altitude = location.getAltitude();// 1040.8563754250627
			dwflag = false;
		} else {
			Point p=PositionUtil.gcj_To_84(new Point(blocation.getLatitude(),blocation.getLongitude(),blocation.getAltitude()));
			/*latitude = blocation.getLatitude(); // 纬度  26.569726
			longitude = blocation.getLongitude(); // 经度 106.694609
			altitude = blocation.getAltitude();*/

			dwflag = true;
			return p;
		}
		return new Point(longitude,latitude,altitude);
	}



	/**
	 * 判断 设置中的 GPS是否可用
	 * @author 王海啸
	 * @return true:可用 false：不可用
	 */
	public boolean IsGpsProviderEnabled() {
		boolean isOpened = false;
		locationManager = (LocationManager) context.getSystemService(android.content.Context.LOCATION_SERVICE);
		isOpened = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//		if (!isOpened) {
//			Toast.makeText(context, "请启用GPS", Toast.LENGTH_LONG).show();
//		}

		return isOpened;
	}

	/**
	 * 判断GPS模块是否存在或者是开启
	 */
	public void openGPSSettings() {
		LocationManager alm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			Toast.makeText(context, "GPS模块正常", Toast.LENGTH_SHORT).show();
			return;
		}
		Toast.makeText(context, "请开启GPS！", Toast.LENGTH_SHORT).show();
	}


	/**
	 * 如果当前GPS状态为开启状态，代码运行后则关闭；反之，则开启~
	 *
	 */
	public void GPSCheck(Context context) {

		Intent GPSIntent = new Intent();
		GPSIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
		GPSIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}

	public boolean IsNetworkProviderEnabled() {
		boolean isOpened = false;
		LocationManager locationManager = (LocationManager) context
				.getSystemService(android.content.Context.LOCATION_SERVICE);
		isOpened = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if (!isOpened) {
			Toast.makeText(context, "请打开网络服务", Toast.LENGTH_LONG).show();
		}
		return isOpened;
	}
}
