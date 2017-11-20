package com.titan.gyslfh.videoroom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.titan.BaseActivity;
import com.titan.ViewModelHolder;
import com.titan.newslfh.R;
import com.titan.util.ActivityUtils;

/**
 * Created by whs on 2017/5/17
 * 视频会议Activity
 */

public  class VideoRoomActivity extends BaseActivity {






    private VideoRoomViewModel mViewModel;

    private VideoRoomFragment mFragment;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        setContentView(R.layout.activity_videoroom);
        mContext=this;

        mFragment= findOrCreateViewFragment();
        mViewModel= findOrCreateViewModel();

        mFragment.setViewModel(mViewModel);




    }

    @NonNull
    public VideoRoomFragment findOrCreateViewFragment() {
        VideoRoomFragment tasksFragment =
                (VideoRoomFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = VideoRoomFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), tasksFragment, R.id.content_frame);
        }
        return tasksFragment;
    }




    @NonNull
    public VideoRoomViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<VideoRoomViewModel> retainedViewModel =
                (ViewModelHolder<VideoRoomViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            //LayerControlViewModel layerControlViewModel=new LayerControlViewModel(getApplicationContext(),this);
            // There is no ViewModel yet, create it.
            VideoRoomViewModel viewModel = new VideoRoomViewModel(getApplicationContext(), mFragment);
            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    VIEWMODEL_TAG);
            return viewModel;
        }
    }
}
