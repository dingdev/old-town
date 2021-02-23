package com.example.oldtown.modules.trf.model;

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
 * 交通接驳打捞点
 * </p>
 *
 * @author dyp
 * @since 2020-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(autoResultMap = true)
@ApiModel(value="TrfSweepPoint对象", description="交通接驳打捞点")
public class TrfSweepPoint implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键,新建时不传")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "编号")
    private String serial;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "打捞船标准路线id")
    private Long routeId;

    @ApiModelProperty(value = "经度")
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    private Double latitude;

    @ApiModelProperty(value = "每天需经过的次数")
    private Integer dailyTimes;

    @ApiModelProperty(value = "禁用与否,0:默认启用,1:禁用")
    private Integer deleted;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "#创建时间" , hidden = true)
    private Date createTime;


}
