package com.example.oldtown.modules.trf.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import com.example.oldtown.handler.GeometryTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 交通接驳行驶里程
 * </p>
 *
 * @author dyp
 * @since 2021-01-14
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName(autoResultMap = true)
@ApiModel(value="TrfMileage对象", description="交通接驳行驶里程")
public class TrfMileage implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键,新建时不传")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "gps设备编号")
    private String gpsCode;

    @ApiModelProperty(value = "里程,单位千米")
    private Double miles;

    @ApiModelProperty(value = "日期")
    private Date date;


}
