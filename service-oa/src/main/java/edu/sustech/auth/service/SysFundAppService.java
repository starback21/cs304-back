package edu.sustech.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.model.system.SysFundApp;
import edu.sustech.model.system.SysUser;

public interface SysFundAppService extends IService<SysFundApp>{
    SysFundApp getByAppId(Long appId);
}
