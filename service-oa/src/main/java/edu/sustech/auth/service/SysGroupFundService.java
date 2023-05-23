package edu.sustech.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.model.system.SysGroupFund;
public interface SysGroupFundService extends IService<SysGroupFund> {
    SysGroupFund getByGroupId(Long groupId);
}
