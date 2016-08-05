/*
package com.otitan.push;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.otitan.gyslfh.activity.MapActivity;
import com.otitan.entity.Firepoint;
import com.otitan.gyslfh.R;

import org.json.JSONObject;


public class CustomReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    try {
      if (intent.getAction().equals("com.pushdemo.action")) {
        JSONObject json = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));
        log.e("ss",json.toString());
        //final String message = json.getString("alert");
        String message = json.getString("message");
		Gson gson=new Gson();
		Firepoint fp=gson.fromJson(message, Firepoint.class);
		String remark=fp.getREMARK();
        Intent resultIntent = new Intent(AVOSCloud.applicationContext, MapActivity.class);
        PendingIntent pendingIntent =PendingIntent.getActivity(AVOSCloud.applicationContext, 0, resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =new NotificationCompat.Builder(AVOSCloud.applicationContext) .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(AVOSCloud.applicationContext.getResources().getString(R.string.app_name))
                .setContentText(remark)
                .setTicker(remark);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(false);

        int mNotificationId = 10086;
        NotificationManager mNotifyMgr =(NotificationManager) AVOSCloud.applicationContext .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
      }
    } catch (Exception e) {
     // ToastUtil.setToast(context, "������Ϣ����ʧ��");
    	log.e("PUSH", e.toString());
    }
  }
}
*/
