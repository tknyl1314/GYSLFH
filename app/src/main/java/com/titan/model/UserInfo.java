package com.titan.model;

/**
 * Created by Whs on 2016/12/2 0002.
 */
public class UserInfo {
    private final String userName;
    private final String passWord;

    public UserInfo(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }
}
