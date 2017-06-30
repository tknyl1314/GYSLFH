package com.titan.gyslfh.alarminfo;

/**
 * Created by whs on 2017/5/4
 */

public interface AlarmInfoListInterface {
    //void openAlarmInfoDetails(String id);

    /**
     * 打开火警详细信息
     * @param alarmInfo
     */
    void openAlarmInfoDetails(AlarmInfoModel.AlarmInfo alarmInfo);

    /**
     * 加载更多
     * @param alarmInfos
     */
    //void loadMoreData(List<AlarmInfoModel.AlarmInfo> alarmInfos);

    /**
     * 下拉刷新
     * @param alarmInfos
     */
    //void refreshData(List<AlarmInfoModel.AlarmInfo> alarmInfos);

    /**
     * 停止更新
     */
    void stopUpdate();

}
