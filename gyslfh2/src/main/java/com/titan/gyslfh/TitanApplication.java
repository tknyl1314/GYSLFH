package com.titan.gyslfh;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;

import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.titan.data.source.local.GreenDaoManager;
import com.titan.gyslfh.login.UserModel;
import com.titan.gyslfh.main.MainActivity;
import com.titan.loction.baiduloc.LocationService;
import com.titan.loction.baiduloc.MyLocationService;
import com.titan.model.FireInfo;
import com.titan.push.PushMsg;
import com.titan.util.DeviceUtil;
import com.titan.util.NetUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Whs on 2016/12/9 0009
 */
public class TitanApplication extends Application{

    //野狗视频id
    public static final String WILDDOG_VIDEO_ID = "wd0634562361hwiiyl";
    /** 是否有网络 */
    public static boolean IntetnetISVisible = false;
    /** 获取设备号序列号信息 */
    public static String SBH, XLH;
    public static String MOBILE_MODEL;

    /** 百度位置监听服务 */
    public static LocationService locationService;
    /** 自定义位置监听服务 */
    public static MyLocationService myLocationService;
    /** 震动器（百度定位） */
    Vibrator mVibrator;
    static Context mContext;
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
    private LinkedList<Activity> activityLinkedList = new LinkedList<Activity>();
    public static ActivityManager instance;
    /** 推送*/
    private static GTHandler handler;
    //推送消息接收界面
    public static MainActivity mainActivity;
    //透传数据
    public static String  payloadData;
    //推送ID
    public static String clientid;
    /** 用户信息 */
    public static final String PREFS_NAME = "MyPrefsFile";
    //是否记住用户信息
    public static final String KEYNAME_REMEMBER = "isremember";
    public static final String KEYNAME_USERNAME = "username";
    public static final String KEYNAME_PSD = "password";
    public static final String KEYNAME_ISTRACK = "istrack";
    public static final String KEYNAME_UPTRACKPOINT = "uptrackpoint";
    //网易云信账号
    public static final String IM_ACCOUNT = "imaccount";
    //网易云信登录token
    public static final String IM_TOKEN = "imtoken";


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
        //CrashReport.initCrashReport(getApplicationContext(), "900039321", false);
        /** 百度定位初始化 */
        locationService = new LocationService(getApplicationContext());
        //mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        /**野狗视频会议*/
        /*WilddogOptions options = new WilddogOptions.Builder().setSyncUrl("https://"+WILDDOG_VIDEO_ID+".wilddogio.com").build();
        WilddogApp.initializeApp(this,options);*/
        /**融云**/
        //RongIM.init(this);
        //RongCallKit.i


        //intiData();
        mSharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
        /** 获取当前网络状态 */
        getNetState();

        getDeviceInfo();
        //数据库初始化
        GreenDaoManager.getInstance(mContext);

        //registerDevice();
        if (handler == null) {
            handler = new GTHandler();
        }

    }





    /** 获取设备信息 */
    private void getDeviceInfo() {
        //设备号
        //华为p9:1dab2c6cdf044ee2538d46d629a6c4a4
        SBH= DeviceUtil.getUniqueId(mContext);
    }


    /**
     * 获取网络状态
     * @return
     */
    public boolean getNetState () {
        if (NetUtil.getNetworkState(mContext) == NetUtil.NETWORN_NONE)
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
        //直接用finish()并不能直接退出，虽然结束了activity但是并没有立即释放内存，遵循android内存管理机制；
        //exit()、killProcess()结束当前组件并立即释放内存
        try {
            for (Activity activity : activityLinkedList) {
                activity.finish();
            }
            //android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }catch (Exception e){
            e.printStackTrace();
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

    /**¥
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
                    //透传数据
                    clientid=msg.obj.toString().trim();
                    break;
            }
        }
    }

    /**
     * 网易云信
     * @return
     *//*
    // 如果返回值为 null，则全部使用默认参数。
    private SDKOptions options() {
        SDKOptions options = new SDKOptions();
        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = MainActivity.class; // 点击通知栏跳转到该Activity
        //config.notificationSmallIconId = R.drawable.ic_stat_notify_msg;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用采用默认路径作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        String sdkPath = getAppCacheDir(context) + "/nim"; // 可以不设置，那么将采用默认路径
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        //options.thumbnailSize = ${Screen.width} / 2;

        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        *//*options.userInfoProvider = new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return null;
            }

            @Override
            public int getDefaultIconResId() {
                return R.drawable.avatar_def;
            }

            @Override
            public Bitmap getTeamIcon(String tid) {
                return null;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(String account) {
                return null;
            }

            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId,
                                                           SessionTypeEnum sessionType) {
                return null;
            }
        };*//*
        return options;
    }

    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
    private LoginInfo loginInfo() {
        // 从本地读取上次登录成功时保存的用户登录信息
        String account =mSharedPreferences.getString(IM_ACCOUNT,"");
        String token = mSharedPreferences.getString(IM_TOKEN,"");

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            //DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }*/

    /**
     * 配置 APP 保存图片/语音/文件/log等数据的目录
     * 这里示例用SD卡的应用扩展存储目录
     */
    static String getAppCacheDir(Context context) {
        String storageRootPath = null;
        try {
            // SD卡应用扩展存储区(APP卸载后，该目录下被清除，用户也可以在设置界面中手动清除)，请根据APP对数据缓存的重要性及生命周期来决定是否采用此缓存目录.
            // 该存储区在API 19以上不需要写权限，即可配置 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="18"/>
            if (context.getExternalCacheDir() != null) {
                storageRootPath = context.getExternalCacheDir().getCanonicalPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(storageRootPath)) {
            // SD卡应用公共存储区(APP卸载后，该目录不会被清除，下载安装APP后，缓存数据依然可以被加载。SDK默认使用此目录)，该存储区域需要写权限!
            storageRootPath = Environment.getExternalStorageDirectory() + "/" + mContext.getPackageName();
        }

        return storageRootPath;
    }
}
