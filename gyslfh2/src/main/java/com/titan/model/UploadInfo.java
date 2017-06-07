package com.titan.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by whs on 2016/10/14
 */

public class UploadInfo {
    /**设备号*/
    private String USER_ID;
    /**类型*/
    private String WTSB_TYPE;
    /**标题*/
    private String TITLE;
    /**描述信息*/
    private String DESCRIBE;
    /**备注*/
    private String REMARK;
    /**附件*/
    private List<Image> FJINFO;
    /**设备号*/
    private String SBH;
    /**经度*/
    private String LON;
    /**纬度*/
    private String LAT;


    public UploadInfo() {
    }

    public UploadInfo(String USER_ID, String TYPE, String TITLE, String DESCRIBE, String REMARK, List<Image> FJINFO) {
        this.USER_ID = USER_ID;
        this.WTSB_TYPE = TYPE;
        this.TITLE = TITLE;
        this.DESCRIBE = DESCRIBE;
        this.REMARK = REMARK;
        this.FJINFO = FJINFO;
    }

    public UploadInfo(String USER_ID, String TYPE, String TITLE, String DESCRIBE, String REMARK, List<Image> FJINFO, String SBH, String LON, String LAT) {
        this.USER_ID = USER_ID;
        this.WTSB_TYPE = TYPE;
        this.TITLE = TITLE;
        this.DESCRIBE = DESCRIBE;
        this.REMARK = REMARK;
        this.FJINFO = FJINFO;
        this.SBH = SBH;
        this.LON = LON;
        this.LAT = LAT;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getTYPE() {
        return WTSB_TYPE;
    }

    public void setTYPE(String TYPE) {
        this.WTSB_TYPE = TYPE;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getDESCRIBE() {
        return DESCRIBE;
    }

    public void setDESCRIBE(String DESCRIBE) {
        this.DESCRIBE = DESCRIBE;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public List<Image> getFJINFO() {
        return FJINFO;
    }

    public void setFJINFO(ArrayList<Image> FJINFO) {
        this.FJINFO = FJINFO;
    }

    public void setFJINFO(List<Image> FJINFO) {
        this.FJINFO = FJINFO;
    }

    public String getSBH() {
        return SBH;
    }

    public void setSBH(String SBH) {
        this.SBH = SBH;
    }

    public String getLON() {
        return LON;
    }

    public void setLON(String LON) {
        this.LON = LON;
    }

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }
}
