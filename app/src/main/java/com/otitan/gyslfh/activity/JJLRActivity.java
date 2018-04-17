package com.otitan.gyslfh.activity;

import android.annotation.SuppressLint;
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
import com.otitan.util.ToastUtil;
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
    private CheckBox yyq, nmq, byq, xfx, wdq, qzs, xwx, kyx, hxq, gshq, gaxq,shunhai,changpoling;
    private ArrayAdapter<String> resourceAdapter;
    private String username, userID, DQLEVEL, UNITID, jjbh;
    private WebServiceUtil websUtil;
    private String resourceValue = "";
    //通知区县
    private String quxianValue = "";
    Context mContext;
    @SuppressLint("HandlerLeak")
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
        if (PadUtil.isPad(this)) {
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
        if(jjbh==null||jjbh.equals("网络异常")){
            ToastUtil.setToast(mContext,"获取接警编号异常");
        }else {
            jiejing_bianhao.setText(jjbh);

        }

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
        //顺海林场
        shunhai= (CheckBox) findViewById(R.id.cb_shunhai);
        //长坡岭林场
        changpoling= (CheckBox) findViewById(R.id.cb_changpoling);

        //jkq = (CheckBox) findViewById(R.id.jkq);
    }

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
                    if(shunhai.isChecked()){
                        quxianValue = quxianValue +shunhai.getText().toString()+",";
                    }
                    if(changpoling.isChecked()){
                        quxianValue = quxianValue +changpoling.getText().toString()+",";
                    }
                    upLoadInfo();

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
                    Message msg =new Message();//

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
