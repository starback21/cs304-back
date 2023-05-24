package edu.sustech.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.sustech.model.system.SysGroupFund;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysGroupFundMapper extends BaseMapper<SysGroupFund>{
    List<Map<String,Object>> selectGroupFund();
}
