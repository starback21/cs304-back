package edu.sustech.vo.system;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@ApiModel(description = "分配菜单")
@Data
public class AssginRoleVo {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "组id+角色id")
    private Map<Long,Long> roleIdList;

}
