package edu.sustech.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.sustech.model.system.SysRole;
import edu.sustech.vo.system.AssginRoleVo;

import java.util.Map;

public interface SysRoleService extends IService<SysRole> {

    Map<Long, Long> findRoleByAdminId(Long userId);

    boolean doAssign(Long groupId,Long userId);

    boolean doAssignByName(String name,Long userId);
}
