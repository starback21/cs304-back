package edu.sustech.auth.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.sustech.auth.service.SysGroupService;

import edu.sustech.common.result.Result;
import edu.sustech.model.system.SysGroup;


import edu.sustech.model.system.SysUser;
import edu.sustech.re.system.PageGroup;
import edu.sustech.re.system.PageUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//@CrossOrigin
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
    @ApiOperation(value = "分页获取课题组数据")
    @GetMapping("/getGroups")
    public Result<Map<String, Object>> getGroups(@RequestParam(value = "page") Long page,
                                 @RequestParam(value = "pageSize") Long limit
                            ){
        Map<String,Object> objectMap = new HashMap<>(2);
        List<SysGroup> groupList = sysGroupService.list();
        List<PageGroup> groups = new ArrayList<>();
        int index = 0;
        for (SysGroup group : groupList) {
            index ++;
            if (index > (page - 1) * limit && index <= page * limit) {
                PageGroup tempGroup = new PageGroup();
                tempGroup.setId(group.getId());
                tempGroup.setName(group.getGroupName());
                tempGroup.setCost(20);
                tempGroup.setTotal(100);
                tempGroup.setLeft(80);
                groups.add(tempGroup);
            }
        }
        objectMap.put("groups",groups);
        objectMap.put("total",groupList.size());
        return Result.ok(objectMap);
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
    public Result save(@RequestBody JSONObject jsonParam) {
        // 调用 service 方法
        String name = jsonParam.get("name").toString();
        SysGroup group = new SysGroup();
        group.setGroupName(name);
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

    @ApiOperation(value = "移除课题组")
    @PostMapping("/remove")
    public Result remove(@RequestBody JSONObject jsonParam) {
        Long id = jsonParam.getLong("id");
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
    @ApiOperation(value = "添加用户到课题组")
    @PostMapping("addGroupUsers")
    public Result addGroupUsers(@RequestParam(value = "group_id") Long groupId,
                                @RequestParam(value = "user_id") Long userId){
        if (sysGroupService.addGroupUsers(groupId,userId))
            return Result.ok();
        else
            return Result.fail();
    }
    @ApiOperation(value = "删除组内用户")
    @PostMapping("deleteGroupUser")
    public Result deleteGroupUser(@RequestParam(value = "group_id") Long groupId,
                                  @RequestParam(value = "user_id") Long userId){
        if (sysGroupService.deleteGroupUser(groupId,userId))
            return Result.ok();
        else
            return Result.fail();
    }

    @ApiOperation(value = "删除组内用户")
    @PostMapping("getUsersNotInGroup")
    public Result<List<PageUser>> getUsersNotInGroup(@RequestParam(value = "group_id") Long groupId){
        if (sysGroupService.getUsersNotInGroup(groupId))
            return Result.ok();
        else
            return Result.fail();
    }
}

