package com.titan.gyslfh.alarminfo;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.util.Log;

import com.google.gson.Gson;
import com.titan.BaseViewModel;
import com.titan.data.source.DataRepository;
import com.titan.data.source.DataSource;
import com.titan.newslfh.R;

import java.util.List;

/**
 * Created by whs on 2017/5/25
 */

public class AlarmDetailViewModel extends BaseViewModel {



    private AlarmDetailInterface mViewInterface;
    //火警信息和回警信息
    public ObservableField<AlarmInfoDetailModel> mAlarmInfoDetail=new ObservableField<>();

    public ObservableField<Boolean> databinding=new ObservableField<>();
    //编号
    public ObservableField<String> number=new ObservableField<>();
    //接警时间
    public ObservableField<String> reporttime=new ObservableField<>();
    //来源
    public ObservableField<String> origin=new ObservableField<>();
    //电话
    public ObservableField<String> phonenumber=new ObservableField<>();
    //经度
    public ObservableField<String> LON=new ObservableField<>();
    //纬度
    public ObservableField<String> LAT=new ObservableField<>();
    //通知区县
    public ObservableField<String> noticeCountry=new ObservableField<>();
    //是否火灾
    public ObservableField<String> isfire=new ObservableField<>();
    //回警信息
    public ObservableField<String> backalarminfo=new ObservableField<>();


    //
    private boolean mIsDataLoading;

    private StringBuffer mbackinfo=new StringBuffer("");


    public AlarmDetailViewModel(Context applicationContext, AlarmDetailInterface alarmDetailInterface, DataRepository dataRepository) {
        this.mContext=applicationContext;
        this.mViewInterface=alarmDetailInterface;
        this.mDataRepository=dataRepository;
        mAlarmInfoDetail.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //snackbarText.set(info);
                AlarmInfoDetailModel alarmInfoDetailModel=mAlarmInfoDetail.get();
                if(alarmInfoDetailModel==null){
                    //snackbarText.set("加载数据失败");
                }else {
                    //接警信息
                   AlarmInfoModel.AlarmInfo alarmInfo= alarmInfoDetailModel.getAlarmInfo();
                    number.set(alarmInfo.getID());
                    reporttime.set(alarmInfo.getRECEIPTTIME());
                    origin.set(alarmInfo.getORIGIN());
                    phonenumber.set(alarmInfo.getTEL());
                    LON.set(alarmInfo.getLON());
                    LAT.set(alarmInfo.getLAT());
                    noticeCountry.set(alarmInfo.getNOTICEAREA());
                    isfire.set(alarmInfo.getISFIRE());
                    mbackinfo.delete(0,mbackinfo.length());
                    //回警信息
                    List<AlarmInfoDetailModel.BackInfoBean> backinfos=alarmInfoDetailModel.getBackInfo();
                    for (AlarmInfoDetailModel.BackInfoBean backinfo:backinfos){
                        String backstatus="";
                        switch (backinfo.getBACKSTATUS()){
                            case 0:
                                backstatus="未查看";
                                break;
                            case 1:
                                backstatus="查看未回复";
                                break;
                            case 3:
                                backstatus="已回复";
                                break;
                        }
                        int firestatus= Integer.parseInt(backinfo.getFIRESIUATION());
                       String stutation= mContext.getResources().getStringArray(R.array.firestatus)[firestatus];
                       mbackinfo.append(backinfo.getDQNAME()+":  "+backstatus+"   "+firestatus+"\n");
                    }
                    backalarminfo.set(mbackinfo.toString());
                }

            }
        });
    }

    public void loadData(String alarmid) {
        mDataRepository.getAlarmInfoDetail(alarmid, new DataSource.saveCallback() {
            @Override
            public void onFailure(String info) {
                Log.e("TItan",info);
                mIsDataLoading=false;
                mAlarmInfoDetail.set(null);
                snackbarText.set("加载数据失败"+info);

            }

            @Override
            public void onSuccess(String data) {
                AlarmInfoDetailModel alarmDetail=new Gson().fromJson(data,AlarmInfoDetailModel.class);
                mAlarmInfoDetail.set(alarmDetail);
                mIsDataLoading=false;
                notifyChange();


            }
        });

    }

    public void start(String alarmid) {
        loadData(alarmid);
    }
}
