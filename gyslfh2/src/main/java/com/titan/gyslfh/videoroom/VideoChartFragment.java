package com.titan.gyslfh.videoroom;

import android.databinding.Observable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.titan.newslfh.R;
import com.titan.newslfh.databinding.FragChartBinding;
import com.titan.util.SnackbarUtils;
import com.wilddog.video.base.LocalStream;
import com.wilddog.video.base.LocalStreamOptions;
import com.wilddog.video.base.WilddogVideoError;
import com.wilddog.video.base.WilddogVideoInitializer;
import com.wilddog.video.base.WilddogVideoView;
import com.wilddog.video.base.util.LogUtil;
import com.wilddog.video.base.util.logging.Logger;
import com.wilddog.video.room.CompleteListener;
import com.wilddog.video.room.RoomStream;
import com.wilddog.video.room.WilddogRoom;
import com.wilddog.wilddogauth.WilddogAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created by whs on 2017/4/28
 * 视频会议界面
 */
public class VideoChartFragment extends Fragment implements VideoChart  {

    private VideoChartViewModel mViewModel;

    private FragChartBinding mBinding;
    //videoid
    public static final String WILDDOG_VIDEO_ID = "wd0634562361hwiiyl";

    private WilddogVideoInitializer initializer;
    private WilddogRoom room;

    private String roomId;

    private LocalStream localStream;
    private boolean isAudioEnable = true;
    private boolean isVideoEnable = true;

    private WilddogVideoView localView;
    private WilddogVideoView remoteView1;
    private WilddogVideoView remoteView2;
    private WilddogVideoView remoteView3;
    private WilddogVideoView remoteView4;
    private WilddogVideoView remoteView5;

    private Map<Long, StreamHolder> mPartiViewMap = new TreeMap<>();
    private List<WilddogVideoView> remoteVideoViews = new ArrayList<>();
    private List<StreamHolder> remoteStreamHolders = new ArrayList<>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            deteachAll();
            showRemoteViews();
        }
    };







    public VideoChartFragment() {
        // Requires empty public constructor
    }

    public static VideoChartFragment newInstance(String roomid) {
        VideoChartFragment fragment = new VideoChartFragment();
        Bundle args = new Bundle();
        args.putString("roomid", roomid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            roomId = getArguments().getString("roomid");
        }
        initView();

    }

    private void initView() {
        localView = (WilddogVideoView) getActivity().findViewById(R.id.wvv_local);
        remoteView1 = (WilddogVideoView) getActivity().findViewById(R.id.wvv_remote1);
        remoteView2 = (WilddogVideoView) getActivity().findViewById(R.id.wvv_remote2);
        remoteView3 = (WilddogVideoView) getActivity().findViewById(R.id.wvv_remote3);
        remoteView4 = (WilddogVideoView) getActivity().findViewById(R.id.wvv_remote4);
        remoteView5 = (WilddogVideoView) getActivity().findViewById(R.id.wvv_remote5);
        remoteVideoViews.add(remoteView1);
        remoteVideoViews.add(remoteView2);
        remoteVideoViews.add(remoteView3);
        remoteVideoViews.add(remoteView4);
        remoteVideoViews.add(remoteView5);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //VideoRoomFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragChartBinding.inflate(inflater, container, false);
        mBinding.setViewmodel(mViewModel);
        mViewModel.roomid.set(roomId);
        return mBinding.getRoot();
    }


    public void setViewModel(VideoChartViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupSnackbar();
        initRoomSDK();
        createLocalStream();
        joinRoom();

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

    /**
     *
     */
    private void initRoomSDK() {
        LogUtil.setLogLevel(Logger.Level.DEBUG);
        WilddogVideoInitializer.initialize(getActivity(), WILDDOG_VIDEO_ID, WilddogAuth.getInstance().getCurrentUser().getToken(false).getResult().getToken());
        initializer =  WilddogVideoInitializer.getInstance();
        initializer.addTokenListener(new WilddogVideoInitializer.TokenListener() {
            @Override
            public void onTokenChanged(String s) {

            }
        });
    }
    /**
     * 将本地媒体流绑定到WilddogVideoView中
     */
    private void createLocalStream() {
        LocalStreamOptions options = new LocalStreamOptions.Builder().build();
        localStream = LocalStream.create(options);
        localStream.enableAudio(isAudioEnable);
        localStream.enableVideo(isVideoEnable);
        localStream.attach(localView);

    }

    private void joinRoom() {
        room = new WilddogRoom(roomId, new WilddogRoom.Listener() {
            @Override
            public void onConnected(WilddogRoom wilddogRoom) {
                //Toast.makeText(RoomActivity.this,"已经连接上服务器", Toast.LENGTH_SHORT).show();
                mViewModel.snackbarText.set("已经连接上服务器");
                room.publish(localStream, new CompleteListener() {
                    @Override
                    public void onComplete(WilddogVideoError wilddogVideoError) {
                        if(wilddogVideoError!=null){
                            //失败
                            mViewModel.snackbarText.set("推送流失败");

                            //Toast.makeText(RoomActivity.this,"推送流失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onDisconnected(WilddogRoom wilddogRoom) {
                mViewModel.snackbarText.set("服务器连接断开");
            }

            @Override
            public void onStreamAdded(WilddogRoom wilddogRoom, RoomStream roomStream) {
                //订阅流 如果超过6个就补订阅流
                room.subscribe(roomStream);
            }

            @Override
            public void onStreamRemoved(WilddogRoom wilddogRoom, RoomStream roomStream) {

                //具体流 超过六个的退出可能不包含,所以移除时候判断是否包含
                if(mPartiViewMap.containsKey(roomStream.getStreamId())){
                    remoteStreamHolders.remove(mPartiViewMap.remove(roomStream.getStreamId())  );
                    handler.sendEmptyMessage(0);}
            }

            @Override
            public void onStreamReceived(WilddogRoom wilddogRoom, RoomStream roomStream) {
                // 在控件中显示
                if(mPartiViewMap.size()>=5){return;}
                StreamHolder holder = new StreamHolder(System.currentTimeMillis(),roomStream);
                mPartiViewMap.put(roomStream.getStreamId(),holder);
                remoteStreamHolders.add(holder);
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onStreamChanged(WilddogRoom wilddogRoom, RoomStream roomStream) {
                // 混流使用
            }

            @Override
            public void onError(WilddogRoom wilddogRoom, WilddogVideoError wilddogVideoError) {
                mViewModel.snackbarText.set("发生错误,请产看日志,错误信息:"+wilddogVideoError.getMessage());
                //Toast.makeText(RoomActivity.this,"发生错误,请产看日志", Toast.LENGTH_SHORT).show();
                Log.e("error","错误码:"+wilddogVideoError.getErrCode()+",错误信息:"+wilddogVideoError.getMessage());
            }
        });
        room.connect();
    }

    /**
     * 释放流
     */
    private void deteachAll(){
        for(StreamHolder streamHolder:remoteStreamHolders){
            streamHolder.getStream().detach();
        }
    }

    /**
     * 显示
     */
    private void showRemoteViews(){
        for(int i = 0;i<remoteStreamHolders.size();i++){
            remoteStreamHolders.get(i).getStream().attach(remoteVideoViews.get(i));
        }
    }

    /**
     * 离开
     */

    public void leaveRoom(){
        if(room!=null){
            room.disconnect();
            room=null;
        }
        getActivity().finish();
    }

    @Override
    public void switchCamera() {
        if(localStream!=null){
            localStream.switchCamera();
        }
    }

    @Override
    public void switchAudio(boolean isopen) {
        if(localStream!=null){
            isAudioEnable = !isAudioEnable;
            localStream.enableAudio(isopen);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(!localStream.isClosed()){
            localStream.close();
        }
    }




}
