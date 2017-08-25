package com.titan.gyslfh.monitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.hikvision.netsdk.HCNetSDK;
import com.titan.BaseActivity;
import com.titan.BaseViewModel;
import com.titan.Injection;
import com.titan.ViewModelHolder;
import com.titan.gyslfh.TitanApplication;
import com.titan.newslfh.R;
import com.titan.util.ActivityUtils;

import org.MediaPlayer.PlayM4.Player;

//import com.test.demo.VoiceTalk;

public class MonitorActivity extends BaseActivity {

	private MonitorFragment mFragment;
	private MonitorViewModel mViewModel;

	private Intent monitorintent;

	private HikVisionUtil mHikVisionUtil;
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
			String  data=getIntent().getStringExtra("data");
			MonitorViewModel viewModel = new MonitorViewModel(getApplicationContext(), mFragment, Injection.provideDataRepository(mContext),data);
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
		//TitanApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_mointor);
		mContext=this;
		mHikVisionUtil=new HikVisionUtil();
		mFragment= (MonitorFragment) findOrCreateViewFragment();
        mFragment.setmHikVisionUtil(mHikVisionUtil);
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
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_BACK:

                if(Player.getInstance()==null){
					mHikVisionUtil.stopSinglePlayer();
					mHikVisionUtil.Cleanup();
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





}
