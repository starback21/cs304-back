package edu.sustech.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.sustech.auth.mapper.SysMessageMapper;
import edu.sustech.auth.mapper.SysRemessageMapper;
import edu.sustech.auth.service.SysMessageService;
import edu.sustech.auth.service.SysRemessageService;
import edu.sustech.model.system.SysMessage;
import edu.sustech.model.system.SysRemessage;
import org.springframework.stereotype.Service;


@Service
public class SysRemessageServiceImpl
        extends ServiceImpl<SysRemessageMapper, SysRemessage>
        implements SysRemessageService {
}
