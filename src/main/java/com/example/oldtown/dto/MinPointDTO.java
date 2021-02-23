package com.example.oldtown.dto;

/**
 * @author ding.yp
 * @name
 * @info
 * @date 2020/12/07
 */


public class MinPointDTO {
    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    private String pictureUrl;

    public MinPointDTO(Long id, String name, Double latitude, Double longitude, String pictureUrl) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pictureUrl = pictureUrl;
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
