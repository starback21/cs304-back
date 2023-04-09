package edu.sustech.auth.service;

import edu.sustech.model.system.SysGroup;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课题组 服务类
 * </p>
 *
 * @author starback
 * @since 2023-04-04
 */
public interface SysGroupService extends IService<SysGroup> {
    boolean addGroupUsers(Long groupId, Long userId);

    boolean deleteGroupUser(Long groupId, Long userId);

    boolean getUsersNotInGroup(Long groupId);
}
