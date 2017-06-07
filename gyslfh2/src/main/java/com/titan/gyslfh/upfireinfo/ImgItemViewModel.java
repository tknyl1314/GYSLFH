package com.titan.gyslfh.upfireinfo;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.util.Log;

import com.titan.BaseViewModel;
import com.titan.gyslfh.AlarmInfoInterface;

/**
 * Created by whs on 2017/5/18
 */

public class ImgItemViewModel extends BaseViewModel {

    //public ObservableField<Image> img=new ObservableField<>();
    //图片路径
    public ObservableField<String> imgpath=new ObservableField<>();
    //
    public final ObservableField<Integer> position=new ObservableField<>();


    private AlarmInfoInterface alarmInfoInterface;

    public ImgItemViewModel(Context context,AlarmInfoInterface iUpAlarm) {
        this.mContext=context;
        this.alarmInfoInterface=iUpAlarm;
        imgpath.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Log.e("TITAN", "onPropertyChanged: ");
            }
        });

        //imgpath.addOnPropertyChangedCallback();
    }

    /**
     * 展示照片详细
     */
    public void onImgTouch(){alarmInfoInterface.showImageDetail(position.get());
    }
}
