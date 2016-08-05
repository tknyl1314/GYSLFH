package com.otitan.gis;

import android.content.Context;
import android.graphics.Color;

import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.symbol.CompositeSymbol;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.Symbol;
import com.otitan.entity.TrackPoint;
import com.otitan.gyslfh.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** 
 * 
 * 轨迹回放优化模块
 * 
 * 
 */

public class TrackUtil {
	//轨迹回放样式；
	public static  CompositeSymbol tracksym;
	public static  PictureMarkerSymbol sp;
	public static  PictureMarkerSymbol ep;
	public static PictureMarkerSymbol trackarrow;
	public  TrackUtil(Context context) {
		LineSymbol TracklineSymbol = new SimpleLineSymbol(Color.GREEN, 8);
		 trackarrow=new PictureMarkerSymbol(context.getResources().getDrawable(R.drawable.tarrow8));
		 sp=new PictureMarkerSymbol(context.getResources().getDrawable(R.drawable.startpoint));
		 sp.setOffsetY(20);
		 ep=new PictureMarkerSymbol(context.getResources().getDrawable(R.drawable.endpoint));
		 ep.setOffsetY(20);
		//PictureMarkerSymbol trackpicsym=new PictureMarkerSymbol(context.getResources().getDrawable(R.drawable.trackarow));
		//List<Symbol> listsym=new ArrayList<Symbol>(){TracklineSymbol,trackms};
		List<Symbol> listsym=new ArrayList<Symbol>();
		listsym.add(TracklineSymbol);
		//listsym.add(trackpicsym);
		 tracksym=new CompositeSymbol(listsym);
		/*tracksym.add(TracklineSymbol);
		tracksym.add(trackms);*/
	}
	
	//CompositeSymbol tracksym=new CompositeSymbol(new List<Symbol> {TracklineSymbol,trackms});
    public static List<List<Point>> OptimizeTrackPoint (List<TrackPoint> pts,MapView mapview) {
    //List<Point> points=new ArrayList<Point>();
    	List<List<Point>> points=new ArrayList<List<Point>>();
    	List<TrackPoint> tps=new ArrayList<TrackPoint>();
    	final SimpleDateFormat timeFormat = new SimpleDateFormat(
    			"yyyy-MM-dd HH:mm:ss");
    	Point lastpoint=null;
    	List<TrackPoint> newpts=null;
    	int j=0;
    	List<Point>  pointset=new ArrayList<Point>();
    	//判断点是否在地图范围内,去除不在地图范围内的点
    	for(int i=0;i<pts.size();i++){
    		/*Point pt=new Point(pts.get(i).getLongitude(),pts.get(i).getLatitude());
    		if(!GeometryEngine.within(pt, mapview.getExtent(), mapview.getSpatialReference())){
    			continue;
    		}*/
    		tps.add(pts.get(i));
    		//points.add(pt);
    	}
    	
    	
    	for (int i=0;i<tps.size();i++){
    		if(pointset.size()>0){
    			try {
					Date dt1=timeFormat.parse(tps.get(i).getTime());
					Date dt2=timeFormat.parse(tps.get(i-1).getTime());
					long td=dt1.getTime()-dt2.getTime();
					//long day=td/(24*1000*60*60);
					//long hour=(td/(60*60*1000)-day*24);
					//long min=((td/(60*1000))-day*24*60-hour*60);
					long min=td/(60000);
					Point pt1=new Point(tps.get(i-1).getLongitude(),tps.get(i-1).getLatitude());
					Point pt2=new Point(tps.get(i).getLongitude(),tps.get(i).getLatitude());
					double dis=Calculate.TwoPoint_Distance(pt1, pt2);
					//double degree=Calculate.TwoPoint_slope(pt1, pt2);
					if(min>10||dis>100||i==(tps.size()-1)){
						//忽略时间间隔大于10分钟或者距离大于100米的点
						if(pointset.size()<2){
							//pointset.clear();
							pointset=new ArrayList<Point>();
							continue;
						}
						points.add(pointset);
						pointset=new ArrayList<Point>();
						//pointset.clear();
						continue;
					}
					else{
						Point p=new Point(tps.get(i).getLongitude(),tps.get(i).getLatitude());
		    			pointset.add(p);
		    			//points.add(pointset);
		    			
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
    		}else{
    			Point p=new Point(tps.get(i).getLongitude(),tps.get(i).getLatitude());
    			pointset.add(p);
    			
    		}
    		
    	}
    	/**贝塞尔重绘*/
    	/*for (int i=0;i<points.size();i++){
    		ArrayList<Point> listpt = Calculate.CreateBezierPathPCOnly((ArrayList<Point>) points.get(i), 100);
    		points.set(i, listpt);
    	}*/
    	/*for(List<Point> lp:points){
    		if(lp.size()<2){
    			points.removeAll(lp);
    		}
    	}*/
      /*  Iterator it=points.iterator();
        while(it.hasNext()){
        	List sp= (List) it.next();
        	if(sp.size()<2){
        		continue;
        	}
        }*/
    	
    	return  points;
	}
}
