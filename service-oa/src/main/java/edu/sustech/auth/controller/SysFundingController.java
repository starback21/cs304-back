package edu.sustech.auth.controller;

import edu.sustech.auth.service.SysFundingService;
import edu.sustech.common.result.Result;
import edu.sustech.model.system.SysFunding;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @ApiOperation(value = "根据组名获取经费信息")
    @GetMapping ("find/{groupId}")
    public Result getFundInfoByGroup(String groupId) {
        List<SysFunding> foudingList = sysFundingService.list();
        List<SysFunding> result = new ArrayList<>();
        for (SysFunding sysFunding : foudingList) {
            System.out.println(sysFunding.getGroupId());
            if (Objects.equals(sysFunding.getGroupId().toString(), groupId)) {
                result.add(sysFunding);
            }
        }
        return Result.ok(result);
    }
    @ApiOperation(value = "根据经费获取组名")
    @GetMapping ("findGroup/{fundingId}")
    public Result getGroupInfoByFunding(Integer fundingId) {
        List<SysFunding> foudingList = sysFundingService.list();
        Integer result = 1;
        for (SysFunding sysFunding : foudingList) {
            System.out.println("test");
            if (Objects.equals(sysFunding.getFundingId(), fundingId)) {
                result= sysFunding.getGroupId();
            }
        }
        return Result.ok(result);
    }
}