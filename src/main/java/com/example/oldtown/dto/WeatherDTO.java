package com.example.oldtown.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ding.yp
 * @name
 * @info
 * @date 2020/12/03
 */

@Data
public class WeatherDTO implements Serializable {
    private String date;
    private String week;
    private String city;
    private String wea;
    private String wea_img;
    private String tem;
    private String tem1;
    private String tem2;
    private String win;
    private String win_speed;
    private String humidity;
    private String air_level;
}
