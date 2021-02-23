package com.example.oldtown.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author ding.yp
 * @name
 * @info
 * @date 2020/11/23
 */
@Data
@ApiModel(value="ComPlace对象", description="通用场所")
public class WechatLoginRequest {
    @ApiModelProperty(value = "微信code,由小程序端调用wx.login()去获取", required = true)
    private String code;
    @ApiModelProperty(value = "用户非敏感字段,头像和昵称等,由小程序端调用wx.getUserInfo()去获取")
    private String rawData;
    @ApiModelProperty(value = "签名")
    private String signature;
    @ApiModelProperty(value = "用户敏感字段,unionID等,可不填")
    private String encryptedData;
    @ApiModelProperty(value = "解密算法的向量,可不填")
    private String iv;
}
