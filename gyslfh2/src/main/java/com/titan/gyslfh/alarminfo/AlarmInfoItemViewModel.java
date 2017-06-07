package com.titan.gyslfh.alarminfo;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableField;

import com.titan.newslfh.R;

/**
 * Created by whs on 2017/5/4
 */

public class AlarmInfoItemViewModel extends AlarmInfoViewModel {
    public final ObservableField<AlarmInfoModel.AlarmInfo> alarminfo=new ObservableField<>();

    //时间
    public  final ObservableField<String>  time=new ObservableField<>();
    //地址
    public  final ObservableField<String>  address=new ObservableField<>();
    //来源
    public  final ObservableField<String>  origin=new ObservableField<>();
    //状态
    public  final ObservableField<String>  isfire=new ObservableField<>();

    private AlarmInfoItemNav mAlarmInfoItemNav;
    private  Context mContext;

    public AlarmInfoItemViewModel(Context context, final AlarmInfoItemNav alarmInfoItemNav){
        super(context,alarmInfoItemNav);
        this.mContext=context;
        this.mAlarmInfoItemNav=alarmInfoItemNav;
        this.alarminfo.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {

            @Override
            public void onPropertyChanged(Observable observable, int i) {
                AlarmInfoModel.AlarmInfo alarmInfo=alarminfo.get();

                //接警时间
                time.set(alarmInfo.getRECEIPTTIME());
                //接警来源
                origin.set(alarmInfo.getORIGIN());
                //地址
                address.set(alarmInfo.getADDRESS());

                if(alarmInfo.getISFIRE().equals("")){
                    isfire.set("未回警");
                }else {
                    int isfireid= Integer.parseInt(alarmInfo.getISFIRE());
                    isfire.set(mContext.getResources().getStringArray(R.array.alarm_isfire)[isfireid]);
                }




            }

        });


    }




    /**
     * Called by the Data Binding library when the row is clicked.
     */
    public void onAlarmInfoClicked() {
        String id = alarminfo.get().getID();
        if (id == null) {
            //Click happened before task was loaded, no-op.
            return;
        }
        mAlarmInfoItemNav.openAlarmInfoDetails(alarminfo.get());
    }


}
