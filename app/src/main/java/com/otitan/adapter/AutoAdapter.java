package com.otitan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.otitan.gyslfh.R;

import java.util.ArrayList;

public class AutoAdapter extends BaseAdapter implements Filterable {
	private ArrayFilter mFilter;
	private ArrayList<String> mList;
	private Context context;
	
	public AutoAdapter(ArrayList<String> mList, Context context) {
		this.context = context;
		this.mList = mList;
	}

	@Override
	public int getCount() {
		return mList==null ? 0:mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			convertView = View.inflate(context, R.layout.item_txt, null);
			
			holder = new ViewHolder();
			holder.tv_name = (TextView) convertView.findViewById(R.id.txt);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tv_name.setText(mList.get(position));
		//notifyDataSetChanged();
		return convertView;
	}
	
	static class ViewHolder{
		public TextView tv_name;
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	private class ArrayFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

            if (prefix == null || prefix.length() == 0) {
                ArrayList<String> list = mList;
                results.values = list;
                results.count = list.size();
            } else {
                ArrayList<String> unfilteredValues = mList;
                int count = unfilteredValues.size();
                ArrayList<String> newValues = new ArrayList<String>(count);
                for (int i = 0; i < count; i++) {
                	String pc = unfilteredValues.get(i);
                    if (pc != null && !pc.equals("")) {
                    	newValues.add(pc);
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,FilterResults results) {
            mList = (ArrayList<String>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
		}
	}
}
