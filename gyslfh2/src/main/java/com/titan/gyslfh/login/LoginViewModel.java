package com.titan.gyslfh.login;
/**
 * Created by Whs on 2016/12/1 0001
 */

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;

import com.google.gson.Gson;
import com.titan.data.source.remote.RetrofitHelper;
import com.titan.model.ResultModel;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Whs on 2016/12/1 0001
 */
public class LoginViewModel extends BaseObservable  {
    //用户名
    public final ObservableField<String> username = new ObservableField<>();
    //密码
    public final ObservableField<String> password = new ObservableField<>();

    private Context mContext;
    private ILogin mLogin;
    public LoginViewModel(Context context,ILogin mlogin) {
        this.mContext = context;
        this.mLogin=mlogin;
    }
    /**
     * 登录
     */
    public  void onLongin(){
        mLogin.showProgress();
        String test=username.get()+password.get();

        Observable<String> observable=RetrofitHelper.getInstance(mContext).getServer().Checklogin(username.get(),password.get());
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                mLogin.stopProgress();
            }

            @Override
            public void onError(Throwable e) {
                mLogin.stopProgress();
                mLogin.showToast("登陆异常："+e,1);


            }

            @Override
            public void onNext(String json) {
                ResultModel<UserModel> resultModel=new Gson().fromJson(json, ResultModel.class);
                if(resultModel.getResult().equals("1")){
                    mLogin.showToast("登陆成功",0);
                }else {
                    mLogin.showToast("登陆失败："+resultModel.getMessage(),1);
                }
                //Log.e("titan",s);

            }
        });

        //ToastUtil.setToast(context,"点击了登录");
    }

}
