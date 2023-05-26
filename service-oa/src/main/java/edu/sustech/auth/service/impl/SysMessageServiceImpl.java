package edu.sustech.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.sustech.auth.mapper.SysMessageMapper;
import edu.sustech.auth.service.SysMessageService;
import edu.sustech.model.system.SysMessage;
import org.springframework.stereotype.Service;


@Service
public class SysMessageServiceImpl
        extends ServiceImpl<SysMessageMapper, SysMessage>
        implements SysMessageService {
}
