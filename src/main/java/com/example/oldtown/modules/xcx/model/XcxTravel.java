package com.example.oldtown.modules.xcx.model;

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
 * 小程序用户行程
 * </p>
 *
 * @author dyp
 * @since 2020-12-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(autoResultMap = true)
@ApiModel(value="XcxTravel对象", description="小程序用户行程")
public class XcxTravel implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键,新建时不传")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "路线id", hidden = true)
    private Long routeId;

    @ApiModelProperty(value = "路线名称",hidden = true)
    private String name;

    @ApiModelProperty(value = "封面地址,不含前缀", hidden = true)
    private String coverUrl;

    @ApiModelProperty(value = "标签")
    private String label;

    @ApiModelProperty(value = "计划游玩天数")
    private Integer dayNum;

    @ApiModelProperty(value = "游玩人数")
    private Integer touristNum;

    @ApiModelProperty(value = "出发时间")
    private Date startTime;

    @ApiModelProperty(value = "#数据创建时间" , hidden = true)
    private Date createTime;


}
