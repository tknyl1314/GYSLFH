package com.titan.gis.selectaddress;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.titan.gis.PositionUtil;
import com.titan.model.TitanLocation;
import com.titan.newslfh.R;
import com.titan.newslfh.databinding.FragSelectaddressBinding;
import com.titan.util.SnackbarUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectAddressFragment} interface
 * to handle interaction events.
 * Use the {@link SelectAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 * 选择地址界面
 */
public class SelectAddressFragment extends Fragment implements SelectAddressInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private  TitanLocation titanLocation;

    private OnFragmentInteractionListener mCallback;

    private SelectAddressViewModel mViewModel;

    private FragSelectaddressBinding mDataBinding;
    //定位点样式
    private BitmapDescriptor mCurrentMarker;


    public SelectAddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectAddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectAddressFragment newInstance(String param1, String param2) {
        SelectAddressFragment fragment = new SelectAddressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static SelectAddressFragment newInstance(TitanLocation location) {
        SelectAddressFragment fragment = new SelectAddressFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("loc",location);
        fragment.setArguments(bundle);
        //SelectAddressViewModel viewModel=new SelectAddressViewModel(context,this);

        return  fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            titanLocation= (TitanLocation) getArguments().getSerializable("loc");
           /* mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.frag_selectaddress, container, false);
        mDataBinding.setViewmodel(mViewModel);
        //确认位置
        mDataBinding.btnConfirmloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.onGetNewAddress(mViewModel.titanloc.get());
                    //回退
                    getFragmentManager().popBackStack();
                }
            }
        });
        return mDataBinding.getRoot();
       /* // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_selectaddress, container, false);*/
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupSnackbar();
    }

    private void setupSnackbar() {
        mViewModel.snackbarText.addOnPropertyChangedCallback(
                new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable observable, int i) {
                        SnackbarUtils.showSnackbar(getView(), mViewModel.snackbarText.get());

                    }
                });
    }


    @Override
    public void onResume() {
        super.onResume();
        mViewModel.snackbarText.set("请移动地图选择位置");
        BaiduMap mBaiduMap=mDataBinding.mapview.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //
        double lat=titanLocation.getLat();
        double lon=titanLocation.getLon();
        //坐标转换
        TitanLocation location=PositionUtil.gcj02_To_Bd09(lon,lat);
        LatLng cenpt = new LatLng(location.getLat(),location.getLon());
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(16)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        //监听地图事件
        mBaiduMap.setOnMapStatusChangeListener(mViewModel);

        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()//
                //.accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLat())
                .longitude(location.getLon()).build();

        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);
         // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_geo);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker);
        mBaiduMap.setMyLocationConfiguration(config);
    }

    public void setViewModel(SelectAddressViewModel viewModel) {
        this.mViewModel=viewModel;
    }

    @Override
    public void mapChange(MapStatus mapStatus) {

        mapStatus.target.describeContents();
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
        //位置更新
        void onGetNewAddress(TitanLocation titanLocation);
    }
}
