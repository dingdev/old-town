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
 * 通用问答
 * </p>
 *
 * @author dyp
 * @since 2020-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(autoResultMap = true)
@ApiModel(value="ComQa对象", description="通用问答")
public class ComQa implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键,新建时不传")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "所属分类")
    private String type;

    @ApiModelProperty(value = "问题")
    private String question;

    @ApiModelProperty(value = "解答")
    private String answer;

    @ApiModelProperty(value = "#数据创建时间" , hidden = true)
    private Date createTime;

    @ApiModelProperty(value = "禁用与否:0未禁,1已禁")
    private Integer deleted;


}
