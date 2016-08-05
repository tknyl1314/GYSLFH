package com.otitan.gis;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navi.BNDemoGuideActivity;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;
import com.esri.android.map.Callout;
import com.esri.android.map.MapView;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.core.tasks.query.QueryTask;
import com.otitan.gyslfh.R;
import com.otitan.util.ResourcesManager;
import com.otitan.util.ToastUtil;
import com.titan.navi.BaiduNavi;

import java.util.Map;
import java.util.Map.Entry;

public class MyQueryTask  extends AsyncTask<String, Void, FeatureResult> {

	/**
	 * @param args
	 */
	MapView mView;
	String mapurl;
	Context mContext;
	Callout callout;
	ProgressDialog progress;
	Point sinlePoint;
	Graphic resgrraphic;
	Point p;
	Point startPoint;
	BaiduNavi baiduNavi=null;
	double mapresolution=0;
	SpatialReference mapsr;
	com.otitan.gyslfh.activity.MapActivity.BaiduRoutePlanListener mBaiduRoutePlanListener;
	public static final String ROUTE_PLAN_NODE = "routePlanNode";

	public MyQueryTask(Context context, MapView mapView, String url,Point singlepoint,Point uppoint,com.otitan.gyslfh.activity.MapActivity.BaiduRoutePlanListener navlis) {
		mView = mapView;
		mapurl = url;
		mContext = context;
		sinlePoint=singlepoint;
		startPoint=uppoint;
		mBaiduRoutePlanListener=navlis;
		initBaiduNavi();
	}
	protected void onPreExecute() {
		//在未查询出结果时显示一个进度条
		progress = ProgressDialog.show(mContext, "", "Please wait....query task is executing");
		mapresolution=mView.getResolution();
		mapsr=mView.getSpatialReference();

	}
	protected FeatureResult doInBackground(String... queryParams) {
		if (queryParams == null || queryParams.length <= 1)
			return null;
		//查询条件和URL参数
		String url = queryParams[0];
		//查询所需的参数类
		QueryParameters query = new QueryParameters();
		String whereClause = queryParams[1];
		// SpatialReference sr = SpatialReference.create(102100);
		Polygon circlePolygon = GeometryEngine.buffer(sinlePoint,mapsr, 20*mapresolution, null);
		query.setGeometry(circlePolygon);//设置查询空间范围
		query.setMaxFeatures(1);
		query.setOutFields(new String[] { "*" });
		query.setOutSpatialReference(mapsr);//设置查询输出的坐标系
		query.setReturnGeometry(true);//是否返回空间信息
		//query.setSpatialRelationship(SpatialRelationship.);
		query.setWhere("1=1");//where条件
		QueryTask qTask = new QueryTask(url);//查询任务类
		FeatureResult fs = null;
		try {
			fs = qTask.execute(query);//执行查询，返回查询结果
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return fs;
		}
		return fs;

	}

	protected void onPostExecute(FeatureResult results) {
		String message = "No result comes back";
		results.getFields().get(0).getAlias();
		String [] fieldsname=new String[20];
		if (results != null) {
			int size = (int) results.featureCount();
			for (Object element : results) {
				progress.incrementProgressBy(size / 100);
				for(int i=0;i<results.getFields().size();i++)
				{
					fieldsname[i]=results.getFields().get(i).getAlias();
				}
				if (element instanceof Feature) {
					Feature feature = (Feature) element;
					// turn feature into graphic
					Graphic graphic = new Graphic(feature.getGeometry(), feature.getSymbol(), feature.getAttributes());
					 p = (Point) graphic.getGeometry();
					// graphic.getAttributes()
					Map<String, Object> attibutes= graphic.getAttributes();
					//mView.getCallout().show(p, createIdentifyContent(attributeString));
					mView.centerAt(p, true);
					mView.setResolution(5);
					// point3.getX()
					callout = mView.getCallout();
					callout.setCoordinates((Point) p);
					callout.setOffset(0, 15);
					callout.setMaxWidth(325);
					callout.setStyle(R.xml.calloutstyle);
					// populate callout with results from IdentifyTask
					callout.setContent(createIdentifyContent(fieldsname,attibutes));
					callout.show();
					// add graphic to layer
					//graphicsLayer.addGraphic(graphic);
				}
			}
           if(size==0){
        	   mView.getCallout().hide();
   			Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
   			toast.show();
           }
		} else {
			mView.getCallout().hide();
			Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
			toast.show();
		}
		/* if (result instanceof Feature) {
			 Feature f = (Feature) result;
			
			 Point p =(Point)  f.getGeometry();
				String attributeString=f.getAttributes().toString();
				 mView.getCallout().show(p , createIdentifyContent(attributeString));
		 }
		 else {
				mView.getCallout().hide();
			}*/
		
	/*	if (result != null) {
			//Graphic[] grs = result.getGraphics();
			 

			if (grs.length > 0) {
				Point p =(Point) grs[0].getGeometry();
				String attributeString=grs[0].getAttributes().toString();
				 mView.getCallout().show(p , createIdentifyContent(attributeString));
			}
			else {
				mView.getCallout().hide();
			}

		}*/
		progress.dismiss();//停止进度条

		
		/*querybt.setText("Clear graphics");
		blQuery = false;
*/
	}
	
	//callout 弹框
    private ViewGroup createIdentifyContent(String[] fieldsname,Map<String, Object> attibutes) {
            ViewGroup view = (ViewGroup) ViewGroup.inflate(mContext,R.layout.callout_identify, null);
		    TextView content=(TextView) view.findViewById(R.id.callout_content);
		    String dataString="";
		    int i=0;
		    for(Entry<String, Object> entry: attibutes.entrySet())
		    {
		    	 dataString+=fieldsname[i]+ ":" + entry.getValue()+"\n";
		    	 i++;
		    }
		    content.setText(dataString);
		    //导航按钮
		    Button navButton=(Button) view.findViewById(R.id.callout_nav);
		    //视频监控
		    Button viewButton=(Button) view.findViewById(R.id.callout_view);
		    //close callout
		    ImageButton bt_close = (ImageButton) view.findViewById(R.id.bt_close);
		    
		 /*   String lon=(String) resgrraphic.getAttributeValue("X");
		    String lai=(String) resgrraphic.getAttributeValue("Y");*/
		    navButton.setOnClickListener(new OnClickListener() {
				
				@SuppressLint("ShowToast")
				@Override
				public void onClick(View v) {
					mView.getCallout().hide();
					    ToastUtil.setToast((Activity) mContext, "正在为您计算导航路线，请稍后");
						SpatialReference spatialReference= mView.getSpatialReference();
						Point sPoint=(Point) GeometryEngine.project(startPoint, mView.getSpatialReference(),SpatialReference.create(4326));
						Point epoint = (Point) GeometryEngine.project(p, mView.getSpatialReference(),SpatialReference.create(4326));
						BNRoutePlanNode  sNode = new BNRoutePlanNode(sPoint.getX(), sPoint.getY(), "起点", null, CoordinateType.WGS84);
						BNRoutePlanNode eNode = new BNRoutePlanNode(epoint.getX(), epoint.getY(), "终点", null, CoordinateType.WGS84);
						//com.otitan.gis.MyQueryTask.BaiduRoutePlanListener mBaiduRoutePlanListener=new BaiduRoutePlanListener(sNode);
						baiduNavi.initListener(sNode, eNode,CoordinateType.WGS84,mBaiduRoutePlanListener);
						Toast.makeText(mContext, "正在为您计算导航路线，请稍后", Toast.LENGTH_LONG);
				}
			});
		    
		    viewButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mView.getCallout().hide();
				}
			});
		    
		    bt_close.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mView.getCallout().hide();
				}
			});
			return view;
		}
    public  class BaiduRoutePlanListener implements RoutePlanListener {

		private BNRoutePlanNode mBNRoutePlanNode = null;

		public BaiduRoutePlanListener(BNRoutePlanNode node) {
			mBNRoutePlanNode = node;
		}

		@Override
		public void onJumpToNavigator() {
			/*
			 * 设置途径点以及resetEndNode会回调该接口
			 
		 
			for (Activity ac : activityList) {
			   
				if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {
					return;
				}
			}*/
			Intent intent = new Intent(mContext, BNDemoGuideActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
			intent.putExtras(bundle);
			mContext.startActivity(intent);
			
		}

		@Override
		public void onRoutePlanFailed() {
			Toast.makeText(mContext, "路径计算失败", Toast.LENGTH_SHORT).show();
		}
	}
    private void initBaiduNavi() {
		// BaiduNaviManager.getInstance().setNativeLibraryPath(mSDCardPath +
		// "/BaiduNaviSDK_SO");
		String APP_FOLDER_NAME =  mContext.getResources().getString(R.string.app_name);
		//String APP_FOLDER_NAME="test";
	    String mSDCardPath = null;
		try {
			mSDCardPath = ResourcesManager.getInstance(mContext).getNaviPath();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    baiduNavi=new BaiduNavi();
	    baiduNavi.initNavi(mContext, mSDCardPath, APP_FOLDER_NAME) ;

	
	}
	
}
