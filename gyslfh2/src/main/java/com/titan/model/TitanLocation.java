package com.titan.model;

import java.io.Serializable;

/**
 * Created by whs on 2017/6/26
 * 点位信息
 */

public class TitanLocation implements Serializable {
    //经度
    private double lon;
    //纬度
    private double lat;
    //地址
    private String address;

    public TitanLocation(double lon, double lat, String address) {
        this.lon = lon;
        this.lat = lat;
        this.address = address;
    }

    public TitanLocation(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
