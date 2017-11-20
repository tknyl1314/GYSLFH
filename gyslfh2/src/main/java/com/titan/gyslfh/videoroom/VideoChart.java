package com.titan.gyslfh.videoroom;

/**
 * Created by whs on 2017/10/16
 *
 */

public interface VideoChart {

    /**
     * 离开房间
     */
    void leaveRoom();

    /**
     * 切换摄像头
     */
    void switchCamera();

    /**
     * 开启或这关闭麦克风
     */
    void switchAudio(boolean isopen);


}
