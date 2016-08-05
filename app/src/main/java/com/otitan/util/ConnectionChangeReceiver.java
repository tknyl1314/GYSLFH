package com.otitan.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.otitan.gyslfh.activity.MyApplication;

import java.util.ArrayList;

public class ConnectionChangeReceiver extends BroadcastReceiver {
	public static ArrayList<netEventHandler> mListeners = new ArrayList<netEventHandler>();
	public static String NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	@Override
	public void onReceive(Context context, Intent intent) {
		

		if (intent.getAction().equals(NET_CHANGE_ACTION)) {
			MyApplication.mNetWorkState = NetUtil.getNetworkState(context);
			if (mListeners.size() > 0)
				for (netEventHandler handler : mListeners) {
					handler.onNetChange();
				}
		}
	}

	public static abstract interface netEventHandler {
		public abstract boolean onNetChange();
	}
}
