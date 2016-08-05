package com.otitan.gyslfh.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.otitan.adapter.HuoJingAdapter;
import com.otitan.gyslfh.R;
import com.otitan.util.PadUtil;
import com.otitan.util.WebServiceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HuoQingActivity extends Activity {

	ListView huojingListVeiw;

	private String websUtilResult;
	private JSONObject object;
	WebServiceUtil wbsUtil;
	private String username, userID,realname,telno;

	private JSONObject obj = null;
	private JSONArray arr = null;
	ImageButton huijing_returnBtn;
	HuoJingAdapter hAdapter;
	List<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				arrayList.clear();
				websUtilResult = wbsUtil.getHuoJing(username);

				try {
					if (null != new JSONObject(websUtilResult)) {
						obj = new JSONObject(websUtilResult);
						arr = obj.optJSONArray("ds");
						for (int i = 0; i < arr.length(); i++) {
							object = arr.optJSONObject(i);
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("RECEIPTID", object.getString("ID").toString());
							map.put("BACKSTATE", object.getString("BACKSTATE").toString());
							map.put("ADRESS", object.getString("ADRESS").toString());
							map.put("TEL_ONE", object.getString("TEL_ONE").toString());
							map.put("RECEIPTTIME",object.getString("RECEIPTTIME").toString());
							map.put("ORIGIN", object.getString("ORIGIN").toString());
							arrayList.add(map);
						}
					}else{
						arrayList = null;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				hAdapter.notifyDataSetChanged();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		PadUtil padUtil = new PadUtil();
		if (padUtil.isPad(this)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		setContentView(R.layout.activity_huo_qing);

		Intent intent = getIntent();
		username = intent.getStringExtra("username");
		userID = intent.getStringExtra("userID");
		realname = intent.getStringExtra("REALNAME");
		telno = intent.getStringExtra("TELNO");
		wbsUtil = new WebServiceUtil(getApplicationContext());
		wbsUtil.initWebserviceTry();

		huijing_returnBtn = (ImageButton) findViewById(R.id.huijing_returnBtn);
		huijing_returnBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				HuoQingActivity.this.finish();
			}
		});

		huojingListVeiw = (ListView) findViewById(R.id.huojinglistView);

		if ("1".equalsIgnoreCase("1")) {
			arrayList.clear();
			websUtilResult = wbsUtil.getHuoJing(username);
			try {
				if (null != new JSONObject(websUtilResult)) {
					obj = new JSONObject(websUtilResult);
					arr = obj.optJSONArray("ds");
					for (int i = 0; i < arr.length(); i++) {
						object = arr.optJSONObject(i);
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("RECEIPTID", object.getString("ID").toString());
						map.put("BACKSTATE", object.getString("BACKSTATE")
								.toString());
						map.put("ADRESS", object.getString("ADRESS").toString());
						map.put("TEL_ONE", object.getString("TEL_ONE")
								.toString());
						map.put("RECEIPTTIME", object.getString("RECEIPTTIME")
								.toString());
						map.put("ORIGIN", object.getString("ORIGIN").toString());
						arrayList.add(map);
					}
				} else {
					arrayList = null;
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}

			hAdapter = new HuoJingAdapter(arrayList, HuoQingActivity.this);
			if (hAdapter != null) {
				huojingListVeiw.setAdapter(hAdapter);
			}

			huojingListVeiw.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(final AdapterView<?> parent,
						final View view, final int position, final long id) {
					Intent intent = new Intent(HuoQingActivity.this,
							HuiJingActivity.class);
					String RECEIPTID = null;
					String backid = null;
					String ADMINDIVISION = null;
					String dqname = null;
					String backState = null;
					try {
						if (arr.optJSONObject(position).getString("BACKSTATE").toString().equals("2")) {
							String heshiren = arr.optJSONObject(position).getString("CHECKER");
							String quxianchujing = arr.optJSONObject(position).getString("POLICESIUATION");
							String REMARK = arr.optJSONObject(position).getString("REMARK");
							intent.putExtra("CHECKER", heshiren);
							intent.putExtra("POLICESIUATION", quxianchujing);
							intent.putExtra("REMARK", REMARK);
						}
						backid = arr.optJSONObject(position).getString("ID");
						RECEIPTID = arr.optJSONObject(position).getString("RECEIPTID");
						ADMINDIVISION = arr.optJSONObject(position).getString("ADMINDIVISION");
						dqname = arr.optJSONObject(position).getString("DQ_NAME");
						backState = arr.optJSONObject(position).getString("BACKSTATE");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					intent.putExtra("USERID", userID);
					intent.putExtra("RECEIPTID", RECEIPTID);
					intent.putExtra("ID", backid);
					intent.putExtra("ADMINDIVISION", ADMINDIVISION);
					intent.putExtra("dqname", dqname);
					intent.putExtra("backState", backState);
					intent.putExtra("REALNAME", realname);//设备绑定的真实姓名
					intent.putExtra("TELNO", telno);//
					startActivityForResult(intent, 0);
				}
			});
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 0) {
			
			if (resultCode == RESULT_OK) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Message msg = new Message();
						msg.what = 1;
						msg.obj = true;
						handler.sendMessage(msg);
					}
				});
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.huo_qing, menu);
		return true;
	}

}
