package com.titan.gyslfh.layercontrol;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.esri.arcgisruntime.layers.Layer;
import com.titan.data.source.DataRepository;
import com.titan.model.TitanLayer;

import java.util.List;

/**
 * Created by whs on 2017/5/16
 */

public class LayerItemViewModel extends LayerControlViewModel {
    //图层信息
    public final   ObservableField<Layer> mLayer=new ObservableField<>();
    //是否选中
    public final  ObservableBoolean ischeck=new ObservableBoolean(false);

    //图层index
    public final   ObservableField<Integer> layerindex=new ObservableField<>();


    public final ObservableField<String>  mLayerName=new ObservableField<>();

    public String getmLayerName() {
        return mLayer.get().getName();
    }

    public void setmLayerName(String mLayerName) {
        mLayer.get().setName(mLayerName);
    }



    ILayerControl mILayerControl;

    private DataRepository mDataRepository;

    public LayerItemViewModel(Context context, ILayerControl iLayerControl,DataRepository dataRepository, List<TitanLayer> layerList) {
        super(context, iLayerControl,dataRepository,layerList);
        this.mILayerControl=iLayerControl;
        //mLayerName.set(mLayer.get().getName());

    }

    public void onLayerCheck(){
        ischeck.set(!ischeck.get());
        mILayerControl.onCheckLayer(layerindex.get(),ischeck.get());
    }
}
