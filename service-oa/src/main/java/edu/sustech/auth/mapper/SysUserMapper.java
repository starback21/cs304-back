package edu.sustech.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.sustech.model.system.SysUser;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author starback
 * @since 2023-03-29
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    SysUser selectGroupIdByName(@Param("userId") Long id);

    int selectUid(@Param("uid")Long uid);

    Long selectIdByName(@Param("name")String name);
}
