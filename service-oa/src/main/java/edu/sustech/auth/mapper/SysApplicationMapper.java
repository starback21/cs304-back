package edu.sustech.auth.mapper;

import edu.sustech.model.system.SysApplication;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.sustech.model.system.SysGroup;

import java.util.List;

/**
 * <p>
 * 申请 Mapper 接口
 * </p>
 *
 * @author starback
 * @since 2023-04-09
 */
public interface SysApplicationMapper extends BaseMapper<SysApplication>  {
    List<SysApplication> selectAll();
}
