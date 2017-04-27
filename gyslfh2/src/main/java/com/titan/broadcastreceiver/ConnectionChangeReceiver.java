package com.titan.broadcastreceiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.titan.forestranger.MyApplication;
import com.titan.services.MyIntentService;


public class ConnectionChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		 ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
         
        // NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); 
         if(networkInfo==null){
        	 Log.i("通知", "当前的网络连接不可用");
             Toast.makeText(context, "当前网络不可用", Toast.LENGTH_LONG).show();
             MyApplication.IntetnetISVisible=false;
             return;
         }
         boolean available = networkInfo.isAvailable();  
         if(available){  
        	 MyApplication.IntetnetISVisible=true;
          Log.i("通知", "当前的网络连接可用");
          //部分机型报错
         /* State state = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();  
          if(State.CONNECTED==state){  
           Log.i("通知", "GPRS网络已连接");  
          } */ 
         if( networkInfo.getType()== ConnectivityManager.TYPE_MOBILE){
        	 Log.i("通知", "移动网络已连接");
        	 Toast.makeText(context, "移动网络已连接", Toast.LENGTH_LONG).show();
             //上传轨迹点
             Intent startServiceIntent = new Intent(context,MyIntentService.class);
             Bundle bundle = new Bundle();
             bundle.putString("name", "upPointHistory");
             startServiceIntent.putExtras(bundle);
             context.startService(startServiceIntent);
         }
            
         // state = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();  
          if(networkInfo.getType()== ConnectivityManager.TYPE_WIFI){
           Log.i("通知", "WIFI网络已连接");
             Toast.makeText(context, "WIFI已连接", Toast.LENGTH_LONG).show();
            /*  final Intent intent = new Intent(this,BindService.class);
              bindService(intent,coon,Service.BIND_AUTO_CREATE)*/

              //上传轨迹点
             Intent startServiceIntent = new Intent(context,MyIntentService.class);
 		    Bundle bundle = new Bundle();
 		    bundle.putString("name", "upPointHistory");
 		    startServiceIntent.putExtras(bundle);
 		    context.startService(startServiceIntent);
          }  
         }  
         else{  
          Log.i("通知", "当前的网络连接不可用");
          Toast.makeText(context, "当前网络不可用", Toast.LENGTH_LONG).show();
          MyApplication.IntetnetISVisible=false;
         }  
         
        
     /*    if ( activeNetInfo != null ) {   
             Toast.makeText( context, "Active Network Type : " +
                   activeNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();   
             }   
         if( mobNetInfo != null ) {   
             Toast.makeText( context, "Mobile Network Type : " +
                   mobNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();   
             }   */
       }   
	}


