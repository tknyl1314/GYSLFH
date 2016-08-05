package com.otitan.gyslfh.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.otitan.adapter.AlarmManageAdapter;
import com.otitan.entity.CustomProgressDialog;
import com.otitan.entity.DateDialog;
import com.otitan.entity.ReceiveAlarmInfo;
import com.otitan.gyslfh.R;
import com.otitan.util.PadUtil;
import com.otitan.util.WebServiceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Util.ProgressDialogUtil;

public class ManageActivity extends Activity implements OnClickListener {
	private ImageButton returnButton;
	private PullToRefreshListView pullToRefresh;
	private EditText alarmaddress, alarmnumber, number, equalalarm;
	private Button serch, refresh, huoqing, export;
	private Spinner county, isfire, neartime, dqspinner;
	private TextView starttime, endtime, iteminfo;
	private WebServiceUtil webService;
	private String username;
	private String result;
	// 查询总页数
	private String totalpage = "", totalnum = "";
	private int currentpage = 1;
	private int ischagang = 0;
	private ReceiveAlarmInfo receiveAlarmInfo;
	private List<ReceiveAlarmInfo> receiveAlarmInfos;
	// private PullToRefreshListView pullToRefresh;
	private AlarmManageAdapter adapter;
	private ArrayAdapter arrayAdapter;
	private ArrayAdapter<CharSequence> dqarrayAdapter;
	// private ProgressDialog progressDialog = null;
	private CustomProgressDialog progressDialog = null;
	// refreshnum

	// count rows number
	private int n = 30;

	private int flag = 0;// 1表示通过查询按钮查询

	private String[] searchStrings = new String[10];

	private String[] spinnervalue;

	private SharedPreferences sharedPreferences;

	private ArrayAdapter<String> spadpter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (PadUtil.isPad(this)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		setContentView(R.layout.activity_alarm_manage);
		webService = new WebServiceUtil(getApplicationContext());

		sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);

		Intent intent = getIntent();
		username = intent.getStringExtra("username");
		result = intent.getStringExtra("result");
		returnButton = (ImageButton) findViewById(R.id.alarm_manage_returnBtn);
		// listView = (ListView) findViewById(R.id.lv_alarm_manage);
		pullToRefresh = (PullToRefreshListView) findViewById(R.id.pullToRefresh);
		alarmaddress = (EditText) findViewById(R.id.et_address);
		alarmnumber = (EditText) findViewById(R.id.et_alarmnumber);
		number = (EditText) findViewById(R.id.et_number);
		equalalarm = (EditText) findViewById(R.id.et_equalalarm);
		starttime = (TextView) findViewById(R.id.tv_starttime);
		endtime = (TextView) findViewById(R.id.tv_endtime);
		isfire = (Spinner) findViewById(R.id.sp_isfire);
		neartime = (Spinner) findViewById(R.id.sp_neartime);
		dqspinner = (Spinner) findViewById(R.id.sp_dq);
		// dqspinner.setAdapter(dqarrayAdapter);
		serch = (Button) findViewById(R.id.btn_serch);
		refresh = (Button) findViewById(R.id.btn_refresh);

		iteminfo = (TextView) findViewById(R.id.tv_iteminfo);
		initsqspinner(sharedPreferences.getString("UNITID", ""), sharedPreferences.getString("DQLEVEL", ""));
		returnButton.setOnClickListener(this);
		starttime.setOnClickListener(this);
		endtime.setOnClickListener(this);
		serch.setOnClickListener(this);
		refresh.setOnClickListener(this);

		try {
			totalnum = new JSONObject(result).getString("totalnum");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// result=webService.getHuoJing(username);
		receiveAlarmInfos = getresultformjson(result);
		/*
		 * n=receiveAlarmInfos.size(); iteminfo.setText("当前已加载" + n + "条数据");
		 */
		setListAdapter(receiveAlarmInfos);
		pullToRefresh.setMode(Mode.PULL_FROM_END);
		init();
		searchStrings[0] = username;
		searchStrings[1] = "";
		searchStrings[2] = "";
		searchStrings[3] = "";
		searchStrings[4] = "";
		searchStrings[5] = "";
		searchStrings[6] = "";
		searchStrings[7] = "";
		searchStrings[8] = "";
		searchStrings[9] = "";
		pullToRefresh.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (flag == 1) {
					new GetDataTask().execute(searchStrings);
				}
				ProgressDialogUtil.startProgressDialog(ManageActivity.this);
				new GetDataTask().execute(searchStrings);
			}
		});

		ProgressDialogUtil.stopProgressDialog();
	}

	private void initsqspinner(String UNITID, String dqlevel) {

		if (dqlevel.equals("1") || dqlevel.equals("0")) {

			dqarrayAdapter = ArrayAdapter.createFromResource(ManageActivity.this, R.array.counties,
					android.R.layout.simple_spinner_dropdown_item);
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

	private void setListAdapter(List<ReceiveAlarmInfo> receiveAlarmInfos) {
		adapter = new AlarmManageAdapter(ManageActivity.this, receiveAlarmInfos);
		pullToRefresh.setAdapter(adapter);
		n = receiveAlarmInfos.size();
		iteminfo.setText("查询总数为：" + totalnum + "   当前已加载" + n + "条数据");
	}

	private void init() {
		ILoadingLayout startLabels = pullToRefresh.getLoadingLayoutProxy(true, false);
		startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("正在载入...");// 刷新时
		startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

		ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(false, true);
		endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在载入...");// 刷新时
		endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
	}

	private List<ReceiveAlarmInfo> getresultformjson(String houjingresult) {
		List<ReceiveAlarmInfo> receiveAlarmInfos = new ArrayList<ReceiveAlarmInfo>();
		try {
			JSONObject jsonobject = new JSONObject(houjingresult);

			totalpage = jsonobject.getString("totalpage");
			totalnum = jsonobject.getString("totalnum");
			JSONArray jsonArray = jsonobject.getJSONArray("ds");
			for (int i = 0; i < jsonArray.length(); i++) {
				receiveAlarmInfo = new ReceiveAlarmInfo();
				JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
				receiveAlarmInfo.setId(jsonObject.getString("ID"));
				receiveAlarmInfo.setUnionid(jsonObject.getString("UNIONID"));
				receiveAlarmInfo.setAdress(jsonObject.getString("ADRESS"));
				receiveAlarmInfo.setTelone(jsonObject.getString("TEL_ONE").replace(",", "").trim());
				receiveAlarmInfo.setOrigin(jsonObject.getString("DQ_NAME"));
				receiveAlarmInfo.setIsfire(jsonObject.getString("ISFIRE"));
				receiveAlarmInfo.setFiretype(jsonObject.getString("FIRETYPE"));
				receiveAlarmInfo.setBackstate(jsonObject.getString("BACKSTATE").equals("2") ? "已回警" : "未回警");
				receiveAlarmInfo.setAlarmtime(jsonObject.getString("RECEIPTTIME"));
				receiveAlarmInfos.add(receiveAlarmInfo);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return receiveAlarmInfos;
	}

	private class GetDataTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		protected String doInBackground(String... params) {

			if (currentpage > Integer.parseInt(totalpage)) {
				return null;
			}
			result = webService.serchHuoJing(params[0], params[1], params[2], params[3], params[4], params[5],
					params[6], params[7], params[8], params[9], currentpage, ischagang);
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
				if (flag == 1) {
					receiveAlarmInfos.clear();
					flag = 0;
					// n=0;
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
				// pullToRefresh.setRefreshingLabel();
				ILoadingLayout endLabels = pullToRefresh.getLoadingLayoutProxy(false, true);
				// endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
				endLabels.setRefreshingLabel("所有数据已加载完成...");// 刷新时
				// endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
			}

			iteminfo.setText("查询总数为：" + totalnum + "   当前已加载" + n + "条数据");

			// Call onRefreshComplete when the list has been refreshed.
			pullToRefresh.onRefreshComplete();
			ProgressDialogUtil.stopProgressDialog();
			super.onPostExecute(result);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.alarm_manage_returnBtn:
			ProgressDialogUtil.stopProgressDialog();
			ManageActivity.this.finish();
			break;
		case R.id.tv_starttime:
			Dialog jiejingStartDateDialog = new DateDialog(ManageActivity.this, starttime);
			jiejingStartDateDialog.show();
			break;
		case R.id.tv_endtime:
			Dialog jiejingEndDateDialog = new DateDialog(ManageActivity.this, endtime);
			jiejingEndDateDialog.show();
			break;
		case R.id.btn_refresh:
			/*
			 * result=webService.getHuoJing("yyq"); receiveAlarmInfos
			 * =getresultformjson(result);
			 */
			ProgressDialogUtil.startProgressDialog(ManageActivity.this);
			pullToRefresh.setAdapter(adapter);
			ProgressDialogUtil.stopProgressDialog();
			// iteminfo.setText("共计有" + receiveAlarmInfos.size() + "条数据");
			break;
		case R.id.btn_serch:
			ProgressDialogUtil.startProgressDialog(ManageActivity.this);
			flag = 1;
			currentpage = 1;
			n = 0;
			searchStrings[0] = username;
			String id = searchStrings[1] = number.getText().toString();
			String equal = searchStrings[2] = equalalarm.getText().toString();
			String address = searchStrings[3] = alarmaddress.getText().toString();
			String dq = searchStrings[4] = dqspinner.getSelectedItem().toString();
			String phonenumber = searchStrings[5] = alarmnumber.getText().toString();
			String fireis = searchStrings[6] = isfire.getSelectedItem().toString();
			String start = searchStrings[7] = starttime.getText().toString();
			String near = searchStrings[9] = neartime.getSelectedItem().toString();
			if (start.equals("点击选择时间")) {
				searchStrings[7] = null;
			}
			String end = searchStrings[8] = endtime.getText().toString();
			if (end.equals("点击选择时间")) {
				searchStrings[8] = null;
			}
			new GetDataTask().execute(searchStrings);
			// String start= searchStrings[7] =starttime.getText().toString();
			// String end= searchStrings[8] =endtime.getText().toString();

			// searchStrings=new
			// String[]{username,id,equal,address,dq,phonenumber,fireis,start,end,near,currentpage+""};

			// List<ReceiveAlarmInfo> receiveAlarmInfos1=new
			// ArrayList<ReceiveAlarmInfo>();
			// result=webService.serchHuoJing(username,id,equal,address,dq,phonenumber,fireis,start,end,near,currentpage);
			// receiveAlarmInfos1 =getresultformjson(result);
			// Adapter2=new AlarmManageAdapter(ManageActivity.this,
			// receiveAlarmInfos1);
			// pullToRefresh.setAdapter(Adapter2);
			// iteminfo.setText("共计有" + receiveAlarmInfos1.size() + "条数据");

			break;
		default:
			break;
		}
	}
}