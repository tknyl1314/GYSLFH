package com.titan.gyslfh.videoroom;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.titan.newslfh.databinding.FragVideoroomBinding;
import com.titan.util.PermissionUtil;


/**
 * Created by whs on 2017/4/28
 * 视频会议界面
 */
//@RuntimePermissions
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
        //VideoRoomFragmentPermissionsDispatcher.initPermissionWithCheck(this);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PermissionUtil.videoRoomRequestCode:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //initData()
                } else {
                    this.requestPermissions(PermissionUtil.videoRoomPermissions,PermissionUtil.videoRoomRequestCode);
                }
                break;
        }
        //VideoRoomFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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
        if(PermissionUtil.checkVideoRoomPermission(getActivity())){
            //initData()
            //onlogin();
        }else{
            this.requestPermissions(PermissionUtil.videoRoomPermissions,PermissionUtil.videoRoomRequestCode);
        }
    }

    /**
     * 获取权限
     */
    /*@NeedsPermission({Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA})
    public void initPermission() {

    }*/
    /**
     * 未获取权限
     */
   /* @OnPermissionDenied({Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA})
    void showDeniedForAddRoom() {
        mViewModel.snackbarText.set(getActivity().getString(R.string.camera_or_audio_permission_denied));
    }*/

    private void setupSnackbar() {
        mViewModel.snackbarText.addOnPropertyChangedCallback(
                new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable observable, int i) {
                        //SnackbarUtils.showSnackbar(getView(), mViewModel.getSnackbarText());
                        //Snackbar.make(getView(), mViewModel.getSnackbarText(), Snackbar.LENGTH_SHORT).show();
                    }
                });
    }







    //@NeedsPermission({ Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA})
    public void onlogin(final String roomName) {
        /*WilddogAuth auth = WilddogAuth.getInstance();
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
        });*/

        /*AVChatManager.getInstance().createRoom(roomName, roomName, new AVChatCallback<AVChatChannelInfo>() {
            @Override
            public void onSuccess(AVChatChannelInfo avChatChannelInfo) {
                Intent intent = new Intent(getActivity(), RoomActivity.class);
                //intent.putExtra("roomid", strRoomId);
                startActivity(intent);
            }

            @Override
            public void onFailed(int code) {
                if (code== ResponseCode.RES_EEXIST) {
                    // 417表示该频道已经存在
                    //LogUtil.e(TAG, "create room 417, enter room");
                    Toast.makeText(getActivity(), "创建的房间名：" + roomName, Toast.LENGTH_SHORT).show();
                    //isStartLive = true;
                } else {
                    //isStartLiving = false;
                    //startBtn.setText(R.string.live_start);
                    //LogUtil.e(TAG, "create room failed, code:" + i);
                    showToast(1,getActivity().getString(R.string.error_im_creatroom));

                    //Toast.makeText(LiveActivity.this, "create room failed, code:" + i, Toast.LENGTH_SHORT).show();
                }
                //mViewModel.snackbarText.set(getActivity().getResources().getString(R.string.error_im_creatroom));
            }

            @Override
            public void onException(Throwable exception) {
                mViewModel.snackbarText.set(getActivity().getResources().getString(R.string.error_im_creatroom));

            }
        });*/
    }


    @Override
    public void loginWithAnonymous(String strRoomId) {
        if(PermissionUtil.checkVideoRoomPermission(getActivity())){
            onlogin(strRoomId);
        }else{
            this.requestPermissions(PermissionUtil.videoRoomPermissions,PermissionUtil.videoRoomRequestCode);
        }
       // VideoRoomFragmentPermissionsDispatcher.onloginWithCheck(this,strRoomId);
    }

    @Override
    public void showToast(int type, String msg) {
        Toast.makeText(getActivity(),msg,type).show();
    }
}
