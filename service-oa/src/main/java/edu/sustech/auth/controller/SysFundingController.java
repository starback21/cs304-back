package edu.sustech.auth.controller;

import edu.sustech.auth.service.SysFundingService;
import edu.sustech.auth.service.SysGroupService;
import edu.sustech.common.result.Result;
import edu.sustech.model.system.SysFunding;
import edu.sustech.model.system.SysGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.util.Json;
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
    @Autowired
    private SysGroupService sysGroupService;

    @ApiOperation(value = "获取所有经费")
    @GetMapping("getFunds")
    public Result getAll(){
        List<SysFunding> list = sysFundingService.list();
        List<Map<String, Object>> result = list.stream()
                .map(f -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("key", f.getId().toString());
                    map.put("id", f.getFundingId());
                    map.put("name", f.getFundingName());
                    int[] dataRange = new int[2];
                    dataRange[0] = 2019;
                    dataRange[1] = 2021;
                    map.put("dataRange", dataRange);
                    map.put("totalNum", f.getTotalAmount());
                    map.put("leftNum", f.getRemainAmount());
                    map.put("percent",(f.getRemainAmount()*100/f.getTotalAmount()));
                    map.put("state", f.getStatus());
                    map.put("leftDay",100);
                    map.put("disabled", f.getIsDeleted());
                    return map;
                })
                .collect(Collectors.toList());
        return Result.ok(result);
    }
    @ApiOperation(value = "根据经费id获取组")
    @GetMapping("getGroupByFund")
    public Result getGroupByFund(@RequestParam(value = "id") Long fundId){
        Long fundId1=Long.parseLong(fundId.toString());
        List<SysFunding> foudingList = sysFundingService.list();
        List<Map<String, Object>> result = foudingList.stream()
                .filter(f -> Objects.equals(f.getFundingId(), fundId1))
                .map(f -> {
                    Map<String, Object> map = new HashMap<>();
                    if(f.getStatus().equals("complete")){
                        map.put("complete","True");
                    }
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
    @PostMapping("delete/{id}")
    public Result delete(@PathVariable String id){
        SysFunding sysFunding = sysFundingService.getById(id);
        sysFunding.setIsDeleted(1);
        sysFundingService.updateById(sysFunding);
        return Result.ok();
    }

    @ApiOperation(value = "根据组id和经费id删除经费")
    @PostMapping ("deleteGroupFund")
    public Result deleteGroupFund(@RequestBody Map<String,String> GROUPANDID){
        Long groupId = Long.valueOf(GROUPANDID.get("groupId"));
        Long fundingId = Long.valueOf(GROUPANDID.get("fundingId"));
        List<SysFunding> foudingList = sysFundingService.list();
        List<Map<String, Object>> deletedFundingList = foudingList.stream()
                .filter(f -> Objects.equals(f.getGroupId(), groupId) && Objects.equals(f.getFundingId(), fundingId) && f.getCost()==0)
                .map(f -> {
                    Map<String, Object> map = new HashMap<>();
                    if(f.getStatus().equals("complete")){
                        map.put("complete","True");
                    }
                    map.put("fundId", f.getFundingId());
                    map.put("fund", f.getFundingName());
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
    @PostMapping ("deleteFundGroup")
    public Result deleteFundGroup(@RequestBody Map<String,String> GROUPANDID ){
        System.out.println(GROUPANDID);
        Map<String,String>NAMEMAP =  GROUPANDID;
        String groupName = NAMEMAP.get("groupName");
        Long fundId = Long.valueOf(NAMEMAP.get("fundId"));
        List<SysFunding> foudingList = sysFundingService.list();
        List<Map<String, Object>> deletedFundingList = foudingList.stream()
                .filter(f -> Objects.equals(f.getGroupName(), groupName) && Objects.equals(f.getFundingId(), fundId) && f.getCost()==0)
                .map(f -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("complete", f.getStatus());
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
    @GetMapping ("getFundInfoByGroup")
    public Result getFundInfoByGroup(@RequestParam String groupId) {
        List<SysFunding> foudingList = sysFundingService.list();
        List<Map<String, Object>> result = foudingList.stream()
                .filter(f -> Objects.equals(f.getGroupId().toString(), groupId))
                .map(f -> {
                    Map<String, Object> map = new HashMap<>();
                    if(f.getStatus().equals("complete")){
                        map.put("complete","True");
                    }
                    map.put("fundId", f.getFundingId());
                    map.put("fund", f.getFundingName());
                    map.put("total", f.getTotalAmount());
                    map.put("cost", f.getCost());
                    map.put("left", 999);
                    map.put("percent",(f.getRemainAmount()*100/f.getTotalAmount()));
                    return map;
                })
                .collect(Collectors.toList());
        return Result.ok(result);
    }
    @ApiOperation(value = "根据经费获取组名")
    @GetMapping ("findGroup/{fundingId}")
    public Result getGroupInfoByFunding(@PathVariable Long fundingId) {
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
    @ApiOperation(value = "根据组添加经费")
    @GetMapping ("addGroupsToFund")
    public Result addGroupsToFund(@RequestBody Map<String,Object> GROUPANDID){
        String fundId = GROUPANDID.get("fundId").toString();
        List<String> groups = (List<String>) GROUPANDID.get("groups");
        SysFunding tem=null;
        List<SysFunding> foudingList = sysFundingService.list();
        List<SysGroup> groupList = sysGroupService.list();
        List<Map<String, Object>>result=new ArrayList<>();
        SysFunding sysFunding = foudingList.stream()
                .filter(f -> Objects.equals(f.getFundingId(),Long.parseLong(fundId)))
                .findFirst()
                .orElse(null);
        if (sysFunding == null) {
            return Result.fail("经费不存在");
        }
        for (String group : groups) {
            tem = new SysFunding();
            tem.setFundingId(Long.parseLong(fundId));
            tem.setFundingName(sysFunding.getFundingName());
            tem.setGroupId(Long.parseLong(group));
            tem.setGroupName(sysGroupService.getById(Long.parseLong(group)).getGroupName());
            tem.setTotalAmount(sysFunding.getTotalAmount());
            tem.setRemainAmount(sysFunding.getRemainAmount());
            tem.setCost(0L);
            tem.setStatus(sysFunding.getStatus());
            tem.setEndTime(sysFunding.getEndTime());
            tem.setStartTime(sysFunding.getStartTime());
            Map<String, Object> map = new HashMap<>();
            if(sysFunding.getStatus().equals("complete")){
                map.put("complete","True");
            }
            map.put("group", tem.getGroupName());
            map.put("total", tem.getTotalAmount());
            map.put("cost", tem.getCost());
            map.put("left", tem.getRemainAmount());
            map.put("percent",(tem.getRemainAmount()*100/tem.getTotalAmount()));
            result.add(map);
            sysFundingService.save(tem);
        }
        return Result.ok(result);
    }
    @ApiOperation(value = "根据经费id和组id获取经费详情")
    @GetMapping ("getFundDetailByGroup")
    public Result getFundInfoByGroupAndFund(@RequestParam String groupId,@RequestParam(value ="fundId") String fundingId) {
        System.out.println(groupId);
        System.out.println(fundingId);
        List<SysFunding> foudingList = sysFundingService.list();
        List<Map<String, Object>> result = foudingList.stream()
                .filter(f -> Objects.equals(f.getGroupName(), groupId)&&Objects.equals(f.getFundingId().toString(), fundingId))
                .map(f -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("category1", f.getCategory1());
                    map.put("category2", f.getCategory2());
                    map.put("total", f.getTotalAmount());
                    map.put("cost", f.getCost());
                    map.put("left", f.getRemainAmount());
                    map.put("new","False");
                    return map;
                })
                .collect(Collectors.toList());
        return Result.ok(result);
    }
    @ApiOperation(value = "根据经费id和组id修改经费详情")
    @PostMapping ("modifyGroupFundDetail")
    public Result modifyGroupFundDetail(@RequestBody Map<String, Object> fundDetail) {
        System.out.println(fundDetail.get("groupId"));
        String groupName = (String) fundDetail.get("groupId");
        System.out.println(fundDetail.get("fundId"));
        Long fundingId = Long.parseLong(fundDetail.get("fundId").toString());
        System.out.println(fundDetail.get("detail"));
        List<Map<String,String>>FUNDDETAIL= (List<Map<String, String>>) fundDetail.get("detail");
        System.out.println(FUNDDETAIL);
        List<SysFunding> foudingList = sysFundingService.list();
        List<Map<Object, Object>> result = foudingList.stream()
                .filter(f -> Objects.equals(f.getGroupName(), groupName)&&Objects.equals(f.getFundingId(),fundingId))
                .map(f -> {
                    Map<Object, Object> map = new HashMap<>();
                    map.put("category1", f.getCategory1());
                    map.put("category2", f.getCategory2());
                    map.put("total", f.getTotalAmount());
                    map.put("cost", f.getCost());
                    map.put("left", f.getRemainAmount());
                    map.put("new","False");
                    System.out.println(FUNDDETAIL.get(0).keySet());
                    System.out.println(FUNDDETAIL.get(0).values());
                    System.out.println(FUNDDETAIL.get(0).get("category1"));
                    System.out.println(FUNDDETAIL.get(0).get("category2"));
                    System.out.println(FUNDDETAIL.get(0).get("total"));
                    System.out.println(FUNDDETAIL.get(0).get("left"));
                    System.out.println(FUNDDETAIL.get(0).get("cost"));
                    f.setCategory1(FUNDDETAIL.get(0).get("category1"));
                    f.setCategory2(FUNDDETAIL.get(0).get("category2"));
                    f.setTotalAmount(Long.parseLong(FUNDDETAIL.get(0).get("total")));
                    f.setRemainAmount(Long.parseLong(FUNDDETAIL.get(0).get("left")));
                    f.setCost(Long.parseLong(FUNDDETAIL.get(0).get("cost")));
                    sysFundingService.updateById(f);
                    return map;
                })
                .collect(Collectors.toList());
        return Result.ok();
    }


}