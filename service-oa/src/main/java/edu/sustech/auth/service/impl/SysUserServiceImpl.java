package edu.sustech.auth.service.impl;

import edu.sustech.model.system.SysUser;
import edu.sustech.auth.mapper.SysUserMapper;
import edu.sustech.auth.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
