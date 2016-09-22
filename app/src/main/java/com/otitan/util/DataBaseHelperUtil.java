package com.otitan.util;

import android.app.Activity;
import android.content.Context;

import com.esri.core.geometry.Point;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jsqlite.Callback;
import jsqlite.Database;
import jsqlite.Exception;

public class DataBaseHelperUtil
{
	public static DataBaseHelperUtil dataBaseHelperUtil;

	public static DataBaseHelperUtil getDataBaseHelperUtil()
	{
		return dataBaseHelperUtil;
	}

	public static void setDataBaseHelperUtil(
			DataBaseHelperUtil dataBaseHelperUtil)
	{
		DataBaseHelperUtil.dataBaseHelperUtil = dataBaseHelperUtil;
	}

	public synchronized static DataBaseHelperUtil getInstance(Context context)
	{
		return dataBaseHelperUtil;
	}

	public static List<Map<String, Object>> getAddressInfo(Context context,
			String dbname, ResourcesManager manager, String keyword)
	{
		final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// select * from users where name like %searcherFilter% ;
		try
		{
			String databaseName = manager.getDataBase((Activity) context,
					dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READONLY);
			String sql = "select * from station where name like '%" + keyword
					+ "%'";
			db.exec(sql, new Callback()
			{

				@Override
				public void types(String[] arg0)
				{
				}

				@Override
				public boolean newrow(String[] data)
				{// 3 5 6
					Object name = data[2];
					String lon = data[3];
					String lau = data[4];
					String type = data[5];
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("filename", name + "(" + type + ")");
					Point point = new Point(Double.valueOf(lon), Double
							.valueOf(lau));
					map.put("point", point);
					list.add(map);
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
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (java.lang.Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}

	// 添加数据到本地数据库
	public static void addPointGuiji(Context context, ResourcesManager manager,
			String dbname, String sbh, double lon, double lat, String time,
			String state)
	{
		try
		{
			String databaseName = manager.getDataBase(context, dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into point values(null," + lon + "," + lat
					+ ",'" + sbh + "','" + time + "'," + state
					+ ",geomfromtext('POINT(" + lon + " " + lat + ")',2343))";
			db.exec(sql, null);
			db.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (java.lang.Exception e)
		{
			e.printStackTrace();
		}
	}

	// 足迹查询
	public static List<Map<String, Object>> selectPointGuiji(Context context,
			ResourcesManager manager, String dbname, String sbh,
			String startTime, String endTime)
	{
		final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try
		{
			String databaseName = manager.getDataBase(context, dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "SELECT * FROM point WHERE SBH ='" + sbh
					+ "' and time between datetime('" + startTime
					+ "') and datetime('" + endTime
					+ "') order by datetime(time) desc";
			db.exec(sql, new Callback()
			{

				@Override
				public void types(String[] arg0)
				{

				}

				@Override
				public boolean newrow(String[] data)
				{// 3 5 6

					if (data[1] != null && data[2] != null && data[3] != null)
					{
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("lon", data[1]);
						map.put("lat", data[2]);
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
		} catch (java.lang.Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}

	/* 获取小地名查询结果 */
	public static List<Map<String, Object>> selSearchResult(Context context,
			ResourcesManager manager, String keyword)
	{

		final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try
		{
			String databaseName = manager.getDataBase(context, "db.sqlite");
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			// String sql =
			// "SELECT * FROM History WHERE time order by datetime(time) desc limit 15";
			String sql = "select * from station where name like '%" + keyword
					+ "%'";
			db.exec(sql, new Callback()
			{
				String[] columns;

				@Override
				public void types(String[] arg0)
				{

				}

				@Override
				public boolean newrow(String[] data)
				{
					Map<String, Object> map = new HashMap<String, Object>();
					for (int i = 0; i < columns.length; i++)
					{
						map.put(columns[i], data[i]);
					}
					list.add(map);
					return false;
				}

				@Override
				public void columns(String[] arg0)
				{
					columns = arg0;
				}
			});
			db.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (java.lang.Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}

	/* 获取登录用户历史记录 */
	public static List<Map<String, Object>> selDataHistory(Context context,
			ResourcesManager manager, String dbname)
	{
		final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try
		{
			String databaseName = manager.getDataBase(context, dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "SELECT * FROM History WHERE time order by datetime(time) desc limit 15";
			db.exec(sql, new Callback()
			{

				@Override
				public void types(String[] arg0)
				{

				}

				@Override
				public boolean newrow(String[] data)
				{
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("filename", data[0]);
					map.put("time", data[1]);
					map.put("lon", data[2]);
					map.put("lat", data[3]);
					list.add(map);
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
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (java.lang.Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}

	// 保存 历史查询数据
	public static void addDataToHistory(Context context,
			ResourcesManager manager, String dbname, String searchTxt)
	{

		try
		{
			String databaseName = manager.getDataBase(context, dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sql = "insert into History values('" + searchTxt + "','"
					+ dateFormat.format(new Date()) + "',null,null)";
			// String sql = "select * from station where name like '%" +
			// searchTxt + "%'";
			db.exec(sql, null);
			db.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (java.lang.Exception e)
		{
			e.printStackTrace();
		}
	}

	static boolean sel_result = false;

	public static boolean selDataHistoryByString(Context context,
			ResourcesManager manager, String dbname, String searchTxt)
	{
		try
		{
			String databaseName = manager.getDataBase(context, dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			// String sql =
			// "insert into History values('"+searchTxt+"','"+dateFormat.format(new
			// Date())+"',null,null)";
			// String sql = "select * from History where name like '%" +
			// searchTxt + "%'";
			String sql = "select * from History where name ='" + searchTxt
					+ "'";
			db.exec(sql, new Callback()
			{

				@Override
				public void types(String[] arg0)
				{
				}

				@Override
				public boolean newrow(String[] arg0)
				{
					if (arg0.length > 0)
					{
						sel_result = true;
					}
					return true;
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
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (java.lang.Exception e)
		{
			e.printStackTrace();
		}
		return sel_result;
	}

	/* 获取db.sqlite 中user表中的历史用户名和密码 */
	public static ArrayList<Map<String, String>> getUserList(Context context,
			String dbname)
	{
		final ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

		try
		{
			String filename = ResourcesManager.getInstance(context)
					.getDataBase(context, dbname);
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new Database();
			db.open(filename, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "select * from user ";
			db.exec(sql, new Callback()
			{

				@Override
				public boolean newrow(String[] data)
				{
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", data[0]);
					map.put("pw", data[1]);
					list.add(map);
					return false;
				}

				@Override
				public void columns(String[] arg1)
				{

				}

				@Override
				public void types(String[] arg2)
				{

				}
			});
			db.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (java.lang.Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}

}
