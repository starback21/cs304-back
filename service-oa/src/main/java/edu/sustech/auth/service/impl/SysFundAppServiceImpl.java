package edu.sustech.auth.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.auth.mapper.SysFundAppMapper;
import edu.sustech.auth.service.SysFundAppService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.sustech.model.system.SysFundApp;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysFundAppServiceImpl extends ServiceImpl<SysFundAppMapper, SysFundApp> implements SysFundAppService{

        @Override
        public SysFundApp getByAppId(Long appId) {
            return this.getOne(new LambdaQueryWrapper<SysFundApp>().eq(SysFundApp::getAppId,appId));
        }

    @Override
    public List<SysFundApp> getByFundName(String fundName) {
        return this.list(new LambdaQueryWrapper<SysFundApp>().eq(SysFundApp::getFundName,fundName));
    }

}
