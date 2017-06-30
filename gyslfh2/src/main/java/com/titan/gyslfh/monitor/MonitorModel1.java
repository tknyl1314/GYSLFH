package com.titan.gyslfh.monitor;

import java.util.List;

/**
 * Created by whs on 2017/6/8
 */

public class MonitorModel1 {
    private List<DvrInfoModel> MonitorConfig;
    private List<DvrInfoModel> InfraredConfig;
    private List<DvrInfoModel> AntitheftConfig;

    public List<DvrInfoModel> getMonitorConfig() {
        return MonitorConfig;
    }

    public void setMonitorConfig(List<DvrInfoModel> MonitorConfig) {
        this.MonitorConfig = MonitorConfig;
    }

    public List<DvrInfoModel> getInfraredConfig() {
        return InfraredConfig;
    }

    public void setInfraredConfig(List<DvrInfoModel> InfraredConfig) {
        this.InfraredConfig = InfraredConfig;
    }

    public List<DvrInfoModel> getAntitheftConfig() {
        return AntitheftConfig;
    }

    public void setAntitheftConfig(List<DvrInfoModel> AntitheftConfig) {
        this.AntitheftConfig = AntitheftConfig;
    }

    public static class InfraredConfigBean {
        /**
         * ID : 2
         * MONITORIP : 192.168.1.18
         * TD : 2
         * YPLXJID : 1
         * MONITORSTATUS : 1
         * REMARK : 2017-05-22 14:16:22
         * INTRANETIP :
         * MOBILEPORT : 8000
         * EXTRANET : 222.85.147.92:81
         * USERNAME : admin
         * PASSWORD : sfb12345
         */

        private String ID;
        private String MONITORIP;
        private String TD;
        private String YPLXJID;
        private String MONITORSTATUS;
        private String REMARK;
        private String INTRANETIP;
        private String MOBILEPORT;
        private String EXTRANET;
        private String USERNAME;
        private String PASSWORD;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getMONITORIP() {
            return MONITORIP;
        }

        public void setMONITORIP(String MONITORIP) {
            this.MONITORIP = MONITORIP;
        }

        public String getTD() {
            return TD;
        }

        public void setTD(String TD) {
            this.TD = TD;
        }

        public String getYPLXJID() {
            return YPLXJID;
        }

        public void setYPLXJID(String YPLXJID) {
            this.YPLXJID = YPLXJID;
        }

        public String getMONITORSTATUS() {
            return MONITORSTATUS;
        }

        public void setMONITORSTATUS(String MONITORSTATUS) {
            this.MONITORSTATUS = MONITORSTATUS;
        }

        public String getREMARK() {
            return REMARK;
        }

        public void setREMARK(String REMARK) {
            this.REMARK = REMARK;
        }

        public String getINTRANETIP() {
            return INTRANETIP;
        }

        public void setINTRANETIP(String INTRANETIP) {
            this.INTRANETIP = INTRANETIP;
        }

        public String getMOBILEPORT() {
            return MOBILEPORT;
        }

        public void setMOBILEPORT(String MOBILEPORT) {
            this.MOBILEPORT = MOBILEPORT;
        }

        public String getEXTRANET() {
            return EXTRANET;
        }

        public void setEXTRANET(String EXTRANET) {
            this.EXTRANET = EXTRANET;
        }

        public String getUSERNAME() {
            return USERNAME;
        }

        public void setUSERNAME(String USERNAME) {
            this.USERNAME = USERNAME;
        }

        public String getPASSWORD() {
            return PASSWORD;
        }

        public void setPASSWORD(String PASSWORD) {
            this.PASSWORD = PASSWORD;
        }
    }

    public static class AntitheftConfigBean {
        /**
         * ID : 2
         * MONITORIP : 192.168.1.18
         * TD : 2
         * YPLXJID : 1
         * MONITORSTATUS : 1
         * REMARK : 2017-05-22 14:16:22
         * INTRANETIP :
         * MOBILEPORT : 8000
         * EXTRANET : 222.85.147.92:81
         * USERNAME : admin
         * PASSWORD : sfb12345
         */

        private String ID;
        private String MONITORIP;
        private String TD;
        private String YPLXJID;
        private String MONITORSTATUS;
        private String REMARK;
        private String INTRANETIP;
        private String MOBILEPORT;
        private String EXTRANET;
        private String USERNAME;
        private String PASSWORD;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getMONITORIP() {
            return MONITORIP;
        }

        public void setMONITORIP(String MONITORIP) {
            this.MONITORIP = MONITORIP;
        }

        public String getTD() {
            return TD;
        }

        public void setTD(String TD) {
            this.TD = TD;
        }

        public String getYPLXJID() {
            return YPLXJID;
        }

        public void setYPLXJID(String YPLXJID) {
            this.YPLXJID = YPLXJID;
        }

        public String getMONITORSTATUS() {
            return MONITORSTATUS;
        }

        public void setMONITORSTATUS(String MONITORSTATUS) {
            this.MONITORSTATUS = MONITORSTATUS;
        }

        public String getREMARK() {
            return REMARK;
        }

        public void setREMARK(String REMARK) {
            this.REMARK = REMARK;
        }

        public String getINTRANETIP() {
            return INTRANETIP;
        }

        public void setINTRANETIP(String INTRANETIP) {
            this.INTRANETIP = INTRANETIP;
        }

        public String getMOBILEPORT() {
            return MOBILEPORT;
        }

        public void setMOBILEPORT(String MOBILEPORT) {
            this.MOBILEPORT = MOBILEPORT;
        }

        public String getEXTRANET() {
            return EXTRANET;
        }

        public void setEXTRANET(String EXTRANET) {
            this.EXTRANET = EXTRANET;
        }

        public String getUSERNAME() {
            return USERNAME;
        }

        public void setUSERNAME(String USERNAME) {
            this.USERNAME = USERNAME;
        }

        public String getPASSWORD() {
            return PASSWORD;
        }

        public void setPASSWORD(String PASSWORD) {
            this.PASSWORD = PASSWORD;
        }
    }
}
