package edu.sustech.auth.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.sustech.auth.mapper.SysRoleMapper;
import edu.sustech.auth.service.SysGroupService;
import edu.sustech.auth.service.SysRoleService;
import edu.sustech.auth.service.SysUserRoleService;
import edu.sustech.auth.service.SysUserService;
import edu.sustech.model.system.SysGroup;
import edu.sustech.model.system.SysRole;
import edu.sustech.model.system.SysUser;
import edu.sustech.model.system.SysUserRole;
import edu.sustech.vo.system.AssginRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysGroupService groupService;
    @Override
    public Map<Long, Long> findRoleByAdminId(Long userId) {
        //根据userid查询 user_role 表，查询对应角色所在组与对应角色
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId,userId);
        List<SysUserRole> existUserRoleList = sysUserRoleService.list(wrapper);
        Map<Long,Long> groupRole = new HashMap<>();
        for (SysUserRole sysUserRole : existUserRoleList) {
            Long gid = sysUserRole.getGroupId();
            Long rid = sysUserRole.getRoleId();
            groupRole.put(gid,rid);
        }
        return groupRole;
    }

    @Override
    public boolean doAssign(Long groupId,Long userId) {
        int count1 = groupService.count(new QueryWrapper<SysGroup>()
                .eq("id",groupId)
        );
        int count2 = sysUserService.count(new QueryWrapper<SysUser>()
                .eq("id",userId)
        );
        if (count1 == 0 || count2 == 0){
            return false;
        }
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("group_id",groupId).eq("user_id",userId);

        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setGroupId(groupId);
        sysUserRole.setUserId(userId);
        sysUserRole.setRoleId(2L);
        return sysUserRoleService.saveOrUpdate(sysUserRole,wrapper);
    }


}
