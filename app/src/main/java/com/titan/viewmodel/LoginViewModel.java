package com.titan.viewmodel;
/**
 * Created by Whs on 2016/12/1 0001.
 */

import android.content.Context;
import android.databinding.ObservableField;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import Util.ToastUtil;
public class LoginViewModel implements ViewModel {
    private Context context;
    public ObservableField<String> username= new ObservableField<>();
    public ObservableField<String> password =new ObservableField<>();
    //ObservableArrayMap<String, Object> user = new ObservableArrayMap<>();
    public LoginViewModel(Context context) {
        this.context = context;
        username.set("whs");
        password.set("sssss");
        //this.user=userinfo;
    }
    /**
     * 登录事件
     */
    public  void onLogin(View view){

        ToastUtil.setToast(context,password.get());
    }

    public TextWatcher getUsernameEditTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                username.set(charSequence.toString());
                //searchButtonVisibility.set(charSequence.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }
    public TextWatcher getPasswordEditTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                password.set(charSequence.toString());
                //searchButtonVisibility.set(charSequence.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    @Override
    public void destroy() {
    }
}
