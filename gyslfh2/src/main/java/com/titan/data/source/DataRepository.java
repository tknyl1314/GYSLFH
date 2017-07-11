package com.titan.data.source;

import android.content.Context;

import com.titan.data.source.local.LocalDataSource;
import com.titan.data.source.remote.RemotDataSource;
import com.titan.data.source.remote.RemoteDataSource;
import com.titan.gyslfh.TitanApplication;
import com.titan.gyslfh.backalarm.BackAlarmModel;
import com.titan.model.TrackPoint;

/**
 * Created by whs on 2017/5/18
 */

public class DataRepository implements DataSource ,RemotDataSource{
    private Context mContext;

    private static DataRepository INSTANCE = null;


    private final RemoteDataSource mRemoteDataSource;

    private final LocalDataSource mLocalDataSource;

    public static DataRepository getInstance(RemoteDataSource remoteDataSource, LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new DataRepository(remoteDataSource, localDataSource);
        }
        return INSTANCE;
    }

    public DataRepository(RemoteDataSource remoteDataSource, LocalDataSource localDataSource) {
        //this.mContext=context;
        this.mRemoteDataSource = remoteDataSource;
        this.mLocalDataSource = localDataSource;
    }



    @Override
    public void uplaodAlarmInfo(String infojson,uploadCallback callback) {
          mRemoteDataSource.uplaodAlarmInfo(infojson,callback);
        //return false;
    }

    @Override
    public void saveTrackPoint(TrackPoint trackPoint,saveCallback callback) {
        //有网络是上传轨迹 无网络本地保存
        if(TitanApplication.IntetnetISVisible){
            trackPoint.setStatus(1);
            mLocalDataSource.saveTrackPoint(trackPoint,callback);
            mRemoteDataSource.saveTrackPoint(trackPoint,callback);
        }else {
            trackPoint.setStatus(0);
            mLocalDataSource.saveTrackPoint(trackPoint,callback);
        }


    }

    @Override
    public void getUnDealAlarmCount(String dqid, uploadCallback callback) {
        mRemoteDataSource.getUnDealAlarmCount(dqid,callback);
    }

    @Override
    public void getAlarmInfoDetail(String alarmid,saveCallback callback) {
        mRemoteDataSource.getAlarmInfoDetail(alarmid,callback);

    }

    @Override
    public void getAlarmInfoList(String querystr, String dqid, String index, String number, saveCallback callback) {
        mRemoteDataSource.getAlarmInfoList(querystr,dqid,index,number,callback);
    }

    @Override
    public void onBackAlarm(BackAlarmModel backAlarmModel, saveCallback callback) {
        mRemoteDataSource.onBackAlarm(backAlarmModel,callback);
    }


    /**
     * @param date
     * @param hour
     * @param ThematicType
     * H 湿度  R 降雨 T 温度 W 风速  F 火险
     * @param callback
     */
    public void getFireRiskInfo(String date, String hour, String ThematicType, getWeatherCallback callback) {
        mRemoteDataSource.getFireRiskInfo(date,hour,ThematicType,callback);
    }

    @Override
    public void getDvrInfo(String str, getCallback callback) {
        mRemoteDataSource.getDvrInfo(str,callback);
    }

    @Override
    public void checkLogin(String username, String psd, String cid, getCallback callback) {
        mRemoteDataSource.checkLogin(username,psd,cid,callback);
    }
}
