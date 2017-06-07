package com.titan.gyslfh.layercontrol;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.titan.gyslfh.main.MainFragment;
import com.titan.newslfh.R;
import com.titan.newslfh.databinding.LayercontrolFragBinding;

/**
 * Created by WHS on 2017/1/4
 * 图层控制
 */

public class LayerControlFragment extends DialogFragment {



    private LayercontrolFragBinding mViewDataBinding;
    private LayerControlViewModel mLayerControlViewModel;

    private  static LayerControlFragment Singleton;

    LayersAdapter mAdapter;
    /**
     * Create a new instance of
     */
    public static LayerControlFragment newInstance() {
        return new LayerControlFragment();
    }

    public static LayerControlFragment getInstance(){
        if(Singleton==null){
            Singleton=new LayerControlFragment();
        }
        return Singleton;

    }

    public void setViewModel(LayerControlViewModel viewModel) {
        mLayerControlViewModel = viewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewDataBinding=DataBindingUtil.inflate(inflater, R.layout.layercontrol_frag,container,true);
        mViewDataBinding.setViewmodel(mLayerControlViewModel);
        return mViewDataBinding.getRoot();
    }


    /**
     * 设置dialog弹出来的位置
     */
    private void setDialogLocation() {
        //获取通知栏高度
        Rect rect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        getDialog().getWindow().setGravity(Gravity.RIGHT | Gravity.TOP);
        /**
         *   int[] ll=new int[2];
             MapFragment.iv_more.getLocationInWindow(ll);
         *   如果iv_more在左边，也可以这样做 lp.x=ll[0],因为我不管设置Gravity在左或右边，原点一直在右边
         */
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        //因为iv_more按钮距离右边是16dp
        //lp.x = ConversionUtil.dip2px(getActivity(), 16);
        //设置n - rect.top + MapFragment.iv_more.getHeight() 那么dialogment的高度就和iv_more相同，我在往下移动10dp
        //lp.y = MapFragment.location_iv_more[1] - rect.top + MapFragment.iv_more.getHeight() + ConversionUtil.dip2px(getActivity(), 10);
        getDialog().getWindow().setAttributes(lp);
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        //此处可以设置Dialog的style等等
        super.onCreate(savedInstanceState);

        /*DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
        getDialog().getWindow().setLayout( dm.widthPixels, getDialog().getWindow().getAttributes()..height );*/

        //this.getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //若想无法直接点击外部取消dialog 可设置为false

        setCancelable(false);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogFragment);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        intiRadioGroup();
        intiRecyclerView();
    }

    private void intiRadioGroup() {
       //Drawable d= mViewDataBinding.rbMapfirerisk.getCompoundDrawables()[1];
       // d.set
    }

    /**
     *
     */
    private void intiRecyclerView() {
        mAdapter=new LayersAdapter(getActivity(), MainFragment.mMap.getOperationalLayers(),(ILayerControl) getActivity());
        mViewDataBinding.rvLayers.setAdapter(mAdapter);
        mViewDataBinding.rvLayers.setLayoutManager(new LinearLayoutManager(getActivity()));


    }



    @Override
    public void onStart() {
        //参数在onCreate中设置无效果
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER_HORIZONTAL);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置dialog背景透明
        }
    }





    @Override
    public void onResume() {

        super.onResume();
    }


}
