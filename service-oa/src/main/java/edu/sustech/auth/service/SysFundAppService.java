package edu.sustech.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.auth.mapper.SysFundAppMapper;
import edu.sustech.model.system.SysFundApp;
import edu.sustech.model.system.SysGroupFund;
import edu.sustech.model.system.SysUser;

import java.util.List;

public interface SysFundAppService extends IService<SysFundApp>{
    SysFundApp getByAppId(Long appId);
    List<SysFundApp>getByFundName(String fundName);
    List<SysFundApp>getByFundId(Long fundId);
}
