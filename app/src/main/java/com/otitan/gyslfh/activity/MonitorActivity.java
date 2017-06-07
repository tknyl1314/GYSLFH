package com.otitan.gyslfh.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_COMPRESSIONCFG_V30;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_PLAYBACK_INFO;
import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;
import com.hikvision.netsdk.NET_DVR_TIME;
import com.hikvision.netsdk.PTZCommand;
import com.hikvision.netsdk.PlaybackCallBack;
import com.hikvision.netsdk.PlaybackControlCommand;
import com.hikvision.netsdk.RealPlayCallBack;
import com.otitan.gyslfh.R;
import com.otitan.monitor.PlaySurfaceView;
import com.otitan.util.ResourcesManager;
import com.otitan.util.ToastUtil;

import org.MediaPlayer.PlayM4.Player;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

import redis.clients.jedis.Jedis;

//import com.test.demo.VoiceTalk;

public class MonitorActivity extends Activity implements Callback,OnTouchListener,
		OnClickListener
{


	String TD, LXJID,location,ip,username,password;
	Context mContext;
	private Button m_oLoginBtn = null;
	private Button m_oPreviewBtn = null;
	private Button m_oPlaybackBtn = null;
	private Button m_oParamCfgBtn = null;
	private Button m_oCaptureBtn = null;
	private Button m_oRecordBtn = null;
	private Button m_oTalkBtn = null;
	private Button m_oPTZBtn = null;
	private Button m_oOtherBtn = null;
	private SurfaceView m_osurfaceView = null;


	/*
	 * private EditText m_oIPAddr = null; private EditText m_oPort = null;
	 * private EditText m_oUser = null; private EditText m_oPsd = null;
	 */
	PopupWindow titlepopupWindow, buttompopupWindow, rightpopupwindow;

	Button btUp;
	Button btDown;
	Button btLeft;
	Button btRigth;
	Button zoomin,zoomout;
	LinearLayout monitor_ytkz;
	boolean isup = false;
	private String m_oIPAddr = null;
	private String m_oPort = null;
	private String m_Port = null;
	private String m_oUser = null;
	private String m_oPsd = null;

	private String nomor_port = null;
	private String nomor_td = null;


	private NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = null;

	private int m_iLogID = -1; // return by NET_DVR_Login_v30
	private int m_iPlayID = -1; // return by NET_DVR_RealPlay_V30
	private int m_iPlaybackID = -1; // return by NET_DVR_PlayBackByTime

	private int m_iPort = -1; // play port
	private int m_iStartChan = 0; // start channel no
	private int m_iChanNum = 0; // channel number
	private static PlaySurfaceView[] playView = new PlaySurfaceView[4];

	private final String TAG = "MonitorActivity";

	private boolean m_bTalkOn = false;
	private boolean m_bPTZL = false;
	private boolean m_bMultiPlay = false;

	private boolean m_bNeedDecode = true;
	/**临时测试rtsp */
	private MediaPlayer mediaPlayer;
	String redisip="",monitorip="",irip="";
	SharedPreferences sharedPreferences;
	private ScaleGestureDetector mScaleDetector;
/*	private RotateGestureDetector mRotateDetector;
	private MoveGestureDetector mMoveDetector;
	private ShoveGestureDetector mShoveDetector;*/

	private GestureDetector mGestureDetector;
	/**初始化日志 */
	// private Logger log = LoggerManager.getLoggerInstance();
	/**初始化手势变量*/
	private float mFocusX = 0.f;
	private float mFocusY = 0.f;
	Intent monitorintent=null;
	Handler myhanHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1:
					Toast.makeText(mContext, "报警成功", Toast.LENGTH_SHORT).show();
					break;
				case 0:
					Toast.makeText(mContext, "报警失败："+msg.obj.toString(),Toast.LENGTH_SHORT).show();
					break;
				case 3:
					Toast.makeText(mContext, "监控视频播放失败", Toast.LENGTH_SHORT).show();
					break;
				case 4:
					m_oPreviewBtn.setText("停止预览");
					//Toast.makeText(mContext, "监控视频播放失败", 0).show();
					break;
				case 5:
					m_oPreviewBtn.setText("预览");
					break;

				default:
					break;
			}
		}

	};

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mointor);
		mContext = this;
		monitorintent = getIntent();
		TD = monitorintent.getStringExtra("TD");
		ip= monitorintent.getStringExtra("ip");
		username= monitorintent.getStringExtra("username");
		password= monitorintent.getStringExtra("password");
		//TD="8";
		//董农防盗通道8
		LXJID = monitorintent.getStringExtra("LXJID");
		//监控点位置
		location= monitorintent.getStringExtra("Location");
		sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
		redisip=sharedPreferences.getString("redisip", "192.168.1.105");
		monitorip=sharedPreferences.getString("monitorip",  "192.168.1.64");
		irip=sharedPreferences.getString("anshunmap",  "192.168.1.64");
		m_oIPAddr = ip;// DRV外网ip
		//m_oIPAddr = "192.168.0.64";// DRV内网ip
		if (LXJID.equals("7"))
		{
			m_oPort = "8001";
			m_Port=m_oPort;
		} else
		{
			m_oPort = "8000";
			m_Port=m_oPort;
		}
		//m_oPort="8000";


		//m_oUser = "admin";
		m_oUser = username;
		//m_oPsd = "sfb12345";
		//m_oPsd = "admin12345";
		m_oPsd = password;

		if (!initeSdk())
		{
			this.finish();
			return;
		}

		if (!initeActivity())
		{
			this.finish();
			return;
		}
		//preview();//预览

		// Setup Gesture Detectors
		//mScaleDetector 	= new ScaleGestureDetector(getApplicationContext(), new ScaleListener());
		//mRotateDetector = new RotateGestureDetector(getApplicationContext(), new RotateListener());
		//mMoveDetector 	= new MoveGestureDetector(getApplicationContext(), new MoveListener());



		//mShoveDetector 	= new ShoveGestureDetector(getApplicationContext(), new ShoveListener());

		/*
		 * m_oIPAddr.setText("10.17.133.10"); m_oPort.setText("8000");
		 * m_oUser.setText("admin"); m_oPsd.setText("12345");
		 */
		/*m_oIPAddr.setText("222.85.147.92");
        m_oPort.setText("8000");
        m_oUser.setText("admin");
        m_oPsd.setText("sfb12345");*/

	}



	/**
	 * @fn initeSdk
	 * @author zhuzhenlei
	 * @brief SDK init
	 * @return true - success;false - fail
	 */
	private boolean initeSdk()
	{
		// init net sdk
		if (!HCNetSDK.getInstance().NET_DVR_Init())
		{
			Log.e(TAG, "HCNetSDK init is failed!");
			Toast.makeText(mContext, "HCNetSDK init is failed!", Toast.LENGTH_SHORT).show();
			return false;
		}
		HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, "/mnt/sdcard/sdklog/",
				true);
		return true;
	}

	// GUI init
	private boolean initeActivity()
	{
		mGestureDetector=new GestureDetector(mContext,new simpleGestureListener());
		//mMoveDetector 	= new MoveGestureDetector(getApplicationContext(), new MoveListener());
		findViews();
		m_osurfaceView.getHolder().addCallback(this);
		setListeners();
		login();// 登录设备

		//preview();// 开始预览
		// m_oLoginBtn.performClick();
		/*
		 * if(m_iLogID==0) { m_oPrevisewBtn.performClick(); }
		 */
		/*
		 * m_oLoginBtn.performClick(); m_oPreviewBtn.performClick();
		 */
		/*
		 * if(m_oLoginBtn.performClick()){ m_oPreviewBtn.performClick(); }
		 */
		return true;
	}

	private void preview()
	{

		try
		{
			// 隐藏输入法
			/*
			 * ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE
			 * )).
			 * hideSoftInputFromWindow(MonitorActivity.this.getCurrentFocus()
			 * .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			 */
			if (m_iLogID < 0)
			{
				//log.error("please login on device first");
				Log.e(TAG, "please login on device first");
				ToastUtil.setToast(MonitorActivity.this, "请先登录设备");
				return;
			}
			if (m_bNeedDecode)
			{
				if (m_iChanNum > 1)// preview more than a channel
				{
					if (m_iPlayID < 0)
					{
						startSinglePreview();
					} else
					{
						stopSinglePreview();
						m_oPreviewBtn.setText("预览");
					}
					/*
					 * if(!m_bMultiPlay) {
					 *
					 * startMultiPreview(); m_bMultiPlay = true;
					 * m_oPreviewBtn.setText("Stop"); } else {
					 * stopMultiPreview(); m_bMultiPlay = false;
					 * m_oPreviewBtn.setText("Preview"); }
					 */
				} else
				// preivew a channel
				{
					if (m_iPlayID < 0)
					{
						startSinglePreview();
					} else
					{
						stopSinglePreview();
						m_oPreviewBtn.setText("预览");
					}
				}
			} else
			{

			}
		} catch (Exception err)
		{
			Log.e(TAG, "error: " + err.toString());
		}

	}

	private void login()
	{
		try
		{
			if (m_iLogID < 0)
			{
				// login on the device
				m_iLogID = loginDevice();
				if (m_iLogID < 0)
				{
					Log.e(TAG, "This device logins failed!");
					ToastUtil.setToast(MonitorActivity.this, "登录失败");
					return;
				}
				// get instance of exception callback and set
				ExceptionCallBack oexceptionCbf = getExceptiongCbf();
				if (oexceptionCbf == null)
				{
					Log.e(TAG, "ExceptionCallBack object is failed!");
					//ToastUtil.setToast(MonitorActivity.this, "登录失败");
					return;
				}

				if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(
						oexceptionCbf))
				{
					Log.e(TAG, "NET_DVR_SetExceptionCallBack is failed!");
					return;
				}

				// m_oLoginBtn.setText("退出登陆");
				Log.i(TAG,
						"Login sucess ****************************1***************************");
				// m_oPreviewBtn.performClick();
			} else
			{
				// whether we have logout
				if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(m_iLogID))
				{
					Log.e(TAG, " NET_DVR_Logout is failed!");
					m_iLogID = -1;
					return;
				}
				// m_oLoginBtn.setText("登陆");
				//m_iLogID = -1;
			}
		} catch (Exception err)
		{
			Log.e(TAG, "error: " + err.toString());
			ToastUtil.setToast(MonitorActivity.this, "登录失败");
		}

	}

	// get controller instance
	private void findViews()
	{
		// m_oLoginBtn = (Button) findViewById(R.id.btn_Login);
		m_oPreviewBtn = (Button) findViewById(R.id.btn_Preview);
		/*
		 * m_oPlaybackBtn = (Button) findViewById(R.id.btn_Playback);
		 * m_oParamCfgBtn = (Button) findViewById(R.id.btn_ParamCfg);
		 * m_oCaptureBtn = (Button) findViewById(R.id.btn_Capture); m_oRecordBtn
		 * = (Button) findViewById(R.id.btn_Record); m_oTalkBtn = (Button)
		 * findViewById(R.id.btn_Talk); m_oPTZBtn = (Button)
		 * findViewById(R.id.btn_PTZ);// 云台控制 m_oOtherBtn = (Button)
		 * findViewById(R.id.btn_OTHER); m_osurfaceView = (SurfaceView)
		 * findViewById(R.id.Sur_Player);
		 */
		m_oCaptureBtn = (Button) findViewById(R.id.btn_capture);
		m_oPTZBtn = (Button) findViewById(R.id.btn_ptz);
		m_osurfaceView = (SurfaceView) findViewById(R.id.Sur_Player);
		monitor_ytkz = (LinearLayout) findViewById(R.id.monitor_ytkz);
		btUp = (Button) findViewById(R.id.bt_up);
		btDown = (Button) findViewById(R.id.bt_down);
		btLeft = (Button) findViewById(R.id.bt_left);
		btRigth = (Button) findViewById(R.id.bt_rigth);

		zoomin=(Button) findViewById(R.id.bt_zoomin);
		//zoomin.setOnClickListener(this);
		zoomout=(Button) findViewById(R.id.bt_zoomout);
		//zoomout.setOnClickListener(this);
		//一键报警
		Button call =(Button) findViewById(R.id.btn_call);
		call.setOnClickListener(this);

		Button monitorview=(Button) findViewById(R.id.btn_monitorview);
		monitorview.setOnClickListener(this);
		Button nomorview=(Button) findViewById(R.id.btn_nomorview);
		nomorview.setOnClickListener(this);

		Button irview=(Button) findViewById(R.id.btn_irview);
		irview.setOnClickListener(this);

		Button rtsp= (Button) findViewById(R.id.btn_rtsp);
		rtsp.setOnClickListener(this);
		/*Button setting=(Button) findViewById(R.id.btn_monitorsetting);
		setting.setOnClickListener(this);*/
		/*
		 * m_oIPAddr = (EditText) findViewById(R.id.EDT_IPAddr); m_oPort =
		 * (EditText) findViewById(R.id.EDT_Port); m_oUser = (EditText)
		 * findViewById(R.id.EDT_User); m_oPsd = (EditText)
		 * findViewById(R.id.EDT_Psd);
		 */
	}

	// listen
	private void setListeners()
	{
		m_osurfaceView.setOnTouchListener(this);
		//m_osurfaceView.setOnTouchListener(l);

		  /*m_osurfaceView.setOnDragListener(new OnDragListener() {

		  @Override public boolean onDrag(View arg0, DragEvent arg1) {
		  return false;
		  }
		  });*/

		/*
		 * m_osurfaceView.setOnFocusChangeListener(new OnFocusChangeListener() {
		 *
		 * @Override public void onFocusChange(View arg0, boolean arg1) {
		 * if(arg1){ //showtitlepup(); intipopupwindow(); showpopupwindow();
		 * }else{
		 *
		 * }
		 *
		 * } });
		 */
		// m_oLoginBtn.setOnClickListener(Login_Listener);
		m_oPreviewBtn.setOnClickListener(Preview_Listener);
		//m_oPreviewBtn.setOnClickListener(this);
		/*
		 * m_oPlaybackBtn.setOnClickListener(Playback_Listener);
		 * m_oParamCfgBtn.setOnClickListener(ParamCfg_Listener);
		 * m_oCaptureBtn.setOnClickListener(Capture_Listener);
		 * m_oRecordBtn.setOnClickListener(Record_Listener); //
		 * m_oTalkBtn.setOnClickListener(Talk_Listener);
		 * m_oOtherBtn.setOnClickListener(OtherFunc_Listener);
		 */
		m_oPTZBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				if (monitor_ytkz.getVisibility() == View.VISIBLE)
				{
					monitor_ytkz.setVisibility(View.GONE);
				} else
				{
					monitor_ytkz.setVisibility(View.VISIBLE);
				}

			}
		});
		m_oCaptureBtn.setOnClickListener(Capture_Listener);
		// m_oPTZBtn.setOnTouchListener(PTZ_Listener);

		btUp.setOnTouchListener(PTZ_Listener);
		btDown.setOnTouchListener(PTZ_Listener);
		btRigth.setOnTouchListener(PTZ_Listener);
		btLeft.setOnTouchListener(PTZ_Listener);
		zoomin.setOnTouchListener(PTZ_Listener);
		zoomout.setOnTouchListener(PTZ_Listener);
	}

	/* private GestureDetector.OnGestureListener onGestureListener =
		        new GestureDetector.SimpleOnGestureListener() {
		        @Override
		        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
		                float velocityY) {
		            float x = e2.getX() - e1.getX();
		            float y = e2.getY() - e1.getY();

		            if (x > 0) {
		               // doResult(RIGHT);
		            } else if (x < 0) {
		                //doResult(LEFT);
		            }
		            return true;
		        }
		    }; */

	protected void showpopupwindow()
	{
		titlepopupWindow.showAtLocation(m_osurfaceView, Gravity.TOP, 0, 0);
		buttompopupWindow.showAtLocation(m_osurfaceView, Gravity.BOTTOM, 0, 0);
		rightpopupwindow.showAtLocation(m_osurfaceView, Gravity.RIGHT, 0, 0);
	}

	protected void intipopupwindow()
	{
		View poplayout = LayoutInflater.from(mContext).inflate(
				R.layout.titlepopview, null);
		titlepopupWindow = new PopupWindow(poplayout,
				LayoutParams.MATCH_PARENT, poplayout.getHeight());
		titlepopupWindow.setBackgroundDrawable(getResources().getDrawable(
				android.R.color.transparent));
		titlepopupWindow.setFocusable(false);
		titlepopupWindow.setOutsideTouchable(true);

		View poplayout1 = LayoutInflater.from(mContext).inflate(
				R.layout.monitor_buttompopup, null);
		buttompopupWindow = new PopupWindow(poplayout1,
				LayoutParams.MATCH_PARENT, poplayout1.getHeight());
		buttompopupWindow.setBackgroundDrawable(getResources().getDrawable(
				android.R.color.transparent));
		buttompopupWindow.setFocusable(false);
		buttompopupWindow.setOutsideTouchable(true);

		View poplayout2 = LayoutInflater.from(mContext).inflate(
				R.layout.monitior_rightpopview, null);
		rightpopupwindow = new PopupWindow(poplayout2, 45,
				LayoutParams.WRAP_CONTENT);
		rightpopupwindow.setBackgroundDrawable(getResources().getDrawable(
				android.R.color.transparent));
		rightpopupwindow.setFocusable(false);
		rightpopupwindow.setOutsideTouchable(true);
	}



	private Button.OnTouchListener PTZ_Listener = new OnTouchListener()
	{

		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			try
			{
				if (m_iLogID < 0)
				{
					Log.e(TAG, "please login on a device first");
					Toast.makeText(MonitorActivity.this, "请先登录", Toast.LENGTH_SHORT);
					return false;
				}
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					switch (v.getId())
					{
						case R.id.bt_up:
							pt_goup();
							break;
						case R.id.bt_rigth:
							pt_goright();
							break;
						case R.id.bt_down:
							pt_godown();
							break;
						case R.id.bt_left:
							pt_goleft();
							break;

						case R.id.bt_zoomin:
							if (!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(
									m_iLogID, m_iStartChan, PTZCommand.ZOOM_IN, 0))
							{
								ToastUtil.setToast(MonitorActivity.this, "放大失败");
							} else
							{
								Log.i(TAG, "start ZOOM_IN");
							}
							break;
						case R.id.bt_zoomout:
							if (!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(
									m_iLogID, m_iStartChan, PTZCommand.ZOOM_OUT, 0))
							{
								ToastUtil.setToast(MonitorActivity.this, "缩小失败");
							} else
							{
								Log.i(TAG, "start ZOOM_OUT");
								//btLeft.setText("正在向左");
							}
							break;
						default:
							break;
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP)
				{
					switch (v.getId())
					{
						case R.id.bt_up:
							pt_stopup();
							break;
						case R.id.bt_rigth:
							pt_stopright();
							break;
						case R.id.bt_down:
							pt_stopdown();
							break;
						case R.id.bt_left:
							pt_stopleft();
							break;
						case R.id.bt_zoomin:
							if (!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(
									m_iLogID, m_iStartChan, PTZCommand.ZOOM_IN, 1))
							{
								ToastUtil.setToast(MonitorActivity.this, "放大失败");
							} else
							{
								Log.i(TAG, "start PAN_RIGHT succ");
								//btLeft.setText("正在向左");
							}

							break;
						case R.id.bt_zoomout:
							if (!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(
									m_iLogID, m_iStartChan, PTZCommand.ZOOM_OUT, 1))
							{
								ToastUtil.setToast(MonitorActivity.this, "缩小失败");
							} else
							{
								Log.i(TAG, "start PAN_RIGHT succ");
								//btLeft.setText("正在向左");
							}
							break;
						default:
							break;
					}
				}
				return true;
			} catch (Exception err)
			{
				Log.e(TAG, "error: " + err.toString());
				return false;
			}
		}

	};
	// ptz listener 云台控制
	/*
	 * private Button.OnTouchListener PTZ_Listener = new OnTouchListener() {
	 *
	 * @Override public boolean onTouch(View v, MotionEvent event) { try {
	 * if(m_iLogID < 0) { Log.e(TAG,"please login on a device first"); return
	 * false; } if(event.getAction()== MotionEvent.ACTION_DOWN) {
	 * if(!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID,
	 * m_iStartChan, PTZCommand.PAN_LEFT, 0)) { Log.e(TAG,
	 * "start PAN_LEFT failed with error code: " +
	 * HCNetSDK.getInstance().NET_DVR_GetLastError()); } else { Log.i(TAG,
	 * "start PAN_LEFT succ"); } if(m_bPTZL == false) {
	 * if(!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID,
	 * m_iStartChan, PTZCommand.PAN_LEFT, 0)) { Log.e(TAG,
	 * "start PAN_LEFT failed with error code: " +
	 * HCNetSDK.getInstance().NET_DVR_GetLastError()); } else { Log.i(TAG,
	 * "start PAN_LEFT succ"); } } else {
	 * if(!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID,
	 * m_iStartChan, PTZCommand.PAN_RIGHT, 1)) { Log.e(TAG,
	 * "start PAN_RIGHT failed with error code: " +
	 * HCNetSDK.getInstance().NET_DVR_GetLastError()); } else { Log.i(TAG,
	 * "start PAN_RIGHT succ"); } } } else if(event.getAction() ==
	 * MotionEvent.ACTION_UP) {
	 * if(!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID,
	 * m_iStartChan, PTZCommand.PAN_LEFT, 0)) { Log.e(TAG,
	 * "stop PAN_LEFT failed with error code: " +
	 * HCNetSDK.getInstance().NET_DVR_GetLastError()); } else { Log.i(TAG,
	 * "stop PAN_LEFT succ"); } if(m_bPTZL == false) {
	 *
	 * if(!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID,
	 * m_iStartChan, PTZCommand.PAN_LEFT, 0)) { Log.e(TAG,
	 * "stop PAN_LEFT failed with error code: " +
	 * HCNetSDK.getInstance().NET_DVR_GetLastError()); } else { Log.i(TAG,
	 * "stop PAN_LEFT succ"); } m_bPTZL = true; m_oPTZBtn.setText("PTZ(R)"); }
	 * else { if(!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID,
	 * m_iStartChan, PTZCommand.PAN_RIGHT, 1)) { Log.e(TAG,
	 * "stop PAN_RIGHT failed with error code: " +
	 * HCNetSDK.getInstance().NET_DVR_GetLastError()); } else { Log.i(TAG,
	 * "stop PAN_RIGHT succ"); } m_bPTZL = false; m_oPTZBtn.setText("PTZ(L)"); }
	 * } return true; } catch (Exception err) { Log.e(TAG, "error: " +
	 * err.toString()); return false; } } };
	 */
	// preset listener
	private Button.OnClickListener OtherFunc_Listener = new OnClickListener()
	{
		public void onClick(View v)
		{
			// PTZTest.TEST_PTZ(m_iPlayID, m_iLogID, m_iStartChan);
			// ConfigTest.TEST_Config(m_iPlayID, m_iLogID, m_iStartChan);;
			// ManageTest.TEST_Manage(m_iLogID);
			// AlarmTest.Test_SetupAlarm(m_iLogID);
			// OtherFunction.TEST_OtherFunc(m_iPlayID, m_iLogID, m_iStartChan);
			// OtherFunction.Test_RecycleGetStream(m_iLogID, m_iStartChan);
		}
	};

	// Talk listener
	/*
	 * private Button.OnClickListener Talk_Listener = new
	 * Button.OnClickListener() { public void onClick(View v) { try {
	 * if(m_bTalkOn == false) { if(VoiceTalk.startVoiceTalk(m_iLogID) >= 0) {
	 * m_bTalkOn = true; m_oTalkBtn.setText("Stop"); } } else {
	 * if(VoiceTalk.stopVoiceTalk()) { m_bTalkOn = false;
	 * m_oTalkBtn.setText("Talk"); } } } catch (Exception err) { Log.e(TAG,
	 * "error: " + err.toString()); } } };
	 */
	// record listener
	private Button.OnClickListener Record_Listener = new Button.OnClickListener()
	{
		public void onClick(View v)
		{
			try
			{

			} catch (Exception err)
			{
				Log.e(TAG, "error: " + err.toString());
			}
		}
	};
	// capture listener
	private Button.OnClickListener Capture_Listener = new Button.OnClickListener()
	{
		public void onClick(View v)
		{
			try
			{
				if (m_iPort < 0)
				{
					Log.e(TAG, "please start preview first");
					return;
				}
				Player.MPInteger stWidth = new Player.MPInteger();
				Player.MPInteger stHeight = new Player.MPInteger();
				if (!Player.getInstance().getPictureSize(m_iPort, stWidth,
						stHeight))
				{
					Log.e(TAG, "getPictureSize failed with error code:"
							+ Player.getInstance().getLastError(m_iPort));
					return;
				}
				int nSize = 5 * stWidth.value * stHeight.value;
				byte[] picBuf = new byte[nSize];
				Player.MPInteger stSize = new Player.MPInteger();
				if (!Player.getInstance()
						.getBMP(m_iPort, picBuf, nSize, stSize))
				{
					Log.e(TAG, "getBMP failed with error code:"
							+ Player.getInstance().getLastError(m_iPort));
					return;
				}

				SimpleDateFormat sDateFormat = new SimpleDateFormat(
						"yyyy-MM-dd-hh:mm:ss");
				String date = sDateFormat.format(new java.util.Date());
				/*
				 * FileOutputStream file = new FileOutputStream("/mnt/sdcard/" +
				 * date + ".bmp");
				 */
				String path = ResourcesManager
						.getInstance(MonitorActivity.this).getPath()[0]
						+ "/maps/picture/";
				FileOutputStream file = new FileOutputStream(path + date
						+ ".bmp");
				file.write(picBuf, 0, stSize.value);
				file.close();
				ToastUtil.setToast(MonitorActivity.this, "截图已保存");
			} catch (Exception err)
			{
				ToastUtil.setToast(MonitorActivity.this, "截图保存失败");
				Log.e(TAG, "error: " + err.toString());
			}
		}
	};
	// playback listener
	private Button.OnClickListener Playback_Listener = new Button.OnClickListener()
	{

		public void onClick(View v)
		{
			try
			{
				if (m_iLogID < 0)
				{
					Log.e(TAG, "please login on a device first");
					return;
				}
				if (m_iPlaybackID < 0)
				{
					if (m_iPlayID >= 0)
					{
						Log.i(TAG, "Please stop preview first");
						return;
					}
					PlaybackCallBack fPlaybackCallBack = getPlayerbackPlayerCbf();
					if (fPlaybackCallBack == null)
					{
						Log.e(TAG, "fPlaybackCallBack object is failed!");
						return;
					}
					NET_DVR_TIME struBegin = new NET_DVR_TIME();
					NET_DVR_TIME struEnd = new NET_DVR_TIME();

					struBegin.dwYear = 2015;
					struBegin.dwMonth = 12;
					struBegin.dwDay = 23;

					struEnd.dwYear = 2015;
					struEnd.dwMonth = 12;
					struEnd.dwDay = 24;

					m_iPlaybackID = HCNetSDK.getInstance()
							.NET_DVR_PlayBackByTime(m_iLogID, 1, struBegin,
									struEnd);
					if (m_iPlaybackID >= 0)
					{
						if (!HCNetSDK.getInstance()
								.NET_DVR_SetPlayDataCallBack(m_iPlaybackID,
										fPlaybackCallBack))
						{
							Log.e(TAG, "Set playback callback failed!");
							return;
						}
						NET_DVR_PLAYBACK_INFO struPlaybackInfo = null;
						if (!HCNetSDK
								.getInstance()
								.NET_DVR_PlayBackControl_V40(
										m_iPlaybackID,
										PlaybackControlCommand.NET_DVR_PLAYSTART,
										null, 0, struPlaybackInfo))
						{
							Log.e(TAG, "net sdk playback start failed!");
							return;
						}
						m_oPlaybackBtn.setText("Stop");
						int nProgress = -1;
						/*
						 * while(true) { nProgress =
						 * HCNetSDK.getInstance().NET_DVR_GetPlayBackPos
						 * (m_iPlaybackID);
						 * System.out.println("NET_DVR_GetPlayBackPos:" +
						 * nProgress); if(nProgress < 0 || nProgress >= 100) {
						 * break; } try { Thread.sleep(1000); } catch
						 * (InterruptedException e) { // TODO Auto-generated
						 * catch block e.printStackTrace(); }
						 *
						 * }
						 */

					} else
					{
						Log.i(TAG,
								"NET_DVR_PlayBackByTime failed, error code: "
										+ HCNetSDK.getInstance()
										.NET_DVR_GetLastError());
					}
				} else
				{
					if (!HCNetSDK.getInstance().NET_DVR_StopPlayBack(
							m_iPlaybackID))
					{
						Log.e(TAG, "net sdk stop playback failed");
					}
					// player stop play
					stopSinglePlayer();
					m_oPlaybackBtn.setText("Playback");
					m_iPlaybackID = -1;
				}
			} catch (Exception err)
			{
				Log.e(TAG, "error: " + err.toString());
			}
		}
	};

	// login listener
	private Button.OnClickListener Login_Listener = new Button.OnClickListener()
	{
		public void onClick(View v)
		{
			try
			{
				if (m_iLogID < 0)
				{
					// login on the device
					m_iLogID = loginDevice();
					if (m_iLogID < 0)
					{
						Log.e(TAG, "This device logins failed!");
						ToastUtil.setToast(MonitorActivity.this, "登录失败");
						return;
					}
					// get instance of exception callback and set
					ExceptionCallBack oexceptionCbf = getExceptiongCbf();
					if (oexceptionCbf == null)
					{
						Log.e(TAG, "ExceptionCallBack object is failed!");
						return;
					}

					if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(
							oexceptionCbf))
					{
						Log.e(TAG, "NET_DVR_SetExceptionCallBack is failed!");
						return;
					}

					m_oLoginBtn.setText("退出登陆");
					Log.i(TAG,
							"Login sucess ****************************1***************************");
				} else
				{
					// whether we have logout
					if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(m_iLogID))
					{
						Log.e(TAG, " NET_DVR_Logout is failed!");

						return;
					}
					m_oLoginBtn.setText("登陆");
					m_iLogID = -1;
				}
			} catch (Exception err)
			{
				Log.e(TAG, "error: " + err.toString());
				ToastUtil.setToast(MonitorActivity.this, "登录失败");
			}
		}
	};
	// 预览 Preview listener
	private Button.OnClickListener Preview_Listener = new Button.OnClickListener()
	{
		public void onClick(View v)
		{
			try
			{



				if (m_iLogID < 0)
				{
					Log.e(TAG, "please login on device first");
					return;
				}
				if (m_bNeedDecode)
				{
					if (m_iChanNum > 1)// preview more than a channel
					{
						if (m_iPlayID < 0)
						{
							startSinglePreview();
						} else
						{
							stopSinglePreview();
							m_oPreviewBtn.setText("预览");
						}
						/*
						 * if(!m_bMultiPlay) {
						 *
						 * startMultiPreview(); m_bMultiPlay = true;
						 * m_oPreviewBtn.setText("Stop"); } else {
						 * stopMultiPreview(); m_bMultiPlay = false;
						 * m_oPreviewBtn.setText("Preview"); }
						 */
					} else
					// preivew a channel
					{
						if (m_iPlayID < 0)
						{
							startSinglePreview();
						} else
						{
							stopSinglePreview();
							m_oPreviewBtn.setText("预览");
						}
					}
				} else
				{

				}
			} catch (Exception err)
			{
				Log.e(TAG, "error: " + err.toString());
			}
		}
	};
	// configuration listener
	private Button.OnClickListener ParamCfg_Listener = new Button.OnClickListener()
	{
		public void onClick(View v)
		{
			try
			{
				paramCfg(m_iLogID);
			} catch (Exception err)
			{
				Log.e(TAG, "error: " + err.toString());
			}
		}
	};

	private void startSinglePreview()
	{
		if (m_iPlaybackID >= 0)
		{
			Log.i(TAG, "Please stop palyback first");
			return;
		}
		RealPlayCallBack fRealDataCallBack = getRealPlayerCbf();
		if (fRealDataCallBack == null)
		{
			Log.e(TAG, "fRealDataCallBack object is failed!");
			return;
		}
		Log.i(TAG, "m_iStartChan:" + m_iStartChan);

		NET_DVR_PREVIEWINFO previewInfo = new NET_DVR_PREVIEWINFO();
		previewInfo.lChannel = m_iStartChan;//通道号，目前设备模拟通道号从1开始，数字通道的起始通道号通过NET_DVR_GetDVRConfig（配置命令NET_DVR_GET_IPPARACFG_V40）获取（dwStartDChan）。
		if(Integer.parseInt(TD)>0){
			previewInfo.lChannel = Integer.parseInt(TD) + 32;
			m_iStartChan = Integer.parseInt(TD) + 32;
			nomor_port=m_oPort;
			nomor_td=TD;
		}


		previewInfo.dwStreamType = 1; // 码流类型：0-主码流，1-子码流，2-码流3，3-虚拟码流，以此类推
		previewInfo.bBlocked = 0;//0- 非阻塞取流，1- 阻塞取流。如果阻塞取流，SDK内部connect失败将会有5s的超时才能够返回，不适合于轮询取流操作。
		// HCNetSDK start preview
		m_iPlayID = HCNetSDK.getInstance().NET_DVR_RealPlay_V40(m_iLogID,
				previewInfo, fRealDataCallBack);
		if (m_iPlayID < 0)
		{
			Log.e(TAG, "NET_DVR_RealPlay is failed!Err:"
					+ HCNetSDK.getInstance().NET_DVR_GetLastError());
			/*ToastUtil.setToast(MonitorActivity.this,  "NET_DVR_RealPlay is failed!Err:"
					+ HCNetSDK.getInstance().NET_DVR_GetLastError());*/
			Toast.makeText(mContext, "NET_DVR_RealPlay is failed!Err:"
					+ HCNetSDK.getInstance().NET_DVR_GetLastError(), Toast.LENGTH_LONG).show();
			return;
		}

		Log.i(TAG,
				"NetSdk Play sucess ***********************3***************************");
		m_oPreviewBtn.setText("停止预览");
	}

	private void startMultiPreview()
	{
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int i = 0;
		for (i = 0; i < 4; i++)
		{
			if (playView[i] == null)
			{
				playView[i] = new PlaySurfaceView(this);
				playView[i].setParam(metric.widthPixels);
				FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
						FrameLayout.LayoutParams.WRAP_CONTENT,
						FrameLayout.LayoutParams.WRAP_CONTENT);
				params.bottomMargin = playView[i].getCurHeight() - (i / 2)
						* playView[i].getCurHeight();
				params.leftMargin = (i % 2) * playView[i].getCurWidth();
				params.gravity = Gravity.BOTTOM | Gravity.LEFT;
				addContentView(playView[i], params);
			}
			playView[i].startPreview(m_iLogID, m_iStartChan + i);
		}
		m_iPlayID = playView[0].m_iPreviewHandle;
	}

	private void stopMultiPreview()
	{
		int i = 0;
		for (i = 0; i < 4; i++)
		{
			playView[i].stopPreview();
		}
		m_iPlayID = -1;
	}

	/**
	 * @fn stopSinglePreview
	 * @author zhuzhenlei
	 * @brief stop preview
	 *            [in]
	 *            [out]
	 * @return NULL
	 */
	private void stopSinglePreview()
	{
		if (m_iPlayID < 0)
		{
			Log.e(TAG, "m_iPlayID < 0");
			return;
		}

		// net sdk stop preview
		if (!HCNetSDK.getInstance().NET_DVR_StopRealPlay(m_iPlayID))
		{
			Log.e(TAG, "StopRealPlay is failed!Err:"
					+ HCNetSDK.getInstance().NET_DVR_GetLastError());
			return;
		}

		m_iPlayID = -1;
		stopSinglePlayer();
	}

	private void stopSinglePlayer()
	{
		Player.getInstance().stopSound();
		// player stop play
		if (!Player.getInstance().stop(m_iPort))
		{
			Log.e(TAG, "stop is failed!");
			return;
		}

		if (!Player.getInstance().closeStream(m_iPort))
		{
			Log.e(TAG, "closeStream is failed!");
			return;
		}
		if (!Player.getInstance().freePort(m_iPort))
		{
			Log.e(TAG, "freePort is failed!" + m_iPort);
			return;
		}
		m_iPort = -1;
	}

	/**
	 * @fn loginDevice
	 * @author zhuzhenlei
	 * @brief login on device
	 *            [in]
	 *            [out]
	 * @return login ID
	 */
	private int loginDevice()
	{
		// get instance
		m_oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
		if (null == m_oNetDvrDeviceInfoV30)
		{
			Log.e(TAG, "HKNetDvrDeviceInfoV30 new is failed!");
			return -1;
		}
		/*
		 * String strIP = m_oIPAddr.getText().toString(); int nPort =
		 * Integer.parseInt(m_oPort.getText().toString()); String strUser =
		 * m_oUser.getText().toString(); String strPsd =
		 * m_oPsd.getText().toString();
		 */
		//ip
		String strIP = m_oIPAddr;
		//端口
		int nPort = Integer.parseInt(m_oPort);
		//用户名
		String strUser = m_oUser;
		//密码
		String strPsd = m_oPsd;
		// call NET_DVR_Login_v30 to login on, port 8000 as default
		int iLogID = HCNetSDK.getInstance().NET_DVR_Login_V30(strIP, nPort,
				strUser, strPsd, m_oNetDvrDeviceInfoV30);
		if (iLogID < 0)
		{
			Log.e(TAG, "NET_DVR_Login is failed!Err:"
					+ HCNetSDK.getInstance().NET_DVR_GetLastError());
			Toast.makeText(mContext, "设备登录失败:Err:"
					+ HCNetSDK.getInstance().NET_DVR_GetLastError(), Toast.LENGTH_SHORT).show();
			return -1;
		}
		if (m_oNetDvrDeviceInfoV30.byChanNum > 0)
		{
			m_iStartChan = m_oNetDvrDeviceInfoV30.byStartChan;
			m_iChanNum = m_oNetDvrDeviceInfoV30.byChanNum;
		} else if (m_oNetDvrDeviceInfoV30.byIPChanNum > 0)
		{
			m_iStartChan = m_oNetDvrDeviceInfoV30.byStartDChan;
			m_iChanNum = m_oNetDvrDeviceInfoV30.byIPChanNum
					+ m_oNetDvrDeviceInfoV30.byHighDChanNum * 256;
		}
		Log.i(TAG, "NET_DVR_Login is Successful!");
		Toast.makeText(mContext, "设备登录成功", Toast.LENGTH_SHORT).show();
		return iLogID;
	}

	/**
	 * @fn paramCfg
	 * @author zhuzhenlei
	 * @brief configuration
	 * @param iUserID
	 *            - login ID [in]
	 *            [out]
	 * @return NULL
	 */
	private void paramCfg(final int iUserID)
	{
		// whether have logined on
		if (iUserID < 0)
		{
			Log.e(TAG, "iUserID < 0");
			return;
		}

		NET_DVR_COMPRESSIONCFG_V30 struCompress = new NET_DVR_COMPRESSIONCFG_V30();
		if (!HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,
				HCNetSDK.NET_DVR_GET_COMPRESSCFG_V30, m_iStartChan,
				struCompress))
		{
			Log.e(TAG, "NET_DVR_GET_COMPRESSCFG_V30 failed with error code:"
					+ HCNetSDK.getInstance().NET_DVR_GetLastError());
		} else
		{
			Log.i(TAG, "NET_DVR_GET_COMPRESSCFG_V30 succ");
		}
		// set substream resolution to cif
		struCompress.struNetPara.byResolution = 1;
		if (!HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID,
				HCNetSDK.NET_DVR_SET_COMPRESSCFG_V30, m_iStartChan,
				struCompress))
		{
			Log.e(TAG, "NET_DVR_SET_COMPRESSCFG_V30 failed with error code:"
					+ HCNetSDK.getInstance().NET_DVR_GetLastError());
		} else
		{
			Log.i(TAG, "NET_DVR_SET_COMPRESSCFG_V30 succ");
		}
	}

	/**
	 * @fn getExceptiongCbf
	 * @author zhuzhenlei
	 * @brief process exception
	 *            [in]
	 *            [out]
	 * @return exception instance
	 */
	private ExceptionCallBack getExceptiongCbf()
	{
		ExceptionCallBack oExceptionCbf = new ExceptionCallBack()
		{
			public void fExceptionCallBack(int iType, int iUserID, int iHandle)
			{
				System.out.println("recv exception, type:" + iType);
			}
		};
		return oExceptionCbf;
	}

	/**
	 * @fn getRealPlayerCbf
	 * @author zhuzhenlei
	 * @brief get realplay callback instance
	 *            [in]
	 *            [out]
	 * @return callback instance
	 */
	private RealPlayCallBack getRealPlayerCbf()
	{
		RealPlayCallBack cbf = new RealPlayCallBack()
		{
			public void fRealDataCallBack(int iRealHandle, int iDataType,
										  byte[] pDataBuffer, int iDataSize)
			{
				// player channel 1
				// MonitorActivity.this.processRealData(1, iDataType,
				// pDataBuffer, iDataSize, Player.STREAM_REALTIME);
				MonitorActivity.this.processRealData(1, iDataType, pDataBuffer,
						iDataSize, Player.STREAM_REALTIME);
			}
		};
		return cbf;
	}

	/**
	 * @fn getPlayerbackPlayerCbf
	 * @author zhuzhenlei
	 * @brief get Playback instance
	 *            [in]
	 *            [out]
	 * @return callback instance
	 */
	private PlaybackCallBack getPlayerbackPlayerCbf()
	{
		PlaybackCallBack cbf = new PlaybackCallBack()
		{
			@Override
			public void fPlayDataCallBack(int iPlaybackHandle, int iDataType,
										  byte[] pDataBuffer, int iDataSize)
			{
				// player channel 1
				MonitorActivity.this.processRealData(1, iDataType, pDataBuffer,
						iDataSize, Player.STREAM_FILE);
			}
		};
		return cbf;
	}

	/**
	 * @fn processRealData
	 * @author zhuzhenlei
	 * @brief process real data
	 * @param iPlayViewNo
	 *            - player channel [in]
	 * @param iDataType
	 *            - data type [in]
	 * @param pDataBuffer
	 *            - data buffer [in]
	 * @param iDataSize
	 *            - data size [in]
	 * @param iStreamMode
	 *            - stream mode [in]
	 *            [out]
	 * @return NULL
	 */
	public void processRealData(int iPlayViewNo, int iDataType,
								byte[] pDataBuffer, int iDataSize, int iStreamMode)
	{
		if (!m_bNeedDecode)
		{
			// Log.i(TAG, "iPlayViewNo:" + iPlayViewNo + ",iDataType:" +
			// iDataType + ",iDataSize:" + iDataSize);
		} else
		{
			if (HCNetSDK.NET_DVR_SYSHEAD == iDataType)
			{
				if (m_iPort >= 0)
				{
					return;
				}
				m_iPort = Player.getInstance().getPort();
				if (m_iPort == -1)
				{
					Log.e(TAG, "getPort is failed with: "
							+ Player.getInstance().getLastError(m_iPort));
					return;
				}
				Log.i(TAG, "getPort succ with: " + m_iPort);
				if (iDataSize > 0)
				{
					if (!Player.getInstance().setStreamOpenMode(m_iPort,
							iStreamMode)) // set stream mode
					{
						Log.e(TAG, "setStreamOpenMode failed");
						return;
					}
					if (!Player.getInstance().openStream(m_iPort, pDataBuffer,
							iDataSize, 2 * 1024 * 1024)) // open stream
					{
						Log.e(TAG, "openStream failed");
						return;
					}
					if (!Player.getInstance().play(m_iPort,
							m_osurfaceView.getHolder()))
					{
						Log.e(TAG, "play failed");
						return;
					}
					// play Audio
					/*
					 * if(!Player.getInstance().playSound(m_iPort)) { Log.e(TAG,
					 * "playSound failed with error code:" +
					 * Player.getInstance().getLastError(m_iPort)); return; }
					 */
				}
			} else
			{
				if (!Player.getInstance().inputData(m_iPort, pDataBuffer,
						iDataSize))
				{
					// Log.e(TAG, "inputData failed with: " +
					// Player.getInstance().getLastError(m_iPort));
					for (int i = 0; i < 4000 && m_iPlaybackID >= 0; i++)
					{
						if (!Player.getInstance().inputData(m_iPort,
								pDataBuffer, iDataSize))
							Log.e(TAG, "inputData failed with: "
									+ Player.getInstance()
									.getLastError(m_iPort));
						else
							break;
						try
						{
							Thread.sleep(10);
						} catch (InterruptedException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();

						}
					}
				}

			}
		}

	}

	/**
	 * @fn Cleanup
	 * @author zhuzhenlei
	 * @brief cleanup
	 *            [in]
	 *            [out]
	 * @return NULL
	 */
	public void Cleanup()
	{
		// release player resource

		Player.getInstance().freePort(m_iPort);
		m_iPort = -1;

		// release net SDK resource
		HCNetSDK.getInstance().NET_DVR_Cleanup();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_BACK:

                if(Player.getInstance()==null){
					stopSinglePlayer();
					Cleanup();
				}

				// android.os.Process.killProcess(android.os.Process.myPid());
				MonitorActivity.this.finish();
				break;
			default:
				break;
		}

		return true;
	}




	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
	{

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		m_osurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		Log.i(TAG, "surface is created" + m_iPort);
		if (-1 == m_iPort)
		{
			return;
		}
		Surface surface = holder.getSurface();
		if (true == surface.isValid()) {
			if (false == Player.getInstance().setVideoWindow(m_iPort, 0, holder)) {
				Log.e(TAG, "Player setVideoWindow failed!");
			}
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		Log.i(TAG, "Player setVideoWindow release!" + m_iPort);
		if (-1 == m_iPort)
		{
			return;
		}
		if (true == holder.getSurface().isValid()) {
			if (false == Player.getInstance().setVideoWindow(m_iPort, 0, null)) {
				Log.e(TAG, "Player setVideoWindow failed!");
			}
		}

	}
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			/**一键报警 */
			case R.id.btn_call:
				new Thread(new Runnable() {

					@Override
					public void run() {
						//String redisip="192.168.0.105";
						String result="";
						Message msg=new Message();
						try{
							Jedis jedis = new Jedis(redisip, 6379,15000);
                            Point point= (Point) GeometryEngine.project(MapActivity.upPoint,SpatialReference.create(2343), SpatialReference.create(4326));
							//jedis.set("foo", "bar");
						/*	String lon=MapActivity.upPoint.getX()+"";
							String lat=MapActivity.upPoint.getY()+"";*/
                            String lon=point.getX()+"";
                            String lat=point.getY()+"";
							//jedis.set("call", "{'alarm':1,'lon':"+lon+",'lat':"+lat+",}");
							String alarminfo="Android,otitan,"+lat.toString()+","+lon.toString();
							result=jedis.set("msg_sdbj", alarminfo);
							jedis.close();
						}catch (Exception e){
							msg.what=0;
                            msg.obj=e;
							myhanHandler.sendMessage(msg);
							return;
						}



						if(result.equals("OK")){
							msg.what=1;
						}else{
                            msg.obj="jedis 报警失败";
							msg.what=0;
						}
						//String value = jedis.get("foo");


						myhanHandler.sendMessage(msg);

					}
				}).start();
				break;
			/**防盗监控 */
			case R.id.btn_monitorview:
				m_oPsd="sfb12345";
				m_oIPAddr="222.85.147.92";
				m_oPort = "8000";
				TD="8";
				//先登出在登录
				if(m_iLogID!=-1){
					if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(m_iLogID))
					{
						Log.e(TAG, " NET_DVR_Logout is failed!");
						ToastUtil.setToast(MonitorActivity.this, "设备登出失败");
						return;
					}else{
						m_iLogID = -1;
						stopSinglePreview();
					}
				}
				login();
				preview();
				break;
			/**可见光 */
			case R.id.btn_nomorview:
				TD=monitorintent.getStringExtra("TD");
				m_oPsd=password;
				m_oIPAddr = ip;
				m_oPort=m_Port;
				//m_oIPAddr=nomor_td;
				//m_oPort=nomor_port;
				//TD=nomor_td;
				if(m_iLogID!=-1){
					if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(m_iLogID))
					{
						Log.e(TAG, " NET_DVR_Logout is failed!");
						ToastUtil.setToast(MonitorActivity.this, "设备登出失败");
						return;
					}else{
						m_iLogID = -1;
						stopSinglePreview();
					}
				}

				login();
				preview();
				break;
			/**红外 */
			case R.id.btn_irview:
				//m_oPort="8000";
				TD=monitorintent.getStringExtra("TD");
				m_oPsd=password;
				m_oIPAddr=irip;
				m_oPort=m_Port;
				if(m_iLogID!=-1){
					if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(m_iLogID))
					{
						Log.e(TAG, " NET_DVR_Logout is failed!");
						ToastUtil.setToast(MonitorActivity.this, "设备登出失败");
						return;
					}else{
						m_iLogID = -1;
						stopSinglePreview();
					}
				}
				login();
				preview();
         /* new Thread(new Runnable() {

				@Override
				public void run() {
					  if(m_oPreviewBtn.getText().equals("停止预览")){
						  mediaPlayer.release();
						  mediaPlayer=null;
							Message msg=new Message();
							msg.what=5;
							myhanHandler.sendMessage(msg);

						  return;
					  }
					  if(mediaPlayer==null){
			              mediaPlayer = new MediaPlayer();
			          }
					  try {
			          mediaPlayer.reset();
			          //主码流
			          //rtsp://admin:12345@192.0.0.64:81/h264/ch1/main/av_stream
			          //rtsp://admin:12345@192.0.0.64:81/MPEG-4/ch1/main/av_stream
			          //宿州
			          //rtsp://admin:admin@112.26.183.12:554/cam/realmonitor?channel=1&subtype=2
			          String rtsp="rtsp://admin:admin12345@"+irip+":554/h264/ch1/sub/av_stream";

							mediaPlayer.setDataSource(rtsp);
				              mediaPlayer.setDisplay(m_osurfaceView.getHolder());
				              mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				                  @Override
				                  public void onPrepared(MediaPlayer mp) {
				                      mp.start();
				                  	Message msg=new Message();
									msg.what=4;
									myhanHandler.sendMessage(msg);
				                  }
				              });
				              mediaPlayer.prepareAsync();
						} catch (Exception e) {
							e.printStackTrace();
							Message msg=new Message();
							msg.what=3;
							myhanHandler.sendMessage(msg);
						}
			              //Sets the audio stream type for this MediaPlayer，设置流的类型，此为音乐流

			              //Thread.currentThread().sleep(1000);
			             // mediaPlayer.prepareAsync();
			              //Interface definition for a callback to be invoked when the media source is ready for playback


				}
			}).start();*/
				break;

			case R.id.bt_zoomin:
				//PTZTest.Test_PTZSelZoomIn(m_iPlayID);
				break;

			case R.id.bt_zoomout:
				//PTZTest.Test_PTZSelZoomIn_EX(m_iLogID, m_iChanNum);
				break;
			case R.id.btn_Preview:
				new Thread(new Runnable() {

					@Override
					public void run() {
						if(m_oPreviewBtn.getText().equals("停止预览")){
							mediaPlayer.release();
							mediaPlayer=null;
							Message msg=new Message();
							msg.what=5;
							myhanHandler.sendMessage(msg);

							return;
						}
						if(mediaPlayer==null){
							mediaPlayer = new MediaPlayer();
						}
						try {
							mediaPlayer.reset();
							//子码流：

							// rtsp://admin:12345@192.0.0.64/mpeg4/ch1/sub/av_stream

							//rtsp://admin:12345@192.0.0.64/h264/ch1/sub/av_stream
							String rtsp="rtsp://admin:admin12345@"+monitorip+":554/h264/ch1/sub/av_stream";

							mediaPlayer.setDataSource(rtsp);
							mediaPlayer.setDisplay(m_osurfaceView.getHolder());
							mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
								@Override
								public void onPrepared(MediaPlayer mp) {
									mp.start();
									Message msg=new Message();
									msg.what=4;
									myhanHandler.sendMessage(msg);
								}
							});
							mediaPlayer.prepareAsync();
						} catch (Exception e) {
							e.printStackTrace();
							Message msg=new Message();
							msg.what=3;
							myhanHandler.sendMessage(msg);
						}
						//Sets the audio stream type for this MediaPlayer，设置流的类型，此为音乐流

						//Thread.currentThread().sleep(1000);
						// mediaPlayer.prepareAsync();
						//Interface definition for a callback to be invoked when the media source is ready for playback


					}
				}).start();

				break;
			case  R.id.btn_rtsp:
				new Thread(new Runnable() {
				@Override
				public void run() {
					  if(m_oPreviewBtn.getText().equals("停止预览")){
						  mediaPlayer.release();
						  mediaPlayer=null;
							Message msg=new Message();
							msg.what=5;
							myhanHandler.sendMessage(msg);
						  return;
					  }
					  if(mediaPlayer==null){
			              mediaPlayer = new MediaPlayer();
			          }
					  try {
			          mediaPlayer.reset();
			          //主码流
			          //rtsp://admin:12345@192.0.0.64:81/h264/ch1/main/av_stream
			          //rtsp://admin:12345@192.0.0.64:81/MPEG-4/ch1/main/av_stream
			          //宿州
						  //rtsp://admin:admin@112.26.183.12:554/cam/realmonitor?channel=1&subtype=2
						  String rtsp="rtsp://admin:admin@112.26.183.12:554/cam/realmonitor?channel=1&subtype=2";//大华
                          //MediaPlayer   error (1, -2147483648) 编码错误
			         //String rtsp="rtsp://admin:admin12345@"+irip+":554/h264/ch1/sub/av_stream";//海康
                      //创建client，需要传入一个SurfaceView作为显示
						  //String host = "rtsp://192.168.0.217/test.264"

							  mediaPlayer.setDataSource(rtsp);
				              mediaPlayer.setDisplay(m_osurfaceView.getHolder());
				              mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				                  @Override
				                  public void onPrepared(MediaPlayer mp) {
				                      mp.start();
				                  	Message msg=new Message();
									msg.what=4;
									myhanHandler.sendMessage(msg);
				                  }
				              });
				              mediaPlayer.prepareAsync();
						} catch (Exception e) {
							e.printStackTrace();
							Message msg=new Message();
							msg.what=3;
							myhanHandler.sendMessage(msg);
						}
			              //Sets the audio stream type for this MediaPlayer，设置流的类型，此为音乐流

			              //Thread.currentThread().sleep(1000);
			             // mediaPlayer.prepareAsync();
			              //Interface definition for a callback to be invoked when the media source is ready for playback


				}
			}).start();
				break;


			default:
				break;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mediaPlayer != null) {
			stopSinglePreview();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		HCNetSDK.getInstance().NET_DVR_Cleanup();//释放sdk资源
		//关闭,请在Activity销毁时调用此方法
		//在UDP模式下即使销毁Activity某些RTSP服务器也会继续发送报文
		//mRtspClient.shutdown();
	}
	private class simpleGestureListener extends
			GestureDetector.SimpleOnGestureListener {

		/*****OnGestureListener的函数*****/

		final int FLING_MIN_DISTANCE = 100, FLING_MIN_VELOCITY = 200;

// 触发条件 ：
// X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒

		// 参数解释：
// e1：第1个ACTION_DOWN MotionEvent
// e2：最后一个ACTION_MOVE MotionEvent
// velocityX：X轴上的移动速度，像素/秒
// velocityY：Y轴上的移动速度，像素/秒
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
							   float velocityY) {

			float xlen=e1.getX() - e2.getX();
			float ylen=e2.getY() - e1.getY();
			if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
					&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
				pt_goleft();
				try {
					float t1=xlen/ Math.abs(velocityX);
					float t2=xlen%Math.abs(velocityX);
					long time=(long) (xlen/ Math.abs(velocityX)+xlen%Math.abs(velocityX))*1000;
					if(time<0||time>10000){
						pt_stopleft();
					}else{

						Thread.sleep(time);
						pt_stopleft();
					}

				} catch (InterruptedException e) {
					pt_stopleft();
					e.printStackTrace();
				}
				Log.i("MyGesture", "Fling left");
				Toast.makeText(MonitorActivity.this, "Fling Left", Toast.LENGTH_SHORT).show();

			} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
					&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
				pt_goright();
				try {
					long time=(long) (xlen/ Math.abs(velocityX)+xlen%Math.abs(velocityX))*1000;
					if(time<0||time>10000){
						pt_stopright();
					}else{
						Thread.sleep(time);
						pt_stopright();
					}

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					pt_stopright();
					e.printStackTrace();
				}
				Log.i("MyGesture", "Fling right");
				Toast.makeText(MonitorActivity.this, "Fling Right", Toast.LENGTH_SHORT).show();
			}
			// 手势向下 down
			if ((e2.getY() - e1.getY()) > 200) {
				pt_godown();
				try {
					long time=(long) (ylen/ Math.abs(velocityY)+ylen%Math.abs(velocityY))*1000;
					if(time<0||time>10000){
						pt_stopdown();
					}else{

						Thread.sleep(time);
						pt_stopdown();
					}

				} catch (InterruptedException e) {
					pt_stopdown();
					e.printStackTrace();
				}
				Log.i("MyGesture", "Fling Down");
				Toast.makeText(MonitorActivity.this, "Fling Down", Toast.LENGTH_SHORT).show();
			}
			// 手势向上 up
			if ((e1.getY() - e2.getY()) > 200) {
				pt_goup();
				try {
					long time=(long) (ylen/ Math.abs(velocityY)+ylen%Math.abs(velocityY))*1000;
					if(time<0||time>10000){
						pt_stopup();
					}else{

						Thread.sleep(time);
						pt_stopup();
					}

				} catch (InterruptedException e) {
					pt_stopup();
					e.printStackTrace();
				}
				Log.i("MyGesture", "Fling Up");
				Toast.makeText(MonitorActivity.this, "Fling Up", Toast.LENGTH_SHORT).show();
			}
    /*if(e1.getPointerCount())*/

			return true;
		}
	}

	/**
	 *
	 *
	 * 自定义滑动手势
	 *
	 */
	private class MoveListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
							   float velocityY) {
			// 手势向下 down
			if ((e2.getRawY() - e1.getRawY()) > 200) {
				finish();//在此处控制关闭
				return true;
			}
			// 手势向上 up
			if ((e1.getRawY() - e2.getRawY()) > 200) {

				return true;
			}

			return super.onFling(e1, e2, velocityX, velocityY);
		}

		/*@Override
		public boolean onMoveBegin(MoveGestureDetector detector) {
			log.info("滑动开始");

			return super.onMoveBegin(detector);
		}

		@Override
		public void onMoveEnd(MoveGestureDetector detector) {
			log.info("滑动结束");
			 super.onMoveEnd(detector);
		}

		@Override
		public boolean onMove(MoveGestureDetector detector) {
			if(m_iLogID<0){
				Toast.makeText(mContext, "请先登录设备", Toast.LENGTH_SHORT).show();
				return true;
			}
			PointF d = detector.getFocusDelta();
			detector.getFocusX();
			mFocusX += d.x;
			mFocusY += d.y;
			//向左
			if(mFocusX<0&&(mFocusY>-60&&mFocusY<60)){
				pt_goleft();
				try {
					Thread.sleep(2000);
					pt_stopleft();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//向右
			if(mFocusX>0&&(mFocusY>-60&&mFocusY<60)){
				pt_goright();
				try {
					Thread.sleep(2000);
					pt_stopright();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			//向上
			if(mFocusY>0&&(mFocusX>-50&&mFocusX<50)){
				pt_goup();
				try {
					Thread.sleep(2000);
					pt_stopup();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//向下
			if(mFocusY<0&&(mFocusX>-50&&mFocusX<50)){
				pt_godown();
				try {
					Thread.sleep(2000);
					pt_stopdown();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// mFocusX = detector.getFocusX();
			// mFocusY = detector.getFocusY();
			return true;
		}*/
	}
	/**
	 * 云台开始向右
	 */
	public void pt_goright () {
		if (!HCNetSDK.getInstance()
				.NET_DVR_PTZControl_Other(m_iLogID,
						m_iStartChan, PTZCommand.PAN_RIGHT, 0))
		{
			Log.e(TAG,
					"stop PAN_RIGHT failed with error code: "
							+ HCNetSDK.getInstance()
							.NET_DVR_GetLastError());
		} else
		{
			Log.i(TAG, "stop PAN_RIGHT succ");
			btRigth.setText("正在向右");
		}
	}
	/**
	 * 云台开始向左
	 */
	public void pt_goleft () {
		if (!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(
				m_iLogID, m_iStartChan, PTZCommand.PAN_LEFT, 0))
		{
			Log.e(TAG,
					"start PAN_RIGHT failed with error code: "
							+ HCNetSDK.getInstance()
							.NET_DVR_GetLastError());
		} else
		{
			Log.i(TAG, "start PAN_RIGHT succ");
			btLeft.setText("正在向左");
		}
	}
	/**
	 * 云台开始向上
	 */
	public void pt_goup () {
		if (!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(
				m_iLogID, m_iStartChan, PTZCommand.TILT_UP, 0))
		{
			Log.e(TAG,
					"start PAN_LEFT failed with error code: "
							+ HCNetSDK.getInstance()
							.NET_DVR_GetLastError());
			ToastUtil.setToast(
					(Activity) getApplicationContext(),
					"云台上仰失败");
		} else
		{
			Log.i(TAG, "start PAN_LEFT succ");
			btUp.setText("正在上仰");
		}
	}
	/**
	 * 云台开始向下
	 */
	public void pt_godown () {
		if (!HCNetSDK.getInstance()
				.NET_DVR_PTZControl_Other(m_iLogID,
						m_iStartChan, PTZCommand.TILT_DOWN, 0))
		{
			Log.e(TAG,
					"start PAN_LEFT failed with error code: "
							+ HCNetSDK.getInstance()
							.NET_DVR_GetLastError());
			ToastUtil.setToast(
					(Activity) getApplicationContext(),
					"云台下俯失败");
		} else
		{
			Log.i(TAG, "start PAN_LEFT succ");
			btDown.setText("正在下俯");
		}
	}
	/**
	 * 云台停止向右
	 */
	public void pt_stopright() {
		if (!HCNetSDK.getInstance()
				.NET_DVR_PTZControl_Other(m_iLogID,
						m_iStartChan, PTZCommand.PAN_RIGHT, 1))
		{
			Log.e(TAG,
					"stop PAN_RIGHT failed with error code: "
							+ HCNetSDK.getInstance()
							.NET_DVR_GetLastError());
		} else
		{
			Log.i(TAG, "stop PAN_RIGHT succ");
			btRigth.setText("向右");
		}
	}
	/**
	 * 云台停止向左
	 */
	public void pt_stopleft() {
		if (!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(
				m_iLogID, m_iStartChan, PTZCommand.PAN_LEFT, 1))
		{
			Log.e(TAG,
					"stop PAN_LEFT failed with error code: "
							+ HCNetSDK.getInstance()
							.NET_DVR_GetLastError());
		} else
		{
			Log.i(TAG, "stop PAN_LEFT succ");
			btLeft.setText("向左");
		}
	}
	/**
	 * 云台停止向上
	 */
	public void pt_stopup () {
		if (!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(
				m_iLogID, m_iStartChan, PTZCommand.TILT_UP, 1))
		{
			Log.e(TAG,
					"start PAN_RIGHT failed with error code: "
							+ HCNetSDK.getInstance()
							.NET_DVR_GetLastError());
		} else
		{
			Log.i(TAG, "start PAN_RIGHT succ");
			btUp.setText("向上");
		}
	}
	/**
	 * 云台停止向下
	 */
	public void pt_stopdown () {
		if (!HCNetSDK.getInstance()
				.NET_DVR_PTZControl_Other(m_iLogID,
						m_iStartChan, PTZCommand.TILT_DOWN, 1))
		{
			Log.e(TAG,
					"start PAN_RIGHT failed with error code: "
							+ HCNetSDK.getInstance()
							.NET_DVR_GetLastError());
		} else
		{
			Log.i(TAG, "start PAN_RIGHT succ");
			btDown.setText("向下");
		}
	}



	@Override
	public boolean onTouch(View v, MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		// return super.onTouchEvent(event);
		return true;
	}

}
