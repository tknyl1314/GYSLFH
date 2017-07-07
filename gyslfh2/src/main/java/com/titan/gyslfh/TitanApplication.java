package com.titan.gyslfh;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.tencent.bugly.crashreport.CrashReport;
import com.titan.data.source.local.GreenDaoManager;
import com.titan.gyslfh.login.UserModel;
import com.titan.gyslfh.main.MainActivity;
import com.titan.loction.baiduloc.LocationService;
import com.titan.model.FireInfo;
import com.titan.push.PushMsg;
import com.titan.util.NetUtil;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Whs on 2016/12/9 0009
 */
public class TitanApplication extends Application{
    /** 是否有网络 */
    public static boolean IntetnetISVisible = false;
    /** 获取设备号序列号信息 */
    public static String SBH, XLH;
    public static String MOBILE_MODEL;

    public LocationService getLocationService() {
        return locationService;
    }

    /** 百度位置监听服务 */
    public static LocationService locationService;
    /** 震动器（百度定位） */
    Vibrator mVibrator;
    static Context mContext;
    String dbname="SMLY.sqlite";
    public  SharedPreferences sharedPreferences=null;
    public  static  TitanApplication singleton;

    public static UserModel getmUserModel() {
        return mUserModel;
    }

    public static void setmUserModel(UserModel mUserModel) {
        TitanApplication.mUserModel = mUserModel;
    }

    public static UserModel mUserModel;
    /** 数据存储路径 */
    static  String filePath = null;
    /** 是否首次定位 */
    boolean isfirstlogin=true;
    private LinkedList<Activity> activityLinkedList = new LinkedList<Activity>();
    public static ActivityManager instance;
    /** 推送*/
    private static GTHandler handler;
    //推送消息接收界面
    public static MainActivity mainActivity;
    //透传数据
    public static String  payloadData;
    /** 用户信息 */
    public static final String PREFS_NAME = "MyPrefsFile";
    //是否记住用户信息
    public static final String KEYNAME_REMEMBER = "isremember";
    public static final String KEYNAME_USERNAME = "username";
    public static final String KEYNAME_PSD = "password";
    public static SharedPreferences mSharedPreferences;

    public static TitanApplication getInstance() {
        if(singleton!=null){
            return singleton;
        }else {
            return new TitanApplication();
        }

    }
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this.getApplicationContext();
        singleton=this;
        /** Bugly SDK初始化
         * 参数1：上下文对象
         * 参数2：APPID，平台注册时得到,注意替换成你的appId
         * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
         * 发布新版本时需要修改以及bugly isbug需要改成false等部分
         */
        CrashReport.initCrashReport(getApplicationContext(), "900039321", false);
        /** 百度定位初始化 */
        locationService = new LocationService(getApplicationContext());
        //mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        //intiData();
        mSharedPreferences=getSharedPreferences(PREFS_NAME,0);
        /** 获取当前网络状态 */
        getNetState();
        getDeviceInfo();
        //数据库初始化
        GreenDaoManager.getInstance(this);

        //registerDevice();
        if (handler == null) {
            handler = new GTHandler();
        }
    }






    /**
     * 初始化数据
     */
    private void intiData() {

        // 用户信息存储
        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        //是否记录轨迹
        sharedPreferences.edit().putBoolean("istrack",false).apply();
        filePath= mContext.getFilesDir().getAbsolutePath();
        //检查本地数据库
        /*if (!DataBaseHelper.checkDataBase(filePath, dbname))
        {
            DataBaseHelper.CopyDatabase(mContext,filePath, dbname);
        }*/
    }
    /** 获取设备信息 */
    private void getDeviceInfo() {
        XLH = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
         TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        //SBH= tm.getDeviceId();
        //phoneNumber = tm.getLine1Number();//获取本机号码
        //sharedPreferences.edit().putString("phonenumber",phoneNumber).apply();
        // 用户信息存储
        //sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
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
            //sharedPreferences.edit().putString("SBH", SBH).commit();
        }
        MOBILE_MODEL = android.os.Build.MODEL;// SM-P601 型号
        // MOBILE_MANUFACTURER = android.os.Build.MANUFACTURER;// samsung 厂商
        MOBILE_MODEL = android.os.Build.MODEL + "-"
                + android.os.Build.MANUFACTURER;

    }

    /** 注册用户设备信息*/
    public void registerDevice()
    {
         MOBILE_MODEL = android.os.Build.MODEL;// SM-P601 型号
        // MOBILE_MANUFACTURER = android.os.Build.MANUFACTURER;// samsung 厂商
        MOBILE_MODEL = android.os.Build.MODEL + "-"
                + android.os.Build.MANUFACTURER;
        //WebService websUtil = new WebService(mContext);
        /*String result = websUtil.addMacAddress(SBH, XLH, MOBILE_MODEL);
        if (result.equals(WebService.netException))
        {
            // 网络异常
            //sharedPreferences.edit().putBoolean(SBH, false).commit();
        } else if (result.equals("已录入"))
        {
            // 设备信息已经录入
            //sharedPreferences.edit().putBoolean(SBH, true).commit();
        } else if (result.equals("录入成功"))
        {
           // sharedPreferences.edit().putBoolean(SBH, true).commit();
            // 设备信息录入成功
        } else if (result.equals("录入失败"))
        {
            // 设备信息录入失败
          //  sharedPreferences.edit().putBoolean(SBH, false).commit();
        }*/
    }

    /**
     * 获取网络状态
     * @return
     */
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



    //结束所有的Activities
    public void   finshAllActivities() {
        for (Activity activity : activityLinkedList) {
            activity.finish();
        }
    }

    //向list中添加Activity
    public void addActivity(Activity activity){
        activityLinkedList.add(activity);
    }

    //结束特定的Activity(s)
    public void finshActivities(Class<? extends Activity>... activityClasses) {
        for (Activity activity : activityLinkedList) {
            if (Arrays.asList(activityClasses).contains(activity.getClass())) {
                activity.finish();
            }
        }
    }

    public static void sendMessage(Message msg) {
        handler.sendMessage(msg);
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * 个推Handler
     */
    public static class GTHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //接受到信息
                case 0:
                    if (mainActivity != null) {
                        //透传数据
                        payloadData=msg.obj.toString().trim();
                        if(mainActivity!=null){
                            PushMsg<FireInfo> pushMsg=new Gson().fromJson(payloadData, PushMsg.class);
                            mainActivity.mainFragment.showFirePt(pushMsg.getCONTENT());
                            /*if(MainActivity.tv_msg.getVisibility()==View.GONE){
                                MainActivity.tv_msg.setVisibility(View.VISIBLE);
                                MainActivity.tv_msg.setText(msg.obj.toString());
                            }*/


                            //MainActivity.tv_msg.append(msg.obj + "\n");
                        }
                    }
                    break;
                //获取clientid
                case 1:
                    if (mainActivity != null) {
                       /* if (GetuiSdkDemoActivity.tLogView != null) {
                            GetuiSdkDemoActivity.tView.setText((String) msg.obj);
                        }*/
                        //MainActivity.tv_msg.append(msg.obj + "1\n");
                    }
                    break;
            }
        }
    }
}
