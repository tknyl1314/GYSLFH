package com.titan.navi;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.widget.Toast;

import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.NaviInitListener;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.titan.gyslfh.main.MainActivity;
import com.titan.gyslfh.main.MainViewModel;
import com.titan.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class BaiduNavi {

	private String mSDCardPath = null;
	private static final String APP_FOLDER_NAME = "TitanNav";

	public boolean isHasInitSuccess() {
		return hasInitSuccess;
	}

	//是否初始化
	private boolean hasInitSuccess = false;
    //是否获取权限
    private boolean hasRequestComAuth = false;

    public static final String ROUTE_PLAN_NODE = "routePlanNode";

    public MapViewOnTouchListener mapViewOnTouchListener;





    public static final String[] authBaseArr = { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION };
	public static final String[] authComArr = { Manifest.permission.READ_PHONE_STATE};
	public static final int authBaseRequestCode = 1;
	public static final int authComRequestCode = 2;

    private Context mContext;
    private MapView mapView;

    public BNRoutePlanNode getsNode() {
        return sNode;
    }

    public void setsNode(BNRoutePlanNode sNode) {
        this.sNode = sNode;
    }

    //起点
    private BNRoutePlanNode sNode;
    /*View buttonView;
    BNRoutePlanNode spoint,epoint;*/
    String authinfo=null;
    CoordinateType mcoType;
    //计算路径会回调
    BaiduNaviManager.RoutePlanListener mBaiduRoutePlanListener;
    private CoordinateType mCoordinateType;

    private MainViewModel mViewModel;

    public BaiduNavi(Context mContext, MapView mapView, MainViewModel viewModel) {
		this.mContext = mContext;
        this.mapView=mapView;
        mapViewOnTouchListener=new MapViewOnTouchListener(mContext,mapView);
        mViewModel=viewModel;
        //mBaiduRoutePlanListener=new RoutePlanListener();
		//initDirs();
	}

	/**
	 * 百度导航初始化
	 */
	public void initNavi() {



		BNOuterTTSPlayerCallback ttsCallback = null;

		BaiduNaviManager.getInstance().init((Activity) mContext, mSDCardPath, APP_FOLDER_NAME, new NaviInitListener() {
			@Override
			public void onAuthResult(int status, String msg) {
				if (0 == status) {
					authinfo = "key校验成功!";
				} else {
					authinfo = "key校验失败, " + msg;
					ToastUtil.setToast(mContext,authinfo);

				}
				/*BNDemoMainActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(BNDemoMainActivity.this, authinfo, Toast.LENGTH_LONG).show();
					}
				});*/
			}

			public void initSuccess() {

				//Toast.makeText(BNDemoMainActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
				hasInitSuccess = true;
				initSetting();
			}

			public void initStart() {
				//Toast.makeText(BNDemoMainActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
			}

			public void initFailed() {
				ToastUtil.setToast(mContext,"导航引擎初始化失败");
				//Toast.makeText(BNDemoMainActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
			}

		}, null, ttsHandler, ttsPlayStateListener);
	}

	private void initSetting() {
		// BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
		BNaviSettingManager
				.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
		BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
		// BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
		BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
		Bundle bundle = new Bundle();
		// 必须设置APPID，否则会静音
		bundle.putString(BNCommonSettingParam.TTS_APP_ID, "9354030");
		BNaviSettingManager.setNaviSdkParam(bundle);
	}
    /*public void initListener(BNRoutePlanNode sNode,BNRoutePlanNode eNode,CoordinateType coType, BaiduNaviManager.RoutePlanListener routePlanListener) {
		//buttonView=button;
		spoint=sNode;
		epoint=eNode;
		mcoType=coType;
		mBaiduRoutePlanListener=routePlanListener;
		if (BaiduNaviManager.isNaviInited()) {
			routeplanToNavi(mcoType,spoint,epoint);
		}



	}*/
	/**
	 * 内部TTS播报状态回传handler
	 */
	private Handler ttsHandler = new Handler() {
		public void handleMessage(Message msg) {
			int type = msg.what;
			switch (type) {
				case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
					//showToastMsg("Handler : TTS play start");
					break;
				}
				case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
					//showToastMsg("Handler : TTS play end");
					break;
				}
				default :
					break;
			}
		}
	};

	/**
	 * 内部TTS播报状态回调接口
	 */
	private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

		@Override
		public void playEnd() {
			showToastMsg("TTSPlayStateListener : TTS play end");
		}

		@Override
		public void playStart() {
			showToastMsg("TTSPlayStateListener : TTS play start");
		}
	};

	public void showToastMsg(final String msg) {
		Toast.makeText(mContext, "导航引擎初始化失败"+msg, Toast.LENGTH_SHORT).show();
		/*BNDemoMainActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(BNDemoMainActivity.this, msg, Toast.LENGTH_SHORT).show();
			}
		});*/
	}


    /**
     * 计算路径
     * @param coType
     * @param sNode
     * @param eNode
     */
    public void routeplanToNavi(CoordinateType coType, BNRoutePlanNode sNode, BNRoutePlanNode eNode) {
        CoordinateType cotype=CoordinateType.WGS84;
        mCoordinateType = coType;
        if (!hasInitSuccess) {
            Toast.makeText(mContext, "还未初始化!", Toast.LENGTH_SHORT).show();

        }
        // 权限申请
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // 保证导航功能完备
            if (!hasCompletePhoneAuth(mContext)) {
                if (!hasRequestComAuth) {
                    /*hasRequestComAuth = true;
                    this.requestPermissions(authComArr, authComRequestCode);
                    return;*/
                } else {
                    Toast.makeText(mContext, "没有完备的权限!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        /*BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;*/
        /*switch (coType) {
            case GCJ02: {
                sNode = new BNRoutePlanNode(116.30142, 40.05087, "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(116.39750, 39.90882, "北京天安门", null, coType);
                break;
            }
            case WGS84: {
                sNode = new BNRoutePlanNode(116.300821, 40.050969, "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(116.397491, 39.908749, "北京天安门", null, coType);
                break;
            }
            case BD09_MC: {
                sNode = new BNRoutePlanNode(12947471, 4846474, "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(12958160, 4825947, "北京天安门", null, coType);
                break;
            }
            case BD09LL: {
                sNode = new BNRoutePlanNode(116.30784537597782, 40.057009624099436, "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(116.40386525193937, 39.915160800132085, "北京天安门", null, coType);
                break;
            }
            default:
                ;
        }*/
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);

            BaiduNaviManager.getInstance().launchNavigator((Activity) mContext, list, 1, true, new RoutePlanListener(sNode));
        }
    }

	/*public void initListener1(BNRoutePlanNode sNode, BNRoutePlanNode eNode, CoordinateType wgs84,BaiduNaviManager.RoutePlanListener routePlanListener) {
		spoint=sNode;
		epoint=eNode;
		mcoType=wgs84;
		mBaiduRoutePlanListener=routePlanListener;
		if (BaiduNaviManager.isNaviInited()) {
			routeplanToNavi(mcoType,spoint,epoint);
		}
	}*/
    public  boolean hasBasePhoneAuth(Context mContext) {
        // TODO Auto-generated method stub

        PackageManager pm = mContext.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, mContext.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    public  boolean hasCompletePhoneAuth(Context mContext) {
        // TODO Auto-generated method stub

        PackageManager pm = mContext.getPackageManager();
        for (String auth : authComArr) {
            if (pm.checkPermission(auth, mContext.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

	/**
	 *
	 * 初始化路径
	 * @return
	 */
	public boolean initDirs() {
		mSDCardPath = getSdcardDir();
		if (mSDCardPath == null) {
			return false;
		}
		File f = new File(mSDCardPath, APP_FOLDER_NAME);
		if (!f.exists()) {
			try {
				f.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

    private class RoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public RoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            /*
             * 设置途径点以及resetEndNode会回调该接口
             */

            /*for (Activity ac : activityList) {

                if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {

                    return;
                }
            }*/
            Intent intent = new Intent(mContext, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(BaiduNavi.ROUTE_PLAN_NODE, mBNRoutePlanNode);
            intent.putExtras(bundle);
            MainActivity dd= (MainActivity) mContext;
            dd.getmViewModel().startNavigation();
            mContext.startActivity(intent);

        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            Toast.makeText(mContext, "计算路径失败", Toast.LENGTH_SHORT).show();
        }
    }


    private class MapViewOnTouchListener extends DefaultMapViewOnTouchListener {
        MapView mMapView;
        Context mContext;

        public MapViewOnTouchListener(Context context, MapView mapView) {
            super(context, mapView);
            mMapView=mapView;
            mContext=context;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            Point point=mMapView.screenToLocation(new android.graphics.Point(Math.round(event.getX()), Math.round(event.getY())));
            Point point1= (Point) GeometryEngine.project(point, SpatialReferences.getWgs84());
            BNRoutePlanNode eNode=new BNRoutePlanNode(point1.getX(),point1.getY(),"终点",null,CoordinateType.WGS84);
            routeplanToNavi(CoordinateType.WGS84,sNode,eNode);
            return super.onSingleTapConfirmed(event);
        }

    }
}
