package edu.sustech.auth.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.sustech.auth.service.SysApplicationService;
import edu.sustech.auth.service.SysFundingService;

import edu.sustech.auth.service.SysUserService;
import edu.sustech.common.handler.SpecialException;
import edu.sustech.common.utils.MD5;
import edu.sustech.model.system.SysApplication;
import edu.sustech.model.system.SysFunding;

import edu.sustech.re.system.PageUser;
import edu.sustech.common.result.Result;
import edu.sustech.model.system.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
//@CrossOrigin
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService service;
    @Autowired
    private SysFundingService fundingService;
    @Autowired
    private SysApplicationService applicationService;

    //用户条件分页查询
    @ApiOperation("用户条件分页查询")
    @GetMapping("getUsers")
    public Result<Map<String, Object>> index(@RequestParam(value = "page") Long page,
                                             @RequestParam(value = "pageSize") Long limit,
                                             @RequestParam(value = "key",required = false)Long userid
                                        ) {
        Map<String, Object> result = new HashMap<>(2);
        //封装条件，判断条件值不为空
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.ne("name","admin");
        wrapper.orderByAsc("uid");
        List<SysUser> userList = service.list(wrapper);
        List<PageUser> users = new ArrayList<>();
        int index = 0;
    if (userid != null){
        SysUser user = service.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUid,userid));
        PageUser tuser = new PageUser();
        tuser.setId(user.getId());
        tuser.setKey(user.getUid());
        tuser.setName(user.getName());
        tuser.setEmail(user.getEmail());
        tuser.setPhone(user.getPhone());
        List<String> groupList = service.getUserGroup(user.getId());
        tuser.setGroup(groupList);
        users.add(tuser);
        result.put("users",users);
        result.put("total",1);
    }else {
        for (SysUser user : userList) {
            index ++;
            if (index > (page - 1) * limit && index <= page * limit) {
                PageUser tempUser = new PageUser();
                tempUser.setId(user.getId());
                tempUser.setKey(user.getUid());
                tempUser.setName(user.getName());
                tempUser.setEmail(user.getEmail());
                tempUser.setPhone(user.getPhone());
                users.add(tempUser);
            }
        }
        result.put("users",users);
        result.put("total",userList.size());
    }
    return Result.ok(result);

    }


    @ApiOperation(value = "根据id获取用户")
    @GetMapping("/get/{id}")
    public Result<SysUser> get(@PathVariable Long id) {
        SysUser user = service.getById(id);
        if (user == null){
            return Result.fail();
        }
        return Result.ok(user);
    }
    @ApiOperation("查询所有用户名字")
    @GetMapping("/getAllAccountName")
    public Result<List<String>> getAllAccountName() {
        List<String> nameList = new ArrayList<>();
        List<SysUser> userList = service.list();
        for (SysUser sysUser : userList){
            nameList.add(sysUser.getName());
        }
        return Result.ok(nameList);
    }
    @ApiOperation(value = "添加用户")
    @PostMapping("/createAccount")
    public Result save(@RequestBody JSONObject jsonParam) {
        JSONArray data = jsonParam.getJSONArray("groups");
        String js = JSONObject.toJSONString(data, SerializerFeature.WriteClassName);
        List<String > groupNameList = JSONObject.parseArray(js, String.class);
        String name = jsonParam.get("name").toString();
        String mail = jsonParam.get("email").toString();
        String phone = jsonParam.get("phone").toString();
        SysUser user = new SysUser();
        if (service.selectNameSame(name)){
            throw new SpecialException(201,"用户名重复");
        }
        user.setName(name);
        user.setEmail(mail);
        user.setPhone(phone);
        //获取一个范围在100000-199999 之间的随机uid，共九千九百九十九个uid
        Random random = new Random();
        int l1 = random.nextInt(199999) + 100000;
        //当uid不重复时
        while (service.selectUidSame((long) l1)){
            l1 = random.nextInt(199999) + 100000;
        }
        user.setUid((long) l1);
        //初始密码即为uid
        String psw = String.valueOf(l1);
        //加密密码
        String passwordMd5 = MD5.encrypt(psw);
        user.setPassword(passwordMd5);
        boolean is_success = service.save(user);
        service.addUserToGroup((long) l1,groupNameList);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "根据id列表删除")
    @PostMapping("/deleteUsers")
    public Result batchRemove(@RequestBody JSONObject jsonParam) {
        JSONArray data = jsonParam.getJSONArray("idList");
        String js = JSONObject.toJSONString(data, SerializerFeature.WriteClassName);
        List<Long> idList = JSONObject.parseArray(js, Long.class);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.in("uid",idList);
        boolean is_success = service.remove(wrapper);
        if (is_success){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "获取管理员主页数据")
    @GetMapping("/getAdminHomeStatistics")
    public Result<Map<String,Integer>> getAdminHomeStatistics(){
        List<SysApplication> appList = applicationService.selectAll();
        List<SysFunding> fundingList = fundingService.list();
        Map<String,Integer> map = new HashMap<>();
        int newApp = 0;
        int unApp = 0;
        int newFund = 0;
        int unFund = 0;
        for (SysApplication app : appList){
            if (app.getState().equals("0")){
                newApp++;
            }else {
                unApp++;
            }
        }
        for (SysFunding f : fundingList){
            if (f.getStatus().equals("completed")){
                unFund++;
            }else {
                newFund++;
            }
        }
        map.put("newApplication",newApp);
        map.put("unserwayApplication",unApp);
        map.put("newFund",newFund);
        map.put("underwayFund",unFund);
        return Result.ok();
    }

    @ApiOperation(value = "添加用户")
    @PostMapping("/addUserToGroups")
    public Result addUserToGroups(@RequestBody JSONObject jsonParam){
        JSONArray data = jsonParam.getJSONArray("groups");
        String js = JSONObject.toJSONString(data, SerializerFeature.WriteClassName);
        List<String > groupNameList = JSONObject.parseArray(js, String.class);

        JSONArray data1 = jsonParam.getJSONArray("admin");
        String js1 = JSONObject.toJSONString(data1, SerializerFeature.WriteClassName);
        List<String > adminList = JSONObject.parseArray(js1, String.class);

        Long uid = jsonParam.getLong("userId");
        service.addUserToGroup(uid,groupNameList,adminList);
        return Result.ok();
    }
}

