package com.otitan.gyslfh.activity;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Vibrator;
import android.provider.Settings.Secure;
import android.support.multidex.MultiDexApplication;

import com.igexin.sdk.PushManager;
import com.otitan.DataBaseHelper;
import com.otitan.util.NetUtil;
import com.otitan.util.ResourcesManager;
import com.otitan.util.ScreenTool;
import com.otitan.util.WebServiceUtil;
import com.tencent.bugly.crashreport.CrashReport;
import com.titan.loction.baiduloc.LocationService;

public class MyApplication extends MultiDexApplication

	{
		public static int mNetWorkState;
		/** 是否有网络 */
		public static boolean IntetnetISVisible = false;
		/** 获取设备号序列号信息 */
		public static String SBH, XLH;

		public static String MOBILE_MODEL, MOBILE_MANUFACTURER;
		/** 设备屏幕信息 */
		public static ScreenTool.Screen screen;
		/** 移动设备唯一号 */
		//public static String macAddress;
		/** 用于查询的图层url */
		//public static String featureurl;
		//public static FeatureLayer queryfeature;

		public  static  SharedPreferences sharedPreferences;
		//屏幕宽高
		int window_width,window_height;
		Context mcontext;

	/* 注册网络 */
		//private ConnectionChangeReceiver mNetworkStateReceiver;
		//private Logger log;
		/** 百度位置监听服务 */
		public LocationService locationService;
		/** 震动器 */
		Vibrator mVibrator;
		@Override
		public void onCreate()
		{
			super.onCreate();

		/** Bugly SDK初始化
	        * 参数1：上下文对象
	        * 参数2：APPID，平台注册时得到,注意替换成你的appId
	        * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
	        * 发布新版本时需要修改以及bugly isbug需要改成false等部分
	        */
			CrashReport.initCrashReport(getApplicationContext(), "900039321", true);

			/** 百度定位初始化 */
			locationService = new LocationService(getApplicationContext());

			mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
			//SDKInitializer.initialize(getApplicationContext());
			mcontext=this.getApplicationContext();
			//个推推送初始化
			PushManager.getInstance().initialize(this.getApplicationContext());
		/* 注册网络监听 */
			//initNetworkReceiver();
			/** 获取当前网络状态 */
			getNetState();
			getDeviceInfo();
			new Thread(new Runnable() {

				@Override
				public void run() {
					initData();
					/** 获取屏幕分辨率 */
					screen = ScreenTool.getScreenPix(mcontext);
					/*if (IntetnetISVisible)
					{
						registerSBH();
						//在有网络情况下上传本地的历史轨迹
					}*/
				}
			}).start();
		}
		/** 获取设备信息 */
		private void getDeviceInfo() {
            // 用户信息存储
            sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
			// 获取唯一识表示 mac地址
			WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			if (!wifiMgr.isWifiEnabled())
			{
				wifiMgr.setWifiEnabled(true);
			}
			WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
			if (null != info)
			{
				SBH = info.getMacAddress();
				sharedPreferences.edit().putString("SBH", SBH).apply();
			}
		}
		/** 注册用户设备信息*/
	public void registerSBH()
	{
		// 用户信息存储
		sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
		//初始化系统设置参数
		//sharedPreferences.edit().putInt("time", 1000).commit();
		//sharedPreferences.edit().putInt("distance", 100).commit();
		//是否标绘轨迹
		//sharedPreferences.edit().putBoolean("zongji", false).commit();
		//是否上传轨迹
		//sharedPreferences.edit().putBoolean("guiji", false).commit();
		// 获取唯一识表示 mac地址
		WifiManager wifiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (!wifiMgr.isWifiEnabled())
		{
			wifiMgr.setWifiEnabled(true);
		}
		WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
		if (null != info)
		{
			SBH = info.getMacAddress();
			sharedPreferences.edit().putString("SBH", SBH).apply();
		} else
		{
			SBH = sharedPreferences.getString("SBH", "");
		}
		XLH = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
		// MOBILE_MODEL = android.os.Build.MODEL;// SM-P601 型号
		// MOBILE_MANUFACTURER = android.os.Build.MANUFACTURER;// samsung 厂商
		MOBILE_MODEL = android.os.Build.MODEL + "-"
				+ android.os.Build.MANUFACTURER;
		WebServiceUtil websUtil = new WebServiceUtil(mcontext);
		String result = websUtil.addMacAddress(SBH, XLH, MOBILE_MODEL);
		if (result.equals(WebServiceUtil.netException))
		{
			// 网络异常
			sharedPreferences.edit().putBoolean(SBH, false).apply();
		} else if (result.equals("已录入"))
		{
			// 设备信息已经录入
			sharedPreferences.edit().putBoolean(SBH, true).apply();
		} else if (result.equals("录入成功"))
		{
			sharedPreferences.edit().putBoolean(SBH, true).apply();
			// 设备信息录入成功
		} else if (result.equals("录入失败"))
		{
			// 设备信息录入失败
			sharedPreferences.edit().putBoolean(SBH, false).apply();
		}
	}

	/** 初始化本地数据*/
	public void initData()
	{
		//mNetWorkState = NetUtil.getNetworkState(this);
		ResourcesManager manager;
		try
		{
			manager = ResourcesManager.getInstance(mcontext);
			manager.createFolder();// 创建相关文件夹
			// 初始化轨迹存储表
			String path = ResourcesManager.getDataPath(ResourcesManager.sqlite) ;
			/*if (!DataBaseHelper.checkDataBase(path, "guiji.sqlite"))
			{
				DataBaseHelper.CopyDatabase(MyApplication.this,path, "guiji.sqlite");
			}*/
			//检查小地名数据库
			if (!DataBaseHelper.checkDataBase(path, "db.sqlite"))
			{
				DataBaseHelper.CopyDatabase(MyApplication.this,path, "db.sqlite");
			}
			//检查本地数据库
			if (!DataBaseHelper.checkDataBase(path, "GYSLFH.sqlite"))
			{
				DataBaseHelper.CopyDatabase(MyApplication.this,path, "GYSLFH.sqlite");
			}

			//检查空间数据库
			/*if (!DataBaseHelper.checkDataBase(path, "gy.geodatabase"))
			{

				DataBaseHelper.CopyDatabase(MyApplication.this,path, "gy.geodatabase");
				String geodatabaseName =  ResourcesManager.getDataBase("gy.geodatabase");
				queryfeature=GeodatabaseHelper.loadGeodatabase(geodatabaseName);

			}else{
				String geodatabaseName =  ResourcesManager.getDataBase("gy.geodatabase");
				queryfeature=GeodatabaseHelper.loadGeodatabase(geodatabaseName);

			}*/
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

		/**
		 * 检查权限
		 */
		/*private void getRequeatPermission() {
			// If an error is found, handle the failure to start.
			// Check permissions to see if failure may be due to lack of permissions.
			boolean permissionCheck1 = ContextCompat.checkSelfPermission(getApplicationContext(), reqPermissions[0]) ==
					PackageManager.PERMISSION_GRANTED;
			boolean permissionCheck2 = ContextCompat.checkSelfPermission(getApplicationContext, reqPermissions[1]) ==
					PackageManager.PERMISSION_GRANTED;

			if (Build.VERSION.SDK_INT >= 23&&!(permissionCheck1 && permissionCheck2)) {
				// If permissions are not already granted, request permission from the user.
				int requestCode = 3;
				ActivityCompat.requestPermissions((Activity) mContext, reqPermissions, requestCode);
			}
		}*/

	public boolean getNetState () {
		if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE)
		{
			IntetnetISVisible = false;
		} else
		{
			IntetnetISVisible = true;
		}
		return IntetnetISVisible;
	}
}
