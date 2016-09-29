package com.titan.jedis.activity;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.esri.core.geometry.Point;
import com.titan.jedis.R;

import redis.clients.jedis.Jedis;

public class MainActivity extends Activity {
    EditText ed_redisip;
    public static Point upPoint;
    Context mcontext;
    String result;
    Button btn_request;
    LocationManager locationManager;
    Handler myhanHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(mcontext, "报警成功", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(mcontext, "报警失败", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(mcontext, "监控视频播放失败", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext=this;
        setContentView(R.layout.activity_main);
        ed_redisip=(EditText) findViewById(R.id.et_redisip);
        btn_request=(Button) findViewById(R.id.btn_request);
        btn_request.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final String redisip=ed_redisip.getText().toString();
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        request(redisip);
                    }
                }).start();

            }
        });

    }
    public void request(String redisip){
        Message msg=new Message();
        try{
            upPoint=getGPSpoint();
            Jedis jedis = new Jedis(redisip, 6379,15000);
            //Point point= (Point) GeometryEngine.project(MapActivity.upPoint,SpatialReference.create(2343), SpatialReference.create(4326));
            //jedis.set("foo", "bar");
						/*	String lon=MapActivity.upPoint.getX()+"";
							String lat=MapActivity.upPoint.getY()+"";*/
            if(upPoint!=null){
                String lon=upPoint.getX()+"";
                String lat=upPoint.getY()+"";
                //jedis.set("call", "{'alarm':1,'lon':"+lon+",'lat':"+lat+",}");
                String alarminfo="Android,otitan,"+lat.toString()+","+lon.toString();
                result=jedis.set("msg_sdbj", alarminfo);
                jedis.close();
            }else{
                msg.what=0;
                //Toast.makeText(mcontext,"获取GPS位置失败",Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            msg.what=0;
            myhanHandler.sendMessage(msg);
            return;
        }
        if(result.equals("OK")){
            msg.what=1;
            //Toast.makeText(mcontext,"报警成功",Toast.LENGTH_SHORT).show();
        }else{
            msg.what=0;
            // Toast.makeText(mcontext,"报警失败",Toast.LENGTH_SHORT).show();
        }

    }

    /** 获取位置坐标（优先使用gps定位） GPS定位 网络定位 百度定位 */
    public Point getGPSpoint( ) {
        locationManager = (LocationManager) mcontext.getSystemService(Context.LOCATION_SERVICE);
		/*Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}*/
        Location location=null;
        try {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }catch (SecurityException e){
            e.printStackTrace();
        }

        if (location == null) {
            try{
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            catch (SecurityException e){
                e.printStackTrace();
            }

        }

        if (location != null) {
            double latitude = location.getLatitude(); // 纬度 26.567741305680546
            double longitude = location.getLongitude(); // 经度 106.68937683886078
            double altitude = location.getAltitude();// 1040.8563754250627
            return new Point(longitude,latitude,altitude);
        }
        return null;
    }
}
