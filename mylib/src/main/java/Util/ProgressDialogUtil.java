package Util;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.mylib.R;

import dialog.CustomProgressDialog;

public class ProgressDialogUtil {
	
	private static CustomProgressDialog progressDialog;
	
	@SuppressLint("ShowToast")
	public static Toast makeText(Context context, CharSequence text,
			int duration) {
		Toast result = Toast.makeText(context, text, duration);
		TextView textView = new TextView(new ContextThemeWrapper(context,R.style.FetionTheme_Dialog_Toast));
		textView.setText(text);
		result.setView(textView);
		result.setGravity(Gravity.CENTER, 0, 120);
		return result;
	}
	
	/**
	 * @param context
	 * @param text
	 */
	public static void setToast(final Activity context, final String text) {
		context.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				ProgressDialogUtil.makeText(context, text, Toast.LENGTH_SHORT).show();
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
	public static void startProgressDialog(Context context,String msg) {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(context);
			progressDialog.setMessage(msg);
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
