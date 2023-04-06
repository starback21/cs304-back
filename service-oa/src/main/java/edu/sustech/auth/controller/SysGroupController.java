package edu.sustech.auth.controller;


import edu.sustech.auth.service.SysGroupService;

import edu.sustech.common.result.Result;
import edu.sustech.model.system.SysGroup;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课题组 前端控制器
 * </p>
 *
 * @author starback
 * @since 2023-04-04
 */
@Api(tags = "课题组管理接口")
@RestController
@CrossOrigin
@RequestMapping("/admin/system/sysGroup")
public class SysGroupController {

    //注入service
    @Autowired
    private SysGroupService sysGroupService;

    @ApiOperation("查询所有课题组")
    @GetMapping("/findAll")
    public Result<List<SysGroup>> findAll() {
        List<SysGroup> roleList = sysGroupService.list();
        return Result.ok(roleList);
    }
    @ApiOperation("查询所有课题组名字")
    @GetMapping("/getAllGroupName")
    public Result<List<String>> getAllGroupName() {
        List<SysGroup> roleList = sysGroupService.list();
        List<String> nameList = new ArrayList<>();
        for (SysGroup sysGroup : roleList) {
            nameList.add(sysGroup.getGroupName());
        }
        return Result.ok(nameList);
    }
    @ApiOperation(value = "根据id查询")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        SysGroup group = sysGroupService.getById(id);
        if (group == null){
            return  Result.fail();
        }
        return Result.ok(group);
    }

//    @ApiOperation(value = "根据name查询")
//    @GetMapping("/get/{name}")
//    public Result get(@PathVariable String name) {
//        SysGroup group = sysGroupService.get;
//        if (group == null){
//            return  Result.fail();
//        }
//        return Result.ok(group);
//    }


    @ApiOperation("添加课题组")
    @PostMapping("/create")
    public Result save(@RequestBody SysGroup group) {
        // 调用 service 方法
        boolean is_success = sysGroupService.save(group);

        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "修改课题组")
    @PutMapping("/update")
    public Result updateById(@RequestBody SysGroup group) {
        boolean is_success = sysGroupService.updateById(group);
        if (is_success){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "删除课题组")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        boolean is_success = sysGroupService.removeById(id);
        if (is_success){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "根据id列表删除")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        boolean is_success = sysGroupService.removeByIds(idList);
        if (is_success){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

}

