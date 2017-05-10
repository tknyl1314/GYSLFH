package com.titan;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;

/**
 * Created by whs on 2017/5/5
 */

public class BaseViewModel extends BaseObservable {

    //提示信息
   public final ObservableField<String> snackbarText = new ObservableField<>();
}
