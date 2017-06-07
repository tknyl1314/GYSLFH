package com.titan.gyslfh.alarminfo;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.gson.Gson;
import com.titan.BaseViewModel;
import com.titan.data.source.remote.RetrofitHelper;
import com.titan.gyslfh.TitanApplication;
import com.titan.model.ResultModel;
import com.titan.newslfh.BR;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by whs on 2017/5/3
 */

public class AlarmInfoViewModel extends BaseViewModel {

    public final ObservableList<AlarmInfoModel.AlarmInfo> mAlarmInfos = new ObservableArrayList<>();


    public final ObservableField<Integer> totalcount = new ObservableField<>();
    //火警ID
    public  final ObservableField<String>  alarmId=new ObservableField<>();
    //empty label
    public final ObservableField<String> noItemLabel = new ObservableField<>();
    //empty iocn
    public final ObservableField<Drawable> noItemIconRes = new ObservableField<>();

    private AlarmInfoItemNav mAlarmInfoItemNav;

    private Context mContext;


    public AlarmInfoViewModel(Context context, AlarmInfoItemNav alarmInfoItemNav){

        this.mAlarmInfoItemNav=alarmInfoItemNav;
        this.mContext=context;


    }



    @Bindable
    public boolean isEmpty() {
        return mAlarmInfos.isEmpty();
    }


    public void start() {
        loadData(true);
    }

    /**
     * 加载数据
     */
    public void loadData(final boolean showLoadingUI) {
        if(showLoadingUI) {
            dataLoading.set(true);

        }
        Observable<String> observable = RetrofitHelper.getInstance(mContext).getServer().getAlarmInfo("", TitanApplication.mUserModel.getDqid(),"1","10");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        dataLoading.set(false);

                        //setupRefreshLayout();
                        //intiRecyclerView(alarmInfoList);
                    }

                    @Override
                    public void onError(Throwable e) {

                        dataLoading.set(false);

                        Log.e("error",e.toString());
                        snackbarText.set("获取数据异常："+e);
                    }

                    @Override
                    public void onNext(String json) {
                        Gson gson=new Gson();
                        ResultModel<AlarmInfoModel> resultModel=gson.fromJson(json, ResultModel.class);
                        if(resultModel.getResult()){
                            AlarmInfoModel infos=gson.fromJson(gson.toJson(resultModel.getData()),AlarmInfoModel.class);
                            totalcount.set(Integer.valueOf(infos.getRecordCount()));
                            //Log.e("Titan",totalcount+"");
                            mAlarmInfos.clear();

                            /*for (AlarmInfoModel.AlarmInfo alarminfo: infos.getDs()) {
                               mAlarmInfos.add(new AlarmInfoItemViewModel(alarminfo))  ;
                            }*/
                            mAlarmInfos.addAll(infos.getDs());
                            snackbarText.set("获取数据"+totalcount.get());

                            notifyPropertyChanged(BR.empty); // It's a @Bindable so update manually

                            //notifyChange();

                            //mAlarmInfos.add( infos.getDs());
                            //mAlarmInfos= (ObservableList<AlarmInfoModel.AlarmInfo>) infos.getDs();


                        }else {
                            snackbarText.set("获取数据失败："+resultModel.getMessage());
                           // Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
