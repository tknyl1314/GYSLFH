package com.titan.broadcastreceiver;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.otitan.gyslfh.activity.MapActivity;

public class GetuiCustonReceiver extends BroadcastReceiver {

	/*public GetuiCustonReceiver() {
		// TODO Auto-generated constructor stub
	}*/

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {
			case PushConsts.GET_CLIENTID:
				// 获取ClientID(CID)
				String cid = bundle.getString("clientid");
				break;
			case PushConsts.GET_MSG_DATA:
				// 获取透传数据 //
				byte[] payload = bundle.getByteArray("payload");
				//  String appid = bundle.getString("appid");
				String taskid = bundle.getString("taskid");
				String messageid = bundle.getString("messageid");
				// smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
				boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
				System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));


				if (payload != null) {
					String data = new String(payload);
					Log.d("GetuiSdkDemo", "receiver payload : " + data);
	 	/*Gson gson=new Gson();
		Firepoint fp=gson.fromJson(data, Firepoint.class);
		String remark=fp.getREMARK();*/
					Intent resultIntent = new Intent(context, MapActivity.class);
					resultIntent.putExtra("data", data);
					resultIntent.putExtra("com.otitan.getui", "");

					PendingIntent pendingIntent =PendingIntent.getActivity(context, 0, resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
					try {
						pendingIntent.send();
					} catch (CanceledException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					Intent resultIntent = new Intent(context, MapActivity.class);
					resultIntent.putExtra("data", "接收到一条推送消息");
					resultIntent.putExtra("com.otitan.getui", "0");
					PendingIntent pendingIntent =PendingIntent.getActivity(context, 0, resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

					try {
						pendingIntent.send();
					} catch (CanceledException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}}
				break;
			default:
				break;
		}
	}


}
