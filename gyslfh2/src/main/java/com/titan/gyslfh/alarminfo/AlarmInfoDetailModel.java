package com.titan.gyslfh.alarminfo;

import java.util.List;

/**
 * Created by whs on 2017/5/26
 * 接警和回警信息
 */

public class AlarmInfoDetailModel {
    /**
     * AlarmInfo : {"ID":"83","DAILYID":"gys201706110052","UNIONID":"","RECEIPTTIME":"2017/2/27 星期一 9:27:23","ORIGINID":"1","ADRESS":"贵州省贵阳市乌当区东风镇高穴村","LON":"106.75663920","LAT":"26.70217790","POLICECASE":"热同仁堂","POLICETIME":"2017/6/13 星期二 10:09:19","REPORTCASE":"ret ","REPORTTIME":"2017/6/13 星期二 10:09:15","USERID":"1","TEL":"13678907654","DQNAME":"贵阳市","DQID":"1470","ISWORK":"0","REMARK":"人都退热贴32543543dsfd","REMARK1":"","REMARK2":""}
     * BackInfo : [{"ID":29,"RECEIPTID":83,"DQID":1462,"DQNAME":"开阳县","CHECKER":"张三","BACKTIME":"2017-06-13T10:13:08","BACKSTATUS":3,"ISFIRE":0,"FIREYTYPE":-1,"FIRESIUATION":"已经熄灭","POLICESIUATION":"出警10人","BELONGAREAID":null,"BELONGAREANAME":null,"USERID":16,"ISREAD":0,"INRELATION":null,"REMARK":null,"REMARK1":"2017-06-13 10:14:23","REMARK2":null},{"ID":30,"RECEIPTID":83,"DQID":1462,"DQNAME":"开阳县","CHECKER":"张三","BACKTIME":"2017-06-13T10:13:08","BACKSTATUS":3,"ISFIRE":0,"FIREYTYPE":-1,"FIRESIUATION":"已经熄灭","POLICESIUATION":"出警10人","BELONGAREAID":null,"BELONGAREANAME":null,"USERID":16,"ISREAD":1,"INRELATION":null,"REMARK":null,"REMARK1":"2017-06-13 10:15:02","REMARK2":null}]
     */
    //接警信息
    private AlarmInfoModel.AlarmInfo AlarmInfo;
    //回警信息
    private List<BackInfoBean> BackInfo;

    public AlarmInfoModel.AlarmInfo getAlarmInfo() {
        return AlarmInfo;
    }

    public void setAlarmInfo(AlarmInfoModel.AlarmInfo AlarmInfo) {
        this.AlarmInfo = AlarmInfo;
    }

    public List<BackInfoBean> getBackInfo() {
        return BackInfo;
    }

    public void setBackInfo(List<BackInfoBean> BackInfo) {
        this.BackInfo = BackInfo;
    }

    /*public static class AlarmInfoBean {
        *//**
         * ID : 83
         * DAILYID : gys201706110052
         * UNIONID :
         * RECEIPTTIME : 2017/2/27 星期一 9:27:23
         * ORIGINID : 1
         * ADRESS : 贵州省贵阳市乌当区东风镇高穴村
         * LON : 106.75663920
         * LAT : 26.70217790
         * POLICECASE : 热同仁堂
         * POLICETIME : 2017/6/13 星期二 10:09:19
         * REPORTCASE : ret
         * REPORTTIME : 2017/6/13 星期二 10:09:15
         * USERID : 1
         * TEL : 13678907654
         * DQNAME : 贵阳市
         * DQID : 1470
         * ISWORK : 0
         * REMARK : 人都退热贴32543543dsfd
         * REMARK1 :
         * REMARK2 :
         *//*

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
    }*/

    public static class BackInfoBean {
        /**
         * ID : 29
         * RECEIPTID : 83
         * DQID : 1462
         * DQNAME : 开阳县
         * CHECKER : 张三
         * BACKTIME : 2017-06-13T10:13:08
         * BACKSTATUS : 3
         * ISFIRE : 0
         * FIREYTYPE : -1
         * FIRESIUATION : 已经熄灭
         * POLICESIUATION : 出警10人
         * BELONGAREAID : null
         * BELONGAREANAME : null
         * USERID : 16
         * ISREAD : 0
         * INRELATION : null
         * REMARK : null
         * REMARK1 : 2017-06-13 10:14:23
         * REMARK2 : null
         */

        private int ID;
        private int RECEIPTID;
        private int DQID;
        private String DQNAME;
        private String CHECKER;
        private String BACKTIME;
        private int BACKSTATUS;
        private int ISFIRE;
        private int FIREYTYPE;
        private String FIRESIUATION;
        private String POLICESIUATION;
        private Object BELONGAREAID;
        private Object BELONGAREANAME;
        private int USERID;
        private int ISREAD;
        private Object INRELATION;
        private Object REMARK;
        private String REMARK1;
        private Object REMARK2;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public int getRECEIPTID() {
            return RECEIPTID;
        }

        public void setRECEIPTID(int RECEIPTID) {
            this.RECEIPTID = RECEIPTID;
        }

        public int getDQID() {
            return DQID;
        }

        public void setDQID(int DQID) {
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

        public int getBACKSTATUS() {
            return BACKSTATUS;
        }

        public void setBACKSTATUS(int BACKSTATUS) {
            this.BACKSTATUS = BACKSTATUS;
        }

        public int getISFIRE() {
            return ISFIRE;
        }

        public void setISFIRE(int ISFIRE) {
            this.ISFIRE = ISFIRE;
        }

        public int getFIREYTYPE() {
            return FIREYTYPE;
        }

        public void setFIREYTYPE(int FIREYTYPE) {
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

        public Object getBELONGAREAID() {
            return BELONGAREAID;
        }

        public void setBELONGAREAID(Object BELONGAREAID) {
            this.BELONGAREAID = BELONGAREAID;
        }

        public Object getBELONGAREANAME() {
            return BELONGAREANAME;
        }

        public void setBELONGAREANAME(Object BELONGAREANAME) {
            this.BELONGAREANAME = BELONGAREANAME;
        }

        public int getUSERID() {
            return USERID;
        }

        public void setUSERID(int USERID) {
            this.USERID = USERID;
        }

        public int getISREAD() {
            return ISREAD;
        }

        public void setISREAD(int ISREAD) {
            this.ISREAD = ISREAD;
        }

        public Object getINRELATION() {
            return INRELATION;
        }

        public void setINRELATION(Object INRELATION) {
            this.INRELATION = INRELATION;
        }

        public Object getREMARK() {
            return REMARK;
        }

        public void setREMARK(Object REMARK) {
            this.REMARK = REMARK;
        }

        public String getREMARK1() {
            return REMARK1;
        }

        public void setREMARK1(String REMARK1) {
            this.REMARK1 = REMARK1;
        }

        public Object getREMARK2() {
            return REMARK2;
        }

        public void setREMARK2(Object REMARK2) {
            this.REMARK2 = REMARK2;
        }
    }

}
