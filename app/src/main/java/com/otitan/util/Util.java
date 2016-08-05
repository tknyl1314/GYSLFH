package com.otitan.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

	private static Util util;

	private Context context;

	public synchronized static Util getInstance(Context context)
			throws Exception {
		if (util == null) {
			util = new Util(context);
		}
		return util;
	}

	public static void resetUtil() {
		util = null;
	}

	private Util(Context context) throws Exception {
		this.context = context;
	}

	// �ж��Ƿ�����������
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	/**
	 * 检测 obj 是否为 null,是返回false 不是返回 true
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean IsEmpty(Object obj) {
		if (obj != null) {
			return true;
		}
		return false;
	}
	
	/* 打开或关闭GPS 当前是开启的，那么就会关闭它，反之亦然 */
	public static void toggleGPS(Context context) {
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, gpsIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}

	// �ж�WIFI�����Ƿ����
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	// �ж�MOBILE�����Ƿ����
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 */
	public static void isExist(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
	}



	/**
	 */
	public static final boolean isOPen(final Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean network = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps || network) {
			return true;
		}
		return false;
	}


	/**
	 */
	public static boolean isIntersect(Point point, Point lineStartPoint,
			Point lineEndPoint) {
		boolean result1 = false;
		boolean result2 = false;
		double X21, Y21, X10, Y10;
		X21 = lineEndPoint.getX() - lineStartPoint.getX();
		Y21 = lineEndPoint.getY() - lineStartPoint.getY();
		X10 = point.getX() - lineStartPoint.getX();
		Y10 = point.getY() - lineStartPoint.getY();
		double mm = X21 * Y10 - X10 * Y21;
		DecimalFormat df = new DecimalFormat("0.0000000");
		if (df.format(Math.abs(mm)).equals("0.0000000")) {
			result1 = true;
		}
		double xMin = Math.min(lineStartPoint.getX(), lineEndPoint.getX());
		double xMax = Math.max(lineEndPoint.getX(), lineStartPoint.getX());
		double yMin = Math.min(lineStartPoint.getY(), lineEndPoint.getY());
		double yMax = Math.max(lineEndPoint.getY(), lineStartPoint.getY());
		// �жϵ��Ƿ����ڸ��ߵ��ӳ�����
		if (xMin <= point.getX() && point.getX() <= xMax
				&& yMin <= point.getY() && point.getY() <= yMax) {
			// �����ӳ����Ϸ���true
			result2 = true;
		} else {
			// �����ӳ����Ϸ���false
			result2 = false;
		}
		return result1 && result2;
	}

	/**
	 * ��������Ƿ������ĳ��
	 * 
	 * @param point
	 * @return
	 */
	public static boolean containsPoint(Point point, Point[] vertices) {
		int verticesCount = vertices.length;
		int nCross = 0;
		for (int i = 0; i < verticesCount; ++i) {
			Point p1 = vertices[i];
			Point p2 = vertices[(i + 1) % verticesCount];

			// ��� y=p.y �� p1 p2 �Ľ���
			if (p1.getY() == p2.getY()) { // p1p2 �� y=p0.yƽ��
				continue;
			}
			if (point.getY() < Math.min(p1.getY(), p2.getY())) { // ������p1p2�ӳ�����
				continue;
			}
			if (point.getY() >= Math.max(p1.getY(), p2.getY())) { // ������p1p2�ӳ�����
				continue;
			}
			// �󽻵�� X ����
			double x = (point.getY() - p1.getY()) * (p2.getX() - p1.getX())
					/ (p2.getY() - p1.getY()) + p1.getX();
			if (x > point.getX()) { // ֻͳ�Ƶ��߽���
				nCross++;
			}
		}
		// ���߽���Ϊż�������ڶ����֮��
		return (nCross % 2 == 1);
	}

	/**
	 * ��������Ƿ��Ǻ���
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isHanzi(String name, Activity context) {
		String regEx = "[\u4e00-\u9fa5]";
		// String regEx = "^[\u4e00-\u9fa5]{2,4}";
		Pattern p = Pattern.compile(regEx);
		int num = 0;// ���ֳ���
		for (int i = 0; i < name.length(); i++) {
			if (p.matches(regEx, name.substring(i, i + 1))) {
				num++;
			} else {
				ToastUtil.setToast(context, "�û���2��4λ����");
				return false;
			}
		}
		if (num < 2 || num > 4) {
			ToastUtil.setToast(context, "�û���2��4λ����");
			return false;
		} else {
			return true;
		}
	}

	// ����Ƿ�������
	public static boolean checkFomatNumber(String number) { // *Regexpƥ��ģʽ
		String postCodeRegexp = "([0-9]{3})+.([0-9]{4})+"; // ���������ƥ��ģʽ
		String phoneRegexp = "([0-9]{2})+-([0-9]{4})+-([0-9]{4})+";// �̻���ƥ��ģʽ
		String mobileRegexp = "([0-9]{3})+-([0-9]{4})+-([0-9]{4})+"; // �ֻ���ƥ��ģʽ
		String regExp = "^[1]([3-9][0-9]{1}|59|58|88|89)[0-9]{8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(number);
		return m.find();
	}

	// / <summary>
	// / �ж�ƽ�����������Ƿ��ཻ�����ؽ���
	// / </summary>
	// / <param name="a">�߶�1�������</param>
	// / <param name="b">�߶�1�յ�����</param>
	// / <param name="c">�߶�2�������</param>
	// / <param name="d">�߶�2�յ�����</param>
	// / <param name="intersection">�ཻ������</param>
	// / <returns>�Ƿ��ཻ 0:����ƽ�� -1:��ƽ����δ�ཻ 1:�����ཻ</returns>
	public static Point getIntersection(Point a, Point b, Point c, Point d) {
		Point intersection = new Point();
		intersection
				.setX(((b.getX() - a.getX()) * (c.getX() - d.getX())
						* (c.getY() - a.getY()) - c.getX()
						* (b.getX() - a.getX()) * (c.getY() - d.getY()) + a
						.getX() * (b.getY() - a.getY()) * (c.getX() - d.getX()))
						/ ((b.getY() - a.getY()) * (c.getX() - d.getX()) - (b
								.getX() - a.getX()) * (c.getY() - d.getY())));
		intersection
				.setY(((b.getY() - a.getY()) * (c.getY() - d.getY())
						* (c.getX() - a.getX()) - c.getY()
						* (b.getY() - a.getY()) * (c.getX() - d.getX()) + a
						.getY() * (b.getX() - a.getX()) * (c.getY() - d.getY()))
						/ ((b.getX() - a.getX()) * (c.getY() - d.getY()) - (b
								.getY() - a.getY()) * (c.getX() - d.getX())));
		return intersection;
	}

	// �ж�polyline�Ƿ����ཻ
	public static List<Geometry> getGeometry(Polyline pl, MapView mapView) {

		List<Geometry> list = new ArrayList<Geometry>();
		int m = pl.getPointCount();
		for (int i = 0; i < m; i++) {
			Polyline pl_s = new Polyline();
			pl_s.startPath(pl.getPoint(i));
			if (m > i + 1) {
				pl_s.lineTo(pl.getPoint(i + 1));
			}
			if (m > i + 2) {
				for (int j = i + 2; j < m; j++) {
					if (j < m - 1) {
						Polyline pl_e = new Polyline();
						pl_e.startPath(pl.getPoint(j));
						pl_e.lineTo(pl.getPoint(j + 1));
						boolean result = GeometryEngine.intersects(pl_s, pl_e,
								mapView.getSpatialReference());
						if (result) {
							Point point = getIntersection(pl.getPoint(i),
									pl.getPoint(i + 1), pl.getPoint(j),
									pl.getPoint(j + 1));
							Polyline polyline = new Polyline();
							polyline.startPath(point);
							for (int k = i + 1; k <= j; k++) {
								polyline.lineTo(pl.getPoint(k));
							}
							polyline.lineTo(point);
							Geometry geometry = GeometryEngine.simplify(
									polyline, mapView.getSpatialReference());
							list.add(geometry);
						}
					}
				}
			}
		}
		return list;
	}

}
