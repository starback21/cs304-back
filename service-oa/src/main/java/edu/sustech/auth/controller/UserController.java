package edu.sustech.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.sustech.auth.service.SysApplicationService;
import edu.sustech.auth.service.SysGroupService;
import edu.sustech.common.handler.SpecialException;
import edu.sustech.common.result.Result;
import edu.sustech.model.system.SysApplication;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/user/application")
public class UserController {
    @Autowired
    SysApplicationService applicationService;
    @Autowired
    SysGroupService groupService;

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
        return Result.ok();
    }
}
