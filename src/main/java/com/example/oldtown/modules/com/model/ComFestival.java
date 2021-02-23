package com.example.oldtown.modules.com.model;

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
 * 通用节假日数据
 * </p>
 *
 * @author dyp
 * @since 2021-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(autoResultMap = true)
@ApiModel(value="ComFestival对象", description="通用节假日数据")
public class ComFestival implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键,新建时不传")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "节假名称")
    private String name;

    @ApiModelProperty(value = "节假期间游客总数")
    private Integer visitorNum;

    @ApiModelProperty(value = "节假期间销售金额")
    private Double salesAmount;

    @ApiModelProperty(value = "所属年份")
    private Integer year;

    @ApiModelProperty(value = "开始日期")
    private Date startTime;

    @ApiModelProperty(value = "禁用与否:0未禁,1已禁")
    private Integer deleted;

    @ApiModelProperty(value = "#创建时间" , hidden = true)
    private Date createTime;

    @ApiModelProperty(value = "备注")
    private String remarks;


}
