package edu.sustech.auth.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.sustech.auth.mapper.SysGroupFundDetailMapper;
import edu.sustech.auth.service.SysGroupFundDetailService;
import edu.sustech.model.system.SysGroupFundDetail;
import org.springframework.stereotype.Service;

@Service

public class SysGroupFundDetailImpl extends ServiceImpl<SysGroupFundDetailMapper, SysGroupFundDetail> implements SysGroupFundDetailService {

        @Override
        public SysGroupFundDetail getByGroupCategory(String category1, String category2, Long fundId, Long groupId) {
            return this.getOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysGroupFundDetail>().eq(SysGroupFundDetail::getCategory1,category1).eq(SysGroupFundDetail::getCategory2,category2).eq(SysGroupFundDetail::getFundingId,fundId).eq(SysGroupFundDetail::getGroupId,groupId));
        }

}
