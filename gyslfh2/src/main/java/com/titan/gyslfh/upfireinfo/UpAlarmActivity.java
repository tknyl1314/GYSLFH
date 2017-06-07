package com.titan.gyslfh.upfireinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.titan.BaseActivity;
import com.titan.BaseViewModel;
import com.titan.Injection;
import com.titan.ViewModelHolder;
import com.titan.newslfh.R;
import com.titan.util.ActivityUtils;

/**
 * 火情上报
 */
public class UpAlarmActivity extends BaseActivity  {



    private UpAlarmFragment mUpAlarmFragment;

    private UpAlarmViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upalarm);
        mUpAlarmFragment= (UpAlarmFragment) findOrCreateViewFragment();
        mViewModel= (UpAlarmViewModel) findOrCreateViewModel();
        mUpAlarmFragment.setViewModel(mViewModel);
        //mContext=this;

    }
    @Override
    public Fragment findOrCreateViewFragment() {
        UpAlarmFragment tasksFragment =
                (UpAlarmFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = UpAlarmFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), tasksFragment, R.id.content_frame);
        }
        return tasksFragment;
    }

    @Override
    public BaseViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<UpAlarmViewModel> retainedViewModel =
                (ViewModelHolder<UpAlarmViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            //LayerControlViewModel layerControlViewModel=new LayerControlViewModel(getApplicationContext(),this);
            // There is no ViewModel yet, create it.
            UpAlarmViewModel viewModel = new UpAlarmViewModel(getApplicationContext(),mUpAlarmFragment, Injection.provideDataRepository(mContext));
            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    VIEWMODEL_TAG);
            return viewModel;
        }
    }



}
