package com.titan.gyslfh.login;
/**
 * Created by Whs on 2016/12/1 0001
 */

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.igexin.sdk.PushManager;
import com.titan.data.source.remote.RetrofitHelper;
import com.titan.gyslfh.TitanApplication;
import com.titan.model.ResultModel;
import com.titan.newslfh.R;
import com.titan.util.NetUtil;

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
    //是否记住用户
    //public final ObservableField<Boolean> isremember = new ObservableField<>();
    public final ObservableBoolean isremember=new ObservableBoolean(true);

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
        if(NetUtil.checkNetState(mContext)){
           /* username.set("admin");
            password.set("admin");*/
            if(TextUtils.isEmpty(username.get())||TextUtils.isEmpty(password.get())){
                mLogin.showToast(mContext.getString(R.string.error_loginempty),0);
                return;
            }
            //个推ClientId
            String cid= PushManager.getInstance().getClientid(mContext);
            //Log.e("TITAN",cid);

            //mLogin.showProgress();
            Observable<String> observable=RetrofitHelper.getInstance(mContext).getServer().Checklogin(username.get(),password.get(),cid);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onCompleted() {
                           // mLogin.stopProgress();
                        }

                        @Override
                        public void onError(Throwable e) {
                            //mLogin.stopProgress();
                            mLogin.showToast("登陆异常："+e,1);
                            Log.i("dd",e.toString());
                        }
                        @Override
                        public void onNext(String json) {
                            //mLogin.showToast("获取结果",0);
                            try {
                                ResultModel<UserModel> resultModel=new Gson().fromJson(json, ResultModel.class);

                                if(resultModel.getResult()){
                                /*String dd=resultModel.getData().toString();
                                mLogin.showToast("登陆成功"+dd,1);*/
                                    String user=new Gson().toJson(resultModel.getData());
                                    //TitanApplication.mUserModel=new Gson().fromJson(user,UserModel.class);
                                    TitanApplication.setmUserModel(new Gson().fromJson(user,UserModel.class));
                                    //mLogin.showToast("登陆成功"+is,0);
                                    if(isremember.get()){
                                        TitanApplication.mSharedPreferences.edit().putString(TitanApplication.KEYNAME_USERNAME,username.get()).apply();
                                        TitanApplication.mSharedPreferences.edit().putString(TitanApplication.KEYNAME_PSD,password.get()).apply();

                                    }

                                    //mLogin.showToast("登陆成功",0);
                                    mLogin.onNext();
                                }else {
                                    //ToastUtil.showToast(mContext,"登陆",0);
                                    mLogin.showToast("登陆失败："+resultModel.getMessage(),1);
                                }
                            }catch (JsonSyntaxException e){
                                mLogin.showToast("用户数据解析失败"+e,1);
                            }

                            //Log.e("titan",s);

                        }
                    });
        }
    }

    /**
     * 记住用户
     */
    public void onCheckRemember(){
      if(isremember.get()){
          mLogin.showToast("用户已记住",1);
          isremember.set(true);

      }else {
          mLogin.showToast("记住用户已取消",1);
          isremember.set(false);
      }
      TitanApplication.mSharedPreferences.edit().putBoolean(TitanApplication.KEYNAME_REMEMBER,isremember.get()).apply();

    }

    /**
     *
     * @return
     */
    private String getClientId(){
        String cid= PushManager.getInstance().getClientid(mContext);
        return null;
    }


    /**
     * 初始化
     */
    public void onStart() {
        isremember.set(TitanApplication.mSharedPreferences.getBoolean(TitanApplication.KEYNAME_REMEMBER,false));
        if(isremember.get()){
            username.set(TitanApplication.mSharedPreferences.getString(TitanApplication.KEYNAME_USERNAME,""));
            password.set(TitanApplication.mSharedPreferences.getString(TitanApplication.KEYNAME_PSD,""));
        }
    }
}
