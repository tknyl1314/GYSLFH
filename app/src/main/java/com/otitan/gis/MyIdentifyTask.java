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

import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;
import com.esri.android.map.Callout;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.tasks.identify.IdentifyParameters;
import com.esri.core.tasks.identify.IdentifyResult;
import com.esri.core.tasks.identify.IdentifyTask;
import com.otitan.gyslfh.R;
import com.otitan.gyslfh.activity.MapActivity;
import com.otitan.gyslfh.activity.MonitorActivity;
import com.otitan.util.ResourcesManager;
import com.otitan.util.ToastUtil;
import com.otitan.util.WebServiceUtil;
import com.titan.navi.BNDemoGuideActivity;
import com.titan.navi.BaiduNavi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

@SuppressLint("ShowToast") public class MyIdentifyTask extends AsyncTask<IdentifyParameters, Void, IdentifyResult[]> {

	IdentifyTask mIdentifyTask;
	Point mAnchor;
	MapView mView;
	ProgressDialog progress;
	String mapurl;
	Context mContext;
	Callout callout;
	Point startPoint;
	BaiduNavi baiduNavi = null;
	String TD, LXJID;
	MapActivity.BaiduRoutePlanListener mBaiduRoutePlanListener;
	public static final String ROUTE_PLAN_NODE = "routePlanNode";
	String objid;//监控点objectid
	public WebServiceUtil websUtil;

	public MyIdentifyTask(Context context, MapView mapView, String url, Point uppoint,
			MapActivity.BaiduRoutePlanListener navlis) {
		mView = mapView;
		mapurl = url;
		mContext = context;
		startPoint = uppoint;
		mBaiduRoutePlanListener = navlis;
		initBaiduNavi();
		websUtil = new WebServiceUtil(mContext);
		// mAnchor=point;
	}

	@Override
	protected IdentifyResult[] doInBackground(IdentifyParameters... params) {
		IdentifyResult[] mResult = null;
		if (params != null && params.length > 0) {
			IdentifyParameters mParams = params[0];
			try {
				// 获取要素数据
				mResult = mIdentifyTask.execute(mParams);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return mResult;
	}

	@Override
	protected void onPostExecute(IdentifyResult[] results) {
		ArrayList<IdentifyResult> resultList = new ArrayList<IdentifyResult>();
		if(results!=null){
			for (int i = 0; i < results.length;i++) {
				Geometry geometry = results[i].getGeometry();
				if(geometry.getType()== Geometry.Type.POINT){
					resultList.add(results[i]);
				}
			}
			if(resultList.size() == 0){
				mView.getCallout().hide();
				Toast.makeText(mContext, "未查询到结果", Toast.LENGTH_LONG);
				progress.dismiss();// 停止进度条
				return;
			}
			Map<String, Object> attibutes = resultList.get(0).getAttributes();
			mAnchor =(Point) resultList.get(0).getGeometry();
			mView.centerAt(mAnchor, true);
			mView.setResolution(5);
			callout = mView.getCallout();
			callout.setCoordinates(mAnchor);
			callout.setOffset(0, 15);
			callout.setMaxWidth(325);
			callout.setStyle(R.xml.calloutstyle);
			callout.setContent(createIdentifyContent(attibutes));
			callout.show();
		}else {
			mView.getCallout().hide();
			Toast.makeText(mContext, "未查询到结果", Toast.LENGTH_LONG);
		}
		progress.dismiss();// 停止进度条
	}

	@Override
	protected void onPreExecute() {
		// 实例化一个要素识别类对象
		mIdentifyTask = new IdentifyTask(mapurl);
		// 在未查询出结果时显示一个进度条
		//progress = ProgressDialog.show(mContext, "", "Please wait....query task is executing");
		progress = ProgressDialog.show(mContext, "", "请稍等....正在查询中");
	}

	// callout 弹框
	private ViewGroup createIdentifyContent(Map<String, Object> attibutes) {

		ViewGroup view = (ViewGroup) ViewGroup.inflate(mContext, R.layout.callout_identify, null);
		TextView content = (TextView) view.findViewById(R.id.callout_content);
		// 导航按钮
		Button navButton = (Button) view.findViewById(R.id.callout_nav);
		// 视频监控
		Button viewButton = (Button) view.findViewById(R.id.callout_view);
		// close callout
		ImageButton bt_close = (ImageButton) view.findViewById(R.id.bt_close);
		String dataString = "";
		 objid=null;
		for (Entry<String, Object> entry : attibutes.entrySet()) {
			Boolean d =attibutes.containsKey("监控点名称");
			if (entry.getKey().equals("OBJECTID")|| entry.getKey().equals("SHAPE")) {
				if(attibutes.containsKey("监控点名称")&&entry.getKey().equals("OBJECTID"))
				{
					objid=(String) entry.getValue();
					viewButton.setVisibility(View.VISIBLE);
				}
				continue;
			}
			dataString += entry.getKey() + ":" + entry.getValue() + "\n";
			
//			if(!(entry.getKey().equals("OBJECTID")|| entry.getKey().equals("SHAPE"))){
//				dataString += entry.getKey() + ":" + entry.getValue() + "\n";
//			}else{
//				if(attibutes.containsKey("监控点名称") && entry.getKey().equals("OBJECTID"))
//				{
//					objid=(String) entry.getValue();
//					viewButton.setVisibility(View.VISIBLE);
//				}
//			}
		}
		content.setText(dataString);
		
		navButton.setOnClickListener(new OnClickListener() {

			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
				mView.getCallout().hide();
				ToastUtil.setToast((Activity) mContext, "正在为您计算导航路线，请稍后");
				Point sPoint = (Point) GeometryEngine.project(startPoint, mView.getSpatialReference(),
						SpatialReference.create(4326));
				Point epoint = (Point) GeometryEngine.project(mAnchor, mView.getSpatialReference(),
						SpatialReference.create(4326));
				BNRoutePlanNode sNode = new BNRoutePlanNode(sPoint.getX(), sPoint.getY(), "起点", null,
						CoordinateType.WGS84);
				BNRoutePlanNode eNode = new BNRoutePlanNode(epoint.getX(), epoint.getY(), "终点", null,
						CoordinateType.WGS84);
				baiduNavi.initListener(sNode, eNode, CoordinateType.WGS84, mBaiduRoutePlanListener);
				//Toast.makeText(mContext, "正在为您计算导航路线，请稍后", Toast.LENGTH_LONG);
			}
		});
		// 视频监控
		viewButton.setOnClickListener(new OnClickListener() {

			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			@Override
			public void onClick(View v) {
				 
				 mView.getCallout().hide();
				 
				 Intent monitorintent=new Intent(mContext,MonitorActivity.class);
				 String result=websUtil.getMonitortd(objid);
				 if(result!=null){
					 JSONObject obj;
					try {
						obj = new JSONObject(result);
						JSONArray arr = obj.optJSONArray("ds");
						if (arr != null) {
							JSONObject object = arr.optJSONObject(0);
							if(object!=null){
								 TD = object.getString("TD");//监控点通道
								 LXJID= object.getString("LXJID");//监控点所在硬盘录像机id
								 monitorintent.putExtra("TD", TD);
								 monitorintent.putExtra("LXJID", LXJID);
								monitorintent.putExtra("username", "admin");//用户名
								monitorintent.putExtra("password", "sfb12345");//密码
								monitorintent.putExtra("ip", "222.85.147.92");//设备ip
								//监控点位置
								monitorintent.putExtra("Location", object.getString("TDNAME"));
								mContext.startActivity(monitorintent);
							}
							else{
								ToastUtil.setToast((Activity) mContext, "获取通道失败");
							}
						
						}
						else{
							ToastUtil.setToast((Activity) mContext, "获取通道失败");
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				 }
				
				
				
			}
		});
		// callout close
		bt_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mView.getCallout().hide();
				//MapActivity.active = false;
			}
		});
		return view;
	}

	public class BaiduRoutePlanListener implements RoutePlanListener {

		private BNRoutePlanNode mBNRoutePlanNode = null;

		public BaiduRoutePlanListener(BNRoutePlanNode node) {
			mBNRoutePlanNode = node;
		}

		@Override
		public void onJumpToNavigator() {
			/*
			 * 设置途径点以及resetEndNode会回调该接口
			 */
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
		String APP_FOLDER_NAME = mContext.getResources().getString(R.string.app_name);
		// String APP_FOLDER_NAME="test";
		String mSDCardPath = null;
		try {
			mSDCardPath = ResourcesManager.getInstance(mContext).getNaviPath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		baiduNavi = new BaiduNavi();
		baiduNavi.initNavi(mContext, mSDCardPath, APP_FOLDER_NAME);

	}
}