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


    /**
     * 检查用户登陆
     * @param username
     * @param psd
     * @param cid  推送客户端id
     * @param callback
     */
    void checkLogin(String username,String psd,String cid,getCallback callback);
}
