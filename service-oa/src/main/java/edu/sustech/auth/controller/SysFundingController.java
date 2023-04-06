package edu.sustech.auth.controller;

import edu.sustech.auth.service.SysFundingService;
import edu.sustech.common.result.Result;
import edu.sustech.model.system.SysFunding;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Api(tags = "经费管理接口")
@RestController
@CrossOrigin
@RequestMapping("/admin/system/sysFunding")

public class SysFundingController {
    @Autowired
    private SysFundingService sysFundingService;

    @ApiOperation(value = "获取所有经费")
    @GetMapping("findAll")
    public Result getAll(){
        List<SysFunding> list = sysFundingService.list();
        return Result.ok(list);
    }

    @ApiOperation(value = "添加经费")
    @PostMapping("add")
    public Result add(@RequestBody @Validated SysFunding sysFunding){
        sysFundingService.save(sysFunding);
        return Result.ok();
    }

    @ApiOperation(value = "更新经费")
    @PostMapping("update")
    public Result update(@RequestBody @Validated SysFunding sysFunding){
        sysFundingService.updateById(sysFunding);
        return Result.ok();
    }

    @ApiOperation(value = "删除经费")
    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable String id){
        sysFundingService.removeById(id);
        return Result.ok();
    }
}