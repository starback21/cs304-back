package edu.sustech.auth.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.sustech.auth.service.SysUserService;
import edu.sustech.re.system.PageUser;
import edu.sustech.common.result.Result;
import edu.sustech.model.system.SysUser;
import edu.sustech.vo.system.SysUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author starback
 * @since 2023-03-29
 */
@Api(tags = "用户管理接口")
@RestController
//@CrossOrigin
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService service;

    //用户条件分页查询
    @ApiOperation("用户条件分页查询")
    @GetMapping("getUsers")
    public Result<Map<String, Object>> index(@RequestParam(value = "page") Long page,
                             @RequestParam(value = "pageSize") Long limit,
                             SysUserQueryVo sysUserQueryVo
                                        ) {
        //封装条件，判断条件值不为空
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        //获取条件值
        String uid = sysUserQueryVo.getKeyword();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();
        //判断条件值不为空
        //like 模糊查询
        if(!StringUtils.isEmpty(uid)) {
            wrapper.like(SysUser::getUid,uid);
        }
        //ge 大于等于
        if(!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge(SysUser::getCreateTime,createTimeBegin);
        }
        //le 小于等于
        if(!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le(SysUser::getCreateTime,createTimeEnd);
        }

        List<SysUser> userList = service.list(wrapper);
        List<PageUser> users = new ArrayList<>();
        int index = 0;
        for (SysUser user : userList) {
            index ++;
            if (index > (page - 1) * limit && index <= page * limit) {
                PageUser tempUser = new PageUser();
                tempUser.setId(user.getId());
                tempUser.setKey(user.getUid());
                tempUser.setName(user.getName());
                tempUser.setEmail(user.getEmail());
                tempUser.setPhone(user.getPhone());
                users.add(tempUser);
            }
        }
        Map<String, Object> result = new HashMap<>(2);
        result.put("users",users);
        result.put("total",userList.size());
        return Result.ok(result);

    }


    @ApiOperation(value = "根据id获取用户")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        SysUser user = service.getById(id);
        if (user == null){
            return Result.fail();
        }
        return Result.ok(user);
    }
    @ApiOperation("查询所有用户名字")
    @GetMapping("/getAllAccountName")
    public Result<List<String>> getAllAccountName() {
        List<String> nameList = new ArrayList<>();
        List<SysUser> userList = service.list();
        for (SysUser sysUser : userList){
            nameList.add(sysUser.getName());
        }
        return Result.ok(nameList);
    }
    @ApiOperation(value = "添加用户")
    @PostMapping("/createAccount")
    public Result save(@RequestBody SysUser user) {
        user.setPassword("123456");
        boolean is_success = service.save(user);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "更新用户")
    @PutMapping("/update")
    public Result updateById(@RequestBody SysUser user) {
        service.updateById(user);
        return Result.ok();
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        service.removeById(id);
        return Result.ok();
    }
    @ApiOperation(value = "根据id列表删除")
//    @DeleteMapping("/batchRemove")
    @PostMapping("/batchRemove")
    public Result batchRemove(@RequestBody JSONObject jsonParam) {
        JSONArray data = jsonParam.getJSONArray("idList");
        String js = JSONObject.toJSONString(data, SerializerFeature.WriteClassName);
        List<Long> idList = JSONObject.parseArray(js, Long.class);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.in("uid",idList);
        boolean is_success = service.remove(wrapper);
        if (is_success){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
}

