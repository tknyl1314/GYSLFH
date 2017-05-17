package com.titan.gyslfh.inputAlarm;

import android.databinding.BaseObservable;

import java.io.Serializable;

/**
 * Created by li on 2017/4/28.
 */

public class InputAlarm extends BaseObservable implements Serializable {

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getPoliceCase() {
        return policeCase;
    }

    public void setPoliceCase(String policeCase) {
        this.policeCase = policeCase;
    }

    public String getPoliceTime() {
        return policeTime;
    }

    public void setPoliceTime(String policeTime) {
        this.policeTime = policeTime;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getReportCase() {
        return reportCase;
    }

    public void setReportCase(String reportCase) {
        this.reportCase = reportCase;
    }

    public String getDqid() {
        return dqid;
    }

    public void setDqid(String dqid) {
        this.dqid = dqid;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsWork() {
        return isWork;
    }

    public void setIsWork(String isWork) {
        this.isWork = isWork;
    }

    public String getNoticeAreaIDs() {
        return noticeAreaIDs;
    }

    public void setNoticeAreaIDs(String noticeAreaIDs) {
        this.noticeAreaIDs = noticeAreaIDs;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    /**
     * 详细地址
     */
    private String address;
    /**
     * 经度
     */
    private String lon;
    /**
     * 纬度
     */
    private String lat;
    /**
     * 出警情况
     */
    private String policeCase;
    /**
     * 出警时间
     */
    private String policeTime;
    /**
     * 上报时间
     */
    private String reportTime;
    /**
     * 上报情况
     */
    private String reportCase;
    /**
     * 地区id
     */
    private String dqid;
    /**
     * 用户id
     */
    private String userID;
    /**
     * 电话号码
     */
    private String tel;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否查岗
     */
    private String isWork;
    /**
     * 通知地区ID（用，分隔）
     */
    private String noticeAreaIDs;
    /**
     * 图片
     */
    private String pic;


    public InputAlarm() {

    }

    /**
     */
    public InputAlarm(String address, String lon, String lat, String policeCase, String policeTime, String reportTime,
                      String reportCase, String dqid, String userID, String tel, String remark, String isWork, String noticeAreaIDs, String pic) {
        this.address = address;
        this.lon = lon;
        this.lat = lat;
        this.policeCase = policeCase;
        this.policeTime = policeTime;
        this.reportTime = reportTime;
        this.reportCase = reportCase;
        this.dqid = dqid;
        this.userID = userID;
        this.tel = tel;
        this.remark = remark;
        this.isWork = isWork;
        this.noticeAreaIDs = noticeAreaIDs;
        this.pic = pic;
    }


}
