package com.titan.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by whs on 2018/4/24
 */
public class PermissionUtil {

    //视频会议所需的全部权限
    public  static final String[] videoRoomPermissions = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    //定位所需的全部权限
    public  static final String[] LoctionPermissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public static final int loctionRequestCode=102;

    public static final int videoRoomRequestCode=103;

    /**
     * 请求存储权限
     */
    public static boolean requestStoragePermission(Context context){
        return false;
        //return ContextCompat.checkSelfPermission(context,storagePermission[0])== PackageManager.PERMISSION_GRANTED
    }

    /**
     * 请求存储权限
     */
    public static boolean checkLoctionPermission(Context context){
        return ContextCompat.checkSelfPermission(context,LoctionPermissions[0])== PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(context,LoctionPermissions[1])==PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 请求存储权限
     */
    public static boolean checkVideoRoomPermission(Context context){
        return ContextCompat.checkSelfPermission(context,videoRoomPermissions[0])== PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(context,videoRoomPermissions[1])==PackageManager.PERMISSION_GRANTED;
    }


}
