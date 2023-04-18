package edu.sustech.auth.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.sustech.auth.service.SysGroupService;

import edu.sustech.auth.service.SysRoleService;
import edu.sustech.auth.service.SysUserRoleService;
import edu.sustech.auth.service.SysUserService;
import edu.sustech.common.result.Result;
import edu.sustech.model.system.SysGroup;


import edu.sustech.model.system.SysUser;
import edu.sustech.model.system.SysUserRole;
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
    @Autowired
    private SysUserRoleService userRoleService;
    @Autowired
    private SysUserService userService;

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
    @GetMapping("/getGroup")
    public Result<PageGroup> get(@RequestParam(value = "id")Long id) {
        SysGroup group = sysGroupService.getById(id);
        PageGroup tempGroup = new PageGroup();
        QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("group_id",id);
        List<SysUserRole> tlist =  userRoleService.list(wrapper);
        if (tlist.size() != 0){
            List<PageUser> users = new ArrayList<>();
            for (SysUserRole t:tlist){
                Long userId = t.getUserId();
                SysUser user = userService.getById(userId);
                PageUser pageUser = new PageUser();
                pageUser.setId(userId);
                pageUser.setAdmin(t.getRoleId() == 2);
                pageUser.setName(user.getName());
                users.add(pageUser);
            }
            tempGroup.setUsers(users);
        }else {
            tempGroup.setUsers(null);
        }

        tempGroup.setId(group.getId());
        tempGroup.setName(group.getGroupName());
        tempGroup.setCost(1000);
        tempGroup.setTotal(100);
        tempGroup.setLeft(80);

        return Result.ok(tempGroup);
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
                QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
                wrapper.eq("group_id",group.getId());
                List<SysUserRole> tlist =  userRoleService.list(wrapper);
                if (tlist.size() != 0){
                    List<PageUser> users = new ArrayList<>();
                    for (SysUserRole t:tlist){
                        Long userId = t.getUserId();
                        SysUser user = userService.getById(userId);
                        PageUser pageUser = new PageUser();
                        pageUser.setId(userId);
                        pageUser.setAdmin(t.getRoleId() == 2);
                        pageUser.setName(user.getName());
                        users.add(pageUser);
                    }
                    tempGroup.setUsers(users);
                }else {
                    tempGroup.setUsers(null);
                }
                tempGroup.setUsers(null);
                tempGroup.setId(group.getId());
                tempGroup.setName(group.getGroupName());
                tempGroup.setCost(1000);
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
        JSONArray data = jsonParam.getJSONArray("users");
        String js = JSONObject.toJSONString(data, SerializerFeature.WriteClassName);
        List<Long> userList = JSONObject.parseArray(js, Long.class);
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
    @PostMapping("/deleteGroup")
    public Result remove(@RequestBody JSONObject jsonParam) {
        Long id = jsonParam.getLong("id");
        boolean is_success = sysGroupService.removeById(id);
        if (is_success){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

//    @ApiOperation(value = "根据id列表删除")
//    @DeleteMapping("/batchRemove")
//    public Result batchRemove(@RequestBody List<Long> idList) {
//        boolean is_success = sysGroupService.removeByIds(idList);
//        if (is_success){
//            return Result.ok();
//        }else {
//            return Result.fail();
//        }
//    }
    @ApiOperation(value = "添加用户到课题组")
    @PostMapping("addGroupUsers")
    public Result addGroupUsers(@RequestBody JSONObject jsonParam){
        Long groupId = jsonParam.getLong("group");
        Map<String,Object> params = JSONObject.parseObject(jsonParam.getString("form"),
                new TypeReference<Map<String,Object>>(){});
        System.out.println("groupId: "+ groupId);
        System.out.println("params: " + params);
        System.out.println(".........................");
//        JSONArray data = jsonParam.getJSONArray("idList");
//        String js = JSONObject.toJSONString(data, SerializerFeature.WriteClassName);
//        List<Long> idList = JSONObject.parseArray(js, Long.class);
//        if (sysGroupService.addGroupUsers(groupId,idList))
//            return Result.ok();
//        else
//            return Result.fail();
        return Result.ok();
    }
    @ApiOperation(value = "删除组内用户")
    @PostMapping("deleteGroupUser")
    public Result deleteGroupUser(@RequestBody JSONObject jsonParam){
        //        String groupName = jsonParam.getString("group");
        Long groupId = jsonParam.getLong("group");
        Long userId = jsonParam.getLong("user");
        if (sysGroupService.deleteGroupUser(groupId,userId))
            return Result.ok();
        else
            return Result.fail();
    }

    @ApiOperation(value = "获取不在目前课题组中的用户")
    @GetMapping("getUsersNotInGroup")
    public Result<List<PageUser>> getUsersNotInGroup(@RequestParam(value = "groupName") String name){
        Long groupId = sysGroupService.getIdByName(name);
        List<PageUser> list = sysGroupService.getUsersNotInGroup(groupId);
        return Result.ok(list);
    }
}

