package com.otitan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otitan.gyslfh.R;

import java.util.List;
import java.util.Map;

public class XbAdapter extends BaseAdapter {
	List<Map<String, Object>> list = null;
	// �������벼��
	private LayoutInflater inflater = null;
	public XbAdapter(List<Map<String, Object>> list,Context context) {
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
		if(null == convertView){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_xiaoban, null);
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tv1.setText(list.get(position).get("name").toString());
		return convertView;
	}
	
	final class ViewHolder {
		TextView tv1;
	}
}
