package edu.sustech.auth.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.sustech.auth.mapper.SysFundingMapper;
import edu.sustech.auth.service.SysFundingService;
import edu.sustech.model.system.SysFunding;
import org.springframework.stereotype.Service;

@Service
public class SysFundingServiceImpl extends ServiceImpl<SysFundingMapper, SysFunding> implements SysFundingService {

        @Override
        public SysFunding getByFundingId(Long fundingId) {
            return this.getOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysFunding>().eq(SysFunding::getFundingId,fundingId));
        }

}

