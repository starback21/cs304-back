package edu.sustech.auth.service;

import edu.sustech.model.system.SysApplication;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 申请 服务类
 * </p>
 *
 * @author starback
 * @since 2023-04-09
 */
public interface SysApplicationService extends IService<SysApplication> {
    public List<SysApplication> selectAll();
}
