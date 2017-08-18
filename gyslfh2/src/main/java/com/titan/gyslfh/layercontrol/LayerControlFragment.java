package com.titan.gyslfh.layercontrol;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.loadable.LoadStatusChangedEvent;
import com.esri.arcgisruntime.loadable.LoadStatusChangedListener;
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


    public  List<TitanLayer> getmLayerList() {
        return mLayerList;
    }

    private static  List<TitanLayer> mLayerList;
    private static ArcGISMap marcGISMap;
    //加载图层的序号
    private int[] layers=new int[]{1,2,3,4,5,6,7,8,9};

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
            marcGISMap=arcGISMap;
            mMapView=mapView;
            mGraphicsOverlay=graphicsOverlay;
            Singleton=new LayerControlFragment();
        }
        return Singleton;

    }
    /*public static LayerControlFragment getInstance(List<TitanLayer> layerList){


        //return  new LayerControlFragment();
        if(Singleton==null){
            mLayerList=layerList;
            Singleton=new LayerControlFragment();
        }
        return Singleton;

    }*/

    public void setViewModel(LayerControlViewModel viewModel) {
        mViewModel = viewModel;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //此处可以设置Dialog的style等等
        super.onCreate(savedInstanceState);

        setCancelable(false);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogFragment);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewDataBinding=DataBindingUtil.inflate(inflater, R.layout.layercontrol_frag,container,true);
        mViewDataBinding.setViewmodel(mViewModel);
        //mLayerList.clear();

        if(mLayerList==null||mLayerList.isEmpty()){
            //loadLyaers(
            mLayerList=new ArrayList<>();
            initLayers();
            mViewModel.mLayerList.set(mLayerList);
        }else {
            //mAdapter.notifyDataSetChanged();
            intiRecyclerView();
        }
        return mViewDataBinding.getRoot();
    }

    /**
     * 初始图层信息
     */
    private void initLayers() {
        //动态图层
        final ArcGISMapImageLayer dlayer=new ArcGISMapImageLayer(getActivity().getString(R.string.zturl));
        dlayer.setVisible(false);
        dlayer.addLoadStatusChangedListener(new LoadStatusChangedListener() {
            @Override
            public void loadStatusChanged(LoadStatusChangedEvent loadStatusChangedEvent) {
                String mapLoadStatus = loadStatusChangedEvent.getNewLoadStatus().name();
                // set the status in the TextView accordingly
                switch (mapLoadStatus) {
                    case "LOADING":
                        Log.e("Titan","LOADING");
                        break;

                    case "FAILED_TO_LOAD":
                        Log.e("Titan","图层加载异常");
                        break;

                    case "NOT_LOADED":
                        Log.e("Titan","NOT_LOADED");

                        break;

                    case "LOADED":
                        //List<MapServiceLayerIdInfo>  d=  dad.getMapServiceInfo().getLayerInfos();
                        for(int i:layers){
                            String layerurl=getActivity().getString(R.string.gisserverhost)+"/"+i;
                            String layername=dlayer.getMapServiceInfo().getLayerInfos().get(i).getName();
                            int layerid= (int) dlayer.getMapServiceInfo().getLayerInfos().get(i).getId();
                            TitanLayer layer=new TitanLayer();
                            layer.setName(layername);
                            layer.setIndex(layerid);
                            layer.setUrl(layerurl);
                            layer.setVisiable(false);
                            mLayerList.add(layer);
                            ServiceFeatureTable service=new ServiceFeatureTable(layerurl);
                            FeatureLayer flayer=new FeatureLayer(service);
                            flayer.setVisible(false);
                            marcGISMap.getOperationalLayers().add(flayer);
                        }
                        intiRecyclerView();
                        break;

                    default :
                        break;
                }


            }
        });
        marcGISMap.getOperationalLayers().add(dlayer);
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
                if(layerIndex>0){
                    viewStatusString(layerIndex-1,viewStatus);
                }
            }
        });
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

                mViewModel.snackbarText.set(mLayerList.get(layerindex).getName()+"加载失败");
                //return getActivity().getString(R.string.error);
            case "LOADING":
                mViewModel.snackbarText.set(mLayerList.get(layerindex).getName()+"正在加载");
                //return getActivity().getString(R.string.loading);

            case "NOT_VISIBLE":
                if (mLayerList!=null&&!mLayerList.isEmpty()){
                    mLayerList.get(layerindex).setVisiable(false);
                    mViewModel.snackbarText.set(mLayerList.get(layerindex).getName()+"已移除");
                }
                //return getActivity().getString(R.string.notVisible);

            case "OUT_OF_SCALE":
                if (mLayerList!=null&&!mLayerList.isEmpty()){
                    mViewModel.snackbarText.set(mLayerList.get(layerindex).getName()+"超出范围");
                }
                //return getActivity().getString(R.string.outOfScale);

        }


    }





    /**
     * 初始化图层控制图层列表
     */
    private void intiRecyclerView() {
        /*if(mAdapter!=null){
            mAdapter.replaceData(mLayerList);
            return;
        }*/
        if (mLayerList!=null&&!mLayerList.isEmpty()) {
            LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
            mViewDataBinding.rvLayers.setLayoutManager(new LinearLayoutManager(getActivity()));
            mAdapter = new LayersAdapter(getActivity(), mLayerList, this);
            mViewDataBinding.rvLayers.setAdapter(mAdapter);
        }
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

    /**
     * 图层点击
     * @param index
     * @param isvisable
     */
    @Override
    public void onCheckLayer(int index, boolean isvisable) {
        marcGISMap.getOperationalLayers().get(index+1).setVisible(isvisable);
        mLayerList.get(index).setVisiable(isvisable);
    }

    @Override
    public void showWeatherLayer(FireRiskModel fireRiskModel) {
        List<Graphic> graphics= WeatherUtil.creatFireRiskGraphicsOverlay(fireRiskModel);
        mGraphicsOverlay.getGraphics().addAll(graphics);
        //设置透明度
        mGraphicsOverlay.setOpacity(WeatherUtil.OPACITY);
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
                marcGISMap.setBasemap(Basemap.createOpenStreetMap());
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
