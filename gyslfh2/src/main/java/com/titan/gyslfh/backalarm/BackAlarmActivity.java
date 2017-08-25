package com.titan.gyslfh.backalarm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.titan.BaseActivity;
import com.titan.BaseViewModel;
import com.titan.Injection;
import com.titan.ViewModelHolder;
import com.titan.gyslfh.TitanApplication;
import com.titan.gyslfh.alarminfo.AlarmDetailFragment;
import com.titan.gyslfh.alarminfo.AlarmDetailViewModel;
import com.titan.newslfh.R;
import com.titan.util.ActivityUtils;

public class BackAlarmActivity extends BaseActivity {
    private AlarmListViewModel mViewModel;
    private BackAlarmListFragment mFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_info);
        //TitanApplication.getInstance().addActivity(this);
        mContext=this;
        mFragment = (BackAlarmListFragment) findOrCreateViewFragment();
        mViewModel = (AlarmListViewModel) findOrCreateViewModel();
        // Link View and ViewModel
        mFragment.setViewModel(mViewModel);
    }
    @Override
    public Fragment findOrCreateViewFragment() {
        BackAlarmListFragment alarmInfoFragment =
                (BackAlarmListFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (alarmInfoFragment == null) {
            // Create the fragment
            alarmInfoFragment = BackAlarmListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), alarmInfoFragment, R.id.content_frame);
        }
        return alarmInfoFragment;
    }

    @Override
    public BaseViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<AlarmListViewModel> retainedViewModel =
                (ViewModelHolder<AlarmListViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            // There is no ViewModel yet, create it.
            AlarmListViewModel viewModel = new AlarmListViewModel(mContext,  mFragment,Injection.provideDataRepository(mContext));
            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    TAG);
            return viewModel;
        }
    }

    public void openAlarmInfoDetails(String id) {
        //getFragmentManager().
        /*Toast.makeText(mContext, "id:"+id, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(AlarmInfoActivity.this,AlarmInfoDetailActivity.class);
        intent.putExtra("alramid",id);
        startActivity(intent);*/
        addFragment(id);
    }

    private void addFragment(String alarmid) {
        FragmentManager manager =getSupportFragmentManager();
        AlarmDetailFragment alarmDetailFragment =  AlarmDetailFragment.newInstance(alarmid);
        AlarmDetailViewModel viewModel= new AlarmDetailViewModel(mContext,alarmDetailFragment, Injection.provideDataRepository(mContext));
        alarmDetailFragment.setViewModel(viewModel);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.content_frame, alarmDetailFragment);
        transaction.commit();
    }



}
