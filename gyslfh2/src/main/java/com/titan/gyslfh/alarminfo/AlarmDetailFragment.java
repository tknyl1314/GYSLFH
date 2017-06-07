package com.titan.gyslfh.alarminfo;


import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.titan.newslfh.R;
import com.titan.newslfh.databinding.FragAlarmdetailBinding;
import com.titan.util.SnackbarUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmDetailFragment extends Fragment implements AlarmDetailInterface{
    private AlarmDetailViewModel mViewModel;

    private FragAlarmdetailBinding mDataBinding;
    private static final String ARG_PARAM1 = "param1";

    private String alarmid;

    public AlarmDetailFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            alarmid = getArguments().getString(ARG_PARAM1);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataBinding=DataBindingUtil.inflate(inflater,R.layout.frag_alarmdetail,container,false);
        mDataBinding.setViewmodel(mViewModel);
        // Inflate the layout for this fragment
        return mDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupSnackbar();
    }

    public void setViewModel(AlarmDetailViewModel mViewModel) {
        this.mViewModel=mViewModel;
    }

    public static AlarmDetailFragment newInstance(String alarmid) {
        AlarmDetailFragment fragment=new AlarmDetailFragment();
        Bundle bundle=new Bundle();
        bundle.putString(ARG_PARAM1,alarmid);
        fragment.setArguments(bundle);
        return  fragment;
    }
    public static AlarmDetailFragment newInstance() {
        AlarmDetailFragment fragment = new AlarmDetailFragment();
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start(alarmid);
    }

    private void setupSnackbar() {
        mViewModel.snackbarText.addOnPropertyChangedCallback(
                new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable observable, int i) {
                        SnackbarUtils.showSnackbar(getView(), mViewModel.getSnackbarText());
                    }
                });
    }
}
