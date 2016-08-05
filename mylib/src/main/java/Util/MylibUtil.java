package Util;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

public class MylibUtil {

	private static MylibUtil util;

	private Context context;

	public synchronized static MylibUtil getInstance(Context context)
			throws Exception {
		if (util == null) {
			util = new MylibUtil(context);
		}
		return util;
	}

	public static void resetUtil() {
		util = null;
	}

	private MylibUtil(Context context) throws Exception {
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
	 * �ж�һ��·���µ��ļ����ļ��У��Ƿ����
	 * 
	 * @param path
	 *            �ļ���·��
	 */
	public static void isExist(String path) {
		File file = new File(path);
		// �ж��ļ����Ƿ����,����������򴴽��ļ���
		if (!file.exists()) {
			file.mkdir();
		}
	}

	/**
	 * ��SD���ϴ���һ���ļ���
	 * 
	 * @param folderName
	 *            �ļ�������
	 */
	public static void createFolder(String path) {
		File file = new File(path);
		if (!file.exists())
			file.mkdirs();// �������� �Զ������ϼ�Ŀ¼
	}

	/**
	 * GPS��λ
	 */
	public void getLocation() {
		// ��ȡλ�ù������
		LocationManager locationManager;
		String serviceName = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) context
				.getSystemService(serviceName);
		// ���ҵ�������Ϣ
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // �߾���
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW); // �͹���

		String provider = locationManager.getBestProvider(criteria, true); // ��ȡGPS��Ϣ
		Location location = locationManager.getLastKnownLocation(provider); // ͨ��GPS��ȡλ��
		updateToNewLocation(location);
		// ���ü��������Զ����µ���Сʱ��Ϊ���N��(1��Ϊ1*1000������д��ҪΪ�˷���)����Сλ�Ʊ仯����N��
		locationManager.requestLocationUpdates(provider, 1 * 1000, 50,
				new LocationListener() {

					@Override
					public void onStatusChanged(String arg0, int arg1,
							Bundle arg2) {

					}

					@Override
					public void onProviderEnabled(String arg0) {

					}

					@Override
					public void onProviderDisabled(String arg0) {

					}

					@Override
					public void onLocationChanged(Location arg0) {

					}
				});
	}

	private void updateToNewLocation(Location location) {
		String str = "";
		if (location != null) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			str = "���ȣ�" + longitude + "\n   γ�ȣ�" + latitude;
		} else {
			str = "�޷���ȡ������Ϣ";
		}
		System.out.println(str);
	}

	/**
	 * ��������֮���ֱ�߾���
	 * 
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return
	 */
	public static Double Distance(double lat1, double lng1, double lat2,
			double lng2) {

		Double R = 6370996.81; // ����İ뾶 6378137.0
		/*
		 * ��ȡ�����x,y��֮��ľ���
		 */
		Double x = (lng2 - lng1) * Math.PI * R
				* Math.cos(((lat1 + lat2) / 2) * Math.PI / 180) / 180;
		Double y = (lat2 - lat1) * Math.PI * R / 180;

		Double distance = Math.hypot(x, y); // �õ�����֮���ֱ�߾���

		return distance;
	}

	/**
	 * �ж�GPS�Ƿ�����GPS����AGPS����һ������Ϊ�ǿ�����
	 * 
	 * @param context
	 * @return true ��ʾ����
	 */
	public static final boolean isOPen(final Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// ͨ��GPS���Ƕ�λ����λ������Ծ�ȷ���֣�ͨ��24�����Ƕ�λ��������Ϳտ��ĵط���λ׼ȷ���ٶȿ죩
		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// ͨ��WLAN���ƶ�����(3G/2G)ȷ����λ�ã�Ҳ����AGPS������GPS��λ����Ҫ���������ڻ��ڸ������Ⱥ��ï�ܵ����ֵȣ��ܼ��ĵط���λ��
		boolean network = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps || network) {
			return true;
		}
		return false;
	}

	// // ��⵱ǰGPS״̬
	// private boolean isGPSEnable() {
	// /*
	// * ��Setting.System����ȡҲ���ԣ�ֻ�����Ǹ��ɵ��÷� String str =
	// * Settings.System.getString(getContentResolver(),
	// * Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	// */
	// String str = Settings.Secure.getString(getContentResolver(),
	// Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	// Log.v("GPS", str);
	// if (str != null) {
	// return str.contains("gps");
	// } else {
	// return false;
	// }
	// }

	/**
	 * �жϵ��Ƿ����߶���
	 * 
	 * @param cPoint
	 *            ��ǰ��
	 * @param p1
	 *            �߶���ʼ��
	 * @param p2
	 *            �߶��յ�
	 * @return ���ΪQ���߶�ΪP1P2���жϵ�Q�ڸ��߶��ϵ������ǣ�( Q- P1 )�� ( P2 - P1 ) = 0 �� Q����
	 *         P1��P2Ϊ�ԽǶ���ľ����ڡ�ǰ�߱�֤Q����ֱ��P1P2�ϣ������Ǳ�֤Q�㲻���߶�P1P2���ӳ��߻����ӳ�����
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
	
	/**
	 * �ж������Ƿ�����
	 */
	public static boolean onNetChange(Context context) {
		boolean IntetnetISVisible = true;
		if (NetUtil.getNetworkState(context) == NetUtil.NETWORN_NONE) {
			IntetnetISVisible = false;
		} else {
			IntetnetISVisible = true;
		}
		return IntetnetISVisible;
	}
	
	/**
	 * ��ת��Ϊ��γ��
	 * @param point
	 */
	public static String pointToStr(Point point){
		String jd_du = (int) Math.floor(point.getX()) + "";
		String jd_fen = (int) Math.floor((point.getX() - Math.floor(point
				.getX())) * 60) + "";
		String jd_miao = (int) Math.floor((((point.getX() - Math
				.floor(point.getX())) * 60) - (int) Math.floor((point
				.getX() - Math.floor(point.getX())) * 60)) * 60)
				+ "";

		String wd_du = (int) Math.floor(point.getY()) + "";
		String wd_fen = (int) Math.floor((point.getY() - Math.floor(point
				.getY())) * 60) + "";
		String wd_miao = (int) Math.floor((((point.getY() - Math
				.floor(point.getY())) * 60) - (int) Math.floor((point
				.getY() - Math.floor(point.getY())) * 60)) * 60)
				+ "";

		String str = " ����: " + jd_du + " �� " + jd_fen + " ��  " + jd_miao
				+ " ��" + "\n  γ��:   " + wd_du + " ��  " + wd_fen + " ��  "
				+ wd_miao + " ��";
		return str;
	}

}
