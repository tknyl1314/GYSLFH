package com.titan.gis;


import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.titan.model.TitanLocation;

/**
 * 各地图API坐标系统比较与转换;
 * WGS84坐标系：即地球坐标系，国际上通用的坐标系。设备一般包含GPS芯片或者北斗芯片获取的经纬度为WGS84地理坐标系,
 * 谷歌地图采用的是WGS84地理坐标系（中国范围除外）;
 * GCJ02坐标系：即火星坐标系，是由中国国家测绘局制订的地理信息系统的坐标系统。由WGS84坐标系经加密后的坐标系。
 * 谷歌中国地图和搜搜中国地图采用的是GCJ02地理坐标系; BD09坐标系：即百度坐标系，GCJ02坐标系经加密后的坐标系;
 * 搜狗坐标系、图吧坐标系等，估计也是在GCJ02基础上加密而成的。 chenhua
 */
public class PositionUtil {

	public static final String BAIDU_LBS_TYPE = "bd09ll";

	public static double pi = 3.1415926535897932384626;
	public static double a = 6378245.0;
	public static double ee = 0.00669342162296594323;

	/**
	 * 84 to 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System
	 *
	 * @param lat
	 * @param lon
	 * @return
	 */
	public static Gps gps84_To_Gcj02(double lat, double lon) {
		if (outOfChina(lat, lon)) {
			return null;
		}
		double dLat = transformLat(lon - 105.0, lat - 35.0);
		double dLon = transformLon(lon - 105.0, lat - 35.0);
		double radLat = lat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		double mgLat = lat + dLat;
		double mgLon = lon + dLon;
		return new Gps(mgLat, mgLon);
	}

	/**
	 * * 火星坐标系 (GCJ-02) to 84 * * @param lon * @param lat * @return
	 * */
	public static Gps gcj_To_Gps84(double lat, double lon) {
		Gps gps = transform(lat, lon);
		double lontitude = lon * 2 - gps.getWgLon();
		double latitude = lat * 2 - gps.getWgLat();
		return new Gps(latitude, lontitude);
	}

	/**
	 * * 火星坐标系 (GCJ-02) to 84 * * @param lon * @param lat * @return
	 * */
	/*public static Gps gcj_To_Gps84(Point point) {
		Gps gps = transform(point.getY(), point.getX());

		double lontitude = point.getX() * 2 - gps.getWgLon();
		double latitude = point.getY() * 2 - gps.getWgLat();
		return new Gps(latitude, lontitude);
	}*/
	/**
	 * * 火星坐标系 (GCJ-02) to 84 * * @param lon * @param lat * @return
	 * */
	/*public static Point gcj_To_84(Point point) {
		Gps gps = transform(point.getY(), point.getX());

		double lontitude = point.getX() * 2 - gps.getWgLon();
		double latitude = point.getY() * 2 - gps.getWgLat();
		return new Point(latitude, lontitude,point.getZ());
	}*/


	/**
	 * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
	 *
	 * @param gg_lat
	 * @param gg_lon
	 */
	/*public static Gps gcj02_To_Bd09(double gg_lat, double gg_lon) {
		double x = gg_lon, y = gg_lat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);
		double bd_lon = z * Math.cos(theta) + 0.0065;
		double bd_lat = z * Math.sin(theta) + 0.006;
		return new Gps(bd_lat, bd_lon);
	}*/

    public static TitanLocation gcj02_To_Bd09(double gg_lon,double gg_lat) {
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new TitanLocation(bd_lon, bd_lat);
    }

	/**
	 * * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 * * 将 BD-09 坐标转换成GCJ-02 坐标 * * @param
	 * bd_lat * @param bd_lon * @return
	 */
	/*public static Gps bd09_To_Gcj02(double bd_lat, double bd_lon) {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new Gps(gg_lat, gg_lon);
    }*/
    public static TitanLocation bd09_To_Gcj02(double bd_lon, double bd_lat) {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new TitanLocation(gg_lon, gg_lat);

    }

	/**
	 * (BD-09)-->84
	 * @param
	 * @param
	 * @return
	 */
	/*public static Gps bd09_To_Gps84(double bd_lat, double bd_lon) {

		Gps gcj02 = PositionUtil.bd09_To_Gcj02(bd_lon, bd_lat);
		Gps map84 = PositionUtil.gcj_To_Gps84(gcj02.getWgLat(),
				gcj02.getWgLon());
		return map84;

	}*/

	public static boolean outOfChina(double lat, double lon) {
		if (lon < 72.004 || lon > 137.8347)
			return true;
		if (lat < 0.8293 || lat > 55.8271)
			return true;
		return false;
	}

	public static Gps transform(double lat, double lon) {
		if (outOfChina(lat, lon)) {
			return new Gps(lat, lon);
		}
		double dLat = transformLat(lon - 105.0, lat - 35.0);
		double dLon = transformLon(lon - 105.0, lat - 35.0);
		double radLat = lat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		double mgLat = lat + dLat;
		double mgLon = lon + dLon;
		return new Gps(mgLat, mgLon);
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

	public static LatLng gpsToBd09(double lat,double lon){
		LatLng latLng = new LatLng(lat,lon);
		CoordinateConverter converter  = new CoordinateConverter();
		converter.from(CoordinateConverter.CoordType.GPS);
		converter.coord(latLng);
		return converter.convert();
	}
	/**
	 * wgs84 转 西安80
	 * 使用Proj4纠偏（三参数纠偏）
	 */
	/*public static  Point meth(Point pt,String[] locparam) {
		*//*double x1 = Double.parseDouble(sharedPreferences.getString("dx", "0"));
		double y1 = Double.parseDouble(sharedPreferences.getString("dy", "0"));
		double z1 = Double.parseDouble(sharedPreferences.getString("dz", "0"));*//*
		double x1 = 0;
		double y1 = 0;
		double z1 = 0;
		//String [] locparam=DataBaseHelper.getLocParam(qxid);
		double lon=pt.getX();
		double lat=pt.getY();
		double al=pt.getZ();
		if(al==4.9E-324){
			al=0;
		}

		*//*double x1 = Double.parseDouble("0");
		double y1 = Double.parseDouble("0");
		double z1 = Double.parseDouble("0");*//*
		if(locparam!=null){
			 x1 = Double.parseDouble(locparam[0]);
			 y1 = Double.parseDouble(locparam[1]);
			 z1 = Double.parseDouble(locparam[2]);
		}

		//x1=-107.4036667;y1=-37.915;z1=3.961;//南明区参数
		String[] wgs840 = new String[] { "+proj=longlat", "+ellps=WGS84",
				"+datum=WGS84", "+no_defs" };
		Datum datum0 = Datum.WGS84;
		Projection proj840 = ProjectionFactory.fromPROJ4Specification(wgs840);
		CoordinateReferenceSystem wgs84cs0 = new CoordinateReferenceSystem("WGS84", wgs840, datum0, proj840);
		String[] xian801 = new String[] { "+proj=longlat", "+a=6378140",
				"+b=6356755.288157528", "+no_defs" };
		Ellipsoid ellipsoid1 = new Ellipsoid("xian80", 6378140, 0.0, 298.257,
				"xian80");
		Projection proj801 = ProjectionFactory.fromPROJ4Specification(xian801);

		Datum datum801 = new Datum("xian80", x1, y1, z1, ellipsoid1, "xian80");
		CoordinateReferenceSystem xian80cs1 = new CoordinateReferenceSystem("xian80", xian801, datum801, proj801);

		String[] xian80 = new String[] { "+proj=tmerc", "+lat_0=0",
				"+lon_0=105", "+k=1", "+x_0=500000", "+y_0=0", "+a=6378140",
				"+b=6356755.288157528", "+units=m", "+no_defs" };
		Ellipsoid ellipsoid = new Ellipsoid("xian80", 6378140, 0.0, 298.257,
				"xian80");
		Projection proj80 = ProjectionFactory.fromPROJ4Specification(xian80);

		Datum datum80 = new Datum("xian80", x1, y1, z1, ellipsoid, "xian80");
		CoordinateReferenceSystem xian80cs = new CoordinateReferenceSystem("xian80", xian80, datum80, proj80);

		ProjCoordinate src = new ProjCoordinate(lon, lat, al);
		ProjCoordinate dst = new ProjCoordinate();

		BasicCoordinateTransform transformation = new BasicCoordinateTransform(
				wgs84cs0, xian80cs1);
		ProjCoordinate ddd = transformation.transform(src, dst);
		//System.out.println("未投影西安80 " + ddd.x + " === " + ddd.y + "---- " + ddd.z);
		ProjCoordinate src1 = new ProjCoordinate(ddd.x, ddd.y, ddd.z);

		BasicCoordinateTransform transformation1 = new BasicCoordinateTransform(
				xian80cs1, xian80cs);
		ProjCoordinate ddd1 = transformation1.transform(src1, dst);
		//System.out.println("西安80 投影" + ddd1.x + " === " + ddd1.y + "---- " + ddd1.z);
		//return ddd1;
		Point p=new Point(ddd1.x, ddd1.y, ddd1.z);
		return p;
	}


	public static void main(String[] args) {

		// 北斗芯片获取的经纬度为WGS84地理坐标 31.426896,119.496145
		Gps gps = new Gps(31.426896, 119.496145);
		System.out.println("gps :" + gps);
		Gps gcj = gps84_To_Gcj02(gps.getWgLat(), gps.getWgLon());
		System.out.println("gcj :" + gcj);
		Gps star = gcj_To_Gps84(gcj.getWgLat(), gcj.getWgLon());
		System.out.println("star:" + star);
		Gps bd = gcj02_To_Bd09(gcj.getWgLat(), gcj.getWgLon());
		System.out.println("bd  :" + bd);
		Gps gcj2 = bd09_To_Gcj02(bd.getWgLat(), bd.getWgLon());
		System.out.println("gcj :" + gcj2);
	}*/
}

