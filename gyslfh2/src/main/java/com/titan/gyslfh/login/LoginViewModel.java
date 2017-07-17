package com.titan.gyslfh.login;
/**
 * Created by Whs on 2016/12/1 0001
 */

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.titan.BaseViewModel;
import com.titan.data.source.DataRepository;
import com.titan.data.source.remote.RemotDataSource;
import com.titan.gyslfh.TitanApplication;
import com.titan.newslfh.R;
import com.titan.util.NetUtil;

/**
 * Created by Whs on 2016/12/1 0001
 * 登陆
 */
public class LoginViewModel extends BaseViewModel {
    //用户名
    public final ObservableField<String> username = new ObservableField<>();
    //密码
    public final ObservableField<String> password = new ObservableField<>();
    //是否记住用户
    public final ObservableBoolean isremember=new ObservableBoolean(true);

    private ILogin mLogin;
    public LoginViewModel(Context context, ILogin mlogin, DataRepository dataRepository) {
        this.mContext = context;
        this.mLogin=mlogin;
        this.mDataRepository=dataRepository;
    }
    /**
     * 登录
     */
    public  void onLongin(){
        if(NetUtil.checkNetState(mContext)){
           /* username.set("admin");
            password.set("admin");*/
            if(checkEmpty()){
                mLogin.showToast(mContext.getString(R.string.error_loginempty),0);
                return;
            }
            mLogin.showProgress();
            //个推ClientId
            String cid= PushManager.getInstance().getClientid(mContext);
            mDataRepository.checkLogin(username.get().trim(), password.get().trim(), cid, new RemotDataSource.getCallback() {
                @Override
                public void onFailure(String info) {
                    mLogin.stopProgress();
                    mLogin.showToast("登陆异常："+info,1);
                }

                @Override
                public void onSuccess(String data) {
                    mLogin.stopProgress();

                    //String user=new Gson().toJson(data);
                    //TitanApplication.mUserModel=new Gson().fromJson(user,UserModel.class);
                    TitanApplication.setmUserModel(new Gson().fromJson(data,UserModel.class));
                    //mLogin.showToast("登陆成功"+is,0);
                    if(isremember.get()){
                        TitanApplication.mSharedPreferences.edit().putString(TitanApplication.KEYNAME_USERNAME,username.get()).apply();
                        TitanApplication.mSharedPreferences.edit().putString(TitanApplication.KEYNAME_PSD,password.get()).apply();
                    }
                    //snackbarText.set("登陆成功");
                    mLogin.onNext();

                }
            });

        }
    }

    /**
     * 记住用户
     */
    public void onCheckRemember(){
        if (checkEmpty()) {
            isremember.set(!isremember.get());
            Log.e("TItan",mContext.getString(R.string.error_loginempty));
            mLogin.showToast(mContext.getString(R.string.error_loginempty), 1);
            //snackbarText.set(mContext.getString(R.string.error_loginempty));
            return;
        }
        if (isremember.get()) {
            //mLogin.showToast("用户已记住", 1);
            snackbarText.set("用户已记住");
            isremember.set(true);

        } else {
            snackbarText.set("记住用户已取消");
            //mLogin.showToast("记住用户已取消", 1);
            isremember.set(false);
      }
      TitanApplication.mSharedPreferences.edit().putBoolean(TitanApplication.KEYNAME_REMEMBER,isremember.get()).apply();

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

    /**
     * 检查提交内容是否为空
     * @return
     */
    private boolean checkEmpty(){
        if(TextUtils.isEmpty(username.get())||TextUtils.isEmpty(password.get())){
            return true;
        }else {
            return false;
        }
    }

}
