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
import com.titan.util.UpdateUtil;
import com.titan.viewmodel.LoginViewModel;

import Util.ProgressDialogUtil;

/**
 *登录界面
 */
public class LoginActivity extends Activity
{
	Context mContext;
    LoginViewModel loginViewModel;
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
		//UserInfo user=new UserInfo("whs","123");
	  // LoginActivityBinding binding=DataBindingUtil.setContentView(this, R.layout.activity_login_1);

        //binding.setUser(user);
		loginViewModel = new LoginViewModel(this);
		binding.setViewModel(loginViewModel);
		if (!MyApplication.IntetnetISVisible)
		{
			// 无网络时跳转手机网络设置界面
			ToastUtil.setToast(mContext, "网络异常，请检查网络连接");
			/*binding.loginName.setText(MyApplication.sharedPreferences.getString("name", ""));
			binding.loginPassword.setText(MyApplication.sharedPreferences.getBoolean("remember",
					false) ? MyApplication.sharedPreferences.getString("pwd", "") : "");*/
			binding.cbRemeber.setChecked(MyApplication.sharedPreferences.getBoolean("remember",
					false));
            binding.cbAutologin.setChecked(MyApplication.sharedPreferences
					.getBoolean("zidong", false));
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
		loginViewModel.destroy();
	}

}
