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
 * 通用活动演出
 * </p>
 *
 * @author dyp
 * @since 2020-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(autoResultMap = true)
@ApiModel(value="ComActivity对象", description="通用活动演出")
public class ComActivity implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键,新建时不传")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "简介")
    private String intro;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "演出点在通用设施表的id")
    private Long poiId;

    @ApiModelProperty(value = "联系方式")
    private String tel;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "内部图片地址,不含前缀,多个时英文逗号分隔")
    private String pictureUrl;

    @ApiModelProperty(value = "封面图片,单张,不含前缀,可直接使用内部图片地址中的首个")
    private String coverUrl;

    @ApiModelProperty(value = "排序得分，数字越大越靠前")
    private Integer sortingOrder;

    @ApiModelProperty(value = "状态,0:默认正常,1:取消")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "#创建时间" , hidden = true)
    private Date createTime;


}
