package com.example.oldtown.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.oldtown.handler.GeometryTypeHandler;
import com.example.oldtown.modules.com.model.ComPlace;
import com.example.oldtown.modules.com.model.ComRoute;
import com.example.oldtown.modules.xcx.model.XcxTravel;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author ding.yp
 * @name
 * @info
 * @date 2020/12/07
 */


public class XcxTravelDTO {
    @JsonUnwrapped
    private XcxTravel xcxTravel;
    private String intro;
    private Double length;
    private String lineString;
    private String detailsUrl;
    private List<MinPointDTO> pointList;

    public XcxTravelDTO(XcxTravel xcxTravel, String intro, Double length, String lineString, String detailsUrl, List<MinPointDTO> pointList) {
        this.xcxTravel = xcxTravel;
        this.intro = intro;
        this.length = length;
        this.lineString = lineString;
        this.detailsUrl = detailsUrl;
        this.pointList = pointList;
    }

    public XcxTravel getXcxTravel() {
        return xcxTravel;
    }

    public void setXcxTravel(XcxTravel xcxTravel) {
        this.xcxTravel = xcxTravel;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public String getLineString() {
        return lineString;
    }

    public void setLineString(String lineString) {
        this.lineString = lineString;
    }

    public List<MinPointDTO> getPointList() {
        return pointList;
    }

    public void setPointList(List<MinPointDTO> pointList) {
        this.pointList = pointList;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }
}
