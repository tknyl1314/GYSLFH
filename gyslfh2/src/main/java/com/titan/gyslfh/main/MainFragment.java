package com.titan.gyslfh.main;

import android.content.Intent;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatusChangedEvent;
import com.esri.arcgisruntime.loadable.LoadStatusChangedListener;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.Symbol;
import com.google.gson.Gson;
import com.titan.Injection;
import com.titan.gis.PlotUtil;
import com.titan.gis.SymbolUtil;
import com.titan.gis.callout.CalloutInterface;
import com.titan.gyslfh.alarminfo.AlarmInfoActivity;
import com.titan.gyslfh.backalarm.BackAlarmActivity;
import com.titan.gyslfh.layercontrol.LayerControlFragment;
import com.titan.gyslfh.layercontrol.LayerControlViewModel;
import com.titan.gyslfh.monitor.MonitorActivity;
import com.titan.gyslfh.monitor.MonitorModel;
import com.titan.gyslfh.sceneview.SceneActivity;
import com.titan.gyslfh.upfireinfo.UpAlarmActivity;
import com.titan.model.FireInfo;
import com.titan.model.TitanLayer;
import com.titan.navi.BaiduNavi;
import com.titan.newslfh.R;
import com.titan.newslfh.databinding.CalloutBinding;
import com.titan.newslfh.databinding.MainFragBinding;
import com.titan.util.DateUtil;
import com.titan.util.SnackbarUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by whs on 2017/4/28
 * 主界面
 */

public class MainFragment extends Fragment implements IMain, CalloutInterface {

    //图层控制
    public static final String LAYERCONTROL_TAG = "LAYERCONTROL_TAG";

    private MainViewModel mMainViewModel;

    public MainFragBinding mMainFragBinding;

    public CalloutBinding mCalloutBinding;
    //图层控制
    private LayerControlFragment mlayerControlFragment;

    LocationDisplay mLocationDisplay;
    //绘制图层
    public GraphicsOverlay mGraphicsOverlay;
    //存储point的集合
    private PointCollection mPointCollection;

    public ArcGISMap getmMap() {
        return mMap;
    }

    //基础图层
    public static ArcGISMap mMap;
    //专题图层数量
    private final  int layers=9;
    //专题图层
    private List<ServiceFeatureTable> featuretables=new ArrayList<>();

    private List<FeatureLayer> featurelayers=new ArrayList<>();

    //地图查询callout
    private Callout mCallout;
    //查询结果
    private FeatureQueryResult result;
    //标绘模块
    private PlotUtil mPlotUtil;
    //导航模块
    private BaiduNavi mBaiduNavi;
    //样式库
    private SymbolUtil mSymbolUtil;



    public List<TitanLayer> getmLayerlist() {
        return mLayerlist;
    }

    //专题图层
    private  List<TitanLayer> mLayerlist=new ArrayList<>();
    //第一次加载
    private  boolean firstLoad=true;





    public MainFragment() {
        // Requires empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMainFragBinding.mapview.resume();
        //mMainViewModel.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMainFragBinding.mapview.pause();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainFragBinding = MainFragBinding.inflate(inflater, container, false);

        //mMainFragBinding.setView(this);
        mMainFragBinding.setViewmodel(mMainViewModel);

        setHasOptionsMenu(true);


        return mMainFragBinding.getRoot();
    }

    public void setViewModel(MainViewModel viewModel) {
        mMainViewModel = viewModel;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupSnackbar();

        intiMapView();

        initPlot();

        initNavi();




    }

    /**
     * 导航初始化
     */
    private void initNavi() {
        mBaiduNavi=new BaiduNavi(getActivity(),mMainFragBinding.mapview,mMainViewModel);
        BNOuterTTSPlayerCallback ttsCallback = null;
        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            if (!mBaiduNavi.hasBasePhoneAuth(getActivity())) {

                ActivityCompat.requestPermissions(getActivity(),BaiduNavi.authBaseArr, BaiduNavi.authBaseRequestCode);
                mMainViewModel.snackbarText.set("没有获取权限");
                return;

            }
            if(!mBaiduNavi.hasCompletePhoneAuth(getActivity())){
                ActivityCompat.requestPermissions(getActivity(),BaiduNavi.authComArr, BaiduNavi.authComRequestCode);
                mMainViewModel.snackbarText.set("没有获取权限");
                return;
            }
        }
        BNOuterLogUtil.setLogSwitcher(true);
        if(mBaiduNavi.initDirs()){
            mBaiduNavi.initNavi();
        }




    }

    /**
     * 初始化标绘
     */
    private void initPlot() {
        mPlotUtil=new PlotUtil(getActivity(),mMainFragBinding.mapview);
    }

    private void setupSnackbar() {
        mMainViewModel.snackbarText.addOnPropertyChangedCallback(
                new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable observable, int i) {
                        SnackbarUtils.showSnackbar(getView(), mMainViewModel.getSnackbarText());

                    }
                });
    }




    /**
     * 初始化地图
     */
    private void intiMapView() {
        //ArcGISRuntime.setClientId("qwvvlkN4jCDmbEAO");//去除水印的
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud8065403504,none,RP5X0H4AH7CLJ9HSX018");
        //初始化底图()
        mMap = new ArcGISMap(Basemap.createOpenStreetMap());
        mMainFragBinding.mapview.setMap(mMap);
        //instantiate an ArcGISMap with OpenStreetMap Basemap
        // mMap = new ArcGISMap(Basemap.Type.OPEN_STREET_MAP, 34.056295, -117.195800, 10);
        //loadLyaers();

        /*mMainFragBinding.mapview.addLayerViewStateChangedListener(new LayerViewStateChangedListener() {
            @Override
            public void layerViewStateChanged(LayerViewStateChangedEvent layerViewStateChangedEvent) {
                // get the layer which changed it's state
                Layer layer = layerViewStateChangedEvent.getLayer();

                // get the View Status of the layer
                // View status will be either of ACTIVE, ERROR, LOADING, NOT_VISIBLE, OUT_OF_SCALE, UNKNOWN
                String viewStatus = layerViewStateChangedEvent.getLayerViewStatus().iterator().next().toString();

                final int layerIndex = mMap.getOperationalLayers().indexOf(layer);
                String name=layerViewStateChangedEvent.getLayer().getName();
                if(layerIndex>=0){
                    mLayerlist.get(layerIndex).setName(name);
                    viewStatusString(layerIndex,viewStatus);
                }


            }
        });
*/


        //添加绘制图层
        mGraphicsOverlay=addGraphicsOverlay(mMainFragBinding.mapview);
        mMap.addLoadStatusChangedListener(new LoadStatusChangedListener() {
            @Override
            public void loadStatusChanged(LoadStatusChangedEvent loadStatusChangedEvent) {
                String mapLoadStatus;
                mapLoadStatus = loadStatusChangedEvent.getNewLoadStatus().name();
                // map load status can be any of LOADING, FAILED_TO_LOAD, NOT_LOADED or LOADED

                // set the status in the TextView accordingly
                switch (mapLoadStatus) {
                    case "LOADING":
                        Log.e("Titan","LOADING");
                        break;

                    case "FAILED_TO_LOAD":

                        mMainViewModel.snackbarText.set("图层加载异常");
                        Log.e("Titan","图层加载异常");
                        break;

                    case "NOT_LOADED":
                        Log.e("Titan","NOT_LOADED");

                        break;

                    case "LOADED":
                        //loadLyaers();
                        firstLoad=false;
                        setTouchListener();
                        Log.e("Titan","图层加载完成");
                        Log.e("TItan","WKID"+mMainFragBinding.mapview.getSpatialReference().getWKText());
                        break;

                    default :
                        break;
                }

            }
        });

        // create feature layer with its service feature table
        // create the service feature table
        //ServiceFeatureTable serviceFeatureTable = new ServiceFeatureTable(getResources().getString(R.string.gisserverhost));

        // create the feature layer using the service feature table


        // get the callout that shows attributes
        mCallout = mMainFragBinding.mapview.getCallout();
        mCallout.setStyle(new Callout.Style(getActivity(),R.xml.calloutstyle));

        mPointCollection = new PointCollection(mMainFragBinding.mapview.getSpatialReference());

        //去除版权声明
        mMainFragBinding.mapview.setAttributionTextVisible(false);




        mLocationDisplay = mMainFragBinding.mapview.getLocationDisplay();

        //当位置符号移动到“漂移范围”之外时，通过重新定位位置符号来保持屏幕上的位置符号。
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
        //适合徒步
        //mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);

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

    /**
     * 加载专题图层
     */
    private void loadLyaers(){
        for (int i = 1; i <=layers ; i++) {
            String layerurl=getActivity().getString(R.string.gisserverhost1)+"/"+i;
            ServiceFeatureTable serviceFeatureTable = new ServiceFeatureTable(layerurl);
            //String name=serviceFeatureTable.getLayerInfo().getServiceLayerName();
            //serviceFeatureTable.setFeatureRequestMode(ServiceFeatureTable.FeatureRequestMode.MANUAL_CACHE);
            FeatureLayer featureLayer = new FeatureLayer(serviceFeatureTable);
            featuretables.add(serviceFeatureTable);
            featurelayers.add(featureLayer);
            featureLayer.setVisible(false);
            TitanLayer titanLayer=new TitanLayer();
            String name=featureLayer.getName();
            titanLayer.setName(name);
            titanLayer.setVisiable(false);
            titanLayer.setUrl(layerurl);
            mLayerlist.add(titanLayer);

            mMap.getOperationalLayers().add(featureLayer);

            /*if(isadd){
                name=featureLayer.getName();
                Log.e("图层加载成功",featureLayer.getName()) ;
            }*/

        }

    }
    /**
     * The method looks up the view status of the layer and returns a string which is displayed
     *
     * @param status View Status of the layer
     * @return String equivalent of the status
     */
    private String viewStatusString (int layerindex,String status) {

        switch(status) {
            case "ACTIVE":
                mLayerlist.get(layerindex).setVisiable(true);
                mMainViewModel.snackbarText.set(mLayerlist.get(layerindex).getName()+"已加载");
                return getActivity().getString(R.string.active);

            case "ERROR":
                mMainViewModel.snackbarText.set(mLayerlist.get(layerindex).getName()+getActivity().getString(R.string.error));
                return getActivity().getString(R.string.error);

            case "LOADING":
                mMainViewModel.snackbarText.set(mLayerlist.get(layerindex).getName()+getActivity().getString(R.string.loading));
                return getActivity().getString(R.string.loading);

            case "NOT_VISIBLE":
                mLayerlist.get(layerindex).setVisiable(false);
                mMainViewModel.snackbarText.set(mLayerlist.get(layerindex).getName()+"已移除");
                return getActivity().getString(R.string.notVisible);

            case "OUT_OF_SCALE":
                mMainViewModel.snackbarText.set(mLayerlist.get(layerindex).getName()+getActivity().getString(R.string.outOfScale));
                return getActivity().getString(R.string.outOfScale);

        }

        return getActivity().getString(R.string.unknown);

    }

    /**
     * 设置地图点击监听事件
     */
    private  void  setTouchListener(){
        mMainFragBinding.mapview.setOnTouchListener(new DefaultMapViewOnTouchListener(getActivity(),mMainFragBinding.mapview){
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {


                if(mCallout.isShowing()){
                    mCallout.dismiss();
                }
                // get the point that was clicked and convert it to a point in map coordinates
                final Point clickPoint = mMainFragBinding.mapview.screenToLocation(new android.graphics.Point(Math.round(e.getX()), Math.round(e.getY())));
                int tolerance = 100;
                double mapTolerance = tolerance * mMainFragBinding.mapview.getUnitsPerDensityIndependentPixel();
                // create objects required to do a selection with a query
                Envelope envelope = new Envelope(clickPoint.getX() - mapTolerance, clickPoint.getY() - mapTolerance, clickPoint.getX() + mapTolerance, clickPoint.getY() + mapTolerance, mMap.getSpatialReference());
                QueryParameters query = new QueryParameters();
                query.setGeometry(envelope);
                query.setReturnGeometry(true);

                List<ServiceFeatureTable> tt=new ArrayList<>();
                for (int i = 0; i <mMap.getOperationalLayers().size() ; i++) {
                    if(mMap.getOperationalLayers().get(i).isVisible()){
                        tt.add(featuretables.get(i));
                    }
                }

                for (ServiceFeatureTable featureTable:tt){
                    //featureTable.get
                    //FeatureLayer featureLayer = new FeatureLayer(featureTable);

                        // call select features
                        final ListenableFuture<FeatureQueryResult> future =featureTable.queryFeaturesAsync(query, ServiceFeatureTable.QueryFeatureFields.LOAD_ALL);
                      /* if(future.isDone()){
                            //第二次查询
                            break;
                        }*/

                        // add done loading listener to fire when the selection returns
                        future.addDoneListener(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //call get on the future to get the result
                                    result = future.get();
                                    //result.getFields().get(1).getAlias()

                                    // create an Iterator
                                    Iterator<Feature> iterator = result.iterator();

                                    Feature feature = null;
                                    // cycle through selections
                                    int counter = 0;
                                    while (iterator.hasNext()){
                                        feature = iterator.next();


                                        // create a Map of all available attributes as name value pairs
                                        //Map<String, Object> attr = feature.getAttributes();


                                        Log.d(getResources().getString(R.string.app_name), "Selection #: " + counter + " Table name: " + feature.getFeatureTable().getTableName());
                                        counter++;

                                        Envelope envelope = feature.getGeometry().getExtent();
                                        mMapView.setViewpointGeometryAsync(envelope, 200);
                                        mCallout.setLocation(clickPoint);
                                        //mCallout.setContent(createCallView(feature.getAttributes()));
                                        mCallout.setContent(createCallView(result.getFields(),feature.getAttributes()));
                                        mCallout.show();
                                    }

                                    // center the mapview on selected feature
                                    //Envelope envelope = feature.getGeometry().getExtent();
                                    //String tablename=feature.getFeatureTable().getTableName();

                                    //mMapView.setViewpointGeometryAsync(envelope, 200);


                                } catch (Exception e) {
                                    mMainViewModel.snackbarText.set("查询异常"+e.toString());
                                    Log.e(getResources().getString(R.string.app_name), "Select feature failed: " + e.getMessage());
                                }
                            }
                        });
                    }
                return super.onSingleTapConfirmed(e);
            }




        });
    }

    /**
     * 选择底图
     * @param position
     */
    public void selectBasemap(int position){
        switch (position){
            //基础图
            case 0:
                mMap.setBasemap(Basemap.createStreets());
                break;
            //影像图
            case 1:
                mMap.setBasemap(Basemap.createImagery());
                break;
            //森林火险等级图
            case 2:
                mMap.setBasemap(Basemap.createStreets());
                break;
            //三维场景
            /*case 3:
               *//* mMainViewModel.isSceneView.set(true);
                int dd=mMainFragBinding.mapview.getVisibility();
                mMainFragBinding.mapview.setVisibility(View.INVISIBLE);
                init3D();
                loadLayers();*//*
                //mMap.setBasemap(Basemap.createStreets());
                break;*/
        }
    }





    /**
     * 创建callout
     * @return
     */
    public View createCallView(List<Field> fields,Map<String, Object> attr){

        final MonitorModel monitorModel=new MonitorModel("","","","","","");

        View view =LayoutInflater.from(getActivity()).inflate(R.layout.callout,null);
        TextView tv_attr= (TextView) view.findViewById(R.id.tv_content);
        Button btn_monitor= (Button) view.findViewById(R.id.btn_monitor);
        btn_monitor.setVisibility(View.GONE);
        ImageView iv_close= (ImageView) view.findViewById(R.id.iv_close);
        StringBuffer calloutcontent=new StringBuffer();
        calloutcontent.append("");
        try {


            final Set<String> keys = attr.keySet();
            if(keys.contains("MONITORIP")){
                btn_monitor.setVisibility(View.VISIBLE);
                //alloutViewModel.ismonitor.set(true);

            }

            for (Field field :fields){
                String alias=field.getAlias();
                Object value=  attr.get(field.getName());

                // format observed field value as date
                if (value instanceof GregorianCalendar) {
                    //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                    //DateUtil.getYMD(((GregorianCalendar) value).getTime());
                    value = DateUtil.getYMD(((GregorianCalendar) value).getTime());
                }
                if(field.getName().equals("MONITORIP")&& value!=null){
                    monitorModel.setMONITORIP((String) value);

                }

                if(field.getName().equals("NTITHEFTIP")&& value!=null){
                    monitorModel.setANTITHEFTIP((String) value);

                }
                if(field.getName().equals("INFRAREDIP")&& value!=null){
                    monitorModel.setINFRAREDIP((String) value);

                }
                if(field.getName().equals("MONITORDVR")&& value!=null){

                    monitorModel.setMONITORDVR((String) value );
                }
                if(field.getName().equals("INFRAREDDVR")&& value!=null){
                    monitorModel.setINFRAREDDVR((String) value);
                }
                if(field.getName().equals("ANTITHEFTDVR")&& value!=null){
                    monitorModel.setANTITHEFTDVR((String) value);
                }
                calloutcontent.append(alias + " | " + value + "\n");

            }


        } catch (Exception e) {
            Toast.makeText(getActivity(),"获取属性信息异常"+e,Toast.LENGTH_LONG).show();

        }
        //如果是监控点显示视频监控

        tv_attr.setText(calloutcontent);
        btn_monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calloutClose();
                Intent intent=new Intent();
                String json=new Gson().toJson(monitorModel);
                intent.putExtra("data",json);
                intent.setClass(getActivity(), MonitorActivity.class);
                startActivity(intent);
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallout.isShowing()){
                    mCallout.dismiss();
                }
            }
        });
        return view;

    }




    /**
     * 添加绘制层
     * @param mapView
     * @return
     */
    private GraphicsOverlay addGraphicsOverlay(MapView mapView) {
        //create the graphics overlay
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        //add the overlay to the map view
        mapView.getGraphicsOverlays().add(graphicsOverlay);
        return graphicsOverlay;
    }

    /**
     * 添加轨迹图形
     * @param geometry
     * @param symbol
     */
    public void addTrackLineGraphic(Geometry geometry, Symbol symbol) {
        Graphic polineGraphic = new Graphic(geometry, symbol);
        mGraphicsOverlay.getGraphics().clear();
        mGraphicsOverlay.getGraphics().add(polineGraphic);
    }


    @Override
    public void calloutClose() {

        if(mCallout.isShowing()){
            mCallout.dismiss();
        }
    }

    @Override
    public void showMonitor() {
        calloutClose();
        startActivity(new Intent(getActivity(), MonitorActivity.class));

    }




    /**
     * 1:火警上报
     * 2:接警录入
     */
    @Override
    public void onAlarm(int type) {
        Intent intent =new Intent();
        intent.putExtra("type",type);
        mMainViewModel.getTitanloc();
        Bundle bundle=new Bundle();
        bundle.putSerializable("loc",mMainViewModel.getTitanloc());
        intent.putExtras(bundle);
        intent.setClass(getActivity(),UpAlarmActivity.class);
        startActivity(intent);

    }

    /**
     * 接警管理
     */
    @Override
    public void onAlarmInfo() {
        startActivity(new Intent(getActivity(),AlarmInfoActivity.class));

    }

    /**
     * 回警
     */
    @Override
    public void onBackAlarm() {
        startActivity(new Intent(getActivity(),BackAlarmActivity.class));

    }

    @Override
    public void onLocation(Point currentPoint) {
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
        mLocationDisplay.startAsync();
    }

    @Override
    public void showToast(String info, int type) {
        Toast.makeText(getActivity(), info, type).show();

    }

    /**
     * 展示实时轨迹
     * @param pointList
     */
    @Override
    public void showTrackLine(List<Point> pointList) {

        /*if(pointList.size()==1){
            try{

                PointCollection pointCollection=new PointCollection((Iterable<Point>) pointList.iterator());
                lineBuilder = new PolylineBuilder(pointCollection);

            }catch(Exception e) {
                Log.e("TITAN",e.toString());
                ToastUtil.setToast(getActivity(),"轨迹异常："+e);
            }
        }else {
            lineBuilder.addPoint(pointList.get(pointList.size()-1));
        }
        if(lineBuilder!=null){
            addTrackLineGraphic(lineBuilder.toGeometry(),lineSymbol);
        }*/
    }

    /**
     * 标绘
     * @param isplot
     */
    @Override
    public void Plot(boolean isplot) {
         if (isplot) {
             mMainFragBinding.mapview.setOnTouchListener(mPlotUtil.plotTouchListener);
             mPlotUtil.showPlotDialog(mMainFragBinding.ivPlot);
            //mapView.setOnTouchListener(myTouchListener);
        } else {
             setTouchListener();
             mPlotUtil.closePlotDialog();
           // plotUtil.showPlotDialog(v);
        }
    }

    /**
     * 开启导航
     * @param isnav
     */
    @Override
    public void startNavigation(boolean isnav) {
       if(mMainViewModel.isnav.get()){
           if(mBaiduNavi.isHasInitSuccess()){
               Point spoint=mMainViewModel.currentPoint.get();
               mBaiduNavi.setsNode(new BNRoutePlanNode(spoint.getX(),spoint.getY(),"起点",null, BNRoutePlanNode.CoordinateType.WGS84));
               mMainFragBinding.mapview.setOnTouchListener(mBaiduNavi.mapViewOnTouchListener);
               mMainViewModel.snackbarText.set("请在地图上选择终点");
           }else {
               mMainViewModel.snackbarText.set("导航功能没有初始化");
               mMainViewModel.isnav.set(false);
           }
       }else {
           setTouchListener();
       }

    }


    @Override
    public void test() {



        /*FragmentManager manager =getFragmentManager();

        SceneFragment sceneFragment =  SceneFragment.newInstance(mMainViewModel.getTitanloc(),mLayerlist);
        SceneViewModel viewModel= new SceneViewModel(getActivity(),sceneFragment);
        sceneFragment.setViewModel(viewModel);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_frame, sceneFragment);
        transaction.addToBackStack(null);
        //设置过度动画
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();*/

        //SceneFragment sceneFragment = getActivity().findOrCreateSceneViewFragment();
        //startActivity(new Intent(getActivity(), MonitorActivity.class));
    }

    @Override
    public void openScene() {
        Intent intent=new Intent(getActivity(), SceneActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("loc",mMainViewModel.getTitanloc());
        bundle.putSerializable("layers", (Serializable) LayerControlFragment.getmLayerList());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void showLayerControl() {
        if(mlayerControlFragment==null){
            mlayerControlFragment= LayerControlFragment.getInstance(mMap,mMainFragBinding.mapview,mGraphicsOverlay);
            LayerControlViewModel viewModel=new LayerControlViewModel(getActivity(),mlayerControlFragment, Injection.provideDataRepository(getActivity()),mLayerlist);
            mlayerControlFragment.setViewModel(viewModel);
        }
        mlayerControlFragment.show(getFragmentManager(),LAYERCONTROL_TAG);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        System.out.print(true);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * 展示推送火情信息
     */
    public void showFirePt(FireInfo fireinfo) {
        Point pt=new Point(fireinfo.getLON(),fireinfo.getLAT());
        mSymbolUtil=new SymbolUtil(getActivity());
        Graphic graphic=new Graphic(pt,SymbolUtil.firepoint);
        mGraphicsOverlay.getGraphics().add(graphic);
        mMainFragBinding.mapview.setViewpointCenterAsync(pt);
        //mMapView.setViewpointGeometryAsync(envelope, 200);

    }


}
