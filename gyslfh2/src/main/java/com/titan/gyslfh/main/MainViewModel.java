package com.titan.gyslfh.main;
/**
 * Created by Whs on 2016/12/1 0001
 */

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.esri.arcgisruntime.geometry.Point;
import com.titan.data.source.DataRepository;
import com.titan.data.source.DataSource;
import com.titan.gis.TrackUtil;
import com.titan.gyslfh.TitanApplication;
import com.titan.gyslfh.layercontrol.ILayerControl;
import com.titan.model.TrackPoint;
import com.titan.util.DateUtil;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by Whs on 2016/12/1 0001
 */
public class MainViewModel extends BaseObservable implements BDLocationListener {

    public ObservableBoolean isplot=new ObservableBoolean(false);
    //用户等级
    public final ObservableField<Integer> userlevel = new ObservableField<>();
    //用户当前位置
    public static final ObservableField<Point> currentPoint = new ObservableField<>();
    //是否跟踪轨迹
    public final ObservableField<Boolean> istrack = new ObservableField<>();

    public final ObservableList<Point> listpt = new ObservableArrayList<>();



    //提示信息
    final ObservableField<String> snackbarText = new ObservableField<>();


    public String getSnackbarText() {
        return snackbarText.get();
    }

    private Context mContext;
    private IMain mMain;
    private ILayerControl mLayerControl;

    private DataRepository mDataRepository;


    private int i=0;

    /**经纬度格式化*/
    DecimalFormat locformat=new DecimalFormat(".000000");
    public MainViewModel(Context context,DataRepository dataRepository, IMain mlogin, ILayerControl iLayerControl) {
        this.mContext = context;
        this.mMain=mlogin;
        this.mLayerControl=iLayerControl;
        this.mDataRepository=dataRepository;


        try {

            //地区等级 市级3，区县 4
            userlevel.set(Integer.valueOf(TitanApplication.mUserModel.getDqLevel()));
            //是否跟踪轨迹
            istrack.set(TitanApplication.mSharedPreferences.getBoolean("istrack",false));

        } catch (Exception e) {
            snackbarText.set("获取用户信息异常"+e.toString());

        }



    }

    /**
     * 一键报警
     */
    public void onAlarm(){
        mMain.onAlarm();
    }

    /**
     * 接警信息
     */
    public void onAlarmInfo(){
        mMain.onAlarmInfo();
    }

    /**
     * 定位到当前位置
     */
    public void onLocation(){
        mMain.onLocation(currentPoint.get());
    }
    /**
     * 回警
     */
    public void onBackAlarm(){
        mMain.onBackAlarm();
    }

    /**
     * 图层控制
     */
    public void showLayerDialog(){
        mLayerControl.showLayerControl(true);
        //mMain.onLocation(currentPoint.get());
    }

    /**
     * 开启态势标绘
     */
    public void startPlot(){
        if(isplot.get()){
            snackbarText.set("态势标绘已关闭");
            isplot.set(false);
        }else {
            snackbarText.set("态势标绘已开启");
            isplot.set(true);
        }
        mMain.Plot(isplot.get());

        //mLayerControl.startPlot();
    }
    /**
     * 开启导航
     */
    public void startNavigation(){

       mMain.test();
    }





    /**
     * 百度定位回调
     * @param bdLocation
     */
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        // TODO Auto-generated method stub
        if (null != bdLocation && bdLocation.getLocType() != BDLocation.TypeServerError) {


            //Message locMsg = locHander.obtainMessage();
            Bundle locData;
            locData = TrackUtil.Algorithm(bdLocation);
            Point pt=new Point(bdLocation.getLongitude(),bdLocation.getLatitude());
            currentPoint.set(pt);

           if (locData != null) {
                //BDLocation location=locData.getParcelable("loc");
                int iscalculate=locData.getInt("iscalculate");
                if(iscalculate==1){

                    uplaodTrackPoint(pt);
                    //是否需要跟踪轨迹
                    if(istrack.get()){
                        listpt.add(pt);
                        //展示轨迹
                        //mMain.showTrackLine(listpt);
                    }else {
                        listpt.clear();
                        //移除轨迹
                        //mMain.removeTrackLine();
                    }
                }
            }



        }else {
            Log.e("TITAN",bdLocation.getLocTypeDescription()+bdLocation.getLocType());
            Toast.makeText(mContext, "定位失败"+bdLocation.getLocTypeDescription(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }


    /**
     * 上传轨迹点
     */
    private void uplaodTrackPoint (Point point) {
        String uptime = DateUtil.dateFormat(new Date());
        //2363 :Xian_1980_3_Degree_GK_Zone_39
        if (point != null) {
            //snackbarText.set("dajk");
            String time = DateUtil.dateFormat(new Date());
            String userid=TitanApplication.mUserModel.getUserID();
            String lon = locformat.format(point.getX());
            String lat = locformat.format(point.getY());
            TrackPoint trackPoint=new TrackPoint();

            trackPoint.setLat(Double.parseDouble(lat));
            trackPoint.setLon(Double.parseDouble(lon));
            trackPoint.setTime(time);
            trackPoint.setUserid(userid);
            trackPoint.setSbh(TitanApplication.SBH);
            if(i==0){
                trackPoint.setTag(1);

            }else {
                trackPoint.setTag(0);
            }
            trackPoint.setREMARK("");
            //GCS_WGS_1984  102100
            trackPoint.setWkid("4326");
            i++;

            mDataRepository.saveTrackPoint(trackPoint, new DataSource.saveCallback() {
                @Override
                public void onFailure(String info) {
                    snackbarText.set(info);
                }

                @Override
                public void onSuccess(String data) {
                    Log.e("TITAN",data);
                    snackbarText.set(data);
                }
            });

        }


    }

    /**
     * 上传轨迹到本地数据库
     * @param point
     */
   /* private void upToLocalDb(final Point point, final int status) {
        Observable.create(new Observable.OnSubscribe<Point>() {
            @Override
            public void call(Subscriber<? super Point> subscriber) {
                String time = DateUtil.dateFormat(new Date());
                String userid=TitanApplication.mUserModel.getUserID();
                TrackPoint tp= new TrackPoint(null,time,point.getX(),point.getY(),userid,status);
                long d=GreenDaoManager.getInstance().getNewSession().getTrackPointDao().insert(tp);
                //snackbarText.set(d+"");
                subscriber.onNext(point);
                subscriber.onCompleted();
                //subscriber.onError();

            }}).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Point>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TITAN",e.toString());
                        snackbarText.set(mContext.getString(R.string.error_uploc2server)+e);

                    }

                    @Override
                    public void onNext(Point point) {
                        snackbarText.set(mContext.getString(R.string.success_uploc));
                    }
                });

    }*/

    /**
     * 上传轨迹点到服务端
     */
  /*  private void upToServerDb(final Point point) {
        Observable<String> observable=RetrofitHelper.getInstance(mContext).getServer().upLoadTrackPoint("");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        upToLocalDb(point,1);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {

                    }
                });

    }
*/

    /**
     *
     */
    public void showTrackLine() {
    }
}
