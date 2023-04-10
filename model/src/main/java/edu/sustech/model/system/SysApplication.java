package edu.sustech.model.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import edu.sustech.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 申请
 * </p>
 *
 * @author starback
 * @since 2023-04-09
 */
@Data
@ApiModel(description = "申请")
@TableName("sys_application")
public class SysApplication extends BaseEntity {

    private static final long serialVersionUID = 1L;



    @ApiModelProperty(value = "标题")
    @TableField("title")
    private String title;

    @ApiModelProperty(value = "组")
    @TableField("group")
    private Long group;

    @ApiModelProperty(value = "类1")
    @TableField("category1")
    private String category1;

    @ApiModelProperty(value = "类2")
    @TableField("category2")
    private String category2;

    @ApiModelProperty(value = "数量")
    @TableField("number")
    private Integer number;

    @ApiModelProperty(value = "状态")
    @TableField("state")
    private String state;

    @ApiModelProperty(value = "处理人")
    @TableField("people")
    private String people;

    @ApiModelProperty(value = "评价")
    @TableField("comment")
    private String comment;



}
