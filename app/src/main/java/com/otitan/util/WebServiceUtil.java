package com.otitan.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.otitan.entity.Menu;
import com.otitan.entity.ReceiveAlarmInfo;
import com.otitan.entity.TrackModel;
import com.otitan.gyslfh.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jsqlite.Callback;
import jsqlite.Database;

public class WebServiceUtil {

	private String nameSpace = "http://tempuri.org/";
	private String methodName = null;
	private String soapAction = null;
	private String urlWebService;
	private static int timeout = 5000;
	private Context context;
	public static String NetworkException = "网络错误";
	public static String CharException = "用户名中包含特殊字符";
	public static String psException = "用户名或密码错误";
	public static String netException = "网络异常";
	public static String serverException = "服务器异常";

	public WebServiceUtil(Context context) {
		this.context = context;
		urlWebService = context.getResources().getString(R.string.webservice);
		initWebserviceTry();
	}

	/**
	 * 录入设备使用者信息
	 *
	 * @param sysname
	 *            使用者名称
	 * @param tel
	 *            使用者电话
	 * @param dw
	 *            单位
	 * @param sbmc
	 *            设备名称
	 * @param sbh
	 *            设备号
	 * @return
	 */
	public String addMobileSysInfo(String sysname, String tel, String dw, String sbmc, String sbh, String xlh) {
		methodName = "addMoblieSysInfo";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("sysname", sysname);
		soapObject.addProperty("tel", tel);
		soapObject.addProperty("dz", dw);
		soapObject.addProperty("sbmc", sbmc);
		soapObject.addProperty("sbh", sbh);
		soapObject.addProperty("xlh", xlh);
		return getResult(soapObject, "addMoblieSysInfoResult");
	}

	/**
	 * 添加设备信息
	 *
	 * @param macAddress
	 *            设备识别号
	 * @param xlh
	 *            设备序列号
	 * @param type
	 *            设备型号
	 * @return 返回结果 1、 已录入 2、录入成功 3、录入失败
	 */
	public String addMacAddress(String macAddress, String xlh, String type) {

		methodName = "addMacAddress";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("macAddress", macAddress);
		soapObject.addProperty("xlh", xlh);
		soapObject.addProperty("type", type);
		return getResult(soapObject, "addMacAddressResult");
	}

	/**
	 * 登陆 只做了用户名的特殊字符验证
	 * @param loginName
	 * @param loginPassword
	 * @return
	 */
	public String CheckLogin(String loginName, String loginPassword) {

		String result = null;
		SoapObject soapObject=null;
		if (stringCheck(loginName)) {
			String methodName = "CheckLogin";
			soapAction = "http://tempuri.org/" + methodName;
			soapObject = new SoapObject(nameSpace, methodName);
			soapObject.addProperty("username", loginName);
			soapObject.addProperty("password", loginPassword);
			result = getResult(soapObject, "CheckLoginResult");
		} else {
			result = CharException;
		}
		return result;
	}

	/**
	 * 修改火情状态
	 * @param state
	 * @return
	 */
	public boolean updateFireinfoState(int id, int state, String endTime) {
		boolean result = false;

		String methodName = "updateFireIfor";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);
		soapObject.addProperty("state", state);
		soapObject.addProperty("endTime", endTime);
		SoapSerializationEnvelope envelope = getEnvelope(soapObject);
		if (envelope == null) {
			return false;
		}

		SoapObject object = null;
		try {
			object = (SoapObject) envelope.bodyIn;
		} catch (Exception e) {
			return result;
		}
		if (object != null) {
			result = Boolean.valueOf(object.getProperty("updateFireIforResult").toString());
		}
		return result;
	}

	/* 获取设备信息及使用者信息 */
	public String selMobileInfo(String sbh) {
		methodName = "selMobileInfo";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("sbh", sbh);
		return getResult(soapObject, "selMobileInfoResult");
	}

	/**
	 * 添加火情
	 *
	 * @param CITY
	 * @param COUNTY
	 * @param TOWN
	 * @param VILLAGE
	 * @param PLACE
	 * @param FIRESTART
	 * @param FIREEND
	 * @param REMARK
	 * @param USERID
	 * @param X
	 * @param Y
	 * @param FIRE_STATE
	 * @return
	 */
	public boolean upFireInfo(String CITY, String COUNTY, String TOWN, String VILLAGE, String PLACE, String FIRESTART,
							  String FIREEND, String REMARK, String USERID, String X, String Y, String FIRE_STATE, String UNITID,
							  String devisionID, String isUpSJ) {

		boolean result = false;
		String methodName = "addFireIfor";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("CITY", CITY);
		soapObject.addProperty("COUNTY", COUNTY);
		soapObject.addProperty("TOWN", TOWN);
		soapObject.addProperty("VILLAGE", VILLAGE);
		soapObject.addProperty("PLACE", PLACE);
		soapObject.addProperty("FIRESTART", FIRESTART);
		soapObject.addProperty("FIREEND", FIREEND);
		soapObject.addProperty("REMARK", REMARK);
		soapObject.addProperty("USERID", USERID);
		soapObject.addProperty("X", X);
		soapObject.addProperty("Y", Y);
		soapObject.addProperty("FIRE_STATE", FIRE_STATE);
		soapObject.addProperty("UNITID", UNITID);
		soapObject.addProperty("devisionID", devisionID);
		soapObject.addProperty("isUpSJ", isUpSJ);

		SoapSerializationEnvelope envelope = getEnvelope(soapObject);
		if (envelope == null) {
			return false;
		}
		SoapObject object = null;
		try {
			object = (SoapObject) envelope.bodyIn;
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (object != null) {
			try {
				result = Boolean.parseBoolean(((SoapObject) object).getProperty("addFireIforResult").toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 火警上报
	 *
	 * @param CITY
	 * @param COUNTY
	 * @param TOWN
	 * @param VILLAGE
	 * @param PLACE
	 * @return
	 */
	public boolean updateAlarmFireInfor(String CITY, String COUNTY, String TOWN, String VILLAGE, String PLACE,
										String NAME, String TEL, String USERID, String LON, String LAT, String devisionID, String FIRETYPE, String ISFIRE,String REMARK, String IMAGE_ID) {

		boolean result = false;
		String methodName = "updateAlarmFireInfor";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("CITY", CITY);
		soapObject.addProperty("COUNTY", COUNTY);
		soapObject.addProperty("TOWN", TOWN);
		soapObject.addProperty("VILLAGE", VILLAGE);
		soapObject.addProperty("PLACE", PLACE);
		soapObject.addProperty("LON", LON);
		soapObject.addProperty("LAT", LAT);
		soapObject.addProperty("devisionID", devisionID);//地区id
		soapObject.addProperty("NAME", NAME);//报警人姓名
		soapObject.addProperty("TEL", TEL);//报警人电话
		soapObject.addProperty("USERID", USERID);//用户id
		soapObject.addProperty("FIRETYPE", FIRETYPE);
		soapObject.addProperty("ISFIRE", ISFIRE);
		soapObject.addProperty("REMARK", REMARK);//备注
		soapObject.addProperty("IMAGE_ID", IMAGE_ID);
		//soapObject.addProperty("STAUS", IMAGE_ID);


		SoapSerializationEnvelope envelope = getEnvelope(soapObject);
		if (envelope == null) {
			return false;
		}
		SoapObject object = null;
		try {
			object = (SoapObject) envelope.bodyIn;
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (object != null) {
			try {
				result = Boolean.parseBoolean(((SoapObject) object).getProperty("updateAlarmFireInforResult").toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 根据用户名查询用户所在区域的火情（火点）
	 *
	 * @return
	 * @throws Exception
	 */
	public String selHuoDian(String COUNTY, String DQLEVEL, String UNITID) {
		String methodName = "selHuoDian";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("COUNTY", COUNTY);
		soapObject.addProperty("DQLEVEL", DQLEVEL);
		soapObject.addProperty("UNITID", UNITID);
		return getResult(soapObject, "selHuoDianResult");
	}

	/**
	 * 查询所在区域的接警信息
	 *
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public String serchHuoJing(String username, String id, String unionid, String address, String dq,
							   String alarmnumber, String isfire, String starttime, String endtime, String neartime, int currentpage,
							   int ischagang) {
		String methodName = "serchHuoJing";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("username", username);
		soapObject.addProperty("id", id);
		soapObject.addProperty("unionid", unionid);
		soapObject.addProperty("address", address);
		soapObject.addProperty("dq", dq);
		soapObject.addProperty("alarmnumber", alarmnumber);
		soapObject.addProperty("isfire", isfire);
		// soapObject.addProperty("firetype", firetype);
		soapObject.addProperty("starttime", starttime);
		soapObject.addProperty("endtime", endtime);
		soapObject.addProperty("neartime", neartime);
		soapObject.addProperty("currentpage", currentpage);
		// 是否显示查岗数据 0表示不显示1表示显示
		soapObject.addProperty("ischagang", ischagang);
		return getResult(soapObject, "serchHuoJingResult");
	}

	/**
	 * 根据行号查询接警信息
	 *
	 * @return
	 * @throws Exception
	 */
	public String getJieJingInfobyRownum(String rownum, String[] searchStrings) {

		String methodName = "getJieJingInfobyRownum";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);

		soapObject.addProperty("username", searchStrings[0]);
		soapObject.addProperty("id", searchStrings[1]);
		soapObject.addProperty("unionid", searchStrings[2]);
		soapObject.addProperty("address", searchStrings[3]);
		soapObject.addProperty("dq", searchStrings[4]);
		soapObject.addProperty("alarmnumber", searchStrings[5]);
		soapObject.addProperty("isfire", searchStrings[6]);
		// soapObject.addProperty("firetype", firetype);
		soapObject.addProperty("starttime", searchStrings[7]);
		soapObject.addProperty("endtime", searchStrings[8]);
		soapObject.addProperty("neartime", searchStrings[9]);
		// soapObject.addProperty("currentpage", currentpage);
		// 是否显示查岗数据 0表示不显示1表示显示
		soapObject.addProperty("ischagang", searchStrings[10]);
		soapObject.addProperty("rownum", rownum);
		return getResult(soapObject, "serchHuoJingResult");

	}

	/**
	 * 动态获取菜单
	 *
	 * @return true：成功 false：失败
	 */
	public List<Menu> getMenu(String username) {
		List<Menu> menus = new ArrayList<Menu>();
		String result = null;
		methodName = "getmenu";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("name", username);// username
		SoapSerializationEnvelope envelope = getEnvelope(soapObject);
		if (envelope == null) {
			// 此时检查后台服务是否能正常打开 如果可以 则不是后台服务问题检查自身设备网络
			return null;
		}

		SoapObject object = null;
		try {
			object = (SoapObject) envelope.bodyIn;
			System.out.println("object返回数据：" + object);
		} catch (Exception e) {
			return null;
		}
		if (envelope.bodyIn instanceof SoapFault) {
			return null;
		} else {
			object = (SoapObject) envelope.bodyIn;
			if (object != null) {
				result = object.getProperty("getmenuResult").toString();
				org.json.JSONArray jsonArray;
				try {
					jsonArray = new org.json.JSONArray(result);
					for (int i = 0; i < jsonArray.length(); i++) {
						Menu menu = new Menu();
						JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
						menu.setId(jsonObject.getString("ID"));
						menu.setName(jsonObject.getString("NAME"));
						menu.setParentid(jsonObject.getString("PARENTID"));
						menus.add(menu);
					}
					return menus;
				} catch (JSONException e) {
					return null;
				}

			} else {
				return null;
			}
		}
	}

	/**
	 * 更新接警管理信息
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean updateJJInfo(ReceiveAlarmInfo receiveAlarmInfo) {
		boolean result = false;
		String[] data = receiveAlarmInfo.toStringarray();
		String methodName = "updateJJInfo";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		// string id,string dailyid,string unionid,string address,string
		// telone,string infomareawatcher,
		// string isfire,string firetype,string policecase,string receipttiem
		soapObject.addProperty("id", data[0]);
		soapObject.addProperty("dailyid", data[1]);
		soapObject.addProperty("unionid", data[2]);
		soapObject.addProperty("address", data[3]);
		soapObject.addProperty("telone", data[4]);
		soapObject.addProperty("infomareawatcher", data[5]);
		soapObject.addProperty("isfire", data[6]);
		soapObject.addProperty("firetype", data[7]);
		soapObject.addProperty("policecase", data[8]);
		soapObject.addProperty("receipttiem", data[9]);
		SoapSerializationEnvelope envelope = getEnvelope(soapObject);
		if (envelope == null) {
			return false;
		}
		SoapObject object = null;
		try {
			object = (SoapObject) envelope.bodyIn;
		} catch (Exception e) {
			return false;
		}

		if (object != null) {
			String reString = object.getProperty(methodName + "Result").toString();
			if (reString.equals("1")) {
				result = true;
			}
		} else {
			result = false;
		}
		return result;

	}

	/**
	 * 根据用户名查询用户所在区域的火情信息统计
	 *
	 * @throws Exception
	 */
	public String selDayHuoDian(String COUNTY, String DQLEVEL) {
		String methodName = "selHuoDian";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("COUNTY", COUNTY);
		soapObject.addProperty("DQLEVEL", DQLEVEL);

		return getResult(soapObject, "selHuoDianResult");
	}

	/**
	 *
	 * @param username
	 * @return
	 */
	public String getHuoJing(String username) {

		String methodName = "selHuoJing";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("username", username);
		return getResult(soapObject, "selHuoJingResult");
	}

	// 上传轨迹信息到服务器
	public boolean UPLonLat(String SBH, String LON, String LAT, String time) {

		methodName = "UPLonLat";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("SBH", SBH);
		soapObject.addProperty("LON", LON);
		soapObject.addProperty("LAT", LAT);
		soapObject.addProperty("time", time);
		SoapSerializationEnvelope envelope = getEnvelope(soapObject);
		SoapObject object = null;
		if (envelope.bodyIn instanceof SoapFault) {
			return false;
		} else {
			object = (SoapObject) envelope.bodyIn;
			if (object != null) {
				String result = object.getProperty("UPLonLatResult").toString();
				if (result.equals("0")) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	// 上传轨迹信息到本地数据库
	public boolean UploadLocalDatebase(String SBH, String LON, String LAT, String time) {
		try {
			// String string = SBH;

			//String databaseName = ResourcesManager.getInstance(context).getPath()[0] + "/maps/sqlite/guiji.sqlite";
			String databaseName = ResourcesManager.getInstance(context).getPath()[0] + "/maps/sqlite/GYSLFH.sqlite";
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			String sql = "insert into point (sbh,lon,lat,time,Geometry) values ('" + SBH + "','" + LON+ "','" + LAT + "','" + time + "',geomfromtext('POINT(" + LON + " " + LAT + ")',2343))";
			Log.e("", "enter:WebServiceUtil.UploadLocalDatebase():sql:" + sql);
			Log.d("mylist", sql);
			db.exec(sql, null);
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
		return true;
	}
	//获取接警编号
	public String getJieJingBainHao(String username) {

		String methodName = "getJieJingBainHao";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("username", username);

		return getResult(soapObject, "getJieJingBainHaoResult");
	}

	/**
	 * @param username
	 * @param DQLEVEL
	 * @param RECEIPTTIME
	 * @param ORIGIN
	 * @param ADRESS
	 * @return
	 */
	public boolean addJieJing(String username, String DQLEVEL, String ORIGIN, String RECEIPTTIME, String ADRESS,
							  String TEL_ONE, String POLICECASE, String POLICETIME, String USERID, String INFORMAREAWATCHER,String jjbh) {
		boolean result = false;
		String methodName = "addHuoJing";//接警录入
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("username", username);
		soapObject.addProperty("DQLEVEL", DQLEVEL);
		soapObject.addProperty("RECEIPTTIME", RECEIPTTIME);
		soapObject.addProperty("ORIGIN", ORIGIN);
		soapObject.addProperty("ADRESS", ADRESS);
		soapObject.addProperty("TEL_ONE", TEL_ONE);
		soapObject.addProperty("POLICECASE", POLICECASE);
		soapObject.addProperty("POLICETIME", POLICETIME);
		soapObject.addProperty("USERID", USERID);
		soapObject.addProperty("INFORMAREAWATCHER", INFORMAREAWATCHER);//火警发生时通知的区县
		soapObject.addProperty("jjbh", jjbh);//接警编号

		// soapObject.addProperty("ADMINDIVISION", ADMINDIVISION);//火灾所属区县ID

		SoapSerializationEnvelope envelope = getEnvelope(soapObject);
		if (envelope == null) {
			return false;
		}
		SoapObject object = null;
		try {
			object = (SoapObject) envelope.bodyIn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (object != null) {
			try {
				result = Boolean.parseBoolean(object.getProperty("addHuoJingResult").toString());
			} catch (Exception e) {
			}
		} else {
			result = false;
		}
		return result;
	}

	public boolean updateBack(String id) {
		boolean result = false;
		String methodName = "updateBackState";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", id);

		SoapSerializationEnvelope envelope = getEnvelope(soapObject);
		if (envelope == null) {
			return false;
		}
		SoapObject object = null;
		try {
			object = (SoapObject) envelope.bodyIn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (object != null) {
			try {
				result = Boolean.parseBoolean(object.getProperty("updateBackStateResult").toString());
			} catch (Exception e) {
			}
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * 回警
	 *
	 * @param BACKTIME
	 * @param ISFIRE
	 * @param FIRETYPE
	 * @param FIRESIUATION
	 * @param CHECKER
	 * @param POLICESIUATION
	 * @param REMARK
	 * @return
	 */
	public boolean addHuiJing(String backId, String RECEIPTID, String USERID, String BACKTIME, String ISFIRE,
							  String FIRETYPE, String FIRESIUATION, String CHECKER, String POLICESIUATION, String REMARK,
							  String ADMINDIVISION, String DQNAME,String BELONGNAME, String json) {
		boolean result = false;
		String methodName = "addHuiJing";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("id", backId);
		soapObject.addProperty("RECEIPTID", RECEIPTID);
		soapObject.addProperty("USERID", USERID);
		soapObject.addProperty("BACKTIME", BACKTIME);
		soapObject.addProperty("ISFIRE", ISFIRE);
		soapObject.addProperty("FIRETYPE", FIRETYPE);
		soapObject.addProperty("FIRESIUATION", FIRESIUATION);
		soapObject.addProperty("CHECKER", CHECKER);
		soapObject.addProperty("POLICESIUATION", POLICESIUATION);
		soapObject.addProperty("REMARK", REMARK);
		soapObject.addProperty("ADMINDIVISION", ADMINDIVISION);
		soapObject.addProperty("DQNAME", DQNAME);
		soapObject.addProperty("BELONGNAME", BELONGNAME);
		soapObject.addProperty("json", json);

		SoapSerializationEnvelope envelope = getEnvelope(soapObject);
		if (envelope == null) {
			return false;
		}
		SoapObject object = null;
		try {
			object = (SoapObject) envelope.bodyIn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (object != null) {
			try {
				result = Boolean.parseBoolean(object.getProperty("addHuiJingResult").toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * 向webservice发送照片的信息
	 *
	 * @return true：成功 false：失败
	 */
	public boolean uploadPhotoByService(String json) {
		boolean result = false;

		String methodName = "upImages1";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("json", json);

		SoapSerializationEnvelope envelope = getEnvelope(soapObject);
		if (envelope == null) {
			return false;
		}
		SoapObject object = null;
		try {
			object = (SoapObject) envelope.bodyIn;
			System.out.println("object返回数据：" + object);
		} catch (Exception e) {
			result = false;
		}

		if (object != null) {
			String resultStr = object.getProperty("upImages1Result").toString();
			System.out.println("resultStr返回数据：" + resultStr);
			if (resultStr.equals("true")) {
				result = true;
			} else {
				result = false;
			}

		} else {
			result = false;
		}

		return result;
	}

	/**
	 * 向webservice发送照片的信息
	 *
	 * @return true：成功 false：失败
	 */
	public String  uploadPhotoByService(String json, String id, String lon, String lat, String remark, String time,
										String state) {
		boolean result = false;

		String methodName = "upImages";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("json", json);
		soapObject.addProperty("id", id);
		soapObject.addProperty("id", id);// 用户id
		soapObject.addProperty("lon", lon);
		soapObject.addProperty("lat", lat);
		soapObject.addProperty("remark", remark);
		soapObject.addProperty("time", time);
		soapObject.addProperty("state", state);
		SoapSerializationEnvelope envelope = getEnvelope(soapObject);
		if (envelope == null) {
			return "0";
		}
		SoapObject object = null;
		try {
			object = (SoapObject) envelope.bodyIn;
			System.out.println("object返回数据：" + object);
		} catch (Exception e) {
			return"0";
		}

		if (object != null) {
			String resultStr = object.getProperty("upImagesResult").toString();
			System.out.println("resultStr返回数据：" + resultStr);
			return resultStr;
			/*if (resultStr.equals("0")) {
				result = true;
			} else {
				result = false;
			}*/

		} else {
			return "0";
		}


	}
	//火警上报照片上传
	public String  uploadPhotoByService1(String json) {
		boolean result = false;

		String methodName = "upImages";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("json", json);
		/*soapObject.addProperty("id", id);
		soapObject.addProperty("id", id);// 用户id
		soapObject.addProperty("lon", lon);
		soapObject.addProperty("lat", lat);
		soapObject.addProperty("remark", remark);
		soapObject.addProperty("time", time);
		soapObject.addProperty("state", state);*/
		SoapSerializationEnvelope envelope = getEnvelope(soapObject);
		if (envelope == null) {
			return "0";
		}
		SoapObject object = null;
		try {
			object = (SoapObject) envelope.bodyIn;
			System.out.println("object返回数据：" + object);
		} catch (Exception e) {
			return"0";
		}

		if (object != null) {
			String resultStr = object.getProperty("upImagesResult").toString();
			System.out.println("resultStr返回数据：" + resultStr);
			return resultStr;
			/*if (resultStr.equals("0")) {
				result = true;
			} else {
				result = false;
			}*/

		} else {
			return "0";
		}


	}
	//火警上报照片上传
	public String  uploadPhotoByhuijing(String json) {
		boolean result = false;
		String methodName = "upImagesbyhuijing";
		soapAction = "http://tempuri.org/" + methodName;
		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("json", json);
			/*soapObject.addProperty("id", id);
			soapObject.addProperty("id", id);// 用户id
			soapObject.addProperty("lon", lon);
			soapObject.addProperty("lat", lat);
			soapObject.addProperty("remark", remark);
			soapObject.addProperty("time", time);
			soapObject.addProperty("state", state);*/
		SoapSerializationEnvelope envelope = getEnvelope(soapObject);
		if (envelope == null) {
			return "0";
		}
		SoapObject object = null;
		try {
			object = (SoapObject) envelope.bodyIn;
			System.out.println("object返回数据：" + object);
		} catch (Exception e) {
			return"0";
		}

		if (object != null) {
			String resultStr = object.getProperty("upImagesbyhuijingResult").toString();
			System.out.println("resultStr返回数据：" + resultStr);
			return resultStr;
				/*if (resultStr.equals("0")) {
					result = true;
				} else {
					result = false;
				}*/

		} else {
			return "0";
		}


	}


	// 获取轨迹信息
	@SuppressLint("SimpleDateFormat")
	public List<TrackModel> getTrack(String starttime, String endtime) throws Exception {
		final List<TrackModel> locations = new ArrayList<TrackModel>();

		try {
			//String databaseName = ResourcesManager.getInstance(context).getPath()[0] + "/maps/sqlite/guiji.sqlite";
			String databaseName = ResourcesManager.getInstance(context).getPath()[0] + "/maps/sqlite/GYSLFH.sqlite";
			Class.forName("jsqlite.JDBCDriver").newInstance();
			Database db = new jsqlite.Database();
			db.open(databaseName, jsqlite.Constants.SQLITE_OPEN_READWRITE);
			//String sql = "select * from BUSI_COMMON_MOBILE_ZB_REALTIME";
			final SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			/*final Date start = df2.parse(starttime);
			final Date end = df2.parse(endtime);*/
			String sql = "select * from point where time >='" + starttime+ "' and time <='" + endtime+ "' order by datetime(time) desc";
			//select * from point where time between datetime('Sat Mar 01 10:26:58 GMT+08:00 2014') and datetime('Sat Mar 01 10:26:58 GMT+08:00 2016')
			//final SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

			db.exec(sql, new Callback() {
				@Override
				public boolean newrow(String[] data) {
					TrackModel trackModel = new TrackModel();
					Date datatme = null;
					if (!data[4].toString().equals("")) {
						trackModel.setLongitude(Double.parseDouble(data[1]));
						trackModel.setLatitude(Double.parseDouble(data[2]));
						trackModel.setTime(data[4]);
						locations.add(trackModel);
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
		} catch (jsqlite.Exception e) {
			e.printStackTrace();
			return null;
		}

		return locations;
	}
	/**获取监控点通道*/
	public String  getMonitortd(String objectid) {
		String result=null;
		methodName = "getMonitortd";
		soapAction = "http://tempuri.org/" + methodName;

		SoapObject soapObject = new SoapObject(nameSpace, methodName);
		soapObject.addProperty("objectid", objectid);

		result=getResult(soapObject, "getMonitortdResult");
		return result;
	}

	// 过滤字符函数
	public static String stringFilter(String str) {
		String regEx = "[/\\:*?<>|\"\n\t]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("");
	}

	// 特殊字符验证
	public static boolean stringCheck(String str) {

		String regEx = "[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*";
		if (str.replaceAll(regEx, "").length() == 0) {
			// 如果不包含特殊字符
			return true;
		} else {
			// 如果包含特殊字符
			return false;
		}
	}

	// 加入此段代码连接webservice时 call不会报io异常
	public void initWebserviceTry() {
		String strVer = android.os.Build.VERSION.RELEASE;
		strVer = strVer.substring(0, 3).trim();
		float fv = Float.valueOf(strVer);
		if (fv > 2.3) {
			StrictMode.setThreadPolicy(
					new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork() // 这里可以替换为detectAll()
							// 就包括了磁盘读写和网络I/O
							.penaltyLog() // 打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
							.build());
		}
	}

	public SoapSerializationEnvelope getEnvelope(SoapObject soapObject) {
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);

		try {
			HttpTransportSE transport = new HttpTransportSE(urlWebService, timeout);
			transport.debug = true;
			transport.call(soapAction, envelope);
		} catch (Exception e) {
			envelope = null;
			e.printStackTrace();
		}
		return envelope;
	}

	/* 获取返回结果 */
	public String getResult(SoapObject soapObject, String param) {
		String result = "";
		SoapSerializationEnvelope envelope = getEnvelope(soapObject);
		if (envelope == null) {
			return netException;
		}
		SoapObject object = null;
		if (envelope.bodyIn instanceof SoapFault) {
			return netException;
		} else {
			object = (SoapObject) envelope.bodyIn;
			if (object != null) {
				result = object.getProperty(param).toString();
			} else {
				result = netException;
			}
		}
		return result;
	}
}
