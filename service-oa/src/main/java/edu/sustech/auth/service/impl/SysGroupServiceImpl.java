package edu.sustech.auth.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.sustech.auth.mapper.SysGroupMapper;
import edu.sustech.auth.service.SysGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.sustech.auth.service.SysUserRoleService;
import edu.sustech.auth.service.SysUserService;
import edu.sustech.model.system.SysGroup;
import edu.sustech.model.system.SysUser;
import edu.sustech.model.system.SysUserRole;
import edu.sustech.re.system.PageUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private SysUserService userService;

    @Override
    public boolean createAndAddUser(Long groupId,String groupName, List<String> userNames) {
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setGroupId(groupId);
        sysUserRole.setGroupName(groupName);

        sysUserRole.setRoleId(3);
        for (String n : userNames){
            Long id = userService.selectIdByName(n);
            sysUserRole.setUserId(id);
            sysUserRole.setUserName(n);
            sysUserRoleService.save(sysUserRole);
        }

        return true;
    }

    @Override
    public boolean addGroupUsers(String name, List<String> userList, List<String> adminList) {
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setGroupName(name);
        Long groupId = baseMapper.selectGroupIdByName(name);
        sysUserRole.setGroupId(groupId);
        for (String s : userList){
            boolean isAdmin = false;
            Long id = userService.selectIdByName(s);
            sysUserRole.setUserId(id);
            sysUserRole.setUserName(s);
            sysUserRole.setRoleId(3);
            if (adminList.size() > 0) {
                for (String admin : adminList) {
                    if (admin.equals(s)) {
                        sysUserRole.setRoleId(2);
                        isAdmin = true;

                    }
                }
            }
            if (isAdmin) adminList.remove(s);
            sysUserRoleService.save(sysUserRole);
        }

        return true;
    }

    @Override
    public boolean deleteGroupUser(Long groupId, Long userId) {
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("group_id",groupId).eq("user_id",userId);
        return sysUserRoleService.remove(wrapper);
    }

    @Override
    public List<PageUser> getUsersNotInGroup(Long groupId) {
        List<SysUser> userList = baseMapper.selectUserNotInGroup(groupId);
        List<PageUser> list = new ArrayList<>();
        for (SysUser user : userList){
            PageUser tempUser = new PageUser();
            tempUser.setId(user.getId());
            tempUser.setKey(user.getUid());
            tempUser.setName(user.getName());
            tempUser.setEmail(user.getEmail());
            tempUser.setPhone(user.getPhone());
            list.add(tempUser);
        }
        return list;
    }

    @Override
    public Long getIdByName(String name) {
        return baseMapper.selectGroupIdByName(name);
    }

}
