package com.titan.gyslfh.login;

/**
 * Created by whs on 2017/4/27
 */

public class UserModel {

    /**
     * dqid : 1472
     * dqName : 中国
     * role : 超级管理员
     * accountStatus : 1
     * clientID :
     * userID : 1
     * dqLevel : 1
     */

    private String dqid;
    private String dqName;
    private String role;
    private String accountStatus;
    private String clientID;
    private String userID;
    private String dqLevel;

    public String getDqid() {
        return dqid;
    }

    public void setDqid(String dqid) {
        this.dqid = dqid;
    }

    public String getDqName() {
        return dqName;
    }

    public void setDqName(String dqName) {
        this.dqName = dqName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDqLevel() {
        return dqLevel;
    }

    public void setDqLevel(String dqLevel) {
        this.dqLevel = dqLevel;
    }
}
