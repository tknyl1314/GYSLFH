package com.titan.gyslfh.alarminfo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.titan.ViewModelHolder;
import com.titan.gyslfh.TitanApplication;
import com.titan.newslfh.R;
import com.titan.util.ActivityUtils;

public class AlarmInfoActivity extends AppCompatActivity implements  IAlarmInfo{
    Context mContext;
    private AlarmInfoViewModel mViewModel;
    public static final String ALARMINFO_VIEWMODEL_TAG = "ALARMINFO_VIEWMODEL_TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_info);
        TitanApplication.getInstance().addActivity(this);
        mContext=this;

        //setupNavigationDrawer();

        AlarmInfoFragment alarmInfoFragment = findOrCreateViewFragment();

        mViewModel = findOrCreateViewModel();

        // Link View and ViewModel
        alarmInfoFragment.setViewModel(mViewModel);
    }

    private AlarmInfoFragment findOrCreateViewFragment() {
        AlarmInfoFragment alarmInfoFragment =
                (AlarmInfoFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (alarmInfoFragment == null) {
            // Create the fragment
            alarmInfoFragment = AlarmInfoFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), alarmInfoFragment, R.id.content_frame);
        }
        return alarmInfoFragment;
    }

    @NonNull
    private AlarmInfoViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<AlarmInfoViewModel> retainedViewModel =
                (ViewModelHolder<AlarmInfoViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(ALARMINFO_VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            // There is no ViewModel yet, create it.
            AlarmInfoViewModel viewModel = new AlarmInfoViewModel(getApplicationContext(), this);
            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    ALARMINFO_VIEWMODEL_TAG);
            return viewModel;
        }
    }

}
