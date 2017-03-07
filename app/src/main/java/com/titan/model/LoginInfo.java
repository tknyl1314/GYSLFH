package com.titan.model;

import java.util.List;

/**
 * Created by whs on 2017/3/2
 */

public class LoginInfo {

    /**
     * re : success'
     * ds : [{"USERNAME":"admin","ROLE_ID":1,"PASSWORD":"C127E5917D58A2EBA9492FBE7DC54456","REALNAME":"管理员","TELNO":"","MOBILEPHONENO":"","USEREMAIL":"","DEPTNAME":"林业局","DEPTID":1,"DQLEVEL":1,"UNITID":1,"ID":-10}]
     */

    private String re;
    private List<DsBean> ds;

    public String getRe() {
        return re;
    }

    public void setRe(String re) {
        this.re = re;
    }

    public List<DsBean> getDs() {
        return ds;
    }

    public void setDs(List<DsBean> ds) {
        this.ds = ds;
    }

    public static class DsBean {
        /**
         * USERNAME : admin
         * ROLE_ID : 1
         * PASSWORD : C127E5917D58A2EBA9492FBE7DC54456
         * REALNAME : 管理员
         * TELNO :
         * MOBILEPHONENO :
         * USEREMAIL :
         * DEPTNAME : 林业局
         * DEPTID : 1
         * DQLEVEL : 1
         * UNITID : 1
         * ID : -10
         */

        private String USERNAME;
        private int ROLE_ID;
        private String PASSWORD;
        private String REALNAME;
        private String TELNO;
        private String MOBILEPHONENO;
        private String USEREMAIL;
        private String DEPTNAME;
        private int DEPTID;
        private int DQLEVEL;
        private int UNITID;
        private int ID;

        public String getUSERNAME() {
            return USERNAME;
        }

        public void setUSERNAME(String USERNAME) {
            this.USERNAME = USERNAME;
        }

        public int getROLE_ID() {
            return ROLE_ID;
        }

        public void setROLE_ID(int ROLE_ID) {
            this.ROLE_ID = ROLE_ID;
        }

        public String getPASSWORD() {
            return PASSWORD;
        }

        public void setPASSWORD(String PASSWORD) {
            this.PASSWORD = PASSWORD;
        }

        public String getREALNAME() {
            return REALNAME;
        }

        public void setREALNAME(String REALNAME) {
            this.REALNAME = REALNAME;
        }

        public String getTELNO() {
            return TELNO;
        }

        public void setTELNO(String TELNO) {
            this.TELNO = TELNO;
        }

        public String getMOBILEPHONENO() {
            return MOBILEPHONENO;
        }

        public void setMOBILEPHONENO(String MOBILEPHONENO) {
            this.MOBILEPHONENO = MOBILEPHONENO;
        }

        public String getUSEREMAIL() {
            return USEREMAIL;
        }

        public void setUSEREMAIL(String USEREMAIL) {
            this.USEREMAIL = USEREMAIL;
        }

        public String getDEPTNAME() {
            return DEPTNAME;
        }

        public void setDEPTNAME(String DEPTNAME) {
            this.DEPTNAME = DEPTNAME;
        }

        public int getDEPTID() {
            return DEPTID;
        }

        public void setDEPTID(int DEPTID) {
            this.DEPTID = DEPTID;
        }

        public int getDQLEVEL() {
            return DQLEVEL;
        }

        public void setDQLEVEL(int DQLEVEL) {
            this.DQLEVEL = DQLEVEL;
        }

        public int getUNITID() {
            return UNITID;
        }

        public void setUNITID(int UNITID) {
            this.UNITID = UNITID;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }
    }
}
