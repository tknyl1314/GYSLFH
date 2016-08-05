package com.otitan.gis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Whs on 2016/8/5 0005.
 * Geoconding服务类
 */
public class Geocoding {
    /**
     * 使用阿里地图（高德地图）坐标转地址接口
     *
     * @param lat
     * @param lng
     * @return 地址
     * @throws IOException
     */
    public static String antgeoCeodingbyalli(String lat, String lng) {
        String jsonString = "";
        // type 001 (100代表道路，010代表POI，001代表门址，111可以同时显示前三项)
        String path = "http://gc.ditu.aliyun.com/regeocoding?l=" + lat + ","
                + lng + "&type=010";
        System.out.println(path);
        // 参数直接加载url后面
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("contentType", "GBK");
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            if (conn.getResponseCode() == 200) { // 200表示请求成功
                InputStream is = conn.getInputStream(); // 以输入流的形式返回
                // 将输入流转换成字符串
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }

                if (baos.size() < 1) {
                    return "坐标请求异常";
                }

                JSONArray jsonArray = null;
                List<HashMap<String, String>> locjsons = new ArrayList<HashMap<String, String>>();
                try {
                    jsonArray = new JSONObject(baos.toString())
                            .getJSONArray("addrList");
                    // JSONArray jsonObject1=new
                    // JSONObject(baos.toString()).getJSONArray("queryLocation");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
                        HashMap<String, String> locjson = new HashMap<String, String>();
                        locjson.put("addr", jsonObject.getString("addr"));
                        locjson.put("admCode", jsonObject.getString("admCode"));
                        locjson.put("admName", jsonObject.getString("admName"));
                        locjson.put("distance",
                                jsonObject.getString("distance"));
                        locjson.put("name", jsonObject.getString("name"));
                        locjson.put("nearestPoint",
                                jsonObject.getString("nearestPoint"));
                        locjson.put("status", jsonObject.getString("status"));
                        locjson.put("type", jsonObject.getString("type"));
                        locjsons.add(locjson);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // String
                // jString=locjsons.get(0).get("admName")+locjsons.get(0).get("addr")+","+locjsons.get(0).get("name")+"附近";
                // jsonString=jString.replaceAll(",", "");
                baos.close();
                is.close();
                // 转换成json数据处理
                // {"queryLocation":[39.938133,116.395739],"addrList":[{"type":"doorPlate","status":1,"name":"地安门外大街万年胡同1号","admCode":"110102","admName":"北京市,北京市,西城区,","addr":"","nearestPoint":[116.39546,39.93850],"distance":45.804}]}
            } else {
                return "网络请求失败";
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return  "反向地理编码失败";
        } catch (IOException e) {
            e.printStackTrace();
            return  "反向地理编码失败";
        }
        return jsonString;
    }}
