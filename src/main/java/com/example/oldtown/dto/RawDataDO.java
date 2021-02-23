package com.example.oldtown.dto;

import lombok.Data;

/**
 * @author ding.yp
 * @name 小程序用户非敏感字段
 * @info
 * @date 2020/11/23
 */

@Data
public class RawDataDO {
    private String nickName;
    private String avatarUrl;
    private Integer gender;
    private String city;
    private String country;
    private String province;
}
