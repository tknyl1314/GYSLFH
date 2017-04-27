package com.otitan.gyslfh.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.otitan.entity.DateDialog;
import com.otitan.gyslfh.R;
import com.otitan.util.DateTool;
import com.otitan.util.PadUtil;
import com.otitan.util.WebServiceUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;

/**
 * 接警录入
 */
public class JJLRActivity extends Activity {


    private EditText jiejing_huojingaddress, chujingqingkuang, jiejing_tel;
    private Spinner huojing_resource;
    private ImageButton imgBtn_return, imgBtn_upSure;
    private TextView jiejing_time, shichujingTime, jiejing_bianhao;
    private CheckBox yyq, nmq, byq, xfx, wdq, qzs, xwx, kyx, hxq, gshq, gaxq;
    private ArrayAdapter<String> resourceAdapter;
    private String username, userID, DQLEVEL, UNITID, jjbh;
    private WebServiceUtil websUtil;
    private String resourceValue = "";
    //通知区县
    private String quxianValue = "";
    Context mContext;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(mContext, "接警录入成功", Toast.LENGTH_SHORT).show();
                    JJLRActivity.this.finish();
                    break;
                case 0:
                    Toast.makeText(mContext, "接警录入失败", Toast.LENGTH_LONG).show();
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext=this;
        PadUtil padUtil = new PadUtil();
        if (padUtil.isPad(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_jie_jing);
        ButterKnife.bind(this);
        websUtil = new WebServiceUtil(getApplicationContext());
        websUtil.initWebserviceTry();

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        userID = intent.getStringExtra("userID");
        DQLEVEL = intent.getStringExtra("DQLEVEL");
        UNITID = intent.getStringExtra("UNITID");
        DateTool dateTool = new DateTool();
        jiejing_time = (TextView) findViewById(R.id.jiejing_time_text);
        jiejing_time.setOnClickListener(new MyListener());
        jiejing_time.setText(dateTool.getDateStr());
        jiejing_huojingaddress = (EditText) findViewById(R.id.jiejing_huojing_address);
        chujingqingkuang = (EditText) findViewById(R.id.chujingqingkuang);
        shichujingTime = (TextView) findViewById(R.id.shichujingTime);
        shichujingTime.setOnClickListener(new MyListener());
        jiejing_tel = (EditText) findViewById(R.id.jiejing_tel);
        jiejing_bianhao = (TextView) findViewById(R.id.jiejing_bianhao);

        //获取接警编号
        jjbh = websUtil.getJieJingBainHao(username);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmm");
        jiejing_bianhao.setText(jjbh);

        huojing_resource = (Spinner) findViewById(R.id.huojing_resource);
        final String[] resourceDate = getResources().getStringArray(R.array.huojingresource);
        resourceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, resourceDate);
        resourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        huojing_resource.setAdapter(resourceAdapter);
        huojing_resource.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                TextView tv = (TextView) view;
                tv.setTextColor(getResources().getColor(R.color.balck));
                if (position == 0) {
                    resourceValue = "";
                } else {
                    resourceValue = resourceDate[position].toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        imgBtn_upSure = (ImageButton) findViewById(R.id.jiejing_imgBtn_upSure);
        imgBtn_return = (ImageButton) findViewById(R.id.jiejing_returnBtn);
        imgBtn_return.setOnClickListener(new MyListener());
        imgBtn_upSure.setOnClickListener(new MyListener());

        yyq = (CheckBox) findViewById(R.id.yyq);
        nmq = (CheckBox) findViewById(R.id.nmq);
        byq = (CheckBox) findViewById(R.id.byq);
        xfx = (CheckBox) findViewById(R.id.xfx);
        wdq = (CheckBox) findViewById(R.id.wdq);
        qzs = (CheckBox) findViewById(R.id.qzs);
        xwx = (CheckBox) findViewById(R.id.xwx);
        kyx = (CheckBox) findViewById(R.id.kyx);
        hxq = (CheckBox) findViewById(R.id.hxq);
        gshq = (CheckBox) findViewById(R.id.gshq);
        //贵安新区
        gaxq = (CheckBox) findViewById(R.id.cb_gaxq);

        //jkq = (CheckBox) findViewById(R.id.jkq);
    }

    /*@OnClick({R.id.yyq, R.id.nmq, R.id.byq, R.id.xfx, R.id.wdq, R.id.cb_gaxq, R.id.qzs, R.id.xwx, R.id.kyx, R.id.hxq, R.id.gshq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //云岩区
            case R.id.yyq:
                quxianValue = yyq.getText().toString() + ",";
                break;
            //南明区
            case R.id.nmq:
                quxianValue = quxianValue + nmq.getText().toString() + ",";
                break;
            //白云区
            case R.id.byq:
                quxianValue = quxianValue + byq.getText().toString() + ",";
                break;
            //息烽县
            case R.id.xfx:
                quxianValue = quxianValue + xfx.getText().toString() + ",";
                break;
            //乌当区
            case R.id.wdq:
                quxianValue = quxianValue + wdq.getText().toString() + ",";
                break;
            //贵安新区
            case R.id.cb_gaxq:
                quxianValue = quxianValue + cbGaxq.getText().toString() + ",";

                break;
            //清镇市
            case R.id.qzs:
                quxianValue = quxianValue + qzs.getText().toString() + ",";

                break;
            //修文县
            case R.id.xwx:
                quxianValue = quxianValue + xwx.getText().toString() + ",";

                break;
            //开阳县
            case R.id.kyx:
                quxianValue = quxianValue + kyx.getText().toString() + ",";
                break;
            //花溪区
            case R.id.hxq:
                quxianValue = quxianValue + hxq.getText().toString() + ",";
                break;
            //观山湖区
            case R.id.gshq:
                quxianValue = quxianValue + gshq.getText().toString() + ",";
                break;
        }
    }
*/
    public class MyListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.jiejing_time_text:
                    Dialog jiejingDateDialog = new DateDialog(JJLRActivity.this, jiejing_time);
                    jiejingDateDialog.show();
                    break;
                case R.id.shichujingTime:
                    Dialog shichujingDateDialog = new DateDialog(JJLRActivity.this, shichujingTime);
                    shichujingDateDialog.show();
                    break;
                case R.id.jiejing_returnBtn:
                    JJLRActivity.this.finish();
                    break;
                //接警录入
                case R.id.jiejing_imgBtn_upSure:
                    quxianValue="";
                    if(yyq.isChecked()){
                        quxianValue = yyq.getText().toString()+",";
                    }
                    if(nmq.isChecked()){
                        quxianValue = quxianValue +nmq.getText().toString()+",";
                    }
                    if(byq.isChecked()){
                        quxianValue = quxianValue +byq.getText().toString()+",";
                    }
                    if(xfx.isChecked()){
                        quxianValue = quxianValue +xfx.getText().toString()+",";
                    }
                    if(wdq.isChecked()){
                        quxianValue = quxianValue +wdq.getText().toString()+",";
                    }
                    if(qzs.isChecked()){
                        quxianValue = quxianValue +qzs.getText().toString()+",";
                    }
                    if(xwx.isChecked()){
                        quxianValue = quxianValue +xwx.getText().toString()+",";
                    }
                    if(kyx.isChecked()){
                        quxianValue = quxianValue +kyx.getText().toString()+",";
                    }
                    if(hxq.isChecked()){
                        quxianValue = quxianValue +hxq.getText().toString()+",";
                    }
                    if(gshq.isChecked()){
                        quxianValue = quxianValue +gshq.getText().toString()+",";
                    }
                    if(gaxq.isChecked()){
                        quxianValue = quxianValue +gaxq.getText().toString()+",";
                    }
                    upLoadInfo();
                    /*if(checkContent()) {
                        quxianValue = quxianValue.substring(0, quxianValue.length() - 1);
                        boolean result = websUtil.addJieJing(username, DQLEVEL, ORIGIN,
                                jiejingTimeStr, ADRESS, TEL_ONE, POLICECASE,
                                shichujingTimeStr, userID, quxianValue, jjbh);
                        if (result) {
                            ToastUtil.setToast(JJLRActivity.this, "接警录入成功");
                            JJLRActivity.this.finish();
                        } else {
                            ToastUtil.setToast(JJLRActivity.this, "接警录入失败");
                        }
                    }*/
                   /* String ORIGIN = resourceValue;
                    String ADRESS = jiejing_huojingaddress.getText().toString();
                    String TEL_ONE = jiejing_tel.getText().toString();
                    String POLICECASE = chujingqingkuang.getText().toString();
                    String jiejingTimeStr = jiejing_time.getText().toString();
                    String shichujingTimeStr = shichujingTime.getText().toString();*/

                /*if(quxianValue.subSequence(quxianValue.length()-1, quxianValue.length()).equals(",")){
					quxianValue=quxianValue.substring(0, quxianValue.length()-1);
				}*/
                    /*if (!quxianValue.equals("")) {
                        quxianValue = quxianValue.substring(0, quxianValue.length() - 1);
                    }*/

                    //经开区
				/*if(jkq.isChecked()){
					quxianValue = quxianValue +","+jkq.getText().toString();
				}*/
                    /*if (resourceValue.equals("")) {
                        ToastUtil.setToast(JJLRActivity.this, "报警来源不能为空");
                    } else if (ADRESS.equals("")) {
                        ToastUtil.setToast(JJLRActivity.this, "报警地址不能为空");
                    } else if (TEL_ONE.equals("")) {
                        ToastUtil.setToast(JJLRActivity.this, "报警电话不能为空");
                    } else if (quxianValue.equals("")) {
                        ToastUtil.setToast(JJLRActivity.this, "ͨ通知区县不能为空");
                    } else {

                    }*/
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * 检查表单内容
     */
    private void upLoadInfo() {
        //接警来源
        final String ORIGIN = resourceValue;
        //地址
        final String ADRESS = jiejing_huojingaddress.getText().toString();
        //电话
        final String TEL_ONE = jiejing_tel.getText().toString();
        //市局出警情况
        final String POLICECASE = chujingqingkuang.getText().toString();
        //接警时间
        final String jiejingTimeStr = jiejing_time.getText().toString();
        //市局出警时间
        final String shichujingTimeStr = shichujingTime.getText().toString();
        View focusview = null;
        boolean cancel = false;

        if(TextUtils.isEmpty(ORIGIN)){
            focusview=huojing_resource;
            cancel=true;
            Toast.makeText(mContext, "请选择接警来源", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(ADRESS)){
            jiejing_huojingaddress.setError("请输入火警地址");
            focusview=jiejing_huojingaddress;
            cancel=true;
        }
        else if(TextUtils.isEmpty(TEL_ONE)){
            jiejing_tel.setError("请输入报警电话");
            focusview=jiejing_tel;
            cancel=true;
        }else if(TextUtils.isEmpty(quxianValue)){
            cancel=true;
            Toast.makeText(mContext, "请选择要通知的区县", Toast.LENGTH_SHORT).show();
        }
        if (cancel) {
            //assert focusview != null;
            if(focusview!=null){
                focusview.requestFocus();
            }
        }else {
            quxianValue = quxianValue.substring(0, quxianValue.length() - 1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean result = websUtil.addJieJing(username, DQLEVEL, ORIGIN,
                            jiejingTimeStr, ADRESS, TEL_ONE, POLICECASE,
                            shichujingTimeStr, userID, quxianValue, jjbh);
                    Message msg =new Message();

                    if (result) {
                        msg.what=1;
                    } else {
                        msg.what=0;
                    }
                    handler.sendMessage(msg);
                }
            }).start();


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.jie_jing, menu);
        return true;
    }

}
