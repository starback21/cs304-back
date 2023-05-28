package edu.sustech.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.model.system.SysMessage;

import java.util.List;
import java.util.Map;

public interface SysMessageService extends IService<SysMessage> {
    public List<Map<Object,Object>> getDate(Long userId);
}
