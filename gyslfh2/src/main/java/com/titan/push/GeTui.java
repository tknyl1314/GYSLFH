package com.titan.push;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.igexin.sdk.PushManager;

/**
 * Created by Whs on 2016/12/20 0020
 */
public class GeTui {
    public static String appkey = "";
    public static String appsecret = "";
    public static String appid = "";

    public static int GTrequestCode = 0;
    Context mContext;

    /**个推需要动态获取的权限*/
    public static String[] GTreqPermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};

    public GeTui() {
    }
    public GeTui(Context context) {
        this.mContext=context;
    }

    public String[] getGTreqPermission() {
        return GTreqPermission;
    }

    public void setGTreqPermission(String[] GTreqPermission) {
        this.GTreqPermission = GTreqPermission;
    }
    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    /**
     * 初始化
     * @param context
     */
    public void intial(Context context){
        // 注册 intentService 后 PushDemoReceiver 无效, sdk 会使用 DemoIntentService 传递数据,
        // AndroidManifest 对应保留一个即可(如果注册 DemoIntentService, 可以去掉 PushDemoReceiver, 如果注册了
        // IntentService, 必须在 AndroidManifest 中声明)
        PushManager.getInstance().registerPushIntentService(context.getApplicationContext(), GeTuiIntentService.class);
    }
    /**
     * 获取配置参数
     * @param context
     */
    private void parseManifests(Context context) {
        String packageName = context.getApplicationContext().getPackageName();
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                appid = appInfo.metaData.getString("PUSH_APPID");
                appsecret = appInfo.metaData.getString("PUSH_APPSECRET");
                appkey = appInfo.metaData.getString("PUSH_APPKEY");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
