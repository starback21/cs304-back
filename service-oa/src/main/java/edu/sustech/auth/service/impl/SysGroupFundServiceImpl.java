package edu.sustech.auth.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.sustech.auth.mapper.SysGroupFundMapper;
import edu.sustech.auth.service.SysFundingService;
import edu.sustech.auth.service.SysGroupFundDetailService;
import edu.sustech.auth.service.SysGroupFundService;
import edu.sustech.model.system.SysGroupFund;
import edu.sustech.model.system.SysGroupFundDetail;
import edu.sustech.re.user.UserFund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service

public class SysGroupFundServiceImpl extends ServiceImpl<SysGroupFundMapper, SysGroupFund> implements SysGroupFundService {

    @Autowired
    private SysGroupFundDetailService detailService;
    @Autowired
    private SysFundingService fundingService;
    @Override
    public SysGroupFund getByGroupId(Long groupId) {
        return this.getOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysGroupFund>().eq(SysGroupFund::getGroupId,groupId));
    }

    @Override
    public List<Map<String, Object>> getGroupFundByUser() {
        return baseMapper.selectGroupFund();
    }

    @Override
    public List<UserFund> getGroupFundByGId(Long groupId) {
        LambdaQueryWrapper<SysGroupFundDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysGroupFundDetail::getGroupId,groupId);
        wrapper.select(
                SysGroupFundDetail::getRemainAmount,
                SysGroupFundDetail::getFundingId,
                SysGroupFundDetail::getCategory1
              );
        List<SysGroupFundDetail> list = detailService.list(wrapper);
        List<UserFund> result = new ArrayList<>();
        for (SysGroupFundDetail detail : list){
            UserFund userFund = new UserFund();
            userFund.setId(detail.getFundingId());
            userFund.setCategory(detail.getCategory1());
            userFund.setRemain_amount(Math.toIntExact(detail.getRemainAmount()));
            userFund.setName(fundingService.getById(detail.getFundingId()).getFundingName());
            result.add(userFund);
        }
        return result;
    }

    @Override
    public List<SysGroupFund> getGroupFundByFundId(Long fundId) {
        LambdaQueryWrapper<SysGroupFund> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysGroupFund::getFundingId,fundId);
        return this.list(wrapper);
    }


}
