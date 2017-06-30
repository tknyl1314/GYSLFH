package com.titan.util;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by whs on 2017/6/7
 * 设备信息获取工具类
 */

public class DeviceUtil {
    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
