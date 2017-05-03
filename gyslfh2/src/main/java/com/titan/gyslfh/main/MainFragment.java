package com.titan.gyslfh.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.titan.newslfh.databinding.MainFragBinding;

/**
 * Created by whs on 2017/4/28
 */

public class MainFragment extends Fragment {
    private MainViewModel mMainViewModel;

    private MainFragBinding mMainFragBinding;

    LocationDisplay mLocationDisplay;

    /**地图坐标系*/
    //SpatialReference spatialReference=null;

    public MainFragment() {
        // Requires empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        //mMainViewModel.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainFragBinding = MainFragBinding.inflate(inflater, container, false);

        //mMainFragBinding.setView(this);
        mMainFragBinding.setViewmodel(mMainViewModel);

        setHasOptionsMenu(true);

        View root = mMainFragBinding.getRoot();

        return root;
    }

    public void setViewModel(MainViewModel viewModel) {
        mMainViewModel = viewModel;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        intiMapView();
    }




    /**
     * 初始化地图
     */
    private void intiMapView() {
        //初始化地图
        ArcGISMap mMap = new ArcGISMap(Basemap.createImagery());
        mMainFragBinding.mapview.setMap(mMap);
        //去除版权声明
        mMainFragBinding.mapview.setAttributionTextVisible(false);

        mLocationDisplay = mMainFragBinding.mapview.getLocationDisplay();
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
        //适合徒步
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
        //spatialReference=mMap.getSpatialReference();

        // Listen to changes in the status of the location data source.
        mLocationDisplay.addDataSourceStatusChangedListener(new LocationDisplay.DataSourceStatusChangedListener() {
            @Override
            public void onStatusChanged(LocationDisplay.DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {

                // If LocationDisplay started OK, then continue.
                if (dataSourceStatusChangedEvent.isStarted())
                    return;

                // No error is reported, then continue.
                if (dataSourceStatusChangedEvent.getError() == null)
                    return;


            }
        });
        //定位显示
        if(!mLocationDisplay.isStarted()){
            mLocationDisplay.startAsync();
        }


        //检查更新
        /*if(TitanApplication.IntetnetISVisible){
            UpdateUtil updateUtil=new UpdateUtil(getActivity());
            updateUtil.executeUpdate();
        }*/

    }



}
