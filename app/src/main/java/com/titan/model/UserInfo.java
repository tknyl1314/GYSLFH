package com.titan.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by Whs on 2016/12/2 0002.
 */
public   class UserInfo extends BaseObservable  {
    private  String userName;
    private  String passWord;
    public UserInfo(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    @Bindable
    public String getUserName() {
        return userName;
    }
    @Bindable
    public String getPassWord() {
        return passWord;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
