package com.otitan.gyslfh.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;
import com.esri.android.map.Callout;
import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.GraphicsLayer.RenderingMode;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISLayerInfo;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.tasks.identify.IdentifyParameters;
import com.google.gson.Gson;
import com.otitan.DataBaseHelper;
import com.otitan.adapter.ExpandableAdapter;
import com.otitan.adapter.HuoDianAdapter;
import com.otitan.adapter.XbAdapter;
import com.otitan.api.QueryWeatherTask;
import com.otitan.color.ColorPickerView;
import com.otitan.color.SansumColorSelecter;
import com.otitan.customui.MorePopWindow;
import com.otitan.entity.DateDialog;
import com.otitan.entity.Firepoint;
import com.otitan.entity.TrackPoint;
import com.otitan.gis.Calculate;
import com.otitan.gis.Convert;
import com.otitan.gis.GraphicLayerutil;
import com.otitan.gis.MyIdentifyTask;
import com.otitan.gis.PlotUtil;
import com.otitan.gis.PositionUtil;
import com.otitan.gis.TrackUtil;
import com.otitan.gyslfh.R;
import com.otitan.util.GpsCorrect;
import com.otitan.util.NetUtil;
import com.otitan.util.PadUtil;
import com.otitan.util.ResourcesManager;
import com.otitan.util.SymobelUtils;
import com.otitan.util.Util;
import com.otitan.util.WebServiceUtil;
import com.otitan.util.ZoomControlView;
import com.titan.loction.baiduloc.LocationService;
import com.titan.navi.BNGuideActivity;
import com.titan.navi.BaiduNavi;
import com.titan.util.UpdateUtil;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Util.MylibUtil;
import Util.ProgressDialogUtil;
import Util.ToastUtil;
import symbol.SymbolUtil;

/**
 *
 */
public class MapActivity extends AppCompatActivity {
	Context mcontext;
	public static String DQLEVEL, loginName, userID, websUtilResult, UNITID;
	// 设备信息
	String TELNO, REALNAME;
	boolean iszhuce = false;//是否注册
	boolean isupdateserver;// 是否上传到服务器
	// 保存对应的featureLayer的 文件名和父级文件夹名称
	public static List<HashMap<String, String>> nameList = new ArrayList<HashMap<String, String>>();
	// 保存添加的FeatureLayer图层
	public static List<FeatureLayer> featureLayerList = new ArrayList<FeatureLayer>();
	String qxname = "";
	// private ImageButton ;
	private Button tcControlBtn, btn_upfireInfo, btn_jiejing, btn_fireLocation,
			day_statistics, btn_huijing, btn_jiejingmanage;
	private Button btn_zbdw, btn_sbzx;
	/* 设备中心 */
	MorePopWindow personCenterPopup;
	// 导航
	ImageButton btn_nav;
	// 地名搜索
	EditText editsearchText;
	// 图层控制
	ImageButton imgbtn_tucengcontrol, imgbtn_weather, imgbtn_plot;
	public static boolean active = false;
	List<Map<String, Object>> searchHistoryData = new ArrayList<Map<String, Object>>();
	public boolean focuse = false;
	ImageButton searchButton;
    //是否导航
	boolean isnav = false;
	//是否标绘
	boolean isplot = false;

	//String authinfo = null;
	public static final String ROUTE_PLAN_NODE = "routePlanNode";
	BaiduNavi baiduNavi = null;
	BaiduRoutePlanListener mBaiduRoutePlanListener;

	private ImageButton btn_clear, btn_cemian, btn_ceju, myLocation;
	public MapView mapView = null;
	//定位点样式
	private MarkerSymbol locationMarkerSymbol = null;
	private String titlePath,titlePath1, imageTitlePath, citytitlePath;
	// 图层
	ArcGISLocalTiledLayer arcGISLocalTiledLayer,arcGISLocalTiledLayer2, arcGISLocalCityTiledLayer;
	ArcGISLocalTiledLayer imageLocalTiledLayer;
	ArcGISDynamicMapServiceLayer dynamiclayer = null;// 专题数据图层
	/*ArcGISFeatureLayer arcGISFeatureLayer = null;
	ArcGISDynamicMapServiceLayer arcGISDynamicMapServiceLayer;
	String featurelayerurl;

	FeatureLayer queryfeatureLayer;

	Envelope initExtent;*/
	GraphicsLayer graphicLayer, graphicsLayerLocation, firepointlayer = null,trackgraphiclayer;
	// 标绘图层
	public static GraphicsLayer plotgraphiclayer;
	private Context context;
	private LocationClient mLocClient;
	public MyLocationListenner myLocationListener = new MyLocationListenner();
	private LayoutInflater inflator;
	protected static final int REFRESH = 0;
	public static int mScreenW, mScreenH;
	private JSONObject object;
	private JSONObject obj = null;
	private JSONArray arr = null;
	private ArrayList<String> dataList = new ArrayList<String>();
	public WebServiceUtil websUtil;
	// 定义搜索服务类
	private double longitude, latitude,altitude;
	/** 判别是取的gps坐标还是百度坐标 true 为百度坐标 */
	boolean dwflag = false;
	String [] locparam=null;//定位三参数
	private double last_point_lon = 0;
	private double last_point_lat = 0;
	LocationManager locMag;
    private MyTouchListener myTouchListener = null;
    Location locA;
	Point wgspoint;
	Point mapPoint, centerPoint;
	PictureMarkerSymbol locationSymbol, firepointSymbol;
	Dialog dialog;
	ListView huodianlistView;
	List<HashMap<String, Object>> arrayList;
	private String currentDetailDress = "";
	private ZoomControlView zoomControls;
	private List<HashMap<String, Object>> tcList = new ArrayList<HashMap<String, Object>>();
	AlertDialog menuDialog;// menu菜单Dialog
	GridView menuGrid;
	View menuView;
	/** 菜单图片 **/
	int[] menu_image_array = { R.drawable.menu_filemanager };
	/** 菜单文字 **/
	String[] menu_name_array = { "切换账号" };

	static String[] countries = null;
	private int fireStateValue;
	private Callout callout;
	/* 轨迹 */
	private Polyline gjPolyline;
	private Graphic gjGraphic;
	private int gjGraphicID;
	TextView guiji_startTime, guiji_endTime;
	Button guijireplay;
	private List<TrackPoint> locations = null;
	private List<Point> tps=null;

	private Polyline TrackPolyline;
	int trackGraphicID;
	public final  int trackerror=11,trackersuccess=12;
	LineSymbol TracklineSymbol = new SimpleLineSymbol(Color.RED, 5);
	List<List<Point>> listpts=null;

	/**空间查询 */
	public final int querydistrict=13;
	Map<String, String>  dismap;
	//
	private boolean isFirstLoc = true;// 是否是首次定位
	private boolean ispad = false;// 是否是平板
	public static Point upPoint;// 定位点
	public static String uptime;// 定位点
	public Point getUpPoint() {
		return upPoint;
	}

	public void setUpPoint(Point upPoint) {
		this.upPoint = upPoint;
	}

	private Graphic locationGraphic;// 自身定位graphic
	private int locationID;// 自身定位graphic的id
	private int circleID;// 定位误差圆的id
	private Graphic circleGraphic;// 误差圆的graphic
	private Polygon circlePolygon;// 误差圆的polygon
	// 空间查询
	IdentifyParameters identifyparams;
	MyIdentifyTask mTask=null;
	int dynamiclayerid;
	String dynamiclayerurl;
	// 动态菜单
	List<com.otitan.entity.Menu> menus = null;
	List<com.otitan.entity.Menu> personcentermenus = new ArrayList<com.otitan.entity.Menu>();
	String personcenterid = null;

	private int graphicID;
	public static int plotgraphicID;
	private Point point;
	private Polyline polyline;
	Graphic plotgarphic;
	private Polygon polygon;
	private Point startPoint;
	private MarkerSymbol markerSymbol;
	private LineSymbol lineSymbol;
	private FillSymbol fillSymbol;
	private Graphic drawGraphic;
	/** 标绘方式及geometry类型 */
	public static final int Entity = 0;
	public static final int POINT = 1;
	public static final int POLYLINE = 2;
	public static final int POLYGON = 3;
	public static final int ENVELOPE = 4;
	public static final int CIRCLE = 5;
	public static final int ELLIPSE = 6;
	public static final int FREEHAND_POLYGON = 7;
	public static final int FREEHAND_POLYLINE = 8;
	/** 标绘样式 */

	private boolean isFirstPoint = true;
	private DecimalFormat decimalFormat = new DecimalFormat(".00");
    //经纬度格式化
	DecimalFormat lonlatdf = new DecimalFormat(".000000");
	public int drawType;
	public static ActionMode actionMode;
	private SharedPreferences sharedPreferences;
	private View tcontrolView;
	// 获取手机内部和外部存储地址
	private ResourcesManager manager;

	Geodatabase geodatabase;
	PopupWindow pop_plot;
	PlotUtil plotUtil;
    LocationService mlocationservice;
	//
	/**定位需要动态获取的权限*/
	String[] reqPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
			.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission
			.READ_EXTERNAL_STORAGE};
	/** 轨迹查询 */
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());

	boolean istime=false;
	public static enum ActionMode {
		/**
		 * 测量面积
		 */
		MODE_CEMIAN,
		/**
		 * 测量长度
		 */
		MODE_CEJU,
		/**
		 * 初始化
		 */
		MODE_ENTITY,
		/**
		 * 导航
		 */
		MODE_NAV,
		/* 小地名查询 */
		MODE_searchXDM,
		/* 空间查询 */
		MODE_ISEARCH,
		/**
		 * 标绘模式
		 */
		MODE_PLOT,
		/* 空白 */
		MODE_EMPERTY

	}
	Intent intent=null;
	Context mContext;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mcontext=this;
		mContext=this;
		//获取GPS服务
		intent=getIntent();
		//locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		setContentView(R.layout.activity_map);

		// 检测是平板还是手机。。
		if (PadUtil.isPad(this)) {
			ispad = true;
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
		try {
			manager = ResourcesManager.getInstance(this);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// 获取设备宽高
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenW = dm.widthPixels; // 得到宽度1280
		mScreenH = dm.heightPixels; // 得到高度752
		//
		context = this;
		websUtil = new WebServiceUtil(getApplicationContext());
		websUtil.initWebserviceTry();
		// 获取上个页面传来的值
		DQLEVEL = sharedPreferences.getString("DQLEVEL", "");// 0代表领导//
		// 1代表市级。。。2代表县级之分
		loginName = sharedPreferences.getString("name", "");// 登陆用户名
		userID = sharedPreferences.getString("userID", "");// 用户id
		UNITID = sharedPreferences.getString("UNITID", "");// 市级用户是1或者0
		ArcGISRuntime.setClientId("qwvvlkN4jCDmbEAO");// 去除水印的
		mapView = (MapView) findViewById(R.id.map);
		//mapView.setMapBackground(mcontext.getColor(R.color.white), mcontext.getColor(R.color.balck), 3, 3);
		// 获取定位图标
		LocationDisplayManager ls = mapView.getLocationDisplayManager();
		try {
			locationMarkerSymbol = ls.getDefaultSymbol();

		} catch (java.lang.Exception e) {

			e.printStackTrace();
		}
        intiView();

		// 内部类MyTouchListener
		myTouchListener = new MyTouchListener(MapActivity.this, mapView);
		mapView.setOnTouchListener(myTouchListener);
		mapView.setOnZoomListener(new OnZoomListener() {

			@Override
			public void preAction(float arg0, float arg1, double arg2) {
			}

			@Override
			public void postAction(float arg0, float arg1, double arg2) {
				if(listpts!=null&&listpts.size()>0){
					trackgraphiclayer.removeAll();
					showGuiJi();
				}
			}
		});
		// 小地名搜索
	/*	View xdmsearch = findViewById(R.id.dimingsearch);
		initXDMsearch(xdmsearch);*/
		/*
		 * if (!(DQLEVEL.equals("2"))) { btn_huijing.setVisibility(View.GONE); }
		 */
		// 添加地图
		try {
			// 贵阳矢量切片
			titlePath = ResourcesManager.getInstance(this)
					.getArcGISLocalTiledLayerPath();
			if (!titlePath.equals("")) {
				arcGISLocalTiledLayer = new ArcGISLocalTiledLayer(titlePath);
				arcGISLocalTiledLayer.setVisible(true);
				mapView.addLayer(arcGISLocalTiledLayer);
			} else {
				ToastUtil.setToast(MapActivity.this, "贵阳切片图层不存在");
			}
			// 全国市界切片
			/*citytitlePath = ResourcesManager.getInstance(this)
					.getArcGISLocalCityTiledLayerPath();
			if (!citytitlePath.equals("")) {
				arcGISLocalCityTiledLayer = new ArcGISLocalTiledLayer(
						citytitlePath);
				mapView.addLayer(arcGISLocalCityTiledLayer);
			} else {
				ToastUtil.setToast(MapActivity.this, "全国市级切片图层不存在");
			}*/
			// 安顺矢量切片
		/*	titlePath1 = ResourcesManager.getInstance(this)
					.getArcGISLocalTiledASLayerPath();
			if (!titlePath.equals("")) {
				arcGISLocalTiledLayer2 = new ArcGISLocalTiledLayer(titlePath1);
				mapView.addLayer(arcGISLocalTiledLayer2);
			} else {
				ToastUtil.setToast(MapActivity.this, "安顺切片图层不存在");
			}*/
			// 影像切片
			imageTitlePath = ResourcesManager.getInstance(this)
					.getArcGISLocalImageLayerPath();
			if (!imageTitlePath.equals("")) {
				imageLocalTiledLayer = new ArcGISLocalTiledLayer(imageTitlePath);
				mapView.addLayer(imageLocalTiledLayer);
				imageLocalTiledLayer.setVisible(false);
			} else {
				ToastUtil.setToast(MapActivity.this, "影像切片图层不存在");
			}
			// 有网络时加载动态图层
			if (MyApplication.IntetnetISVisible) {
				dynamiclayerurl = context.getResources().getString(R.string.dynamiclayerurl);
				// "http://192.168.6.219:6080/arcgis/rest/services/SLFH_GEO2/MapServer";
				dynamiclayer = new ArcGISDynamicMapServiceLayer(dynamiclayerurl);
                dynamiclayerid = mapView.addLayer(dynamiclayer);
                dynamiclayer.setVisible(true);

                if(dynamiclayer.getMapServiceInfo()==null){
                    dynamiclayer=null;
                }
				// 获取设备注册信息
			/*	try {
					getMobileInfo();
				} catch (Exception e) {
					//log.error(e.toString());
					Toast.makeText(context, "获取设备注册信息失败", Toast.LENGTH_SHORT).show();
				}*/
				// 初始化菜单
				try {
					intitalmenu();
				} catch (Exception e) {
					//log.error(e.toString());
					Toast.makeText(context, "初始化菜单失败",Toast.LENGTH_SHORT).show();
				}
				//
			}

		} catch (Exception e) {
			Toast.makeText(context, "地图初始化失败", Toast.LENGTH_SHORT).show();
		}

		locationSymbol = new PictureMarkerSymbol(this.getResources()
				.getDrawable(R.drawable.icon_geo));// locationsymbol
		firepointSymbol = new PictureMarkerSymbol(this.getResources()
				.getDrawable(R.drawable.hot));// hotsymbol
		this.markerSymbol = new SimpleMarkerSymbol(Color.BLACK, 16,
				SimpleMarkerSymbol.STYLE.CIRCLE);
		this.lineSymbol = new SimpleLineSymbol(Color.argb(100, 244, 115, 25), 4);
		this.fillSymbol = new SimpleFillSymbol(Color.BLACK);
		this.fillSymbol.setOutline(this.lineSymbol);
		this.fillSymbol.setAlpha(90);

		mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onStatusChanged(Object source, STATUS status) {
				if (STATUS.INITIALIZED == status) {
					if (source instanceof MapView) {
						graphicLayer = new GraphicsLayer(RenderingMode.STATIC);
						trackgraphiclayer=new GraphicsLayer(RenderingMode.STATIC);
						plotgraphiclayer = new GraphicsLayer(
								RenderingMode.DYNAMIC);
						graphicsLayerLocation = new GraphicsLayer(
								RenderingMode.STATIC);
						mapView.addLayer(graphicLayer);
						mapView.addLayer(trackgraphiclayer);
						mapView.addLayer(graphicsLayerLocation);
						mapView.addLayer(plotgraphiclayer);// 添加标绘图层
                        intiPermisson();
                        intiLocation();
						initBaiduNavi();// 初始化导航

					}
				}

				if (STATUS.LAYER_LOADED == status) {// 每加载一次触发一次
					if (source instanceof ArcGISDynamicMapServiceLayer) {
						runOnUiThread(new Runnable() {
							public void run() {
								if (dynamiclayer != null) {
									ArcGISLayerInfo[] layers = dynamiclayer
											.getLayers();
									if (layers == null)
										return;
									ArcGISLayerInfo layer = layers[0];
									if (layer != null) {
										dynamiclayer.getLayers()[0]
												.setVisible(true);
										for (int j = 0; j < dynamiclayer
												.getLayers()[0].getLayers().length; j++) {
											dynamiclayer.getLayers()[0]
													.getLayers()[j]
													.setVisible(true);
										}
									}
								}
							}
						});
					}

				}
			}
		});

		//intiLocation();// 定位初始化


	}
    /**
     * 初始化view
     */
    private void intiView() {

        zoomControls = (ZoomControlView) findViewById(R.id.ZoomControlView);
        zoomControls.setMapView(mapView);
        if(!ispad){
            zoomControls.setVisibility(View.GONE);
        }
        // 图层控制
        tcontrolView = findViewById(R.id.tuceng_control);
        imgbtn_tucengcontrol = (ImageButton) findViewById(R.id.imgbtn_tucengcontrol);
        imgbtn_tucengcontrol.setOnClickListener(new MyListener());
       /* tcControlBtn = (Button) findViewById(R.id.btn_tcControl);
        tcControlBtn.setVisibility(View.GONE);
        tcControlBtn.setOnClickListener(new MyListener());*/
        // 火情上报
        btn_upfireInfo = (Button) findViewById(R.id.btn_upfireInfo);
        btn_upfireInfo.setVisibility(View.GONE);
        btn_upfireInfo.setOnClickListener(new MyListener());
        // 回警
        btn_huijing = (Button) findViewById(R.id.btn_huijing);
        btn_huijing.setVisibility(View.GONE);
        btn_huijing.setOnClickListener(new MyListener());
        // 接警录入
        btn_jiejing = (Button) findViewById(R.id.btn_jiejing);
        btn_jiejing.setVisibility(View.GONE);
        btn_jiejing.setOnClickListener(new MyListener());
        // 接警管理
        btn_jiejingmanage = (Button) findViewById(R.id.btn_jiejingmanage);
        btn_jiejingmanage.setVisibility(View.GONE);
        btn_jiejingmanage.setOnClickListener(new MyListener());
        // 火情统计
        day_statistics = (Button) findViewById(R.id.btn_fireINFO);
        day_statistics.setVisibility(View.GONE);
        day_statistics.setOnClickListener(new MyListener());
		/* 坐标定位 */
        btn_zbdw = (Button) findViewById(R.id.btn_zbdw);
        btn_zbdw.setVisibility(View.GONE);
        btn_zbdw.setOnClickListener(new MyListener());
		/* 个人中心 */
        btn_sbzx = (Button) findViewById(R.id.btn_sbzx);
        btn_sbzx.setVisibility(View.GONE);
        btn_sbzx.setOnClickListener(new MyListener());
        // 天气预报
		/*imgbtn_weather = (ImageButton) findViewById(R.id.imgbtn_weather);
        imgbtn_weather.setVisibility(View.GONE);
        imgbtn_weather.setOnClickListener(new MyListener());*/
        //态势标绘
        imgbtn_plot = (ImageButton) findViewById(R.id.imgbtn_plot);
        imgbtn_plot.setOnClickListener(new MyListener());
        //当日火情
       /* btn_fireLocation = (Button) findViewById(R.id.btn_fireLocation);
        btn_fireLocation.setOnClickListener(new MyListener());*/
        // 工具栏
        myLocation = (ImageButton) findViewById(R.id.myLocation);
        myLocation.setOnClickListener(new MyListener());
        btn_clear = (ImageButton) findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new MyListener());
        btn_ceju = (ImageButton) findViewById(R.id.btn_ceju);
        btn_ceju.setOnClickListener(new MyListener());
        btn_cemian = (ImageButton) findViewById(R.id.btn_cemian);
        btn_cemian.setOnClickListener(new MyListener());

        // 导航
        btn_nav = (ImageButton) findViewById(R.id.btn_nav);
        btn_nav.setOnClickListener(new MyListener());
    }

    public   MapView getMapview() {
		return mapView;

	}

	private void getMobileInfo() {
		final String result = websUtil.selMobileInfo(MyApplication.SBH);// 获取设备
		if (result.equals("网络异常")) {
			ToastUtil.setToast(MapActivity.this, "网络异常,设备信息获取失败");
			return;
		} else {
			try {
				JSONObject obj = new JSONObject(result);
				JSONArray arr = obj.optJSONArray("ds");
				if (arr != null || !arr.equals("[]")) {
					JSONObject object = arr.optJSONObject(0);
					REALNAME = object.getString("SYZNAME");
					TELNO = object.getString("SYZPHONE");
					iszhuce = true;
				} else {
					ToastUtil.setToast(MapActivity.this, "设备信息未注册，请去个人中心注册");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				ToastUtil.setToast(MapActivity.this, "获取设备注册信息失败");
			}
		}
	}
    /**
     * 初始化菜单
     */
    private void intitalmenu() {

		if(true){
			iszhuce=true;
			btn_upfireInfo.setVisibility(View.VISIBLE);
			btn_jiejing.setVisibility(View.VISIBLE);
			day_statistics.setVisibility(View.VISIBLE);
			btn_jiejingmanage.setVisibility(View.VISIBLE);
            if(ispad){

                btn_zbdw.setVisibility(View.VISIBLE);
				btn_sbzx.setVisibility(View.VISIBLE);
            }

			return;
		}
		// 获取菜单
		menus = websUtil.getMenu(loginName);
		for (com.otitan.entity.Menu menu : menus) {
			if (menu.getParentid().equals("0")) {
				switch (menu.getName()) {
					case "火情上报":
						btn_upfireInfo.setVisibility(View.VISIBLE);
						break;
					case "接警录入":
						btn_jiejing.setVisibility(View.VISIBLE);
						break;
					case "火情统计":
						if (ispad) {// 手机中隐藏按钮
							day_statistics.setVisibility(View.VISIBLE);
						}
						break;
					case "回警":
						btn_huijing.setVisibility(View.VISIBLE);
						break;
					case "接警管理":
						btn_jiejingmanage.setVisibility(View.VISIBLE);
						break;
					case "坐标定位":
						if (ispad) { // 手机中隐藏按钮
							btn_zbdw.setVisibility(View.VISIBLE);
						}

						break;
					case "个人中心":
						btn_sbzx.setVisibility(View.VISIBLE);
						personcenterid = menu.getId();
						for (com.otitan.entity.Menu personmenu : menus) {
							if (personmenu.getParentid().equals(personcenterid)) {
								personcentermenus.add(personmenu);
							}
						}
						break;
					default:
						break;
				}
			}
		}
	}
    /**
     *  定位的初始化
     */
    private void intiLocation() {
        // -----------location config ------------
        //定位初始化
        mlocationservice = ((MyApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        mlocationservice.registerListener(myLocationListener);
        //注册监听
        //int type = getIntent().getIntExtra("from", 0);
        setLocationOption();
		mlocationservice.start();// 定位SDK

	}

    /**
     *  设置定位监听相关参数
     */
	private void setLocationOption() {
		int loctime=sharedPreferences.getInt("time", 1000);
		LocationClientOption option = new LocationClientOption();
	/*	高精度定位模式：这种定位模式下，会同时使用网络定位和GPS定位，优先返回最高精度的定位结果；
		低功耗定位模式：这种定位模式下，不会使用GPS进行定位，只会使用网络定位（WiFi定位和基站定位）；
		仅用设备定位模式：这种定位模式下，不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位。*/
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式 默认高精度（低功耗、仅设备）
		option.setOpenGps(true); //可选，默认false,设置是否使用gps
		option.setCoorType("GCJ-02"); // 设置坐标类型 GCJ-02 bd0911
		option.setScanSpan(loctime);
		option.setNeedDeviceDirect(true);        // 返回的定位结果包含手机机头的方向
		//mLocClient.setLocOption(option);
        mlocationservice.setLocationOption(option);
	}

    /**
     * 空间查询
     * @param view
     */
    public void isearch(View view) {

		if (actionMode == ActionMode.MODE_ISEARCH) {
			actionMode = ActionMode.MODE_ENTITY;
			ToastUtil.setToast(MapActivity.this, "空间查询功能已关闭");
		} else {
			ToastUtil.setToast(MapActivity.this, "空间查询功能已开启");
			active = true;
			actionMode = ActionMode.MODE_ISEARCH;
		}

	}

    /**
     * 地图触摸事件监听
     */
    class MyTouchListener extends MapOnTouchListener {

		MapView map;
		Context context;

		public MyTouchListener(Context context, MapView view) {
			super(context, view);
			this.context = context;
			map = view;
			// 获取callout
			callout = mapView.getCallout();
			callout.setStyle(R.xml.calloutstyle);
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			Point point = mapView.toMapPoint(event.getX(), event.getY());
			if (point == null || point.isEmpty()) {
				return false;
			}

			//qureyCurpt(point,queryfeatureLayer);

			if (active
					&& (drawType == FREEHAND_POLYLINE || drawType == FREEHAND_POLYGON)
					&& event.getAction() == MotionEvent.ACTION_DOWN) {
				switch (drawType) {
					case FREEHAND_POLYLINE:
						polyline.startPath(point);
						break;
					case FREEHAND_POLYGON:
						polygon.startPath(point);
						break;
					default:
						break;
				}
			}
			return super.onTouch(v, event);

		}

		@Override
		public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
			Point point = mapView.toMapPoint(to.getX(), to.getY());
			if (point == null || point.isEmpty()) {
				return false;
			}
			if (active
					&& actionMode.equals(ActionMode.MODE_PLOT)
					&& (drawType == FREEHAND_POLYLINE || drawType == FREEHAND_POLYGON)) {
				switch (drawType) {
					case FREEHAND_POLYGON:
						polygon.lineTo(point);
						plotgraphiclayer.updateGraphic(plotgraphicID, polygon);

						break;
					case FREEHAND_POLYLINE:
						polyline.lineTo(point);
						plotgraphiclayer.updateGraphic(plotgraphicID, polyline);
						break;

				}
				return false;
			}
			return super.onDragPointerMove(from, to);

		}

		@Override
		public void onLongPress(MotionEvent point) {
			super.onLongPress(point);

			Point p = map.toMapPoint(point.getX(), point.getY());
			if (p != null) {
				callout.setContent(loadView(p));
				callout.show(p);
			}
		}

		@Override
		public boolean onSingleTap(MotionEvent event) {
			final Point point = mapView.toMapPoint(event.getX(), event.getY());
			if (active
					&& (drawType == POLYGON || drawType == POLYLINE)
					&& (actionMode.equals(ActionMode.MODE_CEJU) || actionMode
					.equals(ActionMode.MODE_CEMIAN))) {
				switch (drawType) {
					case POLYGON:
						if (startPoint == null) {
							startPoint = point;
							polygon.startPath(point);
						} else
							polygon.lineTo(point);
						DroolCeMian(point);
						graphicLayer.updateGraphic(graphicID, polygon);
						break;
					case POLYLINE:
						if (startPoint == null) {
							startPoint = point;
							polyline.startPath(point);
						} else {
							polyline.lineTo(point);
							isFirstPoint = false;
						}
						DroolCeJu(point);
						graphicLayer.updateGraphic(graphicID, polyline);
						break;
				}
				return true;
			} else if (active && actionMode.equals(ActionMode.MODE_NAV)) {
				//导航
				if(upPoint != null){
					ToastUtil.setToast(MapActivity.this, "正在为您计算导航路线，请稍后");
					Point epoint = (Point) GeometryEngine.project(point,
							mapView.getSpatialReference(),
							SpatialReference.create(4326));
					Point sPoint = (Point) GeometryEngine.project(upPoint,
							mapView.getSpatialReference(),
							SpatialReference.create(4326));
                    BNRoutePlanNode sNode=null,eNode=null;
                    try {
                         sNode = new BNRoutePlanNode(sPoint.getX(),
                                sPoint.getY(), "起点", null, CoordinateType.WGS84);
                         eNode = new BNRoutePlanNode(epoint.getX(),
                                epoint.getY(), "终点", null, CoordinateType.WGS84);
                        mBaiduRoutePlanListener = new BaiduRoutePlanListener(sNode);

                        baiduNavi.initListener(sNode, eNode, CoordinateType.WGS84,
                                mBaiduRoutePlanListener);
                    }catch (Exception e){
                        Toast.makeText(mcontext,"坐标点异常",Toast.LENGTH_SHORT).show();
                    }

				}else{
					ToastUtil.setToast(MapActivity.this,"无法获取当前位置信息");
				}

			} else if (active && actionMode.equals(ActionMode.MODE_ISEARCH)) {
				if(upPoint != null){
					Point sPoint = (Point) GeometryEngine.project(upPoint,
							mapView.getSpatialReference(),
							SpatialReference.create(4326));
					BNRoutePlanNode sNode = new BNRoutePlanNode(sPoint.getX(),
							sPoint.getY(), "起点", null, CoordinateType.WGS84);
					mBaiduRoutePlanListener = new BaiduRoutePlanListener(sNode);
					Polygon circlePolygon = GeometryEngine.buffer(point,
							mapView.getSpatialReference(),
							50 * mapView.getResolution(), null);
					// 实例化对象，并且给实现初始化相应的值
					identifyparams = new IdentifyParameters();
					identifyparams.setTolerance(10);
					identifyparams.setDPI(98);
					identifyparams.setLayerMode(IdentifyParameters.ALL_LAYERS);
					identifyparams.setGeometry(circlePolygon);
					identifyparams.setMapHeight(mapView.getHeight());
					identifyparams.setMapWidth(mapView.getWidth());
					identifyparams.setSpatialReference(mapView
							.getSpatialReference());
					identifyparams.setReturnGeometry(true);
					identifyparams.setLayers(new int[] { 0 });
					Envelope env = new Envelope();
					mapView.getExtent().queryEnvelope(env);
					identifyparams.setMapExtent(env);

					// 我们自己扩展的异步类
					MyIdentifyTask mTask = new MyIdentifyTask(MapActivity.this,
							mapView, dynamiclayerurl, upPoint,
							mBaiduRoutePlanListener);
					mTask.execute(identifyparams);// 执行异步操作并传递所需的参数
				}else {
					ToastUtil.setToast(MapActivity.this,"无法获取当前位置信息");
				}

			} else if (firepointlayer != null) {
				//推送添加火点
				if(upPoint!=null){
					Point sPoint = (Point) GeometryEngine.project(upPoint,
							mapView.getSpatialReference(),
							SpatialReference.create(4326));
					BNRoutePlanNode sNode = new BNRoutePlanNode(sPoint.getX(),
							sPoint.getY(), "起点", null, CoordinateType.WGS84);
					mBaiduRoutePlanListener = new BaiduRoutePlanListener(sNode);
					GraphicLayerutil graphicLayerutil = new GraphicLayerutil(
							MapActivity.this, mapView, upPoint,
							mBaiduRoutePlanListener);
					Graphic firepointg = graphicLayerutil.GetGraphicsFromLayer(
							point, firepointlayer);
					if (firepointg != null) {
						// 显示提示callout
						// Create callout from MapView
						callout = mapView.getCallout();
						callout.setCoordinates(point);
						callout.setOffset(0, -3);
						callout.setStyle(R.xml.calloutstyle);
						// populate callout with results from IdentifyTask
						callout.setContent(graphicLayerutil
								.createIdentifyContent(firepointg.getAttributes()));
						// show callout
						callout.show();
					} else {
						callout.hide();
					}
				}else{
					ToastUtil.setToast(MapActivity.this,"无法获取当前位置信息");
				}

			}


			return false;
		}
	}

	private class MyAsyncTask extends AsyncTask<String, Void, Void> {


		@Override
		protected Void doInBackground(final String... params) {
			if (params[0].equals("myLocation")) {
				runOnUiThread(new Runnable() {
					public void run() {
						if (isFirstLoc) {
							if (1 < mapView.getResolution()) {
								mapView.setResolution(10);
							}
							mapView.centerAt(upPoint, true);
							mapView.invalidate();
							isFirstLoc = false;
						}
					}
				});
			} else if(params[0].equals("mytrack")){
				runOnUiThread( new Runnable() {
					public void run() {
						if (gjPolyline == null) {
							gjPolyline = new Polyline();
							LineSymbol gjSymbol = new SimpleLineSymbol(Color.RED, 5);
							gjGraphic = new Graphic(gjPolyline, gjSymbol);
							gjGraphicID = graphicsLayerLocation.addGraphic(gjGraphic);
							gjPolyline.startPath(upPoint);
						} else {
							gjPolyline.lineTo(upPoint);
						}
						graphicsLayerLocation.updateGraphic(gjGraphicID,
								gjPolyline);
					}
				});

			}else if (params[0].equals("uplocation")) {
				Date now = new Date();
				final SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss", Locale.getDefault());// 修改日期格式
				uptime = dateFormat.format(now);
				String lon = upPoint.getX() + "";
				String lat = upPoint.getY() + "";

				if (MyApplication.IntetnetISVisible)
				{
					try {
						// 上传轨迹到服务器
						boolean isuploc=websUtil.UPLonLat(MyApplication.SBH, upPoint.getX() + "",  upPoint.getY() + "", uptime);
						// 上传轨迹到本地数据库
						boolean isup = DataBaseHelper.UploadLocalDatebase(MyApplication.SBH,
								lon, lat, uptime,"1");
					} catch (Exception e) {
						e.printStackTrace();
					}


				} else
				{
					try {
						DataBaseHelper.UploadLocalDatebase(MyApplication.SBH, lon, lat, uptime,"0");
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			} else if (params[0].equals("updateMobilInfo")) {

				String updateMobilInfo = websUtil.addMobileSysInfo(params[1],
						params[2], params[3], params[4], MyApplication.SBH,
						MyApplication.XLH);
				if (updateMobilInfo.equals("true")) {
					ToastUtil.setToast(MapActivity.this, "更新设备信息成功");
				} else {
					ToastUtil.setToast(MapActivity.this, "更新设备信息失败");
				}
				ProgressDialogUtil.stopProgressDialog();
			} else if (params[0].equals("registerMobilInfo")) {

				String addMobileSysInfo = websUtil.addMobileSysInfo(params[1],
						params[2], params[3], params[4], MyApplication.SBH,
						MyApplication.XLH);
				if (addMobileSysInfo.equals("true")) {
					ToastUtil.setToast(MapActivity.this, "注册设备信息成功");
				} else {
					ToastUtil.setToast(MapActivity.this, "注册设备信息失败");
				}
				ProgressDialogUtil.stopProgressDialog();
			}
			return null;
		}
	}
	@Override
	protected void onNewIntent(Intent intent)
	{
		Intent intent2 = intent;
		if(intent2.hasExtra("com.otitan.getui")){
			if(intent2.getStringExtra("com.otitan.getui").equals("0")){
				String message = intent2.getStringExtra("data");
				Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();
				return;
			}
			String message = intent2.getStringExtra("data");
			Gson gson = new Gson();
			Firepoint fp = null;
			try {
				fp = gson.fromJson(message, Firepoint.class);
				double lon = Convert.convertToAngle(fp.getLON());
				double lat = Convert.convertToAngle(fp.getLAT());
				Point p = new Point(lon, lat);
				Point firepoint = (Point) GeometryEngine.project(p,
						SpatialReference.create(4326),
						mapView.getSpatialReference());
				Map<String, Object> attributes = new HashMap<>();
				attributes.put("经度", fp.getLON());
				attributes.put("纬度", fp.getLAT());
				attributes.put("地址", fp.getADDRESS());
				attributes.put("发现时间", fp.getTIME());
				Graphic g = new Graphic(firepoint, firepointSymbol,
						attributes);
				firepointlayer = new GraphicsLayer();
				firepointlayer.addGraphic(g);
				mapView.addLayer(firepointlayer);
				mapView.centerAt(firepoint, false);
			} catch (Exception e) {
				Toast.makeText(mcontext, "解析推送数据失败", Toast.LENGTH_SHORT).show();
			}


		}

	}

	/**
	 * 测距时绘制
	 */
	public void DroolCeJu(final Point point) {
		runOnUiThread(new Runnable() {
			public void run() {
				// 展示距离
				String str = decimalFormat.format(Math.round(polyline.calculateLength2D()))
						+ "米";
				PictureMarkerSymbol pic;
				Drawable color = MapActivity.this.getResources().getDrawable(
						R.drawable.point_pic);
				int size = 0;
				if (mScreenW > 1280) {
					size = 8;
				} else {
					size = 15;
				}
				if (isFirstPoint) {
					pic = SymobelUtils.TextPicSymobel(MapActivity.this, "起点",
							Color.RED, size, SymobelUtils.MODE.LEFT);
				} else {
					pic = SymobelUtils.TextPicSymobel(MapActivity.this, str,
							Color.RED, size, SymobelUtils.MODE.LEFT);
				}
				PictureMarkerSymbol markerSymbol = new PictureMarkerSymbol(
						MapActivity.this, color);
				Graphic gp = new Graphic(point, markerSymbol);
				Graphic graphic = new Graphic(point, pic);
				graphicLayer.addGraphic(gp);
				graphicLayer.addGraphic(graphic);
			}
		});
	}

	/**
	 * 侧面时进行绘制操作
	 */
	public void DroolCeMian(final Point point) {
		runOnUiThread(new Runnable() {
			public void run() {
				// 展示面积
				if (polygon.getPointCount() >= 3) {
					String str = decimalFormat.format(
							Math.abs(polygon.calculateArea2D())) + "平方米";
					Drawable color = MapActivity.this.getResources()
							.getDrawable(R.drawable.point_pic);
					int size = 0;
					if (mScreenW > 1280) {
						size = 8;
					} else {
						size = 15;
					}
					PictureMarkerSymbol pic = SymobelUtils.TextPicSymobelArea(
							MapActivity.this, str, Color.RED, size,
							SymobelUtils.MODE.LEFT);
					PictureMarkerSymbol markerSymbol = new PictureMarkerSymbol(
							MapActivity.this, color);
					Graphic gp = new Graphic(point, markerSymbol);
					Graphic graphic = new Graphic(point, pic);
					graphicLayer.addGraphic(gp);
					graphicLayer.addGraphic(graphic);
				} else {
					Drawable color = MapActivity.this.getResources()
							.getDrawable(R.drawable.point_pic);
					PictureMarkerSymbol markerSymbol = new PictureMarkerSymbol(
							MapActivity.this, color);
					Graphic gp = new Graphic(point, markerSymbol);
					graphicLayer.addGraphic(gp);
				}
			}
		});
	}

	// 提示框
	private View loadView(Point p) {
		View view = LayoutInflater.from(MapActivity.this).inflate(
				R.layout.callout, null);
		ImageButton close = (ImageButton) view.findViewById(R.id.callout_close);
		close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				callout.hide();
			}
		});
		Point point = (com.esri.core.geometry.Point) GeometryEngine.project(p,
				mapView.getSpatialReference(), SpatialReference.create(4326));
		double locx = point.getX();// 117.227766
		double locy = point.getY();// 31.791418
		wgspoint = SymbolUtil.getPoint(locx, locy);
		DecimalFormat df = new DecimalFormat(".000000");
		final TextView addressText = (TextView) view.findViewById(R.id.address);
		addressText.setText(" 经度:" + df.format(p.getX()) + "\n 纬度:"
				+ df.format(p.getY()));// + "\n " +hash.get("formatted_address")
		return view;
	}





	// handler
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
				case R.id.btn_jiejingmanage:
					//接警管理
					Intent manageintent = new Intent(MapActivity.this,
							AlarmManageActivity.class);
					if(!MyApplication.IntetnetISVisible){
						ToastUtil.setToast(MapActivity.this, "当前网络不可用");
						return;
					}
					try {
						ProgressDialogUtil.startProgressDialog(MapActivity.this);
						String result = websUtil.serchHuoJing(loginName, "", "", "",
								"", "", "", "", "", "", 1, 0);

						manageintent.putExtra("result", result);
						manageintent.putExtra("username", loginName);
						startActivity(manageintent);
						ProgressDialogUtil.stopProgressDialog();
					}catch(Exception e){
						e.printStackTrace();
						ToastUtil.setToast(MapActivity.this, "数据获取失败");
					}
					break;
				case R.id.btn_huijing:
					if (iszhuce) {
						Intent hqintent = new Intent(MapActivity.this,
								HuoQingActivity.class);
						hqintent.putExtra("username", loginName);
						hqintent.putExtra("userID", userID);
						hqintent.putExtra("REALNAME", REALNAME);// 设备绑定的真实姓名
						hqintent.putExtra("TELNO", TELNO);//
						startActivity(hqintent);
					} else {
						ToastUtil.setToast(MapActivity.this, "设备信息未注册，请去个人中心注册");
					}

					break;
				case R.id.btn_jiejing:
					Intent jieJingintent = new Intent(MapActivity.this,
							JJLRActivity.class);
					jieJingintent.putExtra("username", loginName);
					jieJingintent.putExtra("userID", userID);
					jieJingintent.putExtra("DQLEVEL", DQLEVEL);
					jieJingintent.putExtra("UNITID", UNITID);
					startActivity(jieJingintent);
					break;
				//火情上报
				case R.id.btn_upfireInfo:
					if (iszhuce&& userID != null) {
						if(upPoint!=null){
							Point point = (Point) GeometryEngine.project(upPoint,
									mapView.getSpatialReference(),
									SpatialReference.create(4326));
						/*currentDetailDress = testUrlRes(point.getY() + "", point.getX()
								+ "");*/
							Intent fireIntent = new Intent(MapActivity.this,
									UpFireActivity.class);

							fireIntent.putExtra("userID", userID);
							fireIntent.putExtra("longitude", point.getX());
							fireIntent.putExtra("latitude", point.getY());
							//fireIntent.putExtra("address", currentDetailDress);
							fireIntent.putExtra("DQLEVEL", DQLEVEL);
							fireIntent.putExtra("UNITID", UNITID);
							startActivity(fireIntent);
						} else {
							ToastUtil.setToast(MapActivity.this, "无法获取经纬度信息");
						}
					} else {
						ToastUtil.setToast(MapActivity.this, "设备信息未注册，请去个人中心注册");
					}
					break;
				/*	if (iszhuce) {

						Point point = (Point) GeometryEngine.project(upPoint,
								mapView.getSpatialReference(),
								SpatialReference.create(4326));
						//反向地理编码
						currentDetailDress = Geocoding.antgeoCeodingbyalli(point.getY() + "", point.getX()
								+ "");
						Intent fireIntent = new Intent(MapActivity.this,
								UpFireActivity.class);
						if (userID != null && longitude != 0 && latitude != 0
								&& !(latitude + "").equals("4.9E-324")
								&& !(longitude + "").equals("4.9E-324")) {
							fireIntent.putExtra("userID", userID);
							fireIntent.putExtra("longitude", point.getX());
							fireIntent.putExtra("latitude", point.getY());
							fireIntent.putExtra("address", currentDetailDress);
							fireIntent.putExtra("DQLEVEL", DQLEVEL);
							fireIntent.putExtra("UNITID", UNITID);
							startActivity(fireIntent);
						} else {
							ToastUtil.setToast(MapActivity.this, "无法获取经纬度信息");
						}
					} else {
						ToastUtil.setToast(MapActivity.this, "设备信息未注册，请去个人中心注册");
					}
					break;*/
				case R.id.btn_fireINFO:// 火情统计
					showFireInfo();
					break;
				case 1:
					showFirePoint((Integer) msg.obj);
					break;

				case R.id.btn_fireLocation:
					System.out.println(System.currentTimeMillis());
					arrayList = new ArrayList<HashMap<String, Object>>();
					dialog = showDialog(R.layout.activity_huo_dian,
							R.style.testStyle);
					huodianlistView = (ListView) dialog
							.findViewById(R.id.huodianlistView);

					arrayList.clear();
					try {
						websUtilResult = websUtil.selHuoDian(
								countries[Integer.parseInt(UNITID)],
								DQLEVEL, UNITID);
						obj = new JSONObject(websUtilResult);
						arr = obj.optJSONArray("ds");
						if (arr != null) {
							try {
								for (int i = 0; i < arr.length(); i++) {
									object = arr.optJSONObject(i);
									HashMap<String, Object> map = new HashMap<String, Object>();
									map.put("id", object.get("ID"));
									map.put("FIRESTART",
											object.getString("FIRESTART"));
									map.put("address", object.getString("PLACE"));
									map.put("X", object.getString("X"));
									map.put("Y", object.getString("Y"));
									map.put("FIRE_STATE",
											object.getString("FIRE_STATE"));
									map.put("COUNTY", object.getString("COUNTY"));
									map.put("FIREEND", object.getString("FIREEND"));
									arrayList.add(map);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					HuoDianAdapter hdAdapter = new HuoDianAdapter(arrayList,
							getApplicationContext());
					if (hdAdapter != null) {
						huodianlistView.setAdapter(hdAdapter);
					}

					huodianlistView
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
														View view, final int position, long id) {
									runOnUiThread(new Runnable() {
										public void run() {
											dialog.dismiss();
											// showFirePoint(position);
											updateFireInfo(arrayList, position);
										}
									});
								}
							});
					dialog.findViewById(R.id.hddw_returnBtn).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
					dialog.show();
					System.out.println(System.currentTimeMillis());
					break;
				case 2:
					if (msg.obj != null) {
						boolean isInsertSuccess = Boolean.parseBoolean(msg.obj
								.toString());
						if (isInsertSuccess) {
							Toast.makeText(MapActivity.this, "照片上传成功！",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(MapActivity.this, "照片上传失败！",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(MapActivity.this, "网络错误，请稍候重试!",
								Toast.LENGTH_SHORT).show();
					}

					break;
				case 3:
					Toast.makeText(getApplicationContext(), "火情登记成功！",
							Toast.LENGTH_SHORT).show();
					MapActivity.this.finish();
					break;
				case 4:
					String str = (String) msg.obj;
					Toast.makeText(MapActivity.this, str, Toast.LENGTH_SHORT)
							.show();
					break;
				case trackerror:
					com.otitan.util.ProgressDialogUtil.stopProgressDialog();
					Toast.makeText(MapActivity.this, "未查询到轨迹点", Toast.LENGTH_SHORT)
							.show();
					break;
				case trackersuccess:
					showGuiJi();
					mapView.setExtent(TrackPolyline);
					break;
				case 6:
					break;
				default:
					break;
			}
		};
	};


	private TextView endTime;

	public void updateFireInfo(final List<HashMap<String, Object>> list,
							   final int position) {
		LayoutInflater layInflater = LayoutInflater.from(context);
		View view = layInflater.inflate(R.layout.dialog_updatefireinfo, null);
		final Dialog dialogTC = new Dialog(context, R.style.testStyle);
		dialogTC.setContentView(view);
		TextView time = (TextView) dialogTC.findViewById(R.id.fireinfo_time);
		TextView quxian = (TextView) dialogTC
				.findViewById(R.id.fireinfo_quxian);
		TextView place = (TextView) dialogTC.findViewById(R.id.fireinfo_place);
		endTime = (TextView) dialogTC.findViewById(R.id.endTime);
		endTime.setOnClickListener(new MyListener());
		final Spinner state = (Spinner) dialogTC
				.findViewById(R.id.fireinfo_state);
		ArrayAdapter<String> fireStateSpinnerAdapter;
		fireStateValue = Integer.valueOf(list.get(position).get("FIRE_STATE")
				.toString());
		final String[] fireStates = getResources().getStringArray(
				R.array.currentqingkuang);
		fireStateSpinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, fireStates);
		fireStateSpinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		state.setAdapter(fireStateSpinnerAdapter);
		state.setSelection(fireStateValue + 1);
		if (DQLEVEL.equalsIgnoreCase("0")) {
			state.setClickable(false);
			endTime.setClickable(false);
		}
		state.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				if (position == 0) {
					fireStateValue = position;
				} else {
					fireStateValue = position - 1;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		time.setText("时间:" + list.get(position).get("FIRESTART").toString());
		quxian.setText("区县:" + list.get(position).get("COUNTY").toString());
		place.setText(list.get(position).get("address").toString());
		System.out.println(list.get(position).get("FIREEND").toString());
		if (!list.get(position).get("FIREEND").toString()
				.equalsIgnoreCase("0001/1/1 0:00:00")) {
			endTime.setText(list.get(position).get("FIREEND").toString());
		}

		ImageButton returnBtn = (ImageButton) dialogTC
				.findViewById(R.id.returnBtn);
		Button dingwei = (Button) dialogTC.findViewById(R.id.bt_dingwei);
		final Button update = (Button) dialogTC.findViewById(R.id.bt_update);

		dingwei.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogTC.dismiss();
				Message msg = new Message();
				msg.what = 1;
				msg.obj = position;
				handler.sendMessage(msg);
				//showFirePoint(position);
			}
		});

		update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DQLEVEL.equalsIgnoreCase("0")) {
					Toast.makeText(context, "用户无修改权限", Toast.LENGTH_SHORT)
							.show();
				} else {
					int id = Integer.valueOf(list.get(position).get("id")
							.toString());
					if (endTime.getText().toString().equals("点击选择时间")) {
						endTime.setText("");
					}
					boolean result = websUtil.updateFireinfoState(id,
							fireStateValue, endTime.getText().toString());
					if (result) {
						dialogTC.dismiss();
						Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
		});

		returnBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogTC.dismiss();
			}
		});
		dialogTC.show();
	}
	/**
	 *
	 * 轨迹回放
	 */
	protected void showGuiJi() {
		TrackUtil trackutil=new TrackUtil(context);
		if(graphicLayer!=null){
			graphicLayer.removeAll();
			//TrackPolyline=null;
		}
		//List<List<Point>> listpts=TrackUtil.OptimizeTrackPoint(locations,mapView);

		//Toast.makeText(MapActivity.this, "共查询到"+tps.size()+"个轨迹点", Toast.LENGTH_SHORT).show();
		if(listpts==null||listpts.size()<1.){
			com.otitan.util.ProgressDialogUtil.stopProgressDialog();
			Toast.makeText(MapActivity.this, "未查询到符合条件的轨迹点", Toast.LENGTH_SHORT)
					.show();
		}
		for(List<Point> s:listpts){
			int i=0;
			Point pp=s.get(0);
			for(Point p :s){
				if(i==0){
					TrackPolyline = new Polyline();
					TrackPolyline.startPath(p);


				}else{
					TrackPolyline.lineTo(p);
					if(i==s.size()-1){
						//判断是否是终点添加起止点符号
						Graphic TrackGraphic = new Graphic(TrackPolyline,
								TrackUtil.tracksym);
						trackGraphicID = graphicLayer
								.addGraphic(TrackGraphic);
						Graphic spg = new Graphic(TrackPolyline.getPoint(0),TrackUtil.sp);
						graphicLayer.addGraphic(spg);
						Graphic epg = new Graphic(p,TrackUtil.ep);
						graphicLayer.addGraphic(epg);
					}else{
						Point sp1 =mapView.toScreenPoint(pp);
						Point sp2 =mapView.toScreenPoint(s.get(i));

						if(Calculate.TwoPoint_Distance(sp1, sp2)>150){
							//Calculate.TwoPoint_Distance(pp, s.get(i))
							//double dd=Calculate.TwoPoint_Distance(sp1, sp2);
							pp=s.get(i);
							Point midpoint=Calculate.TwoPoint_Midpoint(pp,s.get(i));
							double degree=Calculate.TwoPoint_slope(s.get(i), s.get(i+1));
							TrackUtil.trackarrow.setAngle( (float) degree);
							Graphic trackp = new Graphic(pp, TrackUtil.trackarrow);
							trackgraphiclayer.addGraphic(trackp);
						}

					}
				}
				i++;
			}
		}
		//listpts.clear();
		mapView.addLayer(graphicLayer);
		mapView.addLayer(trackgraphiclayer);

		com.otitan.util.ProgressDialogUtil.stopProgressDialog();
	}

	// 定位活动
	public void showFirePoint(int position) {
		try {
			graphicLayer.removeAll();
			double longitude = Double.parseDouble(arr.optJSONObject(position)
					.getString("X"));
			double latitude = Double.parseDouble(arr.optJSONObject(position)
					.getString("Y"));
			double[] latlng = GpsCorrect.gpsToBD09(String.valueOf(latitude),
					String.valueOf(longitude));
			longitude = 2 * longitude - latlng[0];
			latitude = 2 * latitude - latlng[1];
			Point point = new Point(longitude, latitude);
			Point gpoint = (Point) GeometryEngine.project(point,
					SpatialReference.create(4326),
					mapView.getSpatialReference());
			PictureMarkerSymbol picSymbol;
			if (arr.optJSONObject(position).getString("FIRE_STATE").equals("0")) {
				picSymbol = new PictureMarkerSymbol(MapActivity.this
						.getResources().getDrawable(R.drawable.nohotbig));
			} else {
				picSymbol = new PictureMarkerSymbol(MapActivity.this
						.getResources().getDrawable(R.drawable.hotbig));
			}
			Graphic g = new Graphic(gpoint, picSymbol);
			graphicLayer.addGraphic(g);
			if (5 < mapView.getResolution()) {
				mapView.setResolution(5);
			}
			mapView.setExtent(gpoint);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void deactivate() {
		if (graphicLayer != null) {
			this.graphicLayer.removeGraphic(graphicID);
		}
		this.active = false;
		this.drawType = -1;
		this.point = null;
		this.polygon = null;
		this.polyline = null;
		this.drawGraphic = null;
		this.startPoint = null;
	}

	public void activate(int drawType) {
		if (this.mapView == null)
			return;

		this.deactivate();
		this.drawType = drawType;
		this.active = true;
		switch (this.drawType) {
			case POINT:
				point = new Point();
				drawGraphic = new Graphic(point, markerSymbol);
				if (actionMode == actionMode.MODE_PLOT) {
					polygon = new Polygon();
					drawGraphic = new Graphic(point, firepointSymbol);
					plotgraphicID = plotgraphiclayer.addGraphic(drawGraphic);
				}
				break;
			case POLYGON:
				polygon = new Polygon();
				drawGraphic = new Graphic(polygon, fillSymbol);
				if (actionMode == actionMode.MODE_PLOT) {
					polygon = new Polygon();
					drawGraphic = new Graphic(polygon, fillSymbol);
					plotgraphicID = plotgraphiclayer.addGraphic(drawGraphic);
				}
				break;
		/*case CIRCLE:
			polygon = new Polygon();
			drawGraphic = new Graphic(polygon, fillSymbol);
			if (actionMode == actionMode.MODE_PLOT) {
				polygon = new Polygon();
				drawGraphic = new Graphic(polygon, fillSymbol);
				plotgraphicID = plotgraphiclayer.addGraphic(drawGraphic);
			}
			break;*/
			case FREEHAND_POLYGON:
			/*if (actionMode == actionMode.MODE_PLOT) {
				polygon = new Polygon();
				drawGraphic = new Graphic(polygon, plotFireArea);
				plotgraphicID = plotgraphiclayer.addGraphic(drawGraphic);
			}*/
				break;
			case FREEHAND_POLYLINE:
			/*if (actionMode == actionMode.MODE_PLOT) {
				polyline = new Polyline();
				switch (plotType) {
				case FIREBREAK:
					drawGraphic = new Graphic(polyline,
							PlotUtil.getPlotbreaklineSymbol());
					break;

				default:
					break;
				}

				plotgraphicID = plotgraphiclayer.addGraphic(drawGraphic);
			}
			break;*/
			case POLYLINE:
				polyline = new Polyline();
				drawGraphic = new Graphic(polyline, lineSymbol);
				if (actionMode == actionMode.MODE_PLOT) {
					polyline = new Polyline();
					drawGraphic = new Graphic(polyline,
							PlotUtil.getPlotlineSymbol());
					plotgraphicID = plotgraphiclayer.addGraphic(drawGraphic);
				}
				break;
		}
		graphicID = graphicLayer.addGraphic(drawGraphic);
	}

	public class MyListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			countries = getResources().getStringArray(R.array.counties);
			Message msg = new Message();
			switch (v.getId()) {
				case R.id.btn_sbzx:
					if(personCenterPopup!=null&&personCenterPopup.isShowing()){
						personCenterPopup.dismiss();

					}else{
						// 设备中心
						personCenterPopup = new MorePopWindow(MapActivity.this,
								R.layout.popup_person_center);
						if (ispad) {
							personCenterPopup.showAtLocation(btn_sbzx, Gravity.BOTTOM
											| Gravity.RIGHT, btn_sbzx.getWidth() / 2
											- personCenterPopup.getWidth() / 2,
									btn_sbzx.getHeight() + 10);

						} else {
							personCenterPopup.showAtLocation(btn_sbzx, Gravity.BOTTOM
											| Gravity.RIGHT, btn_sbzx.getWidth() * 2,
									btn_sbzx.getHeight() + 5);

						}

						View popup_mobile_info = personCenterPopup.getContentView()
								.findViewById(R.id.popup_mobile_info);
						popup_mobile_info.setOnClickListener(new MyListener());

						View popup_mobile_register = personCenterPopup.getContentView()
								.findViewById(R.id.popup_mobile_register);
						popup_mobile_register.setOnClickListener(new MyListener());

						View systemtings = personCenterPopup.getContentView()
								.findViewById(R.id.system_setting);
						systemtings.setOnClickListener(new MyListener());

						View guijiganli = personCenterPopup.getContentView()
								.findViewById(R.id.guijiganli);
						guijiganli.setOnClickListener(new MyListener());
						//设备直连
						View devicecon = personCenterPopup.getContentView()
								.findViewById(R.id.popup_devicecon);
						devicecon.setOnClickListener(new MyListener());


					}

					break;
				case R.id.guijiganli:
					// 轨迹查询
					personCenterPopup.dismiss();
					View guijisearch = findViewById(R.id.guijisearch);
					showDialogGuijiSearch(guijisearch);

					break;
				case R.id.popup_mobile_register:
					// 设备使用者信息注册
					personCenterPopup.dismiss();
					showMobileInfoRegister();
					break;
				case R.id.popup_mobile_info:
					// 设备信息获取并更新
					personCenterPopup.dismiss();
					runOnUiThread(new Runnable() {
						public void run() {
							showMobileInfo();
						}
					});
					break;
				case R.id.system_setting:
					// 系统设置
					personCenterPopup.dismiss();
					showSystemSettings();
					break;
				case R.id.btn_zbdw:
					// 坐标定位
					showInputDialog();
					break;
				// 测面积
				case R.id.btn_cemian:

					actionMode = ActionMode.MODE_CEMIAN;
					drawType = POLYGON;
					activate(drawType);
					break;
				// 测距
				case R.id.btn_ceju:
					actionMode = ActionMode.MODE_CEJU;
					drawType = POLYLINE;
					activate(drawType);
					break;
				//清空图层
				case R.id.btn_clear:
					if(isplot){
					// 清空标绘图层
						//plotUtil.plotgarphic
						plotgraphiclayer.removeGraphic(plotUtil.plotgraphicID);
						plotUtil.activate(plotUtil.plotType);
						/*plotgraphiclayer.updateGraphic(plotUtil.plotgraphicID,null);
					    plotgraphiclayer.removeAll();*/
						break;
				}
					active = false;
					drawType = Entity;
					isFirstPoint = true;
					if (actionMode == ActionMode.MODE_CEMIAN && polygon != null) {
						startPoint = null;
						activate(drawType);
					}
					actionMode = ActionMode.MODE_ENTITY;
					graphicLayer.removeAll();
					// 清空标绘图层
					plotgraphiclayer.removeAll();
					if (callout != null) {
						callout.hide();
					}
					break;
				case R.id.endTime:
					Dialog endDateDialog = new DateDialog(MapActivity.this, endTime);
					endDateDialog.show();

					break;
				// 图层控制
				case R.id.btn_tcControl:
					tcontrolView.setVisibility(View.VISIBLE);
					showTcDialog();
					break;
				// 轨迹回放开始时间
				case R.id.guiji_startTime:
					Dialog guijistartDateDialog = new DateDialog(MapActivity.this,
							guiji_startTime);
					guijistartDateDialog.show();

					break;
				// 轨迹回放结束时间
				case R.id.guiji_endTime:
					Dialog guijiendDateDialog = new DateDialog(MapActivity.this,
							guiji_endTime);
					guijiendDateDialog.show();
					break;
				// 轨迹查询
				case R.id.btn_guijireplay:
					com.otitan.util.ProgressDialogUtil.startProgressDialog(mcontext,"正在查询中....");
					final String starttime = guiji_startTime.getText().toString();
					final String endtime = guiji_endTime.getText().toString();
					//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					if (starttime == "" || endtime == "") {
						Toast.makeText(MapActivity.this, "请填写时间", Toast.LENGTH_SHORT).show();
						com.otitan.util.ProgressDialogUtil.stopProgressDialog();
					} else {
						try {
							Date d1=sdf.parse(starttime);
							Date d2=sdf.parse(endtime);
							if(d1.getTime()>d2.getTime()){
								Toast.makeText(MapActivity.this, "结束时间小于开始时间", Toast.LENGTH_SHORT).show();
								com.otitan.util.ProgressDialogUtil.stopProgressDialog();
								return;
							}
						} catch (ParseException e1) {
							e1.printStackTrace();
						}

						new Thread(new Runnable() {

							@Override
							public void run() {
								Message msg=new Message();
								try {

									locations=DataBaseHelper.getTrack(starttime, endtime);

									if(locations.size()<1||locations==null){
										msg.what=trackerror;
										handler.sendMessage(msg);
									}else{
										listpts=TrackUtil.OptimizeTrackPoint(locations,mapView);
										msg.what=trackersuccess;
										handler.sendMessage(msg);
									}

								} catch (Exception e) {
									e.printStackTrace();
									//log.debug(LoggerManager.getExceptionMessage(e));
									msg.what=trackerror;
									handler.sendMessage(msg);
								}

							}
						}).start();

					}

					break;
				// 火情统计
				case R.id.btn_fireINFO:
					msg.what = R.id.btn_fireINFO;
					msg.obj = true;
					handler.sendMessage(msg);
					break;

				// 自身定位
				case R.id.myLocation:
					if (upPoint != null) {

						isFirstLoc = true;
						Point point = (Point) GeometryEngine.project(upPoint,
								mapView.getSpatialReference(),
								SpatialReference.create(4326));
						// callout.show(upPoint, loadView(point));提示定位信息
						new MyAsyncTask().execute("myLocation");
					} else {
						if (!Util.isOPen(MapActivity.this)) {
							ToastUtil.setToast(MapActivity.this, "请先开启gps定位");
						} else if (!NetUtil.onNetChange(MapActivity.this)) {
							ToastUtil.setToast(MapActivity.this, "无法获取位置信息,打开网络试试");
						} else {
							ToastUtil.setToast(MapActivity.this, "无法获取位置信息");
						}
					}
					break;

				// 火情上报
				case R.id.btn_upfireInfo:
					msg.what = R.id.btn_upfireInfo;
					msg.obj = true;
					handler.sendMessage(msg);
					break;
				// 当日火情
				case R.id.btn_fireLocation:

					msg.what = R.id.btn_fireLocation;
					msg.obj = true;
					handler.sendMessage(msg);
					break;

				// 接警录入
				case R.id.btn_jiejing:

					msg.what = R.id.btn_jiejing;
					msg.obj = true;
					handler.sendMessage(msg);

					break;
				// 回警
				case R.id.btn_huijing:
					msg.what = R.id.btn_huijing;
					msg.obj = true;
					handler.sendMessage(msg);
					break;

				// 接警管理
				case R.id.btn_jiejingmanage:
					if(NetUtil.onNetChange(mcontext)){
						ProgressDialogUtil.startProgressDialog(MapActivity.this);
						new Thread(new Runnable() {

							@Override
							public void run() {
								Message msg=new Message();
								String result = websUtil.serchHuoJing(loginName, "", "", "",
										"", "", "", "", "", "", 1, 0);
								if (result.equals("网络异常")) {
									msg.obj = "网络异常";
								}
								else{
									msg.obj = result;
								}
								msg.what = R.id.btn_jiejingmanage;
								handler.sendMessage(msg);
							}
						}).start();
					}else {
						ToastUtil.setToast(MapActivity.this, "网络异常，请检查网络");
					}

					break;
				// 导航点击
				case R.id.btn_nav:
					if (isnav) {
						isnav = false;
						actionMode = ActionMode.MODE_ENTITY;
						ToastUtil.setToast(MapActivity.this, "导航功能已关闭");
					} else {
						ToastUtil.setToast(MapActivity.this, "导航功能已开启请在地图上选择终点");
						isnav = true;
						actionMode = ActionMode.MODE_NAV;
						activate(POINT);
					}
					break;
				// migbtn 图层控制
				case R.id.imgbtn_tucengcontrol:
					if (tcontrolView.isShown()) {
						tcontrolView.setVisibility(View.GONE);
					} else {
						tcontrolView.setVisibility(View.VISIBLE);
						showTcDialog();
					}

					break;
				case R.id.imgbtn_weather:
					QueryWeatherTask weathertask = new QueryWeatherTask(
							MapActivity.this, mapView);
					weathertask.execute(latitude + "", longitude + "");
					break;
				// 态势标绘
				case R.id.imgbtn_plot:

					if (isplot) {
						isplot = false;
						actionMode = ActionMode.MODE_ENTITY;
						plotUtil.closePlotDialog();
						mapView.setOnTouchListener(myTouchListener);
						ToastUtil.setToast(MapActivity.this, "标绘功能已关闭");
					} else {
						plotUtil = new PlotUtil(MapActivity.this, mapView);
						isplot = true;
						ToastUtil.setToast(MapActivity.this, "标绘功能已开启");
						mapView.setOnTouchListener(plotUtil.plotTouchListener);
						plotUtil.showPlotDialog(v);
					}

					break;
				//设备直连
				case R.id.popup_devicecon:
					Intent intent =new Intent(MapActivity.this,MonitorActivity.class);
					intent.putExtra("TD", "-32");
					intent.putExtra("LXJID", "6");
					intent.putExtra("username", "admin");
					intent.putExtra("password", "admin12345");
					String devip=sharedPreferences.getString("monitorip","192.168.0.64");
					intent.putExtra("ip", devip);
					startActivity(intent);
					break;
				default:
					break;
			}
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
		if (secondTime - firstTime > 800) { // 如果两次按键时间间隔大于800毫秒，则不退出

			ToastUtil.setToast(MapActivity.this, "再按一次退出程序");
			firstTime = secondTime;// 更新firstTime
			//结束查询task
			if(mTask!=null&&mTask.getStatus()== AsyncTask.Status.RUNNING){
				mTask.cancel(true);
			}
			return;
		} else {
			System.exit(0);// 否则退出程序
		}
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		menuView = View.inflate(this, R.layout.gridview_menu, null);
		// 创建AlertDialog
		menuDialog = new AlertDialog.Builder(this).create();
		menuDialog.setView(menuView);
		menuDialog.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
								 KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_MENU)// 监听按键
					dialog.dismiss();
				return false;
			}
		});
		menuDialog.show();

		menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		menuGrid.setAdapter(getMenuAdapter(menu_name_array, menu_image_array));

		/** 监听menu选项 **/
		menuGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				switch (position) {
					case 0:
						Intent lgnIntent = new Intent(MapActivity.this,
								LoginActivity.class);
						lgnIntent.putExtra("isqiehuan", true);
						startActivity(lgnIntent);
						MapActivity.this.finish();
						break;
					default:
						break;
				}
			}
		});

		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// 必须创建一项
		return super.onCreateOptionsMenu(menu);
	}

	private SimpleAdapter getMenuAdapter(String[] menuNameArray,
										 int[] imageResourceArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", imageResourceArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.item_menu, new String[] { "itemImage", "itemText" },
				new int[] { R.id.item_image, R.id.item_text });
		return simperAdapter;
	}

	// 弹出自定义dialog
	public Dialog showDialog(int resource, int theme) {

		inflator = LayoutInflater.from(getApplicationContext());
		View view = inflator.inflate(resource, null);
		Dialog dialog = new Dialog(MapActivity.this, theme);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				ArrayList<String> tDataList = (ArrayList<String>) bundle
						.getSerializable("dataList");
				if (tDataList != null) {
					if (tDataList.size() < 20) {
						tDataList.add("camera");
					}
					dataList.clear();
					dataList.addAll(tDataList);
				}
			}
		}
	}
	/**
	 * 定位监听函数，更新位置时获取位置信息
	 */
	public class MyLocationListenner implements BDLocationListener {
		public void onReceiveLocation(BDLocation location) {

			if (null != location && location.getLocType() != BDLocation.TypeServerError) {
				//安顺林业局：105.9479815  	26.2492988
				//Point point1= GpsUtil.getInstance(context).getGPSpoint(location);//获取gps坐标或者gcj02坐标
                Point point1=new Point(location.getLongitude(),location.getLatitude(),location.getAltitude());
				final Point pt=(Point) GeometryEngine.project(point1, SpatialReference.create(4326), mapView.getSpatialReference());
				//final Point pt = new Point(667279.435, 2956758.767);
				//首次定位查询当前所在区县
				if(isFirstLoc){
					//qureyCurpt(pt);
					new Thread(new Runnable() {

						@Override
						public void run() {
							dismap=DataBaseHelper.queryDistrict(pt);
							if(dismap!=null){
								locparam=DataBaseHelper.getLocParam(dismap.get("CODE"));
								Message msg=new Message();
								msg.what=querydistrict;
								handler.sendMessage(msg);

							}
						}
					}).start();

				}
				//Gps gps = PositionUtil.gcj_To_Gps84(point);
                upPoint=PositionUtil.meth(point1,locparam);//使用纠偏参数进行校正和投影转换
				longitude=upPoint.getX();
				latitude=upPoint.getY();
				createLocationGraphic(upPoint);
				/* 坐标实时定位 */
				new MyAsyncTask().execute("myLocation");
				/* 坐标实时上传到服务器及本地保存 */
				if (last_point_lon != longitude || last_point_lat != latitude) {

					int l = Calculate.Distance(last_point_lon, last_point_lat,
							longitude, latitude);
					//int dis= sharedPreferences.getInt("distance",100);
					//两点定位间隔大于100米则上传坐标
					if (l > sharedPreferences.getInt("distance", 100)) {
						last_point_lon = longitude;
						last_point_lat = latitude;
						if(sharedPreferences.getBoolean("guiji", false)){
							new MyAsyncTask().execute("uplocation");// 上传位置
						}


						if(sharedPreferences.getBoolean("zongji", false)){
							ToastUtil.makeText(mcontext, "轨迹跟踪中", 0);
							//轨迹跟踪
							new MyAsyncTask().execute("mytrack");
						}
					}

				}
			}

		}
	}


	/* 创建定位时的误差园及定位中心点 */
	public void createLocationGraphic(Point upPoint) {
        if(upPoint==null){
			return;
		}
		if (locationGraphic == null) {
			locationGraphic = new Graphic(upPoint, locationMarkerSymbol);
			locationID = graphicsLayerLocation.addGraphic(locationGraphic);
		} else {
			graphicsLayerLocation.updateGraphic(locationID, upPoint);
		}
		/*circlePolygon = GeometryEngine.buffer(upPoint,
				mapView.getSpatialReference(), 100, null);
		if (circleGraphic == null) {
			FillSymbol symbol = DroolCircle();
			circleGraphic = new Graphic(circlePolygon, symbol);
			circleID = graphicsLayerLocation.addGraphic(circleGraphic);
		} else {
			graphicsLayerLocation.updateGraphic(circleID, circlePolygon);
		}*/
	}




	/**
	 * 当日火情统计
	 */
	private void showFireInfo() {
		Toast.makeText(context,"今日无火情",Toast.LENGTH_SHORT).show();
		return;
		/*websUtilResult = websUtil
				.selHuoDian(countries[Integer.parseInt(UNITID)].toString(),
						DQLEVEL, UNITID);*/
		/*LayoutInflater layInflater = LayoutInflater.from(context);
		View view = layInflater.inflate(R.layout.dialog_hqtj, null);
		final Dialog dialogFire = new Dialog(context, R.style.customDialog);

		*//*final Dialog dialogFire = new Dialog(context);
		android.view.WindowManager.LayoutParams lparam= dialogFire.getWindow().getAttributes();
		lparam.width=(int) (MyApplication.screen.widthPixels*0.8);*//*
		dialogFire.setContentView(view);
		dialogFire.setCanceledOnTouchOutside(true);
		//dialogFire.getWindow().setAttributes(lparam);
		final TextView dayView = (TextView) dialogFire
				.findViewById(R.id.day_view);
		final List<HashMap<String, Object>> stateList = new ArrayList<HashMap<String, Object>>();
		final Set<String> countrySet = new HashSet<String>();

		if (websUtilResult != null && websUtilResult.length() > 0) {
			try {
				obj = new JSONObject(websUtilResult);
				if (obj != null) {
					arr = obj.optJSONArray("ds");
					if (arr != null) {
						try {
							for (int i = 0; i < arr.length(); i++) {
								object = arr.optJSONObject(i);
								HashMap<String, Object> mapState = new HashMap<String, Object>();
								mapState.put("FIRE_STATE",
										object.getString("FIRE_STATE"));
								mapState.put("COUNTY",
										object.getString("COUNTY"));
								countrySet.add(object.getString("COUNTY"));
								stateList.add(mapState);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				} else {

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (stateList.size() == 0) {
				dayView.setText("今日无火情");
			} else {
				try {
					int size = 0;
					int nSize = 0;
					StringBuffer fireInfo = new StringBuffer();
					fireInfo.append("今日共发生 " + stateList.size() + " 场火情\n");
					for (String strs : countrySet) {
						for (int i = 0; i < stateList.size(); i++) {
							if (strs.equalsIgnoreCase((String) stateList.get(i)
									.get("COUNTY"))) {
								if (stateList.get(i).get("FIRE_STATE")
										.equals("0")) {
									size++;// 已经熄灭
								} else if (stateList.get(i).get("FIRE_STATE")
										.equals("1")) {
									nSize++;//
								}
							}
						}
						fireInfo.append(strs + ": 发生 " + (size + nSize)
								+ " 场火情,熄灭 " + size + " 场，正在燃烧 " + nSize
								+ " 场\n");
						size = 0;
						nSize = 0;
					}
					dayView.setText(fireInfo.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			dialogFire.show();
		}*/
	}


	// 方法二画圆
	public FillSymbol DroolCircle() {
		FillSymbol symbol = new SimpleFillSymbol(Color.RED);
		SimpleLineSymbol simplelinesymbol = new SimpleLineSymbol(
				Color.TRANSPARENT, (float) 0.1);
		symbol.setOutline(simplelinesymbol);
		symbol.setAlpha(30);


		return symbol;
	}

	/**
	 * 图层显示
	 */
	public void showTcDialog() {
		//ExpandableListView exp_title= findViewById(R.id.)
		ImageView closeView = (ImageView) findViewById(R.id.close_tuceng);
		closeView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				tcontrolView.setVisibility(View.GONE);
			}
		});

		if (arcGISLocalTiledLayer != null) {
			LinearLayout layer= (LinearLayout) findViewById(R.id.layer_title);
			layer.setVisibility(View.VISIBLE);
			// 基础图
			CheckBox cb_sl = (CheckBox) findViewById(R.id.cb_sl);
			cb_sl.setChecked(arcGISLocalTiledLayer.isVisible());
			// 基础图
			cb_sl.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0,  boolean arg1) {

					if (arg1) {
						if (arcGISLocalTiledLayer != null) {
							if (!arcGISLocalTiledLayer.isVisible()) {
								arcGISLocalTiledLayer.setVisible(true);
							}
						}
					} else {
						if (arcGISLocalTiledLayer != null) {
							if (arcGISLocalTiledLayer.isVisible()) {
								arcGISLocalTiledLayer.setVisible(false);
							}
						}
					}
				}


			});
			// 基础图 缩放到地图范围
			ImageView tileView = (ImageView) findViewById(R.id.tile_extent);
			tileView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					runOnUiThread(new Runnable() {
						public void run() {
							if (arcGISLocalTiledLayer != null) {
								if (arcGISLocalTiledLayer.isVisible()) {
									mapView.setExtent(arcGISLocalTiledLayer
											.getFullExtent());
									mapView.invalidate();
								} else {
									ToastUtil.setToast(MapActivity.this, "基础图未加载");
								}
							} else {
								ToastUtil.setToast(MapActivity.this, "基础图不存在");
							}
							if (arcGISLocalCityTiledLayer != null) {
								if (arcGISLocalCityTiledLayer.isVisible()) {
									mapView.setExtent(arcGISLocalCityTiledLayer
											.getFullExtent());
									mapView.invalidate();
								} else {
									ToastUtil.setToast(MapActivity.this, "市界图未加载");
								}
							} else {
								ToastUtil.setToast(MapActivity.this, "市界图不存在");
							}
						}
					});
				}
			});
		}

		if(arcGISLocalTiledLayer!=null){
			LinearLayout layer= (LinearLayout) findViewById(R.id.layer_img);
			layer.setVisibility(View.VISIBLE);
			// 影像图
			CheckBox cb_yx = (CheckBox) findViewById(R.id.cb_ys);
			if (imageLocalTiledLayer != null) {
				cb_yx.setChecked(imageLocalTiledLayer.isVisible());
			}
			// 影像
			cb_yx.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					if (arg1) {
						if (imageLocalTiledLayer != null) {
							if (!imageLocalTiledLayer.isVisible()) {
								imageLocalTiledLayer.setVisible(true);
							}
							mapView.invalidate();
						}
					} else {
						if (imageLocalTiledLayer != null) {
							if (imageLocalTiledLayer.isVisible()) {
								imageLocalTiledLayer.setVisible(false);
							}
						}
					}
				}
			});
			// 影像图所放到地图范围
			ImageView imageView = (ImageView) findViewById(R.id.image_extent);
			imageView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					runOnUiThread(new Runnable() {
						public void run() {
							if (imageLocalTiledLayer != null) {
								if (imageLocalTiledLayer.isVisible()) {
									mapView.setExtent(imageLocalTiledLayer
											.getFullExtent());
									mapView.invalidate();
								} else {
									ToastUtil.setToast(MapActivity.this, "影像图未加载");
								}
							} else {
								ToastUtil.setToast(MapActivity.this, "影像文件不存在");
							}
						}
					});
				}
			});
		}
		// 专题图层
		if (dynamiclayer != null) {
            LinearLayout layer = (LinearLayout) findViewById(R.id.layer_zhuanti);
            layer.setVisibility(View.VISIBLE);
            CheckBox cb_zt = (CheckBox) findViewById(R.id.cb_zt);
            cb_zt.setChecked(dynamiclayer.isVisible());
            cb_zt.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                    if (arg1) {

                        if (!dynamiclayer.isVisible()) {
                            dynamiclayer.setVisible(true);
                        }
                        mapView.invalidate();

                    } else {

                        if (dynamiclayer.isVisible()) {
                            dynamiclayer.setVisible(false);
                        }
                    }

                }
            });
        }

		if (DQLEVEL.equals("1")) {
			// 市级用户可以查看所有数据
			loginNOshi("");
		} else {
			// 非市级用户登陆 只能查看所在区县的数据
			String str = sharedPreferences.getString("REALNAME", "");
			str = str.substring(0, str.length() - 2);
			loginNOshi(str);
		}
	}

	List<File> groups = null;
	List<List<File>> childs = null;
	List<List<Map<String, Boolean>>> childCheckBox = new ArrayList<List<Map<String, Boolean>>>();
   /**
    *
    * 加载本地数据图层
    */
	public void loginNOshi(String qname) {
		ExpandableListView tc_exp = (ExpandableListView) findViewById(R.id.tc_expandlistview);
		tc_exp.setGroupIndicator(null);
		try {
			groups = manager.getGeodatabaseName();
			childs = manager.getChildeName(qname);
		} catch (java.lang.Exception e) {
			ToastUtil.makeText(mcontext,"初始化图层控制异常,请检查数据",Toast.LENGTH_SHORT);
			return;
		}
		childCheckBox.clear();
		if (groups.size() == 0||groups==null) {
			return;
		}
		if (childs.size() == 0) {
			return;
		}

		for (int i = 0; i < groups.size(); i++) {// 初始时,让所有的子选项均未被选中
			List<Map<String, Boolean>> childCB = new ArrayList<Map<String, Boolean>>();
			HashMap<String, Boolean> map = new HashMap<String, Boolean>();
			boolean check = true;
			if (childs.get(i).size() == 0 || childs.get(i) == null) {
				continue;
			}
			for (int a = 0; a < childs.get(i).size(); a++) {
				if (check && (nameList.size() > 0)) {
					for (int k = 0; k < nameList.size(); k++) {
						if (groups.get(i).getName()
								.contains(nameList.get(k).get("pname"))) {
							if (childs.get(i).get(a).getName()
									.contains(nameList.get(k).get("cname"))) {
								map.put(childs.get(i).get(a).getName(), true);
								check = false;
								break;
							} else {
								map.put(childs.get(i).get(a).getName(), false);
							}
						} else {
							map.put(childs.get(i).get(a).getName(), false);
						}
					}
				} else {
					map.put(childs.get(i).get(a).getName(), false);
				}
				childCB.add(map);

			}
			childCheckBox.add(childCB);
		}

		final ExpandableAdapter expandableAdapter = new ExpandableAdapter(
				MapActivity.this, groups, childs, childCheckBox);
		tc_exp.setAdapter(expandableAdapter);

		tc_exp.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView arg0, View v,
										final int groupPosition, long id) {
				ImageView img = (ImageView) v.findViewById(R.id.id_group_img);
				img.setBackgroundResource(R.drawable.offlineclose);
				img.setPadding(8, 8, 8, 8);
				img.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						runOnUiThread(new Runnable() {
							public void run() {
								for (int i = 0; i < nameList.size(); i++) {
									String gpName = groups.get(groupPosition)
											.getName();
									String pname = nameList.get(i).get("pname");
									if (pname.equals(gpName)) {
										mapView.setExtent(featureLayerList.get(
												i).getFullExtent());
										break;
									}
								}

								mapView.invalidate();
							}
						});
					}
				});

				Button renderButton = (Button) v
						.findViewById(R.id.layer_render);
				renderButton.setBackgroundResource(R.drawable.offlineclose);

				renderButton.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						runOnUiThread(new Runnable() {
							public void run() {
								showLayerRender(groupPosition, groups);
							}
						});
					}
				});

				return false;
			}
		});

		tc_exp.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView arg0, View v,
										final int groupPosition, final int childPosition, long id) {

				CheckBox cBox = (CheckBox) v.findViewById(R.id.cb_child);
				cBox.toggle();// 切换CheckBox状态！！！！！！！！！！
				for (int i = 0; i < childs.get(groupPosition).size(); i++) {
					if (i == childPosition) {
						String keyname = childs.get(groupPosition)
								.get(childPosition).getName();
						boolean ischeck = childCheckBox.get(groupPosition)
								.get(childPosition).get(keyname);
						if (ischeck) {
							childCheckBox.get(groupPosition).get(childPosition)
									.put(keyname, false);
							String childname = strToString(childs
									.get(groupPosition).get(childPosition)
									.getName());
							String cgName = groups.get(groupPosition).getName();
							for (int m = 0; m < nameList.size(); m++) {
								String pname = nameList.get(m).get("pname");
								String cname = nameList.get(m).get("cname");
								if (cgName.equals(pname)
										&& (cname.equals(childname))) {
									mapView.removeLayer(featureLayerList.get(m));
									featureLayerList.remove(m);
									nameList.remove(m);
									break;
								}
							}
							mapView.invalidate();
						} else {
							String cgName = groups.get(groupPosition).getName();
							for (int m = 0; m < nameList.size(); m++) {
								String pname = nameList.get(m).get("pname");
								if (cgName.equals(pname)) {
									mapView.removeLayer(featureLayerList.get(m));
									featureLayerList.remove(m);
									nameList.remove(m);
									break;
								}
							}
							childCheckBox.get(groupPosition).get(childPosition)
									.put(keyname, true);
							final String geodatabasePath = childs
									.get(groupPosition).get(childPosition)
									.getPath();
							qxname = strToString(keyname);
							runOnUiThread(new Runnable() {
								public void run() {
									String pName = groups.get(groupPosition)
											.getName();
									String cName = strToString(childs
											.get(groupPosition)
											.get(childPosition).getName());
									loadGeodatabase(geodatabasePath, pName,
											cName);
								}
							});
						}
					}
				}

				((BaseExpandableListAdapter) expandableAdapter)
						.notifyDataSetChanged();// 通知数据发生了变化
				return false;
			}
		});
	}

	// 转化 .otms 或者 .geodatabase
	public String strToString(String str) {
		if (str.endsWith(".otms")) {
			str = str.replace(".otms", "");
		}
		if (str.endsWith(".geodatabase")) {
			str = str.replace(".geodatabase", "");
		}
		return str;
	}

	/**
	 * 加载离线的geodatabase数据
	 *
	 * @param path
	 */
	public void loadGeodatabase(String path, String pName, String cName) {
		ToastUtil.setToast(MapActivity.this, "加载小班数据");
		try {
			// /storage/extSdCard/maps/otms/test/长坡岭林场.otms
			// path="/storage/extSdCard/maps/otms/林权/白云区.geodatabase";
			geodatabase = new Geodatabase(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// ToastUtil.setToast(MapActivity.this, "加载数据错误");
		} catch (RuntimeException e1) {
			decript(path);
			try {
				geodatabase = new Geodatabase(path);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		/*
		 * for (Layer l : mapView.getLayers()) { if (l instanceof
		 * ArcGISDynamicMapServiceLayer) { mapView.removeLayer(l); } }
		 */
		if (geodatabase != null) {
			List<GeodatabaseFeatureTable> list = geodatabase
					.getGeodatabaseTables();
			for (GeodatabaseFeatureTable gdbFeatureTable : list) {
				if (gdbFeatureTable.hasGeometry()) {
					final FeatureLayer featureLayer = new FeatureLayer(
							gdbFeatureTable);
					mapView.addLayer(featureLayer);
					featureLayerList.add(featureLayer);
					HashMap<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("pname", pName);
					hashMap.put("cname", cName);
					nameList.add(hashMap);
				}
			}

		} else {
			ToastUtil.setToast(MapActivity.this, "加载数据错误");
		}
	}

	/**
	 * 文件解密
	 *
	 * @param path
	 *            文件地址
	 */
	public void decript(String path) {
		try {
			byte[] buffer = File2byte(path);
			ArrayUtils.reverse(buffer);
			FileOutputStream outputStream = new FileOutputStream(new File(path));
			outputStream.write(buffer, 0, buffer.length); // /最后将调换后的文件流重新写回；
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件转为byte[]数组
	 *
	 * @param filePath
	 * @return
	 */
	public static byte[] File2byte(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	public void showLayerRender(final int groupPosition, final List<File> groups) {
		// 展示图层透明度和图层颜色设置的dialog
		final Dialog dialog = new Dialog(this, R.style.Dialog);
		dialog.setContentView(R.layout.color_selector);
		dialog.setCanceledOnTouchOutside(false);

		final TextView txtView = (TextView) dialog.findViewById(R.id.color_17);
		txtView.setBackgroundColor(sharedPreferences.getInt("color",
				Color.GREEN));
		ColorPickerView colorPickerView = (ColorPickerView) dialog
				.findViewById(R.id.color_picker);
		colorPickerView.setOnColorChangeListenrer(new ColorPickerView.OnColorChangedListener() {

			@Override
			public void colorChanged(int color) {
				sharedPreferences.edit().putInt("color", color).apply();

			}
		});

		SansumColorSelecter mColorSelecter = mColorSelecter = (SansumColorSelecter) dialog
				.findViewById(R.id.color_seleter);
		mColorSelecter
				.setColorSelecterLinstener(new SansumColorSelecter.ColorSelecterLinstener() {
					@Override
					public void onColorSeleter(int color) {
						sharedPreferences.edit().putInt("color", color)
								.apply();
						txtView.setBackgroundColor(color);
					}
				});
		final SeekBar seekBar = (SeekBar) dialog
				.findViewById(R.id.symbol_seekbar);
		seekBar.setProgress(100 - sharedPreferences.getInt("tmd", 0));

		final TextView textView = (TextView) dialog
				.findViewById(R.id.toumingdu);
		textView.setText((100 - sharedPreferences.getInt("tmd", 0)) + "");

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				sharedPreferences.edit()
						.putInt("tmd", 100 - arg0.getProgress()).apply();
				textView.setText(arg0.getProgress() + "");
			}
		});

		Button button = (Button) dialog.findViewById(R.id.symbo_reset);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				seekBar.setProgress(0);
				for (int i = 0; i < nameList.size(); i++) {
					if (groups.get(groupPosition).getName()
							.equals(nameList.get(i).get("pname"))) {
						featureLayerList.get(i).setRenderer(null);
						break;
					}
				}
				dialog.dismiss();
			}
		});

		RadioButton radioSure = (RadioButton) dialog
				.findViewById(R.id.layer_render_btn_sure);
		radioSure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(
						sharedPreferences.getInt("color", Color.GREEN));
				int txt = Integer.parseInt(textView.getText().toString());
				simpleFillSymbol.setAlpha(100 - txt);
				if (txt < 50) {
					simpleFillSymbol.setOutline(new SimpleLineSymbol(
							Color.BLACK, (float) 0.005));
				} else {
					simpleFillSymbol.setOutline(new SimpleLineSymbol(
							Color.GREEN, (float) 0.005));
				}
				SimpleRenderer renderer = new SimpleRenderer(simpleFillSymbol);
				for (int i = 0; i < nameList.size(); i++) {
					if (groups.get(groupPosition).getName()
							.equals(nameList.get(i).get("pname"))) {
						featureLayerList.get(i).setRenderer(renderer);
						break;
					}
				}
				dialog.dismiss();
			}
		});

		RadioButton radioCancle = (RadioButton) dialog
				.findViewById(R.id.layer_render_btn_cancle);
		radioCancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		dialog.show();
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		if (PadUtil.isPad(this)) {
			params.width = (int) (mScreenW * 0.5);
			params.height = (int) (mScreenH * 0.65);
		} else {
			params.width = (int) (mScreenW * 0.6);
			params.height = (int) (mScreenH * 0.8);
		}
		// params.height = LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.CENTER;
		dialog.getWindow().setAttributes(params);
	}

	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	/**
	 * 初始化百度导航
	 */
	private void initBaiduNavi() {
		// BaiduNaviManager.getInstance().setNativeLibraryPath(mSDCardPath +
		// "/BaiduNaviSDK_SO");
		String APP_FOLDER_NAME = context.getResources().getString(
				R.string.app_name);
		// String APP_FOLDER_NAME="test";
		String mSDCardPath = null;
		try {
			mSDCardPath = ResourcesManager.getInstance(context).getNaviPath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		baiduNavi = new BaiduNavi();
		baiduNavi.initNavi(MapActivity.this, mSDCardPath, APP_FOLDER_NAME);

	}

    /**
     * 百度导航回调
     */
    public class BaiduRoutePlanListener implements RoutePlanListener {

		private BNRoutePlanNode mBNRoutePlanNode = null;

		public BaiduRoutePlanListener(BNRoutePlanNode node) {
			mBNRoutePlanNode = node;
		}

		@Override
		public void onJumpToNavigator() {
			/*
			 * 设置途径点以及resetEndNode会回调该接口
			 */

			Intent intent = new Intent(MapActivity.this,
					BNGuideActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(ROUTE_PLAN_NODE,
					(BNRoutePlanNode) mBNRoutePlanNode);
			intent.putExtras(bundle);
			startActivity(intent);

		}

		@Override
		public void onRoutePlanFailed() {
			Toast.makeText(MapActivity.this, "路径计算失败", Toast.LENGTH_SHORT)
					.show();
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.destroyDrawingCache();
		if (mapView != null) {
			mapView = null;
		}
		if(mLocClient!=null){
			mLocClient.unRegisterLocationListener(myLocationListener);
			mLocClient.stop();
		}


	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mapView != null) {
			mapView.invalidate();
		}
		mapView.pause();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onStop() {
		super.onStop();
		mapView.unpause();
		centerPoint = mapView.getCenter();

		// mapView.restoreState();

	}

	@Override
	protected void onStart() {
		super.onStart();


		Intent intent = getIntent();
		if (intent.hasExtra("com.avos.avoscloud.Data")) {
			try {
				JSONObject json = new JSONObject(intent.getExtras().getString(
						"com.avos.avoscloud.Data"));
				if (json != null) {
					//log.error("ss"+json.toString());
					String message = json.getString("message");
					Gson gson = new Gson();
					Firepoint fp = gson.fromJson(message, Firepoint.class);
					double lon = Convert.convertToAngle(fp.getLON());
					double lat = Convert.convertToAngle(fp.getLAT());
					Point p = new Point(lon, lat);
					Point firepoint = (Point) GeometryEngine.project(p,
							SpatialReference.create(4326),
							SpatialReference.create(2343));
					Map<String, Object> attributes = new HashMap<>();
					attributes.put("经度", fp.getLON());
					attributes.put("纬度", fp.getLAT());
					attributes.put("地址", fp.getADDRESS());
					attributes.put("发现时间", fp.getTIME());
					Graphic g = new Graphic(firepoint, firepointSymbol,
							attributes);
					firepointlayer = new GraphicsLayer();
					firepointlayer.addGraphic(g);
					mapView.addLayer(firepointlayer);
					// mapView.setExtent(firepoint);
					mapView.centerAt(firepoint, false);

				}
			} catch (JSONException e) {
				e.printStackTrace();
				//log.error(e.toString());
			}
		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		mapView.centerAt(centerPoint, false);
	}

	double x, y;

	/* 显示坐标定位窗口 */
	public void showInputDialog() {
		final Dialog dialog = new Dialog(this, R.style.TransparentDialog);
		View view = LayoutInflater.from(this).inflate(R.layout.location_input,
				null);
		final RadioButton radio_reft = (RadioButton) view
				.findViewById(R.id.radio_btn_left);
		radio_reft.setChecked(true);
		final RadioButton radio_center = (RadioButton) view
				.findViewById(R.id.radio_btn_center);
		final RadioButton radio_right = (RadioButton) view
				.findViewById(R.id.radio_btn_right);
		final LinearLayout layout_type01 = (LinearLayout) view
				.findViewById(R.id.layout_type01);
		final LinearLayout layout_type02 = (LinearLayout) view
				.findViewById(R.id.layout_type02);
		final LinearLayout layout_type03 = (LinearLayout) view
				.findViewById(R.id.layout_type03);
		layout_type01.setVisibility(View.VISIBLE);
		layout_type02.setVisibility(View.GONE);
		layout_type03.setVisibility(View.GONE);
		radio_reft.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					radio_reft.setTextColor(getResources().getColor(
							R.color.white));
					radio_center.setTextColor(getResources().getColor(
							R.color.balck));
					radio_right.setTextColor(getResources().getColor(
							R.color.balck));
					layout_type01.setVisibility(View.VISIBLE);
					layout_type02.setVisibility(View.GONE);
					layout_type03.setVisibility(View.GONE);
				}

			}
		});
		radio_center.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					radio_reft.setTextColor(getResources().getColor(
							R.color.balck));
					radio_center.setTextColor(getResources().getColor(
							R.color.white));
					radio_right.setTextColor(getResources().getColor(
							R.color.balck));
					layout_type01.setVisibility(View.GONE);
					layout_type02.setVisibility(View.VISIBLE);
					layout_type03.setVisibility(View.GONE);
				}
			}
		});

		radio_right.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean arg1) {
				if (arg1) {
					radio_reft.setTextColor(getResources().getColor(
							R.color.balck));
					radio_center.setTextColor(getResources().getColor(
							R.color.balck));
					radio_right.setTextColor(getResources().getColor(
							R.color.white));
					layout_type01.setVisibility(View.GONE);
					layout_type02.setVisibility(View.GONE);
					layout_type03.setVisibility(View.VISIBLE);
				}
			}
		});
		// 缁忓害
		final EditText edit_jd = (EditText) view.findViewById(R.id.edit_jd);
		// 缁村害
		final EditText edit_wd = (EditText) view.findViewById(R.id.edit_wd);
		// 缁忓害瀵瑰簲鐨勫害
		final EditText edit_jd_d = (EditText) view.findViewById(R.id.edit_jd_d);
		// 缁忓害瀵瑰簲鐨勫垎
		final EditText edit_jd_f = (EditText) view.findViewById(R.id.edit_jd_f);
		// 缁忓害瀵瑰簲鐨勭
		final EditText edit_jd_m = (EditText) view.findViewById(R.id.edit_jd_m);
		// 缁村害瀵瑰簲鐨勫害
		final EditText edit_wd_d = (EditText) view.findViewById(R.id.edit_wd_d);
		// 缁村害瀵瑰簲鐨勫垎
		final EditText edit_wd_f = (EditText) view.findViewById(R.id.edit_wd_f);
		// 缁村害瀵瑰簲鐨勭
		final EditText edit_wd_m = (EditText) view.findViewById(R.id.edit_wd_m);
		// 米制x
		final EditText edit_x = (EditText) view.findViewById(R.id.edit_x);
		// 米制y
		final EditText edit_y = (EditText) view.findViewById(R.id.edit_y);
        //显示当前的经纬度
		TextView lon = (TextView) view.findViewById(R.id.location_lon);
		TextView lat = (TextView) view.findViewById(R.id.location_lat);
        if(upPoint!=null){
			Point point = (Point) GeometryEngine.project(upPoint,
					mapView.getSpatialReference(),
					SpatialReference.create(4326));


			lon.setText(lonlatdf.format(point.getX() ) );
			lat.setText(lonlatdf.format(point.getY() ) );
		}


		// 确定按钮
		Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);

		btn_confirm.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//
				Point point = null;
				if (radio_reft.isChecked()) {
					// 经纬度格式
					if (TextUtils.isEmpty(edit_jd.getText().toString())) {
						return;
					}
					if (TextUtils.isEmpty(edit_wd.getText().toString())) {
						return;
					}
					//
					x = Double.valueOf(edit_jd.getText().toString().trim());
					y = Double.valueOf(edit_wd.getText().toString().trim());

				} else if (radio_center.isChecked()) {
					// 度分秒格式
					if (TextUtils.isEmpty(edit_jd_d.getText().toString())) {
						return;
					}
					if (TextUtils.isEmpty(edit_jd_f.getText().toString())) {
						return;
					}
					if (TextUtils.isEmpty(edit_jd_m.getText().toString())) {
						return;
					}
					if (TextUtils.isEmpty(edit_wd_d.getText().toString())) {
						return;
					}
					if (TextUtils.isEmpty(edit_wd_f.getText().toString())) {
						return;
					}
					if (TextUtils.isEmpty(edit_wd_m.getText().toString())) {
						return;
					}
					//
					x = Integer.valueOf(edit_jd_d.getText().toString().trim())
							+ (double) Integer.valueOf(edit_jd_f.getText()
							.toString().trim())
							/ 60
							+ (double) Integer.valueOf(edit_jd_m.getText()
							.toString().trim()) / 3600;
					y = Integer.valueOf(edit_wd_d.getText().toString().trim())
							+ (double) Integer.valueOf(edit_wd_f.getText()
							.toString().trim())
							/ 60
							+ (double) Integer.valueOf(edit_wd_m.getText()
							.toString().trim()) / 3600;
				} else if (radio_right.isChecked()) {
					if (TextUtils.isEmpty(edit_x.getText().toString())) {
						return;
					}
					if (TextUtils.isEmpty(edit_y.getText().toString())) {
						return;
					}
					// 还有问题
					x = Double.parseDouble(edit_x.getText().toString());
					y = Double.parseDouble(edit_y.getText().toString());

				}
				// 判断经纬度是否在中国境内
				if (x > 135 || x < 74 || y > 54 || y < 3) {
					ToastUtil.setToast(MapActivity.this, "坐标范围不在中国内,请重新输入");
					return;
				}
				if (radio_reft.isChecked()) {
					point = SymbolUtil.getPoint(x, y);
				} else if (radio_center.isChecked()) {
					point = SymbolUtil.getPoint(x, y);
				} else if (radio_right.isChecked()) {
					point = new Point(x, y);
				}

				if (graphicsLayerLocation != null) {
					graphicsLayerLocation.removeAll();
					circleGraphic = null;
					locationGraphic = null;
				}

				Graphic graphic = new Graphic(point, new PictureMarkerSymbol(
						getResources().getDrawable(R.drawable.pic_point)));
				graphicsLayerLocation.addGraphic(graphic);
				// graphicLayer.addGraphic(graphic);
				mapView.setExtent(point, 0, true);
				dialog.dismiss();
			}
		});

		ImageView text_close = (ImageView) view.findViewById(R.id.text_close);
		text_close.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		dialog.setContentView(view);
		dialog.show();
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		if (PadUtil.isPad(MapActivity.this)) {
			params.width = (int) (mScreenW * 0.45);
		} else {
			params.width = (int) (mScreenW * 0.6);
		}
		params.height = LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.CENTER;
		dialog.getWindow().setAttributes(params);
	}

	/* 展示设备信息 */
	public void showMobileInfo() {
		String[] str = new String[5];
		if (MyApplication.IntetnetISVisible) {
			final String result = websUtil.selMobileInfo(MyApplication.SBH);
			if (result.equals("网络异常")) {
				ToastUtil.setToast(MapActivity.this, "网络异常,设备信息获取失败");
				return;
			} else {
				try {
					JSONObject obj = new JSONObject(result);
					JSONArray arr = obj.optJSONArray("ds");
					if (arr != null) {
						JSONObject object = arr.optJSONObject(0);
						str[0] = object.getString("SYZNAME");
						str[1] = object.getString("SYZPHONE");
						str[2] = object.getString("DZ");
						str[3] = object.getString("SBMC");
						str[4] = object.getString("DJTIME");
						showMobileInfo(str);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} else {
			ToastUtil.setToast(MapActivity.this, "未连接网络");
			return;
		}
	}

	//
	public void showMobileInfo(String[] str) {
		final Dialog dialog = new Dialog(MapActivity.this, R.style.Dialog);
		dialog.setContentView(R.layout.update_mobileinfo);
		dialog.setCanceledOnTouchOutside(false);
		final EditText name = (EditText) dialog
				.findViewById(R.id.mobile_name_text);
		final EditText tel = (EditText) dialog
				.findViewById(R.id.mobile_tel_text);
		final EditText address = (EditText) dialog
				.findViewById(R.id.mobile_dz_text);
		final EditText denjiTime = (EditText) dialog
				.findViewById(R.id.mobile_time_text);
		final EditText sb_name = (EditText) dialog
				.findViewById(R.id.sb_name_text);
		final TextView sbh = (TextView) dialog.findViewById(R.id.sbh_text);
		sbh.setText(MyApplication.SBH);
		if (!str[0].isEmpty()) {
			name.setText(str[0]);
		}
		if (!str[1].isEmpty()) {
			tel.setText(str[1]);
		}
		if (!str[2].isEmpty()) {
			address.setText(str[2]);
		}
		if (!str[4].isEmpty()) {
			denjiTime.setText(str[4]);
		}
		if (!str[3].isEmpty()) {
			sb_name.setText(str[3]);
		}

		Button bt_sure = (Button) dialog
				.findViewById(R.id.mobile_info_btn_sure);
		bt_sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(name.getText().toString())) {
					ToastUtil.setToast(MapActivity.this, "名称不能为空");
					return;
				}
				if (TextUtils.isEmpty(tel.getText().toString())) {
					ToastUtil.setToast(MapActivity.this, "手机号不能为空");
					return;
				}
				if (!MylibUtil.isHanzi(name.getText().toString(),
						MapActivity.this)) {
					return;
				}
				if (!MylibUtil.checkFomatNumber(tel.getText().toString())) {
					ToastUtil.setToast(MapActivity.this, "请输入11位有效手机号");
					return;
				}

				ProgressDialogUtil.startProgressDialog(MapActivity.this);
				String[] params = { "updateMobilInfo",
						name.getText().toString(), tel.getText().toString(),
						address.getText().toString(),
						sb_name.getText().toString() };
				new MyAsyncTask().execute(params);
				dialog.dismiss();
			}
		});

		ImageView close_view = (ImageView) dialog
				.findViewById(R.id.mobile_info_close);
		close_view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		if (PadUtil.isPad(MapActivity.this)) {
			params.width = (int) (mScreenW * 0.45);
		} else {
			params.width = (int) (mScreenW * 0.6);
		}
		params.height = LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.CENTER;
		dialog.getWindow().setAttributes(params);
	}

	//
	public void showMobileInfoRegister() {
		final Dialog dialog = new Dialog(MapActivity.this, R.style.Dialog);
		dialog.setContentView(R.layout.register_mobileuserinfo);
		dialog.setCanceledOnTouchOutside(false);
		final EditText name = (EditText) dialog
				.findViewById(R.id.mobile_name_text);
		final EditText tel = (EditText) dialog
				.findViewById(R.id.mobile_tel_text);
		final EditText address = (EditText) dialog
				.findViewById(R.id.mobile_dz_text);
		final EditText sb_name = (EditText) dialog
				.findViewById(R.id.sb_name_text);
		final TextView sbh = (TextView) dialog.findViewById(R.id.sbh_text);
		sbh.setText(MyApplication.SBH);
		sb_name.setText(MyApplication.MOBILE_MODEL);
		Button bt_sure = (Button) dialog
				.findViewById(R.id.mobile_info_btn_sure);
		bt_sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(name.getText().toString())) {
					ToastUtil.setToast(MapActivity.this, "名称不能为空");
					return;
				}
				if (TextUtils.isEmpty(tel.getText().toString())) {
					ToastUtil.setToast(MapActivity.this, "手机号不能为空");
					return;
				}
				if (!MylibUtil.isHanzi(name.getText().toString(),
						MapActivity.this)) {
					return;
				}
				if (!MylibUtil.checkFomatNumber(tel.getText().toString())) {
					ToastUtil.setToast(MapActivity.this, "请输入11位有效手机号");
					return;
				}

				ProgressDialogUtil.startProgressDialog(MapActivity.this);
				String[] params = { "registerMobilInfo",
						name.getText().toString(), tel.getText().toString(),
						address.getText().toString(),
						sb_name.getText().toString() };
				new MyAsyncTask().execute(params);
				dialog.dismiss();
			}
		});

		ImageView close_view = (ImageView) dialog
				.findViewById(R.id.mobile_info_close);
		close_view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		if (PadUtil.isPad(MapActivity.this)) {
			params.width = (int) (mScreenW * 0.45);
		} else {
			params.width = (int) (mScreenW * 0.6);
		}
		params.height = LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.CENTER;
		dialog.getWindow().setAttributes(params);
	}

	/* 系统设置窗口 */
	public void showSystemSettings() {

		final Dialog dialog = new Dialog(MapActivity.this,
				R.style.TransparentDialog);
		dialog.setContentView(R.layout.sys_settings);
		dialog.setCanceledOnTouchOutside(false);
		//系统版本号
		TextView tv_version= (TextView) dialog.findViewById(R.id.tv_appversion);
		tv_version.setText(UpdateUtil.getVersionCode()+"");
		CheckBox radio_btn_setting_guiji = (CheckBox) dialog
				.findViewById(R.id.radio_btn_setting_guiji);
		radio_btn_setting_guiji
				.setOnCheckedChangeListener(new myCheckedChangeListener());
		radio_btn_setting_guiji.setChecked(sharedPreferences.getBoolean(
				"guiji", false));

		CheckBox radio_btn_setting_gps = (CheckBox) dialog
				.findViewById(R.id.radio_btn_setting_gps);
		radio_btn_setting_gps
				.setOnCheckedChangeListener(new myCheckedChangeListener());
		radio_btn_setting_gps.setChecked(MylibUtil.isOPen(MapActivity.this));

		CheckBox radio_btn_setting_zongji = (CheckBox) dialog
				.findViewById(R.id.radio_btn_setting_zongji);
		radio_btn_setting_zongji
				.setOnCheckedChangeListener(new myCheckedChangeListener());
		radio_btn_setting_zongji.setChecked(sharedPreferences.getBoolean(
				"zongji", false));

		final EditText rg_time = (EditText) dialog
				.findViewById(R.id.time_input);
		final EditText rg_distance = (EditText) dialog
				.findViewById(R.id.distance_input);
		//redisip地址
		final EditText redisip = (EditText) dialog
				.findViewById(R.id.redis_input);
		//安顺地图服务地址
		final EditText anshunmap = (EditText) dialog
				.findViewById(R.id.anshunmap_input);
		//监控点ip
		final EditText monitorip = (EditText) dialog
				.findViewById(R.id.monitor_input);
		String dd=sharedPreferences.getInt("time", 1000)/1000 + "";
		rg_time.setText(sharedPreferences.getInt("time", 1000)/1000 + "");
		rg_distance.setText(sharedPreferences.getInt("distance",100) + "");
		redisip.setText(sharedPreferences.getString("redisip","192.168.0.109") );
		anshunmap.setText(sharedPreferences.getString("anshunmap","192.168.0.109") );
		monitorip.setText(sharedPreferences.getString("monitorip","192.168.0.109") );
		ImageView close_view = (ImageView) dialog
				.findViewById(R.id.settings_close);
		close_view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isintilocction=false;
				if(rg_time.getText().toString().equals("")||rg_distance.getText().toString().equals("")){
					ToastUtil.setToast(MapActivity.this, "请输入定位参数");
				}
				int  loc_time=Integer.parseInt(rg_time.getText().toString());
				int loc_dis=Integer.parseInt(rg_distance.getText().toString());
				if (loc_time!=sharedPreferences.getInt("time", 1000)) {
					sharedPreferences.edit().putInt("time", loc_time*1000).apply();
					isintilocction=true;
				}
				if (loc_dis!=sharedPreferences.getInt("distance", 100) ){
					sharedPreferences.edit().putInt("distance", loc_dis).apply();
					isintilocction=true;
				}
				/*if(isintilocction){
					intiLocation();// 定位初始化

				}*/
				sharedPreferences.edit().putString("redisip", redisip.getText().toString()).apply();
				sharedPreferences.edit().putString("anshunmap", anshunmap.getText().toString()).apply();
				sharedPreferences.edit().putString("monitorip", monitorip.getText().toString()).apply();
				dialog.dismiss();
			}
		});

		dialog.show();
		WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
		if (PadUtil.isPad(MapActivity.this)) {
			params.width = (int) (mScreenW * 0.45);
		} else {
			params.width = (int) (mScreenW * 0.6);
		}
		params.height = LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.CENTER;
		dialog.getWindow().setAttributes(params);

	}

	class myCheckedChangeListener implements OnCheckedChangeListener {

		@SuppressLint("SimpleDateFormat")
		@Override
		public void onCheckedChanged(CompoundButton compoundbutton, boolean flag) {
			switch (compoundbutton.getId()) {
				case R.id.radio_btn_setting_guiji:
					compoundbutton.setChecked(flag);
					sharedPreferences.edit().putBoolean("guiji", flag).apply();
					break;
				case R.id.radio_btn_setting_gps:
					compoundbutton.setChecked(flag);
					sharedPreferences.edit().putBoolean("gps", flag).apply();
					if (flag && !MylibUtil.isOPen(MapActivity.this)) {
						Util.toggleGPS(MapActivity.this);
					} else if (!flag && MylibUtil.isOPen(MapActivity.this)) {
						Util.toggleGPS(MapActivity.this);
					}
					break;
				case R.id.radio_btn_setting_zongji:
					compoundbutton.setChecked(flag);
					sharedPreferences.edit().putBoolean("zongji", flag).apply();
					if (!flag) {
						graphicsLayerLocation.removeGraphic(gjGraphicID);
					}
					break;
				default:
					break;
			}
		}
	}

	/** 显示轨迹查询界面 */
	public void showDialogGuijiSearch(final View search) {
		search.setVisibility(View.VISIBLE);
		ImageView close = (ImageView) search
				.findViewById(R.id.close_guijiclose);
		guiji_startTime = (TextView) search.findViewById(R.id.guiji_startTime);
		guiji_endTime = (TextView) search.findViewById(R.id.guiji_endTime);
		guijireplay = (Button) search.findViewById(R.id.btn_guijireplay);
		guiji_startTime.setOnClickListener(new MyListener());
		guiji_endTime.setOnClickListener(new MyListener());
		guijireplay.setOnClickListener(new MyListener());
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				search.setVisibility(View.GONE);
			}
		});
	}

	/* 初始化小地名查询 */
	public void initXDMsearch(View dmsearch) {
		searchButton = (ImageButton) dmsearch.findViewById(R.id.searchButton);
		final ImageView close = (ImageView) dmsearch
				.findViewById(R.id.close_btn_search);
		editsearchText = (EditText) dmsearch.findViewById(R.id.searchText);
		final ListView listView = (ListView) dmsearch
				.findViewById(R.id.listView_result);
		searchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (TextUtils.isEmpty(editsearchText.getText())) {
					ToastUtil.setToast(MapActivity.this, "请输入小地名");
					return;
				}
				searchHistoryData.clear();
				searchHistoryData = DataBaseHelper.serchPlace(editsearchText.getText().toString());
				if (searchHistoryData.size() > 0) {
					runOnUiThread(new Runnable() {
						public void run() {
							listView.setVisibility(View.VISIBLE);
							XbAdapter adapter = new XbAdapter(
									searchHistoryData, MapActivity.this);
							listView.setAdapter(adapter);

							final List<Point> list = new ArrayList<>();
							for (int i = 0; i < searchHistoryData.size(); i++) {
								double lon = Double
										.parseDouble(searchHistoryData.get(i)
												.get("x").toString());
								double lat = Double
										.parseDouble(searchHistoryData.get(i)
												.get("y").toString());
								Point point = new Point(lon, lat);
								PictureMarkerSymbol markerSymbol = new PictureMarkerSymbol(
										getResources().getDrawable(
												R.drawable.icon_gcoding));
								Graphic graphic = new Graphic(point,
										markerSymbol);
								graphicLayer.addGraphic(graphic);
								list.add(point);
							}

							listView.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> arg0,
														View arg1, int position, long arg3) {
									searchButton.setVisibility(View.GONE);
									close.setVisibility(View.VISIBLE);
									mapView.centerAt(list.get(position), true);
								}
							});
						}
					});
				} else {
					ToastUtil.setToast(MapActivity.this, "无此类地名");
				}
			}
		});

		close.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (listView.getVisibility() == View.VISIBLE) {
					listView.setVisibility(View.GONE);
					close.setVisibility(View.GONE);
					editsearchText.setText("");
					searchButton.setVisibility(View.VISIBLE);
					searchHistoryData.clear();
					graphicLayer.removeAll();
				}
			}
		});
	}

	/**
	 * 初始化权限
	 */
	private void intiPermisson() {
		// If an error is found, handle the failure to start.
		// Check permissions to see if failure may be due to lack of permissions.
		boolean permissionCheck1 = ContextCompat.checkSelfPermission(mcontext, reqPermissions[0]) ==
				PackageManager.PERMISSION_GRANTED;
		boolean permissionCheck2 = ContextCompat.checkSelfPermission(mcontext, reqPermissions[1]) ==
				PackageManager.PERMISSION_GRANTED;

		boolean permissionCheck3 = ContextCompat.checkSelfPermission(mContext, reqPermissions[2]) ==
				PackageManager.PERMISSION_GRANTED;
		boolean permissionCheck4 = ContextCompat.checkSelfPermission(mContext, reqPermissions[3]) ==
				PackageManager.PERMISSION_GRANTED;

		if (Build.VERSION.SDK_INT >= 23&&!(permissionCheck1 && permissionCheck2&& permissionCheck3 &&permissionCheck4)) {
			int requestCode=1;
			// If permissions are not already granted, request permission from the user.
			ActivityCompat.requestPermissions((Activity) mcontext, reqPermissions, requestCode);
		}/* else {
			// Report other unknown failure types to the user - for example, location services may not
			// be enabled on the device.
                    *//*String message = String.format("Error in DataSourceStatusChangedListener: %s", dataSourceStatusChangedEvent
                            .getSource().getLocationDataSource().getError().getMessage());*//*
			String message="获取定位信息异常，请检查GPS是否开启";
			Toast.makeText(mcontext, message, Toast.LENGTH_LONG).show();
			// Update UI to reflect that the location display did not actually start
			//mSpinner.setSelection(0, true);
		}*/

		// If an error is found, handle the failure to start.
		// Check permissions to see if failure may be due to lack of permissions.
		/*boolean permissionCheck1 = ContextCompat.checkSelfPermission(mContext, reqPermissions[0]) ==
				PackageManager.PERMISSION_GRANTED;
		boolean permissionCheck2 = ContextCompat.checkSelfPermission(mContext, reqPermissions[1]) ==
				PackageManager.PERMISSION_GRANTED;

		if (Build.VERSION.SDK_INT >= 23&&!(permissionCheck1 && permissionCheck2)) {
			// If permissions are not already granted, request permission from the user.
			int requestCode = 3;
			ActivityCompat.requestPermissions((Activity) mContext, reqPermissions, requestCode);
		}*/


	}




}