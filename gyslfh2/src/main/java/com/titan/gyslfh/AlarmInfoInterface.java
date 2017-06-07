package com.titan.gyslfh;

/**
 * Created by whs on 2017/5/27
 */

public interface AlarmInfoInterface {

    /**
     * 时间选择
     */
     void showDateDialog(int timetype);
    /**
     * 通知区县选择
     */
    void showCountrySelectDialog(int type);

    void showProgress(boolean isshow);
    /**
     * 显示图片
     */
    void showImage();
    /**
     * 显示图片
     */
    void showImageDetail(int imgpath);
    /**
     * 接警来源
     */
    void showOriginDialog();
    /**
     * 火情状态
     */
    void showStatusDialog();

    /**
     * 是否火灾
     */
    void showIsfireDialog();
    /**
     * 接警详细
     */
    void openAlarmInfoDetails(String id);




}
