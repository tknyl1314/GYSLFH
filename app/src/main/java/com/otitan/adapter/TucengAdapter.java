package com.otitan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.otitan.gyslfh.R;

import java.util.HashMap;
import java.util.List;

public class TucengAdapter extends BaseAdapter {

	// ������ݵ�list
	private List<HashMap<String, Object>> list;
	// ������
	private Context context;
	
	private ListView tcListView;
	// �������벼��
	private LayoutInflater inflater = null;
	
	private static HashMap<Integer,Boolean> isSelected;
	
	public TucengAdapter(List<HashMap<String, Object>> list, Context context,ListView tcListView) {
		this.context = context;
		this.list = list;
		this.tcListView = tcListView;
		tcListView.setOnItemClickListener(new myListence());
		inflater = LayoutInflater.from(context);
		isSelected = new HashMap<Integer, Boolean>();
		initDate();
	}

	// ��ʼ��isSelected������
	private void initDate() {
		for (int i = 0; i < list.size(); i++) {
			getIsSelected().put(i, (Boolean)list.get(i).get("isTrue"));
		}
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			// ���ViewHolder����
			holder = new ViewHolder();
			// ���벼�ֲ���ֵ��convertview
			convertView = inflater.inflate(R.layout.item_checkbox, null);
			holder.tv = (TextView) convertView.findViewById(R.id.item_tv);
			holder.cb = (CheckBox) convertView.findViewById(R.id.item_cb);
			// Ϊview���ñ�ǩ
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// ����list��TextView����ʾ
		holder.tv.setText(list.get(position).get("name")+"");
		holder.cb.setChecked(getIsSelected().get(position));
		notifyDataSetChanged();
		return convertView;
	}
	
	

	class ViewHolder {
		TextView tv;
		CheckBox cb;
	}
	
	public static HashMap<Integer,Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer,Boolean> isSelected) {
		TucengAdapter.isSelected = isSelected;
	}
	
	class myListence implements AdapterView.OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long arg3) {
			ViewHolder holder = (ViewHolder) view.getTag();
			holder.cb.toggle();
			getIsSelected().put(position, holder.cb.isChecked());
		}
		
	}
	
}
