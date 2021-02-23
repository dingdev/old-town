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
 * 交通接驳打捞船员工考核
 * </p>
 *
 * @author dyp
 * @since 2020-12-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(autoResultMap = true)
@ApiModel(value="TrfSweepCheck对象", description="交通接驳打捞船员工考核")
public class TrfSweepCheck implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键,新建时不传")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "打捞船员工id")
    private Long staffId;

    @ApiModelProperty(value = "员工编号")
    private String serial;

    @ApiModelProperty(value = "员工姓名")
    private String username;

    @ApiModelProperty(value = "打捞船id")
    private Long sweepId;

    @ApiModelProperty(value = "gps设备编号")
    private String gpsCode;

    @ApiModelProperty(value = "标准路线id")
    private Long routeId;

    @ApiModelProperty(value = "联系方式")
    private String tel;

    @ApiModelProperty(value = "原因记录")
    private String reason;

    @ApiModelProperty(value = "禁用与否,0:默认启用,1:禁用")
    private Integer deleted;

    @ApiModelProperty(value = "日期")
    private Date date;

    @ApiModelProperty(value = "备注")
    private String remarks;


}
