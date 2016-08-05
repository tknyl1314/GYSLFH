package com.otitan.entity;

public class ReceiveAlarmInfo {
	//灏嗗璞¤浆鎴愭暟缁�
	public String[] toStringarray() {
		String [] result=new String[]{getId(),getDailyid(),getUnionid(),getAdress(),getTelone(),getInformareawatcher(),getIsfire(),getFiretype(),getPolicecase(),getReceipttime()};
		return result;
	}

	public String getBackstate() {
		return backstate;
	}
	public void setBackstate(String backstate) {
		this.backstate = backstate;
	}
	public String getAlarmtime() {
		return alarmtime;
	}
	public void setAlarmtime(String alarmtime) {
		this.alarmtime = alarmtime;
	}
	
	private String id;
     private String dailyid;
     private String unionid;
     private String receipttime;
     private String origin;
     private String adress;
     //锟角凤拷锟斤拷锟�
     private String isfire;
     //锟斤拷锟斤拷锟斤拷锟斤拷
     private String firetype;
     //锟截撅拷状态
     private String backstate;
     //锟斤拷锟斤拷时锟斤拷
     private String alarmtime;
     private double lng;
     private double lat;
     private Integer policecar;
     //市局出警情况
     private String policecase;
     private String policetime;
     private String reportcase;
     private String reporttime;
     private Integer divisionid;
     private Integer userid;
     //通知区县
     private String informareawatcher;
     
     private String informwatcher;
     private String remark;
     private String telone;
     private String teltwo;
     private String infolcareawatcher;
     private Integer policepeople;
     private String beforecityfire;
     private String remark1;
     private String remark2;
     
     public String getIsfire() {
 		return isfire;
 	}
 	public void setIsfire(String isfire) {
 		this.isfire = isfire;
 	}
     public String getId() {
 		return id;
 	}
 	public void setId(String string) {
 		this.id = string;
 	}
 	public String getDailyid() {
 		return dailyid;
 	}
 	public void setDailyid(String dailyid) {
 		this.dailyid = dailyid;
 	}
 	public String getUnionid() {
 		return unionid;
 	}
 	public void setUnionid(String unionid) {
 		this.unionid = unionid;
 	}
 	public String getReceipttime() {
 		return receipttime;
 	}
 	public void setReceipttime(String receipttime) {
 		this.receipttime = receipttime;
 	}
 	public String getOrigin() {
 		return origin;
 	}
 	public void setOrigin(String origin) {
 		this.origin = origin;
 	}
 	public String getAdress() {
 		return adress;
 	}
 	public void setAdress(String adress) {
 		this.adress = adress;
 	}
 	public double getLng() {
 		return lng;
 	}
 	public void setLng(double lng) {
 		this.lng = lng;
 	}
 	public double getLat() {
 		return lat;
 	}
 	public void setLat(double lat) {
 		this.lat = lat;
 	}
 	public int getPolicecar() {
 		return policecar;
 	}
 	public void setPolicecar(int policecar) {
 		this.policecar = policecar;
 	}
 	public String getPolicecase() {
 		return policecase;
 	}
 	public void setPolicecase(String policecase) {
 		this.policecase = policecase;
 	}
 	public String getPolicetime() {
 		return policetime;
 	}
 	public void setPolicetime(String policetime) {
 		this.policetime = policetime;
 	}
 	public String getReportcase() {
 		return reportcase;
 	}
 	public void setReportcase(String reportcase) {
 		this.reportcase = reportcase;
 	}
 	public String getReporttime() {
 		return reporttime;
 	}
 	public void setReporttime(String reporttime) {
 		this.reporttime = reporttime;
 	}
 	public int getDivisionid() {
 		return divisionid;
 	}
 	public void setDivisionid(int divisionid) {
 		this.divisionid = divisionid;
 	}
 	public int getUserid() {
 		return userid;
 	}
 	public void setUserid(int userid) {
 		this.userid = userid;
 	}
 	public String getInformareawatcher() {
 		return informareawatcher;
 	}
 	public void setInformareawatcher(String informareawatcher) {
 		this.informareawatcher = informareawatcher;
 	}
 	public String getInformwatcher() {
 		return informwatcher;
 	}
 	public void setInformwatcher(String informwatcher) {
 		this.informwatcher = informwatcher;
 	}
 	public String getRemark() {
 		return remark;
 	}
 	public void setRemark(String remark) {
 		this.remark = remark;
 	}
 	public String getTelone() {
 		return telone;
 	}
 	public void setTelone(String telone) {
 		this.telone = telone;
 	}
 	public String getTeltwo() {
 		return teltwo;
 	}
 	public void setTeltwo(String teltwo) {
 		this.teltwo = teltwo;
 	}
 	public String getInfolcareawatcher() {
 		return infolcareawatcher;
 	}
 	public void setInfolcareawatcher(String infolcareawatcher) {
 		this.infolcareawatcher = infolcareawatcher;
 	}
 	public int getPolicepeople() {
 		return policepeople;
 	}
 	public void setPolicepeople(int policepeople) {
 		this.policepeople = policepeople;
 	}
 	public String getBeforecityfire() {
 		return beforecityfire;
 	}
 	public void setBeforecityfire(String beforecityfire) {
 		this.beforecityfire = beforecityfire;
 	}
 	public String getRemark1() {
 		return remark1;
 	}
 	public void setRemark1(String remark1) {
 		this.remark1 = remark1;
 	}
 	public String getRemark2() {
 		return remark2;
 	}
 	public void setRemark2(String remark2) {
 		this.remark2 = remark2;
 	}
	public String getFiretype() {
		return firetype;
	}
	public void setFiretype(String firetype) {
		this.firetype = firetype;
	}
	

}
