package com.otitan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otitan.entity.ReceiveAlarmInfo;
import com.otitan.gyslfh.R;

import java.util.HashMap;
import java.util.List;

public class AlarmManageAdapter extends BaseAdapter {
	
	public static HashMap<Integer, Boolean> isSelected;
	private LayoutInflater Inflater=null;
	private List<ReceiveAlarmInfo> Alarmlist;
	public  AlarmManageAdapter(Context context,List<ReceiveAlarmInfo> alarmlist) {
		isSelected = new HashMap<Integer, Boolean>();
		Inflater = LayoutInflater.from(context);
		Alarmlist=alarmlist;
		initdata(Alarmlist);//��ʼ������
	}

	public static void initdata(List<ReceiveAlarmInfo> Alarmlist) {
		for (int i = 0; i < Alarmlist.size(); i++) {
			isSelected.put(i, false);
		}
	}

	@Override
	public int getCount() {
		return Alarmlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder holder=null;
		if (view == null) {
			// ���ViewHolder����
			holder = new ViewHolder();
			// ���벼�ֲ���ֵ��convertview
			view = Inflater.inflate(R.layout.item_receivealarm, null);
			//holder.xuhao = (TextView) view.findViewById(R.id.xuhao);
			//holder.cb = (CheckBox) view.findViewById(R.id.cb);
			holder.tv_id = (TextView) view.findViewById(R.id.tv_id);
			holder.tv_unionid=(TextView) view.findViewById(R.id.tv_dailyid);
			//holder.tv_dailyid = (TextView) view.findViewById(R.id.tv_dailyid);
			holder.tv_phonenumber=(TextView) view.findViewById(R.id.tv_phonenumber);
			holder.tv_address=(TextView) view.findViewById(R.id.tv_address);
			holder.tv_dq=(TextView) view.findViewById(R.id.tv_dq);
			holder.tv_isfire=(TextView) view.findViewById(R.id.tv_isfire);
			holder.tv_firetype=(TextView) view.findViewById(R.id.tv_firetype);
			holder.tv_backstate=(TextView) view.findViewById(R.id.tv_backstate);
			holder.tv_alarmtime=(TextView) view.findViewById(R.id.tv_alarmtime);			
			// Ϊview���ñ�ǩ
			view.setTag(holder);
		} else {
			// ȡ��holder
			holder = (ViewHolder) view.getTag();
		}
		//holder.xuhao.setText((position+1)+"");
		holder.tv_id.setText(Alarmlist.get(position).getId().toString());
		//holder.tv_dailyid.setText(Alarmlist.get(position)..getDailyid().toString());
		holder.tv_unionid.setText(Alarmlist.get(position).getUnionid().toString());
		holder.tv_phonenumber.setText(Alarmlist.get(position).getTelone().toString());
		holder.tv_address.setText(Alarmlist.get(position).getAdress().toString());
		holder.tv_dq.setText(Alarmlist.get(position).getOrigin().toString());
		holder.tv_isfire.setText(Alarmlist.get(position).getIsfire().toString());
		holder.tv_firetype.setText(Alarmlist.get(position).getFiretype().toString());
		holder.tv_backstate.setText(Alarmlist.get(position).getBackstate().toString());
		holder.tv_alarmtime.setText(Alarmlist.get(position).getAlarmtime().toString());
		// ����isSelected������checkbox��ѡ��״��
		//holder.cb.setChecked(getIsSelected().get(position));
		return view;
	}
	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}
	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		AlarmManageAdapter.isSelected = isSelected;
	}
	public final class ViewHolder {
		//public TextView xuhao;
		//public CheckBox cb;
		public TextView tv_id;
		public TextView tv_unionid;
		public TextView tv_address;
		public TextView tv_phonenumber;
		public TextView tv_dq;
		public TextView tv_isfire;
		public TextView tv_firetype;
		public TextView tv_backstate;
		public TextView tv_alarmtime;
	}
}
