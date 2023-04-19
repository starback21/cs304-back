package edu.sustech.model.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import edu.sustech.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户角色")
@TableName("sys_user_role")
public class SysUserRole extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "组id")
	@TableField("group_id")
	private Long groupId;

	@ApiModelProperty(value = "课题组名称")
	@TableField("group_name")
	private String groupName;

	@ApiModelProperty(value = "角色id")
	@TableField("role_id")
	private int roleId;

	@ApiModelProperty(value = "用户id")
	@TableField("user_id")
	private Long userId;

	@ApiModelProperty(value = "用户姓名")
	@TableField("user_name")
	private String userName;

}

