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
    /**状态 1:已上传到服务器 0:未上传 */
    @Property(nameInDb = "STATUS")
    private int status;
    @Generated(hash = 911014896)
    public TrackPoint(Long id, @NotNull String time, double lon, double lat,
            String userid, int status) {
        this.id = id;
        this.time = time;
        this.lon = lon;
        this.lat = lat;
        this.userid = userid;
        this.status = status;
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
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    



}
