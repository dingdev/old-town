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
 * 交通接驳安保车辆
 * </p>
 *
 * @author dyp
 * @since 2020-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(autoResultMap = true)
@ApiModel(value="TrfSecurityCar对象", description="交通接驳安保车辆")
public class TrfSecurityCar implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键,新建时不传")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "编号")
    private String serial;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "gps设备编号")
    private String gpsCode;

    @ApiModelProperty(value = "安保人员id")
    private Long staffId;

    @ApiModelProperty(value = "图片地址,不含前缀,多个时英文逗号分隔")
    private String pictureUrl;

    @ApiModelProperty(value = "总客容量")
    private Integer capacityNum;

    @ApiModelProperty(value = "当前载客量")
    private Integer currentNum;

    @ApiModelProperty(value = "基本信息")
    private String baseInfo;

    @ApiModelProperty(value = "维修保养信息")
    private String repairInfo;

    @ApiModelProperty(value = "当前状态")
    private String currentStatus;

    @ApiModelProperty(value = "经度")
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    private Double latitude;

    @ApiModelProperty(value = "#gps定位时间戳,单位秒", hidden = true)
    private Long gpsTime;

    @ApiModelProperty(value = "禁用与否,0:默认启用,1:禁用")
    private Integer deleted;

    @ApiModelProperty(value = "#创建时间" , hidden = true)
    private Date createTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "#安保人员姓名" , hidden = true)
    @TableField(exist = false)
    private String staffName;

    @ApiModelProperty(value = "#安保人员编号" , hidden = true)
    @TableField(exist = false)
    private String staffSerial;

    @ApiModelProperty(value = "#安保人员电话" , hidden = true)
    @TableField(exist = false)
    private String staffTel;


}
