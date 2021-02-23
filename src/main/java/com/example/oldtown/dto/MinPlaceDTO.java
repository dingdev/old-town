package com.example.oldtown.dto;

/**
 * @author ding.yp
 * @name
 * @info
 * @date 2020/12/28
 */


public class MinPlaceDTO {
    private Long id;
    private String name;
    private String pictureUrl;
    private Integer viewNum;

    public MinPlaceDTO(Long id, String name, String pictureUrl, Integer viewNum) {
        this.id = id;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.viewNum = viewNum;
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

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Integer getViewNum() {
        return viewNum;
    }

    public void setViewNum(Integer viewNum) {
        this.viewNum = viewNum;
    }
}
