package com.titan.gyslfh.backalarm;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableField;

import com.titan.data.source.DataRepository;
import com.titan.data.source.DataSource;
import com.titan.gyslfh.AlarmViewModel;
import com.titan.gyslfh.TitanApplication;
import com.titan.gyslfh.alarminfo.AlarmInfoModel;
import com.titan.model.Image;
import com.titan.newslfh.R;
import com.titan.util.DateUtil;
import com.titan.util.NetUtil;

import java.util.Date;
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

public class BackAlarmViewModel extends AlarmViewModel {
    //接警信息
    public ObservableField<AlarmInfoModel.AlarmInfo> mAlarmInfo=new ObservableField<>();
    //核实人
    public ObservableField<String> backperson=new ObservableField<>();
    //回警时间
    public ObservableField<String> backtime=new ObservableField<>();
    //火情类型
    public ObservableField<Map<Integer,String>> firetype=new ObservableField<>();
    //火情状态
    public ObservableField<Map<Integer,String>> firestatus=new ObservableField<>();
    //核实情况
    public ObservableField<String> situation=new ObservableField<>();




    protected  BackAlarmInterface mViewinterface;

    public BackAlarmViewModel(Context context, BackAlarmInterface backAlarmInterface, DataRepository dataRepository) {
        super(context, backAlarmInterface,dataRepository);
        mContext=context;
        mViewinterface=backAlarmInterface;
        mDataRepository=dataRepository;

        //是否火情
        isfire.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                notifyChange();

            }
        });
        //回警时间
        backtime.set(DateUtil.dateFormat(new Date()));

        mAlarmInfo.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                AlarmInfoModel.AlarmInfo alarminfo=mAlarmInfo.get();
                if(mAlarmInfo==null){
                    snackbarText.set("加载数据失败");
                }else {
                    address.set(alarminfo.getADDRESS());
                    reportTime.set(alarminfo.getREPORTTIME());

                }
            }
        });


    }

    /**
     * 检查提交内容
     */
    private boolean checkContent() {

        return  true;
    }

    /**
     * 时间选择
     * 1:接警时间
     * 2:出警时间
     */
    public void showDateDialog(int timetype){
        mViewinterface.showDateDialog(timetype);

    }
    /**
     * 火情状态
     */
    public void showStatusDialog(){
        mViewinterface.showStatusDialog();

    }

    /**
     * 1:所属地区、2:通知区县选择
     */
    public void showCountrySelectDialog(int type){
        mViewinterface.showCountrySelectDialog(type);

    }

    /**
     * 火警来源
     */
    public void showOriginDialog(){
        mViewinterface.showOriginDialog();

    }

    /**
     * 是否火情
     */
    public void  showIsfireDialog(){
        mViewinterface.showIsfireDialog();

    }

    /**
     * 火情类型
     */
    public void showFireTypeDialog(){
        mViewinterface.showAlarmType();
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
                            mViewinterface.showImage();
                        }


                    }).openGallery();
        }



    }

    /**
     * 回警
     */
    public void onBackAlarm(){

        //检查网络
        if(NetUtil.checkNetState(mContext)){
            //检查表单必填项
            if(checkContent()){
                BackAlarmModel backAlarmModel=new BackAlarmModel();
                try {
                    backAlarmModel.setBACKID(mAlarmInfo.get().getBACKID());
                    backAlarmModel.setRECEPID(mAlarmInfo.get().getID());
                    //0:未查看
                    //backAlarmModel.setBACKSTATUS("0");
                    backAlarmModel.setBACKTIME(backtime.get());
                    backAlarmModel.setCHECKER(backperson.get());
                    backAlarmModel.setBELONGAREANAME(situation.get());
                    backAlarmModel.setREMARK(remark.get());
                    backAlarmModel.setDQID(TitanApplication.getmUserModel().getDqid());
                    backAlarmModel.setDQNAME(TitanApplication.getmUserModel().getDqName());
                    backAlarmModel.setUSERID(TitanApplication.getmUserModel().getUserID());
                    backAlarmModel.setFIRESIUATION(firestatus.get().get(0));
                    backAlarmModel.setFIRETYPE(firetype.get().get(0));
                    backAlarmModel.setISFIRE(isfire.get().get(0));
                    backAlarmModel.setPOLICESIUATION(policeCase.get());

                } catch (Exception e) {
                    snackbarText.set(e.toString());
                    return;

                }

                mViewinterface.showProgress(true);
                mDataRepository.onBackAlarm(backAlarmModel, new DataSource.saveCallback() {
                    @Override
                    public void onFailure(String info) {
                        mViewinterface.showProgress(false);

                    }

                    @Override
                    public void onSuccess(String data) {
                        mViewinterface.showProgress(false);
                        snackbarText.set("回警成功");

                    }
                });
            }
        }


    }

    public void start(AlarmInfoModel.AlarmInfo alarmInfo) {
        //loadData(alarmid);
        mAlarmInfo.set(alarmInfo);

    }




}
