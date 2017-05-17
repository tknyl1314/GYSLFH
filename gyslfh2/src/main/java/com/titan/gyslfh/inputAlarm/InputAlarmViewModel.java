package com.titan.gyslfh.inputAlarm;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.otitan.timepicke.TimePopupWindow;
import com.titan.data.source.remote.RetrofitHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by li on 2017/4/28.
 */

public class InputAlarmViewModel extends BaseObservable{

    private Context mContext;
    private InputAlarmView alarmView;
    private InputAlarm inputAlarm;

    public InputAlarmViewModel(Context ctx,InputAlarmView inputAlarmView,InputAlarm inputAlarm){
        this.mContext = ctx;
        this.alarmView = inputAlarmView;
        this.inputAlarm = inputAlarm;
    }

    /** 时间选择popupwindow */
    public void showTimePopuwindow(final TextView button, final boolean isBefore) {
        TimePopupWindow timePopupWindow = new TimePopupWindow(mContext, TimePopupWindow.Type.ALL);
        timePopupWindow.setCyclic(true);
        // 时间选择后回调
        timePopupWindow.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                button.setText(format.format(date));
            }
        });
        timePopupWindow.showAtLocation(button, Gravity.CENTER, 0, 0,new Date(), isBefore);
    }


    /**t提交数据到服务器*/
    public void sendMesToServer(){
        Gson gson = new Gson();
        String json = gson.toJson(inputAlarm);
        Observable<String> observable = RetrofitHelper.getInstance(mContext).getServer().sendInputAlarmInfo(json);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

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
