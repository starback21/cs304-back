package edu.sustech.auth.service.impl;

import edu.sustech.model.system.SysApplication;
import edu.sustech.auth.mapper.SysApplicationMapper;
import edu.sustech.auth.service.SysApplicationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 申请 服务实现类
 * </p>
 *
 * @author starback
 * @since 2023-04-09
 */
@Service
public class SysApplicationServiceImpl extends ServiceImpl<SysApplicationMapper, SysApplication> implements SysApplicationService {

    @Override
    public List<SysApplication> selectAll() {
        return baseMapper.selectAll();
    }
}
