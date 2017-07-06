package com.titan.gyslfh.layercontrol;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LayerViewStateChangedEvent;
import com.esri.arcgisruntime.mapping.view.LayerViewStateChangedListener;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.titan.gis.WeatherUtil;
import com.titan.model.FireRiskModel;
import com.titan.model.TitanLayer;
import com.titan.newslfh.R;
import com.titan.newslfh.databinding.LayercontrolFragBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WHS on 2017/1/4
 * 图层控制
 */

public class LayerControlFragment extends DialogFragment implements ILayerControl {



    private LayercontrolFragBinding mViewDataBinding;
    private LayerControlViewModel mViewModel;

    private  static LayerControlFragment Singleton;

    LayersAdapter mAdapter;

    //private static  LayerList mLayerList;

    public static List<TitanLayer> getmLayerList() {
        return mLayerList;
    }

    private static  List<TitanLayer> mLayerList=new ArrayList<>();
    private static ArcGISMap marcGISMap;
    //加载图层的序号
    private int[] layers=new int[]{0,1,2,3,4,5,6,7,8};

    private static MapView mMapView;

    private static GraphicsOverlay mGraphicsOverlay;



    /**
     * Create a new instance of
     */
    public static LayerControlFragment newInstance() {
        return new LayerControlFragment();
    }

    public static LayerControlFragment getInstance(ArcGISMap arcGISMap, MapView mapView,GraphicsOverlay graphicsOverlay){


        //return  new LayerControlFragment();
        if(Singleton==null){
            Singleton=new LayerControlFragment();
            marcGISMap=arcGISMap;
            mMapView=mapView;
            mGraphicsOverlay=graphicsOverlay;
        }
        return Singleton;

    }
    public static LayerControlFragment getInstance(List<TitanLayer> layerList){


        //return  new LayerControlFragment();
        if(Singleton==null){
            Singleton=new LayerControlFragment();
            mLayerList=layerList;
        }
        return Singleton;

    }

    public void setViewModel(LayerControlViewModel viewModel) {
        mViewModel = viewModel;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //此处可以设置Dialog的style等等
        super.onCreate(savedInstanceState);

        /*DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
        getDialog().getWindow().setLayout( dm.widthPixels, getDialog().getWindow().getAttributes()..height );*/

        //this.getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //若想无法直接点击外部取消dialog 可设置为false

        setCancelable(false);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogFragment);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewDataBinding=DataBindingUtil.inflate(inflater, R.layout.layercontrol_frag,container,true);
        mViewDataBinding.setViewmodel(mViewModel);
        //mLayerList.clear();

        if(mLayerList.isEmpty()){
            loadLyaers();
            mViewModel.mLayerList.set(mLayerList);
        }
        return mViewDataBinding.getRoot();
    }


    /**
     * 设置dialog弹出来的位置
     */
    /*private void setDialogLocation() {
        //获取通知栏高度
        Rect rect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        getDialog().getWindow().setGravity(Gravity.RIGHT | Gravity.TOP);
        *//**
         *   int[] ll=new int[2];
             MapFragment.iv_more.getLocationInWindow(ll);
         *   如果iv_more在左边，也可以这样做 lp.x=ll[0],因为我不管设置Gravity在左或右边，原点一直在右边
         *//*
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        //因为iv_more按钮距离右边是16dp
        //lp.x = ConversionUtil.dip2px(getActivity(), 16);
        //设置n - rect.top + MapFragment.iv_more.getHeight() 那么dialogment的高度就和iv_more相同，我在往下移动10dp
        //lp.y = MapFragment.location_iv_more[1] - rect.top + MapFragment.iv_more.getHeight() + ConversionUtil.dip2px(getActivity(), 10);
        getDialog().getWindow().setAttributes(lp);
    }*/






    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /**
     * 加载专题图层
     */
    private void loadLyaers(){
        for (int i = 0; i <layers.length; i++) {
            String layerurl=getActivity().getString(R.string.gisserverhost)+"/"+layers[i];
            ServiceFeatureTable serviceFeatureTable = new ServiceFeatureTable(layerurl);
            //设置缓存模式
            serviceFeatureTable.setFeatureRequestMode(ServiceFeatureTable.FeatureRequestMode.ON_INTERACTION_CACHE);
            //String name=serviceFeatureTable.getLayerInfo().getServiceLayerName();
            //serviceFeatureTable.setFeatureRequestMode(ServiceFeatureTable.FeatureRequestMode.MANUAL_CACHE);
            FeatureLayer featureLayer = new FeatureLayer(serviceFeatureTable);
            //featuretables.add(serviceFeatureTable);
            //featurelayers.add(featureLayer);
            featureLayer.setVisible(false);
            TitanLayer titanLayer=new TitanLayer();
            String name=featureLayer.getName();
            titanLayer.setName(name);
            titanLayer.setVisiable(false);
            titanLayer.setUrl(layerurl);
            mLayerList.add(titanLayer);
            marcGISMap.getOperationalLayers().add(featureLayer);
        }
        mMapView.addLayerViewStateChangedListener(new LayerViewStateChangedListener() {
            @Override
            public void layerViewStateChanged(LayerViewStateChangedEvent layerViewStateChangedEvent) {
                // get the layer which changed it's state
                Layer layer = layerViewStateChangedEvent.getLayer();

                // get the View Status of the layer
                // View status will be either of ACTIVE, ERROR, LOADING, NOT_VISIBLE, OUT_OF_SCALE, UNKNOWN
                String viewStatus = layerViewStateChangedEvent.getLayerViewStatus().iterator().next().toString();

                final int layerIndex = marcGISMap.getOperationalLayers().indexOf(layer);
                String name=layerViewStateChangedEvent.getLayer().getName();
                if(layerIndex>=0){
                    mLayerList.get(layerIndex).setName(name);
                    viewStatusString(layerIndex,viewStatus);
                }
            }
        });

    }

    /**
     * The method looks up the view status of the layer and returns a string which is displayed
     *
     * @param status View Status of the layer
     * @return String equivalent of the status
     */
    private void viewStatusString (int layerindex,String status) {

        switch(status) {
            case "ACTIVE":
                mLayerList.get(layerindex).setVisiable(true);
                mViewModel.snackbarText.set(mLayerList.get(layerindex).getName()+"已加载");
                //return getActivity().getString(R.string.active);
            case "ERROR":
                mViewModel.snackbarText.set(mLayerList.get(layerindex).getName()+getActivity().getString(R.string.error));
                //return getActivity().getString(R.string.error);
            case "LOADING":
                mViewModel.snackbarText.set(mLayerList.get(layerindex).getName()+getActivity().getString(R.string.loading));
                //return getActivity().getString(R.string.loading);

            case "NOT_VISIBLE":
                mLayerList.get(layerindex).setVisiable(false);
                mViewModel.snackbarText.set(mLayerList.get(layerindex).getName()+"已移除");
                //return getActivity().getString(R.string.notVisible);

            case "OUT_OF_SCALE":
                mViewModel.snackbarText.set(mLayerList.get(layerindex).getName()+getActivity().getString(R.string.outOfScale));
                //return getActivity().getString(R.string.outOfScale);

        }

        //return getActivity().getString(R.string.unknown);

    }





    /**
     * 初始化图层控制图层列表
     */
    private void intiRecyclerView() {
        mAdapter=new LayersAdapter(getActivity(),mLayerList,this);
        mViewDataBinding.rvLayers.setAdapter(mAdapter);
        mViewDataBinding.rvLayers.setLayoutManager(new LinearLayoutManager(getActivity()));
    }



    @Override
    public void onStart() {
        //参数在onCreate中设置无效果
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER_HORIZONTAL);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置dialog背景透明
        }
    }





    @Override
    public void onResume() {
        intiRecyclerView();
        super.onResume();
    }


    @Override
    public void closeLayerControl() {
        dismiss();
    }

    @Override
    public void showBaseMap(int position) {
        selectBasemap(position);
    }

    @Override
    public void onCheckLayer(int index, boolean isvisable) {
       marcGISMap.getOperationalLayers().get(index).setVisible(isvisable);
    }

    @Override
    public void showWeatherLayer(FireRiskModel fireRiskModel) {
        List<Graphic> graphics= WeatherUtil.creatFireRiskGraphicsOverlay(fireRiskModel);
        mGraphicsOverlay.getGraphics().addAll(graphics);
        //设置透明度
        mGraphicsOverlay.setOpacity((float) 0.6);
        mMapView.setViewpoint(new Viewpoint(mGraphicsOverlay.getExtent()));
    }

    @Override
    public void closeWeatherLayer() {
        mGraphicsOverlay.getGraphics().clear();
    }

    /**
     * 选择底图
     * @param position
     */
    public void selectBasemap(int position){
        switch (position){
            //基础图
            case 0:
                marcGISMap.setBasemap(Basemap.createStreets());
                break;
            //影像图
            case 1:
                marcGISMap.setBasemap(Basemap.createImagery());
                break;
            //森林火险等级图
            case 2:
                marcGISMap.setBasemap(Basemap.createStreets());
                break;
        }
    }

}
