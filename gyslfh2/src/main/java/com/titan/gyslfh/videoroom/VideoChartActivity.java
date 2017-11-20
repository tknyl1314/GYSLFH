package com.titan.gyslfh.videoroom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.titan.BaseActivity;
import com.titan.ViewModelHolder;
import com.titan.newslfh.R;
import com.titan.util.ActivityUtils;
import com.wilddog.video.base.WilddogVideoInitializer;

/**
 * Created by whs on 2017/5/17
 * 视频通话Activity
 */

public  class VideoChartActivity extends BaseActivity {


    private VideoChartViewModel mViewModel;

    private VideoChartFragment mFragment;
    //会议房间号
    private String roomid;


    private  WilddogVideoInitializer initializer;






    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.activity_util);

        mContext=this;
        roomid = getIntent().getStringExtra("roomid");

        mFragment= findOrCreateViewFragment();
        mViewModel= findOrCreateViewModel();

        mFragment.setViewModel(mViewModel);




    }



    @NonNull
    public VideoChartFragment findOrCreateViewFragment() {
        VideoChartFragment tasksFragment =
                (VideoChartFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = VideoChartFragment.newInstance(roomid);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), tasksFragment, R.id.content_frame);
        }
        return tasksFragment;
    }




    @NonNull
    public VideoChartViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<VideoChartViewModel> retainedViewModel =
                (ViewModelHolder<VideoChartViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            //LayerControlViewModel layerControlViewModel=new LayerControlViewModel(getApplicationContext(),this);
            // There is no ViewModel yet, create it.
            VideoChartViewModel viewModel = new VideoChartViewModel(getApplicationContext(), mFragment);
            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    VIEWMODEL_TAG);
            return viewModel;
        }
    }


}
