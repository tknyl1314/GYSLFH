package com.titan.gyslfh.alarminfo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.titan.Injection;
import com.titan.ViewModelHolder;
import com.titan.gyslfh.TitanApplication;
import com.titan.newslfh.R;
import com.titan.util.ActivityUtils;

public class AlarmInfoActivity extends AppCompatActivity {
    Context mContext;
    private AlarmInfoViewModel mViewModel;

    private AlarmInfoFragment mFragment;
    public static final String ALARMINFO_VIEWMODEL_TAG = "ALARMINFO_VIEWMODEL_TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_info);
        TitanApplication.getInstance().addActivity(this);
        mContext=this;

        //setupNavigationDrawer();

         mFragment = findOrCreateViewFragment();


        mViewModel = findOrCreateViewModel();

        // Link View and ViewModel
        mFragment.setViewModel(mViewModel);
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
            AlarmInfoViewModel viewModel = new AlarmInfoViewModel(getApplicationContext(), mFragment,Injection.provideDataRepository(mContext));
            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    ALARMINFO_VIEWMODEL_TAG);
            return viewModel;
        }
    }

    /*@Override
    public void openAlarmInfoDetails(String id) {
        //getFragmentManager().
        *//*Toast.makeText(mContext, "id:"+id, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(AlarmInfoActivity.this,AlarmInfoDetailActivity.class);
        intent.putExtra("alramid",id);
        startActivity(intent);*//*
        replaceFragment(id);
    }

    @Override
    public void openAlarmInfoDetails(AlarmInfoModel.AlarmInfo alarmInfo) {
        replaceFragment(alarmInfo.getID());
    }*/

    private void replaceFragment(String alarmid) {
        FragmentManager manager =getSupportFragmentManager();
        AlarmDetailFragment alarmDetailFragment =  AlarmDetailFragment.newInstance(alarmid);
        AlarmDetailViewModel viewModel= new AlarmDetailViewModel(mContext,alarmDetailFragment, Injection.provideDataRepository(mContext));
        alarmDetailFragment.setViewModel(viewModel);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_frame, alarmDetailFragment);
        transaction.addToBackStack(null);
        //设置过度动画
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }



}
