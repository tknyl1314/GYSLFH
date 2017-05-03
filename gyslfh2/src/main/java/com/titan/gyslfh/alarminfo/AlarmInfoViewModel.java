package com.titan.gyslfh.alarminfo;

import android.content.Context;
import android.databinding.BaseObservable;

/**
 * Created by whs on 2017/5/3
 */

public class AlarmInfoViewModel extends BaseObservable {
    private Context mContext;
    private IAlarmInfo mAlarmInfo;

    public AlarmInfoViewModel(Context context, IAlarmInfo mAlarmInfo) {
        this.mContext = context;
        this.mAlarmInfo=mAlarmInfo;
    }
}
