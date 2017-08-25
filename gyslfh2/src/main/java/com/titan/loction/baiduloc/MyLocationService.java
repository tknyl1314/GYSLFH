package com.titan.loction.baiduloc;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.esri.arcgisruntime.location.AndroidLocationDataSource;
import com.titan.util.ToastUtil;

/**
 * Created by hanyw on 2017/8/25/025.
 * 自定义定位服务
 */

public class MyLocationService extends Service {
    //电源管理类
    private PowerManager pm;
    //电源管理锁
    private PowerManager.WakeLock wl;
    private final IBinder mBinder = new LocalBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        //阻止CPU在屏幕关闭情况下休眠
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "myservice");
        wl.acquire();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("tag","service");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wl.release();
    }

    public AndroidLocationDataSource initLocation(){
        AndroidLocationDataSource alds = new AndroidLocationDataSource(getBaseContext());
        try {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            //设置是否需要海拔信息
            criteria.setAltitudeRequired(false);
            //设置是否需要方位信息
            criteria.setBearingRequired(false);
            //设置是否允许运营商收费
            criteria.setCostAllowed(true);
            //设置对电源的需求
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            LocationManager locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
            //获取定位供应商名称
            String provider = locationManager.getBestProvider(criteria,true);
            Log.e("tag",provider);
            alds.startAsync();
            alds.requestLocationUpdates(provider,(long)1000,0.0f);
            //mLocationDisplay.setLocationDataSource(alds);
        }catch (Exception e){
            ToastUtil.setToast(getBaseContext(),"定位错误："+e.getMessage());
        }
        return alds;
    }

    public class LocalBinder extends Binder {
        public AndroidLocationDataSource getMyLocationService(){
            return initLocation();
        }
    }
}
