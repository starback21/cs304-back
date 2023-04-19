package edu.sustech.auth;

import edu.sustech.auth.controller.SysGroupController;
import edu.sustech.auth.mapper.SysRoleMapper;
import edu.sustech.auth.service.SysApplicationService;
import edu.sustech.auth.service.SysFundingService;
import edu.sustech.auth.service.SysGroupService;
import edu.sustech.auth.service.SysUserService;
import edu.sustech.model.system.SysApplication;
import edu.sustech.model.system.SysFunding;
import edu.sustech.model.system.SysRole;
import edu.sustech.model.system.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Test1 {

    @Autowired
    private SysApplicationService applicationService;
    @Autowired
    private SysFundingService fundingService;
    @Autowired
    private SysUserService userService;
    @Test
    public void getAll() {
        List<SysApplication> list = applicationService.selectAll();
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
    @Test
    public void addUsers(){
        SysUser user = new SysUser();
        long base = 100000L;
        for (int i = 0; i < 10; i++) {
            user.setName("test_"+i);
            user.setUid(base+i);
            user.setPassword("123456");
            user.setPhone("13888777666");
            user.setEmail("163@qq.com");
            userService.save(user);
        }
    }
}
