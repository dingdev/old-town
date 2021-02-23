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
 * 交通接驳打捞船员工
 * </p>
 *
 * @author dyp
 * @since 2020-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(autoResultMap = true)
@ApiModel(value="TrfSweepStaff对象", description="交通接驳打捞船员工")
public class TrfSweepStaff implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键,新建时不传")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "编号")
    private String serial;

    @ApiModelProperty(value = "登录用户名")
    private String username;

    @ApiModelProperty(value = "登录密码")
    private String password;

    @ApiModelProperty(value = "姓名")
    private String nickname;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "gps设备编号")
    private String gpsCode;

    @ApiModelProperty(value = "头像地址,不含前缀,默认单个")
    private String avatarUrl;

    @ApiModelProperty(value = "联系方式")
    private String tel;

    @ApiModelProperty(value = "#打捞船id,在打捞船管理中设置" , hidden = true)
    private Long sweepId;

    @ApiModelProperty(value = "禁用与否,0:默认启用,1:禁用")
    private Integer deleted;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "#创建时间" , hidden = true)
    private Date createTime;

    @ApiModelProperty(value = "#打捞船名称", hidden = true)
    @TableField(exist = false)
    private String sweepName;

    @ApiModelProperty(value = "#打捞船编号", hidden = true)
    @TableField(exist = false)
    private String sweepSerial;

    @ApiModelProperty(value = "#打捞船类型", hidden = true)
    @TableField(exist = false)
    private String sweepType;

}
