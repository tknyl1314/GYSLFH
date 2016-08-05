package com.otitan.api;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.esri.android.map.MapView;
import com.otitan.entity.WeatherEntity;
import com.otitan.gyslfh.R;
import com.otitan.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class QueryWeatherTask extends
		AsyncTask<String, Void, List<WeatherEntity>>
{
	Context mcontext;
	String lon, lat;
	PopupWindow popwindow;
	MapView mapview;

	public QueryWeatherTask(Context context, MapView mapview)
	{
		this.mcontext = context;
		this.mapview = mapview;
	}

	/**
	 * 通过经纬度获取气象信息
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<WeatherEntity> SearchByCoordinates(String latitude,
			String longitude)
	{
		int responseCode = -1;
		List<WeatherEntity> weathrelist = new ArrayList<>();
		try
		{
			URL apiURL = new URL(
					"http://api.openweathermap.org/data/2.5/forecast?units=metric&lat="
							+ latitude
							+ "&lon="
							+ longitude
							+ "&lang=zh_cn&appid=44db6a862fba0b067b1930da0d769e98");
			HttpURLConnection connection = (HttpURLConnection) apiURL
					.openConnection();
			connection.connect();
			responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK)
			{
				/*
				 * InputStream inputStream = connection.getInputStream(); Reader
				 * reader = new InputStreamReader(inputStream); int
				 * contentLength = connection.getContentLength(); char[]
				 * charArray = new char[contentLength]; reader.read(charArray);
				 * String responseData = new String(charArray);
				 */
				String sCurrentLine = "";
				String sTotalString = "";
				InputStream urlStream;
				urlStream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(urlStream, "utf-8"));// 解决乱码问题

				while ((sCurrentLine = reader.readLine()) != null)
				{
					if (!sCurrentLine.equals(""))
						sTotalString += sCurrentLine;
				}

				JSONObject jsonResponse = new JSONObject(sTotalString);
				JSONArray list = jsonResponse.getJSONArray("list");
				SimpleDateFormat df = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat df3 = new SimpleDateFormat("HH");
				Calendar c = Calendar.getInstance();
				Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
				String curtime = df.format(curDate);
				String curdate = df2.format(curDate);
				c.roll(Calendar.DAY_OF_YEAR, 1);
				String tomorrowdate = df2.format(c.getTime());
				c.roll(Calendar.DAY_OF_YEAR, 1);
				String Acquired = df2.format(c.getTime());
				int curhour = Integer.parseInt(df3.format(curDate));
				int hour = (curhour / 3) * 3;
				String hour2 = new DecimalFormat("00").format(hour);

				for (int i = 0; i < list.length(); i++)
				{
					JSONObject obj = (JSONObject) list.get(i);
					WeatherEntity we = new WeatherEntity();
					String time = obj.getString("dt_txt");
					JSONObject windobj = obj.getJSONObject("wind");// 风
					JSONObject tempobj = obj.getJSONObject("main");// 温度
					JSONArray weatherarr = obj.getJSONArray("weather");// 天气
					JSONObject weatherobj = (JSONObject) weatherarr.get(0);// 温度
					if (time.equals(curdate + " " + hour2 + ":00:00"))
					{
						we.setTime(time);// 时间
						we.setDirection(windobj.getString("deg"));// 风向
						we.setSpeed(windobj.getString("speed"));// 风速
						we.setHumidity(tempobj.getString("humidity"));// 湿度
						we.setTmp(tempobj.getString("temp"));// 温度
						we.setMaxtmp(tempobj.getString("temp_max"));
						we.setMintmp(tempobj.getString("temp_min"));
						we.setDescription(weatherobj.getString("description"));
						weathrelist.add(we);
					} else if (time.equals(tomorrowdate + " 09:00:00"))
					{
						we.setTime(time);// 时间
						we.setDirection(windobj.getString("deg"));// 风向
						we.setSpeed(windobj.getString("speed"));// 风速
						we.setTmp(tempobj.getString("temp"));// 温度
						we.setHumidity(tempobj.getString("humidity"));// 湿度
						we.setMaxtmp(tempobj.getString("temp_max"));
						we.setMintmp(tempobj.getString("temp_min"));
						we.setDescription(weatherobj.getString("description"));
						weathrelist.add(we);
					} else if (time.equals(Acquired + " 09:00:00"))
					{
						we.setTime(time);// 时间
						we.setDirection(windobj.getString("deg"));// 风向
						we.setSpeed(windobj.getString("speed"));// 风速
						we.setTmp(tempobj.getString("temp"));// 温度
						we.setHumidity(tempobj.getString("humidity"));// 湿度
						we.setMaxtmp(tempobj.getString("temp_max"));
						we.setMintmp(tempobj.getString("temp_min"));
						we.setDescription(weatherobj.getString("description"));
						weathrelist.add(we);
					}

				}

			}

		} catch (MalformedURLException e)
		{
			Log.e("log_tag", "Error In URL" + e.toString());
			return null;
		} catch (IOException e)
		{
			Log.e("log_tag", "Error In URL" + e.toString());
			return null;
		} catch (Exception e)
		{
			Log.e("log_tag", "Error In URL" + e.toString());
			return null;
		}
		return weathrelist;

		// return "Code :"+responseCode;
	}

	@Override
	protected List<WeatherEntity> doInBackground(String... arg0)
	{
		List<WeatherEntity> weatherlist = SearchByCoordinates(arg0[0], arg0[1]);
		return weatherlist;
	}

	@Override
	protected void onPostExecute(List<WeatherEntity> result)
	{
		if (result != null)
		{
			// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
			String weather = "";
			View poplayout = LayoutInflater.from(mcontext).inflate(
					R.layout.popup_weather, null);
			popwindow = new PopupWindow(poplayout, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			TextView tv = (TextView) poplayout.findViewById(R.id.popweather_tv);
			SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
			String today = sdf.format(System.currentTimeMillis());
			Calendar c = Calendar.getInstance();
			c.roll(Calendar.DAY_OF_YEAR, 1);
			String tomorrowdate = sdf.format(c.getTime());
			c.roll(Calendar.DAY_OF_YEAR, 1);
			String Acquired = sdf.format(c.getTime());
			int i = 0;
			for (WeatherEntity weatherEntity : result)
			{
				if (i == 0)
				{
					weather += "[实况] " + weatherEntity.getDescription() + " 温度"
							+ weatherEntity.getTmp() + "℃ 湿度"
							+ weatherEntity.getHumidity() + " 风速"
							+ weatherEntity.getSpeed() + " 风向"
							+ weatherEntity.getDirection() + "\n 发布时间："
							+ weatherEntity.getTime() + "\n" + today + " "
							+ weatherEntity.getDescription() + " "
							+ weatherEntity.getTmp() + "℃\n";
				} else if (i == 1)
				{
					weather += tomorrowdate + " "
							+ weatherEntity.getDescription() + " "
							+ weatherEntity.getTmp() + "℃\n";
				} else if (i == 2)
				{
					weather += Acquired + " " + weatherEntity.getDescription()
							+ " " + weatherEntity.getTmp() + "℃\n";
				}
				i++;
			}
			tv.setText(weather);
			popwindow.setBackgroundDrawable(mcontext.getResources()
					.getDrawable(android.R.color.white));
			popwindow.setFocusable(true);
			popwindow.setOutsideTouchable(true);
			popwindow.showAtLocation(mapview, Gravity.CENTER, 0, 0);
		} else
		{
			ToastUtil.setToast((Activity) mcontext, "天气信息获取失败");
		}

	}
}
