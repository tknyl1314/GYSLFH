package com.otitan.util;


import android.app.Activity;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.otitan.entity.CustomProgressDialog;
import com.otitan.gyslfh.R;

public class ToastUtil {

	private static CustomProgressDialog progressDialog;

	public static Toast makeText(Context context, CharSequence text,
								 int duration) {
		Toast result = Toast.makeText(context, text, duration);
		TextView textView = new TextView(new ContextThemeWrapper(context,
				R.style.FetionTheme_Dialog_Toast));
		textView.setText(text);
		result.setView(textView);
		result.setGravity(Gravity.CENTER, 0, 120);
		return result;
	}

	/**
	 * 提示
	 *
	 * @param context
	 * @param text
	 */
	public static void setToast(final Activity context, final String text) {
		context.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				ToastUtil.makeText(context, text, Toast.LENGTH_SHORT).show();
			}
		});
	}




	public static void startProgressDialog(Context context) {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(context);
			progressDialog.setMessage("数据加载中...");
		}
		progressDialog.show();
	}

	public static void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

}
