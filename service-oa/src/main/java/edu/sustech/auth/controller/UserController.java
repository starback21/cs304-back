package edu.sustech.auth.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import edu.sustech.auth.service.*;
import edu.sustech.common.handler.SpecialException;
import edu.sustech.common.jwt.JwtHelper;
import edu.sustech.common.result.Result;
import edu.sustech.model.system.*;
import edu.sustech.re.system.PageApplication;
import edu.sustech.re.system.PageMsg;
import edu.sustech.re.system.PageUser;
import edu.sustech.re.user.UserFund;
import edu.sustech.re.user.UserGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @Autowired
    private SysMessageService messageService;
    @Autowired
    private SysRemessageService remessageService;

    @ApiOperation(value = "删除申请")
    @PostMapping("/cancelApplication")
    public Result cancelApplication(@RequestBody JSONObject jsonParam){
        Long id = jsonParam.getLong("id");
        LambdaQueryWrapper<SysMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMessage::getAppId,id);
        messageService.remove(wrapper);
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
    public Result save(@RequestHeader("Authorization") String token,
                       @RequestBody Map<String,Object> params){
        //获取token信息
        Long userId = JwtHelper.getUserId(token);
        String userName = JwtHelper.getUsername(token);
        if (userId == null || userName == null){
            return Result.fail(204,"wrong");
        }
        //获取表单信息
        Map<String, Object> form = (Map<String, Object>) params.get("form");
        if (form.get("group").equals("")){
            return Result.fail(400,"缺少课题组");
        }
        String fundName = form.get("fund").toString();
        String group = form.get("group").toString();
        String c1 = form.get("category").toString();
        int num = (Integer) form.get("number");
        String comment = form.get("comment").toString();
        SysApplication application = new SysApplication();
        application.setTitle(fundName);
        application.setGroupName(group);
        Long groupId = groupService.getIdByName(group);
        application.setGroupId(groupId);
        application.setNumber(num);
        application.setState("underway");
        application.setCategory1(c1);
        application.setComment(comment);
        applicationService.save(application);
        //插入经费申请对应表
        //获取刚刚插入的app
        QueryWrapper<SysApplication> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time").last("limit 1");
        SysApplication app = applicationService.getOne(wrapper);
        //创建新的对象
        SysFundApp fundApp = new SysFundApp();
        fundApp.setAppId(app.getId());
        fundApp.setAppName(app.getTitle());
        //获取fundID
        Long fundId = fundingService.getByName(fundName).getId();
        fundApp.setFundName(fundName);
        fundApp.setFundId(fundId);
        boolean is_success = fundAppService.save(fundApp);
        //加入新消息
        SysMessage message = new SysMessage();
        message.setType("审批通知");
        message.setGroupId(groupId);
        message.setUserId(userId);
        message.setAppId(app.getId());
        message.setContent("新审批: "+group+" "+userId+" "+userName + comment);
        messageService.save(message);
        if (!is_success) return Result.fail();
        else return Result.ok();
    }

    @ApiOperation(value = "获取申请")
    @GetMapping("/getUserApplications")
    public Result<Map<String, Object>> getApplications(@RequestParam(value = "page") int page,
                                                       @RequestParam(value = "type",required = false) String type,
                                                       @RequestHeader("Authorization") String token){
        Long userId = JwtHelper.getUserId(token);
        String userName = JwtHelper.getUsername(token);
        if (userId == null || userName == null){
            return Result.fail(204,"wrong");
        }
        QueryWrapper<SysUserRole> roleQueryWrapper = new QueryWrapper<>();
        //获取用户对应的课题组
        roleQueryWrapper.eq("user_id",userId);
        List<SysUserRole> roleList = sysUserRoleService.list(roleQueryWrapper);
        List<Long > groupIds = new ArrayList<>();
        for (SysUserRole role : roleList){
            groupIds.add(role.getGroupId());
        }
        LambdaQueryWrapper<SysApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysApplication::getGroupId,groupIds);
        List<SysApplication> list;
        if (type != null){
            if (type.equals("underway")) {
                wrapper.eq(SysApplication::getState,"underway");

            } else if (type.equals("reject")){
                wrapper.eq(SysApplication::getState,"reject");
            }
        }
        list = applicationService.list(wrapper);
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
                tmp.setDate(a.getCreateTime().getTime());
                tmp.setPeople(a.getPeople());
                tmp.setCategory(a.getCategory1());
                data.add(tmp);
            }
        }
        Map<String, Object> result = new HashMap<>(2);
        result.put("data",data);
        result.put("total",list.size());
        return Result.ok(result);

    }

    @ApiOperation(value = "根据用户id获取课题组")
    @GetMapping("getUserGroups")
    public Result<List<UserGroup>> getUserGroups(
            @RequestHeader("Authorization") String token){
        Long userId = JwtHelper.getUserId(token);
        String userName = JwtHelper.getUsername(token);
        if (userId == null || userName == null){
            return Result.fail(204,"wrong");
        }
        QueryWrapper<SysUserRole> roleQueryWrapper = new QueryWrapper<>();
        //获取用户对应的课题组
        roleQueryWrapper.eq("user_id",userId);
        List<SysUserRole> roleList = sysUserRoleService.list(roleQueryWrapper);
        //作为结果返回
        List<UserGroup> groups = new ArrayList<>();
        //遍历课题组
        for (SysUserRole role : roleList) {
            UserGroup tempGroup = new UserGroup();
            tempGroup.setIsAdmin(role.getRoleId() == 2);
            List<PageUser> users = groupService.getGroupMember(role.getGroupId());
            if (users.size() != 0){
                tempGroup.setUsers(users);
            }else {
                tempGroup.setUsers(null);
            }
            tempGroup.setId(role.getGroupId());
            tempGroup.setName(role.getGroupName());
            List<SysGroupFund> groupFund =
                    groupFundService.getGroupFundByGroupId(role.getGroupId());
            long cost = 0;
            long total = 0;
            long left = 0;
            for(SysGroupFund fund:groupFund){
                cost += fund.getCost();
                total += fund.getTotalAmount();
                left += fund.getRemainAmount();
            }
            List<UserFund> list =
                groupFundService.getGroupFundByGId(role.getGroupId());
            tempGroup.setFund(list);
            tempGroup.setCost((int) cost);
            tempGroup.setTotal((int) total);
            tempGroup.setLeft((int) left);
            groups.add(tempGroup);
        }
        return Result.ok(groups);
    }

    @ApiOperation("getUserMessages")
    @GetMapping("getUserMessages")
    public Result<Map<String ,Object>> getUserMessages(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "page") int page,
           @RequestParam(value = "type",required = false)String  type
    ){
        Long userId = JwtHelper.getUserId(token);
        LambdaQueryWrapper<SysRemessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRemessage::getUserId,userId);
        if (type!=null && !type.equals("all")){
            wrapper.eq(SysRemessage::getType,type);
        }
        List<SysRemessage> messages = remessageService.list(wrapper);
        List<PageMsg> result_list = new ArrayList<>();
        UpdateWrapper<SysRemessage> upwrapper = new UpdateWrapper<>();
        int index = 0;
        assert messages != null;
        for (SysRemessage message : messages){
            index++;
            if (index > (page - 1) * 4 && index <= page * 4){
                PageMsg msg = new PageMsg();
                msg.setType(message.getType());
                msg.setDate(message.getCreateTime());
                msg.setNewComing(message.getState()==0);
                msg.setMsg(message.getContent());
                if (message.getState() == 0) {
                    upwrapper.eq("id", message.getId()).set("state", 1);
                    remessageService.update(upwrapper);
                }
                result_list.add(msg);
            }
        }
        Map<String,Object> resul = new HashMap<>(2);
        resul.put("data",result_list);
        resul.put("total",messages.size());
        return Result.ok(resul);
    }

    @ApiOperation(value = "获取用户主页数据")
    @GetMapping("/getUserHomeStatistics")
    public Result<Map<String,Integer>> getUserHomeStatistics(@RequestHeader("Authorization") String token){
        Long userId = JwtHelper.getUserId(token);
        if (userId == null ){
            return Result.fail(204,"wrong");
        }
        List<SysUserRole> roles = sysUserRoleService.list(
                new QueryWrapper<SysUserRole>().eq("user_id",userId)
        );
        int newApp = 0;
        int unApp = 0;
        int permit = 0;
        int reject = 0;
        List<SysRemessage> remessageList = remessageService.list(
                new LambdaQueryWrapper<SysRemessage>().eq(SysRemessage::getUserId, userId)
        );
        if (remessageList != null) {
            for (SysRemessage re : remessageList){
                if (re.getState()==0) newApp++;
            }
        }
        //遍历每个组的信息
        for (SysUserRole r : roles){
            Long groupId = r.getGroupId();
            List<SysApplication> appList = applicationService.list(
                    new QueryWrapper<SysApplication>().eq("group_id",groupId)
            );
            for (SysApplication app : appList){
                switch (app.getState()) {
                    case "underway":
                        unApp++;
                        break;
                    case "reject":
                        reject++;
                        break;
                    case "complete":
                        permit++;
                        break;
                }
            }
        }
        Map<String,Integer> map = new HashMap<>();
        map.put("recentApplication",newApp);
        map.put("underwayApplication",unApp);
        map.put("permittedApplication",permit);
        map.put("rejectedApplication",reject);
        return Result.ok(map);
    }

    @ApiOperation(value = "获取用户权限数据")
    @GetMapping("/getIdentity")
    public Result<Map<String,String>> getIdentity(@RequestHeader("Authorization") String token){
        Long userId = JwtHelper.getUserId(token);
        String userName = JwtHelper.getUsername(token);
        Map<String,String> map = new HashMap<>();
        if (userName == null){
            map.put("identity","");
            return Result.fail(map);
        }
        assert userId != null;
        if (userName.equals("admin")){
            map.put("identity","admin");
        }else {
            map.put("identity","user");
        }
        return Result.ok(map);
    }

    @ApiOperation(value = "根据组名获取经费信息")
    @GetMapping ("getGroupFundRemain")
    public Result<List<Map<String ,Object>>> getFundInfoByGroup(@RequestParam String groupId) {
        List<Map<String ,Object>> list = new ArrayList<>();
        List<SysGroupFund> sysGroupFunds = groupFundService.list(
                new LambdaQueryWrapper<SysGroupFund>().eq(SysGroupFund::getGroupId,groupId)
        );
        for(SysGroupFund sysGroupFund:sysGroupFunds){
            Map<String,Object > map = new HashMap<>();
            map.put("name",sysGroupFund.getGroupName());
            map.put("value",sysGroupFund.getRemainAmount());
            list.add(map);
        }
        return Result.ok(list);
    }

    @ApiOperation(value = "获取每一天的申请次数")
    @GetMapping("getUserAppTimes")
    public Result getUserAppTimes(
            @RequestHeader("Authorization") String token
    ){
        Long userId = JwtHelper.getUserId(token);
        List<Map<Object,Object>> list = messageService.getDate(userId);
        return Result.ok(list);
    }
}
