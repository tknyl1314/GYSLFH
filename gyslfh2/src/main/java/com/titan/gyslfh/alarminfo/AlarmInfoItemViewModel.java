package com.titan.gyslfh.alarminfo;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableField;

/**
 * Created by whs on 2017/5/4
 */

public class AlarmInfoItemViewModel extends AlarmInfoViewModel {
    public final ObservableField<AlarmInfoModel.AlarmInfo> alarminfo=new ObservableField<>();
    private AlarmInfoItemNav mAlarmInfoItemNav;
    private  Context mContext;

   /* @Bindable
    public String getStatus() {
        return "暂无";
    }*/

    public AlarmInfoItemViewModel(Context context, final AlarmInfoItemNav alarmInfoItemNav){
        super(context,alarmInfoItemNav);
        this.mContext=context;
        this.mAlarmInfoItemNav=alarmInfoItemNav;
        /*AlarmInfoModel.AlarmInfo alarmInfo=alarminfo.get();
        //接警时间
        this.time.set(alarmInfo.getRECEIPTTIME());
        //接警来源
        this.origin.set(alarmInfo.getORIGIN());
        //地址
        this.address.set(alarmInfo.getADDRESS());

        this.status.set("暂无");*/
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
                //火情状态
                status.set("暂无");
                //notifyChange();
            }

        });


    }

  /*  public AlarmInfoItemViewModel(AlarmInfoModel.AlarmInfo alarmInfo){
        this.alarminfo.set(alarmInfo);
    }*/


    /**
     * Called by the Data Binding library when the row is clicked.
     */
    public void onAlarmInfoClicked() {
        String id = alarminfo.get().getID();
        if (id == null) {
            // Click happened before task was loaded, no-op.
            return;
        }
        mAlarmInfoItemNav.openAlarmInfoDetails(id);
    }


}
