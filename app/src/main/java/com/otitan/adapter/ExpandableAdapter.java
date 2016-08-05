package com.otitan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.otitan.gyslfh.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExpandableAdapter extends BaseExpandableListAdapter{
	
	private Context context;
	List<File> groups;
	List<List<File>> childs;
	List<List<Map<String, Boolean>>> childCheckBox = new ArrayList<List<Map<String,Boolean>>>();
	public ExpandableAdapter(Context context, List<File> groups, List<List<File>> childs,List<List<Map<String, Boolean>>> childCheckBox){
		this.groups = groups;
		this.childs = childs;
		this.context = context;
		this.childCheckBox = childCheckBox;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childs.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.expandable_child_item, null);
			holder = new ViewHolder();
			holder.cText = (TextView) convertView.findViewById(R.id.id_child_txt);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_child);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.cText.setText(childs.get(groupPosition).get(childPosition).getName().replace(".otms", ""));
		holder.checkBox.setChecked(childCheckBox.get(groupPosition).get(childPosition).get(childs.get(groupPosition).get(childPosition).getName())); 
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childs.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		GroupViewHolder groupHolder;
		if(null == convertView){
			convertView = LayoutInflater.from(context).inflate(
					R.layout.expandable_group_item, null);
			groupHolder = new GroupViewHolder();
			
			groupHolder.imageview_1 = (ImageView) convertView.findViewById(R.id.id_group_img);
			groupHolder.cText = (TextView) convertView.findViewById(R.id.id_group_txt);
			groupHolder.imageview_2 = (Button) convertView.findViewById(R.id.layer_render);
			convertView.setTag(groupHolder);
		}else{
			groupHolder = (GroupViewHolder) convertView.getTag();
		}


		groupHolder.cText.setText(groups.get(groupPosition).getName());
		return convertView;
	
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	static class ViewHolder {
		TextView cText;
		CheckBox checkBox;
	}
	
	static class GroupViewHolder{
		ImageView imageview_1;
		TextView cText;
		Button imageview_2;
	}
	
}
