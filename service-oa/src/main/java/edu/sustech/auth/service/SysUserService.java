package edu.sustech.auth.service;

import edu.sustech.model.system.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

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

    Long selectIdByName(String name);

    SysUser getUserByName(String name);
}
