package com.example.oldtown.modules.trf.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import com.example.oldtown.handler.GeometryTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 交通接驳打捞船标准路线
 * </p>
 *
 * @author dyp
 * @since 2020-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(autoResultMap = true)
@ApiModel(value="TrfSweepRoute对象", description="交通接驳打捞船标准路线")
public class TrfSweepRoute implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键，新建时不传")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "简介")
    private String intro;

    @ApiModelProperty(value = "@线的地理信息,形如LINESTRING(经度 纬度,...)")
    @TableField(typeHandler = GeometryTypeHandler.class)
    private String lineString;

    @ApiModelProperty(value = "时间和打捞点计划文本")
    private String schedule;

    @ApiModelProperty(value = "颜色,16进制表示")
    private String color;

    @ApiModelProperty(value = "禁用与否:0未禁,1已禁")
    private Integer deleted;

    @ApiModelProperty(value = "#创建时间" , hidden = true)
    private Date createTime;

    @ApiModelProperty(value = "#更新时间" , hidden = true)
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remarks;


}
