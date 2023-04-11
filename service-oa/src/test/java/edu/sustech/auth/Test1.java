package edu.sustech.auth;

import edu.sustech.auth.mapper.SysRoleMapper;
import edu.sustech.auth.service.SysApplicationService;
import edu.sustech.auth.service.SysFundingService;
import edu.sustech.model.system.SysApplication;
import edu.sustech.model.system.SysFunding;
import edu.sustech.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Test1 {

    @Autowired
    private SysApplicationService service;
    @Autowired
    private SysFundingService fundingService;
    @Test
    public void getAll() {
        List<SysApplication> list = service.selectAll();
        System.out.println(list);
    }

    @Test
    public void addFundings() {
        SysFunding funding = new SysFunding();
        long base = 510L;
        for (int i = 0; i < 5; i++) {
            funding.setFundingId(base++);
            funding.setFundingName("test"+base);
            funding.setCost(100L);
            funding.setGroupId(15L);
            funding.setGroupName("电子");
            funding.setTotalAmount(10000L);
            funding.setRemainAmount(9900L);
            fundingService.save(funding);
        }


    }
}
