package edu.sustech.auth.controller;

import edu.sustech.auth.service.SysFundingService;
import edu.sustech.common.result.Result;
import edu.sustech.model.system.SysFunding;
import edu.sustech.model.system.SysGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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
    @ApiOperation(value = "根据经费id获取组")
    @GetMapping("getGroupByFund")
    public Result getGroupByFund(@RequestParam Long fundId){
        List<SysFunding> foudingList = sysFundingService.list();
        List<Map<String, Object>> result = foudingList.stream()
                .filter(f -> Objects.equals(f.getFundingId(), fundId))
                .map(f -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("Complete", f.getStatus());
                    map.put("group", f.getGroupName());
                    map.put("total", f.getTotalAmount());
                    map.put("cost", f.getCost());
                    map.put("left", f.getRemainAmount());
                    map.put("percent",(f.getRemainAmount()*100/f.getTotalAmount()));
                    return map;
                })
                .collect(Collectors.toList());
        return Result.ok(result);
    }
    @ApiOperation(value = "添加经费")
    @PostMapping("add")
    public Result add(@RequestBody @Validated SysFunding sysFunding){
        sysFundingService.save(sysFunding);
        return Result.ok();
    }

    @ApiOperation(value = "更新经费")
    @PostMapping("update")
    public Result updateById(@RequestBody SysFunding funding) {
        boolean is_success = sysFundingService.updateById(funding);
        if (is_success){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "删除经费")
    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable String id){
        sysFundingService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "根据组id和经费id删除经费")
    @DeleteMapping ("deleteGroupFund")
    public Result deleteGroupFund(@RequestParam Long groupId, @RequestParam Long fundingId){

        List<SysFunding> foudingList = sysFundingService.list();
        List<Map<String, Object>> deletedFundingList = foudingList.stream()
                .filter(f -> Objects.equals(f.getGroupId(), groupId) && Objects.equals(f.getFundingId(), fundingId) && f.getStatus().equals("COMPLETE"))
                .map(f -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("Complete", f.getStatus());
                    map.put("fundId", f.getFundingId());
                    map.put("fundName", f.getFundingName());
                    map.put("total", f.getTotalAmount());
                    map.put("cost", f.getCost());
                    map.put("left", f.getRemainAmount());
                    map.put("percent",(f.getRemainAmount()*100/f.getTotalAmount()));
                    sysFundingService.removeById(f.getId());
                    return map;
                })
                .collect(Collectors.toList());
        return Result.ok(deletedFundingList);
    }
    @ApiOperation(value = "根据组名和经费id删除经费")
    @DeleteMapping ("deleteFundGroup")
    public Result deleteGroupFund(@RequestParam String groupName, @RequestParam Long fundingId){

        List<SysFunding> foudingList = sysFundingService.list();
        List<Map<String, Object>> deletedFundingList = foudingList.stream()
                .filter(f -> Objects.equals(f.getGroupName(), groupName) && Objects.equals(f.getFundingId(), fundingId) && f.getStatus().equals("COMPLETE"))
                .map(f -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("Complete", f.getStatus());
                    map.put("group", f.getGroupName());
                    map.put("total", f.getTotalAmount());
                    map.put("cost", f.getCost());
                    map.put("left", f.getRemainAmount());
                    map.put("percent",(f.getRemainAmount()*100/f.getTotalAmount()));
                    sysFundingService.removeById(f.getId());
                    return map;
                })
                .collect(Collectors.toList());
        return Result.ok(deletedFundingList);
    }

    @ApiOperation(value = "根据组名获取经费信息")
    @GetMapping ("find/{groupId}")
    public Result getFundInfoByGroup(@PathVariable String groupId) {
        List<SysFunding> foudingList = sysFundingService.list();
        List<Map<String, Object>> result = foudingList.stream()
                .filter(f -> Objects.equals(f.getGroupId().toString(), groupId))
                .map(f -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("Complete", f.getStatus());
                    map.put("fundId", f.getFundingId());
                    map.put("fundName", f.getFundingName());
                    map.put("total", f.getTotalAmount());
                    map.put("cost", f.getCost());
                    map.put("left", f.getRemainAmount());
                    map.put("percent",(f.getRemainAmount()*100/f.getTotalAmount()));
                    return map;
                })
                .collect(Collectors.toList());
        return Result.ok(result);
    }
    @ApiOperation(value = "根据经费获取组名")
    @GetMapping ("findGroup/{fundingId}")
    public Result getGroupInfoByFunding(@PathVariable Integer fundingId) {
        List<SysFunding> foudingList = sysFundingService.list();
        Integer result = 1;
        for (SysFunding sysFunding : foudingList) {
            System.out.println("test");
            if (Objects.equals(sysFunding.getFundingId(), fundingId)) {
                result= Math.toIntExact(sysFunding.getGroupId());
            }
        }
        return Result.ok(result);
    }
}