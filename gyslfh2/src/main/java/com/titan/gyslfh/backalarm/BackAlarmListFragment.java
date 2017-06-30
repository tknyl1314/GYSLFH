package com.titan.gyslfh.backalarm;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.titan.Injection;
import com.titan.gyslfh.alarminfo.AlarmInfoListInterface;
import com.titan.gyslfh.alarminfo.AlarmInfoModel;
import com.titan.newslfh.R;
import com.titan.newslfh.databinding.FragAlarminfolistBinding;
import com.titan.util.LoadDataScrollController;
import com.titan.util.SnackbarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BackAlarmListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BackAlarmListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BackAlarmListFragment extends Fragment  implements BackAlarmInterface,AlarmInfoListInterface, LoadDataScrollController.OnRecycleRefreshListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private AlarmListViewModel mViewModel;
    //火警总数
    private  Integer totalcount ;

    private  List<AlarmInfoModel.AlarmInfo> alarmInfoList=null;

    private FragAlarminfolistBinding mDataBinding;

    private AlarmListAdapter mAdapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LoadDataScrollController mController;

    public BackAlarmListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AlarmInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BackAlarmListFragment newInstance() {
        BackAlarmListFragment fragment = new BackAlarmListFragment();
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);

        //initData();

        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }



    /**
     *
     */
    private void intiRecyclerView() {

        mAdapter = new AlarmListAdapter<AlarmInfoModel.AlarmInfo>(getActivity(),new ArrayList<AlarmInfoModel.AlarmInfo>(0),this);

        mDataBinding.rclAlarminfo.setAdapter(mAdapter);

        mDataBinding.rclAlarminfo.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDataBinding.rclAlarminfo.addOnScrollListener(mController);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(
                inflater, R.layout.frag_alarminfolist, container, false);
        //设置emptyview
        mViewModel.noItemLabel.set(getActivity().getResources().getString(R.string.no_items));
        mViewModel.noItemIconRes.set(getActivity().getResources().getDrawable(
                R.drawable.ic_empty));
        mDataBinding.setViewmodel(mViewModel);
        return mDataBinding.getRoot();

    }


    private void setupSnackbarText() {
        mViewModel.snackbarText.addOnPropertyChangedCallback(
                new android.databinding.Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(android.databinding.Observable observable, int i) {
                        SnackbarUtils.showSnackbar(getView(), mViewModel.getSnackbarText());
                        //Toast.makeText(getActivity(),mViewModel.snackbarText.get(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupSnackbarText();

        setupRefreshLayout();

        intiRecyclerView();

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setViewModel(AlarmListViewModel mViewModel) {
        this.mViewModel=mViewModel;
    }

    @Override
    public void refresh() {
        mViewModel.loadData(true,1,"");

    }

    @Override
    public void loadMore() {
        mViewModel.loadData(true,2,"");
        mAdapter.add(mViewModel.mAlarmInfos);
       /* mAdapter.add();
        mAdapter.replaceData(mViewModel.mAlarmInfos);*/

    }

    @Override
    public void showDateDialog(int timetype) {

    }

    @Override
    public void showCountrySelectDialog(int type) {

    }

    @Override
    public void showProgress(boolean isshow) {

    }

    @Override
    public void showImage() {

    }

    @Override
    public void showImageDetail(int imgpath) {

    }

    @Override
    public void showOriginDialog() {

    }

    @Override
    public void showStatusDialog() {

    }

    @Override
    public void showIsfireDialog() {

    }

    @Override
    public void openAlarmInfoDetails(String id) {
        //addFragment(id);
    }

    @Override
    public void openAlarmInfoDetails(AlarmInfoModel.AlarmInfo alarmInfo) {
        addFragment(alarmInfo);
    }


    @Override
    public void stopUpdate() {

    }

    private void addFragment(AlarmInfoModel.AlarmInfo alarmInfo) {
        FragmentManager manager =getFragmentManager();
        BackAlarmFragment backAlarmFragment =  BackAlarmFragment.newInstance(alarmInfo);
        BackAlarmViewModel viewModel= new BackAlarmViewModel(getActivity(), backAlarmFragment, Injection.provideDataRepository(getActivity()));
        backAlarmFragment.setViewModel(viewModel);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.content_frame, backAlarmFragment);
        transaction.commit();
    }

    @Override
    public void showAlarmType() {

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

    private void setupRefreshLayout() {

        mDataBinding.srl.setColorSchemeResources(R.color.colorPrimaryDark);
        /**
         * 创建控制器，同时使当前activity实现数据监听回调接口
         */
        mController = new LoadDataScrollController(this);
        mDataBinding.srl.setOnRefreshListener(mController);

    }


}
