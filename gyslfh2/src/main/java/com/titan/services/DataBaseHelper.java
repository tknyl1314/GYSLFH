package com.titan.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.titan.forestranger.MyApplication;
import com.titan.model.TrackPoint;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jsqlite.Callback;
import jsqlite.Database;

public class DataBaseHelper {
      private static String dbname = "SMLY.sqlite";
	 static Map<String, String> map=null;

	/**
	 * 查询当前位置所在区县代码
	 */
		/*public static Map<String, String> queryDistrict(Point p)
		{
			
			
			try {
				//String databaseName =  ResourcesManager.getDataBase("guiji.sqlite");
                String databaseName =  ResourcesManager.getDataBase(dbname);
				Class.forName("jsqlite.JDBCDriver").newInstance();
				Database db = new Database();
				db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
				String sql = "select NAME,CODE from gy_jx where MbrWithin( MakePoint("+p.getX()+","+p.getY()+",2343) ,Geometry)";
				db.exec(sql, new Callback() {
					@Override
                    public void types(String[] arg0) {
                        // TODO Auto-generated method stub
                    }
					
					@Override
					public boolean newrow(String[] row) {
						map=new HashMap<String, String>();
						*//*map=new HashMap<String, String>();
						for(int i=0;i<columns.length;i++){
							
							if(columns[i].equals("NAME")){
								map.put("name", row[i]);
							}else if(columns[i].equals("CODE")){
								map.put("name", row[i]);
							}
						}*//*
						map.put("NAME", row[0]);
						map.put("CODE", row[1]);
						return true;
					}
					
					@Override
					public void columns(String[] arg0) {
					}
				});
				db.close();
			} catch (java.lang.Exception e) {
				e.printStackTrace();
				//logger.debug(LoggerManager.getExceptionMessage(e));
				return map;
			}
			return map;
		}*/

	/**
	 * 检查文件是否存在
	 */
		public static boolean checkDataBase(String fileDir, String filename)
		{
			SQLiteDatabase checkDB = null;
			String myPath = fileDir + "/" + filename;
			try
			{
				checkDB = SQLiteDatabase.openDatabase(myPath, null,
						SQLiteDatabase.OPEN_READONLY);
			} catch (SQLiteException e)
			{
				// database does't exist yet.
				return false;
			}
			if (checkDB != null)
			{
				checkDB.close();
			}
			return checkDB != null ? true : false;
		}

		/**
		 * 从安装文件中拷贝到设备中
		 */
		public static  void CopyDatabase(Context context, String fileDir, String filename)
		{
			try
			{
				InputStream db = context.getResources().getAssets().open(filename);
				FileOutputStream fos = new FileOutputStream(fileDir + "/"
						+ filename);
				byte[] buffer = new byte[8129];
				int count = 0;

				while ((count = db.read(buffer)) >= 0)
				{
					fos.write(buffer, 0, count);
				}

				fos.close();
				db.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	/**
	 * 上传轨迹信息到本地数据库
	 */
         public static boolean UploadLocalDatebase(String SBH, String LON, String LAT, String time, String state) {
			try {
				// String string = SBH;
				//String databaseName =  ResourcesManager.getDataBase(dbname);
				String dbpath= MyApplication.getFilePath()+"/"+dbname;
				Class.forName("jsqlite.JDBCDriver").newInstance();
				Database db = new Database();
				db.open(dbpath, jsqlite.Constants.SQLITE_OPEN_READWRITE);
				String sql = "insert into point (sbh,lon,lat,time,state,Geometry) values ('" + SBH + "','" + LON+ "','" + LAT + "','" + time + "','" + state + "',GeomFromText('POINT(" + LON + " " + LAT + ")',2343))";
				//String sql2 = "insert into point (sbh,lon,lat,time,state,Geometry) values ('" + SBH + "','" + LON+ "','" + LAT + "','" + time + "','" + state + "',GeomFromText('POINT(" + LON + " " + LAT + ")',2343))";
				Log.e("", "enter:WebServiceUtil.UploadLocalDatebase():sql:" + sql);
				Log.d("mylist", sql);
				db.exec(sql, null);
				db.close();
				Log.i("guiji","上传轨迹信息到本地数据库");
			} catch (Exception e) {
                return false;
				//e.printStackTrace();
				//logger.debug(LoggerManager.getExceptionMessage(e));

			}
			return true;
		}

	/** 搜索所有本地没有上传服务器的点 */
	public static List<Map<String, Object>> selectPointGuiji(String sbh)
	{
		final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try
		{
            String dbpath= MyApplication.getFilePath()+"/"+dbname;
			//String databaseName = ResourcesManager.getDataBase(dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new Database();
			db.open(dbpath, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "SELECT pkuid,lon,lat,sbh,time FROM point WHERE state ='" + 0 + "' and sbh ='" + sbh + "' ";
			db.exec(sql, new Callback()
			{

				@Override
				public void types(String[] arg0)
				{

				}

				@Override
				public boolean newrow(String[] data)
				{

					if (data[0] != null&&data[1] != null && data[2] != null && data[3] != null)
					{
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("id", data[0]);
						map.put("lon", data[1]);
						map.put("lat", data[2]);
						map.put("sbh", data[3]);
						map.put("time", data[4]);
						list.add(map);
					}
					return false;
				}

				@Override
				public void columns(String[] arg0)
				{
				}
			});
			db.close();
		} catch (Exception e)
		{
			e.printStackTrace();
			//logger.debug(LoggerManager.getExceptionMessage(e));
		}
		return list;
	}

	/**
	 * 获取当前设备轨迹信息
	 */

		@SuppressLint("SimpleDateFormat")
		public static List<TrackPoint> getTrack(String starttime, String endtime) throws Exception {
			//final List<TrackModel> locations = new ArrayList<TrackModel>();
			final List<TrackPoint> locations = new ArrayList<TrackPoint>();
			try {
				//String databaseName = ResourcesManager.getDataBase(dbname);
                String dbpath= MyApplication.getFilePath()+"/"+dbname;
				Class.forName("jsqlite.JDBCDriver").newInstance();
				Database db = new Database();
				db.open(dbpath, jsqlite.Constants.SQLITE_OPEN_READWRITE);
				//String sql = "select * from BUSI_COMMON_MOBILE_ZB_REALTIME";
				final SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				/*final Date start = df2.parse(starttime);
				final Date end = df2.parse(endtime);*/
				String sql = "select * from point where time >='" + starttime+ "' and time <='" + endtime+ "' and sbh ='"+ MyApplication.SBH+"' order by datetime(time) asc";
	            //select * from point where time between datetime('Sat Mar 01 10:26:58 GMT+08:00 2014') and datetime('Sat Mar 01 10:26:58 GMT+08:00 2016')
				//final SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

				db.exec(sql, new Callback() {
					@Override
					public boolean newrow(String[] data) {
						TrackPoint tpoint = new TrackPoint();

						Date datatme = null;
						if (!data[4].toString().equals("")) {
							//Point point=new Point(Double.parseDouble(data[1]), Double.parseDouble(data[2]));
							tpoint.setLongitude(Double.parseDouble(data[1]));
							tpoint.setLatitude(Double.parseDouble(data[2]));
							tpoint.setTime(data[4]);
							locations.add(tpoint);
						}

						return false;
					}

					@Override
					public void columns(String[] arg1) {

					}

					@Override
					public void types(String[] arg2) {
					}
				});
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

			return locations;
		}

	/** 更新本地轨迹点表 */
	public static void updatePointGuiji(String id)
	{
		try
		{
			//String databaseName = ResourcesManager.getDataBase(dbname);
            String dbpath= MyApplication.getFilePath()+"/"+dbname;
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new Database();
			db.open(dbpath, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "update point set state = '"+ 1 +"' where pkuid = '"+ id + "'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e)
		{
			e.printStackTrace();
			//logger.debug(LoggerManager.getExceptionMessage(e));
		}
    }





}
