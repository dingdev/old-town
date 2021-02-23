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
 * 通用场所
 * </p>
 *
 * @author dyp
 * @since 2020-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(autoResultMap = true)
@ApiModel(value="ComPlace对象", description="通用场所")
public class ComPlace implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键,新建时不传")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "别称")
    private String nickname;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "编号")
    private String code;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "图片地址,不含前缀,多个时英文逗号分隔")
    private String pictureUrl;

    @ApiModelProperty(value = "标签")
    private String label;

    @ApiModelProperty(value = "简介")
    private String intro;

    @ApiModelProperty(value = "价格")
    private String price;

    @ApiModelProperty(value = "购买地址,全址含前缀,直接访问")
    private String buyUrl;

    @ApiModelProperty(value = "是否自营,0:非自营,1:自营")
    private Integer ifSelf;

    @ApiModelProperty(value = "音频地址,不含前缀,暂定单个")
    private String audioUrl;

    @ApiModelProperty(value = "VR地址,全址含前缀,直接访问")
    private String vrUrl;

    @ApiModelProperty(value = "等级")
    private String level;

    @ApiModelProperty(value = "#浏览量" , hidden = true)
    private Integer viewNum;

    @ApiModelProperty(value = "#是否收藏了,0:未收藏,正整数:已收藏,值为收藏表里id" , hidden = true)
    private Long ifFavorite;

    @ApiModelProperty(value = "联系人")
    private String contact;

    @ApiModelProperty(value = "联系方式")
    private String tel;

    @ApiModelProperty(value = "总容量")
    private Integer capacityNum;

    @ApiModelProperty(value = "当前量")
    private Integer currentNum;

    @ApiModelProperty(value = "经度")
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    private Double latitude;

    @ApiModelProperty(value = "@范围面地理信息,形如MULTIPOLYGON((经度 纬度),()...))")
    @TableField(typeHandler = GeometryTypeHandler.class)
    private String polygon;

    @ApiModelProperty(value = "建议时间")
    private String suggestTime;

    @ApiModelProperty(value = "三维模型id")
    private Integer modelId;

    @ApiModelProperty(value = "排序得分，数字越大越靠前")
    private Integer sortingOrder;

    @ApiModelProperty(value = "禁用与否:0未禁,1已禁")
    private Integer deleted;

    @ApiModelProperty(value = "#创建时间" , hidden = true)
    private Date createTime;

    @ApiModelProperty(value = "#更新时间" , hidden = true)
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remarks;


}
