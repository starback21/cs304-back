package edu.sustech.auth.service;

import edu.sustech.model.system.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author starback
 * @since 2023-03-29
 */
public interface SysUserService extends IService<SysUser> {
    SysUser getUserById(Long userId);

    Boolean selectUidSame(Long uid);
    Boolean selectNameSame(String name);
    Long selectIdByName(String name);

    SysUser getUserByName(String name);

    void addUserToGroup(Long userId, List<String> groupList, List<String> adminList);

    void addUserToGroup(Long userId, List<String> groupList);

    List<String> getUserGroup(Long userId);
}
