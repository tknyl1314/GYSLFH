package com.titan.gyslfh.layercontrol;

import android.content.Context;
import android.databinding.ObservableArrayMap;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableMap;

import com.titan.BaseViewModel;
import com.titan.data.source.DataRepository;
import com.titan.data.source.DataSource;
import com.titan.model.FireRiskModel;
import com.titan.model.TitanLayer;
import com.titan.util.DateUtil;
import com.titan.util.NetUtil;

import java.util.List;

/**
 * Created by whs on 2017/5/11
 */

public class LayerControlViewModel extends BaseViewModel {


   // public ObservableField<LayerList> mLayerList=new ObservableField<>();
   public ObservableField<List<TitanLayer>> mLayerList=new ObservableField<>();
    //选中底图index
    public ObservableInt selectposition=new ObservableInt(0);
    ObservableMap mCheckInfo=new ObservableArrayMap();




    private Context mContext;
    private ILayerControl mLayerControl;
    private DataRepository mDataRepository;
    public LayerControlViewModel(Context context, ILayerControl iLayerControl, DataRepository dataRepository,List<TitanLayer> layerlist){
        this.mContext=context;
        this.mLayerControl=iLayerControl;
        this.mLayerList.set(layerlist);
        this.mDataRepository=dataRepository;
    }



    /**
     * 关闭图层控制弹框
     */
    public void closeLayerControl(){
        mLayerControl.closeLayerControl();
    }

    /**
     * 选择底图
     */
    public void selectBasemap(int position){
        if(NetUtil.checkNetState(mContext)){
            if(position==2){
                String date= DateUtil.getYMD();
                String hour=DateUtil.getHour();

                mDataRepository.getFireRiskInfo(date,hour, "F", new DataSource.getWeatherCallback() {
                    @Override
                    public void onFailure(String info) {
                        snackbarText.set(info);
                    }

                    @Override
                    public void onSuccess(FireRiskModel fireRiskModel) {

                        mLayerControl.showWeatherLayer(fireRiskModel);

                    }


                });
            }else {
                mLayerControl.closeWeatherLayer();
                mLayerControl.showBaseMap(position);

            }
        }


    }


}
