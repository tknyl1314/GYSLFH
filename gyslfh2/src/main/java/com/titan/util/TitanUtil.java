package com.titan.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by whs on 2017/5/3
 */

public class TitanUtil {
    /**
     * 获取当前软件版本号
     */
    public static double getVersionCode(Context mContext)  {
        double versionCode = 0;
        String versionName;
        try {
            versionCode = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0).versionCode;//0代表是获取版本信息
            //versionCode = Double.parseDouble(versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;


    }
}
