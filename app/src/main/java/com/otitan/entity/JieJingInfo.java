package com.otitan.entity;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

   
public class JieJingInfo {

   @JsonProperty("ID")
   private String id;
   @JsonProperty("UNIONID")
   private String unionid;
   @JsonProperty("ADRESS")
   private String adress;
   @JsonProperty("TEL_ONE")
   private String telOne;
   @JsonProperty("DQ_NAME")
   private String dqName;
   @JsonProperty("ISFIRE")
   private String isfire;
   @JsonProperty("FIRETYPE")
   private String firetype;
   @JsonProperty("BACKSTATE")
   private int backstate;
   @JsonProperty("RECEIPTTIME")
   private String receipttime;


    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
    public String getUnionid() {
        return unionid;
    }
    

    public void setAdress(String adress) {
        this.adress = adress;
    }
    public String getAdress() {
        return adress;
    }
    

    public void setTelOne(String telOne) {
        this.telOne = telOne;
    }
    public String getTelOne() {
        return telOne;
    }
    

    public void setDqName(String dqName) {
        this.dqName = dqName;
    }
    public String getDqName() {
        return dqName;
    }
    

    public void setIsfire(String isfire) {
        this.isfire = isfire;
    }
    public String getIsfire() {
        return isfire;
    }
    

    public void setFiretype(String firetype) {
        this.firetype = firetype;
    }
    public String getFiretype() {
        return firetype;
    }
    

    public void setBackstate(int backstate) {
        this.backstate = backstate;
    }
    public int getBackstate() {
        return backstate;
    }
    

    public void setReceipttime(String receipttime) {
        this.receipttime = receipttime;
    }
    public String getReceipttime() {
        return receipttime;
    }
    
}