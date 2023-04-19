package edu.sustech.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.sustech.model.system.SysUser;
import edu.sustech.auth.mapper.SysUserMapper;
import edu.sustech.auth.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Long selectIdByName(String name) {
        return baseMapper.selectIdByName(name);
    }
}
