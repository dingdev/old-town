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
 * 小程序宣传
 * </p>
 *
 * @author dyp
 * @since 2020-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(autoResultMap = true)
@ApiModel(value="XcxPropaganda对象", description="小程序宣传")
public class XcxPropaganda implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键,新建时不传")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型:视频,VR,AR,图片")
    private String type;

    @ApiModelProperty(value = "简介")
    private String intro;

    @ApiModelProperty(value = "封面图地址,不带前缀,单个")
    private String coverUrl;

    @ApiModelProperty(value = "图片地址,不带前缀,多个时英文逗号分隔")
    private String pictureUrl;

    @ApiModelProperty(value = "视频地址,不带前缀,单个")
    private String videoUrl;

    @ApiModelProperty(value = "VR地址,完整带前缀,单个")
    private String vrUrl;

    @ApiModelProperty(value = "AR地址,完整带前缀,单个")
    private String arUrl;

    @ApiModelProperty(value = "起始时间")
    private Date fromTime;

    @ApiModelProperty(value = "截止时间")
    private Date toTime;

    @ApiModelProperty(value = "排序得分，数字越大越靠前")
    private Integer sortingOrder;

    @ApiModelProperty(value = "#创建时间" , hidden = true)
    private Date createTime;

    @ApiModelProperty(value = "#禁用与否:0未禁,1已禁" , hidden = true)
    private Integer deleted;


}
