package com.example.oldtown.dto;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ding.yp
 * @name
 * @info
 * @date 2021/01/15
 */

public class MinTrfVehicle implements Serializable {
    private Long id;
    private String name;
    private String gpsCode;
    private Long gpsTime;
    private Double longitude;

    public MinTrfVehicle() {
    }

    public MinTrfVehicle(Long id, String name, String gpsCode, Long gpsTime, Double longitude) {
        this.id = id;
        this.name = name;
        this.gpsCode = gpsCode;
        this.gpsTime = gpsTime;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGpsCode() {
        return gpsCode;
    }

    public void setGpsCode(String gpsCode) {
        this.gpsCode = gpsCode;
    }

    public Long getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(Long gpsTime) {
        this.gpsTime = gpsTime;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}

