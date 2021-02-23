package com.example.oldtown.modules.xcx.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import com.example.oldtown.handler.GeometryTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 小程序用户反馈
 * </p>
 *
 * @author dyp
 * @since 2020-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(autoResultMap = true)
@ApiModel(value="XcxFeedback对象", description="小程序用户反馈")
public class XcxFeedback implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键,新建时不传")
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "所属分类")
    private String type;

    @ApiModelProperty(value = "反馈内容")
    private String content;

    @ApiModelProperty(value = "#数据创建时间" , hidden = true)
    private Date createTime;

    @ApiModelProperty(value = "当前状态,0:未处理,1:处理中,2:已处理;3:已忽略")
    private Integer status;


}
