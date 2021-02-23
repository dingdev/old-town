package com.example.oldtown.modules.com.model;

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
 * 通用游线
 * </p>
 *
 * @author dyp
 * @since 2021-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(autoResultMap = true)
@ApiModel(value="ComRoute对象", description="通用游线")
public class ComRoute implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键,新建时不传")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "游线名称")
    private String name;

    @ApiModelProperty(value = "包含的通用场所id,英文逗号分隔")
    private String contentIds;

    @ApiModelProperty(value = "简介")
    private String intro;

    @ApiModelProperty(value = "长度,单位米")
    private Double length;

    @ApiModelProperty(value = "图片地址,不含前缀,多个时英文逗号分隔")
    private String pictureUrl;

    @ApiModelProperty(value = "@线段地理信息,形如LINESTRING(经度 纬度,...)")
    @TableField(typeHandler = GeometryTypeHandler.class)
    private String lineString;

    @ApiModelProperty(value = "标签")
    private String label;

    @ApiModelProperty(value = "建议游玩天数")
    private Integer dayNum;

    @ApiModelProperty(value = "图文详情地址,含前缀,单个")
    private String detailsUrl;

    @ApiModelProperty(value = "是否包含酒店:0不包含,1包含")
    private Integer ifHotel;

    @ApiModelProperty(value = "是否经典展示:0不展示,1展示")
    private Integer ifShow;

    @ApiModelProperty(value = "颜色,16进制表示")
    private String color;

    @ApiModelProperty(value = "#是否收藏了,0:未收藏,正整数:已收藏,值为收藏表里id" , hidden = true)
    private Long ifFavorite;

    @ApiModelProperty(value = "禁用与否:0未禁,1已禁")
    private Integer deleted;

    @ApiModelProperty(value = "#创建时间" , hidden = true)
    private Date createTime;

    @ApiModelProperty(value = "#更新时间" , hidden = true)
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remarks;


}
