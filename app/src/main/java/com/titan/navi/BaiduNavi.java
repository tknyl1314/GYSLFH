package com.titan.navi;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.NaviInitListener;
import com.otitan.gyslfh.activity.MapActivity;

import java.util.ArrayList;
import java.util.List;


public class BaiduNavi {
    Context mContext;
    View buttonView;
    BNRoutePlanNode spoint,epoint;
    String authinfo=null;
    CoordinateType mcoType;
    MapActivity.BaiduRoutePlanListener mBaiduRoutePlanListener;
    /**
	 * 百度导航初始化
	 */
	public void initNavi(Context context,String sdpath,String appname) {
		// BaiduNaviManager.getInstance().setNativeLibraryPath(mSDCardPath +
		// "/BaiduNaviSDK_SO");
		mContext=context;
		BNOuterTTSPlayerCallback ttsCallback = null;
		BaiduNaviManager.getInstance().init((Activity) mContext, sdpath, appname, new NaviInitListener() {
			@Override
			public void onAuthResult(int status, String msg) {
				if (0 == status) {
					authinfo = "key校验成功!";
				} else {
					//导航key校验失败
					authinfo = "导航key校验失败, " + msg;
					((Activity) mContext).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(mContext, authinfo, Toast.LENGTH_LONG).show();
						}
					});
				}
				
				/*((Activity) mContext).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(mContext, authinfo, Toast.LENGTH_LONG).show();
					}
				});*/
			}

			public void initSuccess() {
				//Toast.makeText(mContext, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
			}

			public void initStart() {
				//Toast.makeText(mContext, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
			}

			public void initFailed() {
				Toast.makeText(mContext, "导航引擎初始化失败", Toast.LENGTH_SHORT).show();
			}

		},  null, ttsHandler, ttsPlayStateListener  /* null mTTSCallback */);
	}
    public void initListener(BNRoutePlanNode sNode,BNRoutePlanNode eNode,CoordinateType coType, MapActivity.BaiduRoutePlanListener rp) {
		//buttonView=button;
		spoint=sNode;
		epoint=eNode;
		mcoType=coType;
		mBaiduRoutePlanListener=rp;
		if (BaiduNaviManager.isNaviInited()) {
			routeplanToNavi(mcoType,spoint,epoint);
		}
	/*	if (buttonView != null) {
			if (BaiduNaviManager.isNaviInited()) {
				routeplanToNavi(mcoType,spoint,epoint);
			}
		
		}*/
		/*if (buttonView != null) {
			buttonView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (BaiduNaviManager.isNaviInited()) {
						routeplanToNavi(CoordinateType.GCJ02,spoint,epoint);
					}
				}

			});
		}
		if (buttonView != null) {
			buttonView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					if (BaiduNaviManager.isNaviInited()) {
						routeplanToNavi(CoordinateType.BD09_MC,spoint,epoint);
					}
				}
			});
		}

		if (buttonView != null) {
			buttonView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (BaiduNaviManager.isNaviInited()) {
						routeplanToNavi(CoordinateType.BD09LL,spoint,epoint);
					}
				}
			});
		}*/


	}
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



	public void routeplanToNavi(CoordinateType coType,BNRoutePlanNode sNode,BNRoutePlanNode eNode) {
	/*	BNRoutePlanNode sNode = null;
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
				BaiduNaviManager.getInstance().launchNavigator((Activity) mContext, list, 1, true, mBaiduRoutePlanListener);
			}
	}
	/*public void initListener(BNRoutePlanNode sNode, BNRoutePlanNode eNode, CoordinateType wgs84,BaiduRoutePlanListener mBaiduRoutePlanListener2) {
		
	}*/
	public void initListener1(BNRoutePlanNode sNode, BNRoutePlanNode eNode, CoordinateType wgs84,MapActivity.BaiduRoutePlanListener baiduRoutePlanListener) {
		spoint=sNode;
		epoint=eNode;
		mcoType=wgs84;
		mBaiduRoutePlanListener=baiduRoutePlanListener;
		if (BaiduNaviManager.isNaviInited()) {
			routeplanToNavi(mcoType,spoint,epoint);
		}
	}
	
	
}
