package com.titan.gyslfh.login;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.titan.gyslfh.main.MainActivity;
import com.titan.newslfh.R;
import com.titan.newslfh.databinding.FragLoginBinding;
import com.titan.util.SnackbarUtils;
import com.titan.util.TitanUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 * 登陆
 */
public class LoginFragment extends Fragment implements ILogin {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    private static LoginFragment singleton;

    private LoginViewModel mViewModel;

    private Dialog mProgresssDialog;

    private FragLoginBinding mDataBinding;


    public LoginFragment() {
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
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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

    public static LoginFragment newInstance(){
        if(singleton==null){
            singleton=new LoginFragment();
        }
        return singleton;

    }

    public  void setViewModel(LoginViewModel viewModel) {
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

        mDataBinding= DataBindingUtil.inflate(inflater, R.layout.frag_login,container,false);
        mDataBinding.setViewmodel(mViewModel);
        //显示版本号
        mDataBinding.tvAppversion.setText(getActivity().getString(R.string.app_version)+ TitanUtil.getVersionCode(getActivity()));
        return mDataBinding.getRoot();
        //Inflate the layout for this fragment
        //return inflater.inflate(R.layout.frag_scene, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.onStart();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupSnackbar();
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

    @Override
    public void onNext() {
        Intent intent=new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void showProgress() {
        if (mProgresssDialog == null) {
            mProgresssDialog = new MaterialDialog.Builder(getActivity())
                    //.title(getActivity().getString(R.string.title))
                    .content(getActivity().getString(R.string.logining))
                    .progress(true, 0)
                    .cancelable(false)
                    .build();
        }
        mProgresssDialog.show();

    }

    @Override
    public void stopProgress() {
        if(mProgresssDialog!=null&& mProgresssDialog.isShowing()){
            mProgresssDialog.dismiss();
        }

    }

    @Override
    public void showToast(String info, int type) {
        Toast.makeText(getActivity(), info, type).show();
    }
}
