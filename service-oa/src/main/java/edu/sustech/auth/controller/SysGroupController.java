package edu.sustech.auth.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.sustech.auth.service.SysGroupService;

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
        List<SysGroup> groupList = sysGroupService.list();
        List<String> nameList = new ArrayList<>();
        for (SysGroup sysGroup :groupList) {
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
                                 @RequestParam(value = "pageSize") Long limit,
                                @RequestParam(value = "id",required = false)Long groupid){
        Map<String,Object> objectMap = new HashMap<>(2);
        //查询所有课题组的信息
        List<SysGroup> groupList = sysGroupService.list();
        //作为结果返回
        List<PageGroup> groups = new ArrayList<>();
        //条件查询，如果有id则单独查询
        if (groupid!=null){
            //从id获取课题组的所有信息
            SysGroup group = sysGroupService.getById(groupid);
            //创建返回对象
            PageGroup tempGroup = new PageGroup();
            QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
            wrapper.eq("group_id",groupid);
            //从角色用户对应表中查到所有隶属于该课题组的人
            List<SysUserRole> tlist =  userRoleService.list(wrapper);
            //如果课题组中的人不为空
            if (tlist.size() != 0){
                //返回结果中的用户列表
                List<PageUser> users = new ArrayList<>();
                //遍历角色用户列表
                for (SysUserRole t:tlist){
                    Long userId = t.getUserId();
                    String username = t.getUserName();
                    //创建返回的用户对象
                    PageUser pageUser = new PageUser();
                    pageUser.setId(userId);
                    pageUser.setAdmin(t.getRoleId() == 2);
                    pageUser.setName(username);
                    //在用户列表中加入该对象
                    users.add(pageUser);
                }
                //在返回的group对象中加入user列表
                tempGroup.setUsers(users);
            }else {
                tempGroup.setUsers(null);
            }
            tempGroup.setId(group.getId());
            tempGroup.setName(group.getGroupName());
            tempGroup.setCost(1000);
            tempGroup.setTotal(100);
            tempGroup.setLeft(80);
            groups.add(tempGroup);
        }else {
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
                    tempGroup.setId(group.getId());
                    tempGroup.setName(group.getGroupName());
                    tempGroup.setCost(1000);
                    tempGroup.setTotal(100);
                    tempGroup.setLeft(80);
                    groups.add(tempGroup);
                }
            }
        }

        objectMap.put("groups",groups);
        objectMap.put("total",groupList.size());
        return Result.ok(objectMap);
    }

    @ApiOperation("添加课题组")
    @PostMapping("/create")
    public Result save(@RequestBody JSONObject jsonParam) {
        // 调用 service 方法
        String name = jsonParam.get("name").toString();
        JSONArray data = jsonParam.getJSONArray("users");
        String js = JSONObject.toJSONString(data, SerializerFeature.WriteClassName);
        List<String> userList = JSONObject.parseArray(js, String.class);
        SysGroup group = new SysGroup();
        group.setGroupName(name);
        boolean is_success = sysGroupService.save(group);
        Long groupId = sysGroupService.getIdByName(name);
        sysGroupService.createAndAddUser(groupId,name,userList);
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
    @PostMapping("/addGroupUsers")
    public Result addGroupUsers(@RequestBody JSONObject jsonParam){
        String groupName = jsonParam.getString("group");
        //获取用户列表
        JSONArray data = jsonParam.getJSONArray("account");
        String js = JSONObject.toJSONString(data, SerializerFeature.WriteClassName);
        List<String> userList = JSONObject.parseArray(js, String.class);
        //获取admin列表
        JSONArray data2 = jsonParam.getJSONArray("admin");
        String js2 = JSONObject.toJSONString(data2, SerializerFeature.WriteClassName);
        List<String> adminList = JSONObject.parseArray(js2, String.class);
        if (sysGroupService.addGroupUsers(groupName,userList,adminList)){
            return Result.ok();
        }else {
            return Result.fail();
        }

    }
    @ApiOperation(value = "删除组内用户")
    @PostMapping("deleteGroupUser")
    public Result deleteGroupUser(@RequestBody JSONObject jsonParam){

        String groupName = jsonParam.getString("group");
        Long groupId = sysGroupService.getIdByName(groupName);
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

