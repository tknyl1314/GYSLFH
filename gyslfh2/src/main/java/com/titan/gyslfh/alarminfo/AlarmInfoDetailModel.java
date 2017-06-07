package com.titan.gyslfh.alarminfo;

import java.util.List;

/**
 * Created by whs on 2017/5/26
 * 接警加回警信息
 */

public class AlarmInfoDetailModel {

    private List<AlarmBackRecordBean> AlarmBackRecord;

    public List<AlarmBackRecordBean> getAlarmBackRecord() {
        return AlarmBackRecord;
    }

    public void setAlarmBackRecord(List<AlarmBackRecordBean> AlarmBackRecord) {
        this.AlarmBackRecord = AlarmBackRecord;
    }

    public static class AlarmBackRecordBean {
        private List<AlarmInfoModel.AlarmInfo> ReceiptInfo;
        private List<BackInfoBean> BackInfo;

        public List<AlarmInfoModel.AlarmInfo> getReceiptInfo() {
            return ReceiptInfo;
        }

        public void setReceiptInfo(List<AlarmInfoModel.AlarmInfo> ReceiptInfo) {
            this.ReceiptInfo = ReceiptInfo;
        }

        public List<BackInfoBean> getBackInfo() {
            return BackInfo;
        }

        public void setBackInfo(List<BackInfoBean> BackInfo) {
            this.BackInfo = BackInfo;
        }

        public static class ReceiptInfoBean {
            /**
             * ID : 4
             * DAILYID : gys201705040001
             * UNIONID :
             * RECEIPTTIME : 2017/2/27 9:27:23
             * ORIGINID : 1
             * ADRESS : dvfd
             * LON :
             * LAT :
             * POLICECASE :
             * POLICETIME :
             * REPORTCASE :
             * REPORTTIME :
             * USERID : 7
             * TEL : 125367
             * DQNAME : 白云区艳山红镇高山村
             * DQID : 1
             * ISWORK : 0
             * REMARK :
             * REMARK1 :
             * REMARK2 :
             */

            private String ID;
            private String DAILYID;
            private String UNIONID;
            private String RECEIPTTIME;
            private String ORIGINID;
            private String ADRESS;
            private String LON;
            private String LAT;
            private String POLICECASE;
            private String POLICETIME;
            private String REPORTCASE;
            private String REPORTTIME;
            private String USERID;
            private String TEL;
            private String DQNAME;
            private String DQID;
            private String ISWORK;
            private String REMARK;
            private String REMARK1;
            private String REMARK2;

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

            public String getADRESS() {
                return ADRESS;
            }

            public void setADRESS(String ADRESS) {
                this.ADRESS = ADRESS;
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

            public String getREPORTCASE() {
                return REPORTCASE;
            }

            public void setREPORTCASE(String REPORTCASE) {
                this.REPORTCASE = REPORTCASE;
            }

            public String getREPORTTIME() {
                return REPORTTIME;
            }

            public void setREPORTTIME(String REPORTTIME) {
                this.REPORTTIME = REPORTTIME;
            }

            public String getUSERID() {
                return USERID;
            }

            public void setUSERID(String USERID) {
                this.USERID = USERID;
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

            public String getDQID() {
                return DQID;
            }

            public void setDQID(String DQID) {
                this.DQID = DQID;
            }

            public String getISWORK() {
                return ISWORK;
            }

            public void setISWORK(String ISWORK) {
                this.ISWORK = ISWORK;
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
        //回警信息
        public static class BackInfoBean {
            /**
             * ID : 22
             * RECEIPTID : 4
             * DQID : 1459
             * DQNAME : 白云区
             * CHECKER : adsa
             * BACKTIME : 2017/5/4 18:07:01
             * BACKSTATUS : 3
             * ISFIRE : 1
             * FIREYTYPE :
             * FIRESIUATION :
             * POLICESIUATION :
             * BELONGAREAID :
             * BELONGAREANAME :
             * USERID : 4
             * ISREAD : 0
             * INRELATION :
             * REMARK :
             * REMARK1 :
             * REMARK2 :
             */

            private String ID;
            private String RECEIPTID;
            private String DQID;
            private String DQNAME;
            private String CHECKER;
            private String BACKTIME;
            private String BACKSTATUS;
            private String ISFIRE;
            private String FIREYTYPE;
            private String FIRESIUATION;
            private String POLICESIUATION;
            private String BELONGAREAID;
            private String BELONGAREANAME;
            private String USERID;
            private String ISREAD;
            private String INRELATION;
            private String REMARK;
            private String REMARK1;
            private String REMARK2;

            public String getID() {
                return ID;
            }

            public void setID(String ID) {
                this.ID = ID;
            }

            public String getRECEIPTID() {
                return RECEIPTID;
            }

            public void setRECEIPTID(String RECEIPTID) {
                this.RECEIPTID = RECEIPTID;
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

            public String getFIREYTYPE() {
                return FIREYTYPE;
            }

            public void setFIREYTYPE(String FIREYTYPE) {
                this.FIREYTYPE = FIREYTYPE;
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

            public String getUSERID() {
                return USERID;
            }

            public void setUSERID(String USERID) {
                this.USERID = USERID;
            }

            public String getISREAD() {
                return ISREAD;
            }

            public void setISREAD(String ISREAD) {
                this.ISREAD = ISREAD;
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
    }
}
