package com.otitan.gyslfh.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.otitan.adapter.AlarmManageAdapter;
import com.otitan.entity.DateDialog;
import com.otitan.entity.ReceiveAlarmInfo;
import com.otitan.gyslfh.R;
import com.otitan.util.PadUtil;
import com.otitan.util.WebServiceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import Util.ProgressDialogUtil;
import tablefixheaders.MatrixTableAdapter;
import tablefixheaders.TableFixHeaders;

@SuppressLint("ShowToast")
public class AlarmManageActivity extends Activity implements OnClickListener, MatrixTableAdapter.IMatrixTableListener {


	//页面控件
    private DrawerLayout mDrawerLayout;
    private Spinner isfire,neartime,dqspinner;
    private TextView starttime,endtime,iteminfo;
    private EditText alarmaddress,alarmnumber,number,equalalarm; 
    ImageButton btn_return;
    CheckBox cb_ischagang;
    private SharedPreferences sharedPreferences;
    private String username,susername;
    private String result,sresult;
    
    //查询地区数组适配
    private ArrayAdapter<CharSequence> dqarrayAdapter;
    private ArrayAdapter<String> spadpter;
    
    private WebServiceUtil webService;
    private PullToRefreshListView pullToRefresh;
    private List<ReceiveAlarmInfo> receiveAlarmInfos;
    private ReceiveAlarmInfo receiveAlarmInfo;
    private AlarmManageAdapter adapter;
    private int flag=0;//1表示通过查询按钮查询
    
    //private ActionBarDrawerToggle mDrawerToggle;
    //查询总页数
    private String totalpage="",totalnum="";
    private int currentpage=1;//使用pulltorefresh时初始为1
    private int ischagang=0;//是否查岗
    //查询参数
    private String[] searchStrings = new String[20];
    private String[] orsearchStrings = new String[20];
    //count rows number
    private int n=30;
    //true表示查询参数改变
    Boolean serchchange=true;
    //查询数据存储
    String [][] tableData=null;
    String [] headerstring=null;
    //表格适配器
    MatrixTableAdapter<String> matrixTableAdapter;
    TableFixHeaders table;
    
    private Handler handler=new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			switch ( msg.what) {
			//开始加载数据
			case 0:
				ProgressDialogUtil.startProgressDialog(AlarmManageActivity.this);
				break;
			//数据加载完成更新adapter
			case 1:
				//iteminfo.setText("查询总数为："+totalnum+"   当前已加载" + n + "条数据");
				matrixTableAdapter.notifyDataSetChanged();
				ProgressDialogUtil.stopProgressDialog();
				break;
			default:
				break;
			}
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.alarm_manage);
		if (PadUtil.isPad(this)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		//获取数据
		sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
		Intent intent =getIntent();
		username=intent.getStringExtra("username");
		result=intent.getStringExtra("result");

		try {
			//获取查询结果的总数
			totalnum = new JSONObject(result).getString("totalnum");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		searchStrings=new String[]{username,"","","","","","","","","",ischagang+""};//初始化查询数组
		orsearchStrings=new String[]{username,"","","","--请选择--","","--请选择--","","","--请选择--",ischagang+""};//初始化上一次查询数组
		headerstring = new String[]{"序号","当日编号","相同警号","报警地址","报警电话","通知区县","是否火灾","火灾类型","市局出警","接警时间"};
		tableData=new String[Integer.parseInt(totalnum+10)][headerstring.length];
		tableData[0]=headerstring;
		receiveAlarmInfos =getresultformjson(result);//解析服务端查询数据

		//获取服务接口
		webService=new WebServiceUtil(getApplicationContext());

		//初始化控件
		intiview();
	}
	private void intiview() {
		//pullToRefresh = (PullToRefreshListView) findViewById(R.id.pullToRefresh);
		table=(TableFixHeaders) findViewById(R.id.table);
		mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
		//查询信息统计
		iteminfo = (TextView) findViewById(R.id.tv_iteminfo);
		alarmaddress = (EditText) findViewById(R.id.et_address);
		alarmnumber = (EditText) findViewById(R.id.et_alarmnumber);
		number = (EditText) findViewById(R.id.et_number);
		equalalarm = (EditText) findViewById(R.id.et_equalalarm);

		starttime = (TextView) findViewById(R.id.tv_starttime);
		endtime = (TextView) findViewById(R.id.tv_endtime);

		isfire = (Spinner) findViewById(R.id.sp_isfire);
		neartime = (Spinner) findViewById(R.id.sp_neartime);
		dqspinner=(Spinner) findViewById(R.id.sp_dq);
		//返回按钮
		btn_return=(ImageButton) findViewById(R.id.jiejingmanage_returnBtn);
		//是否查岗
		cb_ischagang=(CheckBox) findViewById(R.id.cb_ischagang);

		//查询时间选择事件
		starttime.setOnClickListener(this);
		endtime.setOnClickListener(this);
		btn_return.setOnClickListener(this);
		table.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int dd=v.getId();
				//TextView dTextView=(TextView) v;
				//String ttString=dTextView.getText().toString();
				int rownum=table.getDowny()/table.getViewHight()+table.getFirstRow()-2;
				String[] rowdata=tableData[rownum];
				Intent intent =new Intent(AlarmManageActivity.this,Alarm_EditActivity.class);
				Bundle b = new Bundle();
				b.putStringArray("result", rowdata);
				intent.putExtras(b);
				startActivityForResult(intent, 0);
				//startActivity(intent);
				//alarm_manageActivity.this.finish();
				Log.e("table", rownum+"");
			}
		});

		//表格点击事件
		//是否查岗设置监听事件
		cb_ischagang.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					ischagang=1;
				} else {
					ischagang=0;
				}
			}
		});
		//cb_ischagang.setChecked(new checked)
		//table.setOnClickListener(this);
		//根据用户等级初始化可查询地区
		initsqspinner(sharedPreferences.getString("UNITID",""), sharedPreferences.getString("DQLEVEL",""));
		//初始化pullrefresh控件
		//initpullToRefresh();
		//表格适配器
		matrixTableAdapter = new MatrixTableAdapter<String>(AlarmManageActivity.this,tableData,n);
		matrixTableAdapter.setMatrixTableListener(AlarmManageActivity.this);
		table.setAdapter(matrixTableAdapter);
		//显示查询信息
		iteminfo.setText("查询总数为："+totalnum+"   当前已加载" + n + "条数据");
		//数据加载完成
		ProgressDialogUtil.stopProgressDialog();

		//设置侧滑监听
		/*mDrawerToggle = new ActionBarDrawerToggle(alarm_manageActivity.this, mDrawerLayout, R.drawable.ic_launcher, R.string.drawer_open,
				R.string.drawer_close) {

			*//** Called when a drawer has settled in a completely closed state. *//*
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				Log.e("侧滑", "侧滑打开了");
				//ToastUtil.makeText(alarm_manageActivity.this, "侧滑关闭了", 0);
			}

			*//** Called when a drawer has settled in a completely open state. *//*
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				Log.e("侧滑", "侧滑关闭了");
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);*/
		mDrawerLayout.setDrawerListener(new DrawerListener() {

			@Override
			public void onDrawerStateChanged(int arg0) {

			}
			@Override
			public void onDrawerSlide(View arg0, float arg1) {
			}

			@Override
			public void onDrawerOpened(View arg0) {
				Log.e("侧滑", "侧滑打开了");
			}

			@Override
			public void onDrawerClosed(View arg0) {
				Log.e("侧滑", "侧滑关闭了");
				//[admin, , , , --请选择--, , --请选择--, , , --请选择--]
				getSerchstrings();//获取查询参数
				if (!isSerchstring_equal()) {
					ProgressDialogUtil.startProgressDialog(AlarmManageActivity.this);
					//获取数据
					getSerchdata();
					ProgressDialogUtil.stopProgressDialog();
				}
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(resultCode==RESULT_OK){
			new GetTableDataTask().execute(searchStrings);
			currentpage=1;
			//获取查询结果总数
			getresultinfoformjson(result);
			tableData=new String[Integer.parseInt(totalnum)][headerstring.length];
			tableData[0]=headerstring;
			getresultformjson(result);
			matrixTableAdapter = new MatrixTableAdapter<String>(AlarmManageActivity.this,tableData,n);
			matrixTableAdapter.setMatrixTableListener(AlarmManageActivity.this);//设置加载更多监听
			table.setAdapter(matrixTableAdapter);
			//matrixTableAdapter.notifyDataSetChanged();
		}

	}
	//获取查询参数
	protected void getSerchstrings() {
		flag=1;
		currentpage=1;
		n = 0;
		searchStrings[0] = username;
		String id = searchStrings[1]= number.getText().toString();
		String equal = searchStrings[2] = equalalarm.getText().toString();
		String address = searchStrings[3] = alarmaddress.getText().toString();
		String dq = searchStrings[4] = dqspinner.getSelectedItem().toString();
		String phonenumber = searchStrings[5] = alarmnumber.getText().toString();
		String fireis = searchStrings[6] =isfire.getSelectedItem().toString();
		String start= searchStrings[7] =starttime.getText().toString();
		String near = searchStrings[9] = neartime.getSelectedItem().toString();
		if(start.equals("点击选择时间"))
		{
			searchStrings[7]=null;
		}
		String end= searchStrings[8] =endtime.getText().toString();
		if(end.equals("点击选择时间"))
		{
			searchStrings[8]=null;
		}
		searchStrings[10]=ischagang+"";
	}
	//判断查询参数是否改变
	public boolean isSerchstring_equal() {
		return Arrays.equals(searchStrings, orsearchStrings);
	}
	//获取查询结果
	protected void getSerchdata() {

		//记录上次查询参数
		int i=0;
		for (String str : searchStrings) {
			orsearchStrings[i] = str;
			i++;
		}
		//清空数据存储获取新的查询个数
		receiveAlarmInfos.clear();

		new GetTableDataTask().execute(searchStrings);
		currentpage=1;
		//获取查询结果总数
		getresultinfoformjson(result);
		tableData=new String[Integer.parseInt(totalnum)][headerstring.length];
		tableData[0]=headerstring;
		getresultformjson(result);
		matrixTableAdapter = new MatrixTableAdapter<String>(AlarmManageActivity.this,tableData,n);
		matrixTableAdapter.setMatrixTableListener(AlarmManageActivity.this);//设置加载更多监听
		table.setAdapter(matrixTableAdapter);

		//pulltorefresh
		//new GetDataTask().execute(searchStrings);
	}
	//为pullToRefresh设置adapter
	private void setListAdapter(List<ReceiveAlarmInfo> receiveAlarmInfos) {
		adapter = new AlarmManageAdapter(AlarmManageActivity.this, receiveAlarmInfos);
		pullToRefresh.setAdapter(adapter);
		n=receiveAlarmInfos.size();
		iteminfo.setText("查询总数为："+totalnum+"   当前已加载" + n + "条数据");
	}

	//根据用户等级初始化可查询地区
	private void initsqspinner(String UNITID, String dqlevel) {

		if (dqlevel.equals("1") || dqlevel.equals("0")) {

			dqarrayAdapter = ArrayAdapter.createFromResource(AlarmManageActivity.this, R.array.counties,android.R.layout.simple_spinner_dropdown_item);
			dqspinner.setAdapter(dqarrayAdapter);
		} else {
			try {
				String dqname = new JSONObject(result).getString("dqname");
				String[] liString = new String[] { "--请选择--", dqname };
				spadpter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, liString);
				spadpter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				dqspinner.setAdapter(spadpter);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	//初始化pullToRefresh控件
	private void initpullToRefresh() {

		pullToRefresh.setMode(Mode.PULL_FROM_END);//设置刷新模式
		setListAdapter(receiveAlarmInfos);//设置数据adapter
		//初始化下拉和上拉布局
		ILoadingLayout startLabels = pullToRefresh.getLoadingLayoutProxy(true,false);
		startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("正在载入...");// 刷新时
		startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
		ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(false,true);
		endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在载入...");// 刷新时
		endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
		//设置刷新监听
		pullToRefresh.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				/*if(flag==1)
				{
					new GetDataTask().execute(searchStrings);
				}*/

				ProgressDialogUtil.startProgressDialog(AlarmManageActivity.this);
				new GetDataTask().execute(searchStrings);
			}
		});
	}
	private class GetTableDataTask extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... params) {
			if (currentpage > Integer.parseInt(totalpage)) {
				return null;
			}
			result=webService.serchHuoJing(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8],params[9],currentpage,ischagang);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return result;
		}
		protected void onPostExecute(final String result) {
			if (result != null) {
				receiveAlarmInfos.addAll(getresultformjson(result));
				n = receiveAlarmInfos.size();
			}
			iteminfo.setText("查询总数为："+totalnum+"   当前已加载" + n + "条数据");
			//ProgressDialogUtil.stopProgressDialog();
		}

	}
	//异步查询数据
	private class GetDataTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		protected String doInBackground(String... params) {

			if (currentpage > Integer.parseInt(totalpage)) {
				return null;
			}
			result=webService.serchHuoJing(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8],params[9],currentpage,ischagang);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			currentpage++;
			return result;
		}

		protected void onPostExecute(final String result) {

			if (result != null) {
				if(flag==1){
					receiveAlarmInfos.clear();
					flag=0;
				}
				receiveAlarmInfos.addAll(getresultformjson(result));

				HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
				for (int i = 0; i < receiveAlarmInfos.size(); i++) {
					isSelected.put(i, false);
				}
				AlarmManageAdapter.setIsSelected(isSelected);
				adapter.notifyDataSetChanged();
				n = receiveAlarmInfos.size();
			} else {
				//pullToRefresh.setRefreshingLabel();
				ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(false, true);
				//endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示    
				endLabels.setRefreshingLabel("所有数据已加载完成...");// 刷新时    
				//endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示    
			}

			iteminfo.setText("查询总数为："+totalnum+"   当前已加载" + n + "条数据");

			// Call onRefreshComplete when the list has been refreshed.
			pullToRefresh.onRefreshComplete();
			ProgressDialogUtil.stopProgressDialog();
			super.onPostExecute(result);
		}
	}
	private String getresultinfoformjson(String houjingresult) {
		JSONObject jsonobject;
		try {
			jsonobject = new JSONObject(houjingresult);
			totalnum =jsonobject.getString("totalnum");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return totalnum;
	}

	//解析返回的json
	private List<ReceiveAlarmInfo> getresultformjson(String houjingresult) {
		List<ReceiveAlarmInfo> receiveAlarmInfos = new ArrayList<ReceiveAlarmInfo>();
		try {
			JSONObject jsonobject=new JSONObject(houjingresult);
			totalpage =jsonobject.getString("totalpage");
			totalnum =jsonobject.getString("totalnum");
			JSONArray jsonArray=jsonobject.getJSONArray("ds");
			for (int i = 0; i < jsonArray.length(); i++) {
				receiveAlarmInfo = new ReceiveAlarmInfo();
				JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
				receiveAlarmInfo.setId(jsonObject.getString("ID"));
				receiveAlarmInfo.setDailyid(jsonObject.getString("DAILYID"));
				receiveAlarmInfo.setUnionid(jsonObject.getString("UNIONID"));
				receiveAlarmInfo.setAdress(jsonObject.getString("ADRESS"));
				receiveAlarmInfo.setTelone(jsonObject.getString("TEL_ONE").replace(",", "").trim());
				receiveAlarmInfo.setInformareawatcher(jsonObject.getString("INFORMAREAWATCHER"));
				receiveAlarmInfo.setIsfire(jsonObject.getString("ISFIRE"));
				receiveAlarmInfo.setFiretype(jsonObject.getString("FIRETYPE"));
				//receiveAlarmInfo.setBackstate(jsonObject.getString("BACKSTATE").equals("2") ? "已回警" : "未回警");
				receiveAlarmInfo.setPolicecase(jsonObject.getString("POLICECASE").trim().equals("")?"未出警":"已出警");
				receiveAlarmInfo.setReceipttime(jsonObject.getString("RECEIPTTIME"));
				tableData[currentpage*30-30+i+1]=receiveAlarmInfo.toStringarray();
				receiveAlarmInfos.add(receiveAlarmInfo);
			}
			//matrixTableAdapter.setInformation(tableData);
			/*Message msg =new Message();
			msg.what=1;
			handler.sendMessage(msg);*/

		} catch (JSONException e) {
			//ToastUtil.makeText(AlarmManageActivity.this, e.toString(), 0);
			e.printStackTrace();
		}
		return receiveAlarmInfos;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_starttime:
				Dialog jiejingStartDateDialog = new DateDialog(AlarmManageActivity.this,
						starttime);
				jiejingStartDateDialog.show();
				break;
			case R.id.tv_endtime:
				Dialog jiejingEndDateDialog = new DateDialog(AlarmManageActivity.this,endtime);
				jiejingEndDateDialog.show();
				break;
			case R.id.jiejingmanage_returnBtn:
				AlarmManageActivity.this.finish();
			case R.id.table:
				//获取当前选择feature 的 id
				int rownum=table.getDowny()/table.getViewHight()+table.getFirstRow();
				Toast.makeText(AlarmManageActivity.this, rownum+"", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
		}
	}
	@Override
	public void onLoadMore(int row) {
		currentpage++;
		new GetTableDataTask().execute(searchStrings);
		getresultformjson(result);
	}
	@Override
	protected void onStop() {
		super.onStop();
		susername=username;
		sresult=result;
		ProgressDialogUtil.stopProgressDialog();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		username=susername;
		result=sresult;
		try {
			//获取查询结果的总数
			totalnum = new JSONObject(result).getString("totalnum");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		searchStrings=new String[]{username,"","","","","","","","","",ischagang+""};//初始化查询数组
		orsearchStrings=new String[]{username,"","","","--请选择--","","--请选择--","","","--请选择--",ischagang+""};//初始化上一次查询数组
		headerstring=new String[]{"序号","当日编号","相同警号","报警地址","报警电话","通知区县","是否火灾","火灾类型","市局出警","接警时间"};
		tableData=new String[Integer.parseInt(totalnum)][headerstring.length];
		tableData[0]=headerstring;
		receiveAlarmInfos =getresultformjson(result);//解析服务端查询数据
		//获取服务接口
		webService=new WebServiceUtil(getApplicationContext());
		//初始化控件
		intiview();

	}

	//保存界面数据重绘时调用
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
  
}