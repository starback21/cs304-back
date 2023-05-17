package edu.sustech.model.system;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import edu.sustech.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel(description = "组经费表")
@TableName("sys_group_funding")
public class SysGroupFund extends BaseEntity{
    @ApiModelProperty(value = "经费id")
    @TableField("funding_id")
    private Long fundingId;
    @ApiModelProperty(value = "经费名称")
    @TableField("funding_name")
    private String fundingName;
    @ApiModelProperty(value = "课题组id")
    @TableField("group_id")
    private Long groupId;
    @ApiModelProperty(value = "课题组名称")
    @TableField("group_name")
    private String groupName;
    @ApiModelProperty(value = "总金额")
    @TableField("total_amount")
    private Long totalAmount;
    @ApiModelProperty(value = "花费")
    @TableField("cost")
    private Long cost;
    @ApiModelProperty(value = "剩余")
    @TableField("remain_amount ")
    private Long remainAmount;
    @ApiModelProperty(value="达标")
    @TableField("status")
    private String status;
}
