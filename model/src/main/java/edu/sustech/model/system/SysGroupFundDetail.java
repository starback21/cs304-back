package edu.sustech.model.system;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import edu.sustech.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel(description = "组经费表详情")
@TableName("sys_group_funding_detail")
public class SysGroupFundDetail extends BaseEntity{
    @ApiModelProperty(value = "经费id")
    @TableField("funding_id")
    private Long fundingId;
    @ApiModelProperty(value = "课题组id")
    @TableField("group_id")
    private Long groupId;
    @ApiModelProperty(value = "总金额")
    @TableField("total_amount")
    private Long totalAmount;
    @ApiModelProperty(value = "已使用金额")
    @TableField("used_amount")
    private Long usedAmount;
    @ApiModelProperty(value = "经费描述1级")
    @TableField("category1")
    private String category1;
    @ApiModelProperty(value = "经费描述2级")
    @TableField("category2")
    private String category2;
}
