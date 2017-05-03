package com.titan.gyslfh.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.titan.ViewModelHolder;
import com.titan.gyslfh.TitanApplication;
import com.titan.gyslfh.main.MainActivity;
import com.titan.newslfh.R;
import com.titan.newslfh.databinding.ActivityLoginBinding;
import com.titan.push.GeTui;
import com.titan.push.GeTuiIntentService;
import com.titan.push.GeTuiPushService;
import com.titan.util.ActivityUtils;
import com.titan.util.TitanUtil;
import com.titan.util.ToastUtil;


/**
 * 登陆界面.
 */
public class LoginActivity extends AppCompatActivity implements ILogin {

    public static final String LOGIN_VIEWMODEL_TAG = "LOGIN_VIEWMODEL_TAG";

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    Context mContext;

    // DemoPushService.class 自定义服务名称, 核心服务
    private Class userPushService = GeTuiPushService.class;
    
    private String appkey = "";
    private String appsecret = "";
    private String appid = "";

    private LoginViewModel mViewModel;
    private ActivityLoginBinding mViewDataBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TitanApplication.getInstance().addActivity(this);
        mContext=this;
        setContentView(R.layout.activity_login);
        //final View root = inflater.inflate(R.layout.activity_login, false);
        if (mViewDataBinding == null) {
            mViewDataBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        }
        mViewModel = findOrCreateViewModel();
        //
        mViewModel.isremember.set(TitanApplication.Titansp.getBoolean("isremember",false));
        mViewDataBinding.setViewmodel(mViewModel);
        //显示版本号
        mViewDataBinding.tvAppversion.setText(mContext.getString(R.string.app_version)+ TitanUtil.getVersionCode(mContext));
    }


    private LoginViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<LoginViewModel> retainedViewModel =
                (ViewModelHolder<LoginViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(LOGIN_VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            // There is no ViewModel yet, create it.
            LoginViewModel viewModel = new LoginViewModel(getApplicationContext(), this);
            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    LOGIN_VIEWMODEL_TAG);
            return viewModel;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //parseManifests();
        intiPermisson();
        //个推初始化
        PushManager.getInstance().registerPushIntentService(mContext.getApplicationContext(), GeTuiIntentService.class);
    }

    /**
     * 初始化权限
     */
    private void intiPermisson() {
        boolean sdCardWritePermission = ContextCompat.checkSelfPermission(mContext, GeTui.GTreqPermission[0]) ==
                PackageManager.PERMISSION_GRANTED;
        boolean phoneSatePermission = ContextCompat.checkSelfPermission(mContext,  GeTui.GTreqPermission[1]) ==
                PackageManager.PERMISSION_GRANTED;
        /**个推权限请求*/
        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission) {
            ActivityCompat.requestPermissions(this, GeTui.GTreqPermission,
                    GeTui.GTrequestCode);
        }else {
            PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
        }
    }
    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,
                                           int[] grantResults) {
        if (requestCode == GeTui.GTrequestCode) {
            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
            } else {
                Log.e("GT", "We highly recommend that you need to grant the special permissions before initializing the SDK, otherwise some "
                        + "functions will not work");
                PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
   /* private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mViewDataBinding.loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
            mViewDataBinding.loginForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mViewDataBinding.loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mViewDataBinding.loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            mViewDataBinding.loginProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mViewDataBinding.loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mViewDataBinding.loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            mViewDataBinding.loginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }*/

    /**
     * 跳转主界面
     */
    @Override
    public void onNext() {
        Intent intent=new Intent(mContext,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void showProgress() {
        //showProgress(true);
    }

    @Override
    public void stopProgress() {
        //showProgress(false);

    }

    @Override
    public void showToast(String info,int type) {
        Toast.makeText(mContext, info, type).show();
    }


    /**
     * 配置个推信息
     */
    private void parseManifests() {
        String packageName = getApplicationContext().getPackageName();
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                appid = appInfo.metaData.getString("PUSH_APPID");
                appsecret = appInfo.metaData.getString("PUSH_APPSECRET");
                appkey = appInfo.metaData.getString("PUSH_APPKEY");
            }
        } catch (Exception e) {
            ToastUtil.showToast(mContext,"获取个推配置信息失败:"+e,1);
            //e.printStackTrace();
        }
    }
}

