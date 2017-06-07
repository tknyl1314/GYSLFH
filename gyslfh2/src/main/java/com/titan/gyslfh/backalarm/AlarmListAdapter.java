package com.titan.gyslfh.backalarm;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.titan.gyslfh.alarminfo.AlarmInfoItemNav;
import com.titan.gyslfh.alarminfo.AlarmInfoItemViewModel;
import com.titan.gyslfh.alarminfo.AlarmInfoModel;
import com.titan.newslfh.R;
import com.titan.newslfh.databinding.ItemAlarminfoBinding;

import java.util.List;

/**
 * Created by whs on 2017/5/31
 */

public class AlarmListAdapter <T> extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<T> listobj;
    private AlarmInfoItemNav mAlarmInfoItemNav;
    private Context mContext;


    public AlarmListAdapter(Context context,List<T> listdata ,AlarmInfoItemNav alarmInfoItemNav){
        this.mContext=context;
        this.listobj=listdata;
        this.mAlarmInfoItemNav=alarmInfoItemNav;
        setList(listdata);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemAlarminfoBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
        public ItemAlarminfoBinding getBinding() {
            return binding;
        }
    }






    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //View view=View.inflate(viewGroup.getContext(),R.layout.item_alarminfo,null);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_alarminfo,viewGroup,false);
        return new AlarmListAdapter.ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        //Log.d(TAG, "Element " + position + " set.");
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        AlarmInfoModel.AlarmInfo alarminfo= (AlarmInfoModel.AlarmInfo) listobj.get(position);
        ViewHolder holder= (ViewHolder) viewHolder;
        ///BackAlarmViewModel viewModel=new BackAlarmViewModel(mContext,)
        final AlarmInfoItemViewModel viewmodel = new AlarmInfoItemViewModel(mContext,mAlarmInfoItemNav);
        viewmodel.alarminfo.set(alarminfo);
        holder.getBinding().setViewmodel(viewmodel);
    }




    @Override
    public int getItemCount() {
        return listobj==null?0:listobj.size();
    }

    private void setList(List<T> alarminfos) {
        this.listobj = alarminfos;
        notifyDataSetChanged();
    }

    public void replaceData(List<T> alarminfos) {
        setList(alarminfos);

    }



    public void refresh(List<T> newlawlist) {
        if(listobj!=null){
            listobj.clear();
        }
        listobj.addAll(newlawlist);
        notifyDataSetChanged();
    }



    public void add(List<T> newlawlist){
        listobj.addAll(newlawlist);
        notifyDataSetChanged();
    }


}
