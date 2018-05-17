package com.titan.gyslfh.login;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.titan.BaseActivity;
import com.titan.Injection;
import com.titan.ViewModelHolder;
import com.titan.gyslfh.TitanApplication;
import com.titan.newslfh.R;
import com.titan.newslfh.databinding.ActivityLoginBinding;
import com.titan.push.GeTui;
import com.titan.push.GeTuiIntentService;
import com.titan.push.GeTuiPushService;
import com.titan.util.ActivityUtils;


/**
 * 登陆界面
 */
public class LoginActivity extends BaseActivity {

    public static final String LOGIN_VIEWMODEL_TAG = "LOGIN_VIEWMODEL_TAG";


    Context mContext;

    // DemoPushService.class 自定义服务名称, 核心服务
    private Class userPushService = GeTuiPushService.class;
    
    private String appkey = "";
    private String appsecret = "";
    private String appid = "";

    private LoginViewModel mViewModel;
    private ActivityLoginBinding mViewDataBinding;
    private LoginFragment mFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TitanApplication.getInstance().addActivity(this);
        mContext=this;

        setContentView(R.layout.activity_login);

        mFragment = findOrCreateViewFragment();

        mViewModel = findOrCreateViewModel();
        mViewModel.isremember.set(TitanApplication.mSharedPreferences.getBoolean("isremember",false));
        // Link View and ViewModel
        mFragment.setViewModel(mViewModel);

    }

    public LoginViewModel findOrCreateViewModel() {
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
            LoginViewModel viewModel = new LoginViewModel(mContext, mFragment, Injection.provideDataRepository(mContext));
            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    LOGIN_VIEWMODEL_TAG);
            return viewModel;
        }
    }

    @NonNull
    public LoginFragment findOrCreateViewFragment() {
        LoginFragment tasksFragment =
                (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), tasksFragment, R.id.content_frame);
        }
        return tasksFragment;
    }

    @Override
    protected void onStart() {
        super.onStart();

        parseManifests();

        intiPermisson();
        //个推初始化
        // 注册 intentService 后 PushDemoReceiver 无效, sdk 会使用 DemoIntentService 传递数据,
        // AndroidManifest 对应保留一个即可(如果注册 DemoIntentService, 可以去掉 PushDemoReceiver, 如果注册了
        // IntentService, 必须在 AndroidManifest 中声明)
        PushManager.getInstance().registerPushIntentService(mContext.getApplicationContext(), GeTuiIntentService.class);
        //String cid= PushManager.getInstance().getClientid(mContext);


    }

    @Override
    protected void onResume() {
        super.onResume();
        //mViewModel.onStart();
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
        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission ) {
            ActivityCompat.requestPermissions(this, GeTui.GTreqPermission,
                    GeTui.GTrequestCode);
        }else {
            //个推初始化
            PushManager.getInstance().initialize(mContext.getApplicationContext(), userPushService);
            //注册服务
            PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), GeTuiIntentService.class);
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
                //注册服务
                PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), GeTuiIntentService.class);
            } else {

                Log.e("GT", "We highly recommend that you need to grant the special permissions before initializing the SDK, otherwise some "
                        + "functions will not work");
                PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
                //注册服务
                PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), GeTuiIntentService.class);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    /**
     * 跳转主界面
     */
    /*@Override
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
    }*/


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
            //ToastUtil.showToast(mContext,"获取个推配置信息失败:"+e,1);
            Toast.makeText(mContext,"获取个推配置信息失败:"+e,Toast.LENGTH_LONG).show();
            //e.printStackTrace();
        }
    }
}

