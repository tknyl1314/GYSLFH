package com.titan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by whs on 2017/5/27
 */

public class BaseFragment extends Fragment {
    private BaseViewModel mViewModel;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }
    /*private void setupSnackbar() {
        mMainViewModel.snackbarText.addOnPropertyChangedCallback(
                new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable observable, int i) {
                        SnackbarUtils.showSnackbar(getView(), mMainViewModel.getSnackbarText());

                    }
                });
    }*/
}
