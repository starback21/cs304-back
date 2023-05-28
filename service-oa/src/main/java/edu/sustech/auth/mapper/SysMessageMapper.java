package edu.sustech.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.sustech.model.system.SysMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface SysMessageMapper extends BaseMapper<SysMessage> {
    List<Map<Object,Object>> selectDate(@Param("userId") Long id);
}

