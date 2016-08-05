package com.otitan.customui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.otitan.gyslfh.R;
import com.otitan.util.PadUtil;

@SuppressLint("ViewConstructor")
public class MorePopWindow extends PopupWindow {
	public static View conentView;

	public static View getConentView() {
		return conentView;
	}

	public static void setConentView(View conentView) {
		MorePopWindow.conentView = conentView;
	}

	public MorePopWindow(final Activity context,int resouce) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(resouce, null);
		@SuppressWarnings("deprecation")
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		this.setContentView(conentView);
		if(PadUtil.isPad(context)){
			this.setWidth((int) (w*0.15));
		}else{
			this.setWidth((int) (w*0.25));
		}
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.update();
		ColorDrawable dw = new ColorDrawable(0000000000);
		this.setBackgroundDrawable(dw);
		// mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		//this.setAnimationStyle(R.style.AnimationPreview_down);
		
		this.setAnimationStyle(R.style.AnimationPreview_up);

	}
	
	public void showAtLocation(View parent) {
		if (!this.isShowing()) {
			int[] location = new int[2];
			parent.getLocationOnScreen(location);
			this.showAtLocation(parent, Gravity.RIGHT | Gravity.BOTTOM, (location[0] + parent.getWidth() / 2) - this.getWidth() / 2, -location[1] + this.getHeight());
		} else {
			this.dismiss();
		}
	}
	
	public void showPopupWindow(View parent) {
		if (!this.isShowing()) {
			this.showAsDropDown(parent, parent.getLayoutParams().width / 2 - this.getWidth()/2, 0);
		} else {
			this.dismiss();
		}
	}

}
