package com.titan.gyslfh.monitor;

/**
 * Created by whs on 2017/6/8
 * 监控点信息
 */

public class MonitorModel {
    /**
     * INFRAREDDVR : 1
     * INFRAREDIP : 192.168.1.18
     * ANTITHEFTDVR : 1
     * ANTITHEFTIP : 192.168.1.34
     * MONITORDVR : 1
     * MONITORIP : 192.192.192
     */

    private String INFRAREDDVR;
    private String INFRAREDIP;
    private String ANTITHEFTDVR;
    private String ANTITHEFTIP;
    private String MONITORDVR;
    private String MONITORIP;

    public MonitorModel() {
    }

    public MonitorModel(String INFRAREDDVR, String INFRAREDIP, String ANTITHEFTDVR, String ANTITHEFTIP, String MONITORDVR, String MONITORIP) {
        this.INFRAREDDVR = INFRAREDDVR;
        this.INFRAREDIP = INFRAREDIP;
        this.ANTITHEFTDVR = ANTITHEFTDVR;
        this.ANTITHEFTIP = ANTITHEFTIP;
        this.MONITORDVR = MONITORDVR;
        this.MONITORIP = MONITORIP;
    }

    public String getINFRAREDDVR() {
        return INFRAREDDVR;
    }

    public void setINFRAREDDVR(String INFRAREDDVR) {
        this.INFRAREDDVR = INFRAREDDVR;
    }

    public String getINFRAREDIP() {
        return INFRAREDIP;
    }

    public void setINFRAREDIP(String INFRAREDIP) {
        this.INFRAREDIP = INFRAREDIP;
    }

    public String getANTITHEFTDVR() {
        return ANTITHEFTDVR;
    }

    public void setANTITHEFTDVR(String ANTITHEFTDVR) {
        this.ANTITHEFTDVR = ANTITHEFTDVR;
    }

    public String getANTITHEFTIP() {
        return ANTITHEFTIP;
    }

    public void setANTITHEFTIP(String ANTITHEFTIP) {
        this.ANTITHEFTIP = ANTITHEFTIP;
    }

    public String getMONITORDVR() {
        return MONITORDVR;
    }

    public void setMONITORDVR(String MONITORDVR) {
        this.MONITORDVR = MONITORDVR;
    }

    public String getMONITORIP() {
        return MONITORIP;
    }

    public void setMONITORIP(String MONITORIP) {
        this.MONITORIP = MONITORIP;
    }
}
