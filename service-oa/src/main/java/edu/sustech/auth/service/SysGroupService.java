package edu.sustech.auth.service;

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


    boolean createAndAddUser(Long groupId,String groupName, List<String> userNames);

    boolean addGroupUsers(String name, List<String> userList, List<String> adminList);

    boolean deleteGroupUser(Long groupId, Long userId);

    List<PageUser> getUsersNotInGroup(Long groupId);

    Long getIdByName(String name);
}
