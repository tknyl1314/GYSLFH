package com.otitan.gyslfh.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.king.photo.util.Res;
import com.otitan.gyslfh.R;
import com.otitan.util.PadUtil;

import java.lang.ref.WeakReference;

public class StartActivity extends Activity
{

	private final int SPLASH_DISPLAY_LENGHT = 1000;
	private static final int NEXT_ACTIVITY = 0;
	// 百度推送
	public static final String api_key = "rnYG5iog6pEhzUaqt4t1nvLX";
	MyHandler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Res.init(this);// 拍照初始化

		// 启动云推送
		// PushManager.startWork(getApplicationContext(),PushConstants.LOGIN_TYPE_API_KEY,Utils.getMetaValue(StartActivity.this,
		// "api_key"));

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (PadUtil.isPad(this))
		{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		setContentView(R.layout.activity_start);

		// 以apikey的方式登录，一般放在主Activity的onCreate中
		// PushManager.startWork(getApplicationContext(),PushConstants.LOGIN_TYPE_API_KEY,
		// "kWEaaVzF8viZEWDZXWRaGQtU");

		// handler = new MyHandler(StartActivity.this);
		Intent intent = new Intent(StartActivity.this, LoginActivity.class);
		StartActivity.this.startActivity(intent);
		StartActivity.this.finish();
		/*
		 * handler = new MyHandler(); new Handler().postDelayed(new Runnable() {
		 * 
		 * @Override public void run() { Message msg = new Message(); msg.what =
		 * NEXT_ACTIVITY; handler.sendMessage(msg); } }, SPLASH_DISPLAY_LENGHT);
		 */
	}

	class MyHandler extends Handler
	{
		WeakReference<StartActivity> reference;

		/*
		 * public MyHandler(StartActivity activity) { reference = new
		 * WeakReference<StartActivity>(activity); }
		 */
		public MyHandler()
		{
		}

		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
			case NEXT_ACTIVITY:
				// StartActivity startActivity = reference.get();
				Intent intent = new Intent(StartActivity.this,
						LoginActivity.class);
				StartActivity.this.startActivity(intent);
				StartActivity.this.finish();
				break;
			default:
				break;
			}
		}
	}
}
