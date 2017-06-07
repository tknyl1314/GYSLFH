package com.titan.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 轨迹点
 */
@Entity
public class TrackPoint  {
    /**id 自增长*/
    @Id(autoincrement = true)
    private Long id;
    /**上传时间*/
    @NotNull
    @Property(nameInDb = "TIME")
    private String time;
	/**经度*/
    @Property(nameInDb = "LON")
    private double lon;
	/**纬度*/
    @Property(nameInDb = "LAT")
    private double lat;
    /**用户id*/
    @Property(nameInDb = "USERID")
    private String userid;
    /**TAG 0：常规点 1:起点 2：终点*/
    @Property(nameInDb = "TAG")
    private int tag;
    /**WKID 坐标系*/
    @Property(nameInDb = "WKID")
    private String wkid;
    /**SBH 设备号*/
    @Property(nameInDb = "SBH")
    private String sbh;
    /**状态 1:已上传到服务器 0:未上传 */
    @Property(nameInDb = "STATUS")
    private int status;
    /**REMARK 备用字段*/
    @Property(nameInDb = "REMARK")
    private String REMARK;
    @Generated(hash = 1809735010)
    public TrackPoint(Long id, @NotNull String time, double lon, double lat,
            String userid, int tag, String wkid, String sbh, int status,
            String REMARK) {
        this.id = id;
        this.time = time;
        this.lon = lon;
        this.lat = lat;
        this.userid = userid;
        this.tag = tag;
        this.wkid = wkid;
        this.sbh = sbh;
        this.status = status;
        this.REMARK = REMARK;
    }
    @Generated(hash = 546431815)
    public TrackPoint() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public double getLon() {
        return this.lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }
    public double getLat() {
        return this.lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public String getUserid() {
        return this.userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public int getTag() {
        return this.tag;
    }
    public void setTag(int tag) {
        this.tag = tag;
    }
    public String getWkid() {
        return this.wkid;
    }
    public void setWkid(String wkid) {
        this.wkid = wkid;
    }
    public String getSbh() {
        return this.sbh;
    }
    public void setSbh(String sbh) {
        this.sbh = sbh;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getREMARK() {
        return this.REMARK;
    }
    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }
   
   
   
}
