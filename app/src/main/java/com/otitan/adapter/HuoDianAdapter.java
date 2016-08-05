package com.otitan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otitan.gyslfh.R;

import java.util.HashMap;
import java.util.List;

public class HuoDianAdapter extends BaseAdapter {

	// 填充数据的list
	private List<HashMap<String, Object>> list;
	// 上下文
	private Context context;
	// 用来导入布局
	private LayoutInflater inflater = null;

	// 构造器
	public HuoDianAdapter(List<HashMap<String, Object>> list, Context context) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			// 获得ViewHolder对象
			holder = new ViewHolder();
			// 导入布局并赋值给convertview
			convertView = inflater.inflate(R.layout.item_huodian, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.item_tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.item_tv2);
			holder.tv3 = (TextView) convertView.findViewById(R.id.item_tv3);
			//holder.tv4 = (TextView) convertView.findViewById(R.id.item_tv4);
			holder.tv5 = (TextView) convertView.findViewById(R.id.item_tv5);
			// 为view设置标签
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 设置list中TextView的显示
		holder.tv1.setText("时间："+list.get(position).get("FIRESTART").toString());
		holder.tv2.setText("地点："+list.get(position).get("address").toString());
		if(list.get(position).get("FIRE_STATE").equals("0")){
			holder.tv3.setText("火情燃烧状态: 已经熄灭");
		}else if(list.get(position).get("FIRE_STATE").equals("1")){
			holder.tv3.setText("火情燃烧状态: 正在燃烧");
		}
		//holder.tv4.setText("纬度："+list.get(position).get("Y").toString());
		holder.tv5.setText("区县："+list.get(position).get("COUNTY").toString());
		// notifyDataSetChanged();
		return convertView;
	}

	final class ViewHolder {
		TextView tv1;
		TextView tv2;
		TextView tv3;
		//TextView tv4;
		TextView tv5;
	}
}
