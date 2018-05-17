package com.titan.gyslfh.videoroom;

import com.titan.base.BaseView;

/**
 * Created by whs on 2017/10/16
 */

public interface VideoRoom extends BaseView{
    //加入房间
    //void addRoom(String roomid);
    /**
     * 匿名加入
     * @param strRoomId
     */
    void loginWithAnonymous(String strRoomId);


}
