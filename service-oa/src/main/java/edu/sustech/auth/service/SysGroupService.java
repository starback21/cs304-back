package edu.sustech.auth.service;

import edu.sustech.model.system.SysFunding;
import edu.sustech.model.system.SysGroup;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.re.system.PageUser;

import java.util.List;

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

    boolean deleteGroupUser(String groupName, Long userId);

    List<PageUser> getUsersNotInGroup(Long groupId);

    Long getIdByName(String name);
}
