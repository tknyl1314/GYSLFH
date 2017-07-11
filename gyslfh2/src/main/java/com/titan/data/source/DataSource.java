/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.titan.data.source;

import com.titan.gyslfh.backalarm.BackAlarmModel;
import com.titan.model.FireRiskModel;
import com.titan.model.TrackPoint;

/**
 * Main entry point for accessing tasks data.
 */
public interface DataSource {

    /*interface LoadTasksCallback {

        void onTasksLoaded(List<Task> tasks);

        void onDataNotAvailable();
    }

    interface GetTaskCallback {

        void onTaskLoaded(Task task);

        void onDataNotAvailable();
    }*/

    /**
     * 获取气象信息
     */
    interface getWeatherCallback {

        void onFailure(String info);

        void onSuccess(FireRiskModel fireRiskModel);
    }


    interface uploadCallback {

        void onFailure(String info);

        void onSuccess(String data);
    }


    interface saveCallback {

        void onFailure(String info);

        void onSuccess(String data);
    }


    //上报火情和接警录入
    void uplaodAlarmInfo(String infojson,uploadCallback callback);
    //上传轨迹
    void saveTrackPoint(TrackPoint trackPoint,saveCallback callback);
    //火情未回警数量
    void getUnDealAlarmCount(String dqid,uploadCallback callback);
    //获取火险等级图层
    //void getFireRiskInfo(String date,String hour,String ThematicType,getWeatherCallback callback);

    void getAlarmInfoDetail(String alarmid,saveCallback callback);

    void getAlarmInfoList(String querystr,String dqid,String index,String number ,saveCallback callback);

    void onBackAlarm(BackAlarmModel backAlarmModel,saveCallback callback);

    //void getDvrInfo();


}
