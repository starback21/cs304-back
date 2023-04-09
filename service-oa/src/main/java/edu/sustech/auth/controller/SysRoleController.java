package edu.sustech.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.sustech.auth.service.SysRoleService;
import edu.sustech.common.result.Result;
import edu.sustech.model.system.SysRole;
import edu.sustech.model.system.SysUserRole;
import edu.sustech.vo.system.AssginRoleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "角色管理接口")
@RestController
@CrossOrigin
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    //注入service
    @Autowired
    private SysRoleService sysRoleService;

    @ApiOperation(value = "根据用户获取角色数据")
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId) {
        Map<Long, Long> roleMap = sysRoleService.findRoleByAdminId(userId);
        return Result.ok(roleMap);
    }

    @ApiOperation(value = "更改用户权限")
    @PostMapping("/changeAdmin")
    public Result changeAdmin(@RequestParam(value = "group_id") Long groupId,
                           @RequestParam(value = "user_id") Long userId) {
        if (sysRoleService.doAssign(groupId,userId))
            return Result.ok();
        else
            return Result.fail();
    }

    @ApiOperation("查询所有角色")
    @GetMapping("/findAll")
    public Result<List<SysRole>> findAll() {
        List<SysRole> roleList = sysRoleService.list();
        return Result.ok(roleList);
    }

    @ApiOperation(value = "根据id查询")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        SysRole role = sysRoleService.getById(id);
        if (role == null){
            return  Result.fail();
        }
        return Result.ok(role);
    }

    /**
     * 添加角色
     * @param sysRole
     * @return
     */
    @ApiOperation("添加角色")
    @PostMapping("/create")
    public Result save(@RequestBody SysRole sysRole) {
        // 调用 service 方法
        boolean is_success = sysRoleService.save(sysRole);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "修改角色")
    @PutMapping("/update")
    public Result updateById(@RequestBody SysRole role) {
        boolean is_success = sysRoleService.updateById(role);
        if (is_success){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "删除角色")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        boolean is_success = sysRoleService.removeById(id);
        if (is_success){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "根据id列表删除")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        boolean is_success = sysRoleService.removeByIds(idList);
        if (is_success){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

}
