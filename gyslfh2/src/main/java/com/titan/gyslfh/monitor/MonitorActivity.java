package com.titan.gyslfh.monitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import com.hikvision.netsdk.HCNetSDK;
import com.titan.BaseActivity;
import com.titan.BaseViewModel;
import com.titan.Injection;
import com.titan.ViewModelHolder;
import com.titan.newslfh.R;
import com.titan.util.ActivityUtils;

//import com.test.demo.VoiceTalk;

public class MonitorActivity extends BaseActivity {

	private MonitorFragment mFragment;
	private MonitorViewModel mViewModel;

	Intent monitorintent=null;
	@Override
	public Fragment findOrCreateViewFragment() {
		MonitorFragment tasksFragment =
				(MonitorFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
		if (tasksFragment == null) {
			// Create the fragment
			tasksFragment = MonitorFragment.newInstance();
			ActivityUtils.addFragmentToActivity(
					getSupportFragmentManager(), tasksFragment, R.id.content_frame);
		}
		return tasksFragment;


	}

	@Override
	public BaseViewModel findOrCreateViewModel() {

		// In a configuration change we might have a ViewModel present. It's retained using the
		// Fragment Manager.
		@SuppressWarnings("unchecked")
		ViewModelHolder<MonitorViewModel> retainedViewModel =
				(ViewModelHolder<MonitorViewModel>) getSupportFragmentManager()
						.findFragmentByTag(VIEWMODEL_TAG);

		if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
			// If the model was retained, return it.
			return retainedViewModel.getViewmodel();
		} else {
			//LayerControlViewModel layerControlViewModel=new LayerControlViewModel(getApplicationContext(),this);
			// There is no ViewModel yet, create it.
			MonitorViewModel viewModel = new MonitorViewModel(getApplicationContext(), mFragment, Injection.provideDataRepository(mContext));
			// and bind it to this Activity's lifecycle using the Fragment Manager.
			ActivityUtils.addFragmentToActivity(
					getSupportFragmentManager(),
					ViewModelHolder.createContainer(viewModel),
					VIEWMODEL_TAG);
			return viewModel;
		}
	}

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mointor);
		mContext=this;
		mFragment= (MonitorFragment) findOrCreateViewFragment();
		mViewModel= (MonitorViewModel) findOrCreateViewModel();
		mFragment.setViewModel(mViewModel);
		monitorintent = getIntent();



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








	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		/*switch (keyCode)
		{
			case KeyEvent.KEYCODE_BACK:

                if(Player.getInstance()==null){
					HikVisionUtil.stopSinglePlayer();
					Cleanup();
				}

				// android.os.Process.killProcess(android.os.Process.myPid());
				MonitorActivity.this.finish();
				break;
			default:
				break;
		}*/

		return true;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		/*if(mediaPlayer != null) {
			stopSinglePreview();
			mediaPlayer.release();
			mediaPlayer = null;
		}*/
		HCNetSDK.getInstance().NET_DVR_Cleanup();//释放sdk资源
		//关闭,请在Activity销毁时调用此方法
		//在UDP模式下即使销毁Activity某些RTSP服务器也会继续发送报文
		//mRtspClient.shutdown();
	}
	/*private class simpleGestureListener extends
			GestureDetector.SimpleOnGestureListener {

		*//*****OnGestureListener的函数*****//*

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
    *//*if(e1.getPointerCount())*//*

			return true;
		}
	}
*/
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
	/*public void pt_goright () {
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
	*//**
	 * 云台开始向左
	 *//*
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
	*//**
	 * 云台开始向上
	 *//*
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
	*//**
	 * 云台开始向下
	 *//*
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
	*//**
	 * 云台停止向右
	 *//*
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
	*//**
	 * 云台停止向左
	 *//*
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
	*//**
	 * 云台停止向上
	 *//*
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
	*//**
	 * 云台停止向下
	 *//*
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
	}*/




}
