package edu.sustech.auth.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.sustech.auth.mapper.SysGroupMapper;
import edu.sustech.auth.service.SysGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.sustech.auth.service.SysUserRoleService;
import edu.sustech.model.system.SysGroup;
import edu.sustech.model.system.SysUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课题组 服务实现类
 * </p>
 *
 * @author starback
 * @since 2023-04-04
 */
@Service
public class SysGroupServiceImpl extends ServiceImpl<SysGroupMapper, SysGroup> implements SysGroupService {

    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Override
    public boolean addGroupUsers(Long groupId, Long userId) {
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(userId);
        sysUserRole.setGroupId(groupId);
        sysUserRole.setRoleId(3L);
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("group_id",groupId).eq("user_id",userId);
        return sysUserRoleService.saveOrUpdate(sysUserRole,wrapper);
    }

    @Override
    public boolean deleteGroupUser(Long groupId, Long userId) {
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("group_id",groupId).eq("user_id",userId);
        return sysUserRoleService.remove(wrapper);
    }

    @Override
    public boolean getUsersNotInGroup(Long groupId) {

        return false;
    }

}
