package com.titan.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.Map;

public class MyIntentService extends IntentService {
	List<Map<String, Object>> list ;
	String state;
	
	public MyIntentService() {
		super("MyIntentService");
	}

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("onBind");
		return super.onBind(intent);
	}

	@Override
	public void onCreate() {
		 System.out.println("onCreate");
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		 System.out.println("onDestroy");
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		System.out.println("onStart");
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		  System.out.println("onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void setIntentRedelivery(boolean enabled) {
		 System.out.println("setIntentRedelivery");
		super.setIntentRedelivery(enabled);
	}

	
	@Override
	protected void onHandleIntent(Intent intent) {
		//Logger log=LoggerManager.getLoggerInstance();  
		 //Intent是从Activity发过来的，携带识别参数，根据参数不同执行不同的任务
	    String action = intent.getExtras().getString("name");
	    switch (action) {
	    //上传历史轨迹
		case "upPointHistory":
			try {
				 //list = DataBaseHelper.selectPointGuiji(MyApplication.SBH);
			} catch (Exception e) {
				//log.debug(LoggerManager.getExceptionMessage(e));  
				//log.i(e.toString());
				return;
			}
		
			if(list.size()>0){
				//WebService web = new WebService(this);
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					String id=map.get("id") + "";
					String lon=map.get("lon") + "";
					String lat=map.get("lat") + "";
					String time= map.get("time") + "";
					String sbh=map.get("sbh")+"";
				    //state= web.upPointHistory(sbh,lon ,lat, time);
				
					if ("1".equals(state)) {
						//DataBaseHelper.updatePointGuiji(id);
						Log.i("+++", "上传历史轨迹成功");
						//log.i("上传历史轨迹成功");
					}else{
						//log.i("上传历史轨迹失败");

						Log.i("+++", "上传历史轨迹失败");
					}
				}
			}
		
			break;

		default:
			break;
		}
		
	}

}
