package com.titan.gyslfh.upfireinfo;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.titan.BaseViewModel;
import com.titan.data.source.DataRepository;
import com.titan.data.source.DataSource;
import com.titan.gyslfh.TitanApplication;
import com.titan.gyslfh.main.MainViewModel;
import com.titan.model.Image;
import com.titan.model.TitanLocation;
import com.titan.newslfh.R;
import com.titan.util.BitmapUtil;
import com.titan.util.FileUtils;
import com.titan.util.NetUtil;
import com.titan.util.TitanTextUtils;
import com.titan.util.TitanUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;

/**
 * Created by whs on 2017/5/17
 *
 *{"address":"火警详细地址","lon":117,"lat":"78","policeCase":"css","policeTime":"2010-11-23 22:33:44","reportTime":"2010-11-23 22:33:44","reportCase":"huaha","dqid":"1490","dqname":"贵阳市","userID":"4","tel":"12345","remark":"sss","isWork":"0","noticeAreaIDs":"[1,2,3]","pic":"[]"}
 */

public class UpAlarmViewModel extends BaseViewModel {
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);

    //用户等级
    public ObservableField<Integer> userlevel=new ObservableField<>();

    //上报类型 1：火情上报 2：接警录入
    //public ObservableField<Integer> type=new ObservableField<>(1);
    public ObservableInt type =new ObservableInt(1);
    //接警来源 5：平板
    //public ObservableField<String> alarmsouce=new ObservableField<>("5");
    public ObservableField<Map<String,Object>> orgin=new ObservableField<>();
    //火情状态
    public ObservableField<String> status=new ObservableField<>();
    //火警地址
    public ObservableField<String> address=new ObservableField<>();
    //经度
    public ObservableField<Double> lon=new ObservableField<>(117.4242);
    //纬度
    public ObservableField<Double> lat=new ObservableField<>(28.424);
    //出警情况
    public ObservableField<String> policeCase=new ObservableField<>();
    //出警时间
    public ObservableField<String> policeTime=new ObservableField<>();
    //上报情况
    public ObservableField<String> reportCase=new ObservableField<>();
    //上报时间
    public ObservableField<String> reportTime=new ObservableField<>();

    //地区ID
    public ObservableField<String> dqid=new ObservableField<>();

    //用户ID
    public ObservableField<String> userid=new ObservableField<>();

    //是否查岗
    //public ObservableField<Boolean> iswork=new ObservableField<>(false);
    public ObservableBoolean iswork=new ObservableBoolean(false);

    //电话
    public ObservableField<String> tel=new ObservableField<>();
    //备注
    public ObservableField<String> remark=new ObservableField<>();
    //位置信息
    public ObservableField<TitanLocation> titanloc=new ObservableField<>();



    //通知区县
    public ObservableList<Integer> noticeAreaIDs=new ObservableArrayList<>();

    //public  ObservableField<Integer[]> noticeDqIds=new ObservableField<>();
    //图片
    public ObservableArrayList<Image> imgs= new ObservableArrayList<>();


    private IUpAlarm mUpAlrm;

    private final DataRepository mDataRepository;
    //已选择图片
    private List<MediaBean> mediaBeanList;






    public UpAlarmViewModel(Context context,IUpAlarm iUpAlarm,DataRepository dataRepository) {
        mContext=context;
        this.mUpAlrm=iUpAlarm;
        this.mDataRepository=dataRepository;



        //地区等级 市级3，区县 4
        if (TitanApplication.mUserModel.getDqLevel()==null){
            return;
        }
        userlevel.set(Integer.valueOf(TitanApplication.mUserModel.getDqLevel()));
        //设置地址
        if(titanloc.get()!=null){
            address.set(titanloc.get().getAddress());

        }

        Date d = new Date(System.currentTimeMillis());
        //上报时间
        reportTime.set(sf.format(d));
        //出警时间
        policeTime.set(sf.format(d));
        //接警来源
        Map<String,Object> map=new HashMap<>();
        map.put("id", 6);
        map.put("name","移动平板");
        orgin.set(map);
        noticeAreaIDs.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<Integer>>() {
            @Override
            public void onChanged(ObservableList<Integer> integers) {
                notifyChange();
            }

            @Override
            public void onItemRangeChanged(ObservableList<Integer> integers, int i, int i1) {

            }

            @Override
            public void onItemRangeInserted(ObservableList<Integer> integers, int i, int i1) {

            }

            @Override
            public void onItemRangeMoved(ObservableList<Integer> integers, int i, int i1, int i2) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList<Integer> integers, int i, int i1) {

            }
        });

        //noticeAreaIDs.isEmpty()

    }

    /**
     * 上报火情
     * @return
     */
    public void upLoadAlarmInfo(){
        //检查网络
        if(NetUtil.checkNetState(mContext)){
            //检查表单必填项
            if(checkContent()){
                mUpAlrm.showProgress(true);
                 String json=intiUplaodAlarmInfo();
                if(json==null||json.equals("")){
                    return;
                }
                //json="{\"address\":\"火警详细地址\",\"lon\":117,\"lat\":\"78\",\"policeCase\":\"css\",\"policeTime\":\"2010-11-23 22:33:44\",\"reportTime\":\"2010-11-23 22:33:44\",\"reportCase\":\"huaha\",\"dqid\":\"1490\",\"dqname\":\"贵阳市\",\"userID\":\"4\",\"tel\":\"12345\",\"remark\":\"sss\",\"isWork\":\"0\",\"noticeAreaIDs\":\"[1,2,3]\",\"pic\":\"[]\"}";
                mDataRepository.uplaodAlarmInfo(json, new DataSource.uploadCallback() {
                    @Override
                    public void onFailure(String info) {
                        mUpAlrm.showProgress(false);
                        snackbarText.set(info);

                    }

                    @Override
                    public void onSuccess(String data) {
                        mUpAlrm.showProgress(false);
                        snackbarText.set(data);
                        mUpAlrm.finish();
                    }
                });
            }
        }
    }

    /**
     * 初始化数据
     */
    private String intiUplaodAlarmInfo() {
        String json="";

        try {
            UploadAlarmInfoModel uploadAlarmInfo = new UploadAlarmInfoModel();
            //地址
            uploadAlarmInfo.setADRESS(address.get());
            //报警时间

            uploadAlarmInfo.setREPORTTIME(reportTime.get());
            //上报情况
            uploadAlarmInfo.setREPORTCASE(reportCase.get());
            //接警时间
            uploadAlarmInfo.setPOLICETIME(policeTime.get());
            //出警情况
            uploadAlarmInfo.setPOLICECASE(policeCase.get());
            //是否查岗
            if(iswork.get()){
                uploadAlarmInfo.setISWORK("1");
            }else {
                uploadAlarmInfo.setISWORK("0");
            }
            //接警来源
            uploadAlarmInfo.setORIGINID( orgin.get().get("id")+"");
            //通知区县id
            uploadAlarmInfo.setNOTICEAREAS(noticeAreaIDs);
            //纬度
            uploadAlarmInfo.setLAT(""+MainViewModel.currentPoint.get().getY());
            //经度
            uploadAlarmInfo.setLON(""+MainViewModel.currentPoint.get().getX());
            //地区ID

            uploadAlarmInfo.setDQID(TitanApplication.mUserModel.getDqid());
            //用户ID

            uploadAlarmInfo.setUSERID(TitanApplication.mUserModel.getUserID());
            //报警电话
            uploadAlarmInfo.setTEL(tel.get());
            //备注
            uploadAlarmInfo.setREMARK(remark.get());

            List<String> imgdata = new ArrayList<>();
            for (int i = 0; i < imgs.size(); i++) {
                String path=imgs.get(i).getPath();
                String filename= FileUtils.pathGetNoSuffixFileName(path);
                String compresspath=BitmapUtil.compressBitmap(path,filename);
                String base64="";
                if(compresspath!=null){
                     base64 = TitanUtil.path2Base64(compresspath);
                }else {
                    base64 = TitanUtil.path2Base64(imgs.get(i).getPath());
                }
                imgdata.add(base64);
                //imgs.get(i).setData(base64);
            }
            //照片
            uploadAlarmInfo.setATTACHMENTS(imgdata);
            json = new Gson().toJson(uploadAlarmInfo, UploadAlarmInfoModel.class);

        }catch (Exception e){
            snackbarText.set("数据初始化异常"+e);
            return null;
        }
        return json;


    }

    /**
     * 检查提交内容
     */
    private boolean checkContent() {

        if(noticeAreaIDs.size()<=0){
            snackbarText.set(mContext.getString(R.string.error_noticecountry));
            return false;
        }
        //检查电话电话

        //boolean ism=TitanTextUtils.isMobileNumber(tel.get());
        //boolean isph=TitanTextUtils.isPhoneNumber(tel.get());
        if (!TitanTextUtils.isMobileNumber(tel.get()) && !TitanTextUtils.isPhoneNumber(tel.get())){
            snackbarText.set(mContext.getString(R.string.error_phonenumber));
            return false;
        }

        if(TextUtils.isEmpty(address.get())){
            snackbarText.set(mContext.getString(R.string.error_address));
            return false;
        }
        switch (type.get()){
            //火情上报
            case 1:
                break;
            //接警录入
            case 2:
                break;
        }

        return  true;
    }

    /**
     * 时间选择
     * 1:接警时间
     * 2:出警时间
     */
    public void showDateDialog(int timetype){
        mUpAlrm.showDateDialog(timetype);

    }
    /**
     * 火情状态
     */
    public void showStatusDialog(){
        mUpAlrm.showStatusDialog();

    }

    /**
     * 1:所属地区、2:通知区县选择
     */
    public void showCountrySelectDialog(int type){
        mUpAlrm.showCountrySelectDialog(type);

    }

    /**
     * 选择地址
     */
    public void showSelectAddress(){

        mUpAlrm.showSelectAddress();

    }


    /**
     * 火警来源
     */
    public void showOriginDialog(){
        mUpAlrm.showOriginDialog();

    }

    /**
     * 打开相册或拍照
     */
    public  void onTakePhoto(){
        if(imgs.size()>=3){
            snackbarText.set(mContext.getString(R.string.imgs));
        }
        else {

            RxGalleryFinal
                    .with(mContext)
                    .image()
                    //.radio()//单选
                    .maxSize(3)
                    .selected(mediaBeanList)
                    //.crop() //裁剪
                    .imageLoader(ImageLoaderType.FRESCO)
                    .subscribe(new RxBusResultSubscriber<ImageMultipleResultEvent>() {
                        @Override
                        protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
                            mediaBeanList=imageMultipleResultEvent.getResult();
                            imgs.clear();
                            for (MediaBean media:mediaBeanList){

                                Image image=new Image();
                                image.setPath(media.getOriginalPath());
                                imgs.add(image);
                            }
                            mUpAlrm.showImage();
                        }


                    }).openGallery();
        }



    }
}
