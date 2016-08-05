package com.otitan.gis;

import com.esri.core.geometry.Point;

import java.util.ArrayList;

/**
 * 
 *  常用的计算方法
 */
public class Calculate {
	/**
	 * 
	 * 计算两点之间的直线距离
	 * 
	 * 
	 * 
	 * @param lat1
	 * 
	 * @param lng1
	 * 
	 * @param lat2
	 * 
	 * @param lng2
	 * 
	 * @return
	 */

	public static int Distance(double lat1, double lng1, double lat2, double lng2) {
		Double R = 6370996.81; // 地球的半径 6378137.0
		/*
		 * 
		 * 获取两点间x,y轴之间的距离
		 */
		Double x = (lng2 - lng1) * Math.PI * R * Math.cos(((lat1 + lat2) / 2) * Math.PI / 180) / 180;
		Double y = (lat2 - lat1) * Math.PI * R / 180;
		int distance = (int) Math.hypot(x, y); // 得到两点之间的直线距离
		return distance;

	}
	
	/**
	 * 计算地图上两点实际距离
	 * 
	 */

	public static double TwoPoint_Distance(Point pt1, Point pt2) {
		return Math.sqrt((pt1.getX() - pt2.getX()) * (pt1.getX() - pt2.getX())
				+ (pt1.getY() - pt2.getY()) * (pt1.getY() - pt2.getY()));

	}
	/**
	 * 计算地图上两点的中点坐标
	 * 
	 */

	public static Point TwoPoint_Midpoint(Point pt1, Point pt2) {
		double x=(pt1.getX() + pt2.getX())/2;
		double y=(pt1.getY() + pt2.getY())/2;
		return new Point(x,y);

	}
	/**
	 * 计算地图上两点斜率
	 * 
	 */

	public static double TwoPoint_slope(Point pt1, Point pt2) {
		double k=(pt2.getY()-pt1.getY())/(pt2.getX()-pt1.getX());
		double deltX=pt2.getX()-pt1.getX();
		double rotateAngle=0;
		if(k == 0)
		{
			rotateAngle = 0;
		}
		else if(k > 0)
		{
			if(deltX > 0)
				rotateAngle = Math.atan(k)*180/Math.PI;
			else if(deltX < 0)
				rotateAngle = 180 + Math.atan(k)*180/Math.PI;
		}
		else if(k < 0)
		{
			if(deltX < 0)
				rotateAngle = 180 + Math.atan(k)*180/Math.PI;
			else if(deltX > 0)
				rotateAngle = 360 + Math.atan(k)*180/Math.PI;
		}
	
	return 360 - rotateAngle;

		
		/*double radian= Math.atan(k);//斜率转弧度
		float degree=(float) Math.toDegrees(radian); 
		twoPtsRelationShip( pt1,  pt2);
		String relation=twoPtsRelationShip( pt1,  pt2);
		if(relation.equals("ne")){
			
		}else if(relation.equals("nw")){
			degree=degree*(-1)+270;
		}else if(relation.equals("sw")){
			degree=degree+180;
		}else if(relation.equals("se")){
			degree=degree*(-1)+90;
		}
		
		if(degree<0){
			degree=degree+180;
		}
		return degree;//弧度转角度
*/
	}

	/**
	 * 计算地图上两点的方位关系 pt2在pt1的什么方位
	 */
	public static String twoPtsRelationShip(Point pt1, Point pt2) {
		if (pt2.getX() > pt1.getX() && pt2.getY() >= pt1.getY())
			return "ne";// 东北
		else if (pt2.getX() <= pt1.getX() && pt2.getY() > pt1.getY())
			return "nw";// 西北
		else if (pt2.getX() < pt1.getX() && pt2.getY() <= pt1.getY())
			return "sw";// 西南
		else
			return "se";// 东南
	}

	/**
	 * 计算地图上两点的
	 * 
	 */
	public static double twoPtsAngle(Point pt1, Point pt2) {
		double angle = Math.acos((pt2.getX() - pt1.getX())
				/ TwoPoint_Distance(pt1, pt2));
		if (pt2.getY() < pt1.getY()) {
			angle = 2 * Math.PI - angle;
		}
		return angle;
	}
	
	/**
	 * 计算地图上Polyline相邻线的夹角
	 * 
	 */
	public static ArrayList<Double> vertexAngle(ArrayList<Point> points) {
		ArrayList<Double> segmentAngle=new 	ArrayList<Double> ();
		ArrayList<Double> vertexAngle=new 	ArrayList<Double> ();
		for (int i = 0; i < points.size()-1; i++) {
			double x=twoPtsAngle(points.get(i),points.get(i+1));
			segmentAngle.add(x);
		}
		double x=twoPtsAngle(points.get(0), points.get(1));
		vertexAngle.add(x+=Math.PI/2);
		for (int i = 1; i < points.size()-1; i++) {
			x = (segmentAngle.get(i - 1) + segmentAngle.get(i)) / 2;
			if (segmentAngle.get(i-1) < Math.PI && segmentAngle.get(i) - Math.PI > segmentAngle.get(i-1))
			{
				x += Math.PI;
			}
			else if (segmentAngle.get(i-1) > Math.PI && segmentAngle.get(i) < segmentAngle.get(i-1) - Math.PI)
			{
				x += Math.PI;
			}


			x += Math.PI / 2;
	
			vertexAngle.add(x);
		}
		return vertexAngle;
	}

	/**
	 * 计算地图上Polyline的从起点的到终点的距离
	 * 
	 */
	public static double ptCollectionLen(ArrayList<Point> ptc, int startIndex) {
		double len = 0;
		for (int i = startIndex, pathLen = ptc.size() - 1; i < pathLen; i++) {
			len += TwoPoint_Distance(ptc.get(i), ptc.get(i + 1));
		}
		return len;
	}
	
	/**
	 * 计算贝塞尔曲线
	 * 
	 */
	public static ArrayList<Point> CreateBezierPathPCOnly(ArrayList<Point> pointCollection,int  numberOfPts) {
		ArrayList<Point> pts = new ArrayList<Point>();
		int pointcount=pointCollection.size();
		if(pointCollection.get(pointcount-1).getX()==pointCollection.get(pointcount-2).getX()&&pointCollection.get(pointcount-1).getY()==pointCollection.get(pointcount-2).getY()){
			pointCollection.remove(pointCollection.get(pointCollection.size()-1));
		}
		pointcount = pointCollection.size();
		if(pointCollection.get(pointcount-1).getX()==pointCollection.get(pointcount-2).getX()&&pointCollection.get(pointcount-1).getY()==pointCollection.get(pointcount-2).getY()){
			pointCollection.remove(pointCollection.get(pointCollection.size()-1));
		}
		  
		
		int n = pointCollection.size() - 1; //
		// u的步长决定了曲线点的精度
		for (float u = 0; u <= 1; u += 0.01) {

			Point[] p = new Point[n + 1];
			for (int i = 0; i <= n; i++) {
				p[i] = new Point(pointCollection.get(i).getX(), pointCollection
						.get(i).getY());
			}

			for (int r = 1; r <= n; r++) {
				for (int i = 0; i <= n - r; i++) {
					p[i].setX((1 - u) * p[i].getX() + u * p[i + 1].getX());
					p[i].setY((1 - u) * p[i].getY() + u * p[i + 1].getY());
				}
			}
			pts.add(p[0]);
		}
		


		return pts;
	}

	/**
	 * 计算三次贝塞尔曲线
	 * 
	 */
	public static Point PointOnCubicBezier(ArrayList<Point> ps, double t) {
		float ax, bx, cx;
		float ay, by, cy;
		double tSquared, tCubed;
		Point result;

		/* 計算多項式係數 */

		cx = (float) (3.0 * (ps.get(1).getX() - ps.get(0).getX()));
		bx = (float) (3.0 * (ps.get(2).getX() - ps.get(1).getX()) - cx);
		ax = (float) (ps.get(3).getX() - ps.get(0).getX() - cx - bx);

		cy = (float) (3.0 * (ps.get(1).getY() - ps.get(0).getY()));
		by = (float) (3.0 * (ps.get(2).getY() - ps.get(1).getY()) - cy);
		ay = (float) (ps.get(3).getY() - ps.get(0).getY() - cy - by);

		/* 計算位於參數值t的曲線點 */

		tSquared = t * t;
		tCubed = tSquared * t;

		double x = (ax * tCubed) + (bx * tSquared) + (cx * t)
				+ ps.get(0).getX();
		double y = (ay * tCubed) + (by * tSquared) + (cy * t)
				+ ps.get(0).getY();
		result = new Point(x, y);

		return result;
	}

	/**
	 * 计算二次贝塞尔曲线
	 * 
	 */
	public static Point PointOnQuadBezier(ArrayList<Point> ps, double t) {
		// （１－ｔ）２　Ｐ０＋２ｔ（１－ｔ）Ｐ１＋ｔ２　 Ｐ２
		double x = (1 - t) * (1 - t) * ps.get(0).getX() + 2 * t * (1 - t)
				* ps.get(1).getX() + t * t * ps.get(2).getX();
		double y = (1 - t) * (1 - t) * ps.get(0).getY() + 2 * t * (1 - t)
				* ps.get(1).getY() + t * t * ps.get(2).getY();
		return new Point(x, y);
	}

	/**
	 * 计算点到直线的最短距离
	 * 
	 */
	public static double PointToSegDist(double x, double y, double x1,
			double y1, double x2, double y2) {
		double cross = (x2 - x1) * (x - x1) + (y2 - y1) * (y - y1);
		if (cross <= 0)
			return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));

		double d2 = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
		if (cross >= d2)
			return Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));

		double r = cross / d2;
		double px = x1 + (x2 - x1) * r;
		double py = y1 + (y2 - y1) * r;
		return Math.sqrt((x - px) * (x - px) + (py - y1) * (py - y1));
	}

	/*
	 * public double pointToLineDistance(Point A, Point B, Point P) { double
	 * normalLength = Math.sqrt((B.x - A.x) * (B.x - A.x) + (B.y - A.y) (B.y -
	 * A.y)); return Math.abs((P.x - A.x) * (B.y - A.y) - (P.y - A.y) * (B.x -
	 * A.x)) / normalLength; }
	 */

	/**
	 * 计算一点与直线中心点的对称点 pt1,pt2为直线上的起止点
	 */
	public static Point PointToLine(Point pt1, Point pt2, Point pt3) {
		double x = pt1.getX() + pt2.getX() - pt3.getX();
		double y = pt1.getY() + pt2.getY() - pt3.getY();
		return new Point(x, y);
	}

}
