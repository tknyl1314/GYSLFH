package com.otitan.gis;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.otitan.gyslfh.activity.MapActivity;
import com.otitan.gyslfh.R;
import com.otitan.util.ResourcesManager;
import com.otitan.util.ToastUtil;
import com.titan.navi.BaiduNavi;

import java.util.Map;
import java.util.Map.Entry;

public class GraphicLayerutil
{
	MapView mapview;
	Context mContext;
	//nav
	BaiduNavi baiduNavi = null;
	Point startpoint;
	Point endpoint;
	MapActivity.BaiduRoutePlanListener mBaiduRoutePlanListener;
	public static final String ROUTE_PLAN_NODE = "routePlanNode";
	public  GraphicLayerutil(Context context,MapView mapview,Point spoint,MapActivity.BaiduRoutePlanListener navlis)
	{
		this.mapview =mapview;
		this.mContext=context;
		this.startpoint=spoint;
		this.mBaiduRoutePlanListener=navlis;
		initBaiduNavi();
	}
	/*public   void SelectOneGraphic(MapView mapview,float x, float y) {
		this.mapview=mapview;
		// 获得图层
		// GraphicsLayer layer = GetGraphicLayer();
		final Point point = mapView.toMapPoint(x, y);
		if (graphicsLayer != null && graphicsLayer.isInitialized() && graphicsLayer.isVisible()) {
			Graphic result = null;
			// 检索当前 光标点（手指按压位置）的附近的 graphic对象
			result = GetGraphicsFromLayer(x, y, graphicsLayer);
			if (result != null) {
				List<ChildEntity> list = getListEntity(result);
				// 显示提示callout
				// Create callout from MapView
				callout = mapView.getCallout();
				callout.setCoordinates(point);
				callout.setOffset(0, -3);
				callout.setStyle(R.xml.calloutstyle);
				// populate callout with results from IdentifyTask
				//callout.setContent(CalloutView(list, 0));
				// show callout
				callout.show();
			} else {
				callout.hide();
			}// end if
		}// end if
	}*/
	
	/*
	 * 从一个图层里里 查找获得 Graphics对象. x,y是屏幕坐标,layer
	 * 是GraphicsLayer目标图层（要查找的）。相差的距离是50像素。
	 */
	public Graphic GetGraphicsFromLayer(double xScreen, double yScreen, GraphicsLayer layer) {
		Graphic result = null;
		try {
			int[] idsArr = layer.getGraphicIDs();
			double x = xScreen;
			double y = yScreen;
			for (int i = 0; i < idsArr.length; i++) {
				Graphic gpVar = layer.getGraphic(idsArr[i]);
				if (gpVar != null) {
					Point pointVar = (Point) gpVar.getGeometry();
					pointVar = mapview.toScreenPoint(pointVar);
					double x1 = pointVar.getX();
					double y1 = pointVar.getY();
					if (Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1)) < 20) {
						result = gpVar;
						break;
					}
				}
			}
		} catch (Exception e) {
			return null;
		}
		return result;
	}
	
	/*
	 * 从一个图层里里 查找获得 Graphics对象. x,y是屏幕坐标,layer
	 * 是GraphicsLayer目标图层（要查找的）。相差的距离是50像素。
	 */
	public  Graphic GetGraphicsFromLayer(Point point, GraphicsLayer layer) {
		Graphic result = null;
		try {
			int[] idsArr = layer.getGraphicIDs();
			double x = point.getX();
			double y = point.getY();
			for (int i = 0; i < idsArr.length; i++) {
				Graphic gpVar = layer.getGraphic(idsArr[i]);
				if (gpVar != null) {
					Point pointVar = (Point) gpVar.getGeometry();
					//pointVar = mapview.toScreenPoint(pointVar);
					double x1 = pointVar.getX();
					double y1 = pointVar.getY();
					if (Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1)) < 50*mapview.getResolution()) {
						result = gpVar;
						this.endpoint=pointVar;
						break;
					}
				}
			}
		} catch (Exception e) {
			return null;
		}
		return result;
	}
	
	// callout 弹框
	public  ViewGroup createIdentifyContent(Map<String, Object> attibutes) {

			ViewGroup view = (ViewGroup) ViewGroup.inflate(mContext, R.layout.callout_identify, null);
			TextView content = (TextView) view.findViewById(R.id.callout_content);
			// 导航按钮
			Button navButton = (Button) view.findViewById(R.id.callout_nav);
			// 视频监控
			Button viewButton = (Button) view.findViewById(R.id.callout_view);
			// close callout
			ImageButton bt_close = (ImageButton) view.findViewById(R.id.bt_close);
			String dataString = "";
			for (Entry<String, Object> entry : attibutes.entrySet()) {
				
				dataString += entry.getKey() + ":" + entry.getValue() + "\n";
				

			}
			content.setText(dataString);
			//导航
			navButton.setOnClickListener(new OnClickListener() {

				@SuppressLint("ShowToast")
				@Override
				public void onClick(View v) {
					mapview.getCallout().hide();
					ToastUtil.setToast((Activity) mContext, "正在为您计算导航路线，请稍后");
					Point sPoint = (Point) GeometryEngine.project(startpoint, mapview.getSpatialReference(),
							SpatialReference.create(4326));
					Point epoint = (Point) GeometryEngine.project(endpoint, mapview.getSpatialReference(),
							SpatialReference.create(4326));
					BNRoutePlanNode sNode = new BNRoutePlanNode(sPoint.getX(), sPoint.getY(), "起点", null,
							CoordinateType.WGS84);
					BNRoutePlanNode eNode = new BNRoutePlanNode(epoint.getX(), epoint.getY(), "终点", null,
							CoordinateType.WGS84);
					baiduNavi.initListener(sNode, eNode, CoordinateType.WGS84, mBaiduRoutePlanListener);
					//Toast.makeText(mContext, "正在为您计算导航路线，请稍后", Toast.LENGTH_LONG);
				}
			});
			
		
			// callout close
			bt_close.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mapview.getCallout().hide();
					MapActivity.active = false;
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
