package com.titan.gyslfh.alarminfo;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.gson.Gson;
import com.titan.BaseViewModel;
import com.titan.data.source.DataRepository;
import com.titan.data.source.DataSource;
import com.titan.gyslfh.TitanApplication;
import com.titan.newslfh.BR;
import com.titan.util.LoadDataScrollController;

/**
 * Created by whs on 2017/5/3
 * 接警信息列表
 */

public class AlarmInfoViewModel extends BaseViewModel implements LoadDataScrollController.OnRecycleRefreshListener {

    public final ObservableList<AlarmInfoModel.AlarmInfo> mAlarmInfos = new ObservableArrayList<>();


    public final ObservableField<Integer> totalcount = new ObservableField<>();
    //火警ID
    public  final ObservableField<String>  alarmId=new ObservableField<>();
    //empty label
    public final ObservableField<String> noItemLabel = new ObservableField<>();
    //empty iocn
    public final ObservableField<Drawable> noItemIconRes = new ObservableField<>();

    private AlarmInfoListInterface mAlarmInfoItemNav;


    //请求页码数
    private int pageindex=1;
    //最大请求页面数
    private int maxpageindex=0;
    //每次请求条数
    private final  int pagecount=10;
    //查询参数
    private String querystr="";


    public AlarmInfoViewModel(Context context, AlarmInfoListInterface alarmInfoItemNav,DataRepository dataRepository){

        this.mAlarmInfoItemNav=alarmInfoItemNav;
        this.mContext=context;
        this.mDataRepository=dataRepository;

    }



    @Bindable
    public boolean isEmpty() {
        return mAlarmInfos.isEmpty();
    }


    /**
     * 首次加载
     */
    public void start() {
        pageindex=1;
        loadData(0,true);
        //mAlarmInfoItemNav.refreshData(mAlarmInfos);
    }

    /**
     * 加载数据
     */
    public void loadData(final int type, boolean showLoadingUI) {
        if(showLoadingUI) {
            dataLoading.set(true);

        }
        mDataRepository.getAlarmInfoList(querystr, TitanApplication.mUserModel.getDqid(), pageindex + "", pagecount+"", new DataSource.saveCallback() {
            @Override
            public void onFailure(String info) {
                dataLoading.set(false);
                Log.e("error",info.toString());
                snackbarText.set("获取数据异常："+info);
            }

            @Override
            public void onSuccess(String data) {
                dataLoading.set(false);
                Gson gson=new Gson();
                AlarmInfoModel infos=gson.fromJson(data,AlarmInfoModel.class);
                totalcount.set(Integer.valueOf(infos.getRecordCount()));
                //获取页数
                maxpageindex= (int) Math.ceil(totalcount.get().doubleValue()/pagecount);
                switch (type){

                    case 0:
                        //初始化
                        mAlarmInfos.clear();

                    case 1:
                        //加载更多
                        mAlarmInfos.addAll(infos.getDs());
                        if(type==0){
                            snackbarText.set("已是最新");
                        }else {
                            snackbarText.set("总共有"+totalcount.get()+"条记录  "+"已加载"+mAlarmInfos.size()+"条");
                        }

                        break;

                }
                //mAlarmInfos.clear();
                //mAlarmInfos.addAll(infos.getDs());

                //snackbarText.set("总共有"+totalcount.get()+"条记录  "+"已加载"+mAlarmInfos.size()+"条");
                notifyPropertyChanged(BR.empty);
                //停止更新
                mAlarmInfoItemNav.stopUpdate();

            }
        });
    }

    /**
     * 下拉刷新(更新UI)
     */
    @Override
    public void refresh() {
        pageindex=1;
        loadData(0,true);


    }

    /**
     * 加载更多
     */
    @Override
    public void loadMore() {

        pageindex++;
        if(pageindex>maxpageindex){
            snackbarText.set("已经是最后了");
            return;
        }
        loadData(1,true);


    }
}
