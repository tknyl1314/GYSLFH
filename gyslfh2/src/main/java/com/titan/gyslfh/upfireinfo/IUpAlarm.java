package com.titan.gyslfh.upfireinfo;

import com.titan.gyslfh.AlarmInfoInterface;

/**
 * Created by whs on 2017/5/17
 */

public interface IUpAlarm extends AlarmInfoInterface {
    /**
     * 上报火情
     * @return
     */
    public void upLoadAlarmInfo();

    /**
     * 时间选择
     */
    public void showDateDialog(int timetype);

    /**
     * 通知区县选择
     */
    public void showCountrySelectDialog(int type);

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
}
