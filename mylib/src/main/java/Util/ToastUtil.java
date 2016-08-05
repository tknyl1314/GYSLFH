package Util;

import android.app.Activity;
import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.mylib.R;

public class ToastUtil {
	
	public static Toast makeText(Context context, CharSequence text,int duration) {
		Toast result = Toast.makeText(context, text, duration);
		TextView textView = new TextView(new ContextThemeWrapper(context,R.style.FetionTheme_Dialog_Toast));
		textView.setText(text);
		result.setView(textView);
		result.setGravity(Gravity.CENTER, 0, 120);
		return result;
	}
	
	public static Toast makeTextLeft(Context context, CharSequence text,int duration){
		Toast result = Toast.makeText(context, text, duration);
		TextView textView = new TextView(new ContextThemeWrapper(context,R.style.FetionTheme_Dialog_Toast));
		textView.setText(text);
		result.setView(textView);
		result.setGravity(Gravity.LEFT, 200, 380);
		return result;
	}
	
	/**
	 * ��ʾ
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
	/**
	 * �����ʾ
	 * @param context
	 * @param text
	 */
	public static void setToatLeft(final Activity context, final String text){
		context.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				ToastUtil.makeTextLeft(context, text, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
