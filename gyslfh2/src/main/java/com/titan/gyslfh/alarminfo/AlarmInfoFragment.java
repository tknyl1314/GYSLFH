package com.titan.gyslfh.alarminfo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.titan.data.source.remote.RetrofitHelper;
import com.titan.gyslfh.TitanApplication;
import com.titan.newslfh.R;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlarmInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlarmInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private AlarmInfoViewModel mViewModel;
    //请求页码数
    private int count=1;
    private String pagecount="10";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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
        initData();

        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Observable<String> observable = RetrofitHelper.getInstance(getActivity()).getServer().getAlarmInfo("", TitanApplication.mUserModel.getDqid(),count+"",pagecount);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        /*if (data.size() > 0) {
                            intiRecyclerView(data);
                        } else {
                            Toast.makeText(mContext, "未获取到法律数据", Toast.LENGTH_SHORT).show();
                        }*/

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "获取接警信息异常" + e, Toast.LENGTH_LONG).show();


                    }

                    @Override
                    public void onNext(String s) {
                        //data = GsonFactory.jsonToArrayList(s, LawInfo.class);

                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm_info, container, false);

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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
}
