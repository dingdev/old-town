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
 * 小程序用户收藏
 * </p>
 *
 * @author dyp
 * @since 2020-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(autoResultMap = true)
@ApiModel(value="XcxFavorite对象", description="小程序用户收藏")
public class XcxFavorite implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键,新建时不传")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "内容来源(前端跳转详情时使用,比如值为comPlace,则查询通用设施)")
    private String contentFrom;

    @ApiModelProperty(value = "内容id")
    private Long contentId;

    @ApiModelProperty(value = "内容名称")
    private String contentName;

    @ApiModelProperty(value = "封面图地址,不含前缀,单个")
    private String contentCover;

    @ApiModelProperty(value = "所属分类")
    private String type;

    @ApiModelProperty(value = "#数据创建时间" , hidden = true)
    private Date createTime;


}
