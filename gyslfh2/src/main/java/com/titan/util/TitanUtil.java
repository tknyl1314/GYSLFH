package com.titan.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

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

    /**
     * 文件路径转Base64
     */
    public static String path2Base64(String path) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[5024];
            int count = 0;
            while ((count = fis.read(buffer)) >= 0) {
                baos.write(buffer, 0, count);
            }
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }

    }



}
