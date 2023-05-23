package edu.sustech.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.sustech.auth.service.*;
import edu.sustech.auth.service.SysUserRoleService;
import edu.sustech.common.result.Result;
import edu.sustech.model.system.*;
import edu.sustech.re.system.PageFund;
import edu.sustech.re.system.PageGroup;
import edu.sustech.re.system.PageGroupFund;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api(tags = "经费管理接口")
@RestController
@CrossOrigin
@RequestMapping("/admin/system/sysFunding")
public class SysFundingController {
    @Autowired
    private SysFundingService sysFundingService;
    @Autowired
    private SysGroupService sysGroupService;
    @Autowired
    private SysGroupFundService sysGroupFundService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysGroupFundDetailService sysGroupFundDetailService;
    @Autowired
    private SysUserService sysUserService;
    @ApiOperation(value = "获取所有经费")
    @GetMapping("getFunds")
    public Result getFunds() {
        List<SysFunding> sysFundings = sysFundingService.list();
        List<PageFund>result= new ArrayList<>();
        for(SysFunding sysFunding:sysFundings){
            PageFund pageFund = new PageFund();
            pageFund.setKey(sysFunding.getId());
            pageFund.setId(sysFunding.getFundingId());
            pageFund.setName(sysFunding.getFundingName());
            pageFund.setTotalNum(sysFunding.getTotalAmount());
            pageFund.setLeftNum(sysFunding.getRemainAmount());
            Map<Integer,Integer>map = new HashMap<>();
            int[] dataRange = new int[2];
            dataRange[0] = 2019;
            dataRange[1] = 2021;
            map.put(dataRange[0],dataRange[1]);
            pageFund.setDataRange(map);
            if(sysFunding.getTotalAmount()!=0){
                pageFund.setPercent((int) (sysFunding.getRemainAmount()*100/sysFunding.getTotalAmount()));
            }
            else{
                pageFund.setPercent(0);
            }
            pageFund.setState(sysFunding.getStatus());
            pageFund.setLeftDay(100);
            pageFund.setDisabled(sysFunding.getIsDeleted());
            result.add(pageFund);

        }
        return Result.ok(result);
    }
    @ApiOperation(value = "根据经费id获取组")
    @GetMapping("getGroupByFund")
    public Result getGroupByFund(@RequestParam(value = "id") Long fundId){
        List<SysGroupFund> sysGroupFunds = sysGroupFundService.list();

        List<PageGroupFund>result = new ArrayList<>();
        for(SysGroupFund sysGroupFund:sysGroupFunds){
            if(sysGroupFund.getFundingId().equals(fundId)){
                PageGroupFund pageGroupFund = new PageGroupFund();
                if(sysGroupFund.getStatus().equals("complete")){
                    pageGroupFund.setComplete("True");
                }
                else{
                    pageGroupFund.setComplete("False");
                }
                pageGroupFund.setGroup(sysGroupFund.getGroupName());
                pageGroupFund.setTotal(sysGroupFund.getTotalAmount());
                pageGroupFund.setCost(sysGroupFund.getCost());
                pageGroupFund.setLeft(sysGroupFund.getRemainAmount());
                if(sysGroupFund.getTotalAmount()!=0){
                    pageGroupFund.setPercent((int) (pageGroupFund.getCost()*100/sysGroupFund.getTotalAmount()));
                }
                else{
                    pageGroupFund.setPercent(100);
                }
                result.add(pageGroupFund);
            }
        }
        return Result.ok(result);
    }
    @ApiOperation(value = "根据组名获取经费信息")
    @GetMapping ("getFundInfoByGroup")
    public Result getFundInfoByGroup(@RequestParam String groupId) {
        List<SysGroupFund> sysGroupFunds = sysGroupFundService.list();
        Map<String,String>result = new HashMap<>();
        for(SysGroupFund sysGroupFund:sysGroupFunds){
            if(sysGroupFund.getGroupId().toString().equals(groupId)){
                if(sysGroupFund.getStatus().equals("complete")){
                    result.put("complete","True");
                }
                else{
                    result.put("complete","False");
                }
                result.put("fundId",sysGroupFund.getGroupName());
                result.put("fund",sysGroupFund.getTotalAmount().toString());
                result.put("total",sysGroupFund.getTotalAmount().toString());
                result.put("cost",sysGroupFund.getCost().toString());
                result.put("left",sysGroupFund.getRemainAmount().toString());
                if(sysGroupFund.getTotalAmount()!=0){
                    result.put("percent",String.valueOf(sysGroupFund.getCost()*100/sysGroupFund.getTotalAmount()));
                }else{
                    result.put("percent","100");
                }

                break;
            }
        }
        return Result.ok(result);
    }
    @ApiOperation(value = "根据组id和经费id删除经费")
    @PostMapping ("deleteGroupFund")
    public Result deleteGroupFund(@RequestBody Map<String,String> GROUPANDID){
        String groupId = GROUPANDID.get("groupId");
        String fundId = GROUPANDID.get("fundId");
        List<SysGroupFund> sysGroupFunds = sysGroupFundService.list();
        List<SysFunding>sysFunding= sysFundingService.list();
        SysFunding sysFunding1 = null;
        for(SysFunding sysFunding2:sysFunding){
            if(sysFunding2.getFundingId().toString().equals(fundId)){
                sysFunding1 = sysFunding2;
                break;
            }
        }
        for(SysGroupFund sysGroupFund:sysGroupFunds){
            if(sysGroupFund.getGroupId().toString().equals(groupId)&&sysGroupFund.getFundingId().toString().equals(fundId)){
                if(sysGroupFund.getCost()==0){
                    sysGroupFundService.removeById(sysGroupFund.getId());
                    assert sysFunding1 != null;
                    sysFunding1.setRemainAmount(sysFunding1.getRemainAmount()+sysGroupFund.getTotalAmount());
                    sysFunding1.setCost(sysFunding1.getCost()-sysGroupFund.getTotalAmount());
                    sysFundingService.updateById(sysFunding1);
                }
                break;
            }
        }
        sysGroupFundDetailService.remove(new QueryWrapper<SysGroupFundDetail>().eq("group_id",groupId).eq("funding_id",fundId));
        sysGroupFunds= sysGroupFundService.list();
        List<PageGroupFund>result = new ArrayList<>();
        for(SysGroupFund sysGroupFund:sysGroupFunds){
            if(sysGroupFund.getFundingId().toString().equals(fundId)){
                PageGroupFund pageGroupFund = new PageGroupFund();
                if(sysGroupFund.getStatus().equals("complete")){
                    pageGroupFund.setComplete("True");
                }
                else{
                    pageGroupFund.setComplete("False");
                }
                pageGroupFund.setGroup(sysGroupFund.getGroupName());
                pageGroupFund.setTotal(sysGroupFund.getTotalAmount());
                pageGroupFund.setCost(sysGroupFund.getCost());
                pageGroupFund.setLeft(sysGroupFund.getRemainAmount());
                if(sysGroupFund.getTotalAmount()!=0){
                    pageGroupFund.setPercent((int) (pageGroupFund.getCost()*100/sysGroupFund.getTotalAmount()));
                }
                else{
                    pageGroupFund.setPercent(100);
                }
                result.add(pageGroupFund);
            }
        }
        return Result.ok(result);
    }
    @ApiOperation(value = "根据组名和经费id删除经费")
    @PostMapping ("deleteFundGroup")
    public Result deleteFundGroup(@RequestBody Map<String,String> GROUPANDID ){
        String groupName = GROUPANDID.get("groupName");
        String fundId = GROUPANDID.get("fundId");
        List<SysGroupFund> sysGroupFunds = sysGroupFundService.list();
        List<SysFunding>sysFunding= sysFundingService.list();
        SysFunding sysFunding1 = null;
        for(SysFunding sysFunding2:sysFunding){
            if(sysFunding2.getFundingId().toString().equals(fundId)){
                sysFunding1 = sysFunding2;
                break;
            }
        }
        for(SysGroupFund sysGroupFund:sysGroupFunds){
            if(sysGroupFund.getGroupName().equals(groupName)&&sysGroupFund.getFundingId().toString().equals(fundId)){
                if(sysGroupFund.getCost()==0){
                    sysGroupFundService.removeById(sysGroupFund.getId());
                    assert sysFunding1 != null;
                    sysFunding1.setRemainAmount(sysFunding1.getRemainAmount()+sysGroupFund.getTotalAmount());
                    sysFunding1.setCost(sysFunding1.getCost()-sysGroupFund.getTotalAmount());
                    sysFundingService.updateById(sysFunding1);
                }
                break;
            }
        }
        Long groupId = sysGroupService.getOne(new QueryWrapper<SysGroup>().eq("group_name",groupName)).getId();
        sysGroupFundDetailService.remove(new QueryWrapper<SysGroupFundDetail>().eq("group_id",groupId).eq("funding_id",fundId));
        sysGroupFunds= sysGroupFundService.list();
        List<PageGroupFund>result = new ArrayList<>();
        for(SysGroupFund sysGroupFund:sysGroupFunds){
            if(sysGroupFund.getFundingId().toString().equals(fundId)){
                PageGroupFund pageGroupFund = new PageGroupFund();
                if(sysGroupFund.getStatus().equals("complete")){
                    pageGroupFund.setComplete("True");
                }
                else{
                    pageGroupFund.setComplete("False");
                }
                pageGroupFund.setGroup(sysGroupFund.getGroupName());
                pageGroupFund.setTotal(sysGroupFund.getTotalAmount());
                pageGroupFund.setCost(sysGroupFund.getCost());
                pageGroupFund.setLeft(sysGroupFund.getRemainAmount());
                if(sysGroupFund.getTotalAmount()!=0){
                    pageGroupFund.setPercent((int) (pageGroupFund.getCost()*100/sysGroupFund.getTotalAmount()));
                }
                else{
                    pageGroupFund.setPercent(100);
                }
                result.add(pageGroupFund);
            }
        }
        return Result.ok(result);
    }
    @ApiOperation(value = "根据组添加经费")
    @PostMapping ("addGroupsToFund")
    public Result addGroupsToFund(@RequestBody Map<String,Object> GROUPANDID){
        String fundId=GROUPANDID.get("fundId").toString();
        List<String> groupNames = (List<String>) GROUPANDID.get("groups");
        List<SysFunding>sysFundings = sysFundingService.list();
        List<SysGroup>sysGroups = sysGroupService.list();
        String sysFundingname="";
        String sysGroupId="";
        for(SysFunding sysFunding:sysFundings){
            if(sysFunding.getFundingId().toString().equals(fundId)){
                sysFundingname=sysFunding.getFundingName();
                break;
            }
        }

        for(String groupName:groupNames){
            for(SysGroup sysGroup:sysGroups){
                if(sysGroup.getGroupName().equals(groupName)){
                    sysGroupId=sysGroup.getId().toString();
                    break;
                }
            }
            SysGroupFund sysGroupFund = new SysGroupFund();
            sysGroupFund.setFundingId((long) Integer.parseInt(fundId));
            sysGroupFund.setFundingName(sysFundingname);
            sysGroupFund.setGroupId((long) Integer.parseInt(sysGroupId));
            sysGroupFund.setGroupName(groupName);
            sysGroupFund.setTotalAmount(0L);
            sysGroupFund.setCost(0L);
            sysGroupFund.setRemainAmount(0L);
            sysGroupFund.setStatus("complete");
            sysGroupFundService.save(sysGroupFund);
        }
        List<SysGroupFund> sysGroupFunds = sysGroupFundService.list();
        List<PageGroupFund>result = new ArrayList<>();
        for(SysGroupFund sysGroupFund:sysGroupFunds){
            if(sysGroupFund.getFundingId().toString().equals(fundId)){
                PageGroupFund pageGroupFund = new PageGroupFund();
                if(sysGroupFund.getStatus().equals("complete")){
                    pageGroupFund.setComplete("True");
                }
                else{
                    pageGroupFund.setComplete("False");
                }
                pageGroupFund.setGroup(sysGroupFund.getGroupName());
                pageGroupFund.setTotal(sysGroupFund.getTotalAmount());
                pageGroupFund.setCost(sysGroupFund.getCost());
                pageGroupFund.setLeft(sysGroupFund.getRemainAmount());
                if(sysGroupFund.getTotalAmount()!=0){
                    pageGroupFund.setPercent((int) (pageGroupFund.getCost()*100/sysGroupFund.getTotalAmount()));
                }
                else{
                    pageGroupFund.setPercent(100);
                }
                result.add(pageGroupFund);
            }
        }
        return Result.ok(result);
    }
    @ApiOperation(value = "根据经费id和组id获取经费详情")
    @GetMapping ("getFundDetailByGroup")
    public Result getFundInfoByGroupAndFund(@RequestParam(value ="groupId") String groupId,
                                            @RequestParam(value ="fundId") String fundingId) {
        List<SysGroupFundDetail> sysGroupFundDetails = sysGroupFundDetailService.list();
        List<SysGroupFund> sysGroupFunds = sysGroupFundService.list();
        SysGroupFund sysGroupFund = null;
        for(SysGroupFund sysGroupFund1:sysGroupFunds){
            if(sysGroupFund1.getGroupName().equals(groupId)&&sysGroupFund1.getFundingId().toString().equals(fundingId)){
                sysGroupFund = sysGroupFund1;
                break;
            }
        }
        List<Map<String,String>>result = new ArrayList<>();
        for(SysGroupFundDetail sysGroupFundDetail:sysGroupFundDetails){
            assert sysGroupFund != null;
            if(sysGroupFundDetail.getGroupId().equals(sysGroupFund.getGroupId())&&sysGroupFundDetail.getFundingId().toString().equals(fundingId)){
                Map<String,String>map = new HashMap<>();
                map.put("category1",sysGroupFundDetail.getCategory1());
                map.put("category2",sysGroupFundDetail.getCategory2());
                map.put("total",sysGroupFundDetail.getTotalAmount().toString());
                map.put("cost",sysGroupFundDetail.getUsedAmount().toString());
                map.put("left", sysGroupFundDetail.getRemainAmount().toString());
                map.put("new","False");
                result.add(map);
            }
        }
        return Result.ok(result);
    }
    @ApiOperation(value = "根据经费id和组id修改经费详情")
    @PostMapping ("modifyGroupFundDetail")
    public Result modifyGroupFundDetail(@RequestBody Map<String, Object> fundDetail) {
        String groupId = fundDetail.get("groupId").toString();
        String fundingId = fundDetail.get("fundId").toString();
        List<Map<Object,Object>>FUNDDETAIL= (List<Map<Object, Object>>) fundDetail.get("detail");
        System.out.println(FUNDDETAIL);
        List<SysGroupFundDetail> sysGroupFundDetails = sysGroupFundDetailService.list();
        List<SysGroupFundDetail> sysGroupFundDetails1 = new ArrayList<>();

        List<SysGroupFund> sysGroupFunds = sysGroupFundService.list();
        List<SysFunding>SysFundings = sysFundingService.list();
        SysFunding sysFunding = null;
        for(SysFunding sysFunding1:SysFundings){
            if(sysFunding1.getFundingId().toString().equals(fundingId)){
                sysFunding = sysFunding1;
                break;
            }
        }
        SysGroupFund sysGroupFund = null;
        for(SysGroupFund sysGroupFund1:sysGroupFunds){
            if(sysGroupFund1.getGroupName().equals(groupId)&&sysGroupFund1.getFundingId().toString().equals(fundingId)){
                sysGroupFund = sysGroupFund1;
                break;
            }
        }
        for(SysGroupFundDetail sysGroupFundDetail:sysGroupFundDetails){
            assert sysGroupFund != null;
            if(sysGroupFundDetail.getGroupId().equals(sysGroupFund.getGroupId())&&sysGroupFundDetail.getFundingId().toString().equals(fundingId)){
                sysGroupFundDetails1.add(sysGroupFundDetail);
            }
        }
        long sum=0;
        long count=0;
        for(Map<Object,Object>map:FUNDDETAIL){
            String category1 = map.get("category1").toString();
            String category2 = map.get("category2").toString();
            String total = map.get("total").toString();
            String cost = map.get("cost").toString();
            String left = map.get("left").toString();
            String new1 = map.get("new").toString();
            if(new1.equals("true")){
                SysGroupFundDetail sysGroupFundDetail = new SysGroupFundDetail();
                sysGroupFundDetail.setGroupId(sysGroupFund.getGroupId());
                sysGroupFundDetail.setFundingId((long) Integer.parseInt(fundingId));
                sysGroupFundDetail.setCategory1(category1);
                sysGroupFundDetail.setCategory2(category2);
                sysGroupFundDetail.setTotalAmount((long) Integer.parseInt(total));
                sysGroupFundDetail.setUsedAmount(0L);
                sysGroupFundDetail.setRemainAmount((long) Integer.parseInt(total));
                sysGroupFundDetailService.save(sysGroupFundDetail);
                sum=sum+(long) Integer.parseInt(total);
            }
            else{
                SysGroupFundDetail sysGroupFundDetail=sysGroupFundDetails1.get((int) count);
                count++;
                long sum1= Long.parseLong(total);
                sum1=sum1-sysGroupFundDetail.getTotalAmount();
                sum=sum+sum1;
                sysGroupFundDetail.setTotalAmount((long) Integer.parseInt(total));
                sysGroupFundDetail.setRemainAmount((long) Integer.parseInt(total)-sysGroupFundDetail.getUsedAmount());
                sysGroupFundDetail.setCategory1(category1);
                sysGroupFundDetail.setCategory2(category2);
                sysGroupFundDetail.setUsedAmount(Long.valueOf(cost));
                sysGroupFundDetail.setRemainAmount((long) (Integer.parseInt(total)-Integer.parseInt(cost)));
                sysGroupFundDetailService.updateById(sysGroupFundDetail);
            }
        }
        assert sysGroupFund != null;
        sysGroupFund.setTotalAmount(sysGroupFund.getTotalAmount()+sum);
        sysGroupFund.setCost(sysGroupFund.getCost());
        sysGroupFund.setRemainAmount(sysGroupFund.getTotalAmount()-sysGroupFund.getCost());
        sysGroupFundService.updateById(sysGroupFund);
        assert sysFunding != null;
        sysFunding.setCost(sysFunding.getCost()+sum);
        sysFunding.setRemainAmount(sysFunding.getTotalAmount()-sysFunding.getCost());
        sysFundingService.updateById(sysFunding);
        List<Map<String,String>>result = new ArrayList<>();
        sysGroupFundDetails = sysGroupFundDetailService.list();
        for(SysGroupFundDetail sysGroupFundDetail:sysGroupFundDetails){
            if(sysGroupFundDetail.getGroupId().equals(sysGroupFund.getGroupId())&&sysGroupFundDetail.getFundingId().toString().equals(fundingId)){
                Map<String,String>map = new HashMap<>();
                map.put("category1",sysGroupFundDetail.getCategory1());
                map.put("category2",sysGroupFundDetail.getCategory2());
                map.put("total",sysGroupFundDetail.getTotalAmount().toString());
                map.put("cost",sysGroupFundDetail.getUsedAmount().toString());
                map.put("left", String.valueOf(sysGroupFundDetail.getTotalAmount()-sysGroupFundDetail.getUsedAmount()));
                map.put("new","False");
                result.add(map);
            }
        }
        return Result.ok(result);

    }
    @ApiOperation(value = "根据id获取、经费信息")
    @GetMapping("getFundStatistics")
    private Result getFundStatistics(@RequestParam(value = "id") int fundId){
        System.out.println("调用获取经费信息接口+ "+fundId);
        SysFunding fund = sysFundingService.getById(fundId);

        Map<String,Object>result = new HashMap<>();
        result.put("name",fund.getFundingName());
        result.put("totalFund",fund.getTotalAmount().toString());
        result.put("left",fund.getRemainAmount().toString());
        result.put("used",fund.getCost().toString());
        result.put("completeRate", fund.getCost()*100/fund.getTotalAmount());
        if(fund.getStatus().equals("complete")){
            result.put("complete","True");
        }else{
            result.put("complete","False");
        }


        System.out.println("result: "+result);
        return Result.ok(result);
    }





}
