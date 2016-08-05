package com.otitan.gis;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.otitan.adapter.TucengAdapter;

public class TuCengControl
{
	/**
	 * 显示图层
	 */
	/*private void showTucengDialog(Context mcontext) {
		LayoutInflater layInflater = LayoutInflater.from(mcontext);
		View view = layInflater.inflate(R.layout.dialog_tuceng_control, null);
		final Dialog dialogTC = new Dialog(this, R.style.customDialog);
		dialogTC.setContentView(view);

		ListView tcListView = (ListView) dialogTC.findViewById(R.id.tcControl_listView);
		TucengAdapter tcAdapter = new TucengAdapter(tcList, this, tcListView);
		tcListView.setAdapter(tcAdapter);

		Button bt_sure = (Button) dialogTC.findViewById(R.id.bt_sure);
		Button bt_cancle = (Button) dialogTC.findViewById(R.id.bt_cancle);
		ImageButton returnButton = (ImageButton) dialogTC.findViewById(R.id.returnBtn);
		bt_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogTC.dismiss();
			}
		});

		returnButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogTC.dismiss();
			}
		});

		bt_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < tcList.size(); i++) {
					tcList.get(i).put("isTrue",
							TucengAdapter.getIsSelected().get(i));
					mapView.getLayer(i).setVisible(
							TucengAdapter.getIsSelected().get(i));
					dialogTC.dismiss();
				}
				mapView.invalidate();
			}
		});
		dialogTC.show();
	}*/

}
