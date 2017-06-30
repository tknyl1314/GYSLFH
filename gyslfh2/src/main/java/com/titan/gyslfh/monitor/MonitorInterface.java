package com.titan.gyslfh.monitor;

/**
 * Created by whs on 2017/6/1
 */

public interface MonitorInterface {
    void processRealData(int i, int iDataType, byte[] pDataBuffer, int iDataSize, int streamRealtime);

    /**
     * 视频预览
     */
    void onPreview();
}
