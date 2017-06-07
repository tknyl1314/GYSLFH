package com.titan.gis.callout;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.titan.BaseViewModel;

/**
 * Created by whs on 2017/6/1
 */

public class CalloutViewModel extends BaseViewModel {

    public ObservableField<String> attr=new ObservableField<>();
    //是监控点
    public ObservableBoolean ismonitor=new ObservableBoolean(false);

    private CalloutInterface mViewInterface;

    public CalloutViewModel(Context context,CalloutInterface mViewInterface) {
        this.mViewInterface = mViewInterface;
        this.mContext=context;

    }

    public void calloutClose(){
        mViewInterface.calloutClose();
    }

    public void showMonitor(){
        mViewInterface.showMonitor();
    }

}
