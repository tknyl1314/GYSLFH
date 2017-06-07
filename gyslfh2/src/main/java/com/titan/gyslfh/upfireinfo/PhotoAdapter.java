package com.titan.gyslfh.upfireinfo;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.titan.gyslfh.AlarmInfoInterface;
import com.titan.model.Image;
import com.titan.newslfh.R;
import com.titan.newslfh.databinding.ItemImgBinding;

import java.io.File;
import java.util.List;

/**
 * Created by whs on 2017/5/16
 */

public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Image> imageList;
    private AlarmInfoInterface alarmInfoInterface;

    public PhotoAdapter(Context mContext, List<Image> imageList, AlarmInfoInterface alarmInfoInterface) {
        this.mContext = mContext;
        this.alarmInfoInterface=alarmInfoInterface;
        setList(imageList);
    }

    private void setList(List<Image> imageList) {
        this.imageList = imageList;
        notifyDataSetChanged();
    }

    public void replaceData(List<Image> imageList) {
        setList(imageList);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemImgBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
        public ItemImgBinding getBinding() {
            return binding;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.item_img,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Image image=imageList.get(position);
        final ImgItemViewModel viewmodel = new ImgItemViewModel(mContext,alarmInfoInterface);
        final ViewHolder viewHolder= (ViewHolder) holder;
        viewmodel.imgpath.set(image.getPath());
        viewmodel.position.set(position);
        viewHolder.getBinding().sdvImg.setImageURI(Uri.fromFile(new File(image.getPath())));
        viewHolder.getBinding().setViewmodel(viewmodel);

    }

    @Override
    public int getItemCount() {
        return imageList==null?0:imageList.size();
    }

    public void refresh() {
        /*if(imageList!=null){
            imageList.clear();
        }
        imageList.addAll(imgs);*/

        notifyDataSetChanged();
    }
}
