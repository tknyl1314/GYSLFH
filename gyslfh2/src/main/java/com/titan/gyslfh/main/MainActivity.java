package com.titan.gyslfh.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.LineSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.titan.ViewModelHolder;
import com.titan.data.source.local.LocalDataSource;
import com.titan.gis.TrackUtil;
import com.titan.gyslfh.TitanApplication;
import com.titan.gyslfh.UpAlarmActivity;
import com.titan.gyslfh.alarminfo.AlarmInfoActivity;
import com.titan.gyslfh.login.LoginActivity;
import com.titan.loction.baiduloc.LocationService;
import com.titan.newslfh.R;
import com.titan.util.ActivityUtils;
import com.titan.util.DateUtil;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity implements IMain {

    public static final String MAIN_VIEWMODEL_TAG = "MAIN_VIEWMODEL_TAG";

    //
    private LocationService locationService;

    private DrawerLayout mDrawerLayout;

    private MainViewModel mViewModel;
    //轨迹开关
    Switch sw_istrack;

    //private MapView mMapView;
    Button btn_loc,btn_track,btn_relogin;
    public  static  TextView tv_msg;
    Context mContext;
    LocationDisplay mLocationDisplay;
    /**定位需要动态获取的权限*/
    String[] reqPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION};
    /**个推需要动态获取的权限*/
    String[] GTreqPermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
    private int requestCode = 2;

    public static   Point getCurrentPoint() {
        return currentPoint;
    }

    /**当前位置*/
    public static Point currentPoint=null;
    /**上次定位的位置*/
    Point lastPoint=null;
    /**上传坐标*/
    MyAsyncTask upTask=null;
    /**轨迹线*/
    Polyline trackPolyLine=null;
    /**轨迹线集合*/
    PointCollection ptc=null;
    Graphic trackGrafic;
    AsyncTask trackTask=null;
    GraphicsOverlay graphicsOverlay=null;
    /**轨迹线样式*/
    LineSymbol trackSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.DASH,Color.RED, 5);
    public  static final  String upPtError="上传坐标异常";
    public  static final  String upPtSuccess="上传坐标成功";
    public  static final  String upException="坐标上传异常";
    public  static final  String track="track";
    boolean istrack=true;
    /**经纬度格式化*/
    DecimalFormat locformat=new DecimalFormat(".000000");
    /**地图坐标系*/
    SpatialReference spatialReference=null;
    /**百度平滑*/
    private LinkedList<TrackUtil.LocationEntity> locationList = new LinkedList<TrackUtil.LocationEntity>();
    public static float[] EARTH_WEIGHT = {0.1f,0.2f,0.4f,0.6f,0.8f}; // 推算计算权重_地球// 存放历史定位结果的链表，最大存放当前结果的前5次定位结果
    BDLocation bdLocation=null;
    double distance;

    SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TitanApplication.getInstance().addActivity(this);
        mContext=this;

        //setupNavigationDrawer();

        MainFragment mainFragment = findOrCreateViewFragment();

        mViewModel = findOrCreateViewModel();

        // Link View and ViewModel
        mainFragment.setViewModel(mViewModel);



        initView();
        intiPermisson();


    }

    private void initView() {
        mSharedPreferences=mContext.getSharedPreferences(TitanApplication.PREFS_NAME,0);
        sw_istrack = (Switch) findViewById(R.id.sw_istrack);
        sw_istrack.setChecked(mSharedPreferences.getBoolean("istrack",false));
        sw_istrack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(mContext, "轨迹跟踪开启", Toast.LENGTH_SHORT).show();
                }else {
                    graphicsOverlay.getGraphics().remove(trackPolyLine);
                    Toast.makeText(mContext, "轨迹跟踪关闭", Toast.LENGTH_SHORT).show();
                }
                mSharedPreferences.edit().putBoolean("istrack",isChecked).apply();
            }
        });
        //
    }

    /**
     * 初始化
     */
    /*private void setupNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }*/

    /**
     * 设置点击事件
     * @param navigationView
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            //切换帐号
                            case R.id.list_navigation_menu_resign:
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                // Do nothing, we're already on that screen
                                break;
                            case R.id.list_nav_menu_setting:
                                Intent intent1 = new Intent(mContext, LoginActivity.class);
                                startActivity(intent1);
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @NonNull
    private MainFragment findOrCreateViewFragment() {
        MainFragment tasksFragment =
                (MainFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = MainFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), tasksFragment, R.id.content_frame);
        }
        return tasksFragment;
    }

    @NonNull
    private MainViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<MainViewModel> retainedViewModel =
                (ViewModelHolder<MainViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(MAIN_VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
             // There is no ViewModel yet, create it.
            MainViewModel viewModel = new MainViewModel(getApplicationContext(), this);
            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    MAIN_VIEWMODEL_TAG);
            return viewModel;
        }
    }

    /**
     * 初始化权限
     */
    private void intiPermisson() {
        // If an error is found, handle the failure to start.
        // Check permissions to see if failure may be due to lack of permissions.
        boolean permissionCheck1 = ContextCompat.checkSelfPermission(MainActivity.this, reqPermissions[0]) ==
                PackageManager.PERMISSION_GRANTED;
        boolean permissionCheck2 = ContextCompat.checkSelfPermission(MainActivity.this, reqPermissions[1]) ==
                PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23&&!(permissionCheck1 && permissionCheck2)) {
            // If permissions are not already granted, request permission from the user.
            ActivityCompat.requestPermissions(MainActivity.this, reqPermissions, requestCode);
        } else {
            // Report other unknown failure types to the user - for example, location services may not
            // be enabled on the device.
                    /*String message = String.format("Error in DataSourceStatusChangedListener: %s", dataSourceStatusChangedEvent
                            .getSource().getLocationDataSource().getError().getMessage());*/
            String message="获取定位信息异常，请检查GPS是否开启";
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            // Update UI to reflect that the location display did not actually start
            //mSpinner.setSelection(0, true);
        }


    }



    /***
     * 接收定位结果消息，并显示在地图上
     */
    /*private Handler locHander = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            try {
                BDLocation location = msg.getData().getParcelable("loc");
                int iscal = msg.getData().getInt("iscalculate");
                if (location != null) {
                    //LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
                    if (iscal == 0) {
                       // bitmap = BitmapDescriptorFactory.fromResource(R.drawable.huaji); // 非推算结果
                    } else {
                        bdLocation=location;
                        new MyAsyncTask().execute("uplocation");
                       // bitmap = BitmapDescriptorFactory.fr
                        // omResource(R.drawable.icon_openmap_focuse_mark); // 推算结果
                    }

                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    };*/

    private GraphicsOverlay addGraphicsOverlay(MapView mapView) {
        //create the graphics overlay
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        //add the overlay to the map view
        mapView.getGraphicsOverlays().add(graphicsOverlay);
        return graphicsOverlay;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Location permission was granted. This would have been triggered in response to failing to start the
            // LocationDisplay, so try starting this again.
            //mLocationDisplay.startAsync();
        } else {
            // If permission was denied, show toast to inform user what was chosen. If LocationDisplay is started again,
            // request permission UX will be shown again, option should be shown to allow never showing the UX again.
            // Alternative would be to disable functionality so request is not shown again.
            Toast.makeText(MainActivity.this, getResources().getString(R.string.location_permission_denied), Toast.LENGTH_SHORT).show();
            // Update UI to reflect that the location display did not actually start
            // mSpinner.setSelection(0, true);
        }
    }
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // -----------location config ------------
        //定位初始化
         locationService = ((TitanApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        /*int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }*/
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();
        // 定位SDK




    }
    /**
     * Stop location service
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(locationService!=null){
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
        }
        super.onDestroy();
    }

    /**
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
               /* Message locMsg = locHander.obtainMessage();
                Bundle locData;
                locData = Algorithm(location);//定位平滑
                if (locData != null) {
                    locData.putParcelable("loc", location);
                    locMsg.setData(locData);
                    locHander.sendMessage(locMsg);
                }*/
                bdLocation=location;

                new MyAsyncTask().execute("uplocation");
                /*if(lastPoint!=null){
                  SpatialReference sp=  currentPoint.getSpatialReference();
                  Unit u= sp.getUnit();
                     distance= GeometryEngine.distanceBetween(currentPoint,lastPoint);
                }*/
                currentPoint=new Point(bdLocation.getLongitude(),bdLocation.getLatitude());
                lastPoint=currentPoint;
               /* bdLocation =location;
                new MyAsyncTask().execute("uplocation");*/
              /*  if (lastPoint != null) {
                    Message locMsg = locHander.obtainMessage();
                    Bundle locData;
                    locData = Algorithm(location);//定位平滑
                    if (locData != null) {
                        locData.putParcelable("loc", location);
                        locMsg.setData(locData);
                        locHander.sendMessage(locMsg);
                    }*/
                   /* double distance = GeometryEngine.distanceBetween(currentPoint, lastPoint);
                    double distance1 = GeometryEngine.distanceGeodetic(currentPoint, lastPoint, new LinearUnit(LinearUnitId.METERS), new AngularUnit(AngularUnitId.SECONDS), GeodeticCurveType.GEODESIC).getDistance();*/
                   // new MyAsyncTask().execute("uplocation");
                    /*if (distance > 10) {
                        if (upTask != null) {
                            upTask.execute("uplocation");
                        } else {
                            new MyAsyncTask().equals("uplocation");
                        }
                    }*/

                /*if (istrack) {
                    new trackAsyncTask().execute();
                }*/
                //lastPoint = currentPoint;
              /*  if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果

                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****

                    }

                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                } else if (location.getLocType() == BDLocation.TypeServerError) {

                   *//* sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");*//*
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    Toast.makeText(mContext,"服务端网络定位失败：请向技术人员反馈",Toast.LENGTH_SHORT).show();
                    *//*sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");*//*
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    Toast.makeText(mContext,"无法获取有效定位依据导致定位失败：请向技术人员反馈",Toast.LENGTH_SHORT).show();
                  *//*  sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");*//*
                }*/
            }else {
                Toast.makeText(mContext, "定位失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /***
     * 平滑策略代码实现方法，主要通过对新定位和历史定位结果进行速度评分，
     * 来判断新定位结果的抖动幅度，如果超过经验值，则判定为过大抖动，进行平滑处理,若速度过快，
     * 则推测有可能是由于运动速度本身造成的，则不进行低速平滑处理 ╭(●｀∀´●)╯
     *
     * @return Bundle
     */
    private Bundle Algorithm(BDLocation location) {

        Bundle locData = new Bundle();
        double curSpeed = 0;
        if (locationList.isEmpty() || locationList.size() < 2) {
            TrackUtil.LocationEntity temp = new TrackUtil.LocationEntity();
            temp.location = location;
            temp.time = System.currentTimeMillis();
            locData.putInt("iscalculate", 0);
            locationList.add(temp);
        } else {
            if (locationList.size() > 5)
                locationList.removeFirst();
            double score = 0;
            for (int i = 0; i < locationList.size(); ++i) {
                LatLng lastPoint = new LatLng(locationList.get(i).location.getLatitude(),
                        locationList.get(i).location.getLongitude());
                LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
                double distance = DistanceUtil.getDistance(lastPoint, curPoint);
                curSpeed = distance / (System.currentTimeMillis() - locationList.get(i).time) / 1000;
                score += curSpeed * EARTH_WEIGHT[i];
            }
            if (score > 0.00000999 && score < 0.00005) { // 经验值,开发者可根据业务自行调整，也可以不使用这种算法
                location.setLongitude(
                        (locationList.get(locationList.size() - 1).location.getLongitude() + location.getLongitude())
                                / 2);
                location.setLatitude(
                        (locationList.get(locationList.size() - 1).location.getLatitude() + location.getLatitude())
                                / 2);
                locData.putInt("iscalculate", 1);
            } else {
                locData.putInt("iscalculate", 0);
            }
            TrackUtil.LocationEntity newLocation = new TrackUtil.LocationEntity();
            newLocation.location = location;
            newLocation.time = System.currentTimeMillis();
            locationList.add(newLocation);

        }
        return locData;
    }



    /**
     * 更改登录
     * @param view
     */
    public void onRelogin(View view) {
        if(locationService!=null){
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
        }
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



    @Override
    public void onNext() {

    }

    /**
     * 一键报警
     */
    @Override
    public void onAlarm() {
        Intent intent =new Intent(mContext,UpAlarmActivity.class);
        mContext.startActivity(intent);
    }

    /**
     * 接警信息
     */
    @Override
    public void onAlarmInfo() {
        Intent intent =new Intent(mContext,AlarmInfoActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void showToast(String info, int type) {
        Toast.makeText(mContext, info, type).show();
    }

    /**
     * 绘制轨迹
     */
    private  class  trackAsyncTask extends  AsyncTask<Void,Void,Void>{

        BDLocation bdLocation;
        public trackAsyncTask(BDLocation bdLocation) {
            this.bdLocation=bdLocation;
        }

        @Override
        protected Void doInBackground(Void... voids) {
          /*  if (trackPolyLine == null) {
                try {
                    PointCollection ptc = new PointCollection(spatialReference);
                    if(bdLocation!=null){
                       Point pt=new Point(bdLocation.getLongitude(),bdLocation.getLatitude());
                        ptc.add(pt);
                        trackPolyLine = new Polyline(ptc);
                        trackGrafic = new Graphic(trackPolyLine, trackSymbol);
                    }

                }catch (Exception e){
                    return null;
                }


            } else {
                ptc.add(pt);
            }*/
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            graphicsOverlay.getGraphics().add(trackGrafic);

        }
    }
    /**
     * 异步上传轨迹
     */
    private class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(final String... params) {
            if (params[0].equals("uplocation")) {
                if(currentPoint!=null){
                   // Point newpt=new Point(bdLocation.getLongitude(),bdLocation.getLatitude());
                    return uplaodTrackPoint(currentPoint);
                }
                else {
                    return "获取当前轨迹点失败";
                }
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals(upPtError)){
                Toast.makeText(mContext,"坐标上传异常",Toast.LENGTH_SHORT).show();
            }else if(result.equals(upPtSuccess)){
                Toast.makeText(mContext,upPtSuccess,Toast.LENGTH_SHORT).show();
            }
            else if(result.equals(track)){
                if (trackPolyLine == null) {

                    PointCollection ptc = new PointCollection(SpatialReferences.getWgs84());
                    trackPolyLine = new Polyline(ptc);
                    ptc.add(currentPoint);
                    trackGrafic = new Graphic(trackPolyLine, trackSymbol);
                    graphicsOverlay.getGraphics().add(trackGrafic);
                } else {
                    ptc.add(currentPoint);
                }
            }else{
                Toast.makeText(mContext,result,Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     *   上传轨迹点
     */
    private String uplaodTrackPoint(Point pt) {
        //String  uptime = dateFormat.format(new Date());
        String  uptime= DateUtil.dateFormat(new Date());
        //2363 :Xian_1980_3_Degree_GK_Zone_39
        ///Point uploadPoint= (Point) GeometryEngine.project(pt, SpatialReference.create(2363));
        //Point uploadPoint= (Point) GeometryEngine.project(pt, mMapView.getSpatialReference());
        if(pt!=null){
            String lon = locformat.format(pt.getX());
            String lat = locformat.format(pt.getY());
            if (TitanApplication.IntetnetISVisible)
            {
                try {
                    // 上传轨迹到服务器
                    //boolean isuploc=webService.UPLonLat(MyApplication.SBH, lon,lat, uptime);
                    // 上传轨迹到本地数据库
                    boolean isup = LocalDataSource.getInstance(mContext).saveTrackPoint();
                            //.UploadLocalDatebase(TitanApplication.SBH, lon, lat, uptime,"1");
                    if(isup){
                        return upPtSuccess;
                    }else {
                        return upPtError;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return  upPtError;
                }
            } else
            {
                try {
                    //boolean isadd=DataBaseHelper.UploadLocalDatebase(MyApplication.SBH, lon, lat, uptime,"0");
                    if(true){
                        return upPtSuccess;
                    }else {
                        return upPtError;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return upPtError;
                }

            }
        }else {
            return "获取当前轨迹点失败";
        }


    }


    /**
     * 监听回退按钮
     */
    long firstTime = 0;
    @Override
    public void onBackPressed() {
        // exitApp();
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 800) { // 如果两次按键时间间隔大于2秒，则不退出

            Toast.makeText(mContext,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            firstTime = secondTime;// 更新firstTime

        } else {
            TitanApplication.getInstance().finshAllActivities();
           // System.exit(0);// 否则退出程序
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        //mMapView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mMapView.resume();
    }
}
