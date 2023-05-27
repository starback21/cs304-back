package edu.sustech.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.model.system.SysGroupFund;
import edu.sustech.model.system.SysGroupFundDetail;
import edu.sustech.re.user.UserFund;

import java.util.List;
import java.util.Map;

public interface SysGroupFundService extends IService<SysGroupFund> {
    SysGroupFund getByGroupId(Long groupId);

    List<Map<String,Object>> getGroupFundByUser();

    List<UserFund> getGroupFundByGId(Long groupId);
    List<SysGroupFund> getGroupFundByFundId(Long fundId);
    List<SysGroupFund>getGroupFundByGroupId(Long groupId);

    SysGroupFund getByGroupandFund(Long groupId,Long fundId);
}
