package com.titan.gyslfh.layercontrol;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.ObservableField;

import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.LayerList;
import com.titan.data.source.DataRepository;

/**
 * Created by whs on 2017/5/16
 */

public class LayerItemViewModel extends LayerControlViewModel {
    //图层信息
    public final   ObservableField<Layer> mLayer=new ObservableField<>();
    //是否选中
    public final   ObservableField<Boolean> ischeck=new ObservableField<>(false);
    //图层index
    public final   ObservableField<Integer> layerindex=new ObservableField<>();

    public String getmLayerName() {
        return mLayer.get().getName();
    }

    public void setmLayerName(String mLayerName) {
        mLayer.get().setName(mLayerName);
    }

    @Bindable
    public String mLayerName;

    ILayerControl mILayerControl;

    private DataRepository mDataRepository;

    public LayerItemViewModel(Context context, ILayerControl iLayerControl,DataRepository dataRepository, LayerList layerList) {
        super(context, iLayerControl,dataRepository,layerList);
        this.mILayerControl=iLayerControl;
        /*ischeck.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {

            }
        });*/
        //mLayerName=mLayer.get().getName();
        //mLayer.get().getName();
    }

    public void onLayerCheck(){
        ischeck.set(!ischeck.get());
        mILayerControl.onCheckLayer(layerindex.get(),ischeck.get());
        /*boolean bb=ischeck.get();
        if(!TextUtils.isEmpty(getmLayerName())){
            Log.e("图层名称",getmLayerName());
            mILayerControl.onCheckLayer(getmLayerName());

        }*/

    }
}
