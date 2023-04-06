package edu.sustech.model.system;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import edu.sustech.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel(description = "经费表")
@TableName("sys_funding")
public class SysFunding extends BaseEntity {
    @ApiModelProperty(value = "经费id")
    @TableField("id")
    private Integer fundingId;
    @ApiModelProperty(value = "课题组id")
    @TableField("group_id")
    private Integer groupId;
    @ApiModelProperty(value = "总金额")
    @TableField("total_amount")
    private Long totalAmount;
    @ApiModelProperty(value = "剩余金额")
    @TableField("remain_amount ")
    private Long remainAmount;
      
}
