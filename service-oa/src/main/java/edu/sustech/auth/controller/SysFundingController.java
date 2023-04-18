package edu.sustech.auth.controller;

import edu.sustech.auth.service.SysFundingService;
import edu.sustech.auth.service.SysGroupService;
import edu.sustech.common.result.Result;
import edu.sustech.model.system.SysFunding;
import edu.sustech.model.system.SysGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.util.Json;
import lombok.var;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
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
        Map<Long,SysFunding> result1=new HashMap<>();
        for (SysFunding f:list) {
            if(!result1.containsKey(f.getFundingId())){
                result1.put(f.getFundingId(),f);
            }else{
                SysFunding temp=result1.get(f.getFundingId());
                temp.setRemainAmount(temp.getRemainAmount()+f.getRemainAmount());
                temp.setTotalAmount(temp.getTotalAmount()+f.getTotalAmount());
                temp.setCost(temp.getCost()+f.getCost());
            }
        }
        List<Map<String, Object>> result=new ArrayList<>();
        for(SysFunding f:result1.values()){
            Map<String, Object> map = new HashMap<>();
            map.put("key", f.getId());
            map.put("id", f.getFundingId());
            map.put("name", f.getFundingName());
            int[] dataRange = new int[2];
            dataRange[0] = 2019;
            dataRange[1] = 2021;
            map.put("dataRange", dataRange);
            map.put("totalNum", f.getTotalAmount());
            map.put("leftNum", f.getRemainAmount());
            if(f.getTotalAmount()!=0){
                map.put("percent",(f.getRemainAmount()*100/f.getTotalAmount()));
            }else{
                map.put("percent",0);
            }
            map.put("state", f.getStatus());
            map.put("leftDay",100);
            map.put("disabled", f.getIsDeleted());
            result.add(map);
        }
        return Result.ok(result);

    }
    @ApiOperation(value = "根据经费id获取组")
    @GetMapping("getGroupByFund")
    public Result getGroupByFund(@RequestParam(value = "id") Long fundId){
        System.out.println(fundId);
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
                    if(f.getTotalAmount()!=0){
                        map.put("percent",(f.getRemainAmount()*100/f.getTotalAmount()));
                    }else{
                        map.put("percent",0);
                    }
                    return map;
                })
                .collect(Collectors.toList());
        Map<String,Map<String,Object>>result1=new HashMap<>();
        for(Map<String,Object> map:result){
            if (!result1.containsKey(map.get("group").toString())){
                result1.put(map.get("group").toString(),map);
            }else{
                Map<String,Object> map1=result1.get(map.get("group").toString());
                map1.put("total",(long)map1.get("total")+(long)map.get("total"));
                map1.put("cost",Long.parseLong(map1.get("cost").toString())+Long.parseLong(map.get("cost").toString()));
                map1.put("left",(long)map1.get("total")-(long)map.get("cost"));
                if(Integer.parseInt(map1.get("total").toString())!=0){
                    map1.put("percent",(Integer.parseInt(map1.get("left").toString())*100/Integer.parseInt(map1.get("total").toString())));
                }else{
                    map1.put("percent",0);
                }
            }
        }
        return Result.ok(result1.values());
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
        List<SysFunding> foudingList1 = sysFundingService.list();
        for(SysFunding f:foudingList1){
            if(Objects.equals(f.getGroupId(), groupId) && Objects.equals(f.getFundingId(), fundingId)&&f.getCost()==0){
                sysFundingService.removeById(f.getId());
            }
        }
        List<SysFunding> foudingList = sysFundingService.list();
        List<Map<String, Object>> result = foudingList.stream()
                .filter(f -> Objects.equals(f.getFundingId(), fundingId))
                .map(f -> {
                    Map<String, Object> map = new HashMap<>();
                    if(f.getStatus().equals("complete")){
                        map.put("complete","True");
                    }
                    map.put("group", f.getGroupName());
                    map.put("total", f.getTotalAmount());
                    map.put("cost", f.getCost());
                    map.put("left", f.getRemainAmount());
                    if(f.getTotalAmount()!=0){
                        map.put("percent",(f.getRemainAmount()*100/f.getTotalAmount()));
                    }else{
                        map.put("percent",0);
                    }
                    return map;
                })
                .collect(Collectors.toList());
        Map<String,Map<String,Object>>result1=new HashMap<>();
        for(Map<String,Object> map:result){
            if (!result1.containsKey(map.get("group").toString())){
                result1.put(map.get("group").toString(),map);
            }else{
                Map<String,Object> map1=result1.get(map.get("group").toString());
                map1.put("total",(long)map1.get("total")+(long)map.get("total"));
                map1.put("cost",Long.parseLong(map1.get("cost").toString())+Long.parseLong(map.get("cost").toString()));
                map1.put("left",(long)map1.get("total")-(long)map.get("cost"));
                if(Integer.parseInt(map1.get("total").toString())!=0){
                    map1.put("percent",(Integer.parseInt(map1.get("left").toString())*100/Integer.parseInt(map1.get("total").toString())));
                }else{
                    map1.put("percent",0);
                }
            }
        }
        return Result.ok(result1.values());
        /*List<Map<String, Object>> deletedFundingList = foudingList.stream()
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
                    if(f.getTotalAmount()!=0){
                        map.put("percent",(f.getRemainAmount()*100/f.getTotalAmount()));
                    }else{
                        map.put("percent",0);
                    }
                    sysFundingService.removeById(f.getId());
                    return map;
                })
                .collect(Collectors.toList());
        return Result.ok(deletedFundingList);*/
    }
    @ApiOperation(value = "根据组名和经费id删除经费")
    @PostMapping ("deleteFundGroup")
    public Result deleteFundGroup(@RequestBody Map<String,String> GROUPANDID ){
        System.out.println(GROUPANDID);
        Map<String,String>NAMEMAP =  GROUPANDID;
        String groupName = NAMEMAP.get("groupName");
        Long fundId = Long.valueOf(NAMEMAP.get("fundId"));
        List<SysFunding> foudingList1 = sysFundingService.list();
        for(SysFunding f:foudingList1){
            if(Objects.equals(f.getGroupName(), groupName) && Objects.equals(f.getFundingId(), fundId)&&f.getCost()==0){
                sysFundingService.removeById(f.getId());
            }
        }
        List<SysFunding> foudingList = sysFundingService.list();
        List<Map<String, Object>> result = foudingList.stream()
                .filter(f -> Objects.equals(f.getFundingId(), fundId))
                .map(f -> {
                    Map<String, Object> map = new HashMap<>();
                    if(f.getStatus().equals("complete")){
                        map.put("complete","True");
                    }
                    map.put("group", f.getGroupName());
                    map.put("total", f.getTotalAmount());
                    map.put("cost", f.getCost());
                    map.put("left", f.getRemainAmount());
                    if(f.getTotalAmount()!=0){
                        map.put("percent",(f.getRemainAmount()*100/f.getTotalAmount()));
                    }else{
                        map.put("percent",0);
                    }
                    return map;
                })
                .collect(Collectors.toList());
        Map<String,Map<String,Object>>result1=new HashMap<>();
        for(Map<String,Object> map:result){
            if (!result1.containsKey(map.get("group").toString())){
                result1.put(map.get("group").toString(),map);
            }else{
                Map<String,Object> map1=result1.get(map.get("group").toString());
                map1.put("total",(long)map1.get("total")+(long)map.get("total"));
                map1.put("cost",Long.parseLong(map1.get("cost").toString())+Long.parseLong(map.get("cost").toString()));
                map1.put("left",(long)map1.get("total")-(long)map.get("cost"));
                if(Integer.parseInt(map1.get("total").toString())!=0){
                    map1.put("percent",(Integer.parseInt(map1.get("left").toString())*100/Integer.parseInt(map1.get("total").toString())));
                }else{
                    map1.put("percent",0);
                }
            }
        }
        return Result.ok(result1.values());
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
                    if(f.getTotalAmount()!=0){
                        map.put("percent",(f.getRemainAmount()*100/f.getTotalAmount()));
                    }else{
                        map.put("percent",0);
                    }
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
        List<Map<Object,Object>>FUNDDETAIL= (List<Map<Object, Object>>) fundDetail.get("detail");
        System.out.println(FUNDDETAIL.size());
        List<SysFunding> foudingList = sysFundingService.list();
        List<SysFunding> foudingLists = new ArrayList<>();
        for (SysFunding f : foudingList) {
            if (Objects.equals(f.getGroupName(), groupName)&&Objects.equals(f.getFundingId(), fundingId)) {
                foudingLists.add(f);
            }
        }
        List<Map<Object, Object>> result =new ArrayList<>();
        int count=0;
        if(foudingLists.size()<=FUNDDETAIL.size()){
            for(SysFunding f:foudingLists){
                result.add(FUNDDETAIL.get(count));
                f.setCategory1((String) FUNDDETAIL.get(count).get("category1"));
                f.setCategory2((String) FUNDDETAIL.get(count).get("category2"));
                f.setTotalAmount((long)(int) FUNDDETAIL.get(count).get("total"));
                f.setRemainAmount((long)(int) FUNDDETAIL.get(count).get("left"));
                f.setCost((long)(int) FUNDDETAIL.get(count).get("cost"));
                sysFundingService.updateById(f);
                count++;
            }
            for (int i=0;i<FUNDDETAIL.size()-foudingLists.size();i++){
                Map<Object, Object> map = FUNDDETAIL.get(i+count);
                SysFunding s=new SysFunding();
                s.setCategory1((String) FUNDDETAIL.get(i+count).get("category1"));
                s.setCategory2((String) FUNDDETAIL.get(i+count).get("category2"));
                s.setTotalAmount((long)(int) FUNDDETAIL.get(i+count).get("total"));
                s.setRemainAmount((long)(int) FUNDDETAIL.get(i+count).get("left"));
                s.setCost((long)(int) FUNDDETAIL.get(i+count).get("cost"));
                s.setFundingId(foudingList.get(0).getFundingId());
                s.setFundingName(foudingList.get(0).getFundingName());
                s.setStatus(foudingList.get(0).getStatus());
                s.setGroupName(foudingList.get(0).getGroupName());
                s.setGroupId(foudingList.get(0).getGroupId());
                s.setEndTime(foudingList.get(0).getEndTime());
                s.setStartTime(foudingList.get(0).getStartTime());
                sysFundingService.save(s);
                result.add(map);
            }
        }else{
            int count2=0;
            for(SysFunding f:foudingLists){
                if(count2<FUNDDETAIL.size()){
                    result.add(FUNDDETAIL.get(count));
                    f.setCategory1((String) FUNDDETAIL.get(count).get("category1"));
                    f.setCategory2((String) FUNDDETAIL.get(count).get("category2"));
                    f.setTotalAmount((long)(int) FUNDDETAIL.get(count).get("total"));
                    f.setRemainAmount((long)(int) FUNDDETAIL.get(count).get("left"));
                    f.setCost((long)(int) FUNDDETAIL.get(count).get("cost"));
                    sysFundingService.updateById(f);
                    count++;
                }else{
                    sysFundingService.removeById(f.getId());
                }
            }
        }
        /*for(SysFunding f:foudingLists){
            result.add(FUNDDETAIL.get(count));
            f.setCategory1((String) FUNDDETAIL.get(count).get("category1"));
            f.setCategory2((String) FUNDDETAIL.get(count).get("category2"));
            f.setTotalAmount((long)(int) FUNDDETAIL.get(count).get("total"));
            f.setRemainAmount((long)(int) FUNDDETAIL.get(count).get("left"));
            f.setCost((long)(int) FUNDDETAIL.get(count).get("cost"));
            sysFundingService.updateById(f);
            count++;
        }
        for (int i=0;i<FUNDDETAIL.size()-foudingLists.size();i++){
            Map<Object, Object> map = FUNDDETAIL.get(i+count);
            SysFunding s=new SysFunding();
            s.setCategory1((String) FUNDDETAIL.get(i+count).get("category1"));
            s.setCategory2((String) FUNDDETAIL.get(i+count).get("category2"));
            s.setTotalAmount((long)(int) FUNDDETAIL.get(i+count).get("total"));
            s.setRemainAmount((long)(int) FUNDDETAIL.get(i+count).get("left"));
            s.setCost((long)(int) FUNDDETAIL.get(i+count).get("cost"));
            s.setFundingId(foudingList.get(0).getFundingId());
            s.setFundingName(foudingList.get(0).getFundingName());
            s.setStatus(foudingList.get(0).getStatus());
            s.setGroupName(foudingList.get(0).getGroupName());
            s.setGroupId(foudingList.get(0).getGroupId());
            s.setEndTime(foudingList.get(0).getEndTime());
            s.setStartTime(foudingList.get(0).getStartTime());
            sysFundingService.save(s);
            result.add(map);
        }*/
        /*for (SysFunding f : foudingLists) {
            if (Objects.equals(f.getGroupName(), groupName)&&Objects.equals(f.getFundingId(), fundingId)) {
                    List<Map<Object,Object>> RE=new ArrayList<>();
                    for(int i=0;i<FUNDDETAIL.size();i++){
                        if(FUNDDETAIL.get(i).get("new").equals("False")){
                            Map<Object, Object> map = FUNDDETAIL.get(i);
                            System.out.println(FUNDDETAIL.get(i).keySet());
                            System.out.println(FUNDDETAIL.get(i).values());
                            System.out.println(FUNDDETAIL.get(i).get("category1"));
                            System.out.println(FUNDDETAIL.get(i).get("category2"));
                            System.out.println(FUNDDETAIL.get(i).get("total"));
                            System.out.println(FUNDDETAIL.get(i).get("left"));
                            System.out.println(FUNDDETAIL.get(i).get("cost"));
                            f.setCategory1((String) FUNDDETAIL.get(i).get("category1"));
                            f.setCategory2((String) FUNDDETAIL.get(i).get("category2"));
                            f.setTotalAmount((long)(int) FUNDDETAIL.get(i).get("total"));
                            f.setRemainAmount((long)(int) FUNDDETAIL.get(i).get("left"));
                            f.setCost((long)(int) FUNDDETAIL.get(i).get("cost"));
                            sysFundingService.updateById(f);
                            result.add(map);
                        }else{
                            Map<Object, Object> map = FUNDDETAIL.get(i);
                            System.out.println(FUNDDETAIL.get(i).keySet());
                            System.out.println(FUNDDETAIL.get(i).values());
                            System.out.println(FUNDDETAIL.get(i).get("category1"));
                            System.out.println(FUNDDETAIL.get(i).get("category2"));
                            System.out.println(FUNDDETAIL.get(i).get("total"));
                            System.out.println(FUNDDETAIL.get(i).get("left"));
                            System.out.println(FUNDDETAIL.get(i).get("cost"));
                            SysFunding s=new SysFunding();
                            s.setCategory1((String) FUNDDETAIL.get(i).get("category1"));
                            s.setCategory2((String) FUNDDETAIL.get(i).get("category2"));
                            s.setTotalAmount((long)(int) FUNDDETAIL.get(i).get("total"));
                            s.setRemainAmount((long)(int) FUNDDETAIL.get(i).get("left"));
                            s.setCost((long)(int) FUNDDETAIL.get(i).get("cost"));
                            s.setFundingId(f.getFundingId());
                            s.setFundingName(f.getFundingName());
                            s.setStatus(f.getStatus());
                            s.setGroupName(f.getGroupName());
                            s.setGroupId(f.getGroupId());
                            s.setEndTime(f.getEndTime());
                            s.setStartTime(f.getStartTime());
                            sysFundingService.save(s);
                            result.add(map);
                        }
                    }
                }
        }*/
        return Result.ok(result);
    }

    @ApiOperation(value = "根据id获取经费信息")
    @GetMapping("getFundStatistics")
    private Result getFundStatistics(@RequestParam(value = "id") Long fundId){

        return Result.ok();
    }

}