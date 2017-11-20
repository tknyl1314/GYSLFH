package com.titan.gyslfh.videoroom;
import android.Manifest;
import android.content.Intent;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.titan.newslfh.R;
import com.titan.newslfh.databinding.FragVideoroomBinding;
import com.titan.util.SnackbarUtils;
import com.wilddog.wilddogauth.WilddogAuth;
import com.wilddog.wilddogauth.core.Task;
import com.wilddog.wilddogauth.core.listener.OnCompleteListener;
import com.wilddog.wilddogauth.core.result.AuthResult;


import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;


/**
 * Created by whs on 2017/4/28
 * 视频会议界面
 */
@RuntimePermissions
public class VideoRoomFragment extends Fragment implements VideoRoom  {

    private VideoRoomViewModel mViewModel;

    private FragVideoroomBinding mBinding;

    //private boolean islogining=false;

    //所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };



    public VideoRoomFragment() {
        // Requires empty public constructor
    }
    public static VideoRoomFragment newInstance() {
        return  new VideoRoomFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VideoRoomFragmentPermissionsDispatcher.initPermissionWithCheck(this);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        VideoRoomFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragVideoroomBinding.inflate(inflater, container, false);
        //mMainFragBinding.setView(this);
        mBinding.setViewmodel(mViewModel);
        return mBinding.getRoot();
    }

    public void setViewModel(VideoRoomViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupSnackbar();


    }

    /**
     * 获取权限
     */
    @NeedsPermission({Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA})
    public void initPermission() {

    }
    /**
     * 未获取权限
     */
    @OnPermissionDenied({Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA})
    void showDeniedForAddRoom() {
        mViewModel.snackbarText.set(getActivity().getString(R.string.camera_or_audio_permission_denied));
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







    @NeedsPermission({ Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA})
    public void onlogin(final String strRoomId) {
        WilddogAuth auth = WilddogAuth.getInstance();
        auth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> var1) {
                if (var1.isSuccessful()) {
                    //islogining = false;
                    Intent intent = new Intent(getActivity(), RoomActivity.class);
                    intent.putExtra("roomid", strRoomId);
                    startActivity(intent);
                } else {
                    //islogining = false;
                    mViewModel.snackbarText.set("登录失败,请查看日志寻找失败原因"+var1.getException().getMessage());
                    //Toast.makeText(LoginActivity.this, "登录失败,请查看日志寻找失败原因", Toast.LENGTH_SHORT).show();
                    Log.e("error", var1.getException().getMessage());
                }
            }
        });
    }


    @Override
    public void loginWithAnonymous(String strRoomId) {
        VideoRoomFragmentPermissionsDispatcher.onloginWithCheck(this,strRoomId);
    }
}
