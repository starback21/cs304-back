package edu.sustech.auth.controller;


import edu.sustech.auth.service.SysGroupService;
import edu.sustech.model.system.SysApplication;
import edu.sustech.auth.service.SysApplicationService;
import edu.sustech.common.result.Result;
import edu.sustech.re.system.PageApplication;
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
 * 申请 前端控制器
 * </p>
 *
 * @author starback
 * @since 2023-04-09
 */
@Api(tags = "申请管理接口")
@RestController
@RequestMapping("/admin/system/application")
public class SysApplicationController {
    @Autowired
    private SysApplicationService service;
    @Autowired
    private SysGroupService groupService;

    @ApiOperation(value = "添加申请")
    @PostMapping("/createApplication")
    public Result save(@RequestBody SysApplication application){
        boolean is_success = service.save(application);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "获取申请")
    @GetMapping("/getApplications")
    public Result<Map<String, Object>> getApplications(@RequestParam(value = "page") int page){
        List<SysApplication> list = service.list();
        List<PageApplication> data = new ArrayList<>();
        int index = 0;
        for (SysApplication a : list){
            index++;
            if (index > (page - 1) * 3 && index <= page * 3){
                PageApplication tmp = new PageApplication();
                tmp.setId(Math.toIntExact(a.getId()));
                tmp.setName(a.getTitle());
                tmp.setState(a.getState());
                String name = groupService.getById(a.getGroup()).getGroupName();
                tmp.setGroup(name);
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
}

