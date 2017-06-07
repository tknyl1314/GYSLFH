package com.titan.gyslfh.layercontrol;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.LayerList;
import com.titan.Injection;
import com.titan.newslfh.R;
import com.titan.newslfh.databinding.ItemLayerBinding;

/**
 * Created by whs on 2017/5/16
 */

public class LayersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayerList mLayers;
    private ILayerControl mIlayerControl;
    //private DataRepository mDataRepository;

    public LayersAdapter(Context mContext, LayerList layers,ILayerControl iLayerControl) {
        this.mContext = mContext;
        ///this.mLayers = layers;

        //int di=mLayers.size();

        this.mIlayerControl=iLayerControl;
        setList(layers);
        //this.mILayerItem=iLayerItem;
    }

    private void setList(LayerList layerlist) {
        this.mLayers = layerlist;
        notifyDataSetChanged();
    }

    public void replaceData(LayerList layerlist) {
        setList(layerlist);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemLayerBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
        public ItemLayerBinding getBinding() {
            return binding;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.item_layer,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Layer layer=mLayers.get(position);
        final LayerItemViewModel viewmodel = new LayerItemViewModel(mContext,mIlayerControl, Injection.provideDataRepository(mContext),mLayers);
        ViewHolder viewHolder= (ViewHolder) holder;
        //String layername=layer.getName();
        viewmodel.layerindex.set(position);
        viewmodel.ischeck.set(layer.isVisible());
        viewmodel.mLayer.set(layer);
        viewHolder.getBinding().setViewmodel(viewmodel);

    }

    @Override
    public int getItemCount() {
        return mLayers==null?0:mLayers.size();
    }
}
