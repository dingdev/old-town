package com.example.oldtown.modules.sys.model;

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
 * 系统用户
 * </p>
 *
 * @author dyp
 * @since 2020-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(autoResultMap = true)
@ApiModel(value="SysAdmin对象", description="系统用户")
public class SysAdmin implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键,新建时不传")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "编号")
    private String serial;

    @ApiModelProperty(value = "头像地址,不含前缀")
    private String icon;

    @ApiModelProperty(value = "手机号")
    private String tel;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "#创建时间" , hidden = true)
    private Date createTime;

    @ApiModelProperty(value = "#最后登录时间" , hidden = true)
    private Date loginTime;

    @ApiModelProperty(value = "备注信息")
    private String remarks;

    @ApiModelProperty(value = "禁用与否:0未禁,1已禁")
    private Integer deleted;


}
