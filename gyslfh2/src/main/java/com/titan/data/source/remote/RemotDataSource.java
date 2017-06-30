package com.titan.data.source.remote;

/**
 * Created by whs on 2017/6/7
 */

public interface RemotDataSource {
    interface getCallback {

        void onFailure(String info);

        void onSuccess(String data);
    }
    /**
     * 获取监控点硬盘录像机信息
     */
    void getDvrInfo(String str,getCallback callback);
}
