package edu.sustech.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.model.system.SysGroupFund;
import edu.sustech.model.system.SysGroupFundDetail;

import java.util.List;
import java.util.Map;

public interface SysGroupFundService extends IService<SysGroupFund> {
    SysGroupFund getByGroupId(Long groupId);

    List<Map<String,Object>> getGroupFundByUser();

    List<SysGroupFundDetail> getGroupFundByGId(Long groupId);
}
