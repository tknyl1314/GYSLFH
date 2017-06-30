package com.titan.gyslfh.alarminfo;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.titan.Injection;
import com.titan.newslfh.R;
import com.titan.newslfh.databinding.FragmentAlarmInfoBinding;
import com.titan.newslfh.databinding.ItemAlarminfoBinding;
import com.titan.util.LoadDataScrollController;
import com.titan.util.SnackbarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlarmInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlarmInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmInfoFragment extends Fragment implements AlarmInfoListInterface  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private AlarmInfoViewModel mViewModel;
    //请求页码数
    private int count=1;
    private String pagecount="10";
    //火警总数
    private  Integer totalcount ;

    private  List<AlarmInfoModel.AlarmInfo> alarmInfoList=null;

    private FragmentAlarmInfoBinding mViewDataBinding;

    private  RVAdapter_listobject mAdapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LoadDataScrollController mController;

    public AlarmInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AlarmInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlarmInfoFragment newInstance() {
        AlarmInfoFragment fragment = new AlarmInfoFragment();
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
     * 初始化
     */
    private void intiRecyclerView() {

        mAdapter = new RVAdapter_listobject<>(getActivity(),new ArrayList<AlarmInfoModel.AlarmInfo>(0),this);

        mViewDataBinding.rclAlarminfo.setAdapter(mAdapter);

        mViewDataBinding.rclAlarminfo.setLayoutManager(new LinearLayoutManager(getActivity()));

        mViewDataBinding.rclAlarminfo.addOnScrollListener(mController);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_alarm_info, container, false);
        //设置emptyview
        mViewModel.noItemLabel.set(getActivity().getResources().getString(R.string.no_items));
        mViewModel.noItemIconRes.set(getActivity().getResources().getDrawable(
                R.drawable.ic_empty));
        mViewDataBinding.setViewmodel(mViewModel);
        return mViewDataBinding.getRoot();

    }


    private void setupToast() {
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
        setupToast();

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

    public void setViewModel(AlarmInfoViewModel mViewModel) {
        this.mViewModel=mViewModel;
    }

    /**
     *
     */
    /*@Override
    public void refresh() {
        mViewModel.setPageindex(1);
        mViewModel.loadData(0,true);
    }*/

    /**
     *
     */
    /*@Override
    public void loadMore() {
        //pageindex++;
        mViewModel.loadData(1,true);

    }*/

    @Override
    public void openAlarmInfoDetails(AlarmInfoModel.AlarmInfo alarmInfo) {
        replaceFragment(alarmInfo.getID());
    }

    private void replaceFragment(String alarmid) {
        FragmentManager manager =getFragmentManager();
        AlarmDetailFragment alarmDetailFragment =  AlarmDetailFragment.newInstance(alarmid);
        AlarmDetailViewModel viewModel= new AlarmDetailViewModel(getActivity(),alarmDetailFragment, Injection.provideDataRepository(getActivity()));
        alarmDetailFragment.setViewModel(viewModel);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_frame, alarmDetailFragment);
        transaction.addToBackStack(null);
        //设置过度动画
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    /*@Override
    public void loadMoreData(List<AlarmInfoModel.AlarmInfo> alarmInfos) {
        try{
            if(alarmInfos.size()>0){
                mAdapter.add(alarmInfos);
                mController.setLoadDataStatus(false);
            }
        }catch(Exception e) {
             mViewModel.snackbarText.set("数据加载异常"+e.toString());
        }


    }

    @Override
    public void refreshData(List<AlarmInfoModel.AlarmInfo> alarmInfos) {
       try{
           if(alarmInfos.size()>0){
               mAdapter.refresh(alarmInfos);
               mController.setLoadDataStatus(false);
           }

       }catch(Exception e) {
           mViewModel.snackbarText.set("数据刷新异常"+e.toString());
       }



    }*/

    @Override
    public void stopUpdate() {
        mController.setLoadDataStatus(false);
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

        mViewDataBinding.srl.setColorSchemeResources(R.color.colorPrimaryDark);
        /**
         * 创建控制器，同时使当前activity实现数据监听回调接口
         */
        mController = new LoadDataScrollController(mViewModel);
        mViewDataBinding.srl.setOnRefreshListener(mController);

    }

    public static class RVAdapter_listobject<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private  List<T> listobj;
        private  AlarmInfoListInterface mAlarmInfoItemNav;
        private  Context mContext;


        public RVAdapter_listobject(Context context,List<T> listdata ,AlarmInfoListInterface alarmInfoItemNav){
            this.mContext=context;
            this.listobj=listdata;
            this.mAlarmInfoItemNav=alarmInfoItemNav;
            setList(listdata);
        }


        public static class ViewHolder extends RecyclerView.ViewHolder {

            private ItemAlarminfoBinding binding;
            public ViewHolder(View itemView) {
                super(itemView);
                binding= DataBindingUtil.bind(itemView);
            }
            public ItemAlarminfoBinding getBinding() {
                return binding;
            }
        }






        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            //View view=View.inflate(viewGroup.getContext(),R.layout.item_alarminfo,null);
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_alarminfo,viewGroup,false);
            return new ViewHolder(view);


        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
            //Log.d(TAG, "Element " + position + " set.");

            // Get element from your dataset at this position and replace the contents of the view
            // with that element

            AlarmInfoModel.AlarmInfo alarminfo= (AlarmInfoModel.AlarmInfo) listobj.get(position);
            RVAdapter_listobject.ViewHolder holder= (ViewHolder) viewHolder;
            final AlarmInfoItemViewModel viewmodel = new AlarmInfoItemViewModel(mContext,mAlarmInfoItemNav, Injection.provideDataRepository(mContext));
            //holder.getBinding().setVariable(BR.uploadinfo,listobj.get(position));
            //holder.getBinding().executePendingBindings();
            viewmodel.alarminfo.set(alarminfo);
            holder.getBinding().setViewmodel(viewmodel);




            //viewHolder.itemView.setTag(listobj.get(position));

        }




        @Override
        public int getItemCount() {
            return listobj==null?0:listobj.size();
        }

        private void setList(List<T> alarminfos) {
            this.listobj = alarminfos;
            notifyDataSetChanged();
        }

        public void replaceData(List<T> alarminfos) {
            setList(alarminfos);

        }



        public void refresh(List<T> newlawlist) {
            if(listobj!=null){
                listobj.clear();
            }
            listobj.addAll(newlawlist);
            //snackbarText.set("总共有"+totalcount.get()+"条记录  "+"已加载"+mAlarmInfos.size()+"条");
            notifyDataSetChanged();
        }



        public void add(List<T> newlawlist){
            listobj.addAll(newlawlist);
            notifyDataSetChanged();
        }

    }
}
