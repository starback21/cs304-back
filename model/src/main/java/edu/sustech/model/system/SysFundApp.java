package edu.sustech.model.system;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import edu.sustech.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(description = "经费申请")
@TableName("sys_fund_app")
public class SysFundApp extends BaseEntity {
    @ApiModelProperty(value = "经费id")
    @TableField("fund_id")
    private Long fundId;

    @ApiModelProperty(value = "经费名称")
    @TableField("fund_name")
    private String fundName;

    @ApiModelProperty(value = "申请id")
    @TableField("app_id")
    private int appId;

    @ApiModelProperty(value = "申请名")
    @TableField("app_name")
    private String appName;

}
