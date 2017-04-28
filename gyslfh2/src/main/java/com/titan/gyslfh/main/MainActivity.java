package com.titan.gyslfh.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.LineSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.titan.forestranger.MyApplication;
import com.titan.forestranger.UpAlarmActivity;
import com.titan.gis.TrackUtil;
import com.titan.loction.baiduloc.LocationService;
import com.titan.newslfh.R;
import com.titan.util.DateUtil;
import com.titan.util.UpdateUtil;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity {
    //
    private LocationService locationService;

    private MapView mMapView;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //MyApplication.getInstance().addActivity(this);
        mContext=this;
        //MyApplication.mainActivity=this;
        mMapView= (MapView) findViewById(R.id.mapview);
        ArcGISMap mMap = new ArcGISMap(Basemap.createImagery());
        mMapView.setMap(mMap);
        /*ArcGISTiledLayer imglayer=new ArcGISTiledLayer(getResources().getString(R.string.ahtiditu));
        Basemap basemap=new Basemap(imglayer);
        ArcGISMap mMap = new ArcGISMap(basemap);
        mMapView.setMap(mMap);*/
        //mMapView.getop.addView(imglayer);
        spatialReference=mMap.getSpatialReference();
        // add graphics overlay to MapView.
        graphicsOverlay = addGraphicsOverlay(mMapView);
        // get the MapView's LocationDisplay
        mLocationDisplay = mMapView.getLocationDisplay();
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
        //适合徒步
        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
        intiView();
        intiPermisson();

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
        if(!mLocationDisplay.isStarted()){
            mLocationDisplay.startAsync();
        }
        upTask=new MyAsyncTask();
        if(MyApplication.IntetnetISVisible){
            UpdateUtil updateUtil=new UpdateUtil(mContext);
            updateUtil.executeUpdate();
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

    /**
     *  初始化view
     */
    private void intiView() {
        //定位
        btn_loc= (Button) findViewById(R.id.btn_loc);
        //推送消息
         tv_msg= (TextView) findViewById(R.id.tv_msg);
        //轨迹跟踪
        //btn_track= (Button) findViewById(R.id.btn_track);
        //更改登录
        btn_track= (Button) findViewById(R.id.btn_relogin);
    }

    /***
     * 接收定位结果消息，并显示在地图上
     */
    private Handler locHander = new Handler() {

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

    };

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
            mLocationDisplay.startAsync();
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
         locationService = ((MyApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }

        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);

        if (!mLocationDisplay.isStarted())
            mLocationDisplay.startAsync();

        locationService.start();// 定位SDK
        currentPoint=mLocationDisplay.getLocation().getPosition();
        try{
            if(currentPoint!=null&&(currentPoint.getX()>0&&currentPoint.getY()>0)){
                String lon=locformat.format(currentPoint.getX());
                String lat=locformat.format(currentPoint.getY());
                Toast.makeText(mContext,"经度："+lon+"\n纬度："
                        +lat,Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(mContext,"获取当前位置异常 请设置定位模式为:准确度高",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(mContext,"获取当前位置异常",Toast.LENGTH_SHORT).show();
        }




    }
    /**
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
      /*  locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务*/
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
     * 轨迹跟踪
     * @param view
     */
    public void onTrack(View view) {
        if (btn_track.getText().toString().equals(getString(R.string.track))){
            btn_track.setText(getString(R.string.trackoff));
            //new trackAsyncTask().execute();
            istrack=true;

        }else{
            istrack=false;
           // graphicsOverlay.
            graphicsOverlay.getGraphics().remove(trackPolyLine);
            btn_track.setText(getString(R.string.track));
        }
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
        finish();
    }

    /**
     * 一键报警
     * @param view
     */
    public void onAlarm(View view) {
         Intent intent =new Intent(mContext,UpAlarmActivity.class);
          startActivity(intent);
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
            if (MyApplication.IntetnetISVisible)
            {
                try {
                    // 上传轨迹到服务器
                    //boolean isuploc=webService.UPLonLat(MyApplication.SBH, lon,lat, uptime);
                    // 上传轨迹到本地数据库
                   /* boolean isup = DataBaseHelper.UploadLocalDatebase(MyApplication.SBH,
                            lon, lat, uptime,"1");*/
                    if(true){
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
        if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出

            Toast.makeText(mContext,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            firstTime = secondTime;// 更新firstTime

        } else {
            MyApplication.getInstance().finshAllActivities();
           // System.exit(0);// 否则退出程序
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.resume();
    }
}
