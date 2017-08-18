package com.titan.gyslfh.sceneview;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.ArcGISScene;
import com.esri.arcgisruntime.mapping.ArcGISTiledElevationSource;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Camera;
import com.titan.model.TitanLayer;
import com.titan.model.TitanLocation;
import com.titan.newslfh.R;
import com.titan.newslfh.databinding.FragSceneBinding;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SceneFragment} interface
 * to handle interaction events.
 * Use the {@link SceneFragment#newInstance} factory method to
 * create an instance of this fragment.
 * 三维场景
 */
public class SceneFragment extends Fragment implements SceneView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    private static SceneFragment singleton;

    private SceneViewModel mViewModel;

    private FragSceneBinding mDataBinding;

    private ArcGISScene mScene;

    //当前位置
    private static  TitanLocation mLocation;
    //图层信息
    private static  List<TitanLayer> mLayerList;

    public SceneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SceneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SceneFragment newInstance(String param1, String param2) {
        SceneFragment fragment = new SceneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static SceneFragment newInstance(TitanLocation titanloc, List<TitanLayer> layerlist){
        if(singleton==null){
            mLayerList=layerlist;
            mLocation=titanloc;
            singleton=new SceneFragment();
        }
        return singleton;

    }

    public  void setViewModel(SceneViewModel viewModel) {
        mViewModel= viewModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDataBinding= DataBindingUtil.inflate(inflater, R.layout.frag_scene,container,false);
        mDataBinding.setViewmodel(mViewModel);
        return mDataBinding.getRoot();
        //Inflate the layout for this fragment
        //return inflater.inflate(R.layout.frag_scene, container, false);
    }

    @Override
    public void onResume() {
        intiScene();

        super.onResume();
    }

    /**
     * 加载图层数据
     */
    private void loadData() {
        if (mLayerList!=null&&!mLayerList.isEmpty()){
            for (TitanLayer layer :mLayerList){
                if(layer.isVisiable()){
                    ServiceFeatureTable serviceFeatureTable=new ServiceFeatureTable(layer.getUrl());
                    FeatureLayer featurelayer=new FeatureLayer(serviceFeatureTable);
                    mScene.getOperationalLayers().add(featurelayer);
                }
            }
        }
    }

    /**
     * 初始化场景
     */
    private void intiScene() {

        // create a scene and add a basemap to it
        mScene = new ArcGISScene();
        mScene.setBasemap(Basemap.createImagery());
        loadData();
        mDataBinding.sceneView.setScene(mScene);
        //去除版权声明
        mDataBinding.sceneView.setAttributionTextVisible(false);
        // add base surface for elevation data
        ArcGISTiledElevationSource elevationSource = new ArcGISTiledElevationSource(
                getResources().getString(R.string.elevation_image_service));
        mScene.getBaseSurface().getElevationSources().add(elevationSource);

        // add a camera and initial camera position
        //纬度、经度、海拔、航向、俯仰角、旋转角
        //Camera camera = new Camera(mLocation.getLat(),mLocation.getLon(),10010.0, 10.0, 80.0, 300.0);
        // add a camera and initial camera position (Snowdonia National Park)英国斯诺登尼亚国家公园
        Camera camera = new Camera(53.06, -4.04, 1289.0, 295.0, 71, 0.0);
        mDataBinding.sceneView.setViewpointCamera(camera);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        /*if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
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
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
