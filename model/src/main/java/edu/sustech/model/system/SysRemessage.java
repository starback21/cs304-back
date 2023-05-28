package edu.sustech.model.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import edu.sustech.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "消息")
@TableName("sys_remessage")
public class SysRemessage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息内容")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "type")
    @TableField("type")
    private String type;

    @ApiModelProperty(value = "状态")
    @TableField("state")
    private int state;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "课题组id")
    @TableField("group_id")
    private Long groupId;

    @ApiModelProperty(value = "申请id")
    @TableField("app_id")
    private Long appId;

}