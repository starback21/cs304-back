package edu.sustech.auth.mapper;

import edu.sustech.model.system.SysGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.sustech.model.system.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课题组 Mapper 接口
 * </p>
 *
 * @author starback
 * @since 2023-04-04
 */
public interface SysGroupMapper extends BaseMapper<SysGroup> {
    List<SysUser> selectUserNotInGroup(@Param("groupId") Long id);
}
