package edu.sustech.auth.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.sustech.auth.mapper.SysGroupFundMapper;
import edu.sustech.auth.service.SysGroupFundService;
import edu.sustech.model.system.SysGroupFund;
import org.springframework.stereotype.Service;

@Service

public class SysGroupFundServiceImpl extends ServiceImpl<SysGroupFundMapper, SysGroupFund> implements SysGroupFundService {
}
