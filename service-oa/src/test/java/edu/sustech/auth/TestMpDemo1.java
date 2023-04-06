package edu.sustech.auth;

import edu.sustech.auth.mapper.SysRoleMapper;
import edu.sustech.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestMpDemo1 {

    @Autowired
    private SysRoleMapper mapper;

    @Test
    public void getAll() {
        SysRole role = mapper.selectById(3);
        List<SysRole> list = mapper.selectList(null);
        System.out.println(list);
    }
}
