package com.titan.gyslfh.alarminfo;

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
 * Created by whs on 2017/5/25
 */

public class AlarmInfoDetailActivity extends BaseActivity {

    //private  AlarmDetailViewModel mViewModel;
    private AlarmDetailFragment mFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmdetail);
        mContext=this;
        mFragment= (AlarmDetailFragment) findOrCreateViewFragment();
        mViewModel =  findOrCreateViewModel();
        // Link View and ViewModel
        mFragment.setViewModel((AlarmDetailViewModel) mViewModel);
    }


    @Override
    public Fragment findOrCreateViewFragment() {
        AlarmDetailFragment tasksFragment =
                (AlarmDetailFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = AlarmDetailFragment.newInstance();
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
        ViewModelHolder<AlarmDetailViewModel> retainedViewModel =
                (ViewModelHolder<AlarmDetailViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            //LayerControlViewModel layerControlViewModel=new LayerControlViewModel(getApplicationContext(),this);
            // There is no ViewModel yet, create it.
            AlarmDetailViewModel viewModel = new AlarmDetailViewModel(getApplicationContext(), mFragment, Injection.provideDataRepository(mContext));
            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    VIEWMODEL_TAG);
            return viewModel;
        }
    }
}
