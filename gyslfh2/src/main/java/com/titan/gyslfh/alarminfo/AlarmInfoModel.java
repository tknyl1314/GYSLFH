package com.titan.gyslfh.alarminfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by whs on 2017/5/3
 */

public class AlarmInfoModel {


    /**
     * recordCount : 1
     * ds : [{"ID":"1","DAILYID":"gys201705030001","UNIONID":"","RECEIPTTIME":"2017/5/3 10:24:20","ORIGINID":"5","TEL":"1234567,76543321","LON":"106.512190","LAT":"26.81600640","POLICECASE":"出动警力情况测试","POLICETIME":"2017/5/3 11:24:05","REPORTTIME":"2017/5/3 11:24:03","REPORTCASE":"上报情况测试","REMARK":"备注情况测试","ADDRESS":"修文县谷堡乡平寨村","DQID":"572","ORIGIN":"森林防火部门","NOTICEAREA":"白云区,花溪区,经济技术开发区,观山湖区,乌当区,清镇市,云岩区,南明区,开阳县,息烽县,修文县","ROWNUM":"1"}]
     */

    private String recordCount;
    private List<AlarmInfo> ds;

    public String getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(String recordCount) {
        this.recordCount = recordCount;
    }

    public List<AlarmInfo> getDs() {
        return ds;
    }

    public void setDs(List<AlarmInfo> ds) {
        this.ds = ds;
    }

    public static class AlarmInfo implements Serializable {

        /**
         * ID : 1
         * DAILYID : gys201705030001
         * UNIONID :
         * RECEIPTTIME : 2017/5/3 10:24:20
         * ORIGINID : 5
         * TEL : 1234567,76543321
         * LON : 106.512190
         * LAT : 26.81600640
         * POLICECASE : 出动警力情况测试
         * POLICETIME : 2017/5/3 11:24:05
         * REPORTTIME : 2017/5/3 11:24:03
         * REPORTCASE : 上报情况测试
         * REMARK : 备注情况测试
         * ADDRESS : 修文县谷堡乡平寨村
         * DQID : 572
         * ORIGIN : 森林防火部门
         * NOTICEAREA : 白云区,花溪区,经济技术开发区,观山湖区,乌当区,清镇市,云岩区,南明区,开阳县,息烽县,修文县
         * ROWNUM : 1
         *
         * ISFIRE:是否火警
         *
         * BACKID:回警ID
         */

        private String ID;
        private String DAILYID;
        private String UNIONID;
        private String RECEIPTTIME;
        private String ORIGINID;
        private String TEL;
        private String LON;
        private String LAT;
        private String POLICECASE;
        private String POLICETIME;
        private String REPORTTIME;
        private String REPORTCASE;
        private String REMARK;
        private String ADDRESS;
        private String DQID;
        private String ORIGIN;
        private String NOTICEAREA;
        private String ROWNUM;
        private String ISFIRE;
        /**
         * BACKSTATUS : 0
         * RN : 1
         */

        private String BACKSTATUS;
        private String RN;

        public String getBACKID() {
            return BACKID;
        }

        public void setBACKID(String BACKID) {
            this.BACKID = BACKID;
        }

        private String BACKID;
        /**
         *  :
         */



        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getDAILYID() {
            return DAILYID;
        }

        public void setDAILYID(String DAILYID) {
            this.DAILYID = DAILYID;
        }

        public String getUNIONID() {
            return UNIONID;
        }

        public void setUNIONID(String UNIONID) {
            this.UNIONID = UNIONID;
        }

        public String getRECEIPTTIME() {
            return RECEIPTTIME;
        }

        public void setRECEIPTTIME(String RECEIPTTIME) {
            this.RECEIPTTIME = RECEIPTTIME;
        }

        public String getORIGINID() {
            return ORIGINID;
        }

        public void setORIGINID(String ORIGINID) {
            this.ORIGINID = ORIGINID;
        }

        public String getTEL() {
            return TEL;
        }

        public void setTEL(String TEL) {
            this.TEL = TEL;
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

        public String getREMARK() {
            return REMARK;
        }

        public void setREMARK(String REMARK) {
            this.REMARK = REMARK;
        }

        public String getADDRESS() {
            return ADDRESS;
        }

        public void setADDRESS(String ADDRESS) {
            this.ADDRESS = ADDRESS;
        }

        public String getDQID() {
            return DQID;
        }

        public void setDQID(String DQID) {
            this.DQID = DQID;
        }

        public String getORIGIN() {
            return ORIGIN;
        }

        public void setORIGIN(String ORIGIN) {
            this.ORIGIN = ORIGIN;
        }

        public String getNOTICEAREA() {
            return NOTICEAREA;
        }

        public void setNOTICEAREA(String NOTICEAREA) {
            this.NOTICEAREA = NOTICEAREA;
        }

        public String getROWNUM() {
            return ROWNUM;
        }

        public void setROWNUM(String ROWNUM) {
            this.ROWNUM = ROWNUM;
        }

        public String getISFIRE() {
            return ISFIRE;
        }

        public void setISFIRE(String ISFIRE) {
            this.ISFIRE = ISFIRE;
        }

        public String getBACKSTATUS() {
            return BACKSTATUS;
        }

        public void setBACKSTATUS(String BACKSTATUS) {
            this.BACKSTATUS = BACKSTATUS;
        }

        public String getRN() {
            return RN;
        }

        public void setRN(String RN) {
            this.RN = RN;
        }
    }
}
