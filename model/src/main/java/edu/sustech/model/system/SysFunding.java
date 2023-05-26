package edu.sustech.model.system;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import edu.sustech.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Date;

@Data
@ApiModel(description = "经费表")
@TableName("sys_funding")
public class SysFunding extends BaseEntity {

//    @ApiModelProperty(value = "经费id")
//    @TableField("funding_id")
//    private Long fundingId;
    @ApiModelProperty(value = "经费名称")
    @TableField("funding_name")
    private String fundingName;
    @ApiModelProperty(value = "总金额")
    @TableField("total_amount")
    private Long totalAmount;
    @ApiModelProperty(value = "花费金额")
    @TableField("cost")
    private Long cost;
    @ApiModelProperty(value = "剩余金额")
    @TableField("remain_amount ")
    private Long remainAmount;
    @ApiModelProperty(value = "经费状态")
    @TableField("status")
    private String status;
    @ApiModelProperty(value = "开始时间")
    @TableField("start_time")
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    @TableField("end_time")
    private Date endTime;

}
