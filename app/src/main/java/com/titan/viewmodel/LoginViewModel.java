package com.titan.viewmodel;
/**
 * Created by Whs on 2016/12/1 0001.
 */

import android.content.Context;
import android.view.View;

import Util.ToastUtil;
/**
 * Created by Whs on 2016/12/1 0001.
 */
public class LoginViewModel implements ViewModel {
    private Context context;
    public LoginViewModel(Context context) {
        this.context = context;
    }
    /**
     * 登录事件
     * @param view
     */
    public  void onLongin(View view){
        ToastUtil.setToast(context,"点击了登录");
    }
    @Override
    public void destroy() {

    }
}
