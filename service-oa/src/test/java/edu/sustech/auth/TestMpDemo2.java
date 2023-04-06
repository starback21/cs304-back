package edu.sustech.auth;

import edu.sustech.auth.mapper.SysRoleMapper;
import edu.sustech.auth.service.SysRoleService;
import edu.sustech.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestMpDemo2 {

    @Autowired
    private SysRoleService service;

    @Test
    public void getAll() {
        List<SysRole> list = service.list();
        System.out.println(list);
    }
}
