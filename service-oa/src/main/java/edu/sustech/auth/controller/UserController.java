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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "后台登录管理")
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
        SysApplication appli = applicationService.getById(id);
        Long fundId = fundAppService.getByAppId(appli.getId()).getFundId();
        SysGroupFundDetail sysGroupFundDetail=groupFundDetailService.getByGroupCategory(appli.getCategory1(),appli.getCategory2(),fundId, id);
        if(sysGroupFundDetail!=null){
            sysGroupFundDetail.setTotalAmount(sysGroupFundDetail.getTotalAmount()-appli.getNumber());
            sysGroupFundDetail.setRemainAmount(sysGroupFundDetail.getRemainAmount()-appli.getNumber());
            groupFundDetailService.updateById(sysGroupFundDetail);
            SysGroupFund sysGroupFund=groupFundService.getByGroupId(sysGroupFundDetail.getGroupId());
            sysGroupFund.setTotalAmount(sysGroupFund.getTotalAmount()-appli.getNumber());
            sysGroupFund.setRemainAmount(sysGroupFund.getRemainAmount()-appli.getNumber());
            groupFundService.updateById(sysGroupFund);
            SysFunding sysFunding=fundingService.getById(fundId);
            sysFunding.setRemainAmount(sysFunding.getRemainAmount()+appli.getNumber());
            sysFunding.setCost(sysFunding.getCost()-appli.getNumber());
            fundingService.updateById(sysFunding);
        }
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
        for (Long id : idList){
            SysApplication app = applicationService.getById(id);
            Long groupId = app.getGroupId();
            SysFundApp fundApp = fundAppService.getByAppId(id);
            SysGroupFund groupFund = groupFundService.getById(groupId);
            SysFunding funding = fundingService.getById(fundApp.getFundId());
            QueryWrapper<SysGroupFundDetail> queryWrapper = new QueryWrapper<>();
            SysGroupFundDetail sysGroupFundDetail=groupFundDetailService.getByGroupCategory(app.getCategory1(),app.getCategory2(),fundApp.getFundId(), app.getGroupId());
            if(sysGroupFundDetail!=null){
                sysGroupFundDetail.setTotalAmount(sysGroupFundDetail.getTotalAmount()-Long.valueOf(app.getNumber()));
                sysGroupFundDetail.setRemainAmount(sysGroupFundDetail.getTotalAmount()-sysGroupFundDetail.getUsedAmount());
                groupFundDetailService.updateById(sysGroupFundDetail);
                groupFund.setTotalAmount(groupFund.getTotalAmount()-app.getNumber());
                groupFund.setRemainAmount(groupFund.getRemainAmount()-app.getNumber());
                groupFund.setTotalAmount(groupFund.getTotalAmount() - Long.valueOf(app.getNumber()));
                groupFundService.updateById(groupFund);
                funding.setCost(funding.getCost() - Long.valueOf(app.getNumber()));
                funding.setRemainAmount(funding.getTotalAmount() - funding.getCost());
                fundingService.updateById(funding);
            }
        }
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
        String c2 = form.get("category2");
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
        application.setCategory2(c2);
        application.setComment(comment);
        applicationService.save(application);
        QueryWrapper<SysApplication> queryWrapper = new QueryWrapper<>();
        SysApplication appli=applicationService.getOne(queryWrapper.last("limit 1"));;
        Long fundId = fundAppService.getByAppId(appli.getId()).getFundId();
        SysGroupFundDetail sysGroupFundDetail=groupFundDetailService.getByGroupCategory(c1,c2,fundId, id);
        if (sysGroupFundDetail==null){
            SysGroupFundDetail sysGroupFundDetail1=new SysGroupFundDetail();
            sysGroupFundDetail1.setFundingId(fundId);
            sysGroupFundDetail1.setGroupId(id);
            sysGroupFundDetail1.setCategory1(c1);
            sysGroupFundDetail1.setCategory2(c2);
            sysGroupFundDetail1.setTotalAmount((long) num);
            sysGroupFundDetail1.setUsedAmount((long) 0);
            sysGroupFundDetail1.setRemainAmount((long) num);
            groupFundDetailService.save(sysGroupFundDetail1);
            SysGroupFund sysGroupFund=groupFundService.getByGroupId(id);
            sysGroupFund.setTotalAmount(sysGroupFund.getTotalAmount()+num);
            sysGroupFund.setRemainAmount(sysGroupFund.getRemainAmount()+num);
            groupFundService.updateById(sysGroupFund);
            SysFunding sysFunding=fundingService.getById(fundId);
            sysFunding.setCost(sysFunding.getCost()+num);
            sysFunding.setRemainAmount(sysFunding.getTotalAmount()-sysFunding.getCost());
            fundingService.updateById(sysFunding);
        }else{
            sysGroupFundDetail.setTotalAmount(sysGroupFundDetail.getTotalAmount()+num);
            sysGroupFundDetail.setRemainAmount(sysGroupFundDetail.getRemainAmount()+num);
            groupFundDetailService.updateById(sysGroupFundDetail);
            SysGroupFund sysGroupFund=groupFundService.getByGroupId(id);
            sysGroupFund.setTotalAmount(sysGroupFund.getTotalAmount()+num);
            sysGroupFund.setRemainAmount(sysGroupFund.getRemainAmount()+num);
            groupFundService.updateById(sysGroupFund);
            SysFunding sysFunding=fundingService.getById(fundId);
            sysFunding.setCost(sysFunding.getCost()+num);
            sysFunding.setRemainAmount(sysFunding.getTotalAmount()-sysFunding.getCost());
            fundingService.updateById(sysFunding);
        }

        return Result.ok();
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
            if(groupIds.contains(sysUserRole.getGroupId())){
                map.put("userId",sysUserRole.getUserId());
                map.put("admin",sysUserRole.getRoleId());
                map.put("groupId",sysUserRole.getGroupId());
                users.add(map);
            }
        }

        List<SysGroupFund> sysGroupFunds = groupFundService.list();

        List<SysUser>sysUsers = userService.list();


        List<Map<Object,Object>>usernames= new ArrayList<>();
        for(Map<Object,Object>user:users){
            for(SysUser sysUser:sysUsers){
                if(user.get("userId").equals(sysUser.getId())){
                    Map<Object,Object>map = new HashMap<>();
                    map.put("username",sysUser.getName());
                    map.put("admin",user.get("admin"));
                    map.put("groupId",user.get("groupId"));
                    usernames.add(map);
                    break;
                }
            }
        }
        List<Map<Object,Object>>result = new ArrayList<>();
        for(Long groupId:groupIds){
            for(SysGroupFund sysGroupFund:sysGroupFunds){
                if(sysGroupFund.getGroupId().equals(groupId)){
                    Map<Object,Object>map = new HashMap<>();
                    map.put("id",sysGroupFund.getGroupId());
                    map.put("name",sysGroupFund.getGroupName());
                    map.put("total",sysGroupFund.getTotalAmount());
                    map.put("left",sysGroupFund.getRemainAmount());
                    Map<String,String>user = new HashMap<>();
                    for(Map<Object,Object>user1:usernames){
                        if(user1.get("groupId").equals(groupId)){
                            user.put("name",user1.get("username").toString());
                            if(user1.get("admin").toString().equals("2")) {user.put("admin","True");}
                            else {user.put("admin","False");}
                        }
                    }
                    map.put("users",user);
                    result.add(map);
                }
            }
        }
        return Result.ok(result);
    }
}
