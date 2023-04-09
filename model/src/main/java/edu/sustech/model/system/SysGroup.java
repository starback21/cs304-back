package edu.sustech.model.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;


import com.baomidou.mybatisplus.annotation.TableName;
import edu.sustech.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 课题组
 * </p>
 *
 * @author starback
 * @since 2023-04-04
 */
@Data
@ApiModel(description = "课题组")
@TableName("sys_group")
public class SysGroup extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "组名")
    @TableField("group_name")
    private String groupName;

    @ApiModelProperty(value = "课题组编码")
    @TableField("group_code")
    private String groupCode;

    @ApiModelProperty(value = "描述")
    @TableField("description")
    private String description;

}
