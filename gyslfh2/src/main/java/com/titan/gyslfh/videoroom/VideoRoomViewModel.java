package com.titan.gyslfh.videoroom;
/**
 * Created by Whs on 2016/12/1 0001
 */

import android.content.Context;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.titan.BaseViewModel;


/**
 * Created by Whs on 2016/12/1
 * 视频会议
 */
public class VideoRoomViewModel extends BaseViewModel{

    //roomid
    public final ObservableField<String> roomid = new ObservableField<>("");

    private VideoRoom mVideoRoom;



    public VideoRoomViewModel(Context context,VideoRoom videoRoom) {
        this.mContext = context;
        this.mVideoRoom=videoRoom;
    }
    /**
     * 检查roomid
     */
    public void checkRoomID() {
        String strRoonId = roomid.get();
        if (TextUtils.isEmpty(strRoonId)) {
            snackbarText.set("输入的房间号不能为空");
            //islogining = false;
            return;
        }

        if (strRoonId.length() > 20 || strRoonId.length() < 1) {
            snackbarText.set("输入的房间号长度应在1-20之间");
            //islogining = false;
            return;
        }

        if (strRoonId.matches("[A-Za-z0-9_]+")) {
            //loginWithAnonymous(strRoonId);
            mVideoRoom.loginWithAnonymous(strRoonId);
        } else {
            snackbarText.set("房间号只能包含数组,字母,下划线");
        }
    }



}
