package com.titan.gyslfh.backalarm;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.graphics.drawable.Drawable;

import com.google.gson.Gson;
import com.titan.BaseViewModel;
import com.titan.data.source.DataRepository;
import com.titan.data.source.DataSource;
import com.titan.gyslfh.TitanApplication;
import com.titan.gyslfh.alarminfo.AlarmInfoModel;

/**
 * Created by whs on 2017/5/3
 */

public class AlarmListViewModel extends BaseViewModel {
    //火警数据
    public final ObservableList<AlarmInfoModel.AlarmInfo> mAlarmInfos = new ObservableArrayList<>();

    //火警数据
    //public final ObservableField<List<AlarmInfoModel.AlarmInfo>> mAlarmInfos = new ObservableField<>();
    //获取分页
    //public ObservableInt index=new ObservableInt(1);

    private int index=1;

    //每页条数
    public ObservableInt pagenumber=new ObservableInt(10);
    //数据加载
    public final ObservableBoolean dataLoading = new ObservableBoolean(false);
    //火警ID
    public  final ObservableField<String>  alarmId=new ObservableField<>();
    //empty label
    public final ObservableField<String> noItemLabel = new ObservableField<>();
    //empty iocn
    public final ObservableField<Drawable> noItemIconRes = new ObservableField<>();

    private BackAlarmInterface mViewInterface;

    public AlarmListViewModel(final Context context, final BackAlarmInterface mViewInterface, DataRepository dataRepository){
        this.mViewInterface=mViewInterface;
        this.mContext=context;
        this.mDataRepository=dataRepository;
        //设置emptyview


    }


    /**
     * 判断是否为空
     * @return
     */
    @Bindable
    public boolean isEmpty() {
       // if(mAlarmInfos.get().isEmpty())

       return  mAlarmInfos.isEmpty();

    }


    public void start() {
        loadData(true,0,"");
    }

    /**
     * 加载数据
     */
    public void loadData(final boolean showLoadingUI, final Integer type,String querystr) {
        if(showLoadingUI) {
            dataLoading.set(true);
        }

        switch (type){
            //inti
            case 0:
                //refresh
            case 1:
                mAlarmInfos.clear();

                index=1;
                break;
            //loadmore
            case 2:
                index++;
                break;
        }
        mDataRepository.getAlarmInfoList("isfire is null", TitanApplication.mUserModel.getDqid(), index+"", pagenumber.get()+"", new DataSource.saveCallback() {
            @Override
            public void onFailure(String info) {
                dataLoading.set(false);
                //mAlarmInfos.set(null);
            }

            @Override
            public void onSuccess(String data) {

                dataLoading.set(false);
                AlarmInfoModel alarmInfoModel=new Gson().fromJson(data,AlarmInfoModel.class);
                if(type==2){
                }else {
                    mAlarmInfos.addAll(alarmInfoModel.getDs());
                }


                //mAlarmInfos.get().addAll(alarmInfoModel.getDs());
                notifyChange();

            }
        });

    }
}
