package edu.sustech.auth.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.sustech.auth.service.*;
import edu.sustech.common.handler.SpecialException;
import edu.sustech.common.jwt.JwtHelper;
import edu.sustech.common.result.Result;
import edu.sustech.model.system.*;
import edu.sustech.re.system.PageApplication;
import edu.sustech.re.user.UserFund;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api(tags = "用户信息管理")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    SysApplicationService applicationService;
    @Autowired
    SysGroupService groupService;
    @Autowired
    SysUserService userService;
    @Autowired
    SysFundAppService fundAppService;
    @Autowired
    SysFundingService fundingService;
    @Autowired
    SysGroupFundDetailService groupFundDetailService;
    @Autowired
    SysGroupFundService groupFundService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @ApiOperation(value = "删除申请")
    @PostMapping("/cancelApplication")
    public Result cancelApplication(@RequestBody JSONObject jsonParam){
        Long id = jsonParam.getLong("id");
        boolean is_success = applicationService.removeById(id);
        if (is_success)
            return Result.ok();
        else
            return Result.fail();
    }

    @ApiOperation(value = "删除申请")
    @PostMapping("/batchCancelApplication")
    public Result batchCancelApplication(@RequestBody JSONObject jsonParam){
        JSONArray data = jsonParam.getJSONArray("ids");
        String js = JSONObject.toJSONString(data, SerializerFeature.WriteClassName);
        List<Long> idList = JSONObject.parseArray(js, Long.class);
        QueryWrapper<SysApplication> wrapper = new QueryWrapper<>();
        wrapper.in("id",idList);
        boolean is_success = applicationService.remove(wrapper);
        if (is_success)
            return Result.ok();
        else
            return Result.fail();
    }

    @ApiOperation(value = "添加申请")
    @PostMapping("/createApplication")
    public Result save(@RequestBody Map<String,Object> params){
        Map<String, String> form = (Map<String, String>) params.get("form");

        System.out.println("params: "+form);
        if (form.get("title").equals("")){
            throw new SpecialException(201,"没有标题");
        }
        String title =  form.get("title");
        if (form.get("group").equals("")){
            throw new SpecialException(201,"没有课题组");
        }
        String group = form.get("group");
        String c1 = form.get("category1");
//        String c2 = form.get("category2");
        if (form.get("number").equals("")){
            throw new SpecialException(201,"没有数字");
        }
        String num_str = form.get("number");
        int num = Integer.parseInt(num_str);
        String comment = form.get("comment");
        SysApplication application = new SysApplication();
        application.setTitle(title);
        application.setGroupName(group);
        Long id = groupService.getIdByName(group);
        application.setGroupId(id);
        application.setNumber(num);
        application.setCategory1(c1);
//        application.setCategory2(c2);
        application.setComment(comment);
        applicationService.save(application);
        //插入经费申请对应表
        //获取刚刚插入的app
        QueryWrapper<SysApplication> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        SysApplication app = applicationService.getOne(wrapper);
        //创建新的对象
        SysFundApp fundApp = new SysFundApp();
        fundApp.setAppId(app.getId());
        fundApp.setAppName(app.getTitle());
        //获取fundID
        Long fundId = fundingService.getByName(title).getId();
        fundApp.setFundName(title);
        fundApp.setFundId(fundId);
        boolean is_success = fundAppService.save(fundApp);
        if (!is_success) return Result.fail();
        else return Result.ok();
    }

    @ApiOperation(value = "获取申请")
    @GetMapping("/getUserApplications")
    public Result<Map<String, Object>> getApplications(@RequestParam(value = "page") int page,
                                                       @RequestParam(value = "type",required = false) String type){

        List<SysApplication> list;
        if (type != null){
            if (type.equals("all")){
                list = applicationService.selectAll();
            } else if (type.equals("underway")) {
                LambdaQueryWrapper<SysApplication> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(SysApplication::getState,"underway");
                list = applicationService.list(wrapper);
            } else {
                list = applicationService.selectAll();
            }
        }else {
            list = applicationService.selectAll();
        }
        List<PageApplication> data = new ArrayList<>();
        int index = 0;
        for (SysApplication a : list){
            index++;
            if (index > (page - 1) * 3 && index <= page * 3){
                PageApplication tmp = new PageApplication();
                tmp.setId(Math.toIntExact(a.getId()));
                tmp.setName(a.getTitle());
                tmp.setKey(a.getId().toString());
                tmp.setState(a.getState());
                tmp.setGroup(a.getGroupName());
                tmp.setNum(a.getNumber());
                tmp.setDate(a.getCreateTime());
                tmp.setPeople(a.getPeople());
                tmp.setCategory(a.getCategory1());
                data.add(tmp);
            }
        }
        Map<String, Object> result = new HashMap<>(2);
        result.put("data",data);
        result.put("total",data.size());
        return Result.ok(result);

    }

    @ApiOperation(value = "根据用户id获取课题组")
    @GetMapping("getUserGroups")
    public Result<List<Map<Object,Object>>> getUserGroups(@RequestHeader("Authorization") String token){
        Long userId = JwtHelper.getUserId(token);
        String userName = JwtHelper.getUsername(token);
        QueryWrapper<SysUserRole> roleQueryWrapper = new QueryWrapper<>();
        //获取用户对应的课题组
        roleQueryWrapper.eq("user_id",userId);
        List<SysUserRole> sysUserRoles = sysUserRoleService.list(roleQueryWrapper);
        List<Long>groupIds = new ArrayList<>();
        //循环加入课题组id
        for(SysUserRole sysUserRole:sysUserRoles){
            groupIds.add(sysUserRole.getGroupId());
        }
        //获取
        List<Map<Object,Object>> users = new ArrayList<>();
        for(SysUserRole sysUserRole:sysUserRoles){
            Map<Object,Object> map = new HashMap<>();
            map.put("userId",sysUserRole.getUserId());
            map.put("admin",sysUserRole.getRoleId());
            map.put("groupId",sysUserRole.getGroupId());
            map.put("userName",userService.getById(sysUserRole.getUserId()).getName());
            users.add(map);
        }

        List<SysGroupFund> sysGroupFunds = groupFundService.list();

        List<Map<Object,Object>>result = new ArrayList<>();
        Set<Long> set = new HashSet<>();
        for(Long groupId:groupIds){
            for(SysGroupFund sysGroupFund:sysGroupFunds){
                if(sysGroupFund.getGroupId().equals(groupId)){
                    if (set.contains(groupId)){
                        for (Map<Object,Object> tempMap : result){
                            if (tempMap.get("id").equals(groupId)){
                                tempMap.put("total",(Long)tempMap.get("total")+
                                       sysGroupFund.getTotalAmount() ) ;
                                tempMap.put("left",(Long)tempMap.get("left")+
                                        sysGroupFund.getRemainAmount());
                            }
                        }
                    }else {
                        set.add(groupId);
                        Map<Object,Object>map = new HashMap<>();
                        map.put("id",sysGroupFund.getGroupId());
                        map.put("name",sysGroupFund.getGroupName());
                        map.put("total",sysGroupFund.getTotalAmount());
                        map.put("left",sysGroupFund.getRemainAmount());
                        Map<String,String>user = new HashMap<>();
                        for(Map<Object,Object> user1 : users){
                            if(user1.get("groupId").equals(groupId)){
                                user.put("name",user1.get("userName").toString());
                                if(user1.get("admin").toString().equals("2"))
                                {
                                    List<UserFund> list =
                                            groupFundService.getGroupFundByGId(groupId);
                                    user.put("admin","True");
                                    map.put("fund",list);
                                }
                                else {
                                    List<UserFund> list =
                                            groupFundService.getGroupFundByGId(groupId);
                                    user.put("admin","False");
                                    map.put("fund",list);
                                }
                            }
                        }
                        map.put("users",user);
                        result.add(map);
                    }

                }
            }
        }
        return Result.ok(result);
    }
}
