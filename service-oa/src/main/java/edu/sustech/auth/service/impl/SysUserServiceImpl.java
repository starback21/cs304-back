package edu.sustech.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.sustech.auth.service.SysGroupService;
import edu.sustech.auth.service.SysUserRoleService;
import edu.sustech.model.system.SysUser;
import edu.sustech.auth.mapper.SysUserMapper;
import edu.sustech.auth.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.sustech.model.system.SysUserRole;
import edu.sustech.re.system.PageGroup;
import edu.sustech.re.system.PageUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author starback
 * @since 2023-03-29
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    SysGroupService groupService;
    @Autowired
    SysUserRoleService roleService;
    @Override
    public SysUser getUserById(Long userId) {
        return baseMapper.selectGroupIdByName(userId);
    }

    @Override
    public Boolean selectUidSame(Long uid) {
        int re = baseMapper.selectUid(uid);
        return re != 0;
    }

    @Override
    public Boolean selectNameSame(String name) {
        int re = baseMapper.selectName(name);
        return re != 0;
    }

    @Override
    public Long selectIdByName(String name) {
        return baseMapper.selectIdByName(name);
    }

    @Override
    public SysUser getUserByName(String name) {
        return this.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getName, name));
    }

    @Override
    public void addUserToGroup(Long userId, List<String> groupList, List<String> adminList) {
        SysUser user = this.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUid, userId));
        SysUserRole role = new SysUserRole();
        role.setUserId(user.getId());
        role.setUserName(user.getName());

        for (String gName : groupList){
            boolean isAdmin = false;
            role.setGroupName(gName);
            Long gid = groupService.getIdByName(gName);
            role.setGroupId(gid);
            role.setRoleId(3);
            if (adminList.size() > 0) {
                for (String admin : adminList) {
                    if (admin.equals(gName)){
                        role.setRoleId(2);
                        isAdmin = true;
                    }
                }
            }
            if (isAdmin) adminList.remove(gName);
            roleService.save(role);
        }
    }

    @Override
    public void addUserToGroup(Long userId, List<String> groupList) {
        SysUser user = this.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUid, userId));
        SysUserRole role = new SysUserRole();
        role.setUserId(user.getId());
        role.setUserName(user.getName());
        role.setRoleId(3);
        for (String gName : groupList){
            role.setGroupName(gName);
            Long gid = groupService.getIdByName(gName);
            role.setGroupId(gid);
            roleService.save(role);
        }
    }

    @Override
    public List<PageGroup> getUserGroup(Long userId) {
        List<SysUserRole> list = roleService.list(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId,userId));
        List<PageGroup> result = new ArrayList<>();
        //课题组和用户，一个用户按id查询，查出一个列表，其中包括他的所有课题组
        for (SysUserRole role : list){
            PageGroup group = new PageGroup();
            group.setId(role.getGroupId());
            group.setName(role.getGroupName());
            List<PageUser> groupMember = groupService.getGroupMember(role.getGroupId());
            group.setUsers(groupMember);
            result.add(group);
        }
        return result;
    }
}
