package com.example.oldtown.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件上传返回结果
 * Created by dyp on 2020/11/05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MinioResponse {
    @ApiModelProperty("文件访问地址,无域名前缀,示例(/photo/景点A的图片1.jpg)")
    private String url;
    @ApiModelProperty("文件名称,尽量明确清晰,示例(景点A的图片1.jpg)")
    private String name;
}
