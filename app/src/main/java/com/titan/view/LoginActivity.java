package com.titan.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.otitan.gyslfh.R;
import com.otitan.gyslfh.activity.MyApplication;
import com.otitan.gyslfh.databinding.ActivityLogin1Binding;
import com.otitan.util.PadUtil;
import com.otitan.util.ToastUtil;
import com.titan.model.UserInfo;
import com.titan.util.UpdateUtil;
import com.titan.viewmodel.LoginViewModel;

import Util.ProgressDialogUtil;

/**
 *
 */
public class LoginActivity extends Activity
{
	Context mContext;
    LoginViewModel loginViewModel;
    //ViewDataBinding binding;
    //LoginActivityBinding binding;
    ActivityLogin1Binding binding;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mContext=this;
		if (PadUtil.isPad(this))
		{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
        binding=   DataBindingUtil.setContentView(this, R.layout.activity_login_1);
		UserInfo user=new UserInfo("whs","123");
	  // LoginActivityBinding binding=DataBindingUtil.setContentView(this, R.layout.activity_login_1);
    /*    loginViewModel = new LoginViewModel(this);
        binding.setViewModel(loginViewModel);*/
        binding.setUser(user);

		if (!MyApplication.IntetnetISVisible)
		{
			// 无网络时跳转手机网络设置界面
			ToastUtil.setToast(mContext, "网络连接异常");
		} else
		{
			//检查更新
            UpdateUtil update=new UpdateUtil(mContext);
            update.executeUpdate();
		}
	}
	@Override
	protected void onDestroy()
	{
		ProgressDialogUtil.stopProgressDialog();
		super.onDestroy();
	}

}
