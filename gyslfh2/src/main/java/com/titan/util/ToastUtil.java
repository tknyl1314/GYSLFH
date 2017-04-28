package com.titan.util;


import android.app.Activity;
import android.content.Context;
import android.widget.Toast;


public class ToastUtil {


	/*public static Toast makeText(Context context, CharSequence text,
								 int duration) {
		Toast result = Toast.makeText(context, text, duration);
		TextView textView = new TextView(new ContextThemeWrapper(context,
				R.style.FetionTheme_Dialog_Toast));
		textView.setText(text);
		result.setView(textView);
		result.setGravity(Gravity.CENTER, 0, 120);
		return result;
	}*/

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

				//ToastUtil.makeText(context, text, Toast.LENGTH_SHORT).show();
			}
		});
	}
	/**
	 * @param context
	 * @param text
	 */
	public static void setToast(final Context context, final String text) {
		((Activity) context).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				//ToastUtil.makeText(context, text, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public static void showToast(final Context context, final String text, final int type) {
		((Activity) context).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context,text, type).show();
				//ToastUtil.makeText(context, text, Toast.LENGTH_SHORT).show();
			}
		});
	}






}
