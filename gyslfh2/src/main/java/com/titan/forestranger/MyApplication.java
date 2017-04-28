package com.titan.forestranger;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;

import com.tencent.bugly.crashreport.CrashReport;
import com.titan.gyslfh.main.MainActivity;
import com.titan.loction.baiduloc.LocationService;
import com.titan.util.NetUtil;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Whs on 2016/12/9 0009
 */
public class MyApplication extends Application{
    /** 是否有网络 */
    public static boolean IntetnetISVisible = false;
    /** 获取设备号序列号信息 */
    public static String SBH, XLH;
    public static String MOBILE_MODEL;
    /** 百度位置监听服务 */
    public LocationService locationService;
    /** 震动器 */
    Vibrator mVibrator;
    Context mContext;
    String dbname="SMLY.sqlite";
    public  SharedPreferences sharedPreferences=null;
    public  static  MyApplication singleton;

    public static String getFilePath() {
        return filePath;
    }
    /** 数据存储路径 */
    static  String filePath = null;
    /** 是否首次定位 */
    boolean isfirstlogin=true;
    private LinkedList<Activity> activityLinkedList = new LinkedList<Activity>();
    public static ActivityManager instance;
    /** 推送*/
    private static GTHandler handler;
    public static MainActivity mainActivity;
    public static String  payloadData;//透传数据

    public static MyApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
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
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        //SDKInitializer.initialize(getApplicationContext());
        intiData();

        /** 获取当前网络状态 */
        getNetState();
        getDeviceInfo();
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

    public static class GTHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //接受到信息
                case 0:
                    if (mainActivity != null) {
                        payloadData=msg.obj.toString().trim();
                        if(mainActivity!=null){
                            if(MainActivity.tv_msg.getVisibility()==View.GONE){
                                MainActivity.tv_msg.setVisibility(View.VISIBLE);
                                MainActivity.tv_msg.setText(msg.obj.toString());
                            }


                            //MainActivity.tv_msg.append(msg.obj + "\n");
                        }

                      /*  payloadData.append((String) msg.obj);
                        payloadData.append("\n");
                        if (GetuiSdkDemoActivity.tLogView != null) {
                            GetuiSdkDemoActivity.tLogView.append(msg.obj + "\n");
                        }*/
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
