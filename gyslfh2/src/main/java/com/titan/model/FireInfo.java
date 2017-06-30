package com.titan.model;

/**
 * Created by whs on 2017/6/30
 * 推送火情信息
 */

public class FireInfo {
    //地址
    private String ADDRESS;
    //经度
    private Double LON;
    //纬度
    private Double LAT;
    //备注
    private String REMARK;
    //时间
    private String TIME;

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String tIME) {
        TIME = tIME;
    }

    public Double getLAT() {
        return LAT;
    }

    public void setLAT(Double lAT) {
        LAT = lAT;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String rEMARK) {
        REMARK = rEMARK;
    }


    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String aDDRESS) {
        ADDRESS = aDDRESS;
    }

    public Double getLON() {
        return LON;
    }

    public void setLON(Double lON) {
        LON = lON;
    }
}
