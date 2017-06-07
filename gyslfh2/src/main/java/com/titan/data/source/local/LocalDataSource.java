/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.titan.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.titan.data.source.DataSource;
import com.titan.gyslfh.backalarm.BackAlarmModel;
import com.titan.model.TrackPoint;
import com.titan.newslfh.R;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Concrete implementation of a data source as a db.
 */
public class LocalDataSource implements DataSource {

    private static LocalDataSource INSTANCE;

    //private DbHelper mDbHelper;

    private Context mContext;

    // Prevent direct instantiation.
    private LocalDataSource(@NonNull Context context) {
        //checkNotNull(context);
        this.mContext=context;
        //mDbHelper = new DbHelper(context);
    }

    public static LocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalDataSource(context);
        }
        return INSTANCE;
    }




    @Override
    public void uplaodAlarmInfo(String infojson,uploadCallback callback) {
    }

    @Override
    public void saveTrackPoint(final TrackPoint trackPoint , final saveCallback callback) {
        //String ptjson=new Gson().toJson(trackPoint,TrackPoint.class);
        Observable.create(new Observable.OnSubscribe<TrackPoint>() {
            @Override
            public void call(Subscriber<? super TrackPoint> subscriber) {
                /*String time = DateUtil.dateFormat(new Date());
                String userid= TitanApplication.mUserModel.getUserID();
                TrackPoint tp= new TrackPoint(null,time,point.getX(),point.getY(),userid,status);*/
                try {
                    long d=GreenDaoManager.getInstance(mContext).getNewSession().getTrackPointDao().insert(trackPoint);
                    subscriber.onNext(trackPoint);

                }catch (Exception e){
                    subscriber.onError(e);
                }
            }}).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TrackPoint>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TITAN",e.toString());
                        callback.onFailure(e.toString());

                    }

                    @Override
                    public void onNext(TrackPoint point) {
                         callback.onSuccess(mContext.getString(R.string.success_uploc));
                    }
                });

    }

    @Override
    public void getUnDealAlarmCount(String dqid, uploadCallback callback) {

    }

    @Override
    public void getAlarmInfoDetail(String alarmid, saveCallback callback) {

    }

    @Override
    public void getAlarmInfoList(String querystr, String dqid, String index, String number, saveCallback callback) {

    }

    @Override
    public void onBackAlarm(BackAlarmModel backAlarmModel, saveCallback callback) {

    }

    @Override
    public void getDvrInfo() {

    }


}
