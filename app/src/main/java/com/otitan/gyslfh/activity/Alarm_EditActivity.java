package com.otitan.gyslfh.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.otitan.entity.DateDialog;
import com.otitan.entity.ReceiveAlarmInfo;
import com.otitan.gyslfh.R;
import com.otitan.util.WebServiceUtil;

@SuppressLint("ShowToast")
public class Alarm_EditActivity extends Activity implements OnClickListener{
   
    //控件
    private EditText jjedit_huojingaddress, jiejing_tel;
	TextView chujingqingkuang;
	private Spinner jiejing_resource;
	private ImageButton imgBtn_return, imgBtn_upSure;
	private EditText xuhao,dailyid,unionid,address,telone,isfire,firetype
	,receiptiem,jiejing_time, shichujingTime,jiejing_bianhao,huojingaddress;
	private CheckBox yyq,nmq,byq,xfx,wdq,qzs,xwx,kyx,hxq,gshq;
	
	private ArrayAdapter<String> resourceAdapter;
	private WebServiceUtil webService;
	String jj_bianhao,tongzhiqx ;
	String[] rowdata;
	 Intent intent=getIntent();
	private Handler handler=new Handler()
	{

		@SuppressLint("ShowToast")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				//ToastUtil.makeText(Alarm_EditActivity.this, "接警信息更新成功", 0);
				Toast.makeText(Alarm_EditActivity.this, "接警信息更新成功", Toast.LENGTH_SHORT);
			    Alarm_EditActivity.this.setResult(RESULT_OK);
			    Alarm_EditActivity.this.finish();
				break;
			case 0:
				//ToastUtil.makeText(Alarm_EditActivity.this, "接警信息更新失败", 0);
				Toast.makeText(Alarm_EditActivity.this, "接警信息更新失败", Toast.LENGTH_SHORT);
				Alarm_EditActivity.this.setResult(RESULT_CANCELED);
				break;
			default:
				break;
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_alarm__edit);
		intigetdata();
		intiview();
	}
    //获取数据
	private void intigetdata() {
		//获取服务接口
		webService = new WebServiceUtil(getApplicationContext());
		Intent intent=getIntent();
	    Bundle b=intent.getExtras();  
	    rowdata = b.getStringArray("result");  
	    jj_bianhao=rowdata[0];
	   
	    
	}

	private void intiview() {
		//序号
		xuhao=(EditText) findViewById(R.id.jjedit_xuhao);
		xuhao.setText(rowdata[0]);
		xuhao.setEnabled(false);
		//当日编号
		dailyid=(EditText) findViewById(R.id.jjedit_DAILYID);
		dailyid.setText(rowdata[1]);
		dailyid.setEnabled(false);
		//相同警号
		unionid=(EditText) findViewById(R.id.jjedit_UNIONID);
		unionid.setText(rowdata[2]);
		unionid.setEnabled(false);
		//报警地址
		address = (EditText) findViewById(R.id.jjedit_ADRESS);
		address.setText(rowdata[3]);
		//报警电话
		telone = (EditText) findViewById(R.id.jjedit_tel);
		telone.setText(rowdata[4]);
		//是否火灾
		isfire=(EditText) findViewById(R.id.jjedit_isfire);
		isfire.setText(rowdata[6]);
		//火灾类型
		firetype=(EditText) findViewById(R.id.jjedit_firetype);
		firetype.setText(rowdata[7]);
		//出警情况
		chujingqingkuang = (EditText) findViewById(R.id.jjedit_chujingqingkuang);
		chujingqingkuang.setText(rowdata[8]);
		//接警时间
		receiptiem=(EditText) findViewById(R.id.jjedit_RECEIPTTIME);
		receiptiem.setText(rowdata[9]);
		
		//接警来源
		/*final String[] resourceDate = getResources().getStringArray(R.array.huojingresource);
		for (int i=0;i<resourceDate.length;i++) {
			if(resourceDate[i].equals(rowdata[i]))
			{
				
			}
		}
		resourceAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, resourceDate);
		jiejing_resource=(Spinner) findViewById(R.id.jjedit_resource);
		jiejing_resource.setAdapter(resourceAdapter);
		jiejing_resource.setSelection(position)*/
		
		
		
		//通知区县checkbox
		yyq = (CheckBox) findViewById(R.id.ck_jjedit_yyq);
		nmq = (CheckBox) findViewById(R.id.ck_jjedit_nmq);
		byq = (CheckBox) findViewById(R.id.ck_jjedit_byq);
		xfx = (CheckBox) findViewById(R.id.ck_jjedit_xfx);
		wdq = (CheckBox) findViewById(R.id.ck_jjedit_wdq);
		qzs = (CheckBox) findViewById(R.id.ck_jjedit_qzs);
		xwx = (CheckBox) findViewById(R.id.ck_jjedit_xwx);
		kyx = (CheckBox) findViewById(R.id.ck_jjedit_kyx);
		hxq = (CheckBox) findViewById(R.id.ck_jjedit_hxq);
		gshq = (CheckBox) findViewById(R.id.ck_jjedit_gshq);
		intisetquxian();//
		
		imgBtn_upSure = (ImageButton) findViewById(R.id.jjedit_imgBtn_upSure);
		imgBtn_return = (ImageButton) findViewById(R.id.jjedit_returnBtn);
		imgBtn_return.setOnClickListener(this);
		imgBtn_upSure.setOnClickListener(this);
	}
    //获取通知区县
	private void intisetquxian() {
		tongzhiqx=rowdata[5];//通知区县
		String [] tongzhiqxsStrings=tongzhiqx.split(",");
		for (int i = 0; i < tongzhiqxsStrings.length; i++) {
			switch (tongzhiqxsStrings[i]) {
			case "云岩区":
				yyq.setChecked(true);
				break;
			case "南明区":
				nmq.setChecked(true);
				break;
			case "白云区":
				byq.setChecked(true);
				break;
			case "息烽县":
				xfx.setChecked(true);
				break;
			case "乌当区":
				wdq.setChecked(true);
				break;
			case "清镇市":
				qzs.setChecked(true);
				break;
			case "修文县":
				xwx.setChecked(true);
				break;
			case "开阳县":
				kyx.setChecked(true);
				break;
			case "花溪区":
				hxq.setChecked(true);
				break;
			case "观山湖区":
				gshq.setChecked(true);
				break;
			default:
				break;
			}
		}
		
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.jjedit_time_text:
			Dialog jiejingDateDialog = new DateDialog(Alarm_EditActivity.this,jiejing_time);
			jiejingDateDialog.show();
			break;
		case R.id.shichujingTime:
			Dialog shichujingDateDialog = new DateDialog(Alarm_EditActivity.this, shichujingTime);
			shichujingDateDialog.show();
			break;
		case R.id.jjedit_returnBtn:
			
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘  
			Alarm_EditActivity.this.setResult(RESULT_CANCELED);
			Alarm_EditActivity.this.finish();
			break;
        //更新数据
		case R.id.jjedit_imgBtn_upSure:
//			InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
//			im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  //强制隐藏键盘  
			/*boolean isopen=im.isActive();//isOpen若返回true，则表示输入法打开  
			if(isopen){
				im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  //强制隐藏键盘  
			}*/
			InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
			im.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘  
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					Message msg =new Message();
					if(update())
					{
						msg.what=1;
					}
					else {
						msg.what=0;
					}
					handler.sendMessage(msg);
					
				}
			}).start();
		/*	if()
			{
				ToastUtil.makeText(Alarm_EditActivity.this, "接警信息更新成功", 0);
			}else {
				ToastUtil.makeText(Alarm_EditActivity.this, "接警信息更新失败", 0);
			}*/
			break;
	  }
	}
	//更新接警信息
	private boolean update() {
		
		if(yyq.isChecked()){
			tongzhiqx = yyq.getText().toString();
		}
		if(nmq.isChecked()){
			tongzhiqx = tongzhiqx +","+nmq.getText().toString();
		}
		if(byq.isChecked()){
			tongzhiqx = tongzhiqx +","+byq.getText().toString();
		}
		if(xfx.isChecked()){
			tongzhiqx = tongzhiqx +","+xfx.getText().toString();
		}
		if(wdq.isChecked()){
			tongzhiqx = tongzhiqx +","+wdq.getText().toString();
		}
		if(qzs.isChecked()){
			tongzhiqx = tongzhiqx +","+qzs.getText().toString();
		}
		if(xwx.isChecked()){
			tongzhiqx = tongzhiqx +","+xwx.getText().toString();
		}
		if(kyx.isChecked()){
			tongzhiqx = tongzhiqx +","+kyx.getText().toString();
		}
		if(hxq.isChecked()){
			tongzhiqx = tongzhiqx +","+hxq.getText().toString();
		}
		if(gshq.isChecked()){
			tongzhiqx = tongzhiqx +","+gshq.getText().toString();
		}
		
		ReceiveAlarmInfo receiveAlarmInfo = new ReceiveAlarmInfo();
		receiveAlarmInfo.setId(xuhao.getText().toString());
		receiveAlarmInfo.setDailyid(dailyid.getText().toString());
		receiveAlarmInfo.setUnionid(unionid.getText().toString());
		receiveAlarmInfo.setAdress(address.getText().toString());
		receiveAlarmInfo.setTelone(telone.getText().toString());
		receiveAlarmInfo.setInformareawatcher(tongzhiqx);//INFORMAREAWATCHER
		receiveAlarmInfo.setIsfire(isfire.getText().toString());
		receiveAlarmInfo.setFiretype(firetype.getText().toString());
		receiveAlarmInfo.setPolicecase(chujingqingkuang.getText().toString());
		receiveAlarmInfo.setReceipttime(rowdata[9]);//接警时间不可编辑
		return webService.updateJJInfo(receiveAlarmInfo);
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
			//imm.hideSoftInputFromWindow(Alarm_EditActivity.getWindowToken(), 0); //强制隐藏键盘  
			Alarm_EditActivity.this.setResult(RESULT_OK, intent);
			Alarm_EditActivity.this.finish();
		}
		return true;
	}
}
