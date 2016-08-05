package Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class NetUtil {
	public static final int NETWORN_NONE = 0;// ������
	public static final int NETWORN_HAVE = 1;// ������
	public static final int NETWORN_WIFI = 2;// wifi����
	public static final int NETWORN_MOBILE = 3;// 3G����

	public static int getNetworkState(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo mNetworkInfo = connManager.getActiveNetworkInfo();
		if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
			return NETWORN_HAVE;
		}

		// Wifi
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if (state != null) {
			if (state == State.CONNECTED || state == State.CONNECTING) {
				return NETWORN_WIFI;
			}
		}

		// 3G
		State state3G = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if (state3G != null) {
			if (state3G == State.CONNECTED || state3G == State.CONNECTING) {
				return NETWORN_MOBILE;
			}
		}

		return NETWORN_NONE;
	}

	public static boolean checkNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == State.CONNECTED) {
						NetworkInfo netWorkInfo = info[i];
						if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
							return true;
						} else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
