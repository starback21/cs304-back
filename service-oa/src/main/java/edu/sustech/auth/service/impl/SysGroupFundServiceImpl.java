package edu.sustech.auth.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.sustech.auth.mapper.SysGroupFundMapper;
import edu.sustech.auth.service.SysGroupFundDetailService;
import edu.sustech.auth.service.SysGroupFundService;
import edu.sustech.model.system.SysGroupFund;
import edu.sustech.model.system.SysGroupFundDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service

public class SysGroupFundServiceImpl extends ServiceImpl<SysGroupFundMapper, SysGroupFund> implements SysGroupFundService {

    @Autowired
    private SysGroupFundDetailService detailService;
    @Override
    public SysGroupFund getByGroupId(Long groupId) {
        return this.getOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysGroupFund>().eq(SysGroupFund::getGroupId,groupId));
    }

    @Override
    public List<Map<String, Object>> getGroupFundByUser() {
        return baseMapper.selectGroupFund();
    }

    @Override
    public List<SysGroupFundDetail> getGroupFundByGId(Long groupId) {
        LambdaQueryWrapper<SysGroupFundDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysGroupFundDetail::getGroupId,groupId);
        wrapper.select(SysGroupFundDetail::getCategory1,
                SysGroupFundDetail::getCategory2,
                SysGroupFundDetail::getTotalAmount);
        return detailService.list(wrapper);
    }
}
