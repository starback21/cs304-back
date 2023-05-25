package edu.sustech.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.model.system.SysGroupFundDetail;
public interface SysGroupFundDetailService extends IService<SysGroupFundDetail>{
    SysGroupFundDetail getByGroupCategory(String category1, Long fundId, Long groupId);
}
