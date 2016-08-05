package com.otitan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otitan.gyslfh.R;

import java.util.HashMap;
import java.util.List;

public class HuoJingAdapter extends BaseAdapter {

	// 填充数据的list
	private List<HashMap<String, Object>> list;
	// 上下文
	@SuppressWarnings("unused")
	private Context context;
	// 用来导入布局
	private LayoutInflater inflater = null;

	// 构造器
	public HuoJingAdapter(List<HashMap<String, Object>> list, Context context) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public HashMap<String, Object> getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			// 获得ViewHolder对象
			holder = new ViewHolder();
			// 导入布局并赋值给convertview
			convertView = inflater.inflate(R.layout.item_huojing, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.item_tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.item_tv2);
			holder.tv3 = (TextView) convertView.findViewById(R.id.item_tv3);
			holder.tv4 = (TextView) convertView.findViewById(R.id.item_tv4);
			holder.tv5 = (TextView) convertView.findViewById(R.id.item_tv5);
			// 为view设置标签
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//0未查看，1查看未回，2已回复
		if (list.get(position).get("BACKSTATE").equals("0")) {
			holder.tv1.setBackgroundColor(Color.rgb(209, 235, 252));
			holder.tv2.setBackgroundColor(Color.rgb(209, 235, 252));
			holder.tv3.setBackgroundColor(Color.rgb(209, 235, 252));
			holder.tv4.setBackgroundColor(Color.rgb(209, 235, 252));
			holder.tv5.setBackgroundColor(Color.rgb(209, 235, 252));
		} else if (list.get(position).get("BACKSTATE").equals("1")) {
			holder.tv1.setBackgroundColor(Color.rgb(203, 203, 203));
			holder.tv2.setBackgroundColor(Color.rgb(203, 203, 203));
			holder.tv3.setBackgroundColor(Color.rgb(203, 203, 203));
			holder.tv4.setBackgroundColor(Color.rgb(203, 203, 203));
			holder.tv5.setBackgroundColor(Color.rgb(203, 203, 203));
		}

		// 设置list中TextView的显示
		holder.tv1.setText("序号:"
				+ list.get(position).get("RECEIPTID").toString());
		holder.tv2
				.setText("电话:" + list.get(position).get("TEL_ONE").toString());
		holder.tv3.setText("时间:"
				+ list.get(position).get("RECEIPTTIME").toString());
		holder.tv4.setText("地址:" + list.get(position).get("ADRESS").toString());
		holder.tv5.setText("来源:" + list.get(position).get("ORIGIN").toString());
		notifyDataSetChanged();
		return convertView;
	}

	final class ViewHolder {
		TextView tv1;
		TextView tv2;
		TextView tv3;
		TextView tv4;
		TextView tv5;
	}
}