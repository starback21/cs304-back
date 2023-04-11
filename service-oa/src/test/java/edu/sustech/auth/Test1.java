package edu.sustech.auth;

import edu.sustech.auth.mapper.SysRoleMapper;
import edu.sustech.auth.service.SysApplicationService;
import edu.sustech.model.system.SysApplication;
import edu.sustech.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Test1 {

    @Autowired
    private SysApplicationService service;

    @Test
    public void getAll() {
        List<SysApplication> list = service.selectAll();
        System.out.println(list);
    }
}
