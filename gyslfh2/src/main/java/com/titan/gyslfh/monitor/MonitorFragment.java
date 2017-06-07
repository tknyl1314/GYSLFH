package com.titan.gyslfh.monitor;

import android.content.Context;
import android.databinding.Observable;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.PTZCommand;
import com.titan.newslfh.databinding.FragMonitorBinding;
import com.titan.util.SnackbarUtils;

import org.MediaPlayer.PlayM4.Player;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MonitorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MonitorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonitorFragment extends Fragment implements MonitorInterface, SurfaceHolder.Callback,View.OnTouchListener{

    private static final String TAG ="MONITOR" ;
    private MonitorViewModel mViewModel;
    //手势
    private GestureDetector mGestureDetector;

    private FragMonitorBinding mDataBinding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MonitorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonitorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MonitorFragment newInstance(String param1, String param2) {
        MonitorFragment fragment = new MonitorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if (!HikVisionUtil.initSdk())
        {
            getActivity().finish();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataBinding=FragMonitorBinding.inflate(inflater,container,false);
        mDataBinding.setViewmodel(mViewModel);
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.frag_monitor, container, false);
        return mDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupSnackbar();
        initView();
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

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start();
    }

    // GUI init
    private boolean initView()
    {
        mGestureDetector=new GestureDetector(getActivity(),new simpleGestureListener());
        //mMoveDetector 	= new MoveGestureDetector(getApplicationContext(), new MoveListener());
        //findViews();
        mDataBinding.svPlayer.getHolder().addCallback(this);
        mDataBinding.svPlayer.setOnTouchListener(this);
        //setListeners();
        return true;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static MonitorFragment newInstance() {
        return new MonitorFragment();
    }


    public void setViewModel(MonitorViewModel mViewModel) {
        this.mViewModel=mViewModel;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        holder.setFormat(PixelFormat.TRANSLUCENT);
        Log.i(TAG, "surface is created" + HikVisionUtil.m_iPort);
        if (-1 == HikVisionUtil.m_iPort)
        {
            return;
        }
        Surface surface = holder.getSurface();
        if (true == surface.isValid()) {
            if (false == Player.getInstance().setVideoWindow(HikVisionUtil.m_iPort, 0, holder)) {
                Log.e(TAG, "Player setVideoWindow failed!");
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        Log.i(TAG, "Player setVideoWindow release!" + HikVisionUtil.m_iPort);
        if (-1 == HikVisionUtil.m_iPort)
        {
            return;
        }
        if (true == holder.getSurface().isValid()) {
            if (false == Player.getInstance().setVideoWindow(HikVisionUtil.m_iPort, 0, null)) {
                Log.e(TAG, "Player setVideoWindow failed!");
            }
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        // return super.onTouchEvent(event);
        return true;
    }

    @Override
    public void processRealData(int i, int iDataType, byte[] pDataBuffer, int iDataSize, int iStreamMode) {
        if (HCNetSDK.NET_DVR_SYSHEAD == iDataType)
        {

            int m_iPort = Player.getInstance().getPort();
            if (m_iPort == -1)
            {
                Log.e(TAG, "getPort is failed with: "
                        + Player.getInstance().getLastError(m_iPort));
                return;
            }
            Log.i(TAG, "getPort succ with: " + m_iPort);
            if (iDataSize > 0)
            {
                if (!Player.getInstance().setStreamOpenMode(m_iPort,
                        iStreamMode)) // set stream mode
                {
                    Log.e(TAG, "setStreamOpenMode failed");
                    return;
                }
                if (!Player.getInstance().openStream(m_iPort, pDataBuffer,
                        iDataSize, 2 * 1024 * 1024)) // open stream
                {
                    Log.e(TAG, "openStream failed");
                    return;
                }
                if (!Player.getInstance().play(m_iPort,
                        mDataBinding.svPlayer.getHolder()))
                {
                    Log.e(TAG, "play failed");
                    return;
                }
                // play Audio
					/*
					 * if(!Player.getInstance().playSound(m_iPort)) { Log.e(TAG,
					 * "playSound failed with error code:" +
					 * Player.getInstance().getLastError(m_iPort)); return; }
					 */
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class simpleGestureListener extends
            GestureDetector.SimpleOnGestureListener {

        /*****OnGestureListener的函数*****/

        final int FLING_MIN_DISTANCE = 100, FLING_MIN_VELOCITY = 200;

        // 触发条件 ：
        // X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒

        // 参数解释：
        // e1：第1个ACTION_DOWN MotionEvent
        // e2：最后一个ACTION_MOVE MotionEvent
        // velocityX：X轴上的移动速度，像素/秒
        // velocityY：Y轴上的移动速度，像素/秒
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {

            float xlen = e1.getX() - e2.getX();
            float ylen = e2.getY() - e1.getY();
            if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                //pt_goleft();
                HikVisionUtil.PTZControl(PTZCommand.PAN_LEFT,0);
                try {
                    float t1 = xlen / Math.abs(velocityX);
                    float t2 = xlen % Math.abs(velocityX);
                    long time = (long) (xlen / Math.abs(velocityX) + xlen % Math.abs(velocityX)) * 1000;
                    if (time < 0 || time > 10000) {
                        HikVisionUtil.PTZControl(PTZCommand.PAN_LEFT,1);


                    } else {

                        Thread.sleep(time);
                        HikVisionUtil.PTZControl(PTZCommand.PAN_LEFT,1);

                    }

                } catch (InterruptedException e) {
                    HikVisionUtil.PTZControl(PTZCommand.PAN_LEFT,1);
                    e.printStackTrace();
                }
                Log.i("MyGesture", "Fling left");
                Toast.makeText(getActivity(), "向左", Toast.LENGTH_SHORT).show();

            } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
                    && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                HikVisionUtil.PTZControl(PTZCommand.PAN_RIGHT,0);
                try {
                    long time = (long) (xlen / Math.abs(velocityX) + xlen % Math.abs(velocityX)) * 1000;
                    if (time < 0 || time > 10000) {
                        HikVisionUtil.PTZControl(PTZCommand.PAN_RIGHT,1);
                    } else {
                        Thread.sleep(time);
                        //pt_stopright();
                        HikVisionUtil.PTZControl(PTZCommand.PAN_RIGHT,1);

                    }

                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    HikVisionUtil.PTZControl(PTZCommand.PAN_RIGHT,1);
                    e.printStackTrace();
                }
                Log.i("MyGesture", "Fling right");
                Toast.makeText(getActivity(), "Fling Right", Toast.LENGTH_SHORT).show();
            }
            // 手势向下 down
            if ((e2.getY() - e1.getY()) > 200) {
                HikVisionUtil.PTZControl(PTZCommand.TILT_DOWN,0);
                try {
                    long time = (long) (ylen / Math.abs(velocityY) + ylen % Math.abs(velocityY)) * 1000;
                    if (time < 0 || time > 10000) {
                        HikVisionUtil.PTZControl(PTZCommand.TILT_DOWN,1);
                    } else {

                        Thread.sleep(time);
                        HikVisionUtil.PTZControl(PTZCommand.TILT_DOWN,1);
                    }

                } catch (InterruptedException e) {
                    HikVisionUtil.PTZControl(PTZCommand.TILT_DOWN,1);
                    e.printStackTrace();
                }
                Log.i("MyGesture", "Fling Down");
                Toast.makeText(getActivity(), "Fling Down", Toast.LENGTH_SHORT).show();
            }
            // 手势向上 up
            if ((e1.getY() - e2.getY()) > 200) {
                HikVisionUtil.PTZControl(PTZCommand.TILT_UP,0);
                try {
                    long time = (long) (ylen / Math.abs(velocityY) + ylen % Math.abs(velocityY)) * 1000;
                    if (time < 0 || time > 10000) {
                        HikVisionUtil.PTZControl(PTZCommand.TILT_DOWN,1);
                    } else {

                        Thread.sleep(time);
                        HikVisionUtil.PTZControl(PTZCommand.TILT_DOWN,1);
                    }

                } catch (InterruptedException e) {
                    HikVisionUtil.PTZControl(PTZCommand.TILT_DOWN,1);
                    e.printStackTrace();
                }
                Log.i("MyGesture", "Fling Up");
                Toast.makeText(getActivity(), "Fling Up", Toast.LENGTH_SHORT).show();
            }

            return true;
        }
    }
}
