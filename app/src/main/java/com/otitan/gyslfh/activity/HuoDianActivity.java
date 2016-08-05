package com.otitan.gyslfh.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.widget.ListView;

import com.otitan.adapter.HuoDianAdapter;
import com.otitan.gyslfh.R;
import com.otitan.util.PadUtil;
import com.otitan.util.WebServiceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HuoDianActivity extends Activity {

	ListView huodianlistView;
	private String webSerResult, username,DQLEVEL;
	private JSONObject object;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		PadUtil padUtil = new PadUtil();
		if (padUtil.isPad(this)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		setContentView(R.layout.activity_huo_dian);

		Intent intent = getIntent();
		username = intent.getStringExtra("username");
		DQLEVEL = intent.getStringExtra("");
		huodianlistView = (ListView) findViewById(R.id.huodianlistView);
		final WebServiceUtil websUtil = new WebServiceUtil(
				getApplicationContext());
		
		(new Thread() {
			public void run() {
				webSerResult = websUtil.selHuoDian(username,DQLEVEL,"");
			};
		}).start();

		JSONObject obj = null;
		JSONArray arr = null;
		try {
			obj = new JSONObject(webSerResult);
			arr = obj.optJSONArray("ds");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		List<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		if (arr != null) {
			try {
				for (int i = 0; i < arr.length(); i++) {
					object = arr.optJSONObject(i);
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("ID", i + 1);
					map.put("X", object.getString("X").toString());
					map.put("Y", object.getString("Y").toString());
					arrayList.add(map);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		HuoDianAdapter hdAdapter = new HuoDianAdapter(arrayList,
				getApplicationContext());
		if (hdAdapter != null) {
			huodianlistView.setAdapter(hdAdapter);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.huo_dian, menu);
		return true;
	}

}
