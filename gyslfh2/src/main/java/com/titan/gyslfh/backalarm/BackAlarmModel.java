package com.titan.gyslfh.backalarm;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by whs on 2017/5/27
 */
@Entity
public class BackAlarmModel {
    //回警时间
    //@Property(nameInDb = "BACKTIME")
    private String BACKID;
    private String RECEPID;
    private String USERID;
    private String DQID;
    private String DQNAME;
    private String CHECKER;
    private String BACKTIME;
    private String BACKSTATUS;
    private String ISFIRE;
    //火情类型
    private String FIRETYPE;
    //火情状态
    private String FIRESIUATION;
    //出警情况
    private String POLICESIUATION;
    //所属地区id
    private  String BELONGAREAID;
    //所属地区Name
    private  String BELONGAREANAME;
    //林场关系
    private String INRELATION;
    //备注
    private String REMARK;
    private String REMARK1;
    private String REMARK2;


    @Generated(hash = 1642723972)
    public BackAlarmModel(String BACKID, String RECEPID, String USERID, String DQID,
            String DQNAME, String CHECKER, String BACKTIME, String BACKSTATUS,
            String ISFIRE, String FIRETYPE, String FIRESIUATION,
            String POLICESIUATION, String BELONGAREAID, String BELONGAREANAME,
            String INRELATION, String REMARK, String REMARK1, String REMARK2) {
        this.BACKID = BACKID;
        this.RECEPID = RECEPID;
        this.USERID = USERID;
        this.DQID = DQID;
        this.DQNAME = DQNAME;
        this.CHECKER = CHECKER;
        this.BACKTIME = BACKTIME;
        this.BACKSTATUS = BACKSTATUS;
        this.ISFIRE = ISFIRE;
        this.FIRETYPE = FIRETYPE;
        this.FIRESIUATION = FIRESIUATION;
        this.POLICESIUATION = POLICESIUATION;
        this.BELONGAREAID = BELONGAREAID;
        this.BELONGAREANAME = BELONGAREANAME;
        this.INRELATION = INRELATION;
        this.REMARK = REMARK;
        this.REMARK1 = REMARK1;
        this.REMARK2 = REMARK2;
    }

    @Generated(hash = 187341111)
    public BackAlarmModel() {
    }


    public String getBACKID() {
        return BACKID;
    }

    public void setBACKID(String BACKID) {
        this.BACKID = BACKID;
    }

    public String getRECEPID() {
        return RECEPID;
    }

    public void setRECEPID(String RECEPID) {
        this.RECEPID = RECEPID;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getDQID() {
        return DQID;
    }

    public void setDQID(String DQID) {
        this.DQID = DQID;
    }

    public String getDQNAME() {
        return DQNAME;
    }

    public void setDQNAME(String DQNAME) {
        this.DQNAME = DQNAME;
    }

    public String getCHECKER() {
        return CHECKER;
    }

    public void setCHECKER(String CHECKER) {
        this.CHECKER = CHECKER;
    }

    public String getBACKTIME() {
        return BACKTIME;
    }

    public void setBACKTIME(String BACKTIME) {
        this.BACKTIME = BACKTIME;
    }

    public String getBACKSTATUS() {
        return BACKSTATUS;
    }

    public void setBACKSTATUS(String BACKSTATUS) {
        this.BACKSTATUS = BACKSTATUS;
    }

    public String getISFIRE() {
        return ISFIRE;
    }

    public void setISFIRE(String ISFIRE) {
        this.ISFIRE = ISFIRE;
    }

    public String getFIRETYPE() {
        return FIRETYPE;
    }

    public void setFIRETYPE(String FIRETYPE) {
        this.FIRETYPE = FIRETYPE;
    }

    public String getFIRESIUATION() {
        return FIRESIUATION;
    }

    public void setFIRESIUATION(String FIRESIUATION) {
        this.FIRESIUATION = FIRESIUATION;
    }

    public String getPOLICESIUATION() {
        return POLICESIUATION;
    }

    public void setPOLICESIUATION(String POLICESIUATION) {
        this.POLICESIUATION = POLICESIUATION;
    }

    public String getBELONGAREAID() {
        return BELONGAREAID;
    }

    public void setBELONGAREAID(String BELONGAREAID) {
        this.BELONGAREAID = BELONGAREAID;
    }

    public String getBELONGAREANAME() {
        return BELONGAREANAME;
    }

    public void setBELONGAREANAME(String BELONGAREANAME) {
        this.BELONGAREANAME = BELONGAREANAME;
    }

    public String getINRELATION() {
        return INRELATION;
    }

    public void setINRELATION(String INRELATION) {
        this.INRELATION = INRELATION;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getREMARK1() {
        return REMARK1;
    }

    public void setREMARK1(String REMARK1) {
        this.REMARK1 = REMARK1;
    }

    public String getREMARK2() {
        return REMARK2;
    }

    public void setREMARK2(String REMARK2) {
        this.REMARK2 = REMARK2;
    }
}
