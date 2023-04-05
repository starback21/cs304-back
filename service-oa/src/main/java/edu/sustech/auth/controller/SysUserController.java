package edu.sustech.auth.controller;


import edu.sustech.auth.service.SysUserService;
import edu.sustech.common.result.Result;
import edu.sustech.model.system.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author starback
 * @since 2023-03-29
 */
@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService service;

    @ApiOperation(value = "根据id获取用户")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        SysUser user = service.getById(id);
        if (user == null){
            return Result.fail();
        }
        return Result.ok(user);
    }

    @ApiOperation(value = "添加用户")
    @PostMapping("/createAccount")
    public Result save(@RequestBody SysUser user) {
        service.save(user);
        return Result.ok();
    }

    @ApiOperation(value = "更新用户")
    @PutMapping("/update")
    public Result updateById(@RequestBody SysUser user) {
        service.updateById(user);
        return Result.ok();
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        service.removeById(id);
        return Result.ok();
    }
}

