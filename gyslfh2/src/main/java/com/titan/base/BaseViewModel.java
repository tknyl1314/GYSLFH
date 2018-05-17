package com.titan.base;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.titan.data.source.DataRepository;

/**
 * Created by hanyw on 2017/12/4/004
 */

public class BaseViewModel extends BaseObservable {
    protected Context mContext;
    protected DataRepository mDataRepository;
    //数据加载中
    public final ObservableBoolean dataLoading = new ObservableBoolean(false);
    //提示信息
    public final ObservableField<String> snackbarText = new ObservableField<>();
    public String getSnackbarText() {
        return snackbarText.get();
    }

}
