package edu.sustech.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import edu.sustech.auth.service.*;
import edu.sustech.common.utils.MD5;
import edu.sustech.model.system.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest
public class Befor {

    @Autowired
    private SysApplicationService applicationService;
    @Autowired
    private SysFundingService fundingService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysGroupService groupService;
    @Autowired
    private SysUserRoleService roleService;

    @Test
    public void addAdmin(){
        SysUser user = userService.getUserByName("admin");
        if (user == null){
            user = new SysUser();
            user.setName("admin");
            user.setUid(1L);
            String psw = "123456";
            String passwordMd5 = MD5.encrypt(psw);
            user.setPassword(passwordMd5);
            boolean is_success = userService.save(user);
            System.out.println(is_success);
        }
        System.out.println("create admin");
    }
//    @Test
//    public void addFundings() {
//        SysFunding funding = new SysFunding();
//        long base = 510L;
//        for (int i = 0; i < 5; i++) {
//            funding.setFundingId(base++);
//            funding.setFundingName("test"+base);
//            funding.setCost(100L);
//            funding.setGroupId(15L);
//            funding.setGroupName("电子");
//            funding.setTotalAmount(10000L);
//            funding.setRemainAmount(9900L);
//            fundingService.save(funding);
//        }
//    }
    @Test
    public void addUsers(){
        SysUser user = new SysUser();
        long base = 100000L;
        for (int i = 0; i < 10; i++) {
            user.setName("test_"+i);
            user.setUid(base+i);
            String psw = "123456";
            String passwordMd5 = MD5.encrypt(psw);
            user.setPassword(passwordMd5);
            user.setPhone("13888777666");
            user.setEmail("163@qq.com");
            userService.save(user);
        }
    }

    @Test
    public void addApplications(){
        SysApplication application = new SysApplication();
        application.setGroupId(3L);
        application.setGroupName("计算机");
        application.setNumber(1000);
        application.setCategory1("eat");
        application.setState("underway");
        application.setTitle("吃喝");
        applicationService.save(application);
    }

    @Test
    public void addFunding(){
        SysApplication application = new SysApplication();
        application.setGroupId(3L);
        application.setGroupName("计算机");
        application.setNumber(2000);
        application.setCategory1("eat");
        application.setState("underway");
        application.setTitle("吃喝");
        applicationService.save(application);
    }

    @Test
    public void changeApplications(){
        SysApplication application = applicationService.getById(1);
        application.setState("complete");
        Date date = new Date(System.currentTimeMillis());
        application.setChangeTime(date);
        UpdateWrapper<SysApplication> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",1).set("state","complete").set("change_time",date);
        applicationService.update(wrapper);

    }
    //添加经费
    @Test
    public void addFundings(){
        SysFunding funding = new SysFunding();
        for(int i = 0;i<10;i++){
            funding.setFundingName("fund"+i);
            funding.setCost(0L);
            funding.setTotalAmount(10000L);
            funding.setRemainAmount(10000L);
            fundingService.save(funding);
        }
    }
    //添加组
    @Test
    public void addGroup(){
        SysGroup group = new SysGroup();
        for (int i = 0;i<10;i++){
            group.setGroupName("group"+i);
            groupService.save(group);
        }
    }

    @Test
    public void test1() throws ParseException {

        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        String day = "2023-1-1";
        Date targerDay = ft.parse(day);
        //计算今天到1-1的日期差
        long targetTime = targerDay.getTime();
        long todaytime = new Date().getTime();
        long time =Math.abs(todaytime - targetTime);
        //转换格式
        long cntday = time/1000/60/60/24;
        //打印今天
        String today = ft.format(new Date());
        System.out.println("今天是"+today);
        System.out.println("距离"+day+"有"+cntday+"天");
        //计算cnt之前的日期
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 0);
        Date date = c.getTime();
        System.out.println(1+"天前是"+ft.format(date));
    }


}
