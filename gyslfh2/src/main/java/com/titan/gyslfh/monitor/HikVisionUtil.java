package com.titan.gyslfh.monitor;

import android.util.Log;

import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;
import com.hikvision.netsdk.RealPlayCallBack;

import org.MediaPlayer.PlayM4.Player;

/**
 * Created by whs on 2017/6/1
 * 海康视频监控帮助类
 */

public class HikVisionUtil {

    private static final String TAG ="HiKVision";
    //当前通道号
    private int m_td;

    private int m_iLogID = -1; // return by NET_DVR_Login_v30
    private int m_iPlayID = -1; // return by NET_DVR_RealPlay_V30
    private int m_iPlaybackID = -1; // return by NET_DVR_PlayBackByTime
    private  NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = null;

    private    int m_iPort = -1; // play port
    //起始通道
    private    int m_iStartChan = 0; // start channel no
    //通道数量
    private  int m_iChanNum = 0; // channel number

    public HikVisionUtil() {
    }


    /**
     * @fn initeSdk
     * @author zhuzhenlei
     * @brief SDK init
     * @return true - success;false - fail
     */
    public  boolean initSdk()
    {
        // init net sdk
        if (!HCNetSDK.getInstance().NET_DVR_Init())
        {
            Log.e(TAG, "HCNetSDK init is failed!");
            //Toast.makeText(mContext, "HCNetSDK init is failed!", Toast.LENGTH_SHORT).show();
            return false;
        }
        HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, "/mnt/sdcard/sdklog/",
                true);
        return true;
    }

    public  int getM_iLogID() {
        return m_iLogID;
    }

    public  void setM_iLogID(int m_iLogID) {
        this.m_iLogID = m_iLogID;
    }

    public int getM_iPlayID() {
        return m_iPlayID;
    }

    public void setM_iPlayID(int m_iPlayID) {
        this.m_iPlayID = m_iPlayID;
    }

    public int getM_iPlaybackID() {
        return m_iPlaybackID;
    }

    public void setM_iPlaybackID(int m_iPlaybackID) {
        this.m_iPlaybackID = m_iPlaybackID;
    }

    public  NET_DVR_DEVICEINFO_V30 getM_oNetDvrDeviceInfoV30() {
        return m_oNetDvrDeviceInfoV30;
    }

    public static void setM_oNetDvrDeviceInfoV30(NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30) {
        m_oNetDvrDeviceInfoV30 = m_oNetDvrDeviceInfoV30;
    }

    public int getM_iPort() {
        return m_iPort;
    }

    public void setM_iPort(int m_iPort) {
        this.m_iPort = m_iPort;
    }

    public int getM_iStartChan() {
        return m_iStartChan;
    }

    public void setM_iStartChan(int m_iStartChan) {
        this.m_iStartChan = m_iStartChan;
    }

    public int getM_iChanNum() {
        return m_iChanNum;
    }

    public void setM_iChanNum(int m_iChanNum) {
        this.m_iChanNum = m_iChanNum;
    }

    /**
     * 登陆硬盘录像机
     */
    public   boolean loginDVR(String ip,int port,String username,String psd)
    {
        try
        {
            if (m_iLogID < 0)
            {
                // login on the device
                m_iLogID = loginDevice(ip,port,username,psd);
                if (m_iLogID < 0)
                {
                    Log.e(TAG, "This device logins failed!");
                    //ToastUtil.setToast(MonitorActivity.this, "登录失败");
                    return false;
                }
                // get instance of exception callback and set
                ExceptionCallBack oexceptionCbf = getExceptiongCbf();
                if (oexceptionCbf == null)
                {
                    Log.e(TAG, "ExceptionCallBack object is failed!");
                    //ToastUtil.setToast(MonitorActivity.this, "登录失败");
                    return false;
                }

                if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(
                        oexceptionCbf))
                {
                    Log.e(TAG, "NET_DVR_SetExceptionCallBack is failed!");
                    return false;
                }

                // m_oLoginBtn.setText("退出登陆");
                Log.i(TAG,
                        "Login sucess ****************************1***************************");
                // m_oPreviewBtn.performClick();
                return true;
            } else
            {
                // whether we have logout
                if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(m_iLogID))
                {
                    Log.e(TAG, " NET_DVR_Logout is failed!");
                    m_iLogID = -1;
                    return false;
                }
                return true;
            }
        } catch (Exception err)
        {
            Log.e(TAG, "error: " + err.toString());
            return false;
        }

    }

    private  int loginDevice(String ip, int port, String username, String psd)
    {
        // get instance
        m_oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
        if (null == m_oNetDvrDeviceInfoV30)
        {
            Log.e(TAG, "HKNetDvrDeviceInfoV30 new is failed!");
            return -1;
        }
		/*
		 * String strIP = m_oIPAddr.getText().toString(); int nPort =
		 * Integer.parseInt(m_oPort.getText().toString()); String strUser =
		 * m_oUser.getText().toString(); String strPsd =
		 * m_oPsd.getText().toString();
		 */
        //ip
        //String strIP = m_oIPAddr;
        //端口
        //int nPort = Integer.parseInt(m_oPort);
        //用户名
        //String strUser = m_oUser;
        //密码
        //String strPsd = m_oPsd;
        // call NET_DVR_Login_v30 to login on, port 8000 as default
        int iLogID = HCNetSDK.getInstance().NET_DVR_Login_V30(ip, port,
                username, psd, m_oNetDvrDeviceInfoV30);
        if (iLogID < 0)
        {
            Log.e(TAG, "NET_DVR_Login is failed!Err:"
                    + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return -1;
        }
        if (m_oNetDvrDeviceInfoV30.byChanNum > 0)
        {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartChan;
            m_iChanNum = m_oNetDvrDeviceInfoV30.byChanNum;
        } else if (m_oNetDvrDeviceInfoV30.byIPChanNum > 0)
        {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartDChan;
            m_iChanNum = m_oNetDvrDeviceInfoV30.byIPChanNum
                    + m_oNetDvrDeviceInfoV30.byHighDChanNum * 256;
        }
        Log.i(TAG, "NET_DVR_Login is Successful!");
        return iLogID;
    }

    /**
     * @return exception instance
     */
    private  ExceptionCallBack getExceptiongCbf()
    {
        ExceptionCallBack oExceptionCbf = new ExceptionCallBack()
        {
            public void fExceptionCallBack(int iType, int iUserID, int iHandle)
            {
                System.out.println("recv exception, type:" + iType);
                Log.e(TAG,"recv exception, type:" + iType);
            }
        };
        return oExceptionCbf;
    }

    public  void   previewSingle(int td,RealPlayCallBack callback){
        if (m_iChanNum > 1)
        // preview more than a channel
        {
            if (m_iPlayID < 0)
            {
                startSinglePreview(td,callback);
            } else
            {
                stopSinglePreview();
            }
						/*
						 * if(!m_bMultiPlay) {
						 *
						 * startMultiPreview(); m_bMultiPlay = true;
						 * m_oPreviewBtn.setText("Stop"); } else {
						 * stopMultiPreview(); m_bMultiPlay = false;
						 * m_oPreviewBtn.setText("Preview"); }
						 */
        } else
        // preivew a channel
        {
            if (m_iPlayID < 0)
            {
                startSinglePreview(td,callback);
            } else
            {
                stopSinglePreview();
            }
        }
    }

    /**
     * 单个预览
     */
    public void startSinglePreview(int TD,RealPlayCallBack callback)
    {
        if (m_iPlaybackID >= 0)
        {
            Log.i(TAG, "Please stop palyback first");
            return;
        }
        //RealPlayCallBack fRealDataCallBack = getRealPlayerCbf();
        if (callback == null)
        {
            Log.e(TAG, "fRealDataCallBack object is failed!");
            return;
        }
        Log.i(TAG, "m_iStartChan:" + m_iStartChan);

        NET_DVR_PREVIEWINFO previewInfo = new NET_DVR_PREVIEWINFO();

        //HCNetSDK.getInstance().NET_DVR_GetDVRConfig()

        //通道号，目前设备模拟通道号从1开始，数字通道的起始通道号通过NET_DVR_GetDVRConfig（配置命令NET_DVR_GET_IPPARACFG_V40）获取（dwStartDChan）。
        /*previewInfo.lChannel = m_iStartChan+13;
        previewInfo.lChannel = m_iStartChan+TD;*/
        /*previewInfo.lChannel = TD + 32;
        m_td=TD+32;*/
        previewInfo.lChannel = TD+32;
        m_td=previewInfo.lChannel;
        //previewInfo.lChannel = TD;

        /*if(TD>0){
            previewInfo.lChannel = TD + 32;
            //m_iStartChan = TD+ 32;
           // m_iStartChan=0;
            *//*previewInfo.lChannel = TD;
            m_iStartChan = TD;*//*
           *//* nomor_port=m_oPort;
            nomor_td=TD;*//*
        }*/


        previewInfo.dwStreamType = 0; // 码流类型：0-主码流，1-子码流，2-码流3，3-虚拟码流，以此类推
        previewInfo.bBlocked = 0;//0- 非阻塞取流，1- 阻塞取流。如果阻塞取流，SDK内部connect失败将会有5s的超时才能够返回，不适合于轮询取流操作。
        //连接方式：0- TCP方式，1- UDP方式，2- 多播方式，3- RTP方式，4-RTP/RTSP，5-RTP/HTTP
        previewInfo.dwLinkMode =0;
        // HCNetSDK start preview
        m_iPlayID = HCNetSDK.getInstance().NET_DVR_RealPlay_V40(m_iLogID,
                previewInfo, callback);
        if (m_iPlayID < 0)
        {
            Log.e(TAG, "NET_DVR_RealPlay is failed!Err:"
                    + HCNetSDK.getInstance().NET_DVR_GetLastError());
			/*ToastUtil.setToast(MonitorActivity.this,  "NET_DVR_RealPlay is failed!Err:"
					+ HCNetSDK.getInstance().NET_DVR_GetLastError());*/
            return;
        }

        Log.i(TAG, "NetSdk Play sucess ***********************3***************************");
        //m_oPreviewBtn.setText("停止预览");
    }

    private void stopSinglePreview()
    {
        if (m_iPlayID < 0)
        {
            Log.e(TAG, "m_iPlayID < 0");
            return;
        }

        // net sdk stop preview
        if (!HCNetSDK.getInstance().NET_DVR_StopRealPlay(m_iPlayID))
        {
            Log.e(TAG, "StopRealPlay is failed!Err:"
                    + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return;
        }

        m_iPlayID = -1;
        stopSinglePlayer();
    }

    public  void stopSinglePlayer()
    {
        Player.getInstance().stopSound();
        // player stop play
        if (!Player.getInstance().stop(m_iPort))
        {
            Log.e(TAG, "stop is failed!");
            return;
        }

        if (!Player.getInstance().closeStream(m_iPort))
        {
            Log.e(TAG, "closeStream is failed!");
            return;
        }
        if (!Player.getInstance().freePort(m_iPort))
        {
            Log.e(TAG, "freePort is failed!" + m_iPort);
            return;
        }
        m_iPort = -1;
    }

    /**
     * @fn Cleanup
     * @author zhuzhenlei
     * @brief cleanup
     *            [in]
     *            [out]
     * @return NULL
     */
    public  void Cleanup()
    {
        // release player resource

        Player.getInstance().freePort(m_iPort);
        m_iPort = -1;

        // release net SDK resource
        HCNetSDK.getInstance().NET_DVR_Cleanup();
    }

    /**
     * 云台开始向左
     * @param PTZCommand
     * @param start 0:开始 1:结束
     * @return
     */
    public  boolean PTZControl (int PTZCommand,int start) {
        if (!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(
                m_iLogID, m_td, PTZCommand, start))
        {
            Log.e(TAG, "start PAN_RIGHT failed with error code: " + HCNetSDK.getInstance().NET_DVR_GetLastError());

            return false;
        } else
        {
            Log.i(TAG, "start PAN_RIGHT succ");
            return true;
        }
    }

    /**
     * 实时播放回调
     * @return callback instance
     */
    /* private RealPlayCallBack getRealPlayerCbf()
    {
        RealPlayCallBack cbf = new RealPlayCallBack()
        {
            public void fRealDataCallBack(int iRealHandle, int iDataType,
                                          byte[] pDataBuffer, int iDataSize)
            {
                // player channel 1
                // MonitorActivity.this.processRealData(1, iDataType,
                // pDataBuffer, iDataSize, Player.STREAM_REALTIME);
                MonitorActivity.this.processRealData(1, iDataType, pDataBuffer,
                        iDataSize, Player.STREAM_REALTIME);
            }
        };
        return cbf;
    }*/
    /**
     * 回放回调
     * @return callback instance
     */
    /*private PlaybackCallBack getPlayerbackPlayerCbf()
    {
        PlaybackCallBack cbf = new PlaybackCallBack()
        {
            @Override
            public void fPlayDataCallBack(int iPlaybackHandle, int iDataType,
                                          byte[] pDataBuffer, int iDataSize)
            {
                // player channel 1
                MonitorActivity.this.processRealData(1, iDataType, pDataBuffer,
                        iDataSize, Player.STREAM_FILE);
            }
        };
        return cbf;
    }*/

}
