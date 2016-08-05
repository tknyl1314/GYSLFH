package com.otitan.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * gps纠偏算法，适用于google,高德体系的地图
 *
 * @author Administrator
 */
public class GpsCorrect {
	final static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
	final static double pi = 3.14159265358979324;
	final static double a = 6378245.0;
	final static double ee = 0.00669342162296594323;

	//CGJ02ToWGS84
	public static double[] Backstepping(double x1, double y1) {

		int count = 0;
		double x2, y2, dX, dY;
		double[] firstIteration = new double[2];
		x2 = x1;
		y2 = y1;

		while (true) {

			if (count > 50) {
				break;
			}
			firstIteration = GpsCorrect.transform(x2, y2);

			dX = firstIteration[0] - x1;
			dY = firstIteration[1] - y1;

			if (Math.abs(dX) < 0.00000000000001
					&& Math.abs(dY) < 0.000000000000001) {
				break;
			}

			x2 = x2 - dX;
			y2 = y2 - dY;

			count++;
		}

		return new double[] { x2, y2 };

	}

	// 84 转火星
	public static double[] transform(double wgLon, double wgLat) {

		double mgLat;
		double mgLon;

		if (outOfChina(wgLat, wgLon)) {
			mgLat = wgLat;
			mgLon = wgLon;
			return null;
		}
		double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
		double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
		double radLat = wgLat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);

		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);

		mgLat = wgLat + dLat;
		mgLon = wgLon + dLon;

		return new double[] { mgLon, mgLat };

	}

	public static boolean outOfChina(double lat, double lon) {
		if (lon < 72.004 || lon > 137.8347)
			return true;
		if (lat < 0.8293 || lat > 55.8271)
			return true;
		return false;
	}

	public static double transformLat(double x, double y) {
		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
				+ 0.2 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	public static double transformLon(double x, double y) {
		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
				* Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
				* pi)) * 2.0 / 3.0;
		return ret;
	}

	// / <summary>
	// / 中国正常坐标系GCJ02协议的坐标，转到 百度地图对应的 BD09 协议坐标
	// / </summary>
	// / <param name="lat">维度</param>
	// / <param name="lng">经度</param>
	public static double[] Convert_GCJ02_To_BD09(double lat, double lng,
												 double[] latlng) {
		double x = lng, y = lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
		lng = z * Math.cos(theta) + 0.0065;
		lat = z * Math.sin(theta) + 0.006;
		lng = latlng[0];
		lat = latlng[1];
		return latlng;
	}

	// / <summary>
	// / 百度地图对应的 BD09 协议坐标，转到 中国正常坐标系GCJ02协议的坐标
	// / </summary>
	// / <param name="lat">维度</param>
	// / <param name="lng">经度</param>
	public static double[] Convert_BD09_To_GCJ02(double lat, double lng,
												 double[] latlng) {
		double x = lng - 0.0065, y = lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		lng = z * Math.cos(theta);
		lat = z * Math.sin(theta);
		lng = latlng[0];
		lat = latlng[1];
		return latlng;
	}

	/**
	 * @param Latitude
	 * @param Longitude
	 * @return
	 * @throws IOException
	 */
	public static double[] gpsToBD09(String Latitude, String Longitude) {
		double[] lnglat = new double[2];
		try {
			URL url = new URL("http://api.map.baidu.com/geoconv/v1/?coords="
					+ Longitude + "," + Latitude
					+ "&from=3&to=5&ak=DD55be08e3404eca6ff7320129d13869");
			URLConnection connection = url.openConnection();
			// 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。
			// 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：
			connection.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream(), "utf-8");
			out.flush();
			out.close();
			// 一旦发送成功，用以下方法就可以得到服务器的回应：

			String sCurrentLine = "";
			String sTotalString = "";
			InputStream urlStream;
			urlStream = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlStream, "utf-8"));// 解决乱码问题

			while ((sCurrentLine = reader.readLine()) != null) {
				if (!sCurrentLine.equals(""))
					sTotalString += sCurrentLine;
			}
			// 获取到的数据进行解析
			String str = sTotalString
					.substring(sTotalString.indexOf("{") + 1,
							sTotalString.lastIndexOf("}")).replaceAll(",", ":")
					.replaceAll("\\{", "").replaceAll("\\}", "")
					.replaceAll("\"", "");
			// 转换为数组
			String[] results = str.split(":");// status:0:result:[x:107.18968047304:y:25.121936392028]
			System.out.println(Double.valueOf(results[4]));
			// 把需要的数据放入 HashMap
			lnglat[0] = Double.valueOf(results[4]);
			lnglat[1] = Double.valueOf(results[6].substring(0,
					results[6].length() - 1));
		} catch (Exception e) {
			return null;
		}
		return lnglat;
	}
}
