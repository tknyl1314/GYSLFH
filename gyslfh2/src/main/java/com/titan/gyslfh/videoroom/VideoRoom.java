package com.titan.gyslfh.videoroom;

/**
 * Created by whs on 2017/10/16
 */

public interface VideoRoom {
    //加入房间
    //void addRoom(String roomid);
    /**
     * 匿名加入
     * @param strRoomId
     */
    void loginWithAnonymous(String strRoomId);

}
