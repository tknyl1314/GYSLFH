package com.titan.gyslfh.main;
/**
 * Created by Whs on 2016/12/1 0001
 */

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.esri.arcgisruntime.geometry.Point;
import com.titan.data.source.local.GreenDaoManager;
import com.titan.data.source.remote.RetrofitHelper;
import com.titan.gyslfh.TitanApplication;
import com.titan.model.TrackPoint;
import com.titan.newslfh.R;
import com.titan.util.DateUtil;

import java.text.DecimalFormat;
import java.util.Date;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Whs on 2016/12/1 0001
 */
public class MainViewModel extends BaseObservable implements BDLocationListener {
    //用户等级
    public final ObservableField<Integer> userlevel = new ObservableField<>();
    //用户当前位置
    public final ObservableField<Point> currentPoint = new ObservableField<>();



    //提示信息
    final ObservableField<String> snackbarText = new ObservableField<>();

    public String getSnackbarText() {
        return snackbarText.get();
    }

    private Context mContext;
    public IMain mMain;

    /**经纬度格式化*/
    DecimalFormat locformat=new DecimalFormat(".000000");
    public MainViewModel(Context context, IMain mlogin) {
        this.mContext = context;
        this.mMain=mlogin;
        //地区等级 市级3，区县 4
        userlevel.set(Integer.valueOf(TitanApplication.mUserModel.getDqLevel()));

    }

    /**
     * 百度定位回调
     * @param bdLocation
     */
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        // TODO Auto-generated method stub
        if (null != bdLocation && bdLocation.getLocType() != BDLocation.TypeServerError) {

            /*Message locMsg = locHander.obtainMessage();
            Bundle locData;
            locData = Algorithm(location);
            if (locData != null) {
                locData.putParcelable("loc", location);
                locMsg.setData(locData);
                locHander.sendMessage(locMsg);
            } */
            /*bdLocation=location;

                currentPoint=new Point(bdLocation.getLongitude(),bdLocation.getLatitude());
                lastPoint=currentPoint;*/
                Point pt=new Point(bdLocation.getLongitude(),bdLocation.getLatitude());
                currentPoint.set(pt);
                uplaodTrackPoint(pt);



        }else {
            Toast.makeText(mContext, "定位失败", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 一键报警
     */
   /* public void onAlarm() {
        mMain.onAlarm();

    }*/

    /**
     * 接警信息
     */
    /*public void onAlarmInfo()
    {
        mMain.onAlarmInfo();
    }*/

    /**
     * 上传轨迹点
     */
    private void uplaodTrackPoint(Point point) {
        String uptime = DateUtil.dateFormat(new Date());
        //2363 :Xian_1980_3_Degree_GK_Zone_39
        if (point != null) {
            snackbarText.set("dajk");

            String lon = locformat.format(point.getX());
            String lat = locformat.format(point.getY());
            if (TitanApplication.IntetnetISVisible) {
               // snackbarText.set("有网络");
                upToServerDb(point);
            } else {
                //无网络轨迹存本地
                upToLocalDb(point,0);
            }
        }


    }

    /**
     * 上传轨迹到本地数据库
     * @param point
     */
    private void upToLocalDb(final Point point, final int status) {
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

            }
              }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Point>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        snackbarText.set(mContext.getString(R.string.error_uploc2server)+e);

                    }

                    @Override
                    public void onNext(Point point) {
                        snackbarText.set(mContext.getString(R.string.success_uploc));
                    }
                });

    }

    /**
     * 上传轨迹点到服务端
     */
    private void upToServerDb(final Point point) {
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


}
