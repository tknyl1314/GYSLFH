package com.otitan.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;
import android.widget.Toast;

public class Network_Service {

	private ConnectivityManager connectivityManage;
	private Context context;

	public Network_Service(Context context) {
		this.context = context;
	}

	/**
	 * �����Ƿ�ɹ�
	 * 
	 * @return true���ɹ� false��ʧ��
	 */
	public boolean isConnectivityTest() {

		connectivityManage = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManage.getActiveNetworkInfo();
		if (networkInfo == null) {
			Toast.makeText(context, "��ǰ���������Ӳ�����", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			boolean available = networkInfo.isAvailable();
			if (available) {
				Log.i("֪ͨ", "��ǰ���������ӿ���");
				return true;
			} else {
				Log.i("֪ͨ", "��ǰ���������Ӳ�����");
				Toast.makeText(context, "��ǰ���������Ӳ�����", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		}

	}

	/**
	 * ��������������
	 * 
	 * @return 0: None, 1: Wifi, 2: GPRS, 3: Other
	 */
	protected int checkNetworkType() {
		// if (Global.IsDebug)
		// return 1;

		connectivityManage = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// ��ȡ��������״̬��NetWorkInfo����
		NetworkInfo networkInfo = connectivityManage.getActiveNetworkInfo();
		// ��ȡ��ǰ�����������Ƿ����
		if (networkInfo == null || !networkInfo.isAvailable())
			return 0;

		// Wifi
		State state = connectivityManage.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).getState();
		if (State.CONNECTED == state) {
			return 1;
		}

		// GPRS
		state = connectivityManage.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).getState();
		if (State.CONNECTED == state) {
			return 2;
		}

		return 3;
	}

}
