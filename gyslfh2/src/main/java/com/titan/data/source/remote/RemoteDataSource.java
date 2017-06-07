package com.titan.data.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.titan.data.source.DataSource;
import com.titan.gyslfh.TitanApplication;
import com.titan.gyslfh.alarminfo.AlarmInfoModel;
import com.titan.gyslfh.backalarm.BackAlarmModel;
import com.titan.gyslfh.login.UserModel;
import com.titan.model.FireRiskModel;
import com.titan.model.ResultModel;
import com.titan.model.TrackPoint;
import com.titan.newslfh.R;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by whs on 2017/5/18
 */

public class RemoteDataSource implements DataSource {
    private Context mContext;
    private static RemoteDataSource INSTANCE;

    public RemoteDataSource(Context mContext) {
        this.mContext = mContext;

    }

    public static RemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource(context);
        }
        return INSTANCE;
    }







    @Override
    public void uplaodAlarmInfo(String infojson, final uploadCallback callback) {
        Observable<String> observable= RetrofitHelper.getInstance(mContext).getServer().uploadAlarmInfo(infojson);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(mContext.getString(R.string.failure_upload)+e.toString());

                    }

                    @Override
                    public void onNext(String json) {
                        try {
                            ResultModel<UserModel> resultModel=new Gson().fromJson(json, ResultModel.class);

                            if(resultModel.getResult()){
                                callback.onSuccess(mContext.getString(R.string.success_upload));
                            }else {
                                callback.onFailure(mContext.getString(R.string.failure_upload)+resultModel.getMessage());
                            }
                        }catch (JsonSyntaxException e){
                            callback.onFailure(mContext.getString(R.string.failure_upload)+e.toString());
                        }
                    }
                });
    }

    @Override
    public void saveTrackPoint(TrackPoint trackPoint, final saveCallback callback) {
        String ptjson=new Gson().toJson(trackPoint,TrackPoint.class);
        Observable<String> observable=RetrofitHelper.getInstance(mContext).getServer().upLoadTrackPoint(ptjson);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(mContext.getString(R.string.error_uploc2server));

                    }

                    @Override
                    public void onNext(String json) {
                        try {
                            ResultModel<UserModel> resultModel=new Gson().fromJson(json, ResultModel.class);

                            if(resultModel.getResult()){
                                callback.onSuccess(mContext.getString(R.string.success_uploc));
                            }else {
                                callback.onFailure(mContext.getString(R.string.error_uploc2server)+resultModel.getMessage());
                            }
                        }catch (JsonSyntaxException e){
                            callback.onFailure(mContext.getString(R.string.error_uploc2server)+e.toString());
                        }

                        //callback.onSuccess();

                    }
                });
    }


    @Override
    public void getUnDealAlarmCount(String dqid, final uploadCallback callback) {
        Observable<String> observable=RetrofitHelper.getInstance(mContext).getServer().getUnDealAlarmCount(dqid);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(mContext.getString(R.string.error_uploc2server));

                    }

                    @Override
                    public void onNext(String json) {
                        try {
                            ResultModel<UserModel> resultModel=new Gson().fromJson(json, ResultModel.class);

                            if(resultModel.getResult()){
                                callback.onSuccess(mContext.getString(R.string.success_uploc));
                            }else {
                                callback.onFailure(mContext.getString(R.string.error_uploc2server)+resultModel.getMessage());
                            }
                        }catch (JsonSyntaxException e){
                            callback.onFailure(mContext.getString(R.string.error_uploc2server)+e.toString());
                        }

                        //callback.onSuccess();

                    }
                });
    }

    @Override
    public void getAlarmInfoDetail(String alarmid , final saveCallback callback) {
        Observable<String> observable=RetrofitHelper.getInstance(mContext).getServer().getAlarmInfoDetail(alarmid);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(mContext.getString(R.string.error_getalarminfodetail)+e);

                    }

                    @Override
                    public void onNext(String json) {
                        //callback.onSuccess(json);

                        try {
                            ResultModel<UserModel> resultModel=new Gson().fromJson(json, ResultModel.class);
                            if(resultModel.getResult()){
                                callback.onSuccess(new Gson().toJson(resultModel.getData()));
                            }else {
                                callback.onFailure(mContext.getString(R.string.error_getalarminfodetail)+resultModel.getMessage());
                            }
                        }catch (JsonSyntaxException e){
                            callback.onFailure(mContext.getString(R.string.error_getalarminfodetail)+e.toString());
                        }
                        //callback.onSuccess(json);

                    }
                });
    }

    @Override
    public void getAlarmInfoList(String querystr, String dqid, String index, String number, final saveCallback callback) {
        Observable<String> observable = RetrofitHelper.getInstance(mContext).getServer().getAlarmInfo(querystr, TitanApplication.mUserModel.getDqid(),index,number);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {


                        Log.e("error",e.toString());
                        callback.onFailure(e.toString());
                        //snackbarText.set("获取数据异常："+e);
                    }

                    @Override
                    public void onNext(String json) {
                        Gson gson=new Gson();
                        ResultModel<AlarmInfoModel> resultModel=gson.fromJson(json, ResultModel.class);
                        if(resultModel.getResult()){
                            callback.onSuccess(new Gson().toJson(resultModel.getData()));
                            //AlarmInfoModel infos=gson.fromJson(gson.toJson(resultModel.getData()),AlarmInfoModel.class);

                        }else {
                            callback.onFailure(resultModel.getMessage());
                        }

                    }
                });

    }

    @Override
    public void onBackAlarm(BackAlarmModel backAlarmModel, final saveCallback callback) {
        String json=new Gson().toJson(backAlarmModel,BackAlarmModel.class);
        Observable<String> observable = RetrofitHelper.getInstance(mContext).getServer().onBackAlarm(json);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {


                        Log.e("error",e.toString());
                        callback.onFailure(e.toString());
                        //snackbarText.set("获取数据异常："+e);
                    }

                    @Override
                    public void onNext(String json) {
                        Gson gson=new Gson();
                        ResultModel<AlarmInfoModel> resultModel=gson.fromJson(json, ResultModel.class);
                        if(resultModel.getResult()){
                            callback.onSuccess(new Gson().toJson(resultModel.getData()));
                            //AlarmInfoModel infos=gson.fromJson(gson.toJson(resultModel.getData()),AlarmInfoModel.class);

                        }else {
                            callback.onFailure(resultModel.getMessage());
                        }

                    }
                });
    }

    @Override
    public void getDvrInfo() {

    }

    public void getFireRiskInfo(String date, String hour, String ThematicType, final getWeatherCallback callback) {

        Observable<FireRiskModel> observable=RetrofitHelper.getInstance(mContext).getWeatherServer().getMeteorology(date,"0",hour,ThematicType);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FireRiskModel>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        callback.onFailure(mContext.getString(R.string.error_getweather)+e);

                    }

                    @Override
                    public void onNext(FireRiskModel fireRiskModel) {
                        //callback.onSuccess(json);
                        callback.onSuccess(fireRiskModel);

                    }
                });


    }
}
