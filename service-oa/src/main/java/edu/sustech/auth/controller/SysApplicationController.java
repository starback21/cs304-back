package edu.sustech.auth.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import edu.sustech.auth.service.*;
import edu.sustech.model.system.*;
import edu.sustech.common.result.Result;
import edu.sustech.re.system.PageApplication;
import edu.sustech.vo.system.SysAppQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

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
    private SysFundAppService fundAppService;
    @Autowired
    private SysGroupFundService groupFundService;
    @Autowired
    private SysGroupFundDetailService groupFundDetailService;
    @Autowired
    private SysMessageService messageService;
    @Autowired
    private SysRemessageService remessageService;



    @ApiOperation(value = "获取申请")
    @GetMapping("/getApplications")
    public Result<Map<String, Object>> getApplications(@RequestParam(value = "page") int page,
                                                       @RequestParam(value = "type",required = false) String type,
                                                       @RequestParam(value = "pageSize",required = false) Long limit,
                                                       SysAppQueryVo sysAppQueryVo){

        List<SysApplication> list;
        //封装条件，判断条件值不为空
        LambdaQueryWrapper<SysApplication> wrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<SysFundApp> fundAppWrapper = new LambdaQueryWrapper<>();
        //获取条件值
        String fundName = sysAppQueryVo.getFundName();
        Long fundId = sysAppQueryVo.getFundId();
        String group = sysAppQueryVo.getGroup();
        String startDate = sysAppQueryVo.getStartDate();
        String endDate = sysAppQueryVo.getEndDate();
        Long startValue = sysAppQueryVo.getStartValue();
        Long endValue = sysAppQueryVo.getEndValue();
        String category = sysAppQueryVo.getCategory();
        String ascend = sysAppQueryVo.getAscend();
        String sortBy = sysAppQueryVo.getSortBy();
        //判断条件值不为空
        //like 模糊查询

        if(!StringUtils.isEmpty(group)) {
            wrapper.like(SysApplication::getGroupName,group);
        }
        //ge 大于等于
        if(!StringUtils.isEmpty(startDate)) {
            wrapper.ge(SysApplication::getCreateTime,startDate);
        }
        //le 小于等于
        if(!StringUtils.isEmpty(endDate)) {
            wrapper.le(SysApplication::getCreateTime,endDate);
        }
        //ge 大于等于
        if(!StringUtils.isEmpty(startValue)) {
            wrapper.ge(SysApplication::getNumber,startValue);
        }
        //le 小于等于
        if(!StringUtils.isEmpty(endValue)) {
            wrapper.le(SysApplication::getNumber,endValue);
        }
        if(!StringUtils.isEmpty(category)) {
            wrapper.like(SysApplication::getCategory1,category);
        }
        //fund
        boolean should_fund = false;
        if(!StringUtils.isEmpty(fundName)) {
            fundAppWrapper.like(SysFundApp::getFundName,fundName);
            should_fund = true;
        }
        if(!StringUtils.isEmpty(fundId)) {
            fundAppWrapper.eq(SysFundApp::getFundId,fundId);
            should_fund = true;
        }
        List<Long> condition = new ArrayList<>();
        if (should_fund){
            List<SysFundApp> list1 = fundAppService.list(fundAppWrapper);
            for (SysFundApp item : list1){
                condition.add(item.getAppId());
            }
            if (list1.size()!=0)
                wrapper.in(SysApplication::getId,condition);
            else {
                Map<String, Object> hashMap = new HashMap<>();
                hashMap.put("data",list1);
                hashMap.put("total",0);
                return Result.ok(hashMap);
            }

        }


        if(!StringUtils.isEmpty(ascend)) {
            if(!StringUtils.isEmpty(sortBy)){
                if (ascend.equals("true"))
                    switch (sortBy){
                        case "group":
                            wrapper.orderByAsc(SysApplication::getGroupName);
                            break;
                        case "value":
                            wrapper.orderByAsc(SysApplication::getNumber);
                            break;
                        case "date":
                            wrapper.orderByAsc(SysApplication::getCreateTime);
                            break;
                        case "category":
                            wrapper.orderByAsc(SysApplication::getCategory1);
                            break;
                    }
                else
                    switch (sortBy){
                        case "group":
                            wrapper.orderByDesc(SysApplication::getGroupName);
                            break;
                        case "value":
                            wrapper.orderByDesc(SysApplication::getNumber);
                            break;
                        case "date":
                            wrapper.orderByDesc(SysApplication::getCreateTime);
                            break;
                        case "category":
                            wrapper.orderByDesc(SysApplication::getCategory1);
                            break;
                    }

            }

        }
        if (type != null){
            switch (type) {
                case "underway":
                    wrapper.eq(SysApplication::getState, "underway");
                    list = service.list(wrapper);
                    break;
                case "reject":
                    wrapper.eq(SysApplication::getState, "reject");
                    list = service.list(wrapper);
                    break;
                case "complete":
                    wrapper.eq(SysApplication::getState, "complete");
                    list = service.list(wrapper);
                    break;
                default:
                    list = service.list(wrapper);
                    break;
            }
        }else list = service.list(wrapper);
        List<PageApplication> data = new ArrayList<>();
        int index = 0;
        if (limit==null){
            limit = 3L;
        }
        for (SysApplication a : list){
            index++;
            if (index > (page - 1) * limit && index <= page * limit){
                PageApplication tmp = new PageApplication();
                tmp.setId(Math.toIntExact(a.getId()));
                tmp.setKey(a.getId().toString());
                tmp.setName(a.getTitle());
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

    @ApiOperation("通过申请")
    @PostMapping("/permitApplication")
    public Result permitApplication(@RequestBody JSONObject jsonParam){
        Long appid = jsonParam.getLong("id");
        UpdateWrapper<SysApplication> wrapper = new UpdateWrapper<>();
        Date date = new Date(System.currentTimeMillis());
        wrapper.eq("id",appid).set("state","complete").set("change_time",date);
        boolean is_success = service.update(wrapper);
        SysApplication app = service.getById(appid);
        Long groupId = app.getGroupId();
//        System.out.println(id);
        SysFundApp fundApp = fundAppService.
                getByAppId(appid);
        SysGroupFund groupFund = groupFundService.
                getByGroupandFund(groupId, fundApp.getFundId());
        SysGroupFundDetail sysGroupFundDetail=groupFundDetailService.
                getByGroupCategory(app.getCategory1(),fundApp.getFundId(), app.getGroupId());
        if(sysGroupFundDetail!=null){
            sysGroupFundDetail.setUsedAmount(sysGroupFundDetail.getUsedAmount()+Long.valueOf(app.getNumber()));
            sysGroupFundDetail.setRemainAmount(sysGroupFundDetail.getTotalAmount()-sysGroupFundDetail.getUsedAmount());
            groupFundDetailService.updateById(sysGroupFundDetail);
        }
        groupFund.setCost(groupFund.getCost() + Long.valueOf(app.getNumber()));
        groupFund.setRemainAmount(groupFund.getTotalAmount() - groupFund.getCost());
        groupFundService.updateById(groupFund);
        //从message中获取userid
        SysMessage sysMessage = messageService.getOne(
                new LambdaQueryWrapper<SysMessage>().eq(SysMessage::getAppId,appid)
        );
        //添加message
        SysRemessage remessage = new SysRemessage();
        remessage.setAppId(appid);
        remessage.setType("permit");
        remessage.setState(0);
        remessage.setGroupId(groupId);
        remessage.setUserId(sysMessage.getUserId());
        remessage.setContent("通过申请");
        remessageService.save(remessage);
        if (is_success)
            return Result.ok();
        else
            return Result.fail();
    }

    @ApiOperation("拒绝申请")
    @PostMapping("/denyApplications")
    public Result denyApplications(@RequestBody JSONObject jsonParam){
        JSONArray data = jsonParam.getJSONArray("ids");
        String js = JSONObject.toJSONString(data, SerializerFeature.WriteClassName);
        List<Long> idList = JSONObject.parseArray(js, Long.class);
        boolean is_success = false;
        UpdateWrapper<SysApplication> wrapper = new UpdateWrapper<>();
        Date date = new Date(System.currentTimeMillis());
        for (Long id : idList){
            wrapper.eq("id",id).set("state","reject").set("change_time",date);
            is_success = service.update(wrapper);

        }
        //从message中获取userid
        SysMessage sysMessage = messageService.getOne(
                new LambdaQueryWrapper<SysMessage>().eq(SysMessage::getAppId,idList.get(0))
        );
        SysApplication app = service.getById(idList.get(0));
        Long groupId = app.getGroupId();
        //添加message
        SysRemessage remessage = new SysRemessage();
        remessage.setAppId(idList.get(0));
        remessage.setType("reject");
        remessage.setState(0);
        remessage.setGroupId(groupId);
        remessage.setUserId(sysMessage.getUserId());
        remessage.setContent("拒绝申请");
        remessageService.save(remessage);
        if (is_success)
            return Result.ok();
        else
            return Result.fail();
    }

    @ApiOperation(value = "获取申请详细信息")
    @GetMapping("/getApplicationInfo")
    public Result<Map<String, Object>> getApplicationInfo(@RequestParam(value = "id") Long id) {
        SysApplication app = service.getById(id);
        Long groupId = app.getGroupId();
        Long fundId = fundAppService.getByAppId(id).getFundId();
        SysGroupFund fund = groupFundService.getByGroupandFund(groupId,fundId);
        Map<String, Object> result = new HashMap<>();
        result.put("applicationId",id);
        result.put("fundId",fundId);
        result.put("fundName",fund.getFundingName());
        result.put("groupName",app.getGroupName());
        result.put("groupTotalFund",fund.getTotalAmount());
        result.put("groupUsedFund",fund.getCost());
        result.put("people","xxx");
        result.put("category1",app.getCategory1());
        result.put("category2",app.getCategory2());
        result.put("useNum",app.getNumber());
        result.put("summary",app.getComment());
        result.put("comment",app.getComment());

        return  Result.ok(result);
    }

    @ApiOperation(value = "获取申请时间线")
    @GetMapping("/getApplicationTimeline")
    public Result<Map<String, Object>> getApplicationTimeline(@RequestParam(value = "id") int fundId) {
        SysApplication app = service.getById(fundId);
        Date date1 = app.getCreateTime();
        Date date2 = app.getChangeTime();
        String state = app.getState();
        Map<String, Object> result = new HashMap<>();
        result.put("date1",date1);
        result.put("date2",date2);
        result.put("state",state);

        return  Result.ok(result);
    }
    @ApiOperation(value="根据经费名获取最近一个月的申请")
    @GetMapping("/getApplicationsByFundId")
    public Result<List<Map<Object,Object>>> getApplicationsByFundName(@RequestParam(value = "fundId") Long fundId){
        List<SysFundApp> fundApps = fundAppService.getByFundId(fundId);
        List<Map<Object,Object>> result = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date oneMonthAgo = cal.getTime();
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        for (Date date = oneMonthAgo; date.before(today); cal.add(Calendar.DATE, 1), date = cal.getTime()) {
            Map<Object,Object> map = new HashMap<>();
            map.put("changeTime", dateFormat.format(date));
            map.put("number", 0);
            result.add(map);
        }
        for(SysFundApp fundApp:fundApps){
            Map<Object,Object> map = new HashMap<>();
            SysApplication application = service.getById(fundApp.getAppId());
            if(application.getState().equals("complete")){
                Date changeTime = application.getChangeTime();
                if(changeTime.after(oneMonthAgo)){
                    String applicationChangeTime = dateFormat.format(changeTime);
                    if(result.size()==0){
                        map.put("changeTime",applicationChangeTime);
                        map.put("number",application.getNumber());
                        result.add(map);
                    }else{
                        boolean isExist = false;
                        for (Map<Object, Object> objectObjectMap : result) {
                            String resultChangeTime = (String) objectObjectMap.get("changeTime");
                            System.out.println(applicationChangeTime);
                            System.out.println(resultChangeTime);
                            if (applicationChangeTime.equals(resultChangeTime)) {
                                objectObjectMap.put("number", Long.parseLong(objectObjectMap.get("number").toString()) + Long.valueOf(application.getNumber()));
                                objectObjectMap.put("changeTime", applicationChangeTime);
                                isExist= true;
                                break;
                            }
                        }
                        if (!isExist) {
                            map.put("changeTime", applicationChangeTime);
                            map.put("number", application.getNumber());
                            result.add(map);
                        }
                    }
                }
            }
        }
        result.sort(new Comparator<Map<Object, Object>>() {
            @Override
            public int compare(Map<Object, Object> o1, Map<Object, Object> o2) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                try {
                    return dateFormat.parse((String) o1.get("changeTime")).compareTo(dateFormat.parse((String) o2.get("changeTime")));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return Result.ok(result);
    }


}

