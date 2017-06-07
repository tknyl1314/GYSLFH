package com.titan.gyslfh.upfireinfo;


import java.util.List;

/**
 * Created by whs on 2016/10/14
 *
 */

public class UploadAlarmInfoModel {


    /**
     * 必填字段
     * USERID : 1
     * ORIGINID : 1
     * DQID : 2
     * ADRESS : 78
     * NOTICEAREAS : [1,2,3]
     * 非必填字段
     *
     * ISWORK : 1
     * RECEIPTTIME : 2010-11-23 22:33:44
     * LAT : 112
     * LON : 22
     * POLICECASE : 1490
     * POLICETIME : 2010-11-23 22:33:44
     * REPORTTIME : 2010-11-23 22:33:44
     *
     * REPORTCASE : 12345
     * 电话
     * TEL : 1123
     * 地区名称
     * DQNAME : 0
     * 备注
     * REMARK :
     * 附件
     * ATTACHMENTS : []
     */

    private String USERID;
    private String ORIGINID;
    private String DQID;
    private String ADRESS;
    private String ISWORK;
    private String RECEIPTTIME;
    private String LAT;
    private String LON;
    private String POLICECASE;
    private String POLICETIME;
    private String REPORTTIME;
    private String REPORTCASE;
    private String TEL;
    private String DQNAME;
    private String REMARK;
    private List<Integer> NOTICEAREAS;
    private List<?> ATTACHMENTS;

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getORIGINID() {
        return ORIGINID;
    }

    public void setORIGINID(String ORIGINID) {
        this.ORIGINID = ORIGINID;
    }

    public String getDQID() {
        return DQID;
    }

    public void setDQID(String DQID) {
        this.DQID = DQID;
    }

    public String getADRESS() {
        return ADRESS;
    }

    public void setADRESS(String ADRESS) {
        this.ADRESS = ADRESS;
    }

    public String getISWORK() {
        return ISWORK;
    }

    public void setISWORK(String ISWORK) {
        this.ISWORK = ISWORK;
    }

    public String getRECEIPTTIME() {
        return RECEIPTTIME;
    }

    public void setRECEIPTTIME(String RECEIPTTIME) {
        this.RECEIPTTIME = RECEIPTTIME;
    }

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }

    public String getLON() {
        return LON;
    }

    public void setLON(String LON) {
        this.LON = LON;
    }

    public String getPOLICECASE() {
        return POLICECASE;
    }

    public void setPOLICECASE(String POLICECASE) {
        this.POLICECASE = POLICECASE;
    }

    public String getPOLICETIME() {
        return POLICETIME;
    }

    public void setPOLICETIME(String POLICETIME) {
        this.POLICETIME = POLICETIME;
    }

    public String getREPORTTIME() {
        return REPORTTIME;
    }

    public void setREPORTTIME(String REPORTTIME) {
        this.REPORTTIME = REPORTTIME;
    }

    public String getREPORTCASE() {
        return REPORTCASE;
    }

    public void setREPORTCASE(String REPORTCASE) {
        this.REPORTCASE = REPORTCASE;
    }

    public String getTEL() {
        return TEL;
    }

    public void setTEL(String TEL) {
        this.TEL = TEL;
    }

    public String getDQNAME() {
        return DQNAME;
    }

    public void setDQNAME(String DQNAME) {
        this.DQNAME = DQNAME;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public List<Integer> getNOTICEAREAS() {
        return NOTICEAREAS;
    }

    public void setNOTICEAREAS(List<Integer> NOTICEAREAS) {
        this.NOTICEAREAS = NOTICEAREAS;
    }

    public List<?> getATTACHMENTS() {
        return ATTACHMENTS;
    }

    public void setATTACHMENTS(List<?> ATTACHMENTS) {
        this.ATTACHMENTS = ATTACHMENTS;
    }
}
